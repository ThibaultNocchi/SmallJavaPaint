package drawingeditor.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;

public class DessinImpl implements Dessin {

    private ObservableList liste;

    public DessinImpl(){
        this.liste = FXCollections.observableArrayList();
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
    public void viderDessin() { this.liste.clear(); }

    @Override
    public ObservableList<Forme> getFormes() {
        return this.liste;
    }

    @Override
    public String toCsv() {
        String str = "";
        for(Object forme : this.liste){
            str += ((Forme) forme).toCsv()+"\n";
        }
        return str;
    }

    @Override
    public void fromCsv(String csv) {
        this.viderDessin();
        String[] formes = csv.split("\n");
        for(String str : formes){
            String[] splitCsv = str.split(",");
            switch (splitCsv[0]){
                case "Rect":
                    ajouterForme(new Rect(str));
                    break;
                case "Ell":
                    ajouterForme(new Ell(str));
                    break;
            }
        }
    }

    @Override
    public String save(String filename) throws FileNotFoundException {
        String newFilename = filename;
        if(newFilename.equals("")) newFilename = "default.csv";
        String[] splitFilename = newFilename.split("\\.");
        if(splitFilename.length == 0 || !splitFilename[splitFilename.length-1].equals("csv")) newFilename += ".csv";
        try (PrintWriter out = new PrintWriter(newFilename)) {
            out.println(this.toCsv());
        }
        return newFilename;
    }

    @Override
    public void load(String filename) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(filename));
        String res = new String(encoded, StandardCharsets.UTF_8);
        this.fromCsv(res);
    }

    public String toString(){
        String str = "Size: "+this.liste.size()+"\n";
        for(Object forme : this.liste){
            str += ((Forme) forme).toString()+"\n";
        }
        return str;
    }
}
