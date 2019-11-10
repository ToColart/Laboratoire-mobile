package unamur.buspanelapp.Model;

import java.io.Serializable;
import java.util.ArrayList;

public class BusModel implements Serializable {
    private Integer number;
    private ArrayList<StopModel> stopsList;
    private String departureHour;
    private String arrivalHour;

    public BusModel(Integer number, ArrayList<StopModel> stopsList, String departureHour, String arrivalHour) {
        this.setNumber(number);
        this.setStopsList(stopsList);
        this.setDepartureHour(departureHour);
        this.setArrivalHour(arrivalHour);
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public ArrayList<StopModel> getStopsList() {
        return stopsList;
    }

    public void setStopsList(ArrayList<StopModel> stopsList) {
        this.stopsList = stopsList;
    }

    public String getDepartureHour() {
        return departureHour;
    }

    public void setDepartureHour(String departureHour) {
        this.departureHour = departureHour;
    }

    public String getArrivalHour() {
        return arrivalHour;
    }

    public void setArrivalHour(String arrivalHour) {
        this.arrivalHour = arrivalHour;
    }
}
