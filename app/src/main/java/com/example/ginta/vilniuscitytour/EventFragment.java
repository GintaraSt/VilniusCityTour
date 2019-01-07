package com.example.ginta.vilniuscitytour;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventFragment extends Fragment {


    public EventFragment() {
        // Required empty public constructor
    }


    private View.OnClickListener onClickListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_places, container, false);

        PlaceLocation.useUsersLocation();

        final ArrayList<Places> places = ActivityMain.getPlacesList(Places.EVENT);
        if(places == null){
            Log.e("EventFragment:  ", "failed to get places list from main activity");
        } else {
            onClickListener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = (Integer) v.getTag(R.id.list_item_button);
                    final Places places1 = places.get(position);
                    if (v.getId() == R.id.get_direction) {
                        String uri = "http://maps.google.com/maps?daddr=" + places1.getLocation().getLatitude() + "," + places1.getLocation().getLongitude();
                        Intent intent = new Intent(android.content.Intent.ACTION_VIEW,
                                Uri.parse(uri));
                        startActivity(intent);
                    }
                    if (v.getId() == R.id.website) {
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse(places1.getUrl()));
                        startActivity(intent);
                    }
                }
            };
            //set adapter
            PlacesAdapter placesAdapter = new PlacesAdapter(getActivity(), places, onClickListener);
            ListView listView = rootView.findViewById(R.id.list);
            listView.setAdapter(placesAdapter);
        }

        return rootView;
    }

}
