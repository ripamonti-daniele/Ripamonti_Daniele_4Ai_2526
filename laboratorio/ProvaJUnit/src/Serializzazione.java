import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.List;

public class Serializzazione {
    public static void serializzaConti(List<ContoCorrente> c) throws Exception {
        try {
            FileOutputStream f = new FileOutputStream("contiCorrenti.ser");
            ObjectOutputStream output = new ObjectOutputStream(f);
            output.writeObject(c);
            output.close();
            f.close();
        }
        catch (Exception e) {
            throw new Exception("Serializzazione non riuscita");
        }
    }

    @SuppressWarnings("unchecked")
    public static List<ContoCorrente> deSerializzaConti() throws Exception {
        List<ContoCorrente> c;
        try {
            FileInputStream f = new FileInputStream("contiCorrenti.ser");
            ObjectInputStream input = new ObjectInputStream(f);
            c = (List<ContoCorrente>) input.readObject();
            input.close();
            f.close();
            return c;
        }
        catch (Exception e) {
            throw new Exception("Serializzazione non riuscita");
        }
    }
}
