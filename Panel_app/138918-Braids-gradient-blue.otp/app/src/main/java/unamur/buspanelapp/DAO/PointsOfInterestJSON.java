package unamur.buspanelapp.DAO;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import unamur.buspanelapp.Exception.GetAllPointsOfInterestException;
import unamur.buspanelapp.Model.PointOfInterestModel;
import unamur.buspanelapp.Service.Converter;

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

    public String putSelectedPointOfInterest(String stopId, int piId){
        String result = "";
        try {
            JSONObject ids = new JSONObject();
            ids.put("idStop", stopId);
            ids.put("idDestination", piId);

            URL url = new URL(URL_API_PointsOfInterest_BASE +"destination/setSelectedDestination?idStop="+stopId+"&idDestination="+piId);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("PUT");
            //connection.setDoInput(true);
            connection.setDoOutput(true);
            /*connection.connect();
            OutputStream out = connection.getOutputStream();
            out.write(ids.toString().getBytes());*/

            InputStream inputStream = connection.getInputStream();
            result = converter.convertStreamToString(inputStream);

            /*inputStream.close();
            out.close();
            connection.disconnect();*/
        }
        catch (IOException | JSONException e){
            System.out.print(e);
        }
        return result;
    }
}
