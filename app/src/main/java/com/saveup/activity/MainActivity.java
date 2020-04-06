package com.saveup.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.snackbar.Snackbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.core.widget.NestedScrollView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.aapbd.appbajarlib.nagivation.StartActivity;
import com.github.pavlospt.roundedletterview.RoundedLetterView;
import com.google.gson.JsonObject;
import com.saveup.adapter.LanguageAdapter;
import com.saveup.android.R;
import com.saveup.fragment.CartListFragment;
import com.saveup.fragment.ChangePasswordFargment;
import com.saveup.fragment.DealBuyNowFragment;
import com.saveup.fragment.DealsListDetailsFragment;
import com.saveup.fragment.DealsListFragment;
import com.saveup.fragment.DealsReviewFragment;
import com.saveup.fragment.FreeGiftFragment;
import com.saveup.fragment.HomeScreenFragment;
import com.saveup.fragment.HowToFragment;
import com.saveup.fragment.OrderDetailsFragment;
import com.saveup.fragment.OrderTabFragment;
import com.saveup.fragment.PaymentContinueFragment;
import com.saveup.fragment.ProductBuyNowFragment;
import com.saveup.fragment.ProductDetailsFragment;
import com.saveup.fragment.ProductListFragment;
import com.saveup.fragment.ProductReviewFragment;
import com.saveup.fragment.ProfileFragment;
import com.saveup.fragment.RedemptionFragment;
import com.saveup.fragment.RewardsFragment;
import com.saveup.fragment.SearchLoactionFragment;
import com.saveup.fragment.SearchViewFragment;
import com.saveup.fragment.StoreListFragment;
import com.saveup.fragment.StoreReviewFragment;
import com.saveup.fragment.WishListFragment;
import com.saveup.model.LanguageData;
import com.saveup.model.Product;
import com.saveup.model.SubCategoryData;
import com.saveup.presenter.UserPresenter;
import com.saveup.utils.ApplyFont;
import com.saveup.utils.RoundedImageView;
import com.saveup.utils.SessionSave;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by AAPBD on 10/11/16.
 */


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        ProductReviewFragment.TextClicked, StoreReviewFragment.storeReview,
        DealsReviewFragment.dealsReview {

    private String TAG = MainActivity.class.getSimpleName();
    public DrawerLayout drawer;
    public NavigationView navigationView;
    private ArrayList<Product> homeCategoryDataArrayList = new ArrayList<>();
    public ArrayList<SubCategoryData> subCategoryDataArrayList = new ArrayList<>();

    public RelativeLayout search_layout;
    private LinearLayout mLinearListView;
    boolean isFirstViewClick = false;
    boolean isSecondViewClick = false;
    boolean isThirdViewClick = false;
    ArrayList<Product.SubCategory> subCategories;
    ArrayList<Product.SubCategoryOne> subCategoryOnes;
    ArrayList<Product.ItemList> itemLists;
    public Toolbar toolbar;
    public ActionBarDrawerToggle toggle;
    public LayerDrawable icon;
    private Context con;
    public boolean isCategoryTouched = true, isTouchFromHome = true, isSearchForMerchants = false;
    String main_category_id = "",
            sec_category_id = "",
            sub_category_id = "",
            sub_sec_category_id = "";
    public String titleTxt = "", productTitle = "", productList = "";
    public MenuItem /*cartItem,*/ cartIconempty, menu_Name, editIcon, action_search, searchIcon;
    public String setTag = "";
    public NestedScrollView nestedScrollView;
    public EditText searchView;
    public TextView coutTxt;
    public View mLinearView3;
    public LinearLayout counterBackgroundLay;
    public boolean isSearchView = false;
    private Dialog alertDialog;
    public ProgressBar country_loader;
    public RoundedImageView profile_image;
    public RoundedLetterView roundedLetterView;
    LanguageData languageData;
    public boolean latestDealSearch = false,  hotDealSearch = false, giftDealSearch = false;
    public int oderFragmentTabIndex;
    public static boolean isGuestMode = false;

    TextView username;

    TextView footer_logout;
    TextView footer_account;
    TextView footer_store;
    TextView myVoucher;
    TextView myRewards;
    TextView nearbystore;
    TextView helpMenu;
    TextView howToMenu;
    TextView getFreeVoucher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // setDetaultLocale();
        con = this;
        setContentView(R.layout.activity_main);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        search_layout = findViewById(R.id.search_layout);
        searchView = findViewById(R.id.searchView);
        navigationView = findViewById(R.id.nav_view);
        nestedScrollView = findViewById(R.id.nestedScrollView);

        View headerView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        username = headerView.findViewById(R.id.username);
        profile_image = headerView.findViewById(R.id.profile_image);

        roundedLetterView = headerView.findViewById(R.id.rlv_name_view);
        roundedLetterView.setTitleText("" + SessionSave.getSession("user_name", MainActivity.this).toString().toUpperCase().charAt(0));
        roundedLetterView.setBackgroundColor(getResources().getColor(R.color.colorAccent));
        drawer = findViewById(R.id.drawer_layout);
        mLinearListView = findViewById(R.id.linear_listview);
        country_loader = findViewById(R.id.country_loader);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        ApplyFont.applyFont(MainActivity.this, findViewById(R.id.linear_listview));
        ApplyFont.applyFont(MainActivity.this, navigationView.findViewById(R.id.footerLay));

        LayoutInflater inflater3 = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mLinearView3 = inflater3.inflate(R.layout.cartititleview, null);
        coutTxt = mLinearView3.findViewById(R.id.counterBackground);
        counterBackgroundLay = mLinearView3.findViewById(R.id.counterBackgroundLay);
        counterBackgroundLay.setVisibility(View.GONE);
        toolbar.addView(mLinearView3);
        mLinearView3.setVisibility(View.GONE);
        toolbar.setLogo(R.mipmap.home_logo);
        toolbar.setTitle("");


        footer_logout = navigationView.findViewById(R.id.logoutTxt);
      //  TextView footer_cart = navigationView.findViewById(R.id.mycartTxt);
        footer_account = navigationView.findViewById(R.id.myaccountTxt);
        footer_store = navigationView.findViewById(R.id.storesTxt);
        myVoucher = navigationView.findViewById(R.id.myVoucher);
        myRewards = navigationView.findViewById(R.id.myRewards);
        nearbystore = navigationView.findViewById(R.id.nearbystore);
        helpMenu = navigationView.findViewById(R.id.helptxt);
        howToMenu = navigationView.findViewById(R.id.howTotxt);
        getFreeVoucher = navigationView.findViewById(R.id.getFreeVoucher);

        /*
        hide temporarily by biplob
        have to fix later
         */

        getFreeVoucher.setVisibility(View.GONE);


        TextView languageTxt = navigationView.findViewById(R.id.languageTxt);

        //don't need multiple language for saveup

//        try {
//            JSONObject json = new JSONObject(SessionSave.getSession("language_details", MainActivity.this));
//            if (json.has("Languages")) {
//
//                JSONArray Languages = json.getJSONArray("Languages");
//                if (Languages.length() > 0)
//                    languageTxt.setVisibility(View.VISIBLE);
//                else
//                    languageTxt.setVisibility(View.GONE);
//            } else {
//                languageTxt.setVisibility(View.GONE);
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        languageTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.START);
                language_alert_view(MainActivity.this);
            }
        });

        footer_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartIconempty.setVisible(false);
              //  cartItem.setVisible(false);
                editIcon.setVisible(false);
                menu_Name.setVisible(false);
                ProfileFragment fragment2 = new ProfileFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });
//
//        getFreeVoucher
//                FreeGiftFragment


        myVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.START);
                cartIconempty.setVisible(false);
             //   cartItem.setVisible(false);
                editIcon.setVisible(false);
                menu_Name.setVisible(false);
                RewardsFragment fragment2 = new RewardsFragment(true,false);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, "reward");
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
               /* OrderTabFragment fragment2 = new OrderTabFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, "voucher");
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();*/
            }
        });

        myRewards.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.START);
                cartIconempty.setVisible(false);
             //   cartItem.setVisible(false);
                editIcon.setVisible(false);
                menu_Name.setVisible(false);
                RewardsFragment fragment2 = new RewardsFragment(false, false);
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, "reward");
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });

        getFreeVoucher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.START);
                cartIconempty.setVisible(false);
              //  cartItem.setVisible(false);
                editIcon.setVisible(false);
                menu_Name.setVisible(false);
                FreeGiftFragment fragment2 = new FreeGiftFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, "free");
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });

        nearbystore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(Gravity.START);
                cartIconempty.setVisible(false);
              //  cartItem.setVisible(false);
                editIcon.setVisible(false);
                menu_Name.setVisible(false);
                SearchLoactionFragment fragment2 = new SearchLoactionFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, setTag);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        });


        helpMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StartActivity.toActivity(con, HelpViewActivity.class);


            }
        });
        howToMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                drawer.closeDrawer(Gravity.START);
                cartIconempty.setVisible(false);
                //   cartItem.setVisible(false);
                editIcon.setVisible(false);
                menu_Name.setVisible(false);
                HowToFragment fragment2 = new HowToFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, "howTo");
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();

            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initiateSearch();
            }
        });
        footer_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cartIconempty.setVisible(false);
              //  cartItem.setVisible(false);
                editIcon.setVisible(false);
                menu_Name.setVisible(false);
                drawer.closeDrawer(Gravity.START);
                final Fragment ff = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (!(ff instanceof StoreListFragment)) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean("isfromMap", false);
                    bundle.putString("store_id", "1");
                    StoreListFragment fragment2 = new StoreListFragment();
                    FragmentManager fragmentManager = getSupportFragmentManager();
                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                    fragmentTransaction.add(R.id.fragment_container, fragment2);
                    fragment2.setArguments(bundle);
                    fragmentTransaction.addToBackStack("");
                    fragmentTransaction.commit();
                }
            }
        });

