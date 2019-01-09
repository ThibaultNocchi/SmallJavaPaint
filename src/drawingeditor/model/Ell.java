package drawingeditor.model;

import javafx.scene.paint.Paint;

public class Ell extends FormeImpl {
    
    public Ell(double x, double y, double w, double h, Paint color) {
        super(x, y, w, h, color);
    }

    public String toString(){
        String str = "Ellipse: x="+this.getPositionX();
        str += " ; y="+this.getPositionY();
        str += " ; w="+this.getWidth();
        str += " ; h="+this.getHeight();
        return str;
    }

}
