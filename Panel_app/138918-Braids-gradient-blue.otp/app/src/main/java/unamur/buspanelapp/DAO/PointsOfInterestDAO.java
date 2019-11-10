package unamur.buspanelapp.DAO;

import org.json.JSONException;

import java.util.ArrayList;

import unamur.buspanelapp.Exception.GetAllPointsOfInterestException;
import unamur.buspanelapp.Model.PointOfInterestModel;

public interface PointsOfInterestDAO {
    ArrayList<PointOfInterestModel> getAllPointsOfInterest() throws GetAllPointsOfInterestException, JSONException;

    ArrayList<PointOfInterestModel> getPointsOfInterestAround(final String X, final String Y) throws JSONException;
}
