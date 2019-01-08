package com.example.ginta.vilniuscitytour;

import android.media.Image;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ginta.vilniuscitytour.dummy.DummyContent.DummyItem;

import java.util.ArrayList;
import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link DummyItem} and makes a call to the
 */
public class ManageItemRecyclerViewAdapter extends RecyclerView.Adapter<ManageItemRecyclerViewAdapter.ViewHolder> {

    private final ArrayList<Places> placesArrayList;
    private final View.OnClickListener deleteButton;
    private final View.OnClickListener editButton;

    public ManageItemRecyclerViewAdapter(ArrayList<Places> placesArrayList, View.OnClickListener deleteButton, View.OnClickListener editButton) {
        this.placesArrayList = placesArrayList;
        this.deleteButton = deleteButton;
        this.editButton = editButton;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_place_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = placesArrayList.get(position);
        holder.titleTextView.setText(placesArrayList.get(position).getTitle());
        holder.typeTextView.setText(placesArrayList.get(position).getPlaceType());
        holder.edit.setOnClickListener(editButton);
        holder.edit.setTag(R.id.manage_places_list_item_button, position);
        holder.delete.setOnClickListener(deleteButton);
        holder.delete.setTag(R.id.manage_places_list_item_button, position);
    }

    @Override
    public int getItemCount() {
        return placesArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView titleTextView;
        public final TextView typeTextView;
        public final ImageView edit;
        public final ImageView delete;
        public Places mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            titleTextView = (TextView) view.findViewById(R.id.manage_place_item_title);
            typeTextView = (TextView) view.findViewById(R.id.manage_place_item_type);
            edit = (ImageView) view.findViewById(R.id.edit_place);
            delete = (ImageView) view.findViewById(R.id.delete_place);
        }

        @Override
        public String toString() {
            return super.toString() + " 'title: " + titleTextView.getText() + ", type: "+ typeTextView.getText() + "'";
        }
    }
}
