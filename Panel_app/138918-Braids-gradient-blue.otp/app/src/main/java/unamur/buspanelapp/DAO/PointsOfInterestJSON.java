package unamur.buspanelapp.DAO;

import com.google.gson.Gson;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

import unamur.buspanelapp.Exception.GetAllPointsOfInterestException;
import unamur.buspanelapp.Exception.GetAllStopsException;
import unamur.buspanelapp.Model.PointOfInterestModel;
import unamur.buspanelapp.Model.StopModel;
import unamur.buspanelapp.Service.Converter;
import unamur.buspanelapp.Service.MySSLSocketFactory;

import static unamur.buspanelapp.Constants.URL_API_PointsOfInterest_BASE;

public class PointsOfInterestJSON implements PointsOfInterestDAO {
    private Converter converter = new unamur.buspanelapp.Service.ConverterJSON();

    public ArrayList<PointOfInterestModel> getAllPointsOfInterest() throws GetAllPointsOfInterestException, JSONException{
        String stringJSON;
        try {
            URL url = new URL(URL_API_PointsOfInterest_BASE +"destination/getDestinations");
            URLConnection connection = url.openConnection();

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            stringJSON = converter.convertStreamToString(inputStream);
        }
        catch (IOException e){
            throw new GetAllPointsOfInterestException();
        }
        return converter.formatToPointOfInterest(stringJSON);
    }

    public ArrayList<PointOfInterestModel> getPointsOfInterestAround(String coordX, String coordY) throws JSONException{
        String stringJSON = "";
        try {
            URL url = new URL(URL_API_PointsOfInterest_BASE +"destination/getDestinationsAround?coordX="+coordX+"&coordY="+coordY);
            URLConnection connection = url.openConnection();

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            stringJSON = converter.convertStreamToString(inputStream);
        }
        catch (IOException e){
            System.out.print(e);
        }

        return converter.formatToPointOfInterest(stringJSON);
    }

    public ArrayList<PointOfInterestModel> getPointsOfInterestAroundAllStops(ArrayList<StopModel> stops) throws JSONException{
        String stringJSON = "";
        try {
            StringBuilder request = new StringBuilder();
            for(StopModel stop:stops){
                request.append("coordX="+stop.getCoordX()+"&coordY="+stop.getCoordY()+"&");
            }
            request.deleteCharAt(request.length()-1);
            URL url = new URL(URL_API_PointsOfInterest_BASE +"destination/getDestinationsAroundMult?"+request);
            URLConnection connection = url.openConnection();

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            stringJSON = converter.convertStreamToString(inputStream);
        }
        catch (IOException e){
            System.out.print(e);
        }

        return converter.formatToPointOfInterest(stringJSON);
    }
}
