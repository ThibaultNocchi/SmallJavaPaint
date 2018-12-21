package drawingeditor.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Paint;

public abstract class FormeImpl implements Forme {

    protected ObjectProperty<Paint> couleurProperty;
    protected DoubleProperty positionXProperty;
    protected DoubleProperty positionYProperty;
    protected DoubleProperty widthProperty;
    protected DoubleProperty heightProperty;

    public FormeImpl(double x, double y, double w, double h, Paint color){
        this.setCouleur(color);
        this.setPosition(x,y);
        this.setWidth(w);
        this.setHeight(h);
    }

    public Paint getCouleur(){
        return this.couleurProperty.getValue();
    }

    public double getPositionX(){
        return this.positionXProperty.getValue();
    }

    public double getPositionY(){
        return this.positionYProperty.getValue();
    }

    public double getWidth(){
        return this.widthProperty.getValue();
    }

    public double getHeight(){
        return this.heightProperty.getValue();
    }

    public void setCouleur(final Paint col){
        this.couleurProperty.setValue(col);
    }

    public void setPosition(final double x, final double y){
        this.positionXProperty.setValue(x);
        this.positionYProperty.setValue(y);
    }

    public void deplacer(final double tx, final double ty){
        this.positionXProperty.setValue(tx+this.getPositionX());
        this.positionYProperty.setValue(ty+this.getPositionY());
    }

    public void setWidth(final double w){
        this.widthProperty.setValue(w);
    }

    public void setHeight(final double h){
        this.heightProperty.setValue(h);
    }

    public ObjectProperty<Paint> couleurProperty(){
        return this.couleurProperty;
    }

    public DoubleProperty positionXProperty(){
        return this.positionXProperty;
    }

    public DoubleProperty positionYProperty(){
        return this.positionYProperty();
    }

    public DoubleProperty widthProperty(){
        return this.widthProperty;
    }

    public DoubleProperty heightProperty(){
        return this.heightProperty;
    }

}
