package com.example.ginta.vilniuscitytour;

import android.location.Location;
import android.util.Log;

import java.util.ArrayList;

import  java.lang.Math;

public class Places {
    private PlaceLocation placeLocation;
    private float mRating = -1; //if rating = -1 - "not rated"
    private String mTitle = "ERROR";
    private String mUrl = null; //if url = null - no website
    private int mPriceInUSD = 0;
    private String mPriceMeaning = "ERROR"; //price meaning (USD/NIGHT, USD/TICKET etc)
    private int placeType = -1;
    private String addressLine;
    private ArrayList<Integer> mImageResourceIds = new ArrayList<Integer>(); //if mImageResourceIds.size() = 0 - no images
    private static int mPriceLimitCheap = 5;   //below this value, price is considered as cheap
    //between above and bellow values price is considered as average
    private static int mPriceLimitExpensive = 15;  //above this value price is considered as expensive

    final public static int LIVE = 0;
    final public static int CULTURE = 1;
    final public static int EVENT = 2;
    final public static int FOOD = 3;

    /************************************************
    ***************Setter methods********************
    *************************************************/

    public void setLocation(double latitude, double longitude){
        placeLocation = new PlaceLocation(latitude, longitude);
    }

    /**set Rating of place
     * @param Rating - integer from 0 to 5 - represents places rating
     **/
    public void setRating(float Rating){
        if(Rating > 5) mRating = 5;
        else if (Rating < 0) mRating = 0;
        else mRating = Rating;
    }

    /**set Price of place
     * @param PriceInUSD - integer price of visit, avg food price ect.
     *              (decided by PlaceType)
     **/
    public void setPriceInUSD(int PriceInUSD){
        if(PriceInUSD < 0) mPriceInUSD = 0;
        else mPriceInUSD = PriceInUSD;
    }

    /**
     * Takes Place type (Hotel, Event, Culture or Food)
     * and sets relevant price meaning and place type
     * @param PlaceType - type of place added
     */
    public void setPlaceType(int PlaceType){
        switch (PlaceType){
            case LIVE:
                placeType = LIVE;
                if(mPriceInUSD == 0) mPriceMeaning = "free";
                else mPriceMeaning = mPriceInUSD + "$/night";

                break;
            case CULTURE:
                placeType = CULTURE;
                if(mPriceInUSD == 0) mPriceMeaning = "free";
                else mPriceMeaning = mPriceInUSD + "$/ticket";
                break;
            case EVENT:
                placeType = EVENT;
                if(mPriceInUSD == 0) mPriceMeaning = "free";
                else mPriceMeaning = mPriceInUSD + "$/ticket";
                break;
            case FOOD:
                placeType = FOOD;
                if(mPriceInUSD == 0) mPriceMeaning = "free";
                else if(mPriceInUSD <= mPriceLimitCheap) mPriceMeaning = "Cheap";
                else if(mPriceInUSD >= mPriceLimitExpensive) mPriceMeaning = "Expensive";
                else mPriceMeaning = "Average";
                break;
            default:
                mPriceMeaning = mPriceInUSD + "??";
                break;
        }
    }

    /**
     * Sets price limits for classification of cheap, average or expensive
     * only used in places of type FOOD
     * @param PriceLimitCheap - lower limit (prices less then this is classified as cheap)
     * @param PriceLimitExpensive - higher limit (prices greater then this is classified as expensive)
     * everything between given parameters will be classified as average
     * IMPORTANT: if lower limit is greater when higher limit - they will be swapped
     *                            if any of limits is set to 0 - original, new limits wont be set
     *                            if lower limit is equal to higher limit - average price tag wont be used
     */
    public void setPriceLimits(int PriceLimitCheap, int PriceLimitExpensive){
        if(PriceLimitCheap > PriceLimitExpensive){
            int temp = PriceLimitCheap;
            PriceLimitCheap = PriceLimitExpensive;
            PriceLimitExpensive = temp;
        }
        //if any of limit prices are less or equal to 0, we wont change current price limits
        if(!(PriceLimitCheap <= 0 && PriceLimitExpensive <= 0)){
            mPriceLimitCheap = PriceLimitCheap;
            mPriceLimitExpensive = PriceLimitExpensive;
            return;
        }
        Log.w("Places.java: ", "Price limits was not set, make sure both provided parameters has value greater then 0");
    }

    /**
     * adds image to Place - can be used multiple times to add multiple images
     * @param ImageResourceId - resource of image to add
     */
    public void addImage(int ImageResourceId){
        mImageResourceIds.add(ImageResourceId);
    }

    /**
     * adds multiple images
     * @param ImageResourceIDs - ArrayList<Integer> of image resource ids
     */
    public void addImage(ArrayList<Integer> ImageResourceIDs){
        mImageResourceIds = ImageResourceIDs;
    }

