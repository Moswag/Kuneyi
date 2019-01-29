package com.android.cytex.acivities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.cytex.MainActivity;
import com.android.cytex.MapsActivitySearch;
import com.android.cytex.R;
import com.android.cytex.backgroundtask.DownloadComments;
import com.android.cytex.backgroundtask.DownloadCompanies;
import com.android.cytex.backgroundtask.DownloadLikes;
import com.android.cytex.backgroundtask.DownloadPosts;
import com.android.cytex.fragment.AboutApp;
import com.android.cytex.fragment.AboutDeveloper;
import com.android.cytex.fragment.Adverts;
import com.android.cytex.fragment.MapFragment;
import com.android.cytex.fragment.RandomPost;
import com.android.cytex.fragment.Welcome;
import com.android.cytex.model.User;
import com.android.cytex.ui.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.Random;

import layout.ListPlaces;

public class Kuneyi extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DatabaseReference userDB;
    private FirebaseAuth mAuth;
    private User myAccount;
    private Context context;
    DownloadPosts downloadPosts;
    DownloadCompanies downloadCompanies;
    DownloadComments downloadComments;
    DownloadLikes downloadLikes;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_d);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Welcome firstFragment=new Welcome();
        FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame, firstFragment, "fragment1");
        fragmentTransaction.commit();

        downloadCompanies=new DownloadCompanies(getApplicationContext());
        downloadCompanies.getJSON();

        downloadPosts=new DownloadPosts(getApplicationContext());
        downloadPosts.getJSON();

        downloadComments=new DownloadComments(getApplicationContext());
        downloadComments.getJSON();
        downloadComments.saveToRemoteServer();

        downloadLikes=new DownloadLikes(getApplicationContext());
        downloadLikes.getJSON();
        downloadLikes.saveToRemoteServer();


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
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
        getMenuInflater().inflate(R.menu.navigation_d, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        else  if (id == R.id.logout) {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
            Kuneyi.this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_category) {
            // Handle the camera action
            setTitle("Pick One");
            Welcome firstFragment=new Welcome();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, firstFragment, "fragment1");
            fragmentTransaction.commit();
        }
       else if (id == R.id.nav_random) {
            // Handle the camera action
            setTitle("Random Posts");
            RandomPost firstFragment=new RandomPost();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, firstFragment, "fragment1");
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_adverts) {
            // Handle the camera action
            setTitle("Adverts");
            Adverts firstFragment=new Adverts();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, firstFragment, "fragment1");
            fragmentTransaction.commit();
        }


        else if (id == R.id.chat) {
            startActivity(new Intent(getApplicationContext(),MainActivity.class));

        } else if (id == R.id.nav_location) {
            // Handle the camera action
            setTitle("Nearby Places By Category");
            MapFragment firstFragment=new MapFragment();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, firstFragment, "fragment1");
            fragmentTransaction.commit();
           //  startActivity(new Intent(getApplicationContext(),MapsActivitySearch.class));
        }
        else if (id == R.id.nav_about_app) {
            setTitle("About Developer");
            AboutApp firstFragment=new AboutApp();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, firstFragment, "fragment1");
            fragmentTransaction.commit();
        }
        else if (id == R.id.nav_dev) {
            setTitle("About Developer");
            AboutDeveloper firstFragment=new AboutDeveloper();
            FragmentTransaction fragmentTransaction=getSupportFragmentManager().beginTransaction();
            fragmentTransaction.replace(R.id.frame, firstFragment, "fragment1");
            fragmentTransaction.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}
