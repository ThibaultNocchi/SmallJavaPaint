package drawingeditor.model;

import javafx.scene.paint.Paint;

import java.util.ArrayList;

public class EventList {

    private ArrayList<EventForme> eventList;
    private int cursor;

    public EventList(){
        this.eventList = new ArrayList<>();
        this.cursor = -1;
    }

    public void add(EventForme event){
        this.eventList.subList(this.cursor+1, this.eventList.size()).clear();
        if(event.getHistory() == null) event.setHistory(this.getLastHistory()+1);
        this.eventList.add(event);
        this.cursor++;
    }

    public void rollback(Dessin dessin){
        int lastHistory = this.getLastHistory();
        while(this.cursor > -1 && lastHistory == this.getLastHistory()) {
            this.eventList.get(this.cursor).rollback(dessin);
            this.cursor--;
        }
    }

    public void rollforward(Dessin dessin){
        if(this.cursor < this.eventList.size()-1){
            this.eventList.get(++this.cursor).rollforward(dessin);
        }
        int lastHistory = this.getLastHistory();
        while(this.cursor < this.eventList.size()-1 && this.eventList.get(this.cursor+1).getHistory() == lastHistory){
            this.eventList.get(++this.cursor).rollforward(dessin);
        }
    }

    public void clear(){
        this.eventList.clear();
        this.cursor = -1;
    }

    public int getLastHistory(){
        if(this.cursor == -1) return -1;
        else return this.eventList.get(this.cursor).getHistory();
    }

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
