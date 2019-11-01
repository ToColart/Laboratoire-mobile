package unamur.buspanelapp.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import unamur.buspanelapp.Service.Converter;

public class ConverterJSON implements Converter {

    /*private Gson gsonObject = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd")
            .serializeNulls()
            .create();

    //Convert a stream into a string Json
    public String convertStreamToString(java.io.InputStream inputStream){
        java.util.Scanner scanner = new java.util.Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext()? scanner.next():"";
    }

    public User formatToUser(String stringJSON) throws JSONException {
        JSONObject jsonUser = new JSONObject(stringJSON);

        return gsonObject.fromJson(jsonUser.toString(), User.class);
    }

    public ArrayList<Garden> formatToGardens(String stringJSON) throws JSONException{
        ArrayList<Garden> gardens = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(stringJSON);

        JSONObject jsonGarden;
        for(int i = 0; i<jsonArray.length();i++){
            jsonGarden = jsonArray.getJSONObject(i);

            gardens.add(gsonObject.fromJson(jsonGarden.toString(), Garden.class));
        }
        return gardens;
    }

    public ArrayList<Event> formatToEvents(String stringJSON) throws JSONException{

        ArrayList<Event> events = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(stringJSON);

        JSONObject jsonGarden;
        for(int i = 0; i<jsonArray.length();i++){
            jsonGarden = jsonArray.getJSONObject(i);

            events.add(gsonObject.fromJson(jsonGarden.toString(), Event.class));
        }

        return events;
    }

    public Plant formatToPlant(String stringJSON) throws JSONException{
        JSONObject jsonPlant = new JSONObject(stringJSON);

        Gson object = new GsonBuilder().create();

        return object.fromJson(jsonPlant.toString(), Plant.class);
    }*/
}
