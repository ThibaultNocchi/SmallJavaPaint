package drawingeditor.model;

import javafx.beans.property.ObjectProperty;
import javafx.collections.ObservableList;
import javafx.scene.paint.Paint;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Interface représentant un dessin.
 */
public interface Dessin {
	/**
	 * Ajoute la forme shape au dessin.
	 * @param shape
	 */
	void ajouterForme(final Forme shape);

	/**
	 * Supprime la forme shape du dessin.
	 * @param shape
	 */
	void supprimerForme(final Forme shape);

	/**
	 * Translate toutes les formes du dessin par une valeur tx pour l'axe X et ty pour l'axe Y.
	 * @param tx
	 * @param ty
	 */
	void deplacerToutesFormes(final double tx, final double ty);

	/**
	 * Supprime toutes les formes du dessin et vide l'historique.
	 */
	void viderDessin();

	/**
	 * Retourne la liste des formes dans le dessin.
	 * @return liste des formes.
	 */
	ObservableList<Forme> getFormes();

	/**
	 * Retourne la couleur du fond du dessin.
	 * @return Objet Paint du fond.
	 */
	Paint getBg();

	/**
	 * Change la couleur du fond du dessin.
	 * @param bg
	 */
	void setBg(Paint bg);

	/**
	 * Retourne la propriété allant avec la couleur de fond.
	 * @return
	 */
	ObjectProperty<Paint> bgProperty();

	/**
	 * Retourne une chaine correspondant à un format CSV du dessin.
	 * @return
	 */
	String toCsv();

	/**
	 * Charge un dessin depuis un CSV.
	 * @param csv Chaine avec le contenu du CSV.
	 * @return La taille du pane nécessaire pour afficher le dessin.
	 */
	double[] fromCsv(String csv);

	/**
	 * Sauvegarde le dessin dans un fichier CSV.
	 * @param filename Nom du fichier.
	 * @param paneWidth Largeur du pane du dessin.
	 * @param paneHeight Hauteur du pane du dessin.
	 * @return Le nom du fichier réellement utilisé (par exemple avec l'extension CSV en plus).
	 * @throws FileNotFoundException
	 */
	String save(String filename, double paneWidth, double paneHeight) throws FileNotFoundException;

	/**
	 * Charge un dessin depuis un fichier CSV.
	 * @param filename Nom du fichier.
	 * @return La taille du pane nécessaire pour afficher le dessin.
	 * @throws IOException
	 */
	double[] load(String filename) throws IOException;
}