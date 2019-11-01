package unamur.buspanelapp.Model;

public class StopModel {
    private String name;
    private String coordX;
    private String coordY;

    public StopModel(String name, String coordX, String coordY) {
        this.setName(name);
        this.setCoordX(coordX);
        this.setCoordY(coordY);
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
