package unamur.buspanelapp.DAO;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;

import unamur.buspanelapp.Model.BusModel;
import unamur.buspanelapp.Model.StopModel;

public interface BusesIncomingDAO {
    ArrayList<BusModel> getAllBuses(StopModel currentStop) throws /*GetAllGardensException,*/ JSONException;
}