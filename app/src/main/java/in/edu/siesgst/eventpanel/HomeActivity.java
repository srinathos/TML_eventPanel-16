package in.edu.siesgst.eventpanel;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import in.edu.siesgst.eventpanel.fragment.ParticipantListFragment;
import in.edu.siesgst.eventpanel.fragment.participant.ParticipantContent;

public class HomeActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, ParticipantListFragment.OnListFragmentInteractionListener {

    private final String PARTICIPANT_FRAGMENT_TAG = "pa";
    private final String SCANNER_TAG = "sc";
    private String uLastFragmentTag = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        setupUI();
        if (findViewById(R.id.home_fragment_framelayout) != null) {
            if (savedInstanceState != null) {
                return;
            }
            applyFragment(PARTICIPANT_FRAGMENT_TAG);
        }
    }

    private void setupUI() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onParticipantSelected(ParticipantContent.Participant item) {
        //TODO DO SHIT WHEN PARTICIPANT IS SELECTED
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*if (id == R.id.action_settings) {
            return true;
        }*/

        return super.onOptionsItemSelected(item);
    }

    private void applyFragment(String FRAGMENT_TAG) {
        switch (FRAGMENT_TAG) {
            case PARTICIPANT_FRAGMENT_TAG: {
                if (uLastFragmentTag == null) {
                    getSupportFragmentManager().beginTransaction().
                            add(R.id.home_fragment_framelayout, new ParticipantListFragment(), PARTICIPANT_FRAGMENT_TAG).
                            commit();
                } else if (!uLastFragmentTag.equals(PARTICIPANT_FRAGMENT_TAG)) {
                    getSupportFragmentManager().beginTransaction().
                            remove(getSupportFragmentManager().findFragmentByTag(uLastFragmentTag)).
                            add(R.id.home_fragment_framelayout, new ParticipantListFragment(), PARTICIPANT_FRAGMENT_TAG).
                            commit();
                }
                uLastFragmentTag = PARTICIPANT_FRAGMENT_TAG;
                break;
            }
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.navigation_header_option_participantList: {
                applyFragment(PARTICIPANT_FRAGMENT_TAG);
                break;
            }
            case R.id.navigation_header_option_scanner: {
                startActivity(new Intent(this,BarcodeScannerActivity.class));
                break;
            }
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
