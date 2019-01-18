package drawingeditor.model;

/**
 * Événement correspondant à la suppression d'une forme.
 */
public class EventFormeDelete extends EventForme {

    /**
     * @see EventForme#EventForme(Forme)
     */
    public EventFormeDelete(Forme removedForme){
        super(removedForme);
    }

    @Override
    public void rollback(Dessin model) {
        model.ajouterForme(this.forme);
    }

    @Override
    public void rollforward(Dessin model) {
        model.supprimerForme(this.forme);
    }

}