/*
        footer_cart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Fragment ff = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
                if (ff instanceof HomeScreenFragment) {
                    setTag = "homepage";
                } else if (ff instanceof ProductDetailsFragment) {
                    setTag = "productdeatils";
                } else if (ff instanceof ProductListFragment) {
                    setTag = "productlist";
                } else if (ff instanceof MainSubCategoryFragment) {
                    setTag = "subcategory";
                } else if (ff instanceof DealsListFragment) {
                    setTag = "dealslist";
                } else if (ff instanceof DealsListDetailsFragment) {
                    setTag = "dealsdeatils";
                } else if (ff instanceof ProfileFragment) {
                    setTag = "profile";
                } else if (ff instanceof WishListFragment) {
                    setTag = "wishlist";
//                } else if (ff instanceof FreeGiftFragment) {
//                    setTag = "free_voucher";
                }
                menu_Name.setVisible(false);
                CartListFragment fragment2 = new CartListFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2, setTag);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();

            }
        });*/

        footer_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startLoginActivity();
            }
        });


        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = new HomeScreenFragment();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.detach(fragment).attach(fragment).commit();
//        Intent service = new Intent(MainActivity.this, PayPalAccessTokenService.class);
//        startService(service);

        if (SessionSave.getSession("user_image", MainActivity.this).equals("")) {
            roundedLetterView.setVisibility(View.GONE);
            profile_image.setVisibility(View.VISIBLE);
            profile_image.setBackground(getResources().getDrawable(R.drawable.noimage));
        } else {
            roundedLetterView.setVisibility(View.GONE);
            profile_image.setVisibility(View.VISIBLE);
            Picasso.get().load((SessionSave.getSession("user_image", MainActivity.this))).error(R.drawable.noimage).into(profile_image);
        }
