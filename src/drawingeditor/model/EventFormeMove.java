package drawingeditor.model;

public class EventFormeMove extends EventForme {

    private double tx;
    private double ty;

    public EventFormeMove(double tx, double ty, Forme forme){
        super(forme);
        this.tx = tx;
        this.ty = ty;
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
