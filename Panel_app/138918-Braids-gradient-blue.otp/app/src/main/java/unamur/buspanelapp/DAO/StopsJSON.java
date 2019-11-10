package unamur.buspanelapp.DAO;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocketFactory;

import unamur.buspanelapp.Exception.GetAllPointsOfInterestException;
import unamur.buspanelapp.Exception.GetAllStopsException;
import unamur.buspanelapp.Exception.GetStopException;
import unamur.buspanelapp.Model.PointOfInterestModel;
import unamur.buspanelapp.Model.StopModel;
import unamur.buspanelapp.Service.Converter;
import unamur.buspanelapp.Service.MySSLSocketFactory;

import static unamur.buspanelapp.Constants.URL_API_Stops_BASE;

public class StopsJSON implements StopsDAO {
    private Converter converter = new unamur.buspanelapp.Service.ConverterJSON();

    public ArrayList<StopModel> getAllStops() throws GetAllStopsException, JSONException{
        String stringJSON;
        try {
            URL url = new URL(URL_API_Stops_BASE);
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();

            connection.setSSLSocketFactory(new MySSLSocketFactory(connection.getSSLSocketFactory()));
            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            stringJSON = converter.convertStreamToString(inputStream);
        }
        catch (IOException e){
            throw new GetAllStopsException();
        }
        return converter.formatToStops(stringJSON);
    }

    public StopModel getStopByName(String stopName) throws GetStopException, JSONException{
        String stringJSON="";
        try {
            URL url = new URL(URL_API_Stops_BASE +"&refine.pot_nom_ha="+stopName);//genre SAINT-SERVAIS+Polyclinique
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(new MySSLSocketFactory(connection.getSSLSocketFactory()));

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            stringJSON = converter.convertStreamToString(inputStream);
        }
        catch (IOException e){
            throw new GetStopException();
        }
        return converter.formatToStop(stringJSON);
    }

    public StopModel getStopByCoordinates(String coordX, String coordY) throws JSONException{
        String stringJSON="";
        try {
            URL url = new URL(URL_API_Stops_BASE +"&geofilter.distance="+coordX+"%2C+"+coordY+"%2C+10");//genre &geofilter.distance=50.473157581274876%2C+4.8436642890889985%2C+5
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setSSLSocketFactory(new MySSLSocketFactory(connection.getSSLSocketFactory()));

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            stringJSON = converter.convertStreamToString(inputStream);
        }
        catch (IOException e){
            //throw new GetAllStopsException();
        }
        return converter.formatToStop(stringJSON);
    }
}
