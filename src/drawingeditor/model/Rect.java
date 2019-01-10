package drawingeditor.model;

import javafx.scene.paint.Paint;

public class Rect extends FormeImpl {

    public Rect(double x, double y, double w, double h, Paint color){
        super(x,y,w,h,color);
    }

    public Rect(String csv){ super(csv); }

    public String toString(){
        String str = "Rectangle: x="+this.getPositionX();
        str += " ; y="+this.getPositionY();
        str += " ; w="+this.getWidth();
        str += " ; h="+this.getHeight();
        return str;
    }

    public String toCsv(){
        String str = "";
        str += "Rect,"+this.getPositionX()+","+this.getPositionY()+","+this.getWidth()+","+this.getHeight()+","+this.getCouleur().toString();
        return str;
    }

}
