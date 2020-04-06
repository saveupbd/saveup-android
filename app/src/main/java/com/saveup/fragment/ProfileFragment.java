package com.saveup.fragment;

import android.Manifest;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.provider.Settings;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.aapbd.appbajarlib.image.BitmapUtils;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.gson.JsonObject;
import com.saveup.activity.MainActivity;
import com.saveup.activity.location.SearchLocationActivity;
import com.saveup.adapter.CountryCityBaseAdapter;
import com.saveup.adapter.CoutryCityAdapter;
import com.saveup.android.R;
import com.saveup.model.CityListData;
import com.saveup.model.CountryListData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.ImageUtils;
import com.saveup.utils.NetworkStatus;
import com.saveup.utils.SessionSave;
import com.saveup.views.RippleView;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

/**
 * @author AAPBD on 21,April,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.fragment
 */


public class ProfileFragment extends Fragment {

    private TextView userName, addPhoto, userNumber, userEmail, myOrdersTxt, showallorder, shippingAddress, shippingAddressTxt;
    private View progressBar;
    private NestedScrollView mainTopLay;
    public ImageView profileImage, back_icon, edit_icon, location_icon;
    private LinearLayout myordersLay, button_updateaddressLay, /*mycartLay, */shippingLay, mywishListLay, mychangepasswordLay, profileLay, mydetailsLay, mydataLay;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private AppCompatEditText edtName, edtNumber, edtEmail;
    private RippleView button_updateinformation, button_updateaddress;
    private String user_id, user_email, user_name, user_phone, user_address1, user_address2, user_country_id, user_city_id, ship_id, ship_name,
            ship_email, ship_phone, ship_address1, ship_address2, ship_country_id, ship_city_id, ship_state, ship_postalcode, user_image;
    private Uri imageUri;
    private Bitmap mBitmap;
    private String encodedImage, selectedPath;
    private TextView header_titleTxt;
    private AppBarLayout appBarLayout;
    boolean firstTimeAccount;
    Toolbar toolbar;
    public Dialog malertmDialog;

    private LinearLayout shippingAddressEdtLay;
    private CardView card_ship_address;
    private EditText edtshipName, edtshipNumber, edtshipEmail, edtbuildingName, edtLocalityName, edtPincode, edtState;
    private Spinner spin_country, spin_city;
    ArrayList<CountryListData> mCountryList = new ArrayList<>();
    ArrayList<CityListData> mCityList;
    private String countryId = "", cityId = "";
    private int defaultCountry = 0, defaultCity = 0;
    private ArrayList<CityListData> mEmptyList = new ArrayList<>();

    private File imageFile;

    public static final int MULTIPLE_PERMISSIONS = 10;

