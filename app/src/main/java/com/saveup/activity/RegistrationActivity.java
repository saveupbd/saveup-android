package com.saveup.activity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import com.google.android.material.snackbar.Snackbar;
import android.text.SpannableStringBuilder;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.aapbd.appbajarlib.nagivation.StartActivity;
import com.saveup.adapter.CountryCityBaseAdapter;
import com.saveup.adapter.CoutryCityAdapter;
import com.saveup.android.R;
import com.saveup.model.CityListData;
import com.saveup.model.CountryListData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.AppConstant;
import com.saveup.utils.NetworkStatus;
import com.saveup.utils.SessionSave;
import com.saveup.utils.StringUtil;
import com.saveup.views.RippleView;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;

/**
 * @author AAPBD on 06,May,2017
 * @version 5.0
 * @Project MultiVendor
 * @Package com.aapbd.activity
 */


public class RegistrationActivity extends BaseActivity {


    @BindView(R.id.button_register)
    RippleView button_register;
    @BindView(R.id.edtName)
    EditText edtName;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.edtConfirmPassword)
    EditText edtConfirmPassword;
    @BindView(R.id.in_date)
    EditText edtBirthDay;
    @BindView(R.id.spin_country)
    Spinner spin_country;
    @BindView(R.id.spin_city)
    Spinner spin_city;
    @BindView(R.id.checkbox)
    CheckBox checkbox;

    @BindView(R.id.privacytextview)
    TextView privacytextview;

    private int mYear, mMonth, mDay, mHour, mMinute;
    private int coutryTouch = 0, cityTouch = 0;
    private String countryId = "", cityId = "";
    ArrayList<CountryListData> mCountryList = new ArrayList<>();
    ArrayList<CityListData> mCityList;
    ArrayList<CityListData> mCList = new ArrayList<>();
    private boolean ischecked = false;
    private View mProgressView;
    private View registrationform;
    String defaultTextForCountrySpinner = "Country";
    private int defaultCountry = 0, defaultCity = 0;
    private CountryCityBaseAdapter countryCityBaseAdapter;
    private ArrayList<CityListData> mEmptyList = new ArrayList<>();

    private String phoneNumber;

    @Override
    public int setLayout() {
        return R.layout.activity_registration;
    }

    @Override
    public void initialize() {
        StringUtil.mActivityList.add(this);
        mProgressView = findViewById(R.id.progressBar);
        registrationform = findViewById(R.id.registrationform);

        spin_country.setVisibility(View.INVISIBLE);
        spin_city.setVisibility(View.INVISIBLE);

        //user phone number from login activity
        phoneNumber = getIntent().getStringExtra("phone_number");

        spin_country.getBackground().setColorFilter(Color.parseColor("#ff2353"), PorterDuff.Mode.SRC_ATOP);
        spin_city.getBackground().setColorFilter(Color.parseColor("#ff2353"), PorterDuff.Mode.SRC_ATOP);
        try {
            if (!SessionSave.getSession("countrylist", RegistrationActivity.this).equals("")) {

                JSONObject jsonObject = new JSONObject(SessionSave.getSession("countrylist", RegistrationActivity.this));


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
                    CoutryCityAdapter adapter = new CoutryCityAdapter(RegistrationActivity.this, mCountryList, defaultTextForCountrySpinner);

                    // Set adapter to spinner
                    spin_country.setAdapter(adapter);

                    spin_country.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View v, MotionEvent event) {
                            defaultCountry++;
                            return false;
                        }
                    });
                    // Listener called when spinner item selected
                    spin_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {

                            // Create custom adapter object ( see below CustomAdapter.java )

                            System.out.println("mCountryList " + defaultCountry);

                            if (!mCountryList.get(position).getCountry_id().equalsIgnoreCase("")) {

                                countryId = mCountryList.get(position).getCountry_id();
                                mCList = mCountryList.get(position).getCityListDataArrayList();
                                countryCityBaseAdapter = new CountryCityBaseAdapter(RegistrationActivity.this, mCList);
                                // Set adapter to spinner
                                spin_city.setAdapter(countryCityBaseAdapter);


                            } else {
                                countryId = "";
                                mEmptyList.clear();
                                CityListData cityListData = new CityListData("", "City", "");
                                mEmptyList.add(cityListData);
                                countryCityBaseAdapter = new CountryCityBaseAdapter(RegistrationActivity.this, mEmptyList);
                                spin_city.setAdapter(countryCityBaseAdapter);
                            }

                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here

                        }

                    });


                    // Listener called when spinner item selected
                    spin_city.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                            if (mCList.size() > 0)
                                cityId = mCList.get(position).getCity_id();
                            else if (mEmptyList.size() > 0)
                                cityId = mEmptyList.get(position).getCity_id();
                            else
                                cityId = "";
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parentView) {
                            // your code here
                        }

                    });
                }

            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*
                hard coded country and city ID for save up only
                 */

                countryId = "1";
                cityId = "1";

                edtEmail.setError(null);
                edtPassword.setError(null);
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
                } else if (edtPassword.getText().toString().trim().isEmpty()) {
                    edtPassword.setError("" + getResources().getString(R.string.please_enter_password));
                    focusView = edtPassword;
                    cancel = true;
                } else if (edtConfirmPassword.getText().toString().trim().isEmpty()) {
                    edtConfirmPassword.setError("" + getResources().getString(R.string.please_confirm_password));
                    focusView = edtConfirmPassword;
                    cancel = true;
                } else if (!edtConfirmPassword.getText().toString().trim().matches(edtPassword.getText().toString().trim())) {
                    edtConfirmPassword.setError("" + getResources().getString(R.string.confirm_password_not_matched));
                    focusView = edtConfirmPassword;
                    cancel = true;
                } else if (edtBirthDay.getText().toString().trim().isEmpty() || edtBirthDay.getText().toString().trim().equals(getString(R.string.please_enter_birth_date))){
                    edtBirthDay.setError("" + getResources().getString(R.string.please_enter_birth_date));
                    focusView = edtBirthDay;
                    cancel = true;
                }
                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
