package unamur.buspanelapp.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

import unamur.buspanelapp.Activity.MainActivity;
import unamur.buspanelapp.R;

public class Custom_BusesList_Adapter extends ArrayAdapter<BusModel> {
    private BusModel bus;

    public Custom_BusesList_Adapter(@NonNull Context context, @NonNull ArrayList<BusModel> list_home_menu) {
        super(context, R.layout.buses_list_display, list_home_menu);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup viewGroup) {
        if(view == null) {
            LayoutInflater layout = LayoutInflater.from(getContext());
            view = layout.inflate(R.layout.buses_list_display, viewGroup, false);

            TextView busNumber = view.findViewById(R.id.busNumber);
            TextView lastStop = view.findViewById(R.id.busDestination);
            Button goToSplitScreenBtn = view.findViewById(R.id.goToSplitScreenBtn);
            TextView stopsList = view.findViewById(R.id.busStopsList);
            TextView departureHour = view.findViewById(R.id.departureHour);
            TextView arrivalHour = view.findViewById(R.id.arrivalHour);

            BusView busView = new BusView(busNumber, lastStop, stopsList, departureHour, arrivalHour, goToSplitScreenBtn);
            view.setTag(busView);
        }

        bus = getItem(position);

        if(bus != null && bus.getStopsList() != null && !bus.getStopsList().isEmpty()) {
            BusView busView = (BusView) view.getTag();
            busView.getNumber().setText(String.format(Locale.FRENCH, "%d", bus.getNumber()));
            ((View) busView.getNumber().getParent()).setPadding(0,10,0,0);

            busView.getFinalDestination().setText(bus.getStopsList().get(bus.getStopsList().size() - 1).getName());

            busView.getGoToSplitScreenBtn().setTag(position);

            //LinearLayout tt = shapeOfList.findViewById(R.id.stopsListOfPath);
            StringBuilder stops = new StringBuilder();
            for(StopModel stop:bus.getStopsList()){
                //TextView t = new TextView(getContext());
                if(bus.getStopsList().indexOf(stop) == 0){
                    //t.setText(stop.getName());
                    //t.setBackgroundColor(Color.parseColor("#EFB722"));
                    stops.append(stop.getName());
                }
                else {
                    //t.setText(String.format(" | %s", stop.getName()));
                    //t.setBackgroundColor(Color.parseColor("#EFB722"));
                    stops.append(" | ").append(stop.getName());
                }
               // t.setLayoutParams(new LinearLayout.LayoutParams(
                       // LinearLayout.LayoutParams.WRAP_CONTENT,
                       // LinearLayout.LayoutParams.WRAP_CONTENT));
                //tt.addView(t);
            }

            final TextView stopsList = busView.getStopsList();
            stopsList.setText(stops);
            stopsList.setMaxLines(2);
            stopsList.setLines(bus.getStopsList().size());
            stopsList.setHeight(2*stopsList.getLineHeight()+10);
            stopsList.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {

                @Override
                public boolean onPreDraw() {
                    // Remove listener because we don't want this called before _every_ frame
                    stopsList.getViewTreeObserver().removeOnPreDrawListener(this);

                    stopsList.setTag(stopsList.getLineCount());
                    stopsList.setMaxLines(2);
                    stopsList.setLines(2);
                    LinearLayout stopListParent = (LinearLayout) stopsList.getParent();
                    ViewGroup.LayoutParams params = stopListParent.getLayoutParams();
                    params.height = stopsList.getMaxLines() * stopsList.getLineHeight() + 10;
                    stopListParent.setLayoutParams(params);
                    stopListParent.requestLayout();

                    return true; // true because we don't want to skip this frame
                }
            });

            busView.getDepartureHour().setText(bus.getDepartureHour());

            busView.getArrivalHour().setText(bus.getArrivalHour());
        }

        return view;
    }

    private class BusView {
        private TextView number;
        private TextView finalDestination;
        private TextView stopsList;
        private TextView departureHour;
        private TextView arrivalHour;
        private Button goToSplitScreenBtn;

        public BusView() {
        }

        public BusView(TextView number, TextView finalDestination, TextView stopsList, TextView departureHour, TextView arrivalHour, Button goToSplitScreenBtn) {
            this.setNumber(number);
            this.setFinalDestination(finalDestination);
            this.setStopsList(stopsList);
            this.setDepartureHour(departureHour);
            this.setArrivalHour(arrivalHour);
            this.setGoToSplitScreenBtn(goToSplitScreenBtn);
        }

        public TextView getNumber() {
            return number;
        }

        public void setNumber(TextView number) {
            this.number = number;
        }

        public TextView getFinalDestination() {
            return finalDestination;
        }

        public void setFinalDestination(TextView finalDestination) {
            this.finalDestination = finalDestination;
        }

        public TextView getStopsList() {
            return stopsList;
        }

        public void setStopsList(TextView stopsList) {
            this.stopsList = stopsList;
        }

        public TextView getDepartureHour() {
            return departureHour;
        }

        public void setDepartureHour(TextView departureHour) {
            this.departureHour = departureHour;
        }

        public TextView getArrivalHour() {
            return arrivalHour;
        }

        public void setArrivalHour(TextView arrivalHour) {
            this.arrivalHour = arrivalHour;
        }

        public Button getGoToSplitScreenBtn() {
            return goToSplitScreenBtn;
        }

        public void setGoToSplitScreenBtn(Button goToSplitScreenBtn) {
            this.goToSplitScreenBtn = goToSplitScreenBtn;
        }
    }
}