    String[] permissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE};

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.profilecollapselayout, container, false);
        progressBar = view.findViewById(R.id.progressBar);
        mainTopLay = view.findViewById(R.id.collapsing_layout);
        userName = view.findViewById(R.id.userName);
        addPhoto = view.findViewById(R.id.addPhoto);
        userNumber = view.findViewById(R.id.userNumber);
        userEmail = view.findViewById(R.id.userEmail);
        myOrdersTxt = view.findViewById(R.id.myOrdersTxt);
        showallorder = view.findViewById(R.id.showallorder);
        shippingAddress = view.findViewById(R.id.shippingAddress);
        shippingAddressTxt = view.findViewById(R.id.shippingAddressTxt);
        header_titleTxt = view.findViewById(R.id.header_titleTxt);
        profileImage = view.findViewById(R.id.profileImage);
        myordersLay = view.findViewById(R.id.myordersLay);
        //mycartLay = view.findViewById(R.id.mycartLay);
        button_updateaddressLay = view.findViewById(R.id.button_updateaddressLay);
        button_updateaddressLay.setVisibility(View.GONE);
        profileLay = view.findViewById(R.id.profileLay);
        mydetailsLay = view.findViewById(R.id.mydetailsLay);
        shippingLay = view.findViewById(R.id.shippingLay);
        mydataLay = view.findViewById(R.id.mydataLay);
        mywishListLay = view.findViewById(R.id.mywishListLay);
        mychangepasswordLay = view.findViewById(R.id.mychangepasswordLay);
        edtName = view.findViewById(R.id.edtName);
        edtNumber = view.findViewById(R.id.edtNumber);
        edtEmail = view.findViewById(R.id.edtEmail);
        button_updateinformation = view.findViewById(R.id.button_updateinformation);
        button_updateaddress = view.findViewById(R.id.button_updateaddress);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setCollapsedTitleTextAppearance(R.style.collapsedappbar);
        collapsingToolbarLayout.setExpandedTitleColor(Color.parseColor("#00000000"));
        toolbar = view.findViewById(R.id.toolbar);
        appBarLayout = view.findViewById(R.id.appBarLayout);
        back_icon = toolbar.findViewById(R.id.back_icon);
        shippingAddressEdtLay = view.findViewById(R.id.shippingAddressEdtLay);
        card_ship_address = view.findViewById(R.id.card_ship_address);

        spin_country = view.findViewById(R.id.spin_country);
        spin_city = view.findViewById(R.id.spin_city);

        edtshipName = view.findViewById(R.id.edtshipName);
        edtshipNumber = view.findViewById(R.id.edtshipNumber);
        edtshipEmail = view.findViewById(R.id.edtshipEmail);
        edtbuildingName = view.findViewById(R.id.edtbuildingName);
        edtLocalityName = view.findViewById(R.id.edtLocalityName);
        edtPincode = view.findViewById(R.id.edtPincode);
        edtState = view.findViewById(R.id.edtState);

        edit_icon = toolbar.findViewById(R.id.edit_icon);
        location_icon = toolbar.findViewById(R.id.location_icon);
        location_icon.setVisibility(View.GONE);
        collapsingToolbarLayout.setTitle("");
        header_titleTxt.setText("" + getResources().getString(R.string.profile));
        header_titleTxt.setVisibility(View.VISIBLE);
        view.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        mydataLay.setVisibility(View.VISIBLE);
        mydetailsLay.setVisibility(View.GONE);
        profileLay.setVisibility(View.VISIBLE);
        addPhoto.setVisibility(View.GONE);
        shippingLay.setVisibility(View.GONE);
        card_ship_address.setVisibility(View.VISIBLE);
        shippingAddressEdtLay.setVisibility(View.GONE);
        button_updateaddressLay.setVisibility(View.GONE);
        applyFont(view);

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            ((MainActivity) getActivity()).searchIcon.setVisible(false);
            final MainActivity mainActivity = (MainActivity) getActivity();
            mainActivity.search_layout.setVisibility(View.GONE);
            mainActivity.menu_Name.setVisible(false);
            mainActivity.toggle.setDrawerIndicatorEnabled(false);
            mainActivity.setTag = "profile";
            mainActivity.disableDrawer(true);
            mainActivity.toolbar.setTitle("" + getResources().getString(R.string.profile));
            ((MainActivity) getActivity()).toolbar.setLogo(R.drawable.transpent_logo);
            mainActivity.toolbar.setVisibility(View.GONE);
            final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
            mainActivity.toggle.setHomeAsUpIndicator(upArrow);
            mainActivity.toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mainActivity.onBackPressed();

                }
            });
            back_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (location_icon.getVisibility() == View.VISIBLE || edit_icon.getVisibility() == View.GONE) {
                        stateMaintain();
                    } else {

                        mainActivity.onBackPressed();
                    }

                }
            });


            showProgress(true);
            UserPresenter userPresenter = new UserPresenter(ProfileFragment.this);
            userPresenter.addProfilePage(SessionSave.getSession("user_id", getActivity()));

            edit_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    header_titleTxt.setText("" + getResources().getString(R.string.edit_profile));
                    header_titleTxt.setVisibility(View.VISIBLE);
                    mydataLay.setVisibility(View.GONE);
                    mydetailsLay.setVisibility(View.VISIBLE);
                    profileLay.setVisibility(View.GONE);
                    addPhoto.setVisibility(View.VISIBLE);
                    edit_icon.setVisibility(View.GONE);
                    shippingLay.setVisibility(View.GONE);
                    button_updateaddressLay.setVisibility(View.GONE);

                    location_icon.setVisibility(View.GONE);
                    appBarLayout.setExpanded(true);
                }
            });

            shippingLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    header_titleTxt.setText(R.string.billing_address);
                    header_titleTxt.setVisibility(View.VISIBLE);
                    mydataLay.setVisibility(View.GONE);
                    mydetailsLay.setVisibility(View.GONE);
                    location_icon.setVisibility(View.GONE);
                    profileLay.setVisibility(View.VISIBLE);
                    edit_icon.setVisibility(View.GONE);
                    button_updateaddressLay.setVisibility(View.VISIBLE);
                    shippingAddressEdtLay.setVisibility(View.VISIBLE);
                    appBarLayout.setExpanded(false);
                    card_ship_address.setVisibility(View.GONE);
                    ViewCompat.setNestedScrollingEnabled(mainTopLay, false);

                }
            });

            button_updateaddress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ship_name = edtshipName.getText().toString();
                    ship_email = edtshipEmail.getText().toString();
                    ship_phone = edtshipNumber.getText().toString();
                    ship_address1 = edtbuildingName.getText().toString();
                    ship_address2 = edtLocalityName.getText().toString();
                    ship_country_id = "" + countryId;
                    ship_city_id = "" + cityId;
                    ship_state = edtState.getText().toString();
                    ship_postalcode = edtPincode.getText().toString();

                    edtshipEmail.setError(null);
                    edtshipName.setError(null);
                    edtshipNumber.setError(null);
                    edtbuildingName.setError(null);
                    edtLocalityName.setError(null);
                    edtState.setError(null);
                    edtPincode.setError(null);
                    boolean cancel = false;
                    View focusView = null;


                    if (edtshipName.getText().toString().trim().isEmpty()) {

                        edtshipName.setError("" + getResources().getString(R.string.please_enter_username));
                        focusView = edtshipName;
                        cancel = true;
//                    edtName.setError();
                    } else if (edtshipEmail.getText().toString().trim().isEmpty()) {
                        edtshipEmail.setError("" + getResources().getString(R.string.please_enter_email));
                        focusView = edtshipEmail;
                        cancel = true;
                    } else if (!isValidEmail(edtshipEmail.getText().toString().trim())) {
                        edtshipEmail.setError("" + getResources().getString(R.string.please_enter_email_invaild));
                        focusView = edtshipEmail;
                        cancel = true;
                    } else if (edtshipNumber.getText().toString().trim().isEmpty()) {
                        edtshipNumber.setError("" + getResources().getString(R.string.please_enter_phone));
                        focusView = edtshipNumber;
                        cancel = true;
                    } else if (edtbuildingName.getText().toString().trim().isEmpty()) {
                        edtbuildingName.setError("" + getResources().getString(R.string.please_enter_building));
                        focusView = edtbuildingName;
                        cancel = true;
                    } else if (edtLocalityName.getText().toString().trim().isEmpty()) {
                        edtLocalityName.setError("" + getResources().getString(R.string.please_enter_street));
                        focusView = edtLocalityName;
                        cancel = true;
                    } else if (edtState.getText().toString().trim().isEmpty()) {
                        edtState.setError("" + getResources().getString(R.string.please_enter_state));
                        focusView = edtState;
                        cancel = true;
                    } else if (edtPincode.getText().toString().trim().isEmpty()) {
                        edtPincode.setError("" + getResources().getString(R.string.please_enter_pincode));
                        focusView = edtPincode;
                        cancel = true;
                    }
