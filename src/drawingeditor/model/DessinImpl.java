package drawingeditor.model;

import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * Implémentation de l'interface Dessin.
 * @see drawingeditor.model.Dessin
 */
public class DessinImpl implements Dessin {

    private ObservableList liste;
    private ObjectProperty<Paint> bgProperty;

    /**
     * Initialise la liste de formes et met une couleur de fond par défaut.
     */
    public DessinImpl(){
        this.liste = FXCollections.observableArrayList();
        this.bgProperty = new SimpleObjectProperty<>(Color.WHITE);
    }

    @Override
    public void ajouterForme(Forme shape) {
        this.liste.add(shape);
    }

    @Override
    public void supprimerForme(Forme shape) {
        this.liste.remove(shape);
    }

    @Override
    public void deplacerToutesFormes(double tx, double ty) {

        for(Object forme : this.liste){
            ((Forme) forme).deplacer(tx, ty);
        }

    }

    @Override
    public void viderDessin() {
        this.liste.clear();
        this.bgProperty.setValue(Color.WHITE);
    }

    @Override
    public ObservableList<Forme> getFormes() {
        return this.liste;
    }

    @Override
    public Paint getBg(){
        return this.bgProperty.getValue();
    }

    @Override
    public void setBg(Paint bg) {
        this.bgProperty.setValue(bg);
    }

    @Override
    public ObjectProperty<Paint> bgProperty(){
        return this.bgProperty;
    }

    @Override
    public String toCsv() {
        String str = "";
        str += "BgColor,"+this.bgProperty.getValue().toString()+"\n";   // Ajout de la couleur de fond dans le CSV.
        for(Object forme : this.liste){
            str += ((Forme) forme).toCsv()+"\n";
        }
        return str;
    }

    @Override
    public double[] fromCsv(String csv) {
        this.viderDessin();
        String[] lignes = csv.split("\n");
        double[] paneSizes = new double[2];
        for(String str : lignes){
            String[] splitCsv = str.split(",");
            switch (splitCsv[0]){
                case "Rect":
                    ajouterForme(new Rect(str));
                    break;
                case "Ell":
                    ajouterForme(new Ell(str));
                    break;
                case "BgColor":
                    String[] hex = splitCsv[1].split("0x");
                    Color color = Color.web(hex[1]);
                    this.bgProperty.setValue(color);
                    break;
                case "paneWidth":
                    paneSizes[0] = Double.parseDouble(splitCsv[1]);
                    break;
                case "paneHeight":
                    paneSizes[1] = Double.parseDouble(splitCsv[1]);
                    break;
            }
        }
        return paneSizes;
    }

    @Override
    public String save(String filename, double paneWidth, double paneHeight) throws FileNotFoundException {
        String newFilename = filename;
        if(newFilename.equals("")) newFilename = "default.csv";     // Si il n'y a pas de nom de fichier spécifié, on en prend un par défaut.
        String[] splitFilename = newFilename.split("\\.");
        if(splitFilename.length == 0 || !splitFilename[splitFilename.length-1].equals("csv")) newFilename += ".csv";    // On regarde si une extension est spéficiée, si non, on l'ajoute.
        try (PrintWriter out = new PrintWriter(newFilename)) {
            out.println("paneWidth,"+paneWidth);    // Ajoute la taille du pane en début de fichier.
            out.println("paneHeight,"+paneHeight);
            out.println(this.toCsv());
        }
        return newFilename;
    }

    @Override
    public double[] load(String filename) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(filename));
        String res = new String(encoded, StandardCharsets.UTF_8);
        return this.fromCsv(res);
    }

    public String toString(){
        String str = "Size: "+this.liste.size()+"\n";
        for(Object forme : this.liste){
            str += ((Forme) forme).toString()+"\n";
        }
        return str;
    }
}
