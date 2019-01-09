package drawingeditor.model;

public class EventFormeDelete implements EventForme {

    Forme forme;

    public EventFormeDelete(Forme removedForme){
        this.forme = removedForme;
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