//

                    if (cancel) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        focusView.requestFocus();
                    } else {
                        if (cityId.equals("")) {
                            ((MainActivity) getActivity()).ShowToast(getActivity(), "No city found ");
                        } else {
                            if (NetworkStatus.isOnline(getActivity())) {
                                showProgress(true);
                                UserPresenter userPresenter = new UserPresenter(ProfileFragment.this);
                                if (selectedPath == null) {
                                    selectedPath = "";
                                }

                                System.out.println("ship_country_id -- > " + ship_country_id + " " + ship_city_id);
                                System.out.println("ship_country_id -- > " + ship_country_id + " " + ship_city_id);
                                userPresenter.editProfilePage(SessionSave.getSession("user_id", getActivity()), edtName.getText().toString().trim(), edtNumber.getText().toString().trim(), edtEmail.getText().toString().trim(),
                                        user_address1, user_address2, user_country_id,
                                        user_city_id, ship_id, ship_name, ship_email,
                                        ship_phone, ship_address1, ship_address2,
                                        ship_country_id, ship_city_id,
                                        ship_state, ship_postalcode, imageFile);

                            } else {
                                ShowToast(getActivity(), "" + getResources().getString(R.string.internet_connection));
                            }
                        }

                    }


                }


            });

            addPhoto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Log.d("profileimageView", "profileimageView: " + "profileimageView");
                    //getCamera();
                    if (checkPermissions()) {
                        getImage();
                    }
                }
            });
            button_updateinformation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    edtEmail.setError(null);
                    edtNumber.setError(null);
                    edtName.setError(null);
                    boolean cancel = false;
                    View focusView = null;

                    if (edtName.getText().toString().trim().isEmpty()) {
                        edtName.setError("" + getResources().getString(R.string.please_enter_username));
                        focusView = edtName;
                        cancel = true;
                    } else if (edtEmail.getText().toString().trim().isEmpty()) {
                        edtEmail.setError("" + getResources().getString(R.string.please_enter_email));
                        focusView = edtEmail;
                        cancel = true;
                    } else if (!isValidEmail(edtEmail.getText().toString().trim())) {
                        edtEmail.setError("" + getResources().getString(R.string.please_enter_email_invaild));
                        focusView = edtEmail;
                        cancel = true;
                    }
