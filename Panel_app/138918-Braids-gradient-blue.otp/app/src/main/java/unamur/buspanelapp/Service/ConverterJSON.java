package unamur.buspanelapp.Service;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import unamur.buspanelapp.Model.PointOfInterestModel;
import unamur.buspanelapp.Model.StopModel;
import unamur.buspanelapp.Service.Converter;

public class ConverterJSON implements Converter {

    //Convert a stream into a string Json
    public String convertStreamToString(java.io.InputStream inputStream){
        java.util.Scanner scanner = new java.util.Scanner(inputStream).useDelimiter("\\A");
        return scanner.hasNext()? scanner.next():"";
    }

    public ArrayList<PointOfInterestModel> formatToPointOfInterest(String stringJSON) throws JSONException{
        ArrayList<PointOfInterestModel> PIs = new ArrayList<>();

        JSONArray jsonArray = new JSONArray(stringJSON);

        JSONObject jsonGarden;
        for(int i = 0; i<jsonArray.length();i++){
            jsonGarden = jsonArray.getJSONObject(i);

            PIs.add(new PointOfInterestModel(jsonGarden.get("name").toString(),
                    jsonGarden.getString("description"),
                    jsonGarden.getString("picture"),
                    jsonGarden.getString("audio"),
                    jsonGarden.getString("coordX"),
                    jsonGarden.getString("coordY")));
        }
        return PIs;
    }

    /**
     *
     * @param stringJSON
     * example request: formatToStop("SAINT-SERVAIS+Polyclinique")
     * @return {"nhits": 4, "parameters": {"dataset": ["arrets-tec"], "refine": {"commune": "Namur", "pot_nom_ha": "SAINT-SERVAIS Polyclinique"}, "timezone": "UTC", "rows": 10, "format": "json", "facet": ["commune"]},
    "records": [{"datasetid": "arrets-tec", "recordid": "cb435725aa2d798443dd7443420641d872193118", "fields": {"commune": "Namur", "arrondissement": "Namur", "geo_point_2d": [50.473157581274876, 4.8436642890889985], "canton": "Namur", "pot_zone_t": "4501", "pot_nom_ha": "SAINT-SERVAIS Polyclinique", "geo_shape": {"type": "Point", "coordinates": [4.8436642890889985, 50.473157581274876]}, "pot_id": "N501icy"}, "geometry": {"type": "Point", "coordinates": [4.8436642890889985, 50.473157581274876]}, "record_timestamp": "2019-02-15T09:13:59.774000+00:00"},
    {"datasetid": "arrets-tec", "recordid": "e90e64f1d37498a775746d9a8719d7881a707fb3", "fields": {"commune": "Namur", "arrondissement": "Namur", "geo_point_2d": [50.473035190649696, 4.844301329776283], "canton": "Namur", "pot_zone_t": "4501", "pot_nom_ha": "SAINT-SERVAIS Polyclinique", "geo_shape": {"type": "Point", "coordinates": [4.844301329776283, 50.473035190649696]}, "pot_id": "N501icz"}, "geometry": {"type": "Point", "coordinates": [4.844301329776283, 50.473035190649696]}, "record_timestamp": "2019-02-15T09:13:59.774000+00:00"},
    {"datasetid": "arrets-tec", "recordid": "98504039d73a0330e68ff4da5ff4a6e800b0b2fb", "fields": {"commune": "Namur", "arrondissement": "Namur", "geo_point_2d": [50.47317089303224, 4.843706684250776], "canton": "Namur", "pot_zone_t": "4501", "pot_nom_ha": "SAINT-SERVAIS Polyclinique", "geo_shape": {"type": "Point", "coordinates": [4.843706684250776, 50.47317089303224]}, "pot_id": "N501ica"}, "geometry": {"type": "Point", "coordinates": [4.843706684250776, 50.47317089303224]}, "record_timestamp": "2019-02-15T09:13:59.774000+00:00"},
    {"datasetid": "arrets-tec", "recordid": "6603a4ea0a0a2a0e65c6a8691339eefe749ccc42", "fields": {"commune": "Namur", "arrondissement": "Namur", "geo_point_2d": [50.47304113291261, 4.843882653558621], "canton": "Namur", "pot_zone_t": "4501", "pot_nom_ha": "SAINT-SERVAIS Polyclinique", "geo_shape": {"type": "Point", "coordinates": [4.843882653558621, 50.47304113291261]}, "pot_id": "N501icb"}, "geometry": {"type": "Point", "coordinates": [4.843882653558621, 50.47304113291261]}, "record_timestamp": "2019-02-15T09:13:59.774000+00:00"}],
    "facet_groups": [{"facets": [{"count": 4, "path": "Namur", "state": "refined", "name": "Namur"}], "name": "commune"}, {"facets": [{"count": 4, "path": "SAINT-SERVAIS Polyclinique", "state": "refined", "name": "SAINT-SERVAIS Polyclinique"}], "name": "pot_nom_ha"}]}
     * @throws JSONException
     */
    public StopModel formatToStop(String stringJSON) throws JSONException{
        JSONObject jsonStop = new JSONObject(stringJSON);
        JSONObject jsonFirstStop = jsonStop.getJSONArray("records").getJSONObject(0);

        return new StopModel(jsonFirstStop.getJSONObject("fields").getString("pot_nom_ha"),
                jsonFirstStop.getJSONObject("fields").getJSONArray("geo_point_2d").get(0).toString(),
                jsonFirstStop.getJSONObject("fields").getJSONArray("geo_point_2d").get(1).toString());
    }

    public ArrayList<StopModel> formatToStops(String stringJSON) throws JSONException{
        ArrayList<StopModel> stops = new ArrayList<>();
        JSONObject jsonStops = new JSONObject(stringJSON);
        JSONArray jsonArray = jsonStops.getJSONArray("records");

        JSONObject jsonStop;
        for(int i = 0; i<jsonArray.length();i++){
            jsonStop = jsonArray.getJSONObject(i);

            stops.add(new StopModel(jsonStop.getJSONObject("fields").getString("pot_nom_ha"),
                    jsonStop.getJSONObject("fields").getJSONArray("geo_point_2d").getString(0),
                    jsonStop.getJSONObject("fields").getJSONArray("geo_point_2d").getString(1)));

        }
        return stops;
    }
/*

    public Plant formatToPlant(String stringJSON) throws JSONException{
        JSONObject jsonPlant = new JSONObject(stringJSON);

        Gson object = new GsonBuilder().create();

        return object.fromJson(jsonPlant.toString(), Plant.class);
    }*/
}
