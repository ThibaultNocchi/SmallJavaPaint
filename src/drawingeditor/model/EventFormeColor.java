package drawingeditor.model;

import javafx.scene.paint.Paint;

public class EventFormeColor extends EventForme {

    private Paint before, after;

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
