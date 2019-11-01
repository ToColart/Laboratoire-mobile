package unamur.buspanelapp.DAO;

import org.json.JSONException;

import java.util.ArrayList;

import unamur.buspanelapp.Model.BusModel;
import unamur.buspanelapp.Model.StopModel;
import unamur.buspanelapp.Service.Converter;

public class BusesIncomingJSON implements BusesIncomingDAO {
    private Converter converter = new unamur.buspanelapp.Service.ConverterJSON();

    public ArrayList<BusModel> getAllBuses() throws /*GetAllGardensException,*/ JSONException {
        /*String stringJSON;
        try {
            URL url = new URL(URL_API_BASE +"Gardens");
            URLConnection connection = url.openConnection();

            InputStream inputStream = new BufferedInputStream(connection.getInputStream());
            stringJSON = converter.convertStreamToString(inputStream);
        }
        catch (IOException e){
            throw new GetAllGardensException();
        }
        return converter.formatToGardens(stringJSON);*/

        return new ArrayList<BusModel>(){
            {
                add(new BusModel(3, new ArrayList<StopModel>(){{
                    add(new StopModel("Citadelle", "50.462620", "4.868829"));
                }}, "13h07", "13h23"));
                add(new BusModel(33, new ArrayList<StopModel>(){{
                    add(new StopModel("Pied-Noir", "50.473889", "4.871366"));
                }}, "13h13", "13h35"));
            }
        };
    }
}
