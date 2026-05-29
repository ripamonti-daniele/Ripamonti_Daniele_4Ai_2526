/**
 * Interfaccia funzionale per la gestione degli eventi di click su una casella della scacchiera.
 * <p>
 * Implementata da chi necessita di ricevere notifica quando l'utente clicca
 * su una casella, seguendo il pattern Observer. Il metodo {@link #casellaCliccata()}
 * viene invocato dal componente grafico della casella al momento del click.
 * </p>
 */
public interface CasellaListener {

    /**
     * Invocato quando l'utente clicca su una casella della scacchiera.
     */
    void casellaCliccata();
}