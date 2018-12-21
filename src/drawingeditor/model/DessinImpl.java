package drawingeditor.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

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
    public ObservableList<Forme> getFormes() {
        return this.liste;
    }
}
