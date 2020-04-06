package com.saveup.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.aapbd.appbajarlib.nagivation.StartActivity;
import com.aapbd.appbajarlib.network.NetInfo;
import com.aapbd.appbajarlib.notification.AlertMessage;
import com.aapbd.appbajarlib.notification.MyToast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.accountkit.Account;
import com.facebook.accountkit.AccountKit;
import com.facebook.accountkit.AccountKitCallback;
import com.facebook.accountkit.AccountKitError;
import com.facebook.accountkit.AccountKitLoginResult;
import com.facebook.accountkit.ui.AccountKitActivity;
import com.facebook.accountkit.ui.AccountKitConfiguration;
import com.facebook.accountkit.ui.LoginType;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.share.Sharer;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.JsonObject;
import com.saveup.android.R;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.NetworkStatus;
import com.saveup.utils.SessionSave;
import com.saveup.utils.StringUtil;
import com.saveup.views.RippleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static com.saveup.utils.AppConstant.referrel_code;

/**
 * Created by AAPBD on 10/11/16.
 */


public class LoginActivity extends BaseActivity implements EasyPermissions.PermissionCallbacks {
    @BindView(R.id.button_login)
    RippleView button_login;
    @BindView(R.id.button_guest)
    RippleView button_guest;
    @BindView(R.id.button_facebook_login)
    RippleView button_facebook_login;
    @BindView(R.id.google_sign_in_button)
    RippleView googleSignInButton;
    @BindView(R.id.edtEmail)
    EditText edtEmail;
    @BindView(R.id.edtPassword)
    EditText edtPassword;
    @BindView(R.id.registerButton)
    TextView newRegistration;
    @BindView(R.id.forgotPassword)
    TextView forgotPassword;
    private final String PENDING_ACTION_BUNDLE_KEY = "com.nyotshong.activity:PendingAction";
    private CallbackManager callbackManager;
    private GoogleSignInClient googleSignInClient;
    private GoogleSignInAccount account;
    private FirebaseAuth mAuth;
    private ShareDialog shareDialog;
    private PendingAction pendingAction = PendingAction.NONE;
    View loginForm, progressBar;

    private static final String EMAIL = "email";

    List<String> permissionNeeds = Arrays.asList("email", "public_profile");
    private AccessToken fbAccessToken;

    private static final int REQUEST_SMS_READ = 100;

// facebook account kit

    public static int APP_REQUEST_CODE = 99;
    public static int GOOGLE_SIGN_IN_REQUEST_CODE = 101;
    View mLayout;

    public static boolean isRegistration = true;
    public static boolean isFromGuestMode = false;

    private FacebookCallback<Sharer.Result> shareCallback = new FacebookCallback<Sharer.Result>() {
        @Override
        public void onCancel() {
            Log.d("HelloFacebook", "Canceled");
        }

        @Override
        public void onError(FacebookException error) {
            String title = getString(R.string.error);
            String alertMessage = error.getMessage();
            showResult(title, alertMessage);
        }

        @Override
        public void onSuccess(Sharer.Result result) {
            if (result.getPostId() != null) {
                String title = getString(R.string.success);
                String id = result.getPostId();
                String alertMessage = getResources().getString(R.string.success);
                showResult(title, alertMessage);
            }
        }

        private void showResult(String title, String alertMessage) {
            new AlertDialog.Builder(LoginActivity.this).setTitle(title).setMessage(alertMessage).setPositiveButton("OK", null).show();
        }
    };
    private LoginButton fbLoginButton;

    private enum PendingAction {
        NONE
    }

    @Override
    public int setLayout() {

        return R.layout.activity_login;
    }

