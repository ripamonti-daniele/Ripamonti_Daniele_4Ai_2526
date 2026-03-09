import java.io.Serial;
import java.io.Serializable;

public class Albero implements Serializable {

    private Nodo radice;

    @Serial
    private final static long serialVersionUID = 1L;

    public Albero() {

        //foglie - animali
        Nodo cane = new Nodo("Cane", getClass().getResource("cane.jpg"));
        Nodo gatto = new Nodo("Gatto", getClass().getResource("gatto.jpg"));
        Nodo cavallo = new Nodo("Cavallo", getClass().getResource("cavallo.jpg"));
        Nodo maiale = new Nodo("Maiale", getClass().getResource("maiale.jpg"));
        Nodo mucca = new Nodo("Mucca", getClass().getResource("mucca.jpg"));
        Nodo capra = new Nodo("Capra", getClass().getResource("capra.jpg"));
        Nodo pecora = new Nodo("Pecora", getClass().getResource("pecora.jpg"));
        Nodo elefante = new Nodo("Elefante", getClass().getResource("elefante.jpg"));
        Nodo giraffa = new Nodo("Giraffa", getClass().getResource("giraffa.jpg"));
        Nodo lupo = new Nodo("Lupo", null);
        Nodo leone = new Nodo("Leone", getClass().getResource("leone.jpg"));
        Nodo tigre = new Nodo("Tigre", getClass().getResource("tigre.jpg"));
        Nodo aquila = new Nodo("Aquila", null);
        Nodo falco = new Nodo("Falco", null);
        Nodo gufo = new Nodo("Gufo", getClass().getResource("gufo.jpg"));
        Nodo pinguino = new Nodo("Pinguino", getClass().getResource("pinguino.jpg"));
        Nodo struzzo = new Nodo("Struzzo", getClass().getResource("struzzo.jpg"));
        Nodo salmone = new Nodo("Salmone", getClass().getResource("salmone.jpg"));
        Nodo tonno = new Nodo("Tonno", getClass().getResource("tonno.jpg"));
        Nodo squalo = new Nodo("Squalo", getClass().getResource("squalo.jpg"));
        Nodo serpente = new Nodo("Serpente", null);
        Nodo coccodrillo = new Nodo("Coccodrillo", getClass().getResource("coccodrillo.jpg"));
        Nodo tartaruga = new Nodo("Tartaruga", getClass().getResource("tartaruga.jpg"));
        Nodo rana = new Nodo("Rana", null);
        Nodo farfalla = new Nodo("Farfalla", null);
        Nodo ape = new Nodo("Ape", null);

        //radice
        Nodo radice = new Nodo("È un mammifero?");

        //--------------------------------- MAMMIFERO ------------------------------
        Nodo mammiferoDomestico = new Nodo("E' un mammifero domestico?");
        radice.si = mammiferoDomestico;

        //mammifero domestico casa
        Nodo mammiferoDomesticoCasa = new Nodo("Vive in casa?");
        mammiferoDomestico.si = mammiferoDomesticoCasa;

        //mammifero domestico (cane / gatto)
        Nodo mammiferoDomesticoCasaAbbaia = new Nodo("Abbaia?");
        mammiferoDomesticoCasa.si = mammiferoDomesticoCasaAbbaia;
        mammiferoDomesticoCasaAbbaia.si = cane; //CANE
        mammiferoDomesticoCasaAbbaia.no = gatto; //GATTO

        //mammifero domestico (cavallo, mucca, pecora, capra, maiale
        Nodo mammiferoDomesticoEquitazione = new Nodo("E' impiegato nelle corse?");
        mammiferoDomesticoCasa.no = mammiferoDomesticoEquitazione;
        mammiferoDomesticoEquitazione.si = cavallo; //CAVALLO

        Nodo mammiferoDomesticoLatte = new Nodo("Produce latte?");
        mammiferoDomesticoEquitazione.no = mammiferoDomesticoLatte;
        mammiferoDomesticoLatte.no = maiale; //MAIALE

        Nodo mammiferoDomesticoLana = new Nodo("E' impiegato nella produzione di lana?");
        mammiferoDomesticoLatte.si = mammiferoDomesticoLana; //MAIALE
        mammiferoDomesticoLana.no = mucca;  //MUCCA

        Nodo mammiferoDomesticoBela = new Nodo("Bela?");
        mammiferoDomesticoLana.si = mammiferoDomesticoBela;
        mammiferoDomesticoBela.si = pecora; //PECORA
        mammiferoDomesticoBela.no = capra;  //CAPRA

        //Mammifero non domestico (elefante / giraffa / Leone / Tigre / Lupo)
        Nodo mammiferoNonDomesticoGrande = new Nodo("Ha le zanne?");
        mammiferoDomestico.no = mammiferoNonDomesticoGrande;
        mammiferoNonDomesticoGrande.si = elefante;  //ELEFANTE

        Nodo mammiferoNonDomesticoColloLungo = new Nodo("Ha il collo lungo?");
        mammiferoNonDomesticoGrande.no = mammiferoNonDomesticoColloLungo;
        mammiferoNonDomesticoColloLungo.si = giraffa;  //GIRAFFA

        Nodo mammiferoNonDomesticoUlula = new Nodo("Ulula alla luna?");
        mammiferoNonDomesticoUlula.si = lupo;  //LUPO
        mammiferoNonDomesticoColloLungo.no = mammiferoNonDomesticoUlula;

        Nodo mammiferoNonDomesticoReSavana = new Nodo("E' noto come re della Savana?");
        mammiferoNonDomesticoReSavana.si = leone;  //LEONE
        mammiferoNonDomesticoReSavana.no = tigre;  //TIGRE
        mammiferoNonDomesticoUlula.no = mammiferoNonDomesticoReSavana;

        // -------------------------------- UCCELLI -----------------------------
        Nodo uccello = new Nodo("È un uccello?");
        radice.no = uccello;

        Nodo uccelloPuoVolare = new Nodo("Può volare?");
        uccello.si = uccelloPuoVolare;

        // uccello che può volare (aquila, falco, gufo) 
        Nodo uccelloPuoVolareNotturno = new Nodo("E' un uccello notturno?");
        uccelloPuoVolare.si = uccelloPuoVolareNotturno;
        uccelloPuoVolareNotturno.si = gufo;     //GUFO

        Nodo uccelloPuoVolareBandiera = new Nodo("E' associato in qualche modo agli Stati Uniti?");
        uccelloPuoVolareNotturno.no = uccelloPuoVolareBandiera;
        uccelloPuoVolareBandiera.si = aquila;   //AQUILA
        uccelloPuoVolareBandiera.no = falco;    //FALCO
        
        // uccello che non può volare (pinguino, struzzo) 
        Nodo uccelloTestaNascosta = new Nodo("E' noto per nascondere la testa sotto terra?");
        uccelloPuoVolare.no = uccelloTestaNascosta;
        uccelloTestaNascosta.si = struzzo;      //STRUZZO
        uccelloTestaNascosta.no = pinguino;     //PINGUINO

        // ------------------------------ ACQUATICI ---------------------------
        Nodo acquatico = new Nodo("Vive in acqua?");
        uccello.no = acquatico;

        Nodo acquaticoControCorrente = new Nodo("Risale la corrente?");
        acquatico.si = acquaticoControCorrente;
        acquaticoControCorrente.si = salmone;   //SALMONE

        Nodo acquaticoPredatore = new Nodo("Gli anno dedicato una serie di film");
        acquaticoControCorrente.no = acquaticoPredatore;
        acquaticoPredatore.si = squalo;     //SQUALO
        acquaticoPredatore.no = tonno;      //TONNO

        Nodo rettile = new Nodo("E' un rettile?");
        acquatico.no = rettile;

        Nodo rettileStrisciante = new Nodo("Striscia?");
        rettile.si = rettileStrisciante;
        rettileStrisciante.si = serpente;   //SERPENTE

        Nodo rettileGuscio = new Nodo("Indossa un guscio?");
        rettileStrisciante.no = rettileGuscio;
        rettileGuscio.si = tartaruga;   //TARTARUGA
        rettileGuscio.no = coccodrillo; //COCCODRILLO
        
        // --------------------------------- ALTRO -------------------------------
        Nodo altroVolante = new Nodo("Può volare?");
        rettile.no = altroVolante;
        altroVolante.no = rana;     //RANA

        Nodo altroPunge = new Nodo("Può pungere?");
        altroVolante.si = altroPunge;
        altroPunge.si = ape;        //APE
        altroPunge.no = farfalla;   //FARFALLA

        this.radice = radice;
    }

    public Nodo getRadice(){
        //attenzione, non restituisce una copia, ma il riferimento diretto!
        return radice;
    }


}