//                    if (countryId.equals("")) {
//                        ShowToast(RegistrationActivity.this, "No country found ");
//                    } else if (cityId.equals("")) {
//                        ShowToast(RegistrationActivity.this, "No city found ");
//                    }
                    if (!ischecked) {
                        ShowToast(RegistrationActivity.this, "" + getResources().getString(R.string.please_verify_termscondition));
                    } else if (NetworkStatus.isOnline(RegistrationActivity.this)) {
                        showProgress(true, registrationform, mProgressView);
                        UserPresenter userPresenter = new UserPresenter(RegistrationActivity.this);
                        userPresenter.userRegisterationAPI(
                                edtName.getText().toString(),
                                edtEmail.getText().toString(),
                                edtPassword.getText().toString(),
                                edtBirthDay.getText().toString(),
                                countryId,
                                cityId,
                                SessionSave.getSession("device_token", RegistrationActivity.this),
                                phoneNumber);
                    } else {
                        ShowToast(RegistrationActivity.this, "" + getResources().getString(R.string.internet_connection));
                    }

                }

            }
        });

        checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ischecked = checkbox.isChecked();
            }
        });


        singleTextView(privacytextview, "I agree with ", "Terms ", "Privacy Policy.");

        edtBirthDay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(RegistrationActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                edtBirthDay.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

    }

    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }


    private static void singleTextView(TextView textView, final String introtext, final String terms, String privacy) {

        SpannableStringBuilder spanText = new SpannableStringBuilder();
        spanText.append(introtext);

        spanText.append(terms);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                // On Click Action
                StartActivity.toebsite(widget.getContext(), AppConstant.termsURL);

            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.RED);    // you can use custom color
                textPaint.setUnderlineText(true);    // this remove the underline
            }
        }, spanText.length() - terms.length(), spanText.length(), 0);

        spanText.append(" and ");
        spanText.append(privacy);
        spanText.setSpan(new ClickableSpan() {
            @Override
            public void onClick(View widget) {

                // On Click Action
                StartActivity.toebsite(widget.getContext(), AppConstant.PrivacyURL);

            }

            @Override
            public void updateDrawState(TextPaint textPaint) {
                textPaint.setColor(Color.RED);    // you can use custom color
                textPaint.setUnderlineText(true);    // this remove the underline
            }
        }, spanText.length() - privacy.length(), spanText.length(), 0);

        textView.setMovementMethod(LinkMovementMethod.getInstance());
        textView.setText(spanText, TextView.BufferType.SPANNABLE);

    }

    /**
     * This is method for show the toast
     */
    public void ShowToast(Context contex, String message) {

        Toast toast = Toast.makeText(contex, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }


    public void registerResult(JsonObject mJsonObject) {
        Log.d("Result", "onNext: " + mJsonObject);


        try {

            if (mJsonObject != null) {
                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {
//                    SessionSave.saveSession("language_details", "" + mJsonObject.toString(), RegistrationActivity.this);
//                    SessionSave.saveSession("shipping_details", "" + mJsonObject.toString(), RegistrationActivity.this);
//                    SessionSave.saveSession("client_id", json.getString("client_id"), RegistrationActivity.this);
//                    SessionSave.saveSession("user_details", mJsonObject.toString(), RegistrationActivity.this);
//                    SessionSave.saveSession("user_id", json.getJSONArray("user_details").getJSONObject(0).getString("user_id"), RegistrationActivity.this);
//                    SessionSave.saveSession("user_name", json.getJSONArray("user_details").getJSONObject(0).getString("user_name"), RegistrationActivity.this);
//                    SessionSave.saveSession("user_email", json.getJSONArray("user_details").getJSONObject(0).getString("user_email"), RegistrationActivity.this);
//                    SessionSave.saveSession("referral_code", json.getJSONArray("user_details").getJSONObject(0).getString("referral_code"), RegistrationActivity.this);
//                    SessionSave.saveSession("old_password", edtPassword.getText().toString(), RegistrationActivity.this);
//                    Log.d("referral_code>>>",json.getJSONArray("user_details").getJSONObject(0).getString("referral_code"));
                    // referrel_code = json.getJSONArray("user_details").getJSONObject(0).getString("referral_code");

                    showProgress(false, registrationform, mProgressView);
                    AlertDialog alertDialog = new AlertDialog.Builder(RegistrationActivity.this).create();
                    alertDialog.setMessage(json.getString("message"));
                    alertDialog.setCancelable(false);
                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();

                                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                                    //              ShowToast(RegistrationActivity.this, "" + json.getString("message"));
                                    startActivity(intent);
                                }
                            });
                    alertDialog.show();

//                    Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
//                      ShowToast(RegistrationActivity.this, "" + json.getString("message"));
//                    startActivity(intent);

//                    for (int i = 0; i < StringUtil.mActivityList.size(); i++) {
//                        StringUtil.mActivityList.get(i).finish();
//                        System.out.println("-----> registrationform ----->");
//                    }
//                    Intent service = new Intent(RegistrationActivity.this, PayPalAccessTokenService.class);
//                    startService(service);
                } else {
                    showProgress(false, registrationform, mProgressView);
                    ShowToast(RegistrationActivity.this, "" + json.getString("message"));

                }
            } else {

                registrationform.setVisibility(View.GONE);
                mProgressView.setVisibility(View.GONE);
                Snackbar.make(registrationform, "" + "Sorry unable to reach server!!!  ", Snackbar.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }
}
