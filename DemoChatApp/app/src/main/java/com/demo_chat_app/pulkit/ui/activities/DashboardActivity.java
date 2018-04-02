package com.demo_chat_app.pulkit.ui.activities;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.demo_chat_app.pulkit.R;
import com.demo_chat_app.pulkit.ui.fragments.tabs.AboutUsFragment;
import com.demo_chat_app.pulkit.ui.fragments.navigation.HomeFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DashboardActivity extends AppCompatActivity {

    private static final String TAG = DashboardActivity.class.getName();

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_toolbar_name)
    TextView tvToolbarName;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.rel_home)
    RelativeLayout relHome;
    @BindView(R.id.rel_about_us)
    RelativeLayout relAboutUs;
    @BindView(R.id.rel_logout)
    RelativeLayout relLogout;

    private final String toolbarTitle = "WhatsApp";
    private final String bundleArgument = "home_tab";
    private final String selectedTab = "2";

    private HomeFragment homeFragment;
    private Bundle bundle;

    private ActionBarDrawerToggle actionBarDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        ButterKnife.bind(this);

        homeFragment = new HomeFragment();
        bundle = new Bundle();

        init();
        setupToolbar();
        initDrawer();

    }

    private void init() {

        bundle.putString(bundleArgument, selectedTab);
        homeFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().add(R.id.dashboard_container, homeFragment).commit();

    }

    private void setupToolbar() {

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        toolbar.setTitle(toolbarTitle);

    }

    private void initDrawer() {

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.setDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };
    }

    @OnClick(R.id.rel_home)
    public void onClickHomeTab() {

        bundle.putString(bundleArgument, selectedTab);
        homeFragment.setArguments(bundle);

        getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_container, homeFragment).commit();

        drawerLayout.closeDrawers();

    }

    @OnClick(R.id.rel_about_us)
    public void onClickAboutUsTab() {

        getSupportFragmentManager().beginTransaction().replace(R.id.dashboard_container, new AboutUsFragment()).commit();

        drawerLayout.closeDrawers();

    }

    MenuItem action_settings;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        action_settings = menu.findItem(R.id.action_settings);

        action_settings.setVisible(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ((item.getItemId())) {

            case R.id.action_settings:

                Toast.makeText(this, "Settings", Toast.LENGTH_SHORT).show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
