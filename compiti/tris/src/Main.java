import java.util.Arrays;

public class Main {
    public static void main(String[] args) {
        char[][] griglia = new char[3][3];
        for (char[] chars : griglia) {
            Arrays.fill(chars, 'a');
        }
    }
}