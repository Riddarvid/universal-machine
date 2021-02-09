package um;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        new Main().initialize();
    }

    private void initialize() {
        Memory memory = initializeMemory();
        RegisterManager registers = new RegisterManager();
        UniversalMachine um = new UniversalMachine(memory, registers);
        um.run();
    }

    private Memory initializeMemory() {
        try {
            byte[] bytes = Files.readAllBytes(Path.of(getClass().getResource("codex.umz").getPath()));
            Integer[] program = new Integer[bytes.length / 4];
            for (int i = 0; i < bytes.length; i += 4) {
                int value = 0;
                for (int j = 0; j < 4; j++) {
                    value *= 256;
                    value += ((bytes[i + j] + 256) % 256);
                }
                program[i / 4] = value;
            }
            return new Memory(program);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
