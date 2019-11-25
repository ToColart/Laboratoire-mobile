package unamur.buspanelapp.Activity;

import android.app.Fragment;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;

import unamur.buspanelapp.DAO.PointsOfInterestDAO;
import unamur.buspanelapp.DAO.PointsOfInterestJSON;
import unamur.buspanelapp.Model.BusModel;
import unamur.buspanelapp.Model.PointOfInterestModel;
import unamur.buspanelapp.Model.StopModel;
import unamur.buspanelapp.R;

import static unamur.buspanelapp.Constants.CURRENT_STOP;

public class DetailsFragment extends Fragment {
    private BusModel bus;
    private GridLayout gridview;
    private TextView piDescription;
    private PointsOfInterestDAO pointsOfInterestDAO = new PointsOfInterestJSON();
    private ImageButton fullScreenFirstScreenBtn;
    private int seconds = 0;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bus = (BusModel) getArguments().get("bus");
        piDescription = (TextView) getActivity().findViewById(R.id.piDescription);
        fullScreenFirstScreenBtn = getActivity().findViewById(R.id.fullScreenFirstScreenBtn);

        gridview = (GridLayout) inflater.inflate(R.layout.way_details_fragment, container, false);

        new LoadPointsOfInterest().execute();

        return gridview;
    }

    private class LoadPointsOfInterest extends AsyncTask<Void, Void, ArrayList<PointOfInterestModel>> {
        private String error = "";

        @Override
        protected ArrayList<PointOfInterestModel> doInBackground(Void... strings) {
            PointsOfInterestDAO busesDAO = new PointsOfInterestJSON();
            ArrayList<PointOfInterestModel> points = new ArrayList<>();

            try{
                ArrayList<PointOfInterestModel> pointsToAdd;
                for(StopModel currentStop: bus.getStopsList()){
                    pointsToAdd = busesDAO.getPointsOfInterestAround(currentStop.getCoordX(), currentStop.getCoordY());
                    for(PointOfInterestModel pi : pointsToAdd){
                        if(!points.contains(pi))
                            points.add(pi);
                    }
                }
            }
            catch (JSONException e){
                error = getString(R.string.JSONException);
            }

            return points;
        }

        @Override
        protected void onPostExecute(ArrayList<PointOfInterestModel> points) {
            super.onPostExecute(points);
            if(error.equals("")) {
                int i = 0;
                for(PointOfInterestModel pi:points) {
                    ImageView pic = new ImageView(gridview.getContext());
                    Picasso
                            .with(gridview.getContext())
                            .load(pi.getPicture())
                            //.resize(400, 230) //for phone
                            .resize(180,120) //tablet
                            .centerCrop()
                            .noFade()
                            .into(pic);
                    setOnClick(pic, pi);
                    //pic.setPadding(15,40,15,40); //phone
                    pic.setPadding(15,20,15,20); //tablet

                    gridview.addView(pic);

                    i = (i==2)? 0 : i+1;
                }

                timeout.start();
            }
            else{
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        }
    }

    private void setOnClick(final ImageView pic, final PointOfInterestModel pi){
        pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gridview.setSelected(false);
                for(int i = 0; i < gridview.getChildCount(); i++){
                    if(gridview.getChildAt(i).isSelected()) {
                        gridview.getChildAt(i).setSelected(false);
                        gridview.getChildAt(i).setBackgroundColor(Color.parseColor("#FAFAFA"));
                        break;
                    }
                }

                pic.setSelected(true);
                piDescription.setText(String.format("%s\n%s", pi.getDescription(), getString(R.string.description_text_pi_selected)));
                seconds = 0;
                pic.setBackgroundColor(Color.parseColor("#58ACFA"));
                new PutSelectedPointOfInterest(CURRENT_STOP.getId(), pi.getId()).execute();
            }
        });
    }

    private class PutSelectedPointOfInterest extends AsyncTask<String, Void, String> {

        private String stopId;
        private int piId;

        PutSelectedPointOfInterest(String stopId, int piId){
            this.stopId = stopId;
            this.piId = piId;
        }

        protected String doInBackground(String... strings) {
            return pointsOfInterestDAO.putSelectedPointOfInterest(stopId, piId);
        }
    }

    private Thread timeout = new Thread()
    {
        @Override
        public void run()
        {
            try {
                while (!Thread.interrupted()) {
                    Thread.sleep(1000);
                    seconds++;

                    if (seconds == 60) {
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                fullScreenFirstScreenBtn.performClick();
                            }
                        });
                        this.interrupt();
                    }
                }
            }
            catch (InterruptedException e){
                System.out.println("damn lost son"+e.getMessage());
            }
        }
    };
}