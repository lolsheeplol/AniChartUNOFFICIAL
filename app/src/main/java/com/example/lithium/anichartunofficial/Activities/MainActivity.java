package com.example.lithium.anichartunofficial.Activities;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;

import com.example.lithium.anichartunofficial.Fragments.AboutFragment;
import com.example.lithium.anichartunofficial.Fragments.AiringFragment;
import com.example.lithium.anichartunofficial.Fragments.SeasonFragment;
import com.example.lithium.anichartunofficial.Fragments.SettingsFragment;
import com.example.lithium.anichartunofficial.R;
import com.example.lithium.anichartunofficial.Utils.LoggerUtil;
import com.example.lithium.anichartunofficial.Utils.PermissionsUtil;
import com.example.lithium.anichartunofficial.Utils.SeasonYearUtil;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;

public class MainActivity extends AppCompatActivity {
    private static final String LOG = MainActivity.class.getSimpleName();

    private FirebaseAnalytics mFirebaseAnalytics;
    private Context mContext = this;
    private Drawer mDrawer = null;
    private Toolbar mToolbar;
    private FragmentManager mFragMan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle("AniChart UNOFFICIAL");
        setSupportActionBar(mToolbar);

        mFragMan = getSupportFragmentManager();
        getFragment(new AiringFragment());

        initialTasks();
        logFirebaseEvents();
    }

    private void initialTasks() {
        PermissionsUtil.checkPermissionsRequestGrant(mContext);
        setupDrawer();
    }

    private void logFirebaseEvents() {
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        // log app_open event for the current season & year
        Bundle bundle = new Bundle();
        String seasonYear = SeasonYearUtil.getCurrentYear() + SeasonYearUtil.getCurrentSeason();
        bundle.putString("app_open_for_season_year", seasonYear);
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.APP_OPEN, bundle);
    }

    private void setupDrawer() {
        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName(getResources().getString(R.string.season_airing)
                + " - "  + SeasonYearUtil.getCurrentSeason() + " " + SeasonYearUtil.getCurrentYear());
        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName(R.string.season_winter);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName(R.string.season_spring);
        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName(R.string.season_summer);
        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withName(R.string.season_fall);

        AccountHeader header = new AccountHeaderBuilder().withActivity(this)
                .withHeaderBackground(R.color.primary)
                //.withHeaderBackground(R.mipmap.ic_logo_anichart)
                .build();

        mDrawer = new DrawerBuilder().withActivity(this)
                .withToolbar(mToolbar)
                .withAccountHeader(header)
                .addDrawerItems(
                        item1,
                        new DividerDrawerItem(),
                        item2,
                        item3,
                        item4,
                        item5,
                        new DividerDrawerItem(),
                        new SecondaryDrawerItem().withName(R.string.settings),
                        new SecondaryDrawerItem().withName(R.string.about)
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        mDrawer.closeDrawer();
                        switch (position) {
                            case 1:
                                mToolbar.setTitle("AniChart UNOFFICIAL");
                                getFragment(new AiringFragment());
                                break;
                            case 3:
                                mToolbar.setTitle("Winter");
                                getFragment(new SeasonFragment());
                                break;
                            case 4:
                                mToolbar.setTitle("Spring");
                                getFragment(new SeasonFragment());
                                break;
                            case 5:
                                mToolbar.setTitle("Summer");
                                getFragment(new SeasonFragment());
                                break;
                            case 6:
                                mToolbar.setTitle("Fall");
                                getFragment(new SeasonFragment());
                                break;
                            case 8:
                                mToolbar.setTitle("Settings");

                                break;
                            case 9:
                                mToolbar.setTitle("About");
                                getFragment(new AboutFragment());
                                break;
                        }
                        return true;
                    }
                })
                .build();
    }

    private void getFragment(Fragment fragment) {
        FragmentTransaction transaction = mFragMan.beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        //transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    protected void onStart() {
        super.onStart();

        // The activity is either being restarted or started for the first time
        // so this is where we should make sure that GPS is enabled
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

        // Release the Camera because we don't need it when paused
        // and other activities might need to use it.
    }

    @Override
    protected void onStop() {
        super.onStop();

        // Save the note's current draft, because the activity is stopping
        // and we want to be sure the current note progress isn't lost.

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        if (mDrawer != null && mDrawer.isDrawerOpen()) {
            mDrawer.closeDrawer();
        } else {
            super.onBackPressed();
        }
    }
}
