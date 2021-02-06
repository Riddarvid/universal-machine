public class UniversalMachine {
    private final Memory memory;
    private final RegisterManager registers;
    private int pc;
    private boolean halted;

    public UniversalMachine(Memory memory, RegisterManager registers) {
        this.memory = memory;
        this.registers = registers;
        pc = 0;
        halted = false;
    }

    private int getBits(int input, int mostMeaningful, int leastMeaningful) {
        long bits = Long.remainderUnsigned(input, (long)(Math.pow(2, 32 - mostMeaningful)));
        bits = Long.divideUnsigned(bits, (long)(Math.pow(2, 32 - leastMeaningful)));
        return (int)bits;
    }

    public void run() {
        while (!halted) {
            int instruction = memory.getInstruction(pc);
            pc++;
            int opcode = getBits(instruction, 0, 4);
            if (opcode < 13) {
                executeNormalInstruction(opcode, instruction);
            } else {
                executeSpecialInstruction(opcode, instruction);
            }
        }
    }

    private void executeSpecialInstruction(int opcode, int instruction) {
        int regA = getBits(instruction, 4, 7);
        int value = getBits(instruction, 7, 32);
        switch (opcode) {
            case 13 -> orthography(regA, value);
        }
    }

    private void orthography(int regA, int value) {
        registers.set(regA, value);
    }

    private void executeNormalInstruction(int opcode, int instruction) {
        int regA = getBits(instruction, 23, 26);
        int regB = getBits(instruction, 26, 29);
        int regC = getBits(instruction, 29, 32);
        switch (opcode) {
            case 0 -> moveIf(regA, regB, regC);
            case 1 -> arrayIndex(regA, regB, regC);
            case 2 -> amendArray(regA, regB, regC);
            case 3 -> add(regA, regB, regC);
            case 4 -> mul(regA, regB, regC);
            case 5 -> div(regA, regB, regC);
            case 6 -> notAnd(regA, regB, regC);
            case 7 -> halt();
            case 8 -> allocate(regB, regC);
            case 9 -> abandon(regC);
            case 10 -> output(regC);
            case 11 -> input(regC);
            case 12 -> loadProgram(regB, regC);
        }
    }

    private void loadProgram(int regB, int regC) {
        int newProgram = registers.get(regB);
        memory.loadProgram(newProgram);
        pc = registers.get(regC);
    }

    private void output(int regC) {
        System.out.print((char)registers.get(regC));
    }

    private void input(int regC) {
        //TODO
    }

    private void abandon(int regC) {
        int index = registers.get(regC);
        memory.abandon(index);
    }

    private void allocate(int regB, int regC) {
        int n = registers.get(regC);
        int id = memory.allocate(n);
        registers.set(regB, id);
    }

    private void halt() {
        halted = true;
    }

    private void notAnd(int regA, int regB, int regC) {
        int result = registers.get(regB) & registers.get(regC);
        result = ~result;
        registers.set(regA, result);
    }

    private void div(int regA, int regB, int regC) {
        int result = Integer.divideUnsigned(registers.get(regB), registers.get(regC));
        registers.set(regA, result);
    }

    private void mul(int regA, int regB, int regC) {
        int product = registers.get(regB) * registers.get(regC);
        registers.set(regA, product);
    }

    private void add(int regA, int regB, int regC) {
        long sum = (long)registers.get(regB) + (long)registers.get(regC);
        sum = Long.remainderUnsigned(sum, (long)Math.pow(2, 32));
        registers.set(regA, (int)sum);
    }

    private void amendArray(int regA, int regB, int regC) {
        int arrayIndex = registers.get(regA);
        int offset = registers.get(regB);
        int value = registers.get(regC);
        memory.set(arrayIndex, offset, value);
    }

    private void moveIf(int regA, int regB, int regC) {
        int condition = registers.get(regC);
        if (condition != 0) {
            int value = registers.get(regB);
            registers.set(regA, value);
        }
    }

    private void arrayIndex(int regA, int regB, int regC) {
        int arrayIndex = registers.get(regB);
        int offset = registers.get(regC);
        int value = memory.get(arrayIndex, offset);
        registers.set(regA, value);
    }
}
