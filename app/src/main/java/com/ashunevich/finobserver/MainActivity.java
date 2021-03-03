package com.ashunevich.finobserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;


import com.ashunevich.finobserver.DashboardPackage.RoomDashboard_VewModel;
import com.ashunevich.finobserver.TransactionsPackage.RoomTransactions_ViewModel;
import com.ashunevich.finobserver.UtilsPackage.PostPOJO;
import com.ashunevich.finobserver.databinding.ActivityMainBinding;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private final ArrayList<String> tabNames = new ArrayList<>();
    private final ArrayList<Integer> drawables = new ArrayList<>();
    private RoomTransactions_ViewModel transactionsRoomData;
    private RoomDashboard_VewModel dashboardRoomData;
    private final static String zero = "0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionsRoomData = new ViewModelProvider(this).get(RoomTransactions_ViewModel.class);
        dashboardRoomData = new ViewModelProvider(this).get(RoomDashboard_VewModel.class);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        init();

    }

    private void init(){
        binding.viewPager.setAdapter( new PageAdapter(getSupportFragmentManager(),getLifecycle()));
        tabNames.add("Dashboard");
        tabNames.add("Transaction");
        drawables.add(R.drawable.ic_dashboard);
        drawables.add(R.drawable.ic_transactions);
        TabLayoutMediator tabLayoutMediator= new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
                  tab.setIcon(drawables.get(position));
                   tab.setText(tabNames.get(position));
        });
        tabLayoutMediator.attach();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.deleteData) {
            createAlertDialog();
        }
        return true;
    }

    private void createAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

          builder.setTitle("WARNING");
          builder.setMessage("You are going to erase all data. Proceed?");
         builder.setPositiveButton("YES", (dialogInterface, i) -> {
             transactionsRoomData.deleteAll();
             dashboardRoomData.deleteAll();
             EventBus.getDefault().post(new PostPOJO(zero));
            Snackbar.make(binding.mainLayout,"All data was deleted.", BaseTransientBottomBar.LENGTH_SHORT).show();
         });

        builder.setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.cancel());
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }







}