package drawingeditor.model;

/**
 * Événement correspondant au changement de taille d'une forme.
 */
public class EventFormeResize extends EventForme {

    private double rx;
    private double ry;

    /**
     * Sauvegarde le changement de taille en X et Y.
     * @param rx Changement X.
     * @param ry Changement Y.
     * @see EventForme#EventForme(Forme) 
     */
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
