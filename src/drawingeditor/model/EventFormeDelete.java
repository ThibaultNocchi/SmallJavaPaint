package drawingeditor.model;

public class EventFormeDelete extends EventForme {

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
