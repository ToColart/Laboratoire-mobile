package unamur.buspanelapp.DAO;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import unamur.buspanelapp.Model.BusModel;
import unamur.buspanelapp.Model.StopModel;
import unamur.buspanelapp.Service.Converter;

public class BusesIncomingJSON implements BusesIncomingDAO {
    private Converter converter = new unamur.buspanelapp.Service.ConverterJSON();

    public ArrayList<BusModel> getAllBuses(StopModel currentStop) throws /*GetAllGardensException,*/ JSONException {
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

        ArrayList<BusModel> allBuses = new ArrayList<BusModel>(){
            {
                add(new BusModel(3, new ArrayList<StopModel>(){{
                    add(new StopModel("111TEST111111TEST111111TEST111", "LA PLANTE Rue J. Hamoir", "50.449440", "4.857088"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "LA PLANTE Av. Félicien Rops", "50.447301", "4.859354"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "LA PLANTE Eglise", "50.445670", "4.859046"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "LA PLANTE Av. de la Pairelle", "50.442974", "4.858849"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "LA PLANTE Rue Dohet", "50.441387", "4.859164"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "LA PLANTE Rue Delonnoy", "50.445103", "4.856241"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "LA PLANTE Marronnier", "50.449948", "4.856821"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Parc de la Plante", "50.452753", "4.860650"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Casino", "50.456165", "4.862922"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Boulevard Ad Aquam", "50.459259", "4.866209"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Pont de France", "50.462491", "4.869908"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Rue de la Tour", "50.463502", "4.868229"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Rue Emile Cuvelier", "50.464611", "4.866448"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Rue de Bruxelles", "50.465378", "4.863314"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Pl. de la Station - Quai A", "50.468412", "4.861886"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Avenue de Stassart", "50.467099", "4.855007"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "SALZINNES Place Falmagne", "50.466157", "4.852775"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "SALZINNES Place Wiertz", "50.464859", "4.848837"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "SALZINNES Eglise Ste Julienne", "50.463630", "4.846059"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "SALZINNES Rue de la Colline", "50.462182", "4.846938"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "SALZINNES Chemin des Mélèzes", "50.459771", "4.847809"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "SALZINNES Rue del Marmol", "50.458074", "4.846624"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "SALZINNES Rue du Travail", "50.455823", "4.843099"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Citadelle école primaire", "50.452772", "4.844067"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Citadelle milieu du monde", "50.451553", "4.847422"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Citadelle motte Bel-air", "50.454169", "4.849387"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Citadelle Rond-point Thonar", "50.456834", "4.852794"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Citadelle théâtre de Verdure", "50.455875", "4.855256"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Citadelle château de Namur", "50.455347", "4.853649"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "NAMUR Citadelle école hôtelière", "50.454218", "4.853419"));
                    add(new StopModel("111TEST111111TEST111111TEST111", "TEST", "50.46200", "4.862092"));
                }}, "16h58", "17h34"));
                add(new BusModel(33, new ArrayList<StopModel>(){{
                    add(new StopModel("111TEST111111TEST111111TEST111", "Pied-Noir", "50.473889", "4.871366"));
                }}, "13h13", "13h35"));
            }
        };

        ArrayList<BusModel> busesComing = new ArrayList<>();
        for(BusModel bus: allBuses){
            if(bus.getStopsList().contains(currentStop)) {
                bus.setStopsList(new ArrayList<>( bus.getStopsList().subList(bus.getStopsList().indexOf(currentStop)+1, bus.getStopsList().size() - 1)));
                busesComing.add(bus);
            }
        }

        return new ArrayList<>(busesComing.subList(0, 9 > busesComing.size() ? busesComing.size() : 9));
    }
}
