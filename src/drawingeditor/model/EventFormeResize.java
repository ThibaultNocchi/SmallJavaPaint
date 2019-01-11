package drawingeditor.model;

public class EventFormeResize extends EventForme {

    private double rx;
    private double ry;

    public EventFormeResize(Forme forme, double rx, double ry){
        super(forme);
        this.rx = rx;
        this.ry = ry;
    }

    @Override
    public void rollback(Dessin model) {
        this.forme.redimensionner(-(this.rx), -(this.ry));
    }

    @Override
    public void rollforward(Dessin model) {
        this.forme.redimensionner(this.rx, this.ry);
    }
}
