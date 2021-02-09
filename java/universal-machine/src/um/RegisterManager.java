package um;

public class RegisterManager {
    private final int[] registers = new int[8];

    public int get(int index) {
        return registers[index];
    }

    public void set(int index, int value) {
        registers[index] = value;
    }
}
