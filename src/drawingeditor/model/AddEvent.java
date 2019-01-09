package drawingeditor.model;

public class AddEvent implements FormeEvent {

    Forme forme;

    public AddEvent(Forme addedForme){
        this.forme = addedForme;
    }

    @Override
    public void rollback(Dessin model) {
        model.supprimerForme(this.forme);
    }

}
