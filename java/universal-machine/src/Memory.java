import java.util.*;

public class Memory {
    private final Map<Integer, Integer[]> arrays;
    private final Queue<Integer> freeIndexes;

    public Memory(Integer[] program) {
        arrays = new HashMap<>();
        arrays.put(0, program);
        freeIndexes = new PriorityQueue<>();
    }

    public int getInstruction(int pc) {
        return arrays.get(0)[pc];
    }

    public int get(int arrayIndex, int offset) {
        return arrays.get(arrayIndex)[offset];
    }

    public void set(int arrayIndex, int offset, int value) {
        arrays.get(arrayIndex)[offset] = value;
    }

    public int allocate(int n) {
        int index;
        if (freeIndexes.isEmpty()) {
            index = arrays.size();
        } else {
            index = freeIndexes.poll();
        }
        Integer[] array = new Integer[n];
        for (int i = 0; i < n; i++) {
            array[i] = 0;
        }
        arrays.put(index, array);
        return index;
    }

    public void abandon(int index) {
        arrays.remove(index);
        freeIndexes.add(index);
    }

    public void loadProgram(int newProgram) {
        arrays.put(0, Arrays.copyOf(arrays.get(newProgram), arrays.get(newProgram).length));
    }
}
