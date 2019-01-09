package com.example.ginta.vilniuscitytour;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ginta.vilniuscitytour.dummy.DummyContent;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ManagePlacesActivity extends AppCompatActivity {
    public final static String filename = "places.dat";
    //Todo: find a solution without creating static Context and ScrollView variables
    private static Context mContext;
    private static ScrollView scrollView;
    private boolean isFragmentDisplayed = false;
    private static boolean dataWasDeleted = false;
    private int rating = -1;
    private int placeType = -1;
    private static StringBuilder dataBackup = new StringBuilder();
    private View.OnClickListener undoOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Toast.makeText(ManagePlacesActivity.this, R.string.undone, Toast.LENGTH_SHORT).show();
            ManagePlacesActivity.restoreDataFileFromBackup();
            ActivityMain.updatePlaces();
        }
    };
    private View.OnClickListener managePlaceListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            ManagePlaceFragment managePlaceFragment;
            FragmentManager fragmentManager = getSupportFragmentManager();
            Button managePlacesButton = (Button) findViewById(R.id.manage_place);
            if(!isFragmentDisplayed) {
                managePlaceFragment = new ManagePlaceFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.manage_places_container, managePlaceFragment).addToBackStack(null).commit();
                isFragmentDisplayed = true;
                managePlacesButton.setText(R.string.done_managing);
            } else {
                 managePlaceFragment = (ManagePlaceFragment) fragmentManager
                        .findFragmentById(R.id.manage_places_container);
                if (managePlaceFragment != null) {
                    FragmentTransaction fragmentTransaction =
                            fragmentManager.beginTransaction();
                    fragmentTransaction.remove(managePlaceFragment).commit();
                } else {
                    Toast.makeText(ManagePlacesActivity.this, "Failed to find fragment", Toast.LENGTH_SHORT).show();
                }
                isFragmentDisplayed = false;
                managePlacesButton.setText(R.string.manage_place);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_places);
        mContext = getApplicationContext();
        scrollView = (ScrollView) findViewById(R.id.scrollView);
        //all bellow methods sets on click listeners for specific fields to do needed actions
        setRating();        //set rating by taping on stars image views
        addPlace();         //add place if all required fields was filled
        setType();          //set type by taping on type
        resetDataFile();    //erase all data in places file
        Button managePlacesButton = (Button) findViewById(R.id.manage_place);
        managePlacesButton.setOnClickListener(managePlaceListener);
    }

    private void addPlace(){
        Button addPlace = (Button) findViewById(R.id.add_place);
        addPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File places = new File(filename);
                String data = getData();
                if(data == null) return;

                if (!places.exists()) places = new File(getApplicationContext().getFilesDir(), filename);
                //create backup before adding place
                createDataFileBackup();
                BufferedWriter placesData = null;
                try {
                    placesData = new BufferedWriter(new FileWriter(places, true));
                    placesData.write(data);
                    placesData.close();
                    Log.v("ManagePlacesActivity:  ", "write successful");
                } catch (IOException e){
                    Log.e("ManagePlacesActivity:  ", "file not found - " + filename + " - failed to write");
                    e.printStackTrace();
                } finally {
                    if(placesData != null){
                        try {
                            placesData.close();
                        } catch (IOException e){
                            e.printStackTrace();
                        }
                    }
                }

                Snackbar.make(findViewById(R.id.scrollView), R.string.place_adding_successful, BaseTransientBottomBar.LENGTH_LONG)
                        .setAction(R.string.undo, undoOnClickListener).show();
                ActivityMain.updatePlaces();

            }
        });
    }

    private void setRating(){
        ImageView star;
        for(int i=0; i<5; i++){
            final int k = i;
            star = (ImageView) findViewById(R.id.rating_1 + i);
            if(star != null) {
                star.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(rating == k + 1) rating--;
                        else rating = k + 1;
                        setStars();
                    }
                });
            } else{
                Log.e("setRating", ":   failed to get view at position - " + i);
            }
        }
    }

    private void setStars(){
        ImageView image;
        for(int i=0; i<5; i++) {
            //rating stars ImageViews id's goes in a row starting from 0x7f08008c and finishing on 0x7f080091
            //because of that it should be possible to increase this hex number by i, and get next ImageView
            image = (ImageView) findViewById(R.id.rating_1 + i);
            if(image != null) {
                if(i >= rating) image.setImageResource(R.drawable.sharp_star_border_24);
                else image.setImageResource(R.drawable.sharp_star_24);
            } else {
                Log.e("setSar", ":   failed to get view at position - " + i);
            }
        }
    }

    private void setType(){
        final TextView type = (TextView) findViewById(R.id.select_type);
        type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                placeType++;
                if(placeType > 3) placeType = 0;
                type.setText(Places.getCustomPlaceType(placeType));
                findViewById(R.id.add_title).setBackgroundColor(ContextCompat.getColor(ManagePlacesActivity.this, Places.getDarkColor(placeType)));
                type.setBackgroundColor(ContextCompat.getColor(ManagePlacesActivity.this, Places.getLightColor(placeType)));
                findViewById(R.id.website).setBackgroundColor(ContextCompat.getColor(ManagePlacesActivity.this, Places.getDarkColor(placeType)));
                findViewById(R.id.place_info).setBackgroundColor(ContextCompat.getColor(ManagePlacesActivity.this, Places.getLightColor(placeType)));
            }
        });
    }

    @Nullable
    private String getData(){
        String temp = "";
        String data;
        //AddPlaceType
        temp += placeType;
        if(placeType != -1) data = temp + "\n";
        else {
            Toast.makeText(ManagePlacesActivity.this, R.string.please_select_type, Toast.LENGTH_SHORT).show();
            return null;
        }
        //Add Place Title
        EditText editText = (EditText) findViewById(R.id.add_title);
        temp = editText.getText().toString();
        if(!temp.isEmpty()) data += temp + "\n";
        else {
            Toast.makeText(ManagePlacesActivity.this, R.string.please_add_title, Toast.LENGTH_SHORT).show();
            return null;
        }
        //Add Place Address
        editText = (EditText) findViewById(R.id.add_address_line);
        temp = editText.getText().toString();
        if(!temp.isEmpty()) data += temp + "\n";
        else {
            Toast.makeText(ManagePlacesActivity.this, R.string.please_add_address, Toast.LENGTH_SHORT).show();
            return null;
        }
        //Add Place Pricing
        editText = (EditText) findViewById(R.id.set_price);
        temp = editText.getText().toString();
        if(!temp.isEmpty()) data += temp + "\n";
        else {
            Toast.makeText(ManagePlacesActivity.this, R.string.please_add_price, Toast.LENGTH_SHORT).show();
            return null;
        }
        //Add Place Website
        editText = (EditText) findViewById(R.id.set_websites_url);
        temp = editText.getText().toString();
        if(!temp.isEmpty()) data += temp + "\n";
        else data += "no_website\n";
        //Add Place Rating
        if (rating == -1) data += "not_rated\n";
        else data += rating + "\n";
        rating = -1;

        return data;
    }

    private void resetDataFile(){
        Button eraseButton = (Button) findViewById(R.id.erase_data);
        eraseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //create backup of file before deleting
                createDataFileBackup();
                //delete file
                eraseFileData();
                Snackbar.make(findViewById(R.id.scrollView), R.string.places_data_erased, Snackbar.LENGTH_LONG)
                        .setAction(R.string.undo, undoOnClickListener).show(); //if undo pressed call undo method to restore file from backup

                dataWasDeleted = true;
            }
        });
    }

    private static void eraseFileData(){
        File places = new File(mContext.getFilesDir(), filename);
        try {
            //open file
            BufferedWriter placesData = new BufferedWriter(new FileWriter(places));
            //close it to delete all content inside
            placesData.close();
        } catch (IOException e) {
            Log.e("ManagePlacesActivity:  ", "failed to erase file data");
            e.printStackTrace();
        }
    }

    private static void createDataFileBackup(){
        File places = new File(mContext.getFilesDir(), filename);
        //clear old dataBackup
        dataBackup = new StringBuilder();
        dataWasDeleted = false;
        BufferedReader placesData = null;
        try {
            String line;
            placesData = new BufferedReader(new FileReader(places));
            while ((line = placesData.readLine()) != null) {
                dataBackup.append(line);
                dataBackup.append("\n");
            }
            Log.v("ManagePlacesActivity:  ", "file backed up successfully");
        } catch (IOException e) {
            Log.e("ManagePlacesActivity:  ", "failed to backup file before deleting");
            e.printStackTrace();
        } finally {
            if(placesData != null){
                try {
                    placesData.close();
                } catch (IOException e){
                    e.printStackTrace();
                }
            }
        }
    }

    private static void restoreDataFileFromBackup(){
        File places = new File(filename);
        if(dataWasDeleted) dataWasDeleted = false;
        //if places doesn't exist create new file (it shouldn't exist cuz it was deleted)
        if (!places.exists()){
            places = new File(mContext.getFilesDir(), filename);
        }
        String mDataBackup = dataBackup.toString();
        //if data backup isn't empty - restore
        if(!(mDataBackup.length() <= 0)) {
            BufferedWriter placesData = null;
            try {
                Log.v("ManagePlacesActivity:  ", "ready to write - " + dataBackup);
                placesData = new BufferedWriter(new FileWriter(places), mDataBackup.length());
                placesData.write(mDataBackup);
                placesData.close();
                Log.v("ManagePlacesActivity:  ", "write successful");
            } catch (Exception e) {
                Log.e("ManagePlacesActivity:  ", "Exception thrown: " + e);
                e.printStackTrace();
            } finally {
                if (placesData != null) {
                    try {
                        placesData.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void deletePlace(Places place){
        createDataFileBackup();
        String[] data = dataBackup.toString().split(System.getProperty("line.separator"));
        String type = "" + place.getPlaceTypeConst();
        String title = place.getTitle();
        String address = place.getAddress();
        String price = "" + place.getPriceInUSD();
        for(int i=0; i < data.length; i+=6 ){
            if(data[i].equals(type) && data[i+1].equals(title) && data[i+2].equals(address) && data[i+3].equals(price)){
                data = removeElement(data, i);
                StringBuilder temp = dataBackup;
                dataBackup = new StringBuilder();
                for(int j=0; j<data.length; j++){
                    dataBackup.append(data[j]);
                    dataBackup.append("\n");
                }
                restoreDataFileFromBackup();
                dataBackup = new StringBuilder();
                dataBackup = temp;
                Snackbar.make(scrollView, R.string.place_removing_successful, BaseTransientBottomBar.LENGTH_LONG)
                        .setAction(R.string.undo, new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Toast.makeText(mContext, R.string.undone, Toast.LENGTH_SHORT).show();
                                restoreDataFileFromBackup();
                                ActivityMain.updatePlaces();
                            }
                        }).show();
                break;
            }
        }
        ActivityMain.updatePlaces();
    }

    private static String[] removeElement(String[] data, int index){
        String[] rezData = new String[data.length - 6];
        for(int i=0; i<index; i++){
            rezData[i] = data[i];
        }
        index += 6;
        for(int i=index; i<data.length; i++){
            rezData[i-6] = data[i];
        }
        return rezData;
    }
}
