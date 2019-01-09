package drawingeditor.model;

import java.util.ArrayList;

public class EventList {

    private ArrayList<FormeEvent> eventList;

    public EventList(){
        this.eventList = new ArrayList<>();
    }

    public void add(FormeEvent event){
        this.eventList.add(event);
    }

    public void rollback(Dessin dessin){
        this.eventList.get(this.eventList.size()-1).rollback(dessin);
        this.eventList.remove(this.eventList.size()-1);
    }

    public void clear(){
        this.eventList.clear();
    }

}
