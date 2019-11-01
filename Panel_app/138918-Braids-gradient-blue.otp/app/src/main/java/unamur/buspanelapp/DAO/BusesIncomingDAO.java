package unamur.buspanelapp.DAO;
import org.json.JSONException;

import java.util.ArrayList;

import unamur.buspanelapp.Model.BusModel;

public interface BusesIncomingDAO {
    ArrayList<BusModel> getAllBuses() throws /*GetAllGardensException,*/ JSONException;
}