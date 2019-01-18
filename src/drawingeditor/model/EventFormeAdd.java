package drawingeditor.model;

/**
 * Événement correspondant à l'ajout d'une forme.
 */
public class EventFormeAdd extends EventForme {

    /**
     * @see EventForme#EventForme(Forme)
     */
    public EventFormeAdd(Forme addedForme){
        super(addedForme);
    }

    @Override
    public void rollback(Dessin model) {
        model.supprimerForme(this.forme);
    }

    @Override
    public void rollforward(Dessin model) {
        model.ajouterForme(this.forme);
    }

}
