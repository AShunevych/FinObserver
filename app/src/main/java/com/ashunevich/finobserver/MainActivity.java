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


import com.ashunevich.finobserver.viewmodel.RoomDashboardViewModel;
import com.ashunevich.finobserver.viewmodel.RoomTransactionsViewModel;
import com.ashunevich.finobserver.utility.PostPOJO;
import com.ashunevich.finobserver.databinding.ActivityMainBinding;

import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.ashunevich.finobserver.utility.Constants.ZERO_VALUE;
import static com.ashunevich.finobserver.utility.Utils.genericDialogOptions;
import static com.ashunevich.finobserver.utility.ViewUtils.uiShowSnackBar;


public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    private final List<String> tabNames = new ArrayList<>();
    private final List<Integer> drawables = new ArrayList<>();
    private RoomTransactionsViewModel transactionsRoomData;
    private RoomDashboardViewModel dashboardRoomData;

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

    drawables.add(R.drawable.ic_dashboard_ico);
    drawables.add(R.drawable.ic_transactions_ico);

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
        dashboardRoomData = new ViewModelProvider(this).get(RoomDashboardViewModel.class);
    }


    private void createAlertDialog(){
        AlertDialog.Builder builder = genericDialogOptions (this,"WARNING",
                "You are going to erase all data. Proceed?");

         builder.setPositiveButton("YES", (dialogInterface, i) -> {
             transactionsRoomData.deleteAll();
             dashboardRoomData.deleteAll();
             EventBus.getDefault().post(new PostPOJO(ZERO_VALUE));
             uiShowSnackBar (binding.mainLayout,"All data was deleted.");
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