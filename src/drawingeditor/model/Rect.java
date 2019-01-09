package drawingeditor.model;

import javafx.scene.paint.Paint;

public class Rect extends FormeImpl {

    public Rect(double x, double y, double w, double h, Paint color){
        super(x,y,w,h,color);
    }

    public String toString(){
        String str = "Rectangle: x="+this.getPositionX();
        str += " ; y="+this.getPositionY();
        str += " ; w="+this.getWidth();
        str += " ; h="+this.getHeight();
        return str;
    }

}
