package drawingeditor.model;

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
        this.eventList.add(event);
        this.cursor++;
    }

    public void rollback(Dessin dessin){
        if(this.cursor > -1) {
            this.eventList.get(this.cursor).rollback(dessin);
            this.cursor--;
        }
    }

    public void rollforward(Dessin dessin){
        if(this.cursor < this.eventList.size()-1){
            this.cursor++;
            this.eventList.get(this.cursor).rollforward(dessin);
        }
    }

    public void clear(){
        this.eventList.clear();
        this.cursor = -1;
    }

}
