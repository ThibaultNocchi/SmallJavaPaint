package drawingeditor.model;

public abstract class EventForme {

    protected Forme forme;
    protected Integer history;

    public EventForme(Forme forme){
        this.forme = forme;
        this.history = null;
    }

    public Forme getForme(){
        return this.forme;
    }

    public Integer getHistory(){
        return this.history;
    }

    public void setHistory(Integer history){
        this.history = history;
    }

    public abstract void rollback(Dessin model);
    public abstract void rollforward(Dessin model);

}
