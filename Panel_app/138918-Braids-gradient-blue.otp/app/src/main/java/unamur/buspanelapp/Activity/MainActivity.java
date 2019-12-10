package unamur.buspanelapp.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import unamur.buspanelapp.DAO.BusesIncomingDAO;
import unamur.buspanelapp.DAO.BusesIncomingJSON;
import unamur.buspanelapp.DAO.StopsDAO;
import unamur.buspanelapp.DAO.StopsJSON;
import unamur.buspanelapp.Model.BusModel;
import unamur.buspanelapp.Model.Custom_BusesList_Adapter;
import unamur.buspanelapp.R;

import static unamur.buspanelapp.Constants.CURRENT_STOP;
import static unamur.buspanelapp.Constants.NUMBER_BUSES_DISPLAYED;
import static unamur.buspanelapp.Constants.NUMBER_LINES_STOPS_DISPLAYED;
import static unamur.buspanelapp.Constants.URL_API_PointsOfInterest_BASE;

public class MainActivity extends AppCompatActivity {
    private ListView busesListView;
    private ImageButton fullScreenFirstScreenBtn;
    private TextView busNumberLabel;
    private TextView busDestinationLabel;
    private TextView departureHourLabel;
    private TextView arrivalHourLabel;
    private TextView piDescriptionPlace;
    private RelativeLayout displayable;
    private ArrayList<BusModel> allBuses;
    private ArrayList<BusModel> listBuses;
    private StopsDAO stopsDAO = new StopsJSON();
    private Custom_BusesList_Adapter busesAdapter;
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
    private Integer selectedWayPosition;

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
        busesListView.setOnItemClickListener(showMoreOrLess);

        fullScreenFirstScreenBtn = (ImageButton) findViewById(R.id.fullScreenFirstScreenBtn);
        fullScreenFirstScreenBtn.setOnClickListener(goToFullScreen);

