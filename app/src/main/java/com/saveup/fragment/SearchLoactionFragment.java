package com.saveup.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.JsonObject;
import com.saveup.activity.MainActivity;
import com.saveup.adapter.StoreListAdapter;
import com.saveup.android.R;
import com.saveup.model.StoreCityData;
import com.saveup.model.StoreData;
import com.saveup.model.SubCategoryData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.AppConstant;
import com.saveup.utils.SessionSave;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by AAPBD on 25/5/17.
 */

public class SearchLoactionFragment extends Fragment implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, LocationListener, OnMapReadyCallback, GoogleMap.OnInfoWindowClickListener {


    private static final String TAG = SearchLoactionFragment.class.getSimpleName();
    private static final int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    private GoogleMap mMap;
    protected SupportMapFragment mapFragment;
    private View mMapView;
    private Menu menu;
    RelativeLayout use_current_location;
    public RelativeLayout listContainer;
    RecyclerView mRecyclerView;
    private StoreListAdapter homeCatergoryAdap;
    private CustomPagerAdapter mCustomPagerAdapter;
    // TabLayoutView
    public View tabviewLayout;
    public TabLayout tabsLayout;
    private ViewPager storedetailsViewer;
    private LocationRequest locationRequest;
    private Integer checkComing = 0;
    protected GoogleApiClient mGoogleApiClient;
    private boolean clicked = false;
    private EditText search_location;
    private TextView header_titleTxt;
    public boolean isNearByVisible = true;
    public ImageView back_icon;
    Toolbar toolbar;
    private ArrayList<StoreData> storeDataArrayList = new ArrayList<>();
    private View mProgressView, mProgressViewLay;
 //   private GetGeoCoderAddress geoObject;
    public ArrayList<StoreCityData> storeCityDataArrayList = new ArrayList<>();
    String lattitude, longitude;
    LocationManager locationManager;
    private static final int REQUEST_LOCATION = 1;
    private UserPresenter userPresenter;
    private MainActivity mainActivity;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationCallback locationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            for (Location location : locationResult.getLocations()) {

                if (location != null) {
                    getStores(location);
                    Log.d(TAG, "---called in callback---> " + location.getLatitude()
                            + "," + location.getLongitude());
                    if(mainActivity.getSupportFragmentManager().findFragmentById(R.id.fragment_container)  instanceof SearchLoactionFragment
                        && isNearByVisible){
                        showMenuOption();
                    }else{
                        hideMenuOption();
                    }
                }
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_search_location, container, false);
        ButterKnife.bind(getActivity());
        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.toolbar);
        header_titleTxt = toolbar.findViewById(R.id.header_titleTxt);
        back_icon = toolbar.findViewById(R.id.back_icon);
        mProgressViewLay = view.findViewById(R.id.predictedRow);
        mProgressView = view.findViewById(R.id.progressBar);
        search_location = view.findViewById(R.id.search_location);
        use_current_location = view.findViewById(R.id.predictedRow);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        homeCatergoryAdap = new StoreListAdapter(getActivity(), storeDataArrayList, mRecyclerView);
        listContainer = view.findViewById(R.id.listContainer);
        mRecyclerView.setAdapter(homeCatergoryAdap);
        homeCatergoryAdap.setOnItemClickListener(new StoreListAdapter.MyClickListener() {
            @Override
            public void onItemClick(int position, View v) {

                try {
                    //showProgress(false,mProgressView,mProgressViewLay);
                    UserPresenter userPresenter = new UserPresenter(SearchLoactionFragment.this);
                    userPresenter.shopdetailsPage(storeDataArrayList.get(position).getStore_id());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });
        storedetailsViewer = view.findViewById(R.id.storedetailsViewer);
        tabsLayout = view.findViewById(R.id.tabsLayout);

        userPresenter = new UserPresenter(SearchLoactionFragment.this);

        Log.d(TAG, "" + lattitude + " + longitude " + longitude);

        AppConstant.lattitude = lattitude;
        AppConstant.longitude = longitude;

        header_titleTxt.setText("" + getResources().getString(R.string.search_location));
        header_titleTxt.setVisibility(View.VISIBLE);
        //showProgress(false, mProgressView, mProgressViewLay);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (isPermissionGranted()) {
            buildGoogleApiClient();
            initGoogleAPIClient();
        }

        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mMapView = view.findViewById(R.id.map);
        mMapView.setVisibility(View.GONE);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainActivity = (MainActivity) getActivity();
        mainActivity.search_layout.setVisibility(View.GONE);
        mainActivity.menu_Name.setVisible(false);
        mainActivity.toggle.setDrawerIndicatorEnabled(false);
        mainActivity.toolbar.setVisibility(View.VISIBLE);
        mainActivity.disableDrawer(false);
        mainActivity.toolbar.setTitle("" + getResources().getString(R.string.near_by_stores));
        ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);

        final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
        mainActivity.toggle.setHomeAsUpIndicator(upArrow);
        mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mainActivity.onBackPressed();
            }
        });
        mRecyclerView.setLayoutManager(new WrapContentLinearLayoutManager(getActivity()));
