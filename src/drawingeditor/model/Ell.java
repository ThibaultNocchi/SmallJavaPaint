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
    public double isOutTop() {
        if(this.getPositionY() - this.getHeight()/2 < 0) return -(this.getPositionY() - this.getHeight()/2);
        else return 0;
    }

    @Override
    public double isOutBottom(double h) {
        if(this.getPositionY() + this.getHeight()/2 > h) return this.getPositionY() + this.getHeight()/2 - h;
        else return 0;
    }

    @Override
    public double isOutLeft() {
        if(this.getPositionX() - this.getWidth()/2 < 0) return -(this.getPositionX() - this.getWidth()/2);
        else return 0;
    }

    @Override
    public double isOutRight(double w) {
        if(this.getPositionX() + this.getWidth()/2 > w) return this.getPositionX() + this.getWidth()/2 - w;
        else return 0;
    }

    @Override
    public String toCsv() {
        String str = "";
        str += "Ell,"+this.getPositionX()+","+this.getPositionY()+","+this.getWidth()+","+this.getHeight()+","+this.getCouleur().toString();
        return str;
    }
}
