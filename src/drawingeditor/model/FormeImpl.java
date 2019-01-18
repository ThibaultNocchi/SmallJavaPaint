package drawingeditor.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * Implémentation de l'interface Forme.
 */
public abstract class FormeImpl implements Forme {

    protected ObjectProperty<Paint> couleurProperty;
    protected DoubleProperty positionXProperty;
    protected DoubleProperty positionYProperty;
    protected DoubleProperty widthProperty;
    protected DoubleProperty heightProperty;

    /**
     * Initialise la forme.
     * @see #initialize(double, double, double, double, Paint)
     */
    public FormeImpl(double x, double y, double w, double h, Paint color){
        this.initialize(x,y,w,h,color);
    }

    /**
     * Initialise la forme une ligne CSV précédemment exportée.
     * @param csv Ligne CSV.
     */
    public FormeImpl(String csv){
        String[] splitCsv = csv.split(",");
        double x = Double.parseDouble(splitCsv[1]);
        double y = Double.parseDouble(splitCsv[2]);
        double w = Double.parseDouble(splitCsv[3]);
        double h = Double.parseDouble(splitCsv[4]);
        String[] hex = splitCsv[5].split("0x");
        Color color = Color.web(hex[1]);
        this.initialize(x,y,w,h,color);
    }

    @Override
    public void initialize(double x, double y, double w, double h, Paint color){
        this.couleurProperty = new SimpleObjectProperty<>(color);
        this.positionXProperty = new SimpleDoubleProperty(x);
        this.positionYProperty = new SimpleDoubleProperty(y);
        this.widthProperty = new SimpleDoubleProperty(w);
        this.heightProperty = new SimpleDoubleProperty(h);
    }

    @Override
    public Paint getCouleur(){
        return this.couleurProperty.getValue();
    }

    @Override
    public double getPositionX(){
        return this.positionXProperty.getValue();
    }

    @Override
    public double getPositionY(){
        return this.positionYProperty.getValue();
    }

    @Override
    public double getWidth(){
        return this.widthProperty.getValue();
    }

    @Override
    public double getHeight(){
        return this.heightProperty.getValue();
    }

    @Override
    public void setCouleur(final Paint col){
        this.couleurProperty.setValue(col);
    }

    @Override
    public void setPosition(final double x, final double y){
        this.positionXProperty.setValue(x);
        this.positionYProperty.setValue(y);
    }

    @Override
    public void deplacer(final double tx, final double ty){
        this.positionXProperty.setValue(tx+this.getPositionX());
        this.positionYProperty.setValue(ty+this.getPositionY());
    }

    @Override
    public void redimensionner(final double tx, final double ty){
        this.widthProperty.setValue(tx+this.getWidth());
        this.heightProperty.setValue(ty+this.getHeight());
    }

    @Override
    public void setWidth(final double w){
        this.widthProperty.setValue(w);
    }

    @Override
    public void setHeight(final double h){
        this.heightProperty.setValue(h);
    }

    @Override
    public ObjectProperty<Paint> couleurProperty(){
        return this.couleurProperty;
    }

    @Override
    public DoubleProperty positionXProperty(){
        return this.positionXProperty;
    }

    @Override
    public DoubleProperty positionYProperty(){
        return this.positionYProperty;
    }

    @Override
    public DoubleProperty widthProperty(){
        return this.widthProperty;
    }

    @Override
    public DoubleProperty heightProperty(){
        return this.heightProperty;
    }

}