//                    else if (edtNumber.getText().toString().trim().isEmpty()) {
//                        edtNumber.setError("" + getResources().getString(R.string.please_enter_phone));
//                        focusView = edtNumber;
//                        cancel = true;
//                    }

                    if (cancel) {
                        // There was an error; don't attempt login and focus the first
                        // form field with an error.
                        focusView.requestFocus();
                    } else {
                        if (NetworkStatus.isOnline(getActivity())) {
                            showProgress(true);
                            if (selectedPath == null) {
                                selectedPath = "";
                            }
                            UserPresenter userPresenter = new UserPresenter(ProfileFragment.this);
                            userPresenter.editProfilePage(SessionSave.getSession("user_id", getActivity()), edtName.getText().toString().trim(), edtNumber.getText().toString().trim() + "", edtEmail.getText().toString().trim(),
                                    user_address1, user_address2, user_country_id,
                                    user_city_id, ship_id, ship_name, ship_email,
                                    ship_phone, ship_address1, ship_address2,
                                    ship_country_id, ship_city_id,
                                    ship_state, ship_postalcode, imageFile);


                        } else {
                            ShowToast(getActivity(), "" + getResources().getString(R.string.internet_connection));
                        }

                    }

                }
            });


            /*mycartLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Fragment ff = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                    if (ff instanceof HomeScreenFragment) {
                        mainActivity.setTag = "homepage";

                    } else if (ff instanceof ProductDetailsFragment) {
                        mainActivity.setTag = "productdeatils";

                    } else if (ff instanceof ProductListFragment) {
                        mainActivity.setTag = "productlist";
                    } else if (ff instanceof MainSubCategoryFragment) {
                        mainActivity.setTag = "subcategory";
                    } else if (ff instanceof DealsListFragment) {
                        mainActivity.setTag = "dealslist";
                    } else if (ff instanceof DealsListDetailsFragment) {
                        mainActivity.setTag = "dealsdeatils";
                    } else if (ff instanceof ProfileFragment) {
                        mainActivity.setTag = "profile";
                    } else if (ff instanceof WishListFragment) {
                        mainActivity.setTag = "wishlist";
                    }
                    mainActivity.setBadgeCount(getActivity(), SessionSave.getSession("cartCount", getActivity()));

                    CartListFragment fragment2 = new CartListFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragment2, mainActivity.setTag);
                    fragmentTransaction.addToBackStack("");
                    fragmentTransaction.commit();

                }
            });*/
            myordersLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    RewardsFragment fragment2 = new RewardsFragment(true, true);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragment2, mainActivity.setTag);
                    fragmentTransaction.addToBackStack("");
                    fragmentTransaction.commit();

                   /* OrderTabFragment fragment2 = new OrderTabFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragment2, mainActivity.setTag);
                    fragmentTransaction.addToBackStack("");
                    fragmentTransaction.commit();*/
                }
            });
            mywishListLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    WishListFragment fragment2 = new WishListFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragment2);
                    fragmentTransaction.addToBackStack("");
                    fragmentTransaction.commit();
                }
            });
            mychangepasswordLay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ChangePasswordFargment fragment2 = new ChangePasswordFargment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragment2);
                    fragmentTransaction.addToBackStack("");
                    fragmentTransaction.commit();
                }
            });

            location_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getActivity(), SearchLocationActivity.class);
                    getActivity().startActivityForResult(intent, 1000);
                }
            });


            spin_country.getBackground().setColorFilter(Color.parseColor("#ff2353"), PorterDuff.Mode.SRC_ATOP);
            spin_city.getBackground().setColorFilter(Color.parseColor("#ff2353"), PorterDuff.Mode.SRC_ATOP);
            try {
                if (!SessionSave.getSession("countrylist", getActivity()).equals("")) {

                    JSONObject jsonObject = new JSONObject(SessionSave.getSession("countrylist", getActivity()));


                    JSONArray country_details = jsonObject.getJSONArray("country_details");


                    if (country_details.length() > 0) {

                        for (int i = 0; i < country_details.length(); i++) {

                            defaultCity = 0;
                            String country_id = country_details.getJSONObject(i).getString("country_id");
                            String country_name = country_details.getJSONObject(i).getString("country_name");

                            JSONArray city_details = country_details.getJSONObject(i).getJSONArray("city_details");
                            mCityList = new ArrayList<>();

                            if (city_details.length() > 0) {
                                for (int j = 0; j < city_details.length(); j++) {
                                    String city_id = city_details.getJSONObject(j).getString("city_id");
                                    String city_name = city_details.getJSONObject(j).getString("city_name");
                                    String city_country_id = city_details.getJSONObject(j).getString("city_country_id");

                                    if (defaultCity == 0) {
                                        defaultCity++;
                                        CityListData cityListData = new CityListData("", "" + getResources().getString(R.string.city), "");
                                        mCityList.add(cityListData);
                                    }
                                    CityListData cityListData = new CityListData(city_id, city_name, city_country_id);
                                    mCityList.add(cityListData);
                                }
                            } else {
                                CityListData cityListData = new CityListData("", "" + getResources().getString(R.string.no_city_found), "");
                                mCityList.add(cityListData);
                            }
                            if (defaultCountry == 0) {
                                defaultCountry++;
                                CountryListData countryListData = new CountryListData("", "" + getResources().getString(R.string.country), mCityList);
                                mCountryList.add(countryListData);
                            }

                            CountryListData countryListData = new CountryListData(country_id, country_name, mCityList);
                            mCountryList.add(countryListData);

                        }
                        // Create custom adapter object ( see below CustomAdapter.java )
                        CoutryCityAdapter adapter = new CoutryCityAdapter(getActivity(), mCountryList, "Country");

                        // Set adapter to spinner
                        spin_country.setAdapter(adapter);

                    }

                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (
                Exception ex
        ) {
            ex.printStackTrace();
        }

    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void applyFont(View view) {


        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.userName));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.userNumber));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.userEmail));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.myOrdersTxt));
        ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.showallorder));
       // ApplyFont.applyBold(getActivity(), view.findViewById(R.id.mycartTxt));
      //  ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.mycart));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.mywishListTxt));
        ApplyFont.applyLightFont(getActivity(), view.findViewById(R.id.mywishList));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.changepasswordTxt));
        ApplyFont.applyMediumFont(getActivity(), view.findViewById(R.id.shippingAddress));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.header_titleTxt));
        ApplyFont.applyBold(getActivity(), view.findViewById(R.id.button_updateinformation));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.shippingAddressTxt));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.edtName));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.edtNumber));
        ApplyFont.applyFont(getActivity(), view.findViewById(R.id.edtEmail));


    }

    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mainTopLay.setVisibility(show ? View.GONE : View.VISIBLE);
            mainTopLay.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mainTopLay.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            mainTopLay.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


    /**
     * This is method for show the toast
     */
    public void ShowToast(Context contex, String message) {

        Toast toast = Toast.makeText(contex, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void getProfileData(JsonObject mJsonObject, int position) {

        try {
            Log.d("Result 1111", "onNext: " + mJsonObject);
            showProgress(false);
            stateMaintain();
            if (mJsonObject != null) {

                JSONObject json = new JSONObject(mJsonObject.toString());
                SessionSave.saveSession("shipping_details", "" + mJsonObject.toString(), getActivity());

                if (json.getString("status").equals("200")) {
                    edit_icon.setVisibility(View.VISIBLE);
                    header_titleTxt.setText("" + getResources().getString(R.string.profile));
                    header_titleTxt.setVisibility(View.VISIBLE);
                    mydataLay.setVisibility(View.VISIBLE);
                    mydetailsLay.setVisibility(View.GONE);
                    profileLay.setVisibility(View.VISIBLE);
                    addPhoto.setVisibility(View.GONE);
                    shippingLay.setVisibility(View.GONE);
                    card_ship_address.setVisibility(View.VISIBLE);
                    shippingAddressEdtLay.setVisibility(View.GONE);
                    JSONArray jsonArray = json.getJSONArray("user_details");
                    user_id = jsonArray.getJSONObject(0).getString("user_id");
                    user_name = jsonArray.getJSONObject(0).getString("user_name").trim();
                    user_email = jsonArray.getJSONObject(0).getString("user_email").trim();
                    user_phone = jsonArray.getJSONObject(0).getString("user_phone").trim();
                    user_address1 = jsonArray.getJSONObject(0).getString("user_address1").trim();
                    user_address2 = jsonArray.getJSONObject(0).getString("user_address2").trim();
                    user_country_id = jsonArray.getJSONObject(0).getString("user_country_id").trim();
                    user_city_id = jsonArray.getJSONObject(0).getString("user_city_id").trim();
                    user_image = jsonArray.getJSONObject(0).getString("user_city_id").trim();
                    userName.setText("" + jsonArray.getJSONObject(0).getString("user_name").trim());
                    userNumber.setText("" + jsonArray.getJSONObject(0).getString("user_phone").trim());
                    userEmail.setText("" + jsonArray.getJSONObject(0).getString("user_email").trim());
                    edtName.setText("" + jsonArray.getJSONObject(0).getString("user_name").trim());
                    edtNumber.setText("" + jsonArray.getJSONObject(0).getString("user_phone").trim());
                    edtEmail.setText("" + jsonArray.getJSONObject(0).getString("user_email").trim());
                    SessionSave.saveSession("user_name", user_name, getActivity());
                    if ((((MainActivity) getActivity()).menu_Name != null)) {
                        if (SessionSave.getSession("user_name", getActivity()).length() > 10) {
                            ((MainActivity) getActivity()).menu_Name.setTitle("Hi " + SessionSave.getSession("user_name", getActivity()).substring(0, 8) + "..!");

                        } else {
                            ((MainActivity) getActivity()).menu_Name.setTitle("Hi " + SessionSave.getSession("user_name", getActivity()) + "!");
                        }

                    }

                    if (jsonArray.getJSONObject(0).getString("user_image").trim().equals("")) {

                        profileImage.setImageResource(R.drawable.noimage);
                    } else {
                        SessionSave.saveSession("user_image", jsonArray.getJSONObject(0).getString("user_image").trim(), getActivity());
                        Picasso.get().load((jsonArray.getJSONObject(0).getString("user_image").trim())).error(R.drawable.noimage).into(profileImage);
                    }


                    if (SessionSave.getSession("user_image", getActivity()).equals("")) {
                        ((MainActivity) getActivity()).roundedLetterView.setVisibility(View.VISIBLE);
                        ((MainActivity) getActivity()).profile_image.setVisibility(View.GONE);
                    } else {
                        ((MainActivity) getActivity()).roundedLetterView.setVisibility(View.GONE);
                        ((MainActivity) getActivity()).profile_image.setVisibility(View.VISIBLE);
                        Picasso.get().load((SessionSave.getSession("user_image", getActivity()))).error(R.drawable.noimage).into(((MainActivity) getActivity()).profile_image);

                    }


                    JSONArray shipping_details = json.getJSONArray("shipping_details");
                    ship_id = shipping_details.getJSONObject(0).getString("ship_id").trim();
                    ship_name = shipping_details.getJSONObject(0).getString("ship_name").trim();
                    ship_email = shipping_details.getJSONObject(0).getString("ship_email").trim();
                    ship_phone = shipping_details.getJSONObject(0).getString("ship_phone").trim();
                    ship_address1 = shipping_details.getJSONObject(0).getString("ship_address1").trim();
                    ship_address2 = shipping_details.getJSONObject(0).getString("ship_address2").trim();
                    ship_country_id = shipping_details.getJSONObject(0).getString("ship_country_id").trim();
//                    if (ship_country_id.equals("0")) {
//                        ship_country_id = "1";
//                    }
                    ship_city_id = shipping_details.getJSONObject(0).getString("ship_city_id").trim();
//                    if (ship_city_id.equals("0")) {
//                        ship_city_id = "1";
//                    }
                    edtshipNumber.setText(ship_phone);
                    edtshipName.setText(ship_name);
                    edtshipEmail.setText(ship_email);
                    edtbuildingName.setText(ship_address1);
                    edtLocalityName.setText(ship_address2);
                    ship_state = shipping_details.getJSONObject(0).getString("ship_state").trim();
                    ship_postalcode = shipping_details.getJSONObject(0).getString("ship_postalcode").trim();
                    edtState.setText(ship_state);
                    edtPincode.setText(ship_postalcode);
                    defaultCountry = 0;
                    for (int i = 0; i < mCountryList.size(); i++) {
                        System.out.println("mCityList ---- > " + mCountryList.get(i).getCountry_name()
                                + " " + shipping_details.getJSONObject(0).getString("ship_country_name"));
                        if (mCountryList.get(i).getCountry_name().equalsIgnoreCase(shipping_details.getJSONObject(0).getString("ship_country_name").trim())) {
                            ship_country_id = mCountryList.get(i).getCountry_id();
                            user_country_id = mCountryList.get(i).getCountry_id();
                            spin_country.setSelection(i);
                            mCityList = mCountryList.get(i).getCityListDataArrayList();
                            CountryCityBaseAdapter adapter = new CountryCityBaseAdapter(getActivity(), mCityList);
                            // Set adapter to spinner
                            spin_city.setAdapter(adapter);
                            break;
                        } else {
                            countryId = "";
                            ship_country_id = "";
                            user_country_id = "";
                            mEmptyList.clear();
                            CityListData cityListData = new CityListData("", "City", "");
                            mEmptyList.add(cityListData);
                            CountryCityBaseAdapter countryCityBaseAdapter = new CountryCityBaseAdapter(getActivity(), mEmptyList);
                            spin_city.setAdapter(countryCityBaseAdapter);
                        }
                    }

                    for (int i = 0; i < mCityList.size(); i++) {

                        System.out.println("mCityList ---- > " + mCityList.get(i).getCity_name()
                                + " " + shipping_details.getJSONObject(0).getString("ship_city_name"));

                        if (mCityList.get(i).getCity_name().equalsIgnoreCase(shipping_details.getJSONObject(0).getString("ship_city_name").trim())) {

                            ship_city_id = mCityList.get(i).getCity_id();
                            spin_city.setSelection(i);
                            break;
                        }
                    }

// Listener called when spinner item selected
                    spin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                            if (!mCountryList.get(position).getCountry_id().equalsIgnoreCase("")) {

                                if (defaultCountry > 0) {
                                    // Create custom adapter object ( see below CustomAdapter.java )
                                    countryId = mCountryList.get(position).getCountry_id();
                                    ship_country_id = mCountryList.get(position).getCountry_id();
                                    user_country_id = mCountryList.get(position).getCountry_id();
                                    System.out.println("mCountryList " + mCountryList.get(position).getCityListDataArrayList().size());
                                    mCityList = mCountryList.get(position).getCityListDataArrayList();
                                    CountryCityBaseAdapter adapter = new CountryCityBaseAdapter(getActivity(), mCityList);
                                    // Set adapter to spinner
                                    spin_city.setAdapter(adapter);
                                }
                            } else {
                                countryId = "";
                                ship_country_id = "";
                                user_country_id = "";
                                mEmptyList.clear();
                                CityListData cityListData = new CityListData("", "City", "");
                                mEmptyList.add(cityListData);
                                CountryCityBaseAdapter countryCityBaseAdapter = new CountryCityBaseAdapter(getActivity(), mEmptyList);
                                spin_city.setAdapter(countryCityBaseAdapter);
                            }

                            System.out.println("mCountryList  cityId 1 " + countryId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });
                    spin_country.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            defaultCountry++;
                            return false;
                        }
                    });

                    // Listener called when spinner item selected
                    spin_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View v, int position, long id) {
                            System.out.println("mCountryList " + mCityList.size());

                            if (mCityList.size() > 0) {
                                cityId = mCityList.get(position).getCity_id();
                                ship_city_id = mCityList.get(position).getCity_id();
                                user_city_id = mCityList.get(position).getCity_id();
                            } else if (mEmptyList.size() > 0) {
                                cityId = mEmptyList.get(position).getCity_id();
                                ship_city_id = mEmptyList.get(position).getCity_id();
                                user_city_id = mEmptyList.get(position).getCity_id();
                            }


                            System.out.println("mCountryList  cityId " + cityId);
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });


