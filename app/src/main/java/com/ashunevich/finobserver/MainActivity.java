package com.ashunevich.finobserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.ashunevich.finobserver.dashboard.RoomDashboardVewModel;
import com.ashunevich.finobserver.transactions.RoomTransactionsViewModel;
import com.ashunevich.finobserver.utils.PostPOJO;
import com.ashunevich.finobserver.databinding.ActivityMainBinding;

import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final ArrayList<String> tabNames = new ArrayList<>();
    private final ArrayList<Integer> drawables = new ArrayList<>();

    private RoomTransactionsViewModel transactionsRoomData;
    private RoomDashboardVewModel dashboardRoomData;
    private final static String zero = "0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        transactionsRoomData = new ViewModelProvider(this).get(RoomTransactionsViewModel.class);
        dashboardRoomData = new ViewModelProvider(this).get(RoomDashboardVewModel.class);

        init();

    }

    private void init(){
        binding.viewPager.setAdapter( new PageAdapter(getSupportFragmentManager(),getLifecycle()));
        tabNames.addAll(Arrays.asList(getResources().getStringArray(R.array.tabNames)));

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