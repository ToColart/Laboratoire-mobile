package unamur.buspanelapp.DAO;

import org.json.JSONException;

import java.util.ArrayList;

import unamur.buspanelapp.Exception.GetAllPointsOfInterestException;
import unamur.buspanelapp.Exception.GetAllStopsException;
import unamur.buspanelapp.Exception.GetStopException;
import unamur.buspanelapp.Model.PointOfInterestModel;
import unamur.buspanelapp.Model.StopModel;

public interface StopsDAO {
    ArrayList<StopModel> getAllStops() throws GetAllStopsException, JSONException;

    StopModel getStopByName(String stopName) throws GetStopException, JSONException;

    StopModel getStopByCoordinates(String coordX, String coordY) throws JSONException;
}
