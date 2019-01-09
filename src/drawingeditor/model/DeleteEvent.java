package drawingeditor.model;

public class DeleteEvent implements FormeEvent {

    Forme forme;

    public DeleteEvent(Forme removedForme){
        this.forme = removedForme;
    }

    @Override
    public void rollback(Dessin model) {
        model.ajouterForme(this.forme);
    }

}
