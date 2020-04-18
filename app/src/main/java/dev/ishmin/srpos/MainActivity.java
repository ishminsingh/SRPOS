package dev.ishmin.srpos;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Menu;


import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.core.view.GravityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import dev.ishmin.srpos.Fragments.dashboard.DashboardFragment;
import dev.ishmin.srpos.Fragments.expenses.ExpensesFragment;
import dev.ishmin.srpos.Fragments.purchase.PurchaseFragment;
import dev.ishmin.srpos.Fragments.sales.SalesFragment;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration AppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    static SQLiteDatabase SRPOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SRPOS=this.openOrCreateDatabase("SRPOS",MODE_PRIVATE,null);

        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        AppBarConfiguration = new AppBarConfiguration.Builder(R.id.dashboardFragment, R.id.billingFragment, R.id.productsFragment, R.id.salesFragment)
                .setDrawerLayout(drawer)
                .build();
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupActionBarWithNavController(this, navController, AppBarConfiguration);

//        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new DashboardFragment()).commit();
            navigationView.setCheckedItem(R.id.dashboardFragment);
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, AppBarConfiguration);
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        return NavigationUI.navigateUp(navController, AppBarConfiguration)
//                || super.onSupportNavigateUp();
    }
    @Override
    public void onBackPressed() {
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START);
        } else{
        super.onBackPressed();
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    public void user_signout(){
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        if ((item.getItemId()) == R.id.action_signOut) {
        user_signout();
        }
        return true;
    }
//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//        switch(item.getItemId()){
//            case R.id.billingFragment:
//                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new SalesFragment()).commit();
//            case R.id.productsFragment:
//                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new PurchaseFragment()).commit();
//            case R.id.salesFragment:
//                getSupportFragmentManager().beginTransaction().replace(R.id.nav_host_fragment, new ExpensesFragment()).commit();
//        }
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