//                    if(!shipping_details.getJSONObject(0).getString("ship_address1").equals("") &&
//                            !shipping_details.getJSONObject(0).getString("ship_address1").equals("")
//                            && !shipping_details.getJSONObject(0).getString("ship_address1").equals("")
//                            && !shipping_details.getJSONObject(0).getString("ship_address1").equals("")){
                    String text = "<font color=#000000>"
                            + shipping_details.getJSONObject(0).getString("ship_name").trim() + "</font>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_email").trim()
                            + "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_phone").trim() + "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_address1").trim() + "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_address2").trim() + "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_city_name").trim() + "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_country_name").trim() + "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_state").trim() + "</br>"
                            + "<br>"
                            + shipping_details.getJSONObject(0).getString("ship_postalcode").trim() + "</br>"
                            + "</font>";
                    shippingAddressTxt.setText(Html.fromHtml(text));
//                    }


//                    shippingAddressTxt.setText(""+ Html.fromHtml(address) +"\n" +Html.fromHtml(address1));
                    if (position == 1) {
                        ((MainActivity) getActivity()).showSnackBar(mainTopLay, "" + json.getString("message"));
                    }
                } else {

                    Log.d("Result 2222", "onNext: " + mJsonObject);
                    showProgress(false);
                    mydataLay.setVisibility(View.VISIBLE);
                    mydetailsLay.setVisibility(View.GONE);
                    profileLay.setVisibility(View.VISIBLE);
                    addPhoto.setVisibility(View.GONE);
                    edit_icon.setVisibility(View.VISIBLE);
                    shippingLay.setVisibility(View.GONE);
                    card_ship_address.setVisibility(View.VISIBLE);
                    shippingAddressEdtLay.setVisibility(View.GONE);
                    if (position == 1) {
                        ((MainActivity) getActivity()).showSnackBar(mainTopLay, "" + json.getString("message"));
                    }

                }


            } else {
                ((MainActivity) getActivity()).showSnackBar(mainTopLay, "" + "Sorry unable to reach server!!! ");

            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    private void getCamera() {

        new AlertDialog.Builder(getActivity()).setMessage("" + getResources().getString(R.string.choose_an_image)).setTitle("" + getResources().getString(R.string.profile_image)).setCancelable(true).setNegativeButton("" + getResources().getString(R.string.gallery), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                // TODO Auto-generated method stub
                System.gc();
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                getActivity().startActivityForResult(intent, 0);
                dialog.cancel();
            }
        }).setPositiveButton("" + getResources().getString(R.string.camera), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                // TODO Auto-generated method stub
                dialog.cancel();
                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                //  intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                final File photo = new File(Environment.getExternalStorageDirectory() + "/Le/img");
//                if (!photo.exists())
//                    photo.mkdirs();
                final String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
                //               final File mediaFile = new File(".");
                final File mediaFile = new File(Environment.getExternalStorageDirectory() + File.separator + "IMG_" + timeStamp + ".jpg");
                System.out.println("mediaFile " + mediaFile);
                selectedPath = mediaFile.toString();
                imageUri = Uri.fromFile(mediaFile);
                //imageUri = FileProvider.getUriForFile(getActivity(), BuildConfig.APPLICATION_ID + ".provider", mediaFile);

                intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                getActivity().startActivityForResult(intent, 1);

            }
        }).show();
    }

    /**
     * Function for getting image from sd card or camera
     */

    private void getImage() {
        CropImage.activity()
                .start(getContext(), ProfileFragment.this);
    }


    private boolean checkPermissions() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(getActivity(), p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            requestPermissions(listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String permissionsList[], int[] grantResults) {
        switch (requestCode) {
            case MULTIPLE_PERMISSIONS: {
                if (grantResults.length > 0) {
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "You need to give storage permission to upload image", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                    getImage();

                }
            }
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("IMAGE_TEST", "called " + requestCode);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            Log.e("IMAGE_TEST", "called inside");

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                imageUri = result.getUri();
                Log.e("API_RESPONSE", "uri: " + imageUri + " path:" + imageUri.getPath());
                setImageToImageFile(imageUri);
                //doc_type_image.setImageURI(imageUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Log.e("IMAGE_TEST", error + "");
            }
        }

        if (requestCode == 1000) {
            Log.d("data", "onActivityResult: " + data);
            if (data != null) {
                Log.d("data", "onActivityResult 1: " + data.getStringExtra("ship_address"));
                Log.d("data", "onActivityResult 2: " + data.getStringExtra("ship_city_name"));
                card_ship_address.setVisibility(View.GONE);
                shippingAddressEdtLay.setVisibility(View.VISIBLE);

                ship_address1 = data.getStringExtra("ship_address");
                ship_state = data.getStringExtra("ship_city_name");

                isProfileAddress(data.getStringExtra("ship_address"), (data.getStringExtra("ship_city_name")), (data.getStringExtra("ship_country_name")));

                String text = "<font color=#000000>" + ship_name + "</font> <br> " +
                        "<font color=#777777>" + data.getStringExtra("ship_address")
                        + "<br>"
                        + data.getStringExtra("ship_city_name")
                        + "<br>"
                        + data.getStringExtra("ship_country_name")
                        + "</font>";
                shippingAddressTxt.setText(Html.fromHtml(text));
            }
        }
    }

    private File getFileFromBitmap(Bitmap bitmap) {

        File file = new File(getContext().getCacheDir(), "tempfile.png");
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

        bitmap.compress(Bitmap.CompressFormat.PNG, 60, byteArrayOutputStream);
        FileOutputStream fileOutputStream = null;

        try {
            fileOutputStream = new FileOutputStream(file);
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fileOutputStream.close();
            } catch (IOException ie) {
                ie.printStackTrace();
            }
        }

        return file;

    }

    private void setImageToImageFile(Uri resultUri) {

        profileImage.setImageURI(resultUri);

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), resultUri);

            Bitmap scaledBitmap = BitmapUtils.getResizedBitmap(bitmap, 1024);

            if (scaledBitmap == null) {
                scaledBitmap = bitmap;
            }

            imageFile = getFileFromBitmap(scaledBitmap);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


