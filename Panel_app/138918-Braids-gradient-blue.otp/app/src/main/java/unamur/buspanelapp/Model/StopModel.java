package unamur.buspanelapp.Model;

import java.io.Serializable;

public class StopModel implements Serializable {
    private String name;
    private String coordX;
    private String coordY;

    public StopModel() {
    }

    public StopModel(String name, String coordX, String coordY) {
        this.setName(name);
        this.setCoordX(coordX);
        this.setCoordY(coordY);
    }

    public boolean equals(StopModel q) {
        return !(q == null
                || !name.equals(q.name)
                || !coordX.equals(q.coordX)
                || !coordY.equals(q.coordY));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(getClass())) { return false; }
        return equals((StopModel) o);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoordX() {
        return coordX;
    }

    public void setCoordX(String coordX) {
        this.coordX = coordX;
    }

    public String getCoordY() {
        return coordY;
    }

    public void setCoordY(String coordY) {
        this.coordY = coordY;
    }
}
