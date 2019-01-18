package drawingeditor.model;

/**
 * Événement correspondant au déplacement d'une forme.
 */
public class EventFormeMove extends EventForme {

    private final double tx;
    private final double ty;

    /**
     * Sauvegarde le déplacement en X et Y de la forme.
     * @param tx Déplacement X.
     * @param ty Déplacement Y.
     * @see EventForme#EventForme(Forme)
     */
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
