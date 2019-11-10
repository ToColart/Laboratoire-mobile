package unamur.buspanelapp.Model;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import unamur.buspanelapp.R;

public class Custom_PointOfInterestLine_Adapter extends ArrayAdapter<ArrayList<PointOfInterestModel>> {

    //private ArrayList<PointOfInterestModel> list_pi;
    //private static int imagePosition = 1;
    private ArrayList<PointOfInterestModel> pis = new ArrayList<>();
    private View shapeOfList;

    public Custom_PointOfInterestLine_Adapter(@NonNull Context context, @NonNull ArrayList<ArrayList<PointOfInterestModel>> list_home_menu) {
        super(context, R.layout.points_of_interest_lines, list_home_menu);
    }

    @NonNull
    @Override
    public View getView(int position, View view, @NonNull ViewGroup viewGroup) {
        LayoutInflater layout = LayoutInflater.from(getContext());
        shapeOfList = layout.inflate(R.layout.points_of_interest_lines, viewGroup, false);

        pis = getItem(position);

        int i = 0;
        for(final PointOfInterestModel pi:pis) {
            if (pi != null) {
                ImageView pic = new ImageView(getContext());
                switch (i) {
                    case 0:
                        pic = shapeOfList.findViewById(R.id.piPic1);
                        break;
                    case 1:
                        pic = shapeOfList.findViewById(R.id.piPic2);
                        break;
                    case 2:
                        pic = shapeOfList.findViewById(R.id.piPic3);
                        break;
                    case 3:
                        pic = shapeOfList.findViewById(R.id.piPic4);
                        break;
                }
                i++;
                //imagePosition = (imagePosition<4)?imagePosition+1:1;

                if (pic != null) {
                    Picasso
                            .with(shapeOfList.getContext())
                            .load(pi.getPicture())
                            .into(pic);
                    pic.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            TextView descr = shapeOfList.findViewById(R.id.selectedPiDescr);
                            descr.setText(pi.getDescription());
                        }
                    });
                }
            }
        }
        return shapeOfList;
    }

    /*private AdapterView.OnClickListener displayDescr = new AdapterView.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };*/
}
