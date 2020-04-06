package com.saveup.activity.location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.saveup.android.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.JsonObject;
import com.saveup.activity.BaseActivity;
import com.saveup.model.NearbyStore;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.AppConstant;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * Created by AAPBD on 10/11/16.
 */

public class SearchLocationActivity extends BaseActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback {


    private static final String TAG = SearchLocationActivity.class.getSimpleName();
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private GoogleMap mMap;

    @BindView(R.id.predictedRow)
    RelativeLayout use_current_location;
    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private LocationRequest locationRequest;
    private Integer checkComing = 0;
    private String address, cityName , countryName, locality ;
    protected GoogleApiClient mGoogleApiClient;
    // private LinearLayoutManager mLinearLayoutManager;
    private PlacesAutoCompleteAdapter mAutoCompleteAdapter;
    private boolean clicked = false;
    private EditText search_location;
    private TextView header_titleTxt;
    public ImageView back_icon;
    Toolbar toolbar;
    private LatLngBounds latLngBounds = new LatLngBounds(new LatLng(23.63936, 68.14712), new LatLng(28.20453, 97.34466));
    private ArrayList<NearbyStore> storeDataArrayList = new ArrayList<>();
    private View mProgressView, mProgressViewLay;

    @Override
    public int setLayout() {
        return R.layout.activity_search_location;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void initialize() {
        ButterKnife.bind(this);
        toolbar = findViewById(R.id.toolbar);
        header_titleTxt = toolbar.findViewById(R.id.header_titleTxt);
        back_icon = toolbar.findViewById(R.id.back_icon);
        mProgressViewLay = findViewById(R.id.predictedRow);
        mProgressView = findViewById(R.id.progressBar);
        search_location = findViewById(R.id.search_location);

        back_icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        header_titleTxt.setText("" + "Search Location");
        header_titleTxt.setVisibility(View.VISIBLE);
        showProgress(false, mProgressView, mProgressViewLay);
        buildGoogleApiClient();
        initGoogleAPIClient();


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
//        getSupportActionBar().setTitle("Select Location");
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        search_location.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                Log.d(TAG, "onTextChanged: " + charSequence);
                if (mAutoCompleteAdapter != null && charSequence != null && charSequence.length() > 0) {
                    mAutoCompleteAdapter.getFilter().filter(charSequence);

                    mRecyclerView.setVisibility(View.VISIBLE);
                    mRecyclerView.getAdapter().notifyDataSetChanged();
                } else {
                    mRecyclerView.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        mRecyclerView.addOnItemTouchListener(
                new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {


                        view = getCurrentFocus();
                        if (view != null) {
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

                            if (imm.isAcceptingText()) {
                                InputMethodManager im = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
                                im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
                            }
                        }


                        try {

                            final PlacesAutoCompleteAdapter.PlaceAutocomplete item = mAutoCompleteAdapter.getItem(position);
                            final String placeId = String.valueOf(item.placeId);

                            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                                    .getPlaceById(mGoogleApiClient, placeId);

                            placeResult.setResultCallback(new ResultCallback<PlaceBuffer>() {
                                @Override
                                public void onResult(@NonNull PlaceBuffer places) {
                                    if (places.getCount() == 1) {


                                        showProgress(false, mProgressView, mProgressViewLay);

                                        getAddressFull( places.get(0).getLatLng().latitude,  places.get(0).getLatLng().longitude);


                                    } else {
                                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                })
        );


    }


    private void hideSoftKeyboard() {

        InputMethodManager im = (InputMethodManager) this.getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }


    @OnClick(R.id.predictedRow)
    void useCurrentLocation() {

//        if(shouldAskPermission())
//        {
//            ActivityCompat.requestPermissions(SearchLocationActivity.this,
//                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
//                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
//        }else{
//            initGoogleAPIClient();
//        }

        clicked = true;
        initGoogleAPIClient();

    }

    private boolean shouldAskPermission() {
        boolean shouldAskPermission = ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED));
        return shouldAskPermission;
    }

    private void initGoogleAPIClient() {

        try {


            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                    .addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);
            PendingResult<LocationSettingsResult> result =
                    LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, builder.build());

            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result.getLocationSettingsStates();

                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:


                            if (shouldAskPermission()) {
                                ActivityCompat.requestPermissions(SearchLocationActivity.this,
                                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }


                            startLocationUpdates();
                            break;

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                Log.d("Failure", "Failure");
                                status.startResolutionForResult(
                                        SearchLocationActivity.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                            }
                            break;

                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            break;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void startLocationUpdates() {
        try {

            Log.d(TAG, "startLocationUpdates: " + mGoogleApiClient);
            if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }
            LocationServices.FusedLocationApi.requestLocationUpdates(
                    mGoogleApiClient, locationRequest, SearchLocationActivity.this);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void stopLocationUpdates() {
        try {
            LocationServices.FusedLocationApi.removeLocationUpdates(
                    mGoogleApiClient, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private String getAddressFull(Double lat, Double lng) {
        List<Address> addresses = null;
        try {
            Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);
            addresses = geocoder.getFromLocation(lat, lng, 1);
            if (addresses != null && addresses.size() > 0) {
                Address addressss = addresses.get(0);
                address = addresses.get(0).getAddressLine(1);
                StringBuilder strReturnedAddress = new StringBuilder();

                for (int i = 0; i < addressss.getMaxAddressLineIndex(); i++) {
                    if (addressss.getAddressLine(i) != null && !addressss.getAddressLine(i).equals(""))
                        strReturnedAddress.append(addressss.getAddressLine(i)).append(" , ");
                }
                showProgress(true, mProgressView, mProgressViewLay);

                Log.d("address ", "address full" + strReturnedAddress.toString());
                Log.d("address ", "address line1 " + addresses.get(0).getAddressLine(0));
                Log.d("address ", "address line1 " + addresses.get(0).getCountryName());
                Log.d("address ", "address line1 " + addresses.get(0).getSubLocality());
                Log.d("address ", "address line1 " + addresses.get(0).getAdminArea());
                Log.d("address ", "address line1 " + addresses.get(0).getSubAdminArea());
                countryName = addresses.get(0).getCountryName();
                if(addresses.get(0).getAdminArea()!=null)
                cityName = addresses.get(0).getAdminArea();
                else
                    cityName = addresses.get(0).getSubLocality();
                if(addresses.get(0).getSubLocality()!=null)
                locality=addresses.get(0).getAddressLine(0)+","+addresses.get(0).getSubLocality();
                else
                    locality=addresses.get(0).getAddressLine(0);
                hideSoftKeyboard();
                Intent intent = new Intent();
                intent.putExtra("ship_address",locality);
                intent.putExtra("ship_city_name", cityName);
                intent.putExtra("ship_country_name", countryName);
                setResult(1000, intent);
                finish();
                overridePendingTransition(0, 0);

            }

        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            ioException.printStackTrace();
        } catch (IllegalArgumentException illegalArgumentException) {
            // Catch invalid latitude or longitude values.
            illegalArgumentException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return address;
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .build();
        mGoogleApiClient.connect();
    }


    @Override
    public void onConnected(Bundle bundle) {
        Log.i(TAG, "Connection started");


        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }

        startLocationUpdates();

    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            try {

                Log.i(TAG, "Connection Changed");

                // SharedPrefUtils.setMyLat(SearchLocationActivity.this, "" + location.getLatitude());
                //SharedPrefUtils.setMyLng(SearchLocationActivity.this, "" + location.getLongitude());
                if (checkComing == 0) {
                 //   getAddressFull(location.getLatitude(), location.getLongitude());

                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(Place.TYPE_COUNTRY)
                            .setCountry("IN")
                            .build();


                    latLngBounds = getBoundingBox(location.getLatitude(), location.getLongitude(), 50000);


                    mAutoCompleteAdapter = new PlacesAutoCompleteAdapter(this, R.layout.serachiew_adapter,
                            mGoogleApiClient, latLngBounds, typeFilter);

                    //mLinearLayoutManager = new WrapContentLinearLayoutManager(this);
                    mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(this));
                    //mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));

                    mRecyclerView.setAdapter(mAutoCompleteAdapter);

                    if (mMap != null) {

                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(11));

                    }
                }

                checkComing++;


            } catch (SecurityException e) {
                e.printStackTrace();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = " + result.getErrorCode());


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Log.v("Google API", "Dis-Connecting");

            stopLocationUpdates();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.d("success", "success");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.d("failure", "failure");
                        //mGoogleApiClient = null;
                        break;
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    Log.d(TAG, "onRequestPermissionsResult: " + "Success");
                    buildGoogleApiClient();
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: " + "Failure");
                    clicked = false;

                }
                break;

            default:
                break;


        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        UserPresenter userPresenter = new UserPresenter(SearchLocationActivity.this);
        userPresenter.nearByStore(AppConstant.lattitude,AppConstant.longitude);

    }


    private LatLngBounds getBoundingBox(final double pLatitude, final double pLongitude, final int pDistanceInMeters) {

        final double[] boundingBox = new double[4];

        final double latRadian = Math.toRadians(pLatitude);

        final double degLatKm = 110.574235;
        final double degLongKm = 110.572833 * Math.cos(latRadian);
        final double deltaLat = pDistanceInMeters / 1000.0 / degLatKm;
        final double deltaLong = pDistanceInMeters / 1000.0 /
                degLongKm;

        final double minLat = pLatitude - deltaLat;
        final double minLong = pLongitude - deltaLong;
        final double maxLat = pLatitude + deltaLat;
        final double maxLong = pLongitude + deltaLong;

        boundingBox[0] = minLat;
        boundingBox[1] = minLong;
        boundingBox[2] = maxLat;
        boundingBox[3] = maxLong;


        return new LatLngBounds(new LatLng(minLat, minLong), new LatLng(maxLat, maxLong));
    }

    class WrapContentLinearLayoutManager extends LinearLayoutManager {
        public WrapContentLinearLayoutManager(Context context) {
            super(context);
        }

        @Override
        public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
            try {
                super.onLayoutChildren(recycler, state);
            } catch (IndexOutOfBoundsException e) {
                // Log.e("probe", "meet a IOOBE in RecyclerView");
            }
        }
    }


    public void nearStoreData(JsonObject jsonObject) {

        try {
            showProgress(true, mProgressView, mProgressViewLay);

            if (jsonObject != null) {


                storeDataArrayList.clear();


                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {


                    JSONArray product_review = json.getJSONArray("store_details");
                    if (product_review.length() > 0) {

                        for (int i = 0; i < product_review.length(); i++) {

                            String store_id = product_review.getJSONObject(i).getString("store_id");
                            String store_name = product_review.getJSONObject(i).getString("store_name");
                            String store_img = product_review.getJSONObject(i).getString("store_img");
                            String store_status = product_review.getJSONObject(i).getString("store_status");
                            String store_latitude = product_review.getJSONObject(i).getString("store_latitude");
                            String store_longitude = product_review.getJSONObject(i).getString("store_longitude");
                            String product_count = product_review.getJSONObject(i).getString("product_count");
                            String deal_count = product_review.getJSONObject(i).getString("deal_count");

                            NearbyStore dealsData = new NearbyStore(store_id, store_name, store_img, store_status, store_latitude, store_longitude, product_count, deal_count, "");
                            storeDataArrayList.add(dealsData);
                            if (mMap != null)
                                mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.home_logo)).position(new LatLng(Double.parseDouble(store_latitude), Double.parseDouble(store_longitude))));


                        }


                    }
                }
            }
        } catch (
                Exception ex
                )

        {
            ex.printStackTrace();

        }
    }



}
