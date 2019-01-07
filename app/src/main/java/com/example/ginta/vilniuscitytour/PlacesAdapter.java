package com.example.ginta.vilniuscitytour;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.net.FileNameMap;
import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class PlacesAdapter extends ArrayAdapter<Places> {

    private static int Tag = -1;

    private View.OnClickListener onClickListener;
    //constructor
    public PlacesAdapter(Context activity, ArrayList<Places> places, View.OnClickListener clickListener){
        super(activity, 0, places);
        onClickListener = clickListener;
    }

    /**
     *
     *@param position the data, that should be displayed in the view, position in list
     * @param v the recycled view to populate - view from list  or grid views stack
     * @param parent parent view to which v will be assigned
     * @return
     */
    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        final Places currentPlace = getItem(position);

        View listItemView = v;
        if(listItemView == null){
            //if there's no free views in stack, create new view by converting layout to view object
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        int lightColor = currentPlace.getLightColor(currentPlace.getPlaceTypeConst());
        int darkColor = currentPlace.getDarkColor(currentPlace.getPlaceTypeConst());
        //set background color for place info view
        listItemView.findViewById(R.id.place_info).setBackgroundColor(ContextCompat.getColor(ActivityMain.getContext(), lightColor));
        String temp;
        //set title of place, and background color of view
        TextView title = (TextView) listItemView.findViewById(R.id.place_title);
        title.setText(currentPlace.getTitle());
        title.setBackgroundColor(ContextCompat.getColor(ActivityMain.getContext(), darkColor));
        //set type of place
        TextView type = (TextView) listItemView.findViewById(R.id.place_type);
        type.setBackgroundColor(ContextCompat.getColor(ActivityMain.getContext(), lightColor));
        type.setText(currentPlace.getPlaceType());
        //set price of place
        TextView price = (TextView) listItemView.findViewById(R.id.price);
        temp = "Price: " + currentPlace.getPriceMeaning();
        price.setText(temp);
        //set rating of place
        float rating = currentPlace.getRating();
        TextView ratingNone = (TextView) listItemView.findViewById(R.id.rating_none);
        LinearLayout ratingLayout = (LinearLayout) listItemView.findViewById(R.id.rating_stars);
        if(rating < 0){
            ratingLayout.setVisibility(LinearLayout.GONE);
            ratingNone.setVisibility(View.VISIBLE);
        }
        else{
            ratingLayout.setVisibility(LinearLayout.VISIBLE);
            ratingNone.setVisibility(View.GONE);

            ImageView image;
            for(int i=0; i<5; i++) {
                //rating stars ImageViews id's goes in a row starting from 0x7f08008c and finishing on 0x7f080091
                //because of that it should be possible to increase this hex number by i, and get next ImageView
                image = (ImageView) listItemView.findViewById(R.id.rating_1 + i);
                if (rating <= 0.25 + i) image.setImageResource(R.drawable.sharp_star_border_24);
                else if (rating <= 0.75 + i) image.setImageResource(R.drawable.sharp_star_half_24);
                else image.setImageResource(R.drawable.sharp_star_24);
            }
        }

        //set websites url
        TextView url = listItemView.findViewById(R.id.websites_url);
        LinearLayout website = (LinearLayout) listItemView.findViewById(R.id.website);
        if(currentPlace.getUrl() != null) {
            website.setVisibility(LinearLayout.VISIBLE);
            website.setBackgroundColor(ContextCompat.getColor(ActivityMain.getContext(), darkColor));
            url.setText(currentPlace.getUrl());
        }
        else website.setVisibility(LinearLayout.GONE);
        //set image
        ImageView image = (ImageView) listItemView.findViewById(R.id.place_image);
        if(currentPlace.getAmountOfImages() > 0) {
            image.setVisibility(View.VISIBLE);
            image.setImageResource(currentPlace.getImageResourceId(0));
        }
        else image.setVisibility(View.GONE);
        //set address
        TextView address = (TextView) listItemView.findViewById(R.id.address_line);
        address.setText(currentPlace.getAddress());
        //set distance from user
        TextView distanceFromUser = listItemView.findViewById(R.id.distance_from_current_location);
        distanceFromUser.setText(currentPlace.getDistanceFromUser());

        //set tags and click listeners
        listItemView.findViewById(R.id.get_direction).setOnClickListener(onClickListener);
        listItemView.findViewById(R.id.get_direction).setTag(R.id.list_item_button, position);
        listItemView.findViewById(R.id.other_info).setOnClickListener(onClickListener);
        listItemView.findViewById(R.id.other_info).setTag(R.id.list_item_button, position);
        listItemView.findViewById(R.id.website).setOnClickListener(onClickListener);
        listItemView.findViewById(R.id.website).setTag(R.id.list_item_button, position);

        return listItemView;
    }
}
