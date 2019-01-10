package drawingeditor.model;

import javafx.scene.paint.Paint;

public class Ell extends FormeImpl {
    
    public Ell(double x, double y, double w, double h, Paint color) {
        super(x, y, w, h, color);
    }

    public Ell(String csv){ super(csv); }

    public String toString(){
        String str = "Ellipse: x="+this.getPositionX();
        str += " ; y="+this.getPositionY();
        str += " ; w="+this.getWidth();
        str += " ; h="+this.getHeight();
        return str;
    }

    @Override
    public String toCsv() {
        String str = "";
        str += "Ell,"+this.getPositionX()+","+this.getPositionY()+","+this.getWidth()+","+this.getHeight()+","+this.getCouleur().toString();
        return str;
    }
}