        busNumberLabel = (TextView) findViewById(R.id.busNumberLabel);
        busDestinationLabel = (TextView) findViewById(R.id.busDestinationLabel);
        /*FOR DEVELOPMENT*/
        busDestinationLabel.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                final EditText editText = new EditText(MainActivity.this);
                editText.setLayoutParams(new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 6f));

                final LinearLayout busDestinationLabelParent = ((LinearLayout) busDestinationLabel.getParent());
                //busDestinationLabelParent.removeView(busDestinationLabel);
                busDestinationLabel.setVisibility(View.GONE);
                busDestinationLabelParent.addView(editText, 2);
                editText.setFocusable(true);
                editText.requestFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editText, InputMethodManager.SHOW_IMPLICIT);

                View.OnFocusChangeListener listener = new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(!hasFocus) {
                            if(!editText.getText().toString().equals(""))
                                URL_API_PointsOfInterest_BASE = "http://" + editText.getText().toString() + ":9000/";
                            busDestinationLabelParent.removeView(editText);
                            busDestinationLabel.setVisibility(View.VISIBLE);
                        }
                    }
                };

                editText.setOnFocusChangeListener(listener);

                return false;
            }
        });
        departureHourLabel = (TextView) findViewById(R.id.departureHourLabel);
        arrivalHourLabel = (TextView) findViewById(R.id.arrivalHourLabel);

        displayable = (RelativeLayout) findViewById(R.id.displayable);
        piDescriptionPlace = (TextView) findViewById(R.id.piDescription);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                v.clearFocus();
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
            }
        }

        return super.dispatchTouchEvent(event);
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
                allBuses = buses;
                listBuses = new ArrayList<>(buses.subList(0, NUMBER_BUSES_DISPLAYED > buses.size() ? buses.size() : NUMBER_BUSES_DISPLAYED));
                busesAdapter = new Custom_BusesList_Adapter(MainActivity.this, listBuses);
                busesListView.setAdapter(busesAdapter);
                reloadBuses.start();
            }
            else{
                Toast.makeText(MainActivity.this, error, Toast.LENGTH_LONG).show();
            }
        }
    }

    private AdapterView.OnItemClickListener showMoreOrLess = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            final TextView stopsList = view.findViewById(R.id.busStopsList);
            int numberOfLines = (int) stopsList.getTag();
            if(numberOfLines > 2) {
                if (stopsList.getMaxLines() == NUMBER_LINES_STOPS_DISPLAYED) {
                    stopsList.setMaxLines(numberOfLines);
                    stopsList.setLines(numberOfLines);
                } else {
                    stopsList.setMaxLines(NUMBER_LINES_STOPS_DISPLAYED);
                    stopsList.setLines(NUMBER_LINES_STOPS_DISPLAYED);
                }
                LinearLayout stopListParent = (LinearLayout) stopsList.getParent();
                ViewGroup.LayoutParams params = stopListParent.getLayoutParams();
                params.height = stopsList.getMaxLines() * stopsList.getLineHeight() + 10;
                stopListParent.setLayoutParams(params);
                stopListParent.requestLayout();
            }
        }
    };

    private AdapterView.OnClickListener goToFullScreen = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View view) {
            displayable.setVisibility(View.GONE);

            busNumberLabel.setText(R.string.busNumberLabel);
            ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            busNumberLabel.setLayoutParams(lp);
            busDestinationLabel.setText(R.string.busDestinationLabel);
            departureHourLabel.setText(R.string.departureHourLabel);
            arrivalHourLabel.setText(R.string.arrivalHourLabel);

            piDescriptionPlace.setText(R.string.description_text_default);

            selectedWayPosition = null;

            fullScreenFirstScreenBtn.setVisibility(View.GONE);
        }
    };

    public void goToSplitScreen(View view) {
        displayable.setVisibility(View.VISIBLE);

        busNumberLabel.setText(R.string.busNumberLabelCropped);
        ViewGroup.LayoutParams lp = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1f);
        busNumberLabel.setLayoutParams(lp);
        busDestinationLabel.setText(R.string.busDestinationLabelCropped);
        departureHourLabel.setText(R.string.departureHourLabelCropped);
        arrivalHourLabel.setText(R.string.arrivalHourLabelCropped);

        piDescriptionPlace.setText(R.string.description_text_default);

        fullScreenFirstScreenBtn.setVisibility(View.VISIBLE);

        BusModel bus = (BusModel) busesListView.getItemAtPosition((int) view.getTag());
        selectedWayPosition = (int) view.getTag();

        FragmentManager fragmentManager = getFragmentManager();
        Fragment newFragment;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        newFragment = new DetailsFragment();

        Bundle bundle = new Bundle();
        bundle.putSerializable("bus", bus);
        newFragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.wayDetails, newFragment);
        fragmentTransaction.commit();
    }

    private Thread reloadBuses = new Thread()
    {
        @Override
        public void run()
        {
            try {
                while (!Thread.interrupted()) {
                    int minutesBeforeNextBus = Integer.parseInt(listBuses.get(0).getDepartureHour().substring(3,5)) - currentTime.getMinutes();
                    int hoursBeforeNextBus = Integer.parseInt(listBuses.get(0).getDepartureHour().substring(0,2)) - currentTime.getHours();
                    if(minutesBeforeNextBus<0)
                        minutesBeforeNextBus += 60;
                    if(hoursBeforeNextBus<0)
                        hoursBeforeNextBus += 24;
                    int secondsBeforeNextBus = minutesBeforeNextBus * 60 + hoursBeforeNextBus * 60 * 60;
                    int millisBeforeNextBus = secondsBeforeNextBus * 1000;
                    Thread.sleep(millisBeforeNextBus);

                    reSortBuses();
                }
            }
            catch (InterruptedException e){
                System.out.println("Thread failed "+e.getMessage());
            }
        }
    };

    private void reSortBuses(){
        BusModel busToPutToEnd = allBuses.get(0);
        allBuses.remove(0);
        allBuses.add(busToPutToEnd);

        listBuses = new ArrayList<>(allBuses.subList(0, NUMBER_BUSES_DISPLAYED > allBuses.size() ? allBuses.size() : NUMBER_BUSES_DISPLAYED));

        MainActivity.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                busesListView.setAdapter(new Custom_BusesList_Adapter(MainActivity.this, listBuses));
                if(selectedWayPosition != null && selectedWayPosition == 0){
                    fullScreenFirstScreenBtn.performClick();
                }
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        reloadBuses.interrupt();
    }

    @Override
    public void onStop() {
        super.onStop();
        reloadBuses.interrupt();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        reloadBuses.interrupt();
    }
}
