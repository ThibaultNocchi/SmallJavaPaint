package drawingeditor.model;

import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.io.IOException;

public interface Dessin {
	void ajouterForme(final Forme shape);
	void supprimerForme(final Forme shape);
	void viderDessin();
	ObservableList<Forme> getFormes();
	String toCsv();
	void fromCsv(String csv);
	String save(String filename) throws FileNotFoundException;
	void load(String filename) throws IOException;
}