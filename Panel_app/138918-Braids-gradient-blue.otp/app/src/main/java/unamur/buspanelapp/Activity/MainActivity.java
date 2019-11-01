package unamur.buspanelapp.Activity;

import android.app.Fragment;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;

import unamur.buspanelapp.DAO.BusesIncomingDAO;
import unamur.buspanelapp.DAO.BusesIncomingJSON;
import unamur.buspanelapp.Model.BusModel;
import unamur.buspanelapp.Model.Custom_BusesList_Adapter;
import unamur.buspanelapp.Model.StopModel;
import unamur.buspanelapp.R;

public class MainActivity extends AppCompatActivity {

    private ArrayList<BusModel> listBuses = new ArrayList<>();
    private ListView busesListView;
    private Button fullScreenFirstScreenBtn;
    private View frag;
    //TODO get coords of pannel && get buses coming thanks to the information
    //private StopModel actualStop;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new LoadBuses().execute();

        busesListView = (ListView) findViewById(R.id.listBuses);
        busesListView.setOnItemClickListener(goToSplitScreen);

        fullScreenFirstScreenBtn = (Button) findViewById(R.id.fullScreenFirstScreenBtn);
        fullScreenFirstScreenBtn.setOnClickListener(goToFullScreen);
    }

    private class LoadBuses extends AsyncTask<Void, Void, ArrayList<BusModel>> {
        private String error = "";

        @Override
        protected ArrayList<BusModel> doInBackground(Void... strings) {
            BusesIncomingDAO busesDAO = new BusesIncomingJSON();
            ArrayList<BusModel> buses = new ArrayList<>();

            try{
                buses = busesDAO.getAllBuses();
            }
            /*catch (GetAllGardensException e){
                error = getString(R.string.error_get_all_gardens_exception);
            }*/
            catch (JSONException e){
                error = getString(R.string.JSONException);
            }

            return buses;
        }

        @Override
        protected void onPostExecute(ArrayList<BusModel> buses) {
            super.onPostExecute(buses);
            if(error.equals("")) {
                busesListView.setAdapter(new Custom_BusesList_Adapter(MainActivity.this, buses));
                listBuses = buses;
            }
            else{
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
            }
        }
    }

    private AdapterView.OnItemClickListener goToSplitScreen = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            frag = findViewById(R.id.wayDetails);
            frag.setVisibility(View.VISIBLE);

            fullScreenFirstScreenBtn.setVisibility(View.VISIBLE);
        }
    };

    private AdapterView.OnClickListener goToFullScreen = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View view) {
            frag.setVisibility(View.GONE);

            fullScreenFirstScreenBtn.setVisibility(View.GONE);
        }
    };
}
