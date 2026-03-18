import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class CalcolatriceTest {

    @Test
    void testSomma() {
        int risultato = Calcolatrice.somma(2, 3);
        assertEquals(5, risultato);
    }

    @Test
    void testJavaDoc() {
        int risultato = Calcolatrice.moltiplicazione(7, 3);
        assertEquals(21, risultato);
    }

}