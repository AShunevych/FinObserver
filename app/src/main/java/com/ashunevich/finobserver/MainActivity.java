package com.ashunevich.finobserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.os.Bundle;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;


import com.ashunevich.finobserver.dashboard.DashboardFragment;
import com.ashunevich.finobserver.dashboard.RoomDashboardVewModel;
import com.ashunevich.finobserver.transactions.RoomTransactionsViewModel;
import com.ashunevich.finobserver.transactions.TransactionBoardFragment;
import com.ashunevich.finobserver.utils.PostPOJO;
import com.ashunevich.finobserver.databinding.ActivityMainBinding;

import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ashunevich.finobserver.utils.Utils.showSnackBar;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final List<String> tabNames = new ArrayList<>();
    private final List<Integer> drawables = new ArrayList<>();
    private RoomTransactionsViewModel transactionsRoomData;
    private RoomDashboardVewModel dashboardRoomData;
    private final static String ZERO_VALUE = "0.0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    binding = ActivityMainBinding.inflate(getLayoutInflater());

    setContentView(binding.getRoot());
    init();
    initModels ();
    }

    private void init(){
    binding.viewPager.setAdapter(new PageAdapter (getSupportFragmentManager (), getLifecycle ()));
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

    private void initModels(){
        transactionsRoomData = new ViewModelProvider(this).get(RoomTransactionsViewModel.class);
        dashboardRoomData = new ViewModelProvider(this).get(RoomDashboardVewModel.class);
    }


    private void createAlertDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("WARNING").setMessage("You are going to erase all data. Proceed?");
         builder.setPositiveButton("YES", (dialogInterface, i) -> {
             transactionsRoomData.deleteAll();
             dashboardRoomData.deleteAll();
             EventBus.getDefault().post(new PostPOJO(ZERO_VALUE));
             showSnackBar(binding.mainLayout,"All data was deleted.");
         });

        builder.setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.cancel());
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }


    private static class PageAdapter extends FragmentStateAdapter {

        ArrayList<Fragment> fragments;

        private PageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragments.get(position);
        }

        @Override
        public int getItemCount() {
            fragments = new ArrayList<>();
            fragments.add(0, new DashboardFragment ());
            fragments.add(1,new TransactionBoardFragment ());
            return fragments.size();
        }
    }



}