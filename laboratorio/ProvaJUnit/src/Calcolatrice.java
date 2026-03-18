public class Calcolatrice {
    public static int somma(int a, int b) {
        return a + b;
    }

    public static int differenza(int a, int b) {
        return a - b;
    }

    public static int moltiplicazione(int a, int b) {
        return a * b;
    }

    public static float divisione(int a, int b) {
        if (b == 0) throw new ArithmeticException("Non si può dividere per 0");
        return Math.round(((float) a / b) * 100) / 100.0f;
    }

    public static int potenza(int a, int b) {
        return (int) Math.pow(a, b);
    }

    public static float radiceQuadrata(int a) {
        return (float) Math.round(Math.sqrt(a) * 100) / 100;
    }

    public static float somma(float a, float b) {
        return a + b;
    }

    public static float differenza(float a, float b) {
        return a - b;
    }

    public static float moltiplicazione(float a, float b) {
        return Math.round(a * b * 100) / 100f;
    }

    public static float divisione(float a, float b) {
        if (b == 0) throw new ArithmeticException("Non si può dividere per 0");
        return Math.round((a / b) * 100) / 100.0f;
    }

    public static float potenza(float a, float b) {
        return (float) Math.pow(a, b);
    }

    public static float radiceQuadrata(float a) {
        return (float) Math.round(Math.sqrt(a) * 100) / 100;
    }
}
