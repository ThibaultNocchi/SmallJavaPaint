package drawingeditor.model;

public class EventFormeAdd extends EventForme {

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
