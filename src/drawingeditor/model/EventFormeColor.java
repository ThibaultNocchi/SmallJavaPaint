package drawingeditor.model;

import javafx.scene.paint.Paint;

/**
 * Événement correspondant à la modification de la couleur d'une forme.
 */
public class EventFormeColor extends EventForme {

    private final Paint before;
    private final Paint after;

    /**
     * Sauvegarde la couleur d'avant et après la modification.
     * @see EventForme#EventForme(Forme)
     * @param before Couleur avant la modification.
     * @param after Nouvelle couleur.
     */
    public EventFormeColor(Forme forme, Paint before, Paint after){
        super(forme);
        this.before = before;
        this.after = after;
    }

    @Override
    public void rollback(Dessin model) {
        this.forme.setCouleur(this.before);
    }

    @Override
    public void rollforward(Dessin model) {
        this.forme.setCouleur(this.after);
    }
}
