package com.ashunevich.finobserver;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.ashunevich.finobserver.databinding.ActivityMainBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private ArrayList<String> tabNames = new ArrayList<>();
    private ArrayList<Integer> drawables = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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






}