package drawingeditor.model;

public class MoveEvent implements FormeEvent {

    private double tx;
    private double ty;
    private Forme forme;

    public MoveEvent(double tx, double ty, Forme forme){
        this.tx = tx;
        this.ty = ty;
        this.forme = forme;
    }

    @Override
    public void rollback(Dessin model) {
        this.forme.deplacer(-(this.tx), -(this.ty));
    }

}
