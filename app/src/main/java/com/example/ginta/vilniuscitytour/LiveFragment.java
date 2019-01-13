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
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class LiveFragment extends Fragment {

    private View.OnClickListener onClickListener;

    public LiveFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_places, container, false);

        PlaceLocation.useUsersLocation();

        final ArrayList<Places> places = ActivityMain.getPlacesList(Places.LIVE);
        if(places == null){
            Log.e("CultureFragment:  ", "failed to get places list from main activity");
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
                        String websites_url = places1.getUrl();
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        if(websites_url.toLowerCase().contains("http://") || websites_url.toLowerCase().contains("https://")){
                            intent.setData(Uri.parse(websites_url));
                        } else {
                            String web_url = "http://" + websites_url;
                            intent.setData(Uri.parse(web_url));
                        }
                        try{
                            startActivity(intent);
                        } catch (Exception e){
                            Toast.makeText(getContext(), R.string.invalid_url, Toast.LENGTH_SHORT).show();
                            e.printStackTrace();
                        }
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
