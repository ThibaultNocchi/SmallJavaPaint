package drawingeditor.model;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.paint.Paint;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Dessin {
	void ajouterForme(final Forme shape);
	void supprimerForme(final Forme shape);
	void deplacerToutesFormes(final double tx, final double ty);
	void viderDessin();
	ObservableList<Forme> getFormes();
	public Paint getBg();
	public void setBg(Paint bg);
	public ObjectProperty<Paint> bgProperty();
	String toCsv();
	double[] fromCsv(String csv);
	String save(String filename, double paneWidth, double paneHeight) throws FileNotFoundException;
	double[] load(String filename) throws IOException;
}