    /**
     * set Title to Place
     * @param Title - title of place
     */
    public void setTitle(String Title){
        mTitle = Title;
    }

    /**
     * set url address if place has its own website
     * @param Url - address of website
     */
    public void setUrl(String Url){
        if(Url.equals("no_website")) mUrl = null;
        else mUrl = Url;
    }
    /************************************************
    **************Main functionality*****************
    *************************************************/
    //temp - change latter
    public Places(String address, String Title, int PlaceType, int Price, float Rating, String Url){
        this(54.681394, 25.271294, Title, PlaceType, Price, Rating, Url);
        addressLine = address;
    }
    public Places(String address, String Title, int PlaceType, int Price, String Url){
        this(54.681394, 25.271294, Title, PlaceType, Price, Url);
        addressLine = address;
    }
    public Places(String address, String Title, int PlaceType, int Price, float Rating){
        this(54.681394, 25.271294, Title, PlaceType, Price, Rating);
        addressLine = address;
    }
    public Places(String address, String Title, int PlaceType, int Price){
        this(54.681394, 25.271294, Title, PlaceType, Price);
        addressLine = address;
    }

    public Places(double latitude, double longitude, String Title, int PlaceType, int Price, float Rating, String Url){
        this(latitude, longitude, Title, PlaceType, Price, Rating);
        setUrl(Url);
    }
    public Places(double latitude, double longitude, String Title, int PlaceType, int Price, String Url){
        this(latitude, longitude, Title, PlaceType, Price);
        setUrl(Url);
    }
    public Places(double latitude, double longitude, String Title, int PlaceType, int Price, float Rating){
        this(latitude, longitude, Title, PlaceType, Price);
        setRating(Rating);
    }
    public Places(double latitude, double longitude, String Title, int PlaceType, int Price){
        setLocation(latitude, longitude);
        setTitle(Title);
        setPriceInUSD(Price);
        setPlaceType(PlaceType);
    }

    /************************************************
    ***************Getter methods********************
    *************************************************/
    public String getAddress(){
        return addressLine;
    }

    public String getDistanceFromUser(){
        String distanceText;
        float distance = placeLocation.getDistanceFromUser();
        if(distance >= 1000){
            int distanceInt;
            distanceInt = Math.round(distance/100);
            distance = distanceInt/10;
            distanceText = distance + "km away";
        }
        else distanceText = distance + "m away";
        return distanceText;
    }
    /**
     * @return rating of place
     */
    public float getRating(){
        return mRating;
    }

    /**
     * @return price of place in usd
     */
    public int getPriceInUSD(){
        return mPriceInUSD;
    }

    /**
     * @param position - position of image to return;
     * @return images id at given position
     */
    public int getImageResourceId(int position){
        return mImageResourceIds.get(position);
    }

    /**
     * @return cheap price limit
     */
    public Location getLocation(){
        return placeLocation.getPlaceLocation();
    }


    public int getAmountOfImages(){
        if (mImageResourceIds == null) return 0;
        else return mImageResourceIds.size();
    }

    /**
     * @return place websites url address
     */
    public String getUrl(){
        return mUrl;
    }

    /**
     * @return title of place
     */
    public String getTitle(){
        return mTitle;
    }

    /**
     * @return type of place
     */
    public String getPlaceType(){
        switch (placeType){
            case LIVE: return "Live";
            case CULTURE: return "Culture";
            case EVENT: return "Event";
            case FOOD: return "Food";
            default: return "Miscellaneous";
        }
    }

    public static String getCustomPlaceType(int type){
        switch (type){
            case LIVE: return "Live";
            case CULTURE: return "Culture";
            case EVENT: return "Event";
            case FOOD: return "Food";
            default: return "Miscellaneous";
        }
    }

    public int getPlaceTypeConst(){
        return placeType;
    }

    /**
     * @return meaning of price
     */
    public String getPriceMeaning(){
        return mPriceMeaning;
    }

    /**
     * get colors
     * @return returns colors to use as a theme for item
     */
    public static int getDarkColor(int Type){
        switch (Type){
            case LIVE: return R.color.colorPurpleMedium;
            case CULTURE: return R.color.colorBlueMedium;
            case EVENT: return R.color.colorOrangeMedium;
            case FOOD: return R.color.colorGreenMedium;
            default: return 0;
        }
    }
    public static int getLightColor(int Type){
        switch (Type){
            case LIVE: return R.color.colorPurpleLight;
            case CULTURE: return R.color.colorBlueLight;
            case EVENT: return R.color.colorOrangeLight;
            case FOOD: return R.color.colorGreenLight;
            default: return 0;
        }
    }
}
