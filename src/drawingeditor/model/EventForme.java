package drawingeditor.model;

/**
 * Classe abstraite gérant un événement d'historique.
 */
public abstract class EventForme {

    protected final Forme forme;
    private Integer history;

    /**
     * Initialise en sauvegardant la forme liée à l'événement et en lui attribuant un numéro d'historique nul.
     * @param forme
     */
    public EventForme(Forme forme){
        this.forme = forme;
        this.history = null;
    }

    /**
     * Retourne la forme liée.
     * @return
     */
    public Forme getForme(){
        return this.forme;
    }

    /**
     * Retourne le numéro d'historique.
     * @return
     */
    public Integer getHistory(){
        return this.history;
    }

    /**
     * Modifie le numéro d'historique.
     * @param history
     */
    public void setHistory(Integer history){
        this.history = history;
    }

    /**
     * Effectue l'opération annuler de l'événement.
     * @param model Dessin sur lequel effectuer le rollback.
     */
    public abstract void rollback(Dessin model);

    /**
     * Effectue l'opération de retour en avant de l'événement.
     * @param model Dessin sur lequel effectuer le rollforward.
     */
    public abstract void rollforward(Dessin model);

}