    @Override
    public void initialize() {


        StringUtil.mActivityList.add(this);

        mLayout = findViewById(R.id.mainloginview);

        loginForm = findViewById(R.id.loginForm);
        progressBar = findViewById(R.id.progressBar);

        if (savedInstance != null) {
            String name = savedInstance.getString(PENDING_ACTION_BUNDLE_KEY);
            pendingAction = PendingAction.valueOf(name);
        }
        button_guest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SessionSave.saveSession("user_name", getString(R.string.username_guest), LoginActivity.this);

                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                hideKeyboard(LoginActivity.this);

                // Reset errors.
                edtEmail.setError(null);
                edtPassword.setError(null);
                boolean cancel = false;
                View focusView = null;

                if (edtEmail.getText().toString().trim().isEmpty()) {
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
                }

                if (cancel) {
                    // There was an error; don't attempt login and focus the first
                    // form field with an error.
                    focusView.requestFocus();
                } else {
                    if (NetworkStatus.isOnline(LoginActivity.this)) {
                        showProgress(true, loginForm, progressBar);
                        UserPresenter userPresenter = new UserPresenter(LoginActivity.this);
                        userPresenter.userLoginAPI(edtEmail.getText().toString(), edtPassword.getText().toString(),
                                SessionSave.getSession("device_token", LoginActivity.this));
                    } else {
                        ShowToast(LoginActivity.this, "" + getResources().getString(R.string.internet_connection));
                    }
                }
            }
        });

        newRegistration.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //        clearFocus();
                isRegistration = true;
//                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
//                startActivity(intent);
              //  checkSMSPermission();

                phoneLogin(new View(LoginActivity.this));
            }
        });

        callbackManager = CallbackManager.Factory.create();
        fbLoginButton = findViewById(R.id.loginFacebookButton);
        fbLoginButton.setReadPermissions(permissionNeeds);
        // If you are using in a fragment, call loginButton.setFragment(this);

        // Callback registration
        fbLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                //Toast.makeText(LoginActivity.this, "Successful login!", Toast.LENGTH_SHORT).show();
                Log.d("FACEBOOK_LOGIN_RESULT", "success");
                callFbGraphApi(loginResult.getAccessToken());
                // updateUI();
            }

            @Override
            public void onCancel() {
                Log.d("FACEBOOK_LOGIN_RESULT", "cancel");
                // App code
//                if (pendingAction != PendingAction.NONE) {
//                    showAlert();
//                    pendingAction = PendingAction.NONE;
//                }
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
//                if (pendingAction != PendingAction.NONE && exception instanceof FacebookAuthorizationException) {
//                    showAlert();
//                    pendingAction = PendingAction.NONE;
//                }
                Log.d("FACEBOOK_LOGIN_RESULT", "error: " + String.valueOf(exception.toString()));
                //  Toast.makeText(LoginActivity.this, exception.toString(), Toast.LENGTH_SHORT).show();
            }

            private void showAlert() {
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle(R.string.cancelled).setMessage(R.string.permission_not_granted)
                        .setPositiveButton("OK", null).show();
            }
        });

        button_facebook_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (fbAccessToken != null && !fbAccessToken.isExpired()) {
                    callFbGraphApi(fbAccessToken);
                } else {
                    fbLoginButton.performClick();
                }
//
//                LoginManager.getInstance().logOut();
//               clearFocus();
//                fbLoginButton.performClick();
            }
        });

//        shareDialog = new ShareDialog(this);
//        shareDialog.registerCallback(callbackManager, shareCallback);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // clearFocus();

