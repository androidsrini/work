package com.codesense.driverapp.ui.drawer;

import android.annotation.SuppressLint;
import android.app.ActionBar;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.codesense.driverapp.R;
import com.codesense.driverapp.di.utils.Utility;
import com.codesense.driverapp.localstoreage.AppSharedPreference;
import com.codesense.driverapp.ui.documentstatus.DocumentStatusActivity;
import com.codesense.driverapp.ui.invitefriends.InviteFriendsActivity;
import com.codesense.driverapp.ui.referalprogram.ReferalProgramActivity;
import com.codesense.driverapp.ui.signin.LoginActivity;

import java.util.ArrayList;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

@SuppressLint("Registered")
public class DrawerActivity extends DaggerAppCompatActivity {


    DrawerLayout drawerLayout;
    protected FrameLayout frameLayout;
    ListView drawerList;
    Button drawerIcon;
    public TextView titleTextView;

    private Toolbar toolBar;
    View v;
    ActionBar.LayoutParams params;
    ArrayList<NavDrawerItem> navDrawerItems;
    int drawerItemcount;
    int details, details1;
    TypedArray typedArray = null, typedArray1 = null;
    String MenuItem, MenuItem1;
    String strcheckstatusTitle, strReferAndEarn, strManagevehicle, strManagedocument, strYourtrip, strEarningreports, strLivetracking, strSetting;
    NavDrawerListAdapter adapter;
    private ActionBarDrawerToggle mDrawerToggle;
    String[] getItemValues;
    public int screenWidth, screenHeight;
    ImageView drawerMenuIconSignOut;
    RelativeLayout drawerSignOutRelativeLayout;
    public static boolean isSignedIn;
    @Inject
    protected AppSharedPreference appSharedPreference;
    @Inject
    protected Utility utility;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_drawer);

        toolBar = findViewById(R.id.toolBar);
        setSupportActionBar(toolBar);
        getSupportActionBar().setIcon(new ColorDrawable(Color.TRANSPARENT));
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.primary_color)));
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayUseLogoEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        LayoutInflater inflater = LayoutInflater.from(this);

        calculateScreenSize();
        v = inflater.inflate(R.layout.custom_title, null);
        params = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);

        v.setLayoutParams(params);
        this.getSupportActionBar().setCustomView(v);


        titleTextView = findViewById(R.id.titleTextView);
        drawerList = findViewById(R.id.drawerList);
        frameLayout = findViewById(R.id.frameLayout);
        drawerIcon = findViewById(R.id.drawerIcon);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerMenuIconSignOut = findViewById(R.id.drawerMenuIconSignOut);
        drawerSignOutRelativeLayout = findViewById(R.id.drawerSignOutRelativeLayout);

        titleTextView.setTypeface(null, Typeface.BOLD);

        View header = getLayoutInflater().inflate(R.layout.drawer_header_view,
                null);
        drawerList.addHeaderView(header);

        int slideMenuLeftRightSpace = (int) (screenWidth * 0.037); //0.3
        int slideMenuWidth = (int) (screenWidth * 0.0864); //0.7
        int slideMenuHeight = (int) (screenWidth * 0.0864);
        int menuWidth = (int) (screenWidth * 0.0741); //0.5

        RelativeLayout.LayoutParams btn_drawer = (RelativeLayout.LayoutParams) drawerIcon.getLayoutParams();
        btn_drawer.setMargins(slideMenuLeftRightSpace, 0, slideMenuLeftRightSpace, 0);
        btn_drawer.width = slideMenuWidth;
        btn_drawer.height = slideMenuHeight;
        drawerIcon.setLayoutParams(btn_drawer);

        RelativeLayout.LayoutParams drawerMenuIconSignOutLay = (RelativeLayout.LayoutParams) drawerMenuIconSignOut.getLayoutParams();
        drawerMenuIconSignOutLay.setMargins(slideMenuLeftRightSpace, 0, slideMenuLeftRightSpace, 0);
        drawerMenuIconSignOutLay.width = menuWidth;
        drawerMenuIconSignOutLay.height = menuWidth;
        drawerMenuIconSignOut.setLayoutParams(drawerMenuIconSignOutLay);

        drawerIcon.setBackgroundResource(R.drawable.ic_drawer);

        drawerSignOutRelativeLayout.setOnClickListener((v)->{
            utility.showConformationDialog(this, "Are you sure you want logout?",
                    (DialogInterface.OnClickListener) (dialog, which) -> {
                        appSharedPreference.clear();
                        LoginActivity.start(this);
                        finish();
            });
        });
        drawerIcon.setOnClickListener(v -> {
            // TODO Auto-generated method stub
            if (drawerLayout.isDrawerOpen(Gravity.START)) {
                drawerLayout.closeDrawers();
            } else {
                drawerLayout.openDrawer(Gravity.START);
            }
        });
        loadMenu();
    }

    private void calculateScreenSize() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        WindowManager windowmanager = (WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        assert windowmanager != null;
        windowmanager.getDefaultDisplay().getMetrics(displayMetrics);
        screenWidth = displayMetrics.widthPixels;
        screenHeight = displayMetrics.heightPixels;
    }

    @SuppressLint("ResourceType")
    protected void loadMenu() {

        navDrawerItems = new ArrayList<>();

        drawerItemcount = Integer.parseInt(this.getString(R.string.drawermenuitem_count));
        ArrayList<String> selectedMenuList = new ArrayList<>();
        Resources res = getResources();

        for (int j = 1; j <= drawerItemcount; j++) {

            try {
                String strItem = "drawermenuitem_" + j;

                details = res.getIdentifier(strItem, "array", getPackageName());
                typedArray = res.obtainTypedArray(details);

                for (int k = 0; k < typedArray.length(); k++) {
                    MenuItem = typedArray.getString(1);
                    String menuIconName = "", menuLabel = "";
                    menuLabel = typedArray.getString(2);

                    if (k == 2) {
                        if (typedArray.getString(k).equals("checkstatus")) {
                            strcheckstatusTitle = typedArray.getString(6);
                        } else if (typedArray.getString(k).equals("referearn")) {
                            strReferAndEarn = typedArray.getString(6);
                        } else if (typedArray.getString(k).equals("managevehicle")) {
                            strManagevehicle = typedArray.getString(6);
                        } else if (typedArray.getString(k).equals("managedocument")) {
                            strManagedocument = typedArray.getString(6);
                        } else if (typedArray.getString(k).equals("yourtrip")) {
                            strYourtrip = typedArray.getString(6);
                        } else if (typedArray.getString(k).equals("earningreports")) {
                            strEarningreports = typedArray.getString(6);
                        } else if (typedArray.getString(k).equals("livetracking")) {
                            strLivetracking = typedArray.getString(6);
                        } else if (typedArray.getString(k).equals("setting")) {
                            strSetting = typedArray.getString(6);
                        }
                    }
                    menuIconName = typedArray.getString(7);

                    if (k == 8) {
                        if (typedArray.getString(k).equals("Guest")) {
                            if (!isSignedIn) {
                                navDrawerItems.add(new NavDrawerItem(MenuItem, menuIconName));
                                selectedMenuList.add(strItem);
                            }

                        } else if (typedArray.getString(k).equals("SignedIn")) {
                            if (isSignedIn) {
                                navDrawerItems.add(new NavDrawerItem(MenuItem, menuIconName));
                                selectedMenuList.add(strItem);
                            }

                        }
                    }

                }

            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }

        }

        drawerList.setOnItemClickListener(new SlideMenuClickListener());
        adapter = new NavDrawerListAdapter(getApplicationContext(), screenWidth,
                navDrawerItems, selectedMenuList);
        adapter.notifyDataSetChanged();
        drawerList.setAdapter(adapter);

        mDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.drawable.ic_drawer, // nav
                // menu
                // toggle
                // icon
                R.string.app_name, // nav drawer open - description for
                // accessibility
                R.string.app_name // nav drawer close - description for
                // accessibility
        ) {
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu();
                mDrawerToggle.setDrawerIndicatorEnabled(true);
            }

            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu();
                mDrawerToggle.setDrawerIndicatorEnabled(true);
            }

            @SuppressLint("NewApi")
            public void onDrawerSlide(View drawerView, float slideOffset) {
                mDrawerToggle.setDrawerIndicatorEnabled(true);
                frameLayout.setTranslationX(slideOffset
                        * drawerList.getWidth());
//                getActionBarView().setTranslationX(
//                        slideOffset * drawerList.getWidth());
            }
        };
        drawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
    }


    private View getActionBarView() {
        int actionViewResId = 0;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            actionViewResId = getResources().getIdentifier(
                    "abs__action_bar_container", "id", getPackageName());
        } else {
            actionViewResId = Resources.getSystem().getIdentifier(
                    "action_bar_container", "id", "android");
        }
        if (actionViewResId > 0) {
            return this.findViewById(actionViewResId);
        }

        return null;
    }

    protected static int currentPosition = -1;


    private class SlideMenuClickListener implements
            ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position,
                                long id) {
            TextView txtView = (TextView) view.findViewById(android.R.id.text1);
            TextView txtView_section = (TextView) view
                    .findViewById(android.R.id.text2);
            if (position != 0) {
                if (txtView != null && !txtView.getText().toString().trim().equals("")) {
                    getItemValues = UserDefinedMethod(txtView);
                    if (currentPosition != position) {
                        selectItem(getItemValues, position);
                    }
                    drawerLayout.closeDrawers();
                } else {
                    if (txtView_section != null && txtView_section.getText().toString().trim().equals("")) {
                        drawerLayout.closeDrawers();
                    }
                }
            }
        }
    }


    @SuppressLint("ResourceType")
    public String[] UserDefinedMethod(TextView txtView) {
        // TODO Auto-generated method stub
        boolean getValue = false;
        Resources res = getResources();
        String menuLabelText = txtView.getText().toString();
        for (int j = 1; j <= drawerItemcount; j++) {
            try {
                String strItem = "drawermenuitem_" + j;
                details1 = res
                        .getIdentifier(strItem, "array", getPackageName());
                typedArray1 = res.obtainTypedArray(details1);

                for (int k = 0; k < typedArray1.length(); k++) {
                    MenuItem1 = typedArray1.getString(1);
                    if (menuLabelText.equals(MenuItem1)) {
                        getItemValues = new String[6];
                        getItemValues[0] = typedArray1.getString(2); // enum

                        getItemValues[1] = typedArray1.getString(3); // url

                        getItemValues[2] = typedArray1.getString(4); // internal
                        // /
                        // external
                        // browser
                        getItemValues[3] = typedArray1.getString(5); // video
                        // /
                        // website
                        getItemValues[4] = typedArray1.getString(6); // Screen
                        // Title
                        // Label
                        getItemValues[5] = typedArray1.getString(7);
                        getValue = true;
                    }
                    if (getValue) {
                        break;
                    }
                }
            } catch (Resources.NotFoundException e) {
            }
        }
        return getItemValues;
    }

    private void selectItem(String[] strItem, int position) {
        Intent intent = null;
        currentPosition = position;
        if (strItem != null) {
            String menuLabel = strItem[0];
            if (menuLabel.equals("checkstatus")) {
                DocumentStatusActivity.start(this);
            } else if (menuLabel.equals("referearn")) {
                intent = new Intent(this, ReferalProgramActivity.class);
                startActivity(intent);
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(android.view.MenuItem item) {
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        switch (item.getItemId()) {
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    /**
     * When using the ActionBarDrawerToggle, you must call it during
     * onPostCreate() and onConfigurationChanged()...
     */

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        if (hasFocus) {
            // mDrawerList.setViewsBounds(ParallaxScollListView.ZOOM_X2);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        currentPosition = -1;
    }

}