//    @Override
//    public void onActivityResult(final int requestcode, final int resultcode, final Intent data) {
//        try {
//        //    Log.e("API_RESPONSE", "called onact " + imageUri.toString());
//            Log.d("API_RESPONSE", "onActivityResult: " + getActivity().RESULT_OK + "resultcode " + resultcode + " requestcode " + requestcode);
//            System.gc();
//
//            try {
//                Uri uriData = null;
//
//                if (resultcode == 1000) {
//                    Log.d("data", "onActivityResult: " + data);
//                    if (data != null) {
//                        Log.d("data", "onActivityResult 1: " + data.getStringExtra("ship_address"));
//                        Log.d("data", "onActivityResult 2: " + data.getStringExtra("ship_city_name"));
//                        card_ship_address.setVisibility(View.GONE);
//                        shippingAddressEdtLay.setVisibility(View.VISIBLE);
//
//                        ship_address1 = data.getStringExtra("ship_address");
//                        ship_state = data.getStringExtra("ship_city_name");
//
//                        isProfileAddress(data.getStringExtra("ship_address"), (data.getStringExtra("ship_city_name")), (data.getStringExtra("ship_country_name")));
//
//                        String text = "<font color=#000000>" + ship_name + "</font> <br> " +
//                                "<font color=#777777>" + data.getStringExtra("ship_address")
//                                + "<br>"
//                                + data.getStringExtra("ship_city_name")
//                                + "<br>"
//                                + data.getStringExtra("ship_country_name")
//                                + "</font>";
//                        shippingAddressTxt.setText(Html.fromHtml(text));
//                    }
//                } else if (resultcode == getActivity().RESULT_OK) {
//                    Log.e("API_RESPONSE", "called else if" + imageUri.toString());
//
//                    System.gc();
//                    switch (requestcode) {
//                        case 0:
//                            try {
//                                Uri uri = data.getData();
//                                selectedPath = ImageUtils.getPath(
//                                        /* context */getActivity(), uri);
//                                new ImageCompressionFromGellaryAsyncTask().execute(data.getData());
//                            } catch (final Exception e) {
//                                e.printStackTrace();
//                            }
//                            break;
//                        case 1:
//                            Log.e("API_RESPONSE", "called outside try" + imageUri.toString());
//
//                            try {
//                                Log.e("API_RESPONSE", "called on act result" + imageUri.toString());
//                                   new ImageCompressionAsyncTask().execute(imageUri.toString());
//
//                            } catch (final Exception e) {
//                                e.printStackTrace();
//                                Log.e("API_RESPONSE", "exception");
//                                Log.d("tag", "exception" + e.getMessage());
//                            }
//                            break;
//
//                    }
//                }
//
//
//            } catch (final Exception e) {
//                e.printStackTrace();
//            }
//
//        } catch (final Exception e) {
//            e.printStackTrace();
//        }
//    }

    private void isProfileAddress(String addresss, String city, String country) {
        try {

            if (!(SessionSave.getSession("shipping_details", getActivity()).equals(""))) {
                System.out.println("ship_name  ship_email  1" + SessionSave.getSession("shipping_details", getActivity()));

                JSONObject json = new JSONObject(SessionSave.getSession("shipping_details", getActivity()));
                JSONArray shipping_details = json.getJSONArray("shipping_details");
                ship_name = shipping_details.getJSONObject(0).getString("ship_name").trim();
                ship_email = shipping_details.getJSONObject(0).getString("ship_email").trim();
                ship_phone = shipping_details.getJSONObject(0).getString("ship_phone").trim();
                ship_address1 = shipping_details.getJSONObject(0).getString("ship_address1").trim();
                ship_address2 = addresss;
                ship_country_id = shipping_details.getJSONObject(0).getString("ship_country_id").trim();
                ship_city_id = shipping_details.getJSONObject(0).getString("ship_city_id").trim();
                ship_state = shipping_details.getJSONObject(0).getString("ship_state").trim();
                ship_postalcode = shipping_details.getJSONObject(0).getString("ship_postalcode").trim();

                edtshipName.setText(ship_name);
                edtshipEmail.setText(ship_email);
                edtshipNumber.setText(ship_phone);
                edtbuildingName.setText(ship_address1);
                edtLocalityName.setText(ship_address2);
                edtState.setText(ship_state);
                edtPincode.setText(ship_postalcode);

                for (int i = 0; i < mCityList.size(); i++) {

                    if (mCityList.get(i).getCity_name().equalsIgnoreCase(city)) {
                        ship_city_id = mCityList.get(i).getCity_id();
                        spin_city.setSelection(i);
                        break;
                    }
                }

                for (int i = 0; i < mCountryList.size(); i++) {

                    if (mCountryList.get(i).getCountry_name().equalsIgnoreCase(country)) {
                        ship_country_id = mCountryList.get(i).getCountry_id();
                        spin_country.setSelection(i);
                        break;
                    }
                }


            } else if (!(SessionSave.getSession("user_details", getActivity()).equals(""))) {

                JSONObject json = new JSONObject(SessionSave.getSession("user_details", getActivity()));
                JSONObject shipping_details = json.getJSONObject("user_details");
                ship_name = shipping_details.getString("user_name").trim();
                ship_email = shipping_details.getString("user_email").trim();
                ship_phone = shipping_details.getString("user_phone").trim();
                ship_address1 = shipping_details.getString("user_address1").trim();
                ship_address2 = addresss;
                ship_country_id = "";
                ship_city_id = "";
                ship_state = "";
                ship_postalcode = "";

                edtName.setText(ship_name);
                edtEmail.setText(ship_email);
                edtNumber.setText(ship_phone);
                edtbuildingName.setText(ship_address1);
                edtLocalityName.setText(ship_address2);
                edtState.setText(ship_state);
                edtPincode.setText(ship_postalcode);


            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private class ImageCompressionAsyncTask extends AsyncTask<String, Void, Bitmap> {
        private Dialog mDialog;
        private String result;
        private int orientation;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            final View view = View.inflate(getActivity(), R.layout.progress_bar, null);
            mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
            mDialog.setContentView(view);
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Bitmap doInBackground(final String... params) {
            try {
                result = getRealPathFromCemara(params[0]);
                final File file = new File(result);
                mBitmap = decodeImageFile(file);
                if (mBitmap != null) {
                    final Matrix matrix = new Matrix();
                    try {
                        final ExifInterface exif = new ExifInterface(file.getAbsolutePath());
                        orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, 1);
                        if (orientation == 3) {
                            matrix.postRotate(180);
                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                        } else if (orientation == 6) {
                            matrix.postRotate(90);
                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                        } else if (orientation == 8) {
                            matrix.postRotate(270);
                            mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), mBitmap.getHeight(), matrix, true);
                        }
                    } catch (final IOException e) {
                        e.printStackTrace();
                    }
                }
                final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                final byte[] image = stream.toByteArray();
                encodedImage = Base64.encodeToString(image, Base64.DEFAULT).trim();
            } catch (final Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowToast(getActivity(), "" + getResources().getString(R.string.please_select_image_from_valid_path));
                    }
                });
            }
            return mBitmap;
        }

        @Override
        protected void onPostExecute(final Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (mDialog.isShowing())
                mDialog.dismiss();
            profileImage.setImageBitmap(result);
        }
    }

    private class ImageCompressionFromGellaryAsyncTask extends AsyncTask<Uri, Void, Bitmap> {
        private Dialog mDialog;
        private String result;
        private int orientation;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            final View view = View.inflate(getActivity(), R.layout.progress_bar, null);
            mDialog = new Dialog(getActivity(), R.style.dialogwinddow);
            mDialog.setContentView(view);
            mDialog.setCancelable(false);
            mDialog.show();
        }

        @Override
        protected Bitmap doInBackground(final Uri... params) {
            try {
                result = getRealPathFromURI(params[0]);
                mBitmap = ImageUtils.decodeUri(params[0], getActivity());
                Matrix matrix = new Matrix();
                matrix.postRotate(ImageUtils.getAngle(selectedPath));
                int width = mBitmap.getWidth();
                int height = mBitmap.getHeight();

                mBitmap = Bitmap.createBitmap(mBitmap, 0, 0, width, height, matrix, true);
                final ByteArrayOutputStream stream = new ByteArrayOutputStream();
                mBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
                final byte[] image = stream.toByteArray();
                encodedImage = Base64.encodeToString(image, Base64.DEFAULT).trim();

            } catch (final Exception e) {
                // TODO: handle exception
                e.printStackTrace();
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ShowToast(getActivity(), "" + getResources().getString(R.string.please_select_image_from_valid_path));
                    }
                });
            }
            return mBitmap;
        }

        @Override
        protected void onPostExecute(final Bitmap result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (mDialog.isShowing())
                mDialog.dismiss();
            profileImage.setImageBitmap(result);
        }

    }


    private String getRealPathFromCemara(final String contentURI) {
        final Uri contentUri = Uri.parse(contentURI);
        final Cursor cursor = getActivity().getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null)
            return contentUri.getPath();
        else {
            cursor.moveToFirst();
            final int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(idx);
        }


    }

    public String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null,
                null, null, null);

        if (cursor == null) { // Source is Dropbox or other similar local file
            // path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            try {
                int idx = cursor
                        .getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            } catch (Exception e) {
                e.printStackTrace();
                result = "";
            }
            cursor.close();
        }
        return result;
    }


    public static Bitmap decodeImageFile(final File f) {
        try {
            final BitmapFactory.Options o = new BitmapFactory.Options();
            o.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(new FileInputStream(f), null, o);
            final int REQUIRED_SIZE = 100;
            int scale = 1;
            while (o.outWidth / scale / 2 >= REQUIRED_SIZE && o.outHeight / scale / 2 >= REQUIRED_SIZE)
                scale *= 2;
            final BitmapFactory.Options o2 = new BitmapFactory.Options();
            o2.inSampleSize = scale;
            return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
        } catch (final FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void stateMaintain() {
        ViewCompat.setNestedScrollingEnabled(mainTopLay, true);
        header_titleTxt.setText("" + getResources().getString(R.string.profile));
        header_titleTxt.setVisibility(View.VISIBLE);
        mydataLay.setVisibility(View.VISIBLE);
        mydetailsLay.setVisibility(View.GONE);
        profileLay.setVisibility(View.VISIBLE);
        addPhoto.setVisibility(View.GONE);
        location_icon.setVisibility(View.GONE);
        edit_icon.setVisibility(View.VISIBLE);
        button_updateaddressLay.setVisibility(View.GONE);
        shippingLay.setVisibility(View.GONE);
        card_ship_address.setVisibility(View.VISIBLE);
        shippingAddressEdtLay.setVisibility(View.GONE);
    }

    public void expanationAlert() {

        final androidx.appcompat.app.AlertDialog.Builder alertDialog = new androidx.appcompat.app.AlertDialog.Builder(getActivity(), R.style.AlertDialogCustom);
        // Setting Dialog Title
        alertDialog.setTitle("Message");
        // Setting Dialog did not cancel while click back button
        alertDialog.setCancelable(true);
        // Setting Dialog Message
        alertDialog.setMessage("" + "Turn on storage permission for the SaveUp app in your device settings");
        // On pressing Settings button
        alertDialog.setPositiveButton("" + "OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                malertmDialog.dismiss();

                final Intent i = new Intent();
                i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                i.addCategory(Intent.CATEGORY_DEFAULT);
                i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                getActivity().startActivity(i);
                getActivity().finish();


            }
        });
        alertDialog.setNegativeButton("" + "CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, final int which) {
                malertmDialog.dismiss();
                final Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        // on pressing cancel button

        // Showing Alert Message

        malertmDialog = alertDialog.show();
    }


}
