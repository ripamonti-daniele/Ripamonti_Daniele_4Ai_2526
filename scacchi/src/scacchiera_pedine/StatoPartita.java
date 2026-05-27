package scacchiera_pedine;

/**
 * Rappresenta i possibili stati di una partita a scacchi.
 * <p>
 * Questo enumerato viene utilizzato per tenere traccia dell'evoluzione
 * della partita, dalla fase attiva fino alle varie condizioni di termine,
 * incluse vittorie, pareggi e situazioni speciali.
 * </p>
 */
public enum StatoPartita {

    /**
     * La partita è attualmente in corso e nessuna condizione di termine è stata raggiunta.
     */
    IN_CORSO,

    /**
     * La partita è terminata con la vittoria del giocatore con i pezzi bianchi
     * (scacco matto al re nero).
     */
    VITTORIA_BIANCO,

    /**
     * La partita è terminata con la vittoria del giocatore con i pezzi neri
     * (scacco matto al re bianco).
     */
    VITTORIA_NERO,

    /**
     * La partita è terminata in pareggio per stallo: il giocatore di turno
     * non ha mosse legali disponibili ma il proprio re non è sotto scacco.
     */
    STALLO,

    /**
     * La partita è terminata in pareggio per materiale insufficiente:
     * nessuno dei due giocatori dispone di abbastanza pezzi per dare scacco matto
     * (ad esempio re contro re, o re e alfiere contro re).
     */
    MATERIALE_INSUFFICIENTE,

    /**
     * La partita è terminata in pareggio per ripetizione di posizione:
     * la stessa posizione sulla scacchiera si è verificata cinque volte
     * con lo stesso giocatore di turno (regola della triplice ripetizione).
     */
    PAREGGIO_RIPETIZIONI,

    /**
     * La partita è terminata in pareggio per la regola delle settantacinque mosse:
     * sono state effettuate settantacinque mosse consecutive senza catture
     * né movimenti di pedoni (mosse neutre).
     */
    PAREGGIO_MOSSE_NEUTRE,

    /**
     * La partita è temporaneamente sospesa in attesa che il giocatore scelga
     * il pezzo in cui promuovere un pedone arrivato all'ultima traversa.
     */
    PROMOZIONE_IN_SOSPESO
}