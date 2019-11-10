package unamur.buspanelapp.Activity;

import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Toast;

import org.json.JSONException;

import java.util.ArrayList;

import unamur.buspanelapp.DAO.PointsOfInterestDAO;
import unamur.buspanelapp.DAO.PointsOfInterestJSON;
import unamur.buspanelapp.Model.BusModel;
import unamur.buspanelapp.Model.Custom_PointOfInterestLine_Adapter;
import unamur.buspanelapp.Model.PointOfInterestModel;
import unamur.buspanelapp.Model.StopModel;
import unamur.buspanelapp.R;

public class DetailsFragment extends Fragment {
    private BusModel bus;
    private ArrayList<PointOfInterestModel> listPointsOfInterest = new ArrayList<>();
    private ListView piListView;
    private View view;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        bus = (BusModel) getArguments().get("bus");

        view = inflater.inflate(R.layout.way_details_fragment, container, false);

        piListView = view.findViewById(R.id.pointsOfInterestLine);

        new LoadPointsOfInterest().execute();
        //piListView.setOnItemClickListener(goToSplitScreen);

        return view;
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
                    //points.addAll(busesDAO.getPointsOfInterestAround(currentStop.getCoordX(), currentStop.getCoordY()));
                }
            }
            /*catch (GetAllPointsOfInterestException e){
                error = getString(R.string.error_get_all_points_of_interest_exception);
            }*/
            catch (JSONException e){
                error = getString(R.string.JSONException);
            }

            return points;
        }

        @Override
        protected void onPostExecute(ArrayList<PointOfInterestModel> points) {
            super.onPostExecute(points);
            if(error.equals("")) {
                /*LinearLayout linearLayout =  view.findViewById(R.id.wayDetailsFragment);
                for(int i = 0; i < points.size(); i += 4) {
                    ListView listView = new ListView(view.getContext());
                    listView.setAdapter(new Custom_PointOfInterestLine_Adapter(getActivity(), new ArrayList<>(points.subList(i, (((i + 4) > points.size()) ? points.size() : i + 4)))));
                    linearLayout.addView(listView);
                }*/
                ArrayList<ArrayList<PointOfInterestModel>> displayPoints = new ArrayList<>();
                for(int i = 0; i < points.size(); i += 4) {
                    displayPoints.add(new ArrayList<>(points.subList(i, (((i + 4) > points.size()) ? points.size() : i + 4))));
                }

                piListView.setAdapter(new Custom_PointOfInterestLine_Adapter(getActivity(), displayPoints/*new ArrayList<>(points.subList(0,3))*/));

                listPointsOfInterest = points;
            }
            else{
                Toast.makeText(getActivity(), error, Toast.LENGTH_LONG).show();
            }
        }
    }
}