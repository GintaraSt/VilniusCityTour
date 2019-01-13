package com.example.ginta.vilniuscitytour;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * A fragment representing a list of Items.
 */
public class ManagePlaceFragment extends Fragment {
    private ArrayList<Places> placesArrayList;
    private View.OnClickListener deleteItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag(R.id.manage_places_list_item_button);
            ManagePlacesActivity.deletePlace(placesArrayList.get(position));
            Toast.makeText(getContext(), "delete position: " + v.getTag(R.id.manage_places_list_item_button), Toast.LENGTH_SHORT).show();
        }
    };
    private View.OnClickListener editItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = (Integer) v.getTag(R.id.manage_places_list_item_button);
            ManagePlacesActivity.enterEditMode(placesArrayList.get(position));
            //ManagePlacesActivity: sate changes
            Toast.makeText(getContext(), "edit position: " + v.getTag(R.id.manage_places_list_item_button), Toast.LENGTH_SHORT).show();
        }
    };

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ManagePlaceFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_places_list, container, false);

        // Set the adapter
        if (view instanceof RecyclerView) {
            placesArrayList = ActivityMain.getPlacesList(Places.LIVE);
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view;
            recyclerView.setLayoutManager(new LinearLayoutManager(context));
            recyclerView.setAdapter(new ManageItemRecyclerViewAdapter(placesArrayList, deleteItemListener, editItemListener));
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                    DividerItemDecoration.VERTICAL));
        }
        //edit layout params, then set them
        ViewGroup.LayoutParams params = view.getLayoutParams();
        params.height = 800;
        view.setLayoutParams(params);
        //--------------------------------------------
        return view;
    }

}
