public class Main {
    public static String capitalize(String s) {
        String cap = "";
        cap += Character.toUpperCase(s.charAt(0));

        for (int i = 1; i < s.length(); i++) {
            if (s.charAt(i - 1) == ' ') {
                cap += Character.toUpperCase(s.charAt(i));
            }
            else {
                cap += s.charAt(i);
            }
        }
        return cap;
    }

    public static void main(String[] args) {
        String s = capitalize("questo è il mio primo compito di java");
        System.out.println(s);
    }
}