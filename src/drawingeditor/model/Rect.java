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

    @Override
    public double isOutTop() {
        if(this.getPositionY() < 0) return -(this.getPositionY());
        else return 0;
    }

    @Override
    public double isOutBottom(double h) {
        if(this.getPositionY() + this.getHeight() > h) return this.getPositionY() + this.getHeight() - h;
        else return 0;
    }

    @Override
    public double isOutLeft() {
        if(this.getPositionX() < 0) return -(this.getPositionX());
        else return 0;
    }

    @Override
    public double isOutRight(double w) {
        if(this.getPositionX() + this.getWidth() > w) return this.getPositionX() + this.getWidth() - w;
        else return 0;
    }

    public String toCsv(){
        String str = "";
        str += "Rect,"+this.getPositionX()+","+this.getPositionY()+","+this.getWidth()+","+this.getHeight()+","+this.getCouleur().toString();
        return str;
    }

}
