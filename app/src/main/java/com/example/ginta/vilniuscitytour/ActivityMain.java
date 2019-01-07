package com.example.ginta.vilniuscitytour;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class ActivityMain extends AppCompatActivity {
    private static Context context;
    private static LocationManager locationManager;
    public static Context getContext() {
        return context;
    }
    private static ArrayList<Places> foodPlaces = new ArrayList<Places>();
    private static ArrayList<Places> eventPlaces = new ArrayList<Places>();
    private static ArrayList<Places> culturePlaces = new ArrayList<Places>();
    private static ArrayList<Places> livePlaces = new ArrayList<Places>();

    public static LocationManager getLocationManager(){
        return locationManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = getApplicationContext();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        //read places file to get all data about saved places
        loadPlaces();

        final ViewPager viewPager = (ViewPager) findViewById(R.id.view_pager);
        PlacesFragmentPagerAdapter adapter = new PlacesFragmentPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        //get drawer layout so we could close and open it when needed
        final DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer);

        ImageView drawerButton = (ImageView) findViewById(R.id.drawer_button);
        drawerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.START, true);
            }
        });


        //get navigation view to assign item selected listener
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        //set listener to get notified when item was selected
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                menuItem.setChecked(true);
                drawerLayout.closeDrawers();
                switch (menuItem.getItemId()){
                    //case selected items id is equal to one of the view pagers items - set view pagers current item to selected one
                    case R.id.nav_live: viewPager.setCurrentItem(0, true); break;
                    case R.id.nav_food: viewPager.setCurrentItem(1, true); break;
                    case R.id.nav_culture: viewPager.setCurrentItem(2, true); break;
                    case R.id.nav_events: viewPager.setCurrentItem(3, true); break;
                    case R.id.nav_manage: startActivity(new Intent(ActivityMain.this, ManagePlacesActivity.class));
                }
                return true;
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        //when the activity is visible again update array list
        updatePlaces();
    }

    private static void loadPlaces(){
        File file = new File(getContext().getFilesDir(), ManagePlacesActivity.filename);
        BufferedReader placesData = null;
        String line;

        int type;
        String title;
        String address;
        int price;
        String website;
        int rating;

        try {
            placesData = new BufferedReader(new FileReader(file));
            while((line = placesData.readLine()) != null){
                 type = Integer.parseInt(line);
                 title = placesData.readLine();
                 address = placesData.readLine();
                 price = Integer.parseInt(placesData.readLine());
                 website = placesData.readLine();
                 line = placesData.readLine();
                 if(line.equals("not_rated")) rating = -1;
                 else rating = Integer.parseInt(line);
                 if(type == Places.LIVE) livePlaces.add(new Places(address, title, type, price, rating, website));
                 else if(type == Places.FOOD) foodPlaces.add(new Places(address, title, type, price, rating, website));
                 else if(type == Places.CULTURE) culturePlaces.add(new Places(address, title, type, price, rating, website));
                 else if(type == Places.EVENT) eventPlaces.add(new Places(address, title, type, price, rating, website));
            }
        }catch (Exception e){
            Log.e("ActivityMain:  ", "Exception thrown while reading file in loadPlaces(): " + e);
            e.printStackTrace();
        } finally {
            if(placesData != null){
                try{
                    placesData.close();
                } catch (Exception e){
                    Log.e("ActivityMain:  ", "Exception thrown while closing file in loadPlaces(): " + e);
                    e.printStackTrace();
                }
            }
        }
    }

    public static void updatePlaces(){
        foodPlaces = new ArrayList<Places>();
        eventPlaces = new ArrayList<Places>();
        culturePlaces = new ArrayList<Places>();
        livePlaces = new ArrayList<Places>();
        loadPlaces();
    }

    public static ArrayList<Places> getPlacesList(int Type){
        switch (Type){
            case Places.LIVE: return livePlaces;
            case Places.CULTURE: return culturePlaces;
            case Places.EVENT: return eventPlaces;
            case Places.FOOD: return foodPlaces;
            default: return null;
        }
    }
}
