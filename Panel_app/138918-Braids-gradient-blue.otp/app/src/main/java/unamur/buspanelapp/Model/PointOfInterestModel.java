package unamur.buspanelapp.Model;

import java.io.Serializable;

public class PointOfInterestModel implements Serializable {
    private String name;
    private String description;
    private String picture;
    private String audioGuide;
    private String coordX;
    private String coordY;

    public PointOfInterestModel() {
    }

    public PointOfInterestModel(String name, String description, String picture, String audioGuide, String coordX, String coordY) {
        this.setName(name);
        this.setDescription(description);
        this.setPicture(picture);
        this.setAudioGuide(audioGuide);
        this.setCoordX(coordX);
        this.setCoordY(coordY);
    }

    public boolean equals(PointOfInterestModel q) {
        return !(q == null
                || !name.equals(q.name)
                || !description.equals(q.description)
                || !picture.equals(q.picture)
                || !audioGuide.equals(q.audioGuide)
                || !coordX.equals(q.coordX)
                || !coordY.equals(q.coordY));
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || !o.getClass().equals(getClass())) { return false; }
        return equals((PointOfInterestModel) o);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAudioGuide() {
        return audioGuide;
    }

    public void setAudioGuide(String audioGuide) {
        this.audioGuide = audioGuide;
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
