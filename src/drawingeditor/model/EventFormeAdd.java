package drawingeditor.model;

public class EventFormeAdd implements EventForme {

    Forme forme;

    public EventFormeAdd(Forme addedForme){
        this.forme = addedForme;
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
