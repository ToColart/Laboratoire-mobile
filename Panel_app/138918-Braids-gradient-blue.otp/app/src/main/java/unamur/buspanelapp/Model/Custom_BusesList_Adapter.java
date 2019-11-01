package unamur.buspanelapp.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import unamur.buspanelapp.R;

public class Custom_BusesList_Adapter extends ArrayAdapter<BusModel> {
    public Custom_BusesList_Adapter(@NonNull Context context, @NonNull ArrayList<BusModel> list_home_menu) {
        super(context, R.layout.buses_list_display, list_home_menu);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup viewGroup) {
        LayoutInflater layout = LayoutInflater.from(getContext());
        View shapeOfList = layout.inflate(R.layout.buses_list_display, viewGroup, false);
        BusModel bus = getItem(position);

        if(bus != null && bus.getStopsList() != null && !bus.getStopsList().isEmpty()) {
            TextView busNumber = shapeOfList.findViewById(R.id.busNumber);
            busNumber.setText(Integer.toString(bus.getNumber()));

            TextView lastStop = shapeOfList.findViewById(R.id.busDestination);
            lastStop.setText(bus.getStopsList().get(bus.getStopsList().size() - 1).getName());

            //TODO add liste des stops parcourus par le bus sous la destination finale

            TextView departureHour = shapeOfList.findViewById(R.id.departureHour);
            departureHour.setText(bus.getDepartureHour());

            TextView arrivalHour = shapeOfList.findViewById(R.id.arrivalHour);
            arrivalHour.setText(bus.getArrivalHour());
        }

        return shapeOfList;
    }
}