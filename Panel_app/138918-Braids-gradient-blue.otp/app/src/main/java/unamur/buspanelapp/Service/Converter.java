package unamur.buspanelapp.Service;

import org.json.JSONException;

import java.util.ArrayList;

import unamur.buspanelapp.Model.PointOfInterestModel;
import unamur.buspanelapp.Model.StopModel;

public interface Converter {
    String convertStreamToString(java.io.InputStream inputStream);
/*
    User formatToUser(String stringJSON) throws JSONException;
*/
    ArrayList<PointOfInterestModel> formatToPointOfInterest(String stringJSON) throws JSONException;

    StopModel formatToStop(String stringJSON) throws JSONException;

    ArrayList<StopModel> formatToStops(String stringJSON) throws JSONException;
/*
    ArrayList<Event> formatToEvents(String stringJSON) throws JSONException;

    Plant formatToPlant(String stringJSON) throws JSONException;*/
}