//        try {
//
//            if (!SessionSave.getSession("category_list", MainActivity.this).equals("")) {
//                JSONObject json = new JSONObject(SessionSave.getSession("category_list", MainActivity.this));
//                System.out.println("category_list--->" + json);
//
//                if (json.getString("status").equals("200")) {
//
//
//                    JSONArray category_details = json.getJSONArray("categories_list");
//
//                    homeCategoryDataArrayList.clear();
//                    if (category_details.length() > 0) {
//
//                        for (int i = 0; i < category_details.length(); i++) {
//
//                            String category_id = category_details.getJSONObject(i).getString("category_id");
//                            String category_name = category_details.getJSONObject(i).getString("category_name");
//
//                            JSONArray sub_category_list = category_details.getJSONObject(i).getJSONArray("sub_category_list");
//
//                            subCategories = new ArrayList<>();
//
//                            if (sub_category_list.length() > 0) {
//                                for (int j = 0; j < sub_category_list.length(); j++) {
//
//                                    String category_id1 = sub_category_list.getJSONObject(j).getString("category_id");
//                                    String category_name2 = sub_category_list.getJSONObject(j).getString("category_name");
//                                    JSONArray sub_category_list1 = sub_category_list.getJSONObject(j).getJSONArray("sub_category_list");
//
//                                    subCategoryOnes = new ArrayList<>();
//                                    if (sub_category_list1.length() > 0) {
//
//                                        for (int m = 0; m < sub_category_list1.length(); m++) {
//
//                                            String category_id2 = sub_category_list1.getJSONObject(m).getString("category_id");
//                                            String category_name3 = sub_category_list1.getJSONObject(m).getString("category_name");
//                                            JSONArray sub_category_list2 = sub_category_list1.getJSONObject(m).getJSONArray("sub_category_list");
//
//                                            itemLists = new ArrayList<>();
//                                            if (sub_category_list2.length() > 0) {
//
//                                                for (int k = 0; k < sub_category_list2.length(); k++) {
//
//                                                    String category_id4 = sub_category_list2.getJSONObject(k).getString("category_id");
//                                                    String category_name4 = sub_category_list2.getJSONObject(k).getString("category_name");
//                                                    System.out.println("sub_category_list1 k --- > " + k + "category_name4 " + category_name4);
//                                                    Product.ItemList subData = new Product.ItemList(category_id4, category_name4);
//                                                    itemLists.add(subData);
//                                                }
//                                            } else {
////                                                Product.ItemList subData = new Product.ItemList("", "");
////                                                itemLists.add(subData);
//                                                itemLists.clear();
//                                            }
//                                            Product.SubCategoryOne subData = new Product.SubCategoryOne(category_id2, category_name3, itemLists);
//                                            subCategoryOnes.add(subData);
//                                        }
//                                    } else {
//
//
//                                    }
//
//                                    Product.SubCategory subData = new Product.SubCategory(category_id1, category_name2, subCategoryOnes);
//                                    subCategories.add(subData);
//                                }
//
//
//                            } else {
//
//                                subCategoryOnes.clear();
//
//                                subCategories.clear();
//                            }
//
//                            Product homeData = new Product(category_id, category_name, subCategories);
//                            homeCategoryDataArrayList.add(homeData);
//                        }
//
//
//                        System.out.println("homeCategoryDataArrayList  1" + homeCategoryDataArrayList.size());
//
//                        mCategoryList();
//
//
//                    }
//                }
//            } else {
//                UserPresenter userPresenter = new UserPresenter(MainActivity.this);
//              //  userPresenter.getcategoriesList("");
//              //  country_loader.setVisibility(View.VISIBLE);
//                mLinearListView.setVisibility(View.GONE);
//            }
//
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }

        UserPresenter userPresenter = new UserPresenter(MainActivity.this);
        userPresenter.addProfilePage(SessionSave.getSession("user_id", this));
    }

    public void startLoginActivity(){
        if(!isGuestMode){
            SessionSave.saveSession("user_id", "", MainActivity.this);
            SessionSave.saveSession("cartCount", "", MainActivity.this);
            Intent i = new Intent(MainActivity.this, SplashActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT | Intent.FLAG_ACTIVITY_NO_ANIMATION | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(i);
            finish();
        }else{
            LoginActivity.isFromGuestMode = true;
            Intent i = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(i);
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {

        final Fragment ff = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        Log.i("Back Pressed", "Back Pressed isTouchFromHome " + isTouchFromHome);
        disableDrawer(false);
        drawer.closeDrawer(Gravity.START);
        searchIcon.setVisible(false);
        mLinearView3.setVisibility(View.GONE);
        if (ff instanceof HomeScreenFragment) {

            if (!isCategoryTouched) {
                ((HomeScreenFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).mainPageView();
            } else {
                FragmentManager fm = getSupportFragmentManager();
                if (fm != null) {
                    fm.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);

                    for (int i = 0; i < fm.getBackStackEntryCount(); ++i) {
                        fm.popBackStack();
                    }
                }
                final Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                super.onBackPressed();
                return;
            }

        } else if (ff instanceof ProductDetailsFragment && isTouchFromHome) {


            Fragment fragment1 = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            String tag1 = fragment1.getTag();
            Log.d("ProductDetailsFragment ", "onBackPressed: " + tag1 + titleTxt);

            if (isCategoryTouched) {
                search_layout.setVisibility(View.VISIBLE);
                searchIcon.setVisible(false);
                menu_Name.setVisible(true);
            } else {
                searchIcon.setVisible(false);
                menu_Name.setVisible(false);
            }
            toolbar.setTitle("");
            toolbar.setLogo(R.mipmap.home_logo);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setHomeAsUpIndicator(0);


            super.onBackPressed();
        } else if (ff instanceof ProductDetailsFragment && !isTouchFromHome) {
            isTouchFromHome = true;

            Fragment fragment1 = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            String tag1 = fragment1.getTag();
            Log.d("isCategoryTouched", "onBackPressed: " + tag1 + titleTxt);
            if (tag1 != null && tag1.equals("wishlist")) {
                toolbar.setTitle("My Wishlist");
                toolbar.setLogo(R.drawable.transpent_logo);
            } else if (tag1 != null && tag1.equals("storelist")) {

                //Ray StoreList <- ProductDetails
                toolbar.setTitle(getString(R.string.merchant_details));
                toolbar.setLogo(R.drawable.transpent_logo);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);
                //toolbar.setLogo(R.mipmap.home_logo);
                //toggle.setDrawerIndicatorEnabled(true);
                disableDrawer(true);
                //toggle.setHomeAsUpIndicator(0);
            } else if (tag1 != null && tag1.equals("productlist")) {
                searchIcon.setVisible(true);
                toolbar.setTitle(productList);
                toolbar.setLogo(R.drawable.transpent_logo);
            } else {
                if (titleTxt.equals("")) {
                    toolbar.setTitle("");
                    toolbar.setLogo(R.mipmap.home_logo);
                    toggle.setDrawerIndicatorEnabled(true);
                    disableDrawer(false);
                    toggle.setHomeAsUpIndicator(0);

                } else {
                    toolbar.setTitle(titleTxt);
                    toolbar.setLogo(R.drawable.transpent_logo);
                }

            }

            Log.d("isCategoryTouched", "onBackPressed: " + isTouchFromHome);
            super.onBackPressed();
        } else if (ff instanceof ProductListFragment) {


//            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            ft.remove(new SearchViewFragment()).commit();
            Fragment fragment1 = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            String tag1 = fragment1.getTag();
            Log.d("isCategoryTouched", "onBackPressed: " + tag1);
            if (tag1 != null && tag1.equals("searchview")) {

                latestDealSearch = false;
                giftDealSearch = false;
                hotDealSearch = false;
                //Ray back from ProductListFragment for search purpose

                getSupportFragmentManager().popBackStack();
                if (isCategoryTouched) {
                    search_layout.setVisibility(View.VISIBLE);
                    searchIcon.setVisible(false);
                    menu_Name.setVisible(true);
                } else {
                    searchIcon.setVisible(true);
                    menu_Name.setVisible(false);
                }
                toolbar.setTitle("");
                toolbar.setLogo(R.mipmap.home_logo);
                toggle.setDrawerIndicatorEnabled(true);
                toggle.setHomeAsUpIndicator(0);
                setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));

                /*cartIconempty.setVisible(false);
                cartItem.setVisible(false);
                editIcon.setVisible(false);
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
                toggle.setDrawerIndicatorEnabled(false);
                disableDrawer(true);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);*/

                // action_search.setVisible(true);
                //  action_search.expandActionView();


            } else if (tag1 != null && tag1.equals("Marchent")) {
                toolbar.setTitle("");
                toolbar.setLogo(R.mipmap.home_logo);
                toggle.setDrawerIndicatorEnabled(true);
                toggle.setHomeAsUpIndicator(0);
                disableDrawer(true);
            } else {
                Log.d("ProductListFragment ", "onBackPressed: " + isCategoryTouched);
                if (isCategoryTouched) {
                    search_layout.setVisibility(View.VISIBLE);
                    searchIcon.setVisible(false);
                    menu_Name.setVisible(true);
                } else {
                    searchIcon.setVisible(true);
                    menu_Name.setVisible(false);
                }
                toolbar.setTitle("");
                toolbar.setLogo(R.mipmap.home_logo);
                toggle.setDrawerIndicatorEnabled(true);
                toggle.setHomeAsUpIndicator(0);
            }
            super.onBackPressed();

        } else if (ff instanceof CartListFragment) {

            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            String tag = fragment.getTag();
            setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));
            ((CartListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).viewGone();
            Log.d("tag ", "onBackPressed:  " + tag);
            if (tag.equals("homepage")) {
                setTag = "homepage";

                if (isCategoryTouched) {
                    search_layout.setVisibility(View.VISIBLE);
                    menu_Name.setVisible(true);
                } else {
                    search_layout.setVisibility(View.GONE);
                    menu_Name.setVisible(false);
                }

                toolbar.setTitle("");
                toolbar.setLogo(R.mipmap.home_logo);
                toggle.setDrawerIndicatorEnabled(true);
                toggle.setHomeAsUpIndicator(0);

            } else if (tag.equals("productdeatils")) {
                setTag = "productdeatils";
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
                toggle.setDrawerIndicatorEnabled(false);
                disableDrawer(true);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);
                toolbar.setTitle("" + productTitle);
                toolbar.setLogo(R.drawable.transpent_logo);
            } else if (tag.equals("productlist")) {
                setTag = "productlist";
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
                toggle.setDrawerIndicatorEnabled(false);
                disableDrawer(true);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);
                toolbar.setTitle("" + productList);
                toolbar.setLogo(R.drawable.transpent_logo);
            } else if (tag.equals("dealsdeatils")) {
                setTag = "dealsdeatils";
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
                toggle.setDrawerIndicatorEnabled(false);
                disableDrawer(true);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);
                toolbar.setTitle("" + productTitle);
                toolbar.setLogo(R.drawable.transpent_logo);
            } else if (tag.equals("dealslist")) {
                setTag = "dealslist";
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
                toggle.setDrawerIndicatorEnabled(false);
                disableDrawer(true);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);
                toolbar.setTitle("" + productList);
                toolbar.setLogo(R.drawable.transpent_logo);
            } else if (tag.equals("subcategory")) {
                setTag = "subcategory";
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
                toolbar.setTitle("");
                toolbar.setLogo(R.mipmap.home_logo);
                toggle.setDrawerIndicatorEnabled(true);
                toggle.setHomeAsUpIndicator(0);
            } else if (tag.equals("profile")) {
                setTag = "profile";
                toolbar.setVisibility(View.GONE);
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
                toolbar.setTitle("Profile");
                toolbar.setLogo(R.drawable.transpent_logo);
                toggle.setDrawerIndicatorEnabled(false);
                disableDrawer(true);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);

            } else if (tag.equals("wishlist")) {
                setTag = "wishlist";
                menu_Name.setVisible(false);
                search_layout.setVisibility(View.GONE);
                toolbar.setTitle("My Wishlist");
                toolbar.setLogo(R.drawable.transpent_logo);
                toggle.setDrawerIndicatorEnabled(false);
                disableDrawer(true);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);
            }


            super.onBackPressed();


        } else if (ff instanceof DealsListFragment) {
            Log.d("isCategoryTouched", "onBackPressed: " + isCategoryTouched);
            if (isCategoryTouched) {
                search_layout.setVisibility(View.VISIBLE);
                menu_Name.setVisible(true);
            }
            toolbar.setTitle("");
            toolbar.setLogo(R.mipmap.home_logo);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setHomeAsUpIndicator(0);
            super.onBackPressed();
        } else if (ff instanceof DealsListDetailsFragment) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            String tag = fragment.getTag();
            Log.d("tag ", "onBackPressed:  " + tag);

            if (tag != null && tag.equals("home")) {
                search_layout.setVisibility(View.VISIBLE);
                menu_Name.setVisible(true);
                toolbar.setTitle("");
                toolbar.setLogo(R.mipmap.home_logo);
                toggle.setDrawerIndicatorEnabled(true);
                disableDrawer(false);
                toggle.setHomeAsUpIndicator(0);
            } else if (tag != null && tag.equals("storedealslist")) {
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
                toolbar.setTitle("");
                toolbar.setLogo(R.mipmap.home_logo);
                toggle.setDrawerIndicatorEnabled(true);
                disableDrawer(false);
                toggle.setHomeAsUpIndicator(0);
            } else {
                Log.d("DealsListDetails ", "" + isCategoryTouched);
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
                toggle.setDrawerIndicatorEnabled(false);
                searchIcon.setVisible(true);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);
                productList = "" + "Deals";
                toolbar.setTitle("" + productList);
                toolbar.setLogo(R.drawable.transpent_logo);
            }
            super.onBackPressed();
        } else if (ff instanceof ProfileFragment) {
            Log.d("DealsListDetails ", "" + isCategoryTouched);

            if (((ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).location_icon.getVisibility() == View.VISIBLE
                    || ((ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).edit_icon.getVisibility() == View.GONE) {
                ((ProfileFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).stateMaintain();
            } else {
                toolbar.setVisibility(View.VISIBLE);
                editIcon.setVisible(false);
                setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));
                if (isCategoryTouched) {
                    search_layout.setVisibility(View.VISIBLE);
                    menu_Name.setVisible(true);
                } else {
                    search_layout.setVisibility(View.GONE);
                    menu_Name.setVisible(false);
                }

                toolbar.setTitle("");
                toolbar.setLogo(R.mipmap.home_logo);
                toggle.setDrawerIndicatorEnabled(true);
                toggle.setHomeAsUpIndicator(0);
                super.onBackPressed();
            }
        } else if (ff instanceof FreeGiftFragment) {

            toolbar.setVisibility(View.VISIBLE);
            editIcon.setVisible(false);
            setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));
            if (isCategoryTouched) {
                search_layout.setVisibility(View.VISIBLE);
                menu_Name.setVisible(true);
            } else {
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
            }

            toolbar.setTitle("");
            toolbar.setLogo(R.mipmap.home_logo);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setHomeAsUpIndicator(0);
            super.onBackPressed();

        } else if (ff instanceof OrderTabFragment) {

            toolbar.setVisibility(View.VISIBLE);
            editIcon.setVisible(false);
            setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));
            if (isCategoryTouched) {
                search_layout.setVisibility(View.VISIBLE);
                menu_Name.setVisible(true);
            } else {
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
            }

            toolbar.setTitle("");
            toolbar.setLogo(R.mipmap.home_logo);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setHomeAsUpIndicator(0);
            super.onBackPressed();

        } else if (ff instanceof RewardsFragment) {

            if (((RewardsFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).isFromProfile){

                searchIcon.setVisible(false);
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
                toggle.setDrawerIndicatorEnabled(false);
                setTag = "profile";
                disableDrawer(true);
                toolbar.setTitle("" + getResources().getString(R.string.profile));
                toolbar.setLogo(R.drawable.transpent_logo);
                toolbar.setVisibility(View.GONE);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);
            }else{
                toolbar.setVisibility(View.VISIBLE);
                editIcon.setVisible(false);
                setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));
                if (isCategoryTouched) {
                    search_layout.setVisibility(View.VISIBLE);
                    menu_Name.setVisible(true);
                } else {
                    search_layout.setVisibility(View.GONE);
                    menu_Name.setVisible(false);
                }

                toolbar.setTitle("");
                toolbar.setLogo(R.mipmap.home_logo);
                toggle.setDrawerIndicatorEnabled(true);
                toggle.setHomeAsUpIndicator(0);
            }
            super.onBackPressed();

        } else if (ff instanceof RedemptionFragment) {
            toolbar.setTitle("" + getResources().getString(R.string.rewards));
            super.onBackPressed();

        } else if (ff instanceof HowToFragment) {

            toolbar.setVisibility(View.VISIBLE);
            editIcon.setVisible(false);
            setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));
            if (isCategoryTouched) {
                search_layout.setVisibility(View.VISIBLE);
                menu_Name.setVisible(true);
            } else {
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
            }

            toolbar.setTitle("");
            toolbar.setLogo(R.mipmap.home_logo);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setHomeAsUpIndicator(0);
            super.onBackPressed();

        }else if (ff instanceof WishListFragment) {
            cartIconempty.setVisible(false);
         //   cartItem.setVisible(false);
            editIcon.setVisible(false);
            search_layout.setVisibility(View.GONE);
            menu_Name.setVisible(false);
            setTag = "profile";
            disableDrawer(true);
            toggle.setDrawerIndicatorEnabled(false);
            toolbar.setVisibility(View.GONE);
            toolbar.setTitle("" + "Profile");
            toolbar.setLogo(R.drawable.transpent_logo);
            final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
            toggle.setHomeAsUpIndicator(upArrow);
            super.onBackPressed();
        } else if (ff instanceof ChangePasswordFargment) {
            cartIconempty.setVisible(false);
          //  cartItem.setVisible(false);
            editIcon.setVisible(false);
            search_layout.setVisibility(View.GONE);
            menu_Name.setVisible(false);
            toggle.setDrawerIndicatorEnabled(false);
            setTag = "profile";
            toolbar.setVisibility(View.GONE);
            disableDrawer(true);
            toolbar.setTitle("" + "Profile");
            toolbar.setLogo(R.drawable.transpent_logo);
            final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
            toggle.setHomeAsUpIndicator(upArrow);
            super.onBackPressed();
        } else if (ff instanceof StoreListFragment) {

            if (((StoreListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).isfromMap) {
                cartIconempty.setVisible(false);
              //  cartItem.setVisible(false);
                editIcon.setVisible(false);
                menu_Name.setVisible(false);
                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);

                toggle.setDrawerIndicatorEnabled(false);
                toolbar.setVisibility(View.VISIBLE);
                disableDrawer(false);
                toolbar.setTitle("Near by Locations");
                toolbar.setLogo(R.drawable.transpent_logo);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);


                super.onBackPressed();
            }else if(((StoreListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).isfromRewards){

                search_layout.setVisibility(View.GONE);
                menu_Name.setVisible(false);
                toggle.setDrawerIndicatorEnabled(false);
                toolbar.setVisibility(View.VISIBLE);
                disableDrawer(true);
                cartIconempty.setVisible(false);
                //   mainActivity.cartItem.setVisible(false);
                editIcon.setVisible(false);
                toolbar.setTitle("" + getResources().getString(R.string.rewards));
                toolbar.setLogo(R.drawable.transpent_logo);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);

                super.onBackPressed();
            } else {
                if (((StoreListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).tabviewLayout.getVisibility() == View.VISIBLE) {

                    ((StoreListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).tabviewLayout.setVisibility(View.GONE);
                    ((StoreListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).nestedScrollView.setVisibility(View.VISIBLE);
                    ((StoreListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).noDataLay.setVisibility(View.GONE);
                    search_layout.setVisibility(View.GONE);
                    toggle.setDrawerIndicatorEnabled(false);
                    menu_Name.setVisible(false);
                    toolbar.setVisibility(View.VISIBLE);
                    disableDrawer(true);
                    toolbar.setTitle(getString(R.string.merchant));
                    toolbar.setLogo(R.drawable.transpent_logo);
                    final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                    toggle.setHomeAsUpIndicator(upArrow);
                 //   cartItem.setVisible(false);
                    cartIconempty.setVisible(false);
                } else {
                    if (isCategoryTouched) {
                        search_layout.setVisibility(View.VISIBLE);
                        menu_Name.setVisible(true);
                    }
                    toolbar.setTitle("");
                    toolbar.setLogo(R.mipmap.home_logo);
                    toggle.setDrawerIndicatorEnabled(true);
                    toggle.setHomeAsUpIndicator(0);
                    setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));
                    super.onBackPressed();
                }
            }

        } else if (ff instanceof OrderTabFragment) {

            toolbar.setTitle("");
            toolbar.setLogo(R.mipmap.home_logo);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setHomeAsUpIndicator(0);
            cartIconempty.setVisible(false);
         //   cartItem.setVisible(true);
//          editIcon.setVisible(false);
            search_layout.setVisibility(View.VISIBLE);
            menu_Name.setVisible(false);
//            toggle.setDrawerIndicatorEnabled(true);
//            setTag = "profile";
//            toolbar.setVisibility(View.GONE);
//            disableDrawer(true);
//            toolbar.setTitle("" + "Profile");
            toolbar.setLogo(R.drawable.transpent_logo);
//            final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
//            toggle.setHomeAsUpIndicator(upArrow);
            super.onBackPressed();
        } else if (ff instanceof StoreReviewFragment) {
            toolbar.setTitle("");
            toolbar.setLogo(R.mipmap.home_logo);
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setHomeAsUpIndicator(0);
            setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));
            super.onBackPressed();
        } else if (ff instanceof SearchViewFragment) {

            if(isSearchForMerchants){

            }else{
                if (isCategoryTouched) {
                    search_layout.setVisibility(View.VISIBLE);
                    searchIcon.setVisible(false);
                    menu_Name.setVisible(true);
                } else {
                    searchIcon.setVisible(true);
                    menu_Name.setVisible(false);
                }
                toolbar.setTitle("");
                toolbar.setLogo(R.mipmap.home_logo);
                toggle.setDrawerIndicatorEnabled(true);
                toggle.setHomeAsUpIndicator(0);
                setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));
            }
            super.onBackPressed();
        } else if (ff instanceof PaymentContinueFragment) {
            System.out.println("onBackPressed  in " + "onBackPressed" + ff);
            if (((PaymentContinueFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).urlType.equals("cart")) {
                mLinearView3.setVisibility(View.VISIBLE);
                toolbar.setTitle("");
                toolbar.setLogo(R.drawable.transpent_logo);
            } else {
                mLinearView3.setVisibility(View.GONE);
                toolbar.setTitle("Buy Now");
                toolbar.setLogo(R.drawable.transpent_logo);
            }
            super.onBackPressed();
        } else if (ff instanceof ProductBuyNowFragment) {
            System.out.println("onBackPressed  in " + "onBackPressed" + productTitle);
            toolbar.setTitle("" + productTitle);
            toolbar.setLogo(R.drawable.transpent_logo);
            setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));
            super.onBackPressed();
        } else if (ff instanceof DealBuyNowFragment) {
            System.out.println("onBackPressed  in " + "onBackPressed" + productTitle);
            toolbar.setTitle("" + productTitle);
            toolbar.setLogo(R.drawable.transpent_logo);
            setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));
            super.onBackPressed();
        } else if (ff instanceof OrderDetailsFragment) {
            System.out.println("onBackPressed  in " + "onBackPressed" + productTitle);
            toolbar.setTitle("" + getResources().getString(R.string.myorders));
            toolbar.setLogo(R.drawable.transpent_logo);
            super.onBackPressed();

            final Fragment ff1 = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (ff1 instanceof OrderTabFragment) {
                if (ff1.isAdded() && ff1.isVisible() && ff1.getUserVisibleHint()) {
                    Log.e("TAB_INDEX", oderFragmentTabIndex + " inside main activity");
                    ((OrderTabFragment) ff1).reloadTabs(oderFragmentTabIndex);
                }
            }

        } else if (ff instanceof SearchLoactionFragment) {

            if (((SearchLoactionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).tabsLayout != null
                    && ((SearchLoactionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).tabsLayout .getVisibility() == View.VISIBLE) {

                ((SearchLoactionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).tabsLayout.setVisibility(View.GONE);
                ((SearchLoactionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).isNearByVisible = true;
                ((SearchLoactionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).listContainer.setVisibility(View.VISIBLE);
                ((SearchLoactionFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).showMenuOption();
                search_layout.setVisibility(View.GONE);
                toggle.setDrawerIndicatorEnabled(false);
                menu_Name.setVisible(false);
                toolbar.setVisibility(View.VISIBLE);
                disableDrawer(true);
                toolbar.setTitle(getString(R.string.near_by_stores));
                toolbar.setLogo(R.drawable.transpent_logo);
                final Drawable upArrow = getResources().getDrawable(R.drawable.back_icon);
                toggle.setHomeAsUpIndicator(upArrow);
              //  cartItem.setVisible(false);
                cartIconempty.setVisible(false);

            } else {
                if (isCategoryTouched) {
                    search_layout.setVisibility(View.VISIBLE);
                    searchIcon.setVisible(false);
                    menu_Name.setVisible(true);
                } else {
                    searchIcon.setVisible(true);
                    menu_Name.setVisible(false);
                }
                toolbar.setTitle("");
                toolbar.setLogo(R.mipmap.home_logo);
                toggle.setDrawerIndicatorEnabled(true);
                toggle.setHomeAsUpIndicator(0);
                setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));

                super.onBackPressed();
            }
        }/*else if (ff instanceof StoreProductListFragment) {
            System.out.println("onBackPressed  in " + "onBackPressed" + productTitle);
            search_layout.setVisibility(View.GONE);
            toolbar.setTitle("Le");
            toggle.setDrawerIndicatorEnabled(true);
            toggle.setHomeAsUpIndicator(0);
            setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));

            super.onBackPressed();
        }*/ else {

            setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));
            super.onBackPressed();
        }
        System.out.println("onBackPressed  " + "onBackPressed" + ff);


    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume  " + "onResume");

        String fullName = SessionSave.getSession("user_name", MainActivity.this);
        String firstName = fullName.split(" ")[0];

        if(menu_Name != null){
            if (firstName.length() < 10) {
                menu_Name.setTitle("Hi " + firstName + "!");
                username.setText("Hi " + firstName + "!");
            } else {
                menu_Name.setTitle("Hi " + firstName.substring(0, 8) + ".. !");
                username.setText("Hi " + firstName.substring(0, 8) + ".. !");
            }
        }

        if(SessionSave.getSession("user_id", MainActivity.this).isEmpty()){
            isGuestMode = true;
        }else{
            isGuestMode = false;
        }

        if(isGuestMode){
            myVoucher.setVisibility(View.GONE);
            myRewards.setVisibility(View.GONE);
            footer_account.setVisibility(View.GONE);
            footer_logout.setText(getString(R.string.login));
        }else{
            myVoucher.setVisibility(View.VISIBLE);
            myRewards.setVisibility(View.VISIBLE);
            footer_account.setVisibility(View.VISIBLE);
            footer_logout.setText(getString(R.string.logout));
        }
    }

    public void initiateSearch(){
        Log.wtf(TAG, " current tag  "+setTag);

        cartIconempty.setVisible(false);
        //  cartItem.setVisible(false);
        editIcon.setVisible(false);
        menu_Name.setVisible(false);
        SearchViewFragment fragment2 = new SearchViewFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.fragment_container, fragment2);
        fragmentTransaction.addToBackStack("");
        fragmentTransaction.commit();
    }

    public void startMerchantFragment(boolean isforSearch, boolean isFromRewards){
        cartIconempty.setVisible(false);
        //  cartItem.setVisible(false);
        editIcon.setVisible(false);
        menu_Name.setVisible(false);
        drawer.closeDrawer(Gravity.START);
        final Fragment ff = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(ff instanceof StoreListFragment)) {
            isSearchForMerchants = true;
            Bundle bundle = new Bundle();
            bundle.putBoolean("isfromMap", false);
            bundle.putBoolean("isforSearch", isforSearch);
            bundle.putBoolean("isFromRewards", isFromRewards);
            bundle.putString("store_id", "1");
            StoreListFragment fragment2 = new StoreListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, fragment2);
            fragment2.setArguments(bundle);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
        }
    }

    public void startRedemptionFragment(){
        cartIconempty.setVisible(false);
        //  cartItem.setVisible(false);
        editIcon.setVisible(false);
        menu_Name.setVisible(false);
        drawer.closeDrawer(Gravity.START);
        final Fragment ff = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (!(ff instanceof RedemptionFragment)) {

            RedemptionFragment fragment2 = new RedemptionFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, fragment2);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
        }
    }


    public void getProfileData(JsonObject mJsonObject) {

        try {
            Log.d("Result 1111", "onNext: " + mJsonObject);

            if (mJsonObject != null) {
                JSONObject json = new JSONObject(mJsonObject.toString());
                SessionSave.saveSession("shipping_details", "" + mJsonObject.toString(), this);

                if (json.getString("status").equals("200")) {
                    JSONArray jsonArray = json.getJSONArray("user_details");
                    SessionSave.saveSession("user_name", jsonArray.getJSONObject(0).optString("user_name").trim(), this);
                    SessionSave.saveSession("user_image", jsonArray.getJSONObject(0).optString("user_image").trim(), this);

                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void mCategoryList() {
        isFirstViewClick = false;
        isSecondViewClick = false;
        isThirdViewClick = false;
        country_loader.setVisibility(View.GONE);
        mLinearListView.setVisibility(View.GONE);
        System.out.println("homeCategoryDataArrayList  2" + homeCategoryDataArrayList.size() + isFirstViewClick);
        /***
         * adding item into listview
         */
        for (int i = 0; i < homeCategoryDataArrayList.size(); i++) {

            LayoutInflater inflater = null;
            inflater = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            final View mLinearView = inflater.inflate(R.layout.row_first, null);

            final TextView mProductName = mLinearView.findViewById(R.id.textViewName);
            final RelativeLayout mLinearFirstArrow = mLinearView.findViewById(R.id.linearFirst);
            final ImageView mImageArrowFirst = mLinearView.findViewById(R.id.imageFirstArrow);
            final LinearLayout mLinearScrollSecond = mLinearView.findViewById(R.id.linear_scroll);
            System.out.println("getmSubCategoryList  " + homeCategoryDataArrayList.get(i).getmSubCategoryList().isEmpty());
            ApplyFont.applyFont(MainActivity.this, mLinearView.findViewById(R.id.linear_scroll));
            if (!(homeCategoryDataArrayList.get(i).getmSubCategoryList().isEmpty())) {
                if (isFirstViewClick == false) {
                    System.out.println("isFirstViewClick  " + isFirstViewClick);
                    mLinearScrollSecond.setVisibility(View.GONE);
                    mImageArrowFirst.setBackgroundResource(R.drawable.add_icon);
                } else {
                    mLinearScrollSecond.setVisibility(View.GONE);
                    mImageArrowFirst.setBackgroundResource(R.drawable.remove_icon);
                }
                mImageArrowFirst.setVisibility(View.VISIBLE);
            } else {
                mImageArrowFirst.setVisibility(View.GONE);
            }
            Log.d("mLinearScrollSecond ", "onClick: " + mLinearScrollSecond.getChildCount());
            mLinearFirstArrow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    main_category_id = "" + view.getTag();
                    if (mLinearScrollSecond.getChildCount() > 0) {

                        if (isFirstViewClick == false) {
                            isFirstViewClick = true;
                            expandAnimation(mLinearScrollSecond, mImageArrowFirst);

                        } else {
                            isFirstViewClick = false;
                            closeAnimation(mLinearScrollSecond, mImageArrowFirst);
                        }
                    } else {


                        drawer.closeDrawer(Gravity.START);
                        Bundle bundle = new Bundle();
                        bundle.putString("main_category_id", main_category_id);
                        bundle.putString("sec_category_id", "");
                        bundle.putString("sub_category_id", "");
                        bundle.putString("sub_sec_category_id", "");
                        bundle.putString("title", "");
                        bundle.putString("page_title", mProductName.getText().toString());
                        bundle.putString("discount", "");
                        bundle.putString("price_min", "");
                        bundle.putString("price_max", "");
                        bundle.putString("availability", "1"); //
                        bundle.putString("sort_order_by", "");
                        ProductListFragment fragment2 = new ProductListFragment();
                        FragmentManager fragmentManager = getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.add(R.id.fragment_container, fragment2, setTag);
                        fragment2.setArguments(bundle);
                        fragmentTransaction.addToBackStack("");
                        fragmentTransaction.commit();
                    }
                }
            });


            final String name = homeCategoryDataArrayList.get(i).getpName();
            mProductName.setText(name);
            mLinearFirstArrow.setTag(homeCategoryDataArrayList.get(i).getPid());


            for (int j = 0; j < homeCategoryDataArrayList.get(i).getmSubCategoryList().size(); j++) {

                LayoutInflater inflater2 = null;
                inflater2 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                View mLinearView2 = inflater2.inflate(R.layout.row_second, null);

                final TextView mSubItemName = mLinearView2.findViewById(R.id.textViewTitle);
                final RelativeLayout mLinearSecondArrow = mLinearView2.findViewById(R.id.linearSecond);
                final ImageView mImageArrowSecond = mLinearView2.findViewById(R.id.imageSecondArrow);
                final LinearLayout mLinearScrollThird = mLinearView2.findViewById(R.id.linear_scroll_third);
                final String catName = homeCategoryDataArrayList.get(i).getmSubCategoryList().get(j).getpSubCatName();

                if (catName.equals("")) {
                    mSubItemName.setVisibility(View.GONE);
                } else {

                    if (homeCategoryDataArrayList.get(i).getmSubCategoryList().get(j).getmSubOneListArray().size() > 0) {
                        mImageArrowSecond.setVisibility(View.VISIBLE);
                        if (isSecondViewClick == false) {
                            mLinearScrollThird.setVisibility(View.GONE);
                            mImageArrowSecond.setBackgroundResource(R.drawable.add_icon);
                        } else {
                            mLinearScrollThird.setVisibility(View.VISIBLE);
                            mImageArrowSecond.setBackgroundResource(R.drawable.remove_icon);
                        }
                    } else {
                        mImageArrowSecond.setVisibility(View.GONE);
                    }

                    mLinearSecondArrow.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {

                            sec_category_id = "" + view.getTag();
                            if (mLinearScrollThird.getChildCount() > 0) {
                                if (isSecondViewClick == false) {
                                    isSecondViewClick = true;
                                    if (catName.equals("")) {
                                        mLinearScrollThird.setVisibility(View.GONE);
                                    } else {
                                        expandAnimation(mLinearScrollThird, mImageArrowSecond);

                                    }

                                } else {
                                    isSecondViewClick = false;
                                    closeAnimation(mLinearScrollThird, mImageArrowSecond);
                                }
                            } else {
                                mLinearFirstArrow.performClick();
                                drawer.closeDrawer(Gravity.START);
                                Bundle bundle = new Bundle();
                                bundle.putString("main_category_id", "");
                                bundle.putString("sec_category_id", sec_category_id);
                                bundle.putString("sub_category_id", "");
                                bundle.putString("sub_sec_category_id", "");
                                bundle.putString("title", "");
                                bundle.putString("page_title", mSubItemName.getText().toString());
                                bundle.putString("discount", "");
                                bundle.putString("price_min", "");
                                bundle.putString("price_max", "");
                                bundle.putString("availability", "1");
                                bundle.putString("sort_order_by", "");
                                ProductListFragment fragment2 = new ProductListFragment();
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.add(R.id.fragment_container, fragment2, setTag);
                                fragment2.setArguments(bundle);
                                fragmentTransaction.addToBackStack("");
                                fragmentTransaction.commit();
                            }
                        }
                    });


                    mSubItemName.setText(catName);
                    mLinearSecondArrow.setTag(homeCategoryDataArrayList.get(i).getmSubCategoryList().get(j).getpSubCatId());
                }


                /**
                 *
                 */
                for (int m = 0; m < homeCategoryDataArrayList.get(i).getmSubCategoryList().get(j).getmSubOneListArray().size(); m++) {

                    LayoutInflater inflater3 = null;
                    inflater3 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    View mLinearView3 = inflater3.inflate(R.layout.row_second_1, null);

                    final TextView mSubItemName1 = mLinearView3.findViewById(R.id.textViewTitle);
                    final RelativeLayout mLinearSecondArrow1 = mLinearView3.findViewById(R.id.linearSecond);
                    final ImageView mImageArrowSecond1 = mLinearView3.findViewById(R.id.imageSecondArrow);
                    final LinearLayout mLinearScrollThird1 = mLinearView3.findViewById(R.id.linear_scroll_third);
                    final String catName1 = homeCategoryDataArrayList.get(i).getmSubCategoryList().get(j).getmSubOneListArray().get(m).getpSubCatName();

                    if (catName1.equals("")) {
                        mSubItemName1.setVisibility(View.GONE);
                    } else {

                        if (homeCategoryDataArrayList.get(i).getmSubCategoryList().get(j).getmSubOneListArray().get(m).getmItemListArray().size() > 0) {
                            if (isThirdViewClick == false) {
                                mLinearScrollThird1.setVisibility(View.GONE);
                                mImageArrowSecond1.setBackgroundResource(R.drawable.add_icon);
                            } else {
                                mLinearScrollThird1.setVisibility(View.VISIBLE);
                                mImageArrowSecond1.setBackgroundResource(R.drawable.remove_icon);
                            }
                            mImageArrowSecond1.setVisibility(View.VISIBLE);
                        } else {
                            mImageArrowSecond1.setVisibility(View.GONE);
                        }

                        mLinearSecondArrow1.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
//                                mLinearScrollThird1.setVisibility(View.GONE);
                                sub_category_id = "" + view.getTag();
                                if (mLinearScrollThird1.getChildCount() > 0) {
                                    if (isThirdViewClick == false) {
                                        isThirdViewClick = true;
                                        expandAnimation(mLinearScrollThird1, mImageArrowSecond1);

                                    } else {
                                        isThirdViewClick = false;
                                        closeAnimation(mLinearScrollThird1, mImageArrowSecond1);
                                    }
                                } else {
                                    mLinearFirstArrow.performClick();
                                    mLinearSecondArrow.performClick();
                                    drawer.closeDrawer(Gravity.START);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("main_category_id", "");
                                    bundle.putString("sec_category_id", "");
                                    bundle.putString("sub_category_id", sub_category_id);
                                    bundle.putString("sub_sec_category_id", "");
                                    bundle.putString("title", "");
                                    bundle.putString("page_title", mSubItemName1.getText().toString());
                                    bundle.putString("discount", "");
                                    bundle.putString("price_min", "");
                                    bundle.putString("price_max", "");
                                    bundle.putString("availability", "1");
                                    bundle.putString("sort_order_by", "");
                                    ProductListFragment fragment2 = new ProductListFragment();
                                    FragmentManager fragmentManager = getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.add(R.id.fragment_container, fragment2, setTag);
                                    fragment2.setArguments(bundle);
                                    fragmentTransaction.addToBackStack("");
                                    fragmentTransaction.commit();
                                }
                            }
                        });


                        mSubItemName1.setText(catName1);
                        mLinearSecondArrow1.setTag(homeCategoryDataArrayList.get(i).getmSubCategoryList().get(j).getmSubOneListArray().get(m).getpSubCatID());
                    }


                    System.out.println("homeCategoryDataArrayList m  " + m + " Size --- > " + homeCategoryDataArrayList.get(i).getmSubCategoryList().get(j).getmSubOneListArray().get(m).getmItemListArray().size());
                    /**
                     *
                     */
                    for (int k = 0; k < homeCategoryDataArrayList.get(i).getmSubCategoryList().get(j).getmSubOneListArray().get(m).getmItemListArray().size(); k++) {

                        LayoutInflater inflater4 = null;
                        inflater4 = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                        View mLinearView4 = inflater4.inflate(R.layout.row_third, null);

                        final TextView mItemName = mLinearView4.findViewById(R.id.textViewItemName);
                        final String itemName = homeCategoryDataArrayList.get(i).getmSubCategoryList().get(j).getmSubOneListArray().get(m).getmItemListArray().get(k).getItemName();
                        if (itemName.equals("")) {
                            mItemName.setVisibility(View.GONE);
                        } else {
                            mItemName.setText(itemName);
                        }

                        mItemName.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                String itemName;
                                sub_sec_category_id = "" + view.getTag();
                                if (mItemName.getText().toString().equals("T-Shirt1")) {
                                    itemName = "T-Shirt";
                                } else {
                                    itemName = mItemName.getText().toString();
                                }
                                drawer.closeDrawer(Gravity.START);
                                Bundle bundle = new Bundle();
                                bundle.putString("main_category_id", "");
                                bundle.putString("sec_category_id", "");
                                bundle.putString("sub_category_id", "");
                                bundle.putString("sub_sec_category_id", sub_sec_category_id);
                                bundle.putString("title", "");
                                bundle.putString("page_title", itemName);
                                bundle.putString("discount", "");
                                bundle.putString("price_min", "");
                                bundle.putString("price_max", "");
                                bundle.putString("availability", "1");
                                bundle.putString("sort_order_by", "");
                                ProductListFragment fragment2 = new ProductListFragment();
                                FragmentManager fragmentManager = getSupportFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.add(R.id.fragment_container, fragment2, setTag);
                                fragment2.setArguments(bundle);
                                fragmentTransaction.addToBackStack("");
                                fragmentTransaction.commit();
                                mLinearFirstArrow.performClick();
                                mLinearSecondArrow1.performClick();
                                mLinearSecondArrow.performClick();

                            }
                        });
                        mItemName.setTag(homeCategoryDataArrayList.get(i).getmSubCategoryList().get(j).getmSubOneListArray().get(m).getmItemListArray().get(k).getItemId());
                        mLinearScrollThird1.addView(mLinearView4);
                    }

                    mLinearScrollThird.addView(mLinearView3);

                }

                mLinearScrollSecond.addView(mLinearView2);
            }

            System.out.println("homeCategoryDataArrayList  6 ---- >    " + homeCategoryDataArrayList.size());
            mLinearListView.addView(mLinearView);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
     //   cartItem = menu.findItem(R.id.cartIcon);
        cartIconempty = menu.findItem(R.id.cartIconempty);
        action_search = menu.findItem(R.id.action_search);
        editIcon = menu.findItem(R.id.editIcon);
        menu_Name = menu.findItem(R.id.userName);
        searchIcon = menu.findItem(R.id.searchIcon);
        editIcon.setVisible(false);
        action_search.setVisible(true);
        searchIcon.setVisible(false);

        String fullName = SessionSave.getSession("user_name", MainActivity.this);
        String firstName = fullName.split(" ")[0];

        if(menu_Name != null){
            if (firstName.length() < 10) {
                menu_Name.setTitle("Hi " + firstName + "!");
                username.setText("Hi " + firstName + "!");
            } else {
                menu_Name.setTitle("Hi " + firstName.substring(0, 8) + ".. !");
                username.setText("Hi " + firstName.substring(0, 8) + ".. !");
            }
        }
        setBadgeCount(this, SessionSave.getSession("cartCount", this));

        getCartCount();
        return true;
    }

    private void getCartCount() {

        UserPresenter userPresenter = new UserPresenter(this);
        userPresenter.cartListPage(SessionSave.getSession("user_id", this));

    }

    public void cartListData(JsonObject mJsonObject) {

        try {

            Fragment ff = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (mJsonObject != null) {
                JSONObject json = new JSONObject(mJsonObject.toString());
                if (json.getString("status").equals("200")) {

                    String cart_count = "" + json.getJSONArray("cart_details").length();
                    Log.e("API_RESPONSE", "cart: " + cart_count);
                    SessionSave.saveSession("cartCount", cart_count, this);
                    if (!(ff instanceof SearchViewFragment)) {
                        setBadgeCount(this, cart_count);
                    }
                } else {
                    SessionSave.saveSession("cartCount", "", this);
                    if (!(ff instanceof SearchViewFragment)) {
                        setBadgeCount(this, "");
                    }
                }
            } else {
                showSnackBar(findViewById(R.id.fragment_container), getString(R.string.unable_to_reach_server));
                if (!(ff instanceof SearchViewFragment)) {
                    setBadgeCount(this, SessionSave.getSession("cartCount", this));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    public void setBadgeCount(Context context, String count) {
        /*if (count.equals("")) {
            cartIconempty.setVisible(true);
            cartItem.setVisible(false);
            editIcon.setVisible(false);
            action_search.setVisible(false);
        } else {
            cartIconempty.setVisible(false);
            cartItem.setVisible(true);
            editIcon.setVisible(false);
            action_search.setVisible(false);
        }

        icon = (LayerDrawable) cartItem.getIcon();
        BadgeDrawable badge;
        Log.d(" setBadgeCount --> 1 ", ": " + SessionSave.getSession("cartCount", MainActivity.this));

        // Reuse drawable if possible
        Drawable reuse = icon.findDrawableByLayerId(R.id.ic_badge);
        if (reuse != null && reuse instanceof BadgeDrawable) {
            badge = (BadgeDrawable) reuse;
        } else {
            badge = new BadgeDrawable(context);
        }
        coutTxt.setText(count);
        badge.setCount(count);
        icon.mutate();
        icon.setDrawableByLayerId(R.id.ic_badge, badge);*/
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
       /* if (id == R.id.cartIcon || id == R.id.cartIconempty) {
            Fragment ff = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (ff instanceof HomeScreenFragment) {
                setTag = "homepage";

            } else if (ff instanceof ProductDetailsFragment) {
                setTag = "productdeatils";

            } else if (ff instanceof ProductListFragment) {
                setTag = "productlist";
            } else if (ff instanceof MainSubCategoryFragment) {
                setTag = "subcategory";
            } else if (ff instanceof DealsListFragment) {
                setTag = "dealslist";
            } else if (ff instanceof DealsListDetailsFragment) {
                setTag = "dealsdeatils";
            } else if (ff instanceof ProfileFragment) {
                setTag = "profile";
            } else if (ff instanceof WishListFragment) {
                setTag = "wishlist";
            }
            cartIconempty.setVisible(false);
            menu_Name.setVisible(false);
         //   cartItem.setVisible(false);
            CartListFragment fragment2 = new CartListFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.fragment_container, fragment2, setTag);
            fragmentTransaction.addToBackStack("");
            fragmentTransaction.commit();
            return true;
        } else*/ if (id == R.id.searchIcon) {
            final Fragment ff = getSupportFragmentManager().findFragmentById(R.id.fragment_container);

            if (ff instanceof DealsListFragment) {
                ((DealsListFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_container)).searchViewVisible();
            } else if(ff instanceof ProductListFragment) {

                //Ray back from ProductListFragment for search purpose

                getSupportFragmentManager().popBackStack();
                getSupportFragmentManager().popBackStack();

                cartIconempty.setVisible(false);
             //   cartItem.setVisible(false);
                editIcon.setVisible(false);
                searchIcon.setVisible(false);
                menu_Name.setVisible(false);

                SearchViewFragment fragment2 = new SearchViewFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }else{
                cartIconempty.setVisible(false);
              //  cartItem.setVisible(false);
                editIcon.setVisible(false);
                searchIcon.setVisible(false);
                menu_Name.setVisible(false);

                SearchViewFragment fragment2 = new SearchViewFragment();
                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.fragment_container, fragment2);
                fragmentTransaction.addToBackStack("");
                fragmentTransaction.commit();
            }
        }


        return super.onOptionsItemSelected(item);
    }


    public void disableDrawer(Boolean bol) {
        if (bol) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        } else {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }
    }

    public void removeFragment() {
        onBackPressed();
    }


    public void expandAnimation(final LinearLayout v, final ImageView imageView) {
        v.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        imageView.setBackgroundResource(R.drawable.remove_icon);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {
                v.getLayoutParams().height = interpolatedTime == 1
                        ? LinearLayout.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(1000);
        v.startAnimation(a);
    }

    public void closeAnimation(final LinearLayout v, final ImageView imageView) {
        final int initialHeight = v.getMeasuredHeight();

        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, Transformation t) {

                System.out.println("interpolatedTime " + interpolatedTime);
                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                    imageView.setBackgroundResource(R.drawable.add_icon);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // 1dp/ms
        a.setDuration(1000);
        v.startAnimation(a);
    }

    public void showSnackBar(View view, String message) {

        Snackbar.make(view, "" + message, Snackbar.LENGTH_LONG).show();

    }

    /**
     * This is method for show the toast
     */
    public void ShowToast(Context contex, String message) {

        Toast toast = Toast.makeText(contex, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.e("IMAGE_TEST", "called in main " + requestCode);

        Fragment ff = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        Log.d("data", "onActivityResult: " + ff);
        Log.d("data", "resultCode: " + resultCode + " requestCode " + requestCode);
        if (ff instanceof ProfileFragment) {
            super.onActivityResult(requestCode, resultCode, data);
//            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
//            fragment.onActivityResult(requestCode, resultCode, data);
        } else if (ff instanceof PaymentContinueFragment) {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            fragment.onActivityResult(requestCode, resultCode, data);
        } else {
            Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    public void reviewListUpdate(JSONArray array) {
        // Get Fragment B
        setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));

        ProductDetailsFragment frag = (ProductDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        frag.reviewListUpdate(array);
    }

    @Override
    public void storeReviewUpdate(JSONArray array) {
        setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));

        StoreListFragment frag = (StoreListFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        frag.reviewListUpdate(array);

    }

    @Override
    public void dealsReviewUpdate(JSONArray array) {
        setBadgeCount(MainActivity.this, SessionSave.getSession("cartCount", MainActivity.this));

        DealsListDetailsFragment frag = (DealsListDetailsFragment)
                getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        frag.reviewListUpdate(array);
    }

    public void setDetaultLocale() {
        if (SessionSave.getSession("lang", MainActivity.this).equals("")) {
            SessionSave.saveSession("lang", "en", MainActivity.this);
            SessionSave.saveSession("Lang_Country", "en_GB", MainActivity.this);
            System.out.println("lang" + SessionSave.getSession("lang", MainActivity.this));
            System.out.println("Lang_Country" + SessionSave.getSession("Lang_Country", MainActivity.this));
            Configuration config = new Configuration();
            String langcountry = SessionSave.getSession("Lang_Country", MainActivity.this);
            String arry[] = langcountry.split("_");
            config.locale = new Locale(arry[0], arry[1]);
            Locale.setDefault(new Locale(arry[0], arry[1]));
            MainActivity.this.getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

        }

    }


    public void setDetaultLocale(LanguageData languageData) {

        SessionSave.saveSession("lang", languageData.getLanguageCode(), MainActivity.this);
        SessionSave.saveSession("Lang_Country", languageData.getLanguageCountryCode(), MainActivity.this);


        System.out.println("lang" + SessionSave.getSession("lang", MainActivity.this));
        System.out.println("Lang_Country" + SessionSave.getSession("Lang_Country", MainActivity.this));
        Configuration config = new Configuration();
        String langcountry = SessionSave.getSession("Lang_Country", MainActivity.this);
        String arry[] = langcountry.split("_");
        config.locale = new Locale(arry[0], arry[1]);
        Locale.setDefault(new Locale(arry[0], arry[1]));
        MainActivity.this.getBaseContext().getResources().updateConfiguration(config, getBaseContext().getResources().getDisplayMetrics());

    }

    /**
     * Custom alert dialog used in entire project.can call from anywhere with the following param Context,title,message,success and failure button text.
     */
    public void language_alert_view(final Context mContext) {
        final ArrayList<LanguageData> languageDataArrayList = new ArrayList<>();
        if (alertDialog != null)
            if (alertDialog.isShowing())
                alertDialog.dismiss();
        final View view = View.inflate(mContext, R.layout.language_lay, null);

        alertDialog = new Dialog(mContext, R.style.dialogwinddow);
        alertDialog.setContentView(view);
        alertDialog.setCancelable(true);
        alertDialog.show();

        RecyclerView langList = alertDialog.findViewById(R.id.langList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);
        langList.setHasFixedSize(true);
        langList.setLayoutManager(linearLayoutManager);
        try {

            JSONObject json = new JSONObject(SessionSave.getSession("language_details", MainActivity.this));
            JSONArray Languages = json.getJSONArray("Languages");
            for (int i = 0; i < getResources().getStringArray(R.array.languagelist).length; i++) {

                String lang_name = (getResources().getStringArray(R.array.languagelist))[i];
                String lang_code = (getResources().getStringArray(R.array.languagecode))[i];
                String lang_country_code = (getResources().getStringArray(R.array.languagecountrycode))[i];

                languageData = new LanguageData(lang_name, lang_code, lang_country_code);
                languageDataArrayList.add(languageData);
            }

            LanguageAdapter detailsSizeAdapter = new LanguageAdapter(MainActivity.this, languageDataArrayList);
            langList.setAdapter(detailsSizeAdapter);


            detailsSizeAdapter.setOnItemClickListener(new LanguageAdapter.MyClickListener() {
                @Override
                public void onItemClick(int position, View v) {

                    alertDialog.dismiss();

                    System.out.println(" Lang_Ccode -- >  " + languageDataArrayList.get(position).getLanguageCode());


                    // setDetaultLocale(languageDataArrayList.get(position));
                    /*
                    restart the activity
                     */
                    Intent intent = getIntent();
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

                }
            });

        } catch (Exception ex) {
            ex.printStackTrace();
        }


    }


    public void getcategoryList(JsonObject mJsonObject) {

        country_loader.setVisibility(View.VISIBLE);
        mLinearListView.setVisibility(View.GONE);
        SessionSave.saveSession("category_list", "" + mJsonObject, MainActivity.this);
        try {

            if (!SessionSave.getSession("category_list", MainActivity.this).equals("")) {
                JSONObject json = new JSONObject(SessionSave.getSession("category_list", MainActivity.this));
                System.out.println("category_list--->" + json);

                if (json.getString("status").equals("200")) {


                    JSONArray category_details = json.getJSONArray("categories_list");

                    homeCategoryDataArrayList.clear();
                    if (category_details.length() > 0) {

                        for (int i = 0; i < category_details.length(); i++) {

                            String category_id = category_details.getJSONObject(i).getString("category_id");
                            String category_name = category_details.getJSONObject(i).getString("category_name");

                            JSONArray sub_category_list = category_details.getJSONObject(i).getJSONArray("sub_category_list");

                            subCategories = new ArrayList<>();

                            if (sub_category_list.length() > 0) {
                                for (int j = 0; j < sub_category_list.length(); j++) {

                                    String category_id1 = sub_category_list.getJSONObject(j).getString("category_id");
                                    String category_name2 = sub_category_list.getJSONObject(j).getString("category_name");
                                    JSONArray sub_category_list1 = sub_category_list.getJSONObject(j).getJSONArray("sub_category_list");

                                    subCategoryOnes = new ArrayList<>();
                                    if (sub_category_list1.length() > 0) {

                                        for (int m = 0; m < sub_category_list1.length(); m++) {

                                            String category_id2 = sub_category_list1.getJSONObject(m).getString("category_id");
                                            String category_name3 = sub_category_list1.getJSONObject(m).getString("category_name");
                                            JSONArray sub_category_list2 = sub_category_list1.getJSONObject(m).getJSONArray("sub_category_list");

                                            itemLists = new ArrayList<>();
                                            if (sub_category_list2.length() > 0) {

                                                for (int k = 0; k < sub_category_list2.length(); k++) {

                                                    String category_id4 = sub_category_list2.getJSONObject(k).getString("category_id");
                                                    String category_name4 = sub_category_list2.getJSONObject(k).getString("category_name");
                                                    System.out.println("sub_category_list1 k --- > " + k + "category_name4 " + category_name4);
                                                    Product.ItemList subData = new Product.ItemList(category_id4, category_name4);
                                                    itemLists.add(subData);
                                                }
                                            } else {
//                                                Product.ItemList subData = new Product.ItemList("", "");
//                                                itemLists.add(subData);
                                                itemLists.clear();
                                            }
                                            Product.SubCategoryOne subData = new Product.SubCategoryOne(category_id2, category_name3, itemLists);
                                            subCategoryOnes.add(subData);
                                        }
                                    } else {
//                                        Product.SubCategoryOne subData = new Product.SubCategoryOne("", "", itemLists);
//                                        subCategoryOnes.add(subData);

                                    }

                                    Product.SubCategory subData = new Product.SubCategory(category_id1, category_name2, subCategoryOnes);
                                    subCategories.add(subData);
                                }


                            } else {

                                subCategoryOnes.clear();

                                subCategories.clear();
                            }

                            Product homeData = new Product(category_id, category_name, subCategories);
                            homeCategoryDataArrayList.add(homeData);
                        }


//                        DrawerAdapterScreen CatergoryAdap = new DrawerAdapterScreen(MainActivity.this, homeCategoryDataArrayList);
//
//                        drawerRecyclerView.setAdapter(CatergoryAdap);

                        System.out.println("homeCategoryDataArrayList  1" + homeCategoryDataArrayList.size());

                        mCategoryList();


                    }
                } else {
                    country_loader.setVisibility(View.GONE);
                }
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

}
