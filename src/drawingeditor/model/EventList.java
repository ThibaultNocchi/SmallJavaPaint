package drawingeditor.model;

import javafx.scene.paint.Paint;

import java.util.ArrayList;

/**
 * Classe servant de liste et gérant les événements de l'historique de dessin.
 */
public class EventList {

    private ArrayList<EventForme> eventList;
    private int cursor;

    /**
     * Créé une liste vide d'événements et place le curseur de l'historique au début.
     */
    public EventList(){
        this.eventList = new ArrayList<>();
        this.cursor = -1;
    }

    /**
     * Ajoute un événement.
     * Si l'événement n'a pas de valeur d'historique spécifiée, on lui en ajoute une correspondant à la dernière dans l'historique + 1.
     * @param event
     */
    public void add(EventForme event){
        if(event.getHistory() == null) event.setHistory(this.getLastHistory()+1);
        this.eventList.add(event);
        this.cursor++;
    }

    /**
     * Annule le dernier événement et tous ceux ayant la même valeur d'historique.
     * @param dessin Dessin où supprimer les formes.
     */
    public void rollback(Dessin dessin){
        int lastHistory = this.getLastHistory();
        while(this.cursor > -1 && lastHistory == this.getLastHistory()) {
            this.eventList.get(this.cursor).rollback(dessin);
            this.cursor--;
        }
    }

    /**
     * Refait le dernier événement annulé et tous ceux ayant la même valeur d'historique.
     * @param dessin
     */
    public void rollforward(Dessin dessin){
        if(this.cursor < this.eventList.size()-1){
            this.eventList.get(++this.cursor).rollforward(dessin);
        }
        int lastHistory = this.getLastHistory();
        while(this.cursor < this.eventList.size()-1 && this.eventList.get(this.cursor+1).getHistory() == lastHistory){
            this.eventList.get(++this.cursor).rollforward(dessin);
        }
    }

    /**
     * Vide la liste d'événements.
     */
    public void clear(){
        this.eventList.clear();
        this.cursor = -1;
    }

    /**
     * Récupère la valeur d'historique du dernier événement.
     * @return
     */
    public int getLastHistory(){
        if(this.cursor == -1) return -1;
        else return this.eventList.get(this.cursor).getHistory();
    }

    /**
     * Change les couleurs de toutes les formes ayant la même valeur d'historique (celle composant un trait en somme).
     * @param eventColor Événement de changement de couleur de la forme cliquée, utilisée pour propager aux autres formes.
     */
    public void changeColorOfAllFormeWithSameHistory(EventFormeColor eventColor){
        ArrayList<Integer> listHistories = new ArrayList<>();
        ArrayList<EventFormeColor> newEvents = new ArrayList<>();
        for(EventForme event : this.eventList){
            if(event.getForme() == eventColor.getForme()) listHistories.add(event.getHistory());
        }
        for(EventForme event : this.eventList){
            if(listHistories.contains(event.getHistory())){
                Paint before = event.getForme().getCouleur();
                event.getForme().setCouleur(eventColor.getForme().getCouleur());
                EventFormeColor newEvent = new EventFormeColor(event.getForme(), before, event.getForme().getCouleur());
                newEvent.setHistory(eventColor.getHistory());
                newEvents.add(newEvent);
                ++this.cursor;
            }
        }
        this.eventList.addAll(newEvents);
    }

}