//                isRegistration = false;
//                checkSMSPermission();
//
               StartActivity.toActivity(LoginActivity.this, FogetPasswordScreen.class);

            }
        });


        //googleSignInButton.setSize(SignInButton.SIZE_STANDARD);
        googleSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = googleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN_REQUEST_CODE);

            }
        });
        mAuth = FirebaseAuth.getInstance();

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleSignInClient = GoogleSignIn.getClient(this, gso);
    }

    @Override
    protected void onStart() {
        super.onStart();
        account = GoogleSignIn.getLastSignedInAccount(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume  " + "Login Activity");
        fbAccessToken = AccessToken.getCurrentAccessToken();
    }

    private void clearFocus() {
        edtEmail.setError(null);
        edtPassword.setError(null);
    }


    private void callFbGraphApi(AccessToken accessToken) {

        if (!NetInfo.isOnline(LoginActivity.this)) {
            Toast.makeText(this, R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }

        GraphRequest request = GraphRequest.newMeRequest(
                accessToken,
                new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object,
                                            final GraphResponse response) {

                        Log.e("FACEBOOK_LOGIN_RESULT", response.toString());

                        if (object != null) {
                            try {
                                final String fbid = object.getString("id");

                                final String userImage = new String(
                                        "http://graph.facebook.com/" + fbid + "/picture?type=large");

                                final String fullName = object.getString("name");
                                final String name = fullName.split(" ")[0];
                                final String email = object.getString("email");

                                //need to call api after getting response
                                showProgress(true, loginForm, progressBar);
                                UserPresenter userPresenter = new UserPresenter(LoginActivity.this);
                                userPresenter.userFacebookAPI(email, fbid, name, userImage, SessionSave.getSession("device_token", LoginActivity.this));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(LoginActivity.this, R.string.no_internet, Toast.LENGTH_SHORT).show();
                        }
                    }


                });
        Bundle parameters = new Bundle();
        parameters.putString("fields",
                "id,name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    private void updateUI() {
        boolean enableButtons = AccessToken.getCurrentAccessToken() != null;
        final Profile profile = Profile.getCurrentProfile();
        System.out.println("success" + enableButtons);
        System.out.println("success profile" + profile);
        if (enableButtons && profile != null) {
            System.out.println("success 1");
            Bundle params = new Bundle();
            params.putString("fields", "id,name,email");
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", params, HttpMethod.GET, new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {
                    try {
                        JSONObject data = response.getJSONObject();
                        if (data.has("email")) {
                            String emailid = data.getString("email");
                            showProgress(true, loginForm, progressBar);
                            UserPresenter userPresenter = new UserPresenter(LoginActivity.this);
                            // userPresenter.userFacebookAPI(emailid, profile.getId(), profile.getName());
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).executeAsync();
        } else {

            Bundle params = new Bundle();
            params.putString("fields", "id,name,email");
            new GraphRequest(AccessToken.getCurrentAccessToken(), "/me", params, HttpMethod.GET, new GraphRequest.Callback() {
                public void onCompleted(GraphResponse response) {

                    try {
                        Log.e("JSON", response.toString());
                        JSONObject data = response.getJSONObject();

                        String emailid = "";
                        if (data.has("email")) {
                            emailid = data.getString("email");

                            try {
                                showProgress(true, loginForm, progressBar);
                                UserPresenter userPresenter = new UserPresenter(LoginActivity.this);
                                userPresenter.userFacebookAPI(emailid, String.valueOf(data.getInt("id")), data.getString("name"), "", "");


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }).executeAsync();
        }
    }


    public boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
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

    public void loginResult(JsonObject mJsonObject) {
        Log.d("Result", "onNext: " + mJsonObject);

        LoginManager.getInstance().logOut();
        if (mJsonObject != null) {
            try {
                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {
                    Log.e("status", json.getString("status"));
                    SessionSave.saveSession("shipping_details", "" + mJsonObject.toString(), LoginActivity.this);
                    SessionSave.saveSession("language_details", "" + mJsonObject.toString(), LoginActivity.this);

                    SessionSave.saveSession("user_details", mJsonObject.toString(), LoginActivity.this);
                    SessionSave.saveSession("user_id", json.getJSONArray("user_details").getJSONObject(0).getString("user_id"), LoginActivity.this);
                    SessionSave.saveSession("user_name", json.getJSONArray("user_details").getJSONObject(0).getString("user_name"), LoginActivity.this);
                    SessionSave.saveSession("user_email", json.getJSONArray("user_details").getJSONObject(0).getString("user_email"), LoginActivity.this);
                    SessionSave.saveSession("old_password", edtPassword.getText().toString(), LoginActivity.this);
                    SessionSave.saveSession("user_image", json.getJSONArray("user_details").getJSONObject(0).getString("user_image").trim(), LoginActivity.this);
                    SessionSave.saveSession("referral_code", json.getJSONArray("user_details").getJSONObject(0).getString("referral_code"), LoginActivity.this);
                    Log.d("referral_code>>>",json.getJSONArray("user_details").getJSONObject(0).getString("referral_code"));

                    referrel_code = json.getJSONArray("user_details").getJSONObject(0).getString("referral_code");
                    if (json.has("cart_count")) {
                        SessionSave.saveSession("cartCount", json.getString("cart_count"), LoginActivity.this);
                    } else {
                        SessionSave.saveSession("cartCount", "0", LoginActivity.this);
                    }
                    SessionSave.saveSession("client_id", json.getString("client_id"), LoginActivity.this);


                    if(!isFromGuestMode){
                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                    isFromGuestMode = false;
                    finish();
                    for (int i = 0; i < StringUtil.mActivityList.size(); i++) {
                        StringUtil.mActivityList.get(i).finish();
                    }

                } else {
                    showProgress(false, loginForm, progressBar);
                    ShowToast(LoginActivity.this, "" + json.getString("message"));

                }
            } catch (Exception ex) {
                ex.printStackTrace();
                showProgress(false, loginForm, progressBar);
                successAlertView(LoginActivity.this, ex.toString());
            }
        } else {

            showProgress(false, loginForm, progressBar);
            successAlertView(LoginActivity.this, "Sorry unable to reach server!!!  ");

        }


    }

    public void phoneLogin(final View view) {

        final Intent intent = new Intent(this, AccountKitActivity.class);
        AccountKitConfiguration.AccountKitConfigurationBuilder configurationBuilder =
                new AccountKitConfiguration.AccountKitConfigurationBuilder(
                        LoginType.PHONE,
                        AccountKitActivity.ResponseType.TOKEN); // or .ResponseType.TOKEN
        // ... perform additional configuration ...
        intent.putExtra(
                AccountKitActivity.ACCOUNT_KIT_ACTIVITY_CONFIGURATION,
                configurationBuilder.build());
        this.startActivityForResult(intent, APP_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {

        Log.e("onActivityResult", "is called requestCode-> "+requestCode+ " resultCode "+resultCode);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == APP_REQUEST_CODE) { // confirm that this response matches your request
            AccountKitLoginResult loginResult = data.getParcelableExtra(AccountKitLoginResult.RESULT_KEY);
            String toastMessage;
            if (loginResult.getError() != null) {
                toastMessage = loginResult.getError().getErrorType().getMessage();

                AlertMessage.showMessage(this, "Facebook login error", loginResult.getError().toString());
            } else if (loginResult.wasCancelled()) {
                toastMessage = "Login Cancelled";
            } else {
                if (loginResult.getAccessToken() != null) {
                    toastMessage = "Success:" + loginResult.getAccessToken().getAccountId();
                } else {
                    toastMessage = String.format(
                            "Success:%s...",
                            loginResult.getAuthorizationCode().substring(0, 10));
                }
                Log.e("PHONE_NUMBER", "called");
                AccountKit.getCurrentAccount(new AccountKitCallback<Account>() {
                    @Override
                    public void onSuccess(Account account) {
                        Log.e("PHONE_NUMBER", "called: " + account.getPhoneNumber().toString());

                        if (isRegistration) {
                            startActivity(new Intent(LoginActivity.this, RegistrationActivity.class).putExtra("phone_number", account.getPhoneNumber().toString()));
                            //StartActivity.toActivity(this, RegistrationActivity.class);
                        } else {
                            StartActivity.toActivity(LoginActivity.this, FogetPasswordScreen.class);

                        }
                    }

                    @Override
                    public void onError(AccountKitError accountKitError) {
                        Log.e("PHONE_NUMBER", "error: " + accountKitError.toString());

                    }
                });


            }


        }else if(requestCode == GOOGLE_SIGN_IN_REQUEST_CODE){
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);

                String email = account.getEmail();
                String name = account.getDisplayName();
                String userImage = String.valueOf(account.getPhotoUrl());
                UserPresenter userPresenter = new UserPresenter(LoginActivity.this);
                userPresenter.userGmailAPI(email, name, userImage, SessionSave.getSession("device_token", LoginActivity.this));
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Can Not Sign in through gmail because "+e.getMessage(), Toast.LENGTH_LONG);
            }
        }
    }


    // easy permission part

    @AfterPermissionGranted(REQUEST_SMS_READ)
    private void checkSMSPermission() {
        String[] perms = {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};
        if (EasyPermissions.hasPermissions(this, perms)) {
            // Already have permission, do the thing
            // ...

            Log.e("SMS permission", "Has given , method checkSMSPermission");

            /*
            call the Account kit
             */
            phoneLogin(new View(this));


        } else {
            // Do not have permissions, request them now

            callPermissionRequestAction();

        }

    }


    /*
    call permission request action
     */
    private void callPermissionRequestAction() {

        String[] perms = {Manifest.permission.READ_SMS, Manifest.permission.RECEIVE_SMS};

        EasyPermissions.requestPermissions(LoginActivity.this, getString(R.string.permission_sms_rationale),
                REQUEST_SMS_READ, perms);

    }

    /**
     * Callback received when a permissions request has been completed.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);

    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {


        MyToast.showToast(this, this.getString(R.string.permision_available_camera));


    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {


        MyToast.showToast(this, this.getString(R.string.permissions_not_granted));

    }

    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