//        mRecyclerView.addOnItemTouchListener(
//                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//
//                        mRecyclerView.setVisibility(View.GONE);
//                        isSearched= true;
//                        view = getActivity().getCurrentFocus();
//                        if (view != null) {
//                            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//
//                            if (imm.isAcceptingText()) {
//                                InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Activity.INPUT_METHOD_SERVICE);
//                                im.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
//                            }
//                        }
//
//                        String city_id = storeCityDataArrayList.get(position).getCity_id();
//
//                        System.out.println("storeCityDataArrayList --- > " +city_id );
//                        System.out.println("storeCityDataArrayList --- >  2 " +storeMainDataArrayList.size() );
//                        if (mMap != null)
//                            mMap.clear();
//
//                        ArrayList<LatLng> latLngList = new ArrayList<LatLng>();
//                        latLngList.clear();
//                        storeDataArrayList.clear();
//
//                        for (int i = 0; i < storeMainDataArrayList.size(); i++) {
//
//
//                            if (storeMainDataArrayList.get(i).getStore_city_id().equals(city_id)) {
//
//                                String store_id = storeMainDataArrayList.get(i).getStore_id();
//                                String store_name = storeMainDataArrayList.get(i).getStore_name();
//                                String store_img = storeMainDataArrayList.get(i).getStore_img();
//                                String store_status = storeMainDataArrayList.get(i).getStore_status();
//                                String store_latitude = storeMainDataArrayList.get(i).getStore_latitude();
//                                String store_longitude = storeMainDataArrayList.get(i).getStore_longitude();
//                                String product_count = storeMainDataArrayList.get(i).getProduct_count();
//                                String store_city_id = storeMainDataArrayList.get(i).getStore_city_id();
//                                String deal_count;
//                                if (storeMainDataArrayList.get(i).getDeal_count().equals("0")) {
//                                    deal_count = "N/A";
//                                } else {
//                                    deal_count = storeMainDataArrayList.get(i).getDeal_count();
//                                }
//
//
//                                System.out.println("storeCityDataArrayList --- > " +store_latitude + " store_longitude " +store_longitude );
//                                NearbyStore dealsData = new NearbyStore(store_id, store_name, store_img, store_status, store_latitude, store_longitude, product_count, deal_count, store_city_id);
//                                storeDataArrayList.add(dealsData);
//
//                            }
//                        }
//
//                        for (int i = 0; i < storeDataArrayList.size(); i++) {
//                            String store_name = storeDataArrayList.get(i).getStore_name();
//                            String store_latitude = storeDataArrayList.get(i).getStore_latitude();
//                            String store_longitude = storeDataArrayList.get(i).getStore_longitude();
//                            String product_count = storeDataArrayList.get(i).getProduct_count();
//                            String deal_count;
//                            if (storeDataArrayList.get(i).getDeal_count().equals("0")) {
//                                deal_count = "N/A";
//                            } else {
//                                deal_count = storeDataArrayList.get(i).getDeal_count();
//                            }
//
//
//                            mMap.addMarker(new MarkerOptions()/*.icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_logo))*/.position(new LatLng(Double.parseDouble(store_latitude), Double.parseDouble(store_longitude))).title("" + store_name).snippet("Deals " + deal_count + " " + " Products " + product_count)).setTag(i);
//                             mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.parseDouble(storeDataArrayList.get(0).getStore_latitude()), Double.parseDouble(storeDataArrayList.get(0).getStore_longitude()))));
//                             mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
////                            latLngList.add(new LatLng(Double.parseDouble(store_latitude), Double.parseDouble(store_longitude)));
////                            zoomToCoverAllMarkers(latLngList, mMap);
////
//                        }
//
//
//                        }
//                })
//        );


    }



    @Override
    public void onPrepareOptionsMenu(Menu menu) {

        menu.clear(); //Clear view of previous menu
        MenuInflater inflater = mainActivity.getMenuInflater();
        inflater.inflate(R.menu.search_location_fragment_menu, menu);
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        this.menu = menu;
        this.menu.findItem(R.id.showMapView).setVisible(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.showMapView:
                this.menu.findItem(R.id.hideMapView).setVisible(true);
                this.menu.findItem(R.id.showMapView).setVisible(false);
                showHideMapView(true);
                break;
            case R.id.hideMapView:
                this.menu.findItem(R.id.hideMapView).setVisible(false);
                this.menu.findItem(R.id.showMapView).setVisible(true);
                showHideMapView(false);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if(!hidden){
            showMenuOption();
        }
    }

    public void showMenuOption(){
        if(this.menu != null){
            if(mMapView.getVisibility() == View.VISIBLE){
                this.menu.findItem(R.id.hideMapView).setVisible(true);
                this.menu.findItem(R.id.showMapView).setVisible(false);
            }else{
                this.menu.findItem(R.id.hideMapView).setVisible(false);
                this.menu.findItem(R.id.showMapView).setVisible(true);
            }
        }
    }
    public void hideMenuOption(){
        this.menu.findItem(R.id.hideMapView).setVisible(false);
        this.menu.findItem(R.id.showMapView).setVisible(false);
    }

    private void showHideMapView(boolean show){
        if(show){
            mMapView.setVisibility(View.VISIBLE);
            mRecyclerView.setVisibility(View.GONE);
        }else{
            mMapView.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(getActivity().getWindow().getDecorView().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }


    @OnClick(R.id.predictedRow)
    void useCurrentLocation() {
        clicked = true;
        initGoogleAPIClient();
    }

    private boolean isPermissionGranted() {
        if ((ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) ||
                (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {

            requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);

            return false;

        } else {
            return true;
        }

    }

    private void initGoogleAPIClient() {

        try {

            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(5000);
            locationRequest.setFastestInterval(1000);

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
                            Log.d(TAG, " LocationSettingsStatusCodes.SUCCES ");
                            startLocationUpdates();
                            break;

                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            try {
                                Log.d(TAG, " LocationSettingsStatusCodes.RESOLUTION_REQUIRED ");
                                status.startResolutionForResult(
                                        getActivity(), 1000);
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
            Log.d(TAG, " startLocationUpdates ");

            Log.d(TAG, "startLocationUpdates: " + mGoogleApiClient);
            if (mGoogleApiClient != null && !mGoogleApiClient.isConnected()) {
                mGoogleApiClient.connect();
            }

            fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {

                            if (location != null) {
                                getStores(location);
                                Log.d(TAG, " startLocationUpdates ->  onSuccess "+location.toString());
                            }
                        }
                    });

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, null);

            // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    protected void stopLocationUpdates() {
        try {
            // LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            fusedLocationProviderClient.removeLocationUpdates(locationCallback);
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


 /*   private String getAddressFull(Double lat, Double lng) {
        List<Address> addresses = null;
        try {
            Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
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
                if (addresses.get(0).getAdminArea() != null)
                    cityName = addresses.get(0).getAdminArea();
                else
                    cityName = addresses.get(0).getSubLocality();
                if (addresses.get(0).getSubLocality() != null)
                    locality = addresses.get(0).getAddressLine(0) + "," + addresses.get(0).getSubLocality();
                else
                    locality = addresses.get(0).getAddressLine(0);
                hideSoftKeyboard();


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
    }*/


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
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

    private void getStores(Location location) {
        if (mMap != null) {
            if (checkComing == 0) {
                //mProgressView.setVisibility(View.INVISIBLE);
                //showProgress(false, mProgressView, mProgressViewLay);
                mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude())));
                mMap.animateCamera(CameraUpdateFactory.zoomTo(13));
                lattitude = String.valueOf(location.getLatitude());
                longitude = String.valueOf(location.getLongitude());
                userPresenter.nearByStore(lattitude, longitude);
            }else{
               // showProgress(false, mProgressView, mProgressViewLay);
            }
            checkComing++;
        }else{
            Log.i(TAG, "Connection started");
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            try {
                Log.e(TAG, "---called in location changed---");
                Log.i(TAG, "Connection Changed");
                getStores(location);

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
    public void onDestroy() {
        super.onDestroy();
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Log.v(TAG, "Dis-Connecting");
            stopLocationUpdates();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case 1000:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        initGoogleAPIClient();
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.d(TAG, "failure");
                        mGoogleApiClient = null;
                        break;
                }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.d(TAG, "onRequestPermissionsResult  : " + "" + grantResults.length);
        Log.d(TAG, "onRequestPermissionsResult  : " + "" + grantResults[0]);
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.d(TAG, "onRequestPermissionsResult: " + "Success");
                    // buildGoogleApiClient();
                    if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
                        mGoogleApiClient.disconnect();
                    }
                    buildGoogleApiClient();
                    initGoogleAPIClient();
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

        Log.d(TAG, "onMapReady: ");

        mMap = googleMap;
        mMap.setOnInfoWindowClickListener(this);

        userPresenter.nearByStore(lattitude, longitude);

         getCurrentLocation();
         getLocation();
    }


/*    private LatLngBounds getBoundingBox(final double pLatitude, final double pLongitude, final int pDistanceInMeters) {

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
    }*/

    @Override
    public void onInfoWindowClick(Marker marker) {

        //TODO
        hideMenuOption();

        System.out.println("Info window clicked 1" + marker.getPosition());
        System.out.println("Info window clicked 1" + marker.getId());

        String store_id = storeDataArrayList.get((Integer) marker.getTag()).getStore_id();
        Log.e(TAG, store_id);
        Bundle bundle = new Bundle();
        bundle.putBoolean("isfromMap", true);
        bundle.putString("store_id", store_id);
        StoreListFragment fragment2 = new StoreListFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment2);
        fragment2.setArguments(bundle);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();

        System.out.println("Info window clicked 1 store_id " + store_id);
        marker.hideInfoWindow();


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
            if (jsonObject != null) {
                storeDataArrayList.clear();
                SessionSave.saveSession("map_details", jsonObject.toString(), getActivity());

                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {
                    mProgressView.setVisibility(View.INVISIBLE);
                    //showProgress(false, mProgressView, mProgressViewLay);
                    JSONArray product_review = json.getJSONArray("store_details");  //storeCityDataArrayList
                    if (product_review.length() > 0) {

                        for (int i = 0; i < product_review.length(); i++) {

                            String store_id = product_review.getJSONObject(i).getString("merchant_id");
                            String store_name = product_review.getJSONObject(i).getString("store_name");
                            String store_img = product_review.getJSONObject(i).getString("store_img");
                            String store_status = product_review.getJSONObject(i).getString("store_status");
                            String store_latitude = product_review.getJSONObject(i).getString("store_latitude");
                            String store_longitude = product_review.getJSONObject(i).getString("store_longitude");
                            String product_count = product_review.getJSONObject(i).getString("product_count");
                            String store_city_id = product_review.getJSONObject(i).getString("store_city_id");
                            String deal_count;
                            if (product_review.getJSONObject(i).getString("deal_count").equals("0")) {
                                deal_count = "N/A";
                            } else {
                                deal_count = product_review.getJSONObject(i).getString("deal_count");
                            }
                            StoreData dealsData = new StoreData(store_id, store_name, store_img, store_status, product_count, deal_count);

                            /*BigDecimal bigDecimalCurrency=new BigDecimal(store_latitude);
                            BigDecimal bigDecimalCurrency2 =new BigDecimal(store_longitude);


                            BigDecimal latitudeDifference = new BigDecimal(store_latitude).subtract(new BigDecimal(store_longitude));
                            BigDecimal longitudeDifference = new BigDecimal(store_latitude).subtract(new BigDecimal(store_longitude));

                            dealsData.setDifferentWithMyLocation(latitudeDifference.subtract(longitudeDifference));*/

                            storeDataArrayList.add(dealsData);

                            if (mMap != null)
                                mMap.addMarker(new MarkerOptions()/*.icon(BitmapDescriptorFactory.fromResource(R.mipmap.location_logo))*/.position(new LatLng(Double.parseDouble(store_latitude), Double.parseDouble(store_longitude))).title("" + store_name).snippet("Deals " + product_count + " ")).setTag(i);

                        }
                        homeCatergoryAdap.notifyDataSetChanged();
                    }
                } else {
                    //showProgress(false, mProgressView, mProgressViewLay);
                    Log.e(TAG, "no_nearby_store_found");
                }
            } else {
                //showProgress(false, mProgressView, mProgressViewLay);
                Log.e(TAG, "unable_to_reach_server");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void storeDetailsView(JsonObject jsonObject) {
        try {
            //TODO
            hideMenuOption();
            isNearByVisible = false;

            final MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.search_layout.setVisibility(View.GONE);
            mainActivity.menu_Name.setVisible(false);

            mainActivity.toggle.setDrawerIndicatorEnabled(false);
            mainActivity.toolbar.setVisibility(View.VISIBLE);
            mainActivity.disableDrawer(false);
            mainActivity.toolbar.setTitle(getString(R.string.store_details));
            ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
            final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
            mainActivity.toggle.setHomeAsUpIndicator(upArrow);

            mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.onBackPressed();
                }
            });

            //showProgress(false,mProgressView, mProgressViewLay);


            if (jsonObject != null) {
                JSONObject json = new JSONObject(jsonObject.toString());
                if (json.getString("status").equals("200")) {
                    listContainer.setVisibility(View.GONE);
                    SessionSave.saveSession("storedetails", "" + jsonObject.toString(), getActivity());
                    mCustomPagerAdapter = new CustomPagerAdapter(getFragmentManager());
                    mCustomPagerAdapter.addFragment(new StoreListDetailsFragment(), getResources().getString(R.string.store).toUpperCase());
                    mCustomPagerAdapter.addFragment(new StoreProductListFragment(), getResources().getString(R.string.deals).toUpperCase());
                    storedetailsViewer.setClipToPadding(false);
                    storedetailsViewer.setOffscreenPageLimit(0);
                    storedetailsViewer.setAdapter(mCustomPagerAdapter);
                    tabsLayout.setVisibility(View.VISIBLE);
                    tabsLayout.setupWithViewPager(storedetailsViewer);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public class CustomPagerAdapter extends FragmentStatePagerAdapter {
        int MaxCount = 0;
        String cat_id = "";
        ArrayList<SubCategoryData> subArrayList;

        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public CustomPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

    //@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    /*public void showProgress(final boolean show, final View mainView, final View mProgressView) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
            mainView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mainView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }*/

 /*   public class GetGeoCoderAddress extends AsyncTask<String, Void, String> {
        private final String Urlcoreconfig;
        private String jsonResult;
        String lat, lng;
        private String json_s;

        public GetGeoCoderAddress(String url) {
            this.Urlcoreconfig = url;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
//
                URL url = new URL(Urlcoreconfig);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                InputStream is = new BufferedInputStream(conn.getInputStream());
                try {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                    StringBuilder sb = new StringBuilder();
                    String line = null;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    json_s = sb.toString();
                    is.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return json_s;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            showProgress(true, mProgressView, mProgressViewLay);


            try {

                if (!result.isEmpty()) {
                    JSONObject json = new JSONObject(result);


                    System.out.println("json_ " + json);
                    JSONArray json1 = json.getJSONArray("results");
                    JSONObject json2 = json1.getJSONObject(0).getJSONObject("geometry");
                    JSONObject json3 = json2.getJSONObject("location");
                    lat = json3.getString("lat");
                    lng = json3.getString("lng");
                    if (mMap != null && lat != null && !(lat.equals("") && lng != null && !(lng.equals("")))) {
                        isSearched = false;
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(Double.valueOf(lat), Double.valueOf(lng))));
                        mMap.animateCamera(CameraUpdateFactory.zoomTo(12));

                    }
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }


        }
    }*/

 /*   private void zoomToCoverAllMarkers(ArrayList<LatLng> latLngList, GoogleMap googleMap) {


        try {

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            for (LatLng marker : latLngList) {
                System.out.println("list latlong" + latLngList);
                builder.include(marker);
            }
            LatLngBounds bounds = builder.build();


            CameraUpdate cu = null;

            cu = CameraUpdateFactory.newLatLngBounds(bounds, 50);
//
//        int padding = 50;
//        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
            if (cu != null) {
                googleMap.animateCamera(cu);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        System.out.println("sucess");


    }*/


    // current location
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission
                (getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);

        } else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            Location location1 = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            Location location2 = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (location != null) {
                double latti = location.getLatitude();
                double longi = location.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                userPresenter.nearByStore(lattitude, longitude);

            } else if (location1 != null) {
                double latti = location1.getLatitude();
                double longi = location1.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                userPresenter.nearByStore(lattitude, longitude);

            } else if (location2 != null) {
                double latti = location2.getLatitude();
                double longi = location2.getLongitude();
                lattitude = String.valueOf(latti);
                longitude = String.valueOf(longi);
                userPresenter.nearByStore(lattitude, longitude);
            } else {
                Toast.makeText(getContext(), "Unable to Trace your location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void getCurrentLocation() {
        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            //   buildAlertMessageNoGps();

        } else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            getLocation();
        }
    }

}
