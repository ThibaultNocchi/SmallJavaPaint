package drawingeditor.model;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.ObjectProperty;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

/**
 * Interface permettant de gérer une forme.
 */
public interface Forme {

	/**
	 * Permet d'initialiser et lier les différentes propriétés de la forme.
	 * @param x Position X sur le dessin.
	 * @param y Position Y sur le dessin.
	 * @param w Largeur.
	 * @param h Hauteur.
	 * @param color Couleur de la forme.
	 */
	void initialize(double x, double y, double w, double h, Paint color);

	/**
	 * Si le haut de la forme dépasse, renvoie sa distance par rapport au bord.
	 * Sinon renvoie 0.
	 * @return
	 */
	double isOutTop();

	/**
	 * Si le bas de la forme dépasse, renvoie sa distance par rapport au bord.
	 * Sinon renvoie 0.
	 * @param h Hauteur du pane.
	 * @return
	 */
	double isOutBottom(double h);

	/**
	 * Si la gauche de la forme dépasse, renvoie sa distance par rapport au bord.
	 * Sinon renvoie 0.
	 * @return
	 */
	double isOutLeft();

	/**
	 * Si la droite de la forme dépasse, renvoie sa distance par rapport au bord.
	 * Sinon renvoie 0.
	 * @param w Largeur du pane.
	 * @return
	 */
	double isOutRight(double w);

	/**
	 * Créé une vue JavaFX liée à la forme actuelle.
	 * @return
	 */
	Shape createViewShape();

	/**
	 * Retourne la couleur de la forme.
	 * @return
	 */
	Paint getCouleur();

	/**
	 * Retourne la position X de la forme.
	 * @return
	 */
	double getPositionX();

	/**
	 * Retourne la position Y de la forme.
	 * @return
	 */
	double getPositionY();

	/**
	 * Retourne la largeur de la forme.
	 * @return
	 */
	double getWidth();

	/**
	 * Retourne la haute de la forme.
	 * @return
	 */
	double getHeight();

	/**
	 * Change la couleur de la forme.
	 * @param col
	 */
	void setCouleur(final Paint col);

	/**
	 * Change la position de la forme.
	 * @param x Position X.
	 * @param y Position Y.
	 */
	void setPosition(final double x, final double y);

	/**
	 * Translate la forme de tx sur l'axe X et ty sur l'axe Y.
	 * @param tx
	 * @param ty
	 */
	void deplacer(final double tx, final double ty);

	/**
	 * Change la taille de la forme de tx en largeur et ty en hauteur.
	 * @param tx
	 * @param ty
	 */
	void redimensionner(final double tx, final double ty);

	/**
	 * Change la largeur de la forme à w.
	 * @param w
	 */
	void setWidth(final double w);

	/**
	 * Change la hauteur de la forme à h.
	 * @param h
	 */
	void setHeight(final double h);

	/**
	 * Retourne la propriété liée à la couleur.
	 * @return
	 */
	ObjectProperty<Paint> couleurProperty();

	/**
	 * Retourne la propriété liée à la position X.
	 * @return
	 */
	DoubleProperty positionXProperty();

	/**
	 * Retourne la propriété liée à la position Y.
	 * @return
	 */
	DoubleProperty positionYProperty();

	/**
	 * Retourne la propriété liée à la largeur.
	 * @return
	 */
	DoubleProperty widthProperty();

	/**
	 * Retourne la propriété liée à la hauteur.
	 * @return
	 */
	DoubleProperty heightProperty();

	/**
	 * Convertit la forme en format CSV avec ses coordonnées, son type, sa couleur...
	 * @return
	 */
	String toCsv();
}
