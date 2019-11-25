package unamur.buspanelapp.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import unamur.buspanelapp.DAO.BusesIncomingDAO;
import unamur.buspanelapp.DAO.BusesIncomingJSON;
import unamur.buspanelapp.DAO.PointsOfInterestDAO;
import unamur.buspanelapp.DAO.PointsOfInterestJSON;
import unamur.buspanelapp.DAO.StopsDAO;
import unamur.buspanelapp.DAO.StopsJSON;
import unamur.buspanelapp.Model.BusModel;
import unamur.buspanelapp.Model.Custom_BusesList_Adapter;
import unamur.buspanelapp.Model.StopModel;
import unamur.buspanelapp.R;

import static unamur.buspanelapp.Constants.CURRENT_STOP;

public class MainActivity extends AppCompatActivity {

    private ListView busesListView;
    private ImageButton fullScreenFirstScreenBtn;
    private TextView busNumberLabel;
    private TextView busDestinationLabel;
    private TextView departureHourLabel;
    private TextView arrivalHourLabel;
    private TextView piDescriptionPlace;
    private RelativeLayout displayable;
    private StopsDAO stopsDAO = new StopsJSON();
    //private StopModel currentStop = new StopModel("7eb8580ad6b0f3b2b1158af3f9000053f2c81c7b", "LA PLANTE Marronnier", "50.449948", "4.856821");
    private Date currentTime = Calendar.getInstance().getTime();
    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location location) {
            /*try {
                currentStop = stopsDAO.getStopByCoordinates(Double.toString(location.getLatitude()), Double.toString(location.getLongitude()));
            }
            catch (JSONException e){
                Toast.makeText(MainActivity.this, getString(R.string.JSONException), Toast.LENGTH_LONG).show();
            }*/
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*try {
            LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }
        catch (SecurityException e){
            Toast.makeText(MainActivity.this, getString(R.string.error_gps_permission), Toast.LENGTH_SHORT).show();
        }*/

        new LoadBuses().execute();

        busesListView = (ListView) findViewById(R.id.listBuses);
        busesListView.setOnItemClickListener(goToSplitScreen);

        fullScreenFirstScreenBtn = (ImageButton) findViewById(R.id.fullScreenFirstScreenBtn);
        fullScreenFirstScreenBtn.setOnClickListener(goToFullScreen);

        busNumberLabel = (TextView) findViewById(R.id.busNumberLabel);
        busDestinationLabel = (TextView) findViewById(R.id.busDestinationLabel);
        departureHourLabel = (TextView) findViewById(R.id.departureHourLabel);
        arrivalHourLabel = (TextView) findViewById(R.id.arrivalHourLabel);

        displayable = (RelativeLayout) findViewById(R.id.displayable);
        piDescriptionPlace = (TextView) findViewById(R.id.piDescription);
    }

    private class LoadBuses extends AsyncTask<Void, Void, ArrayList<BusModel>> {
        private String error = "";

        @Override
        protected ArrayList<BusModel> doInBackground(Void... strings) {
            BusesIncomingDAO busesDAO = new BusesIncomingJSON();
            ArrayList<BusModel> buses = new ArrayList<>();

            try{
                buses = busesDAO.getAllBuses(CURRENT_STOP);
            }
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
                //listBuses = buses;
            }
            else{
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
            }
        }
    }

    private AdapterView.OnItemClickListener goToSplitScreen = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            displayable.setVisibility(View.VISIBLE);

            busNumberLabel.setText(R.string.busNumberLabelCropped);
            busDestinationLabel.setText(R.string.busDestinationLabelCropped);
            departureHourLabel.setText(R.string.departureHourLabelCropped);
            arrivalHourLabel.setText(R.string.arrivalHourLabelCropped);

            fullScreenFirstScreenBtn.setVisibility(View.VISIBLE);

            FragmentManager fragmentManager = getFragmentManager();
            Fragment wayDetailsFrag = new DetailsFragment();

            Bundle bundle = new Bundle();
            bundle.putSerializable("bus", (Serializable) busesListView.getItemAtPosition(position));
            wayDetailsFrag.setArguments(bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            fragmentTransaction.replace(R.id.wayDetails, wayDetailsFrag);
            fragmentTransaction.commit();
        }
    };

    private AdapterView.OnClickListener goToFullScreen = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View view) {
            displayable.setVisibility(View.GONE);

            busNumberLabel.setText(R.string.busNumberLabel);
            busDestinationLabel.setText(R.string.busDestinationLabel);
            departureHourLabel.setText(R.string.departureHourLabel);
            arrivalHourLabel.setText(R.string.arrivalHourLabel);

            piDescriptionPlace.setText(R.string.description_text_default);

            fullScreenFirstScreenBtn.setVisibility(View.GONE);
        }
    };
}
