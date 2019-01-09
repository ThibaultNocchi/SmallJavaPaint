package drawingeditor.model;

public class EventFormeMove implements EventForme {

    private double tx;
    private double ty;
    private Forme forme;

    public EventFormeMove(double tx, double ty, Forme forme){
        this.tx = tx;
        this.ty = ty;
        this.forme = forme;
    }

    @Override
    public void rollback(Dessin model) {
        this.forme.deplacer(-(this.tx), -(this.ty));
    }

    @Override
    public void rollforward(Dessin model) {
        this.forme.deplacer(this.tx, this.ty);
    }

}
