package com.ashunevich.finobserver;

import com.ashunevich.finobserver.DashboardAccountPackage.Dashboard_Fragment;
import com.ashunevich.finobserver.TransactionsPackage.Transactions_Fragment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class PageAdapter extends FragmentStateAdapter {

    ArrayList<Fragment> fragments;

    public PageAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
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
        fragments.add(0, new Dashboard_Fragment());
        fragments.add(1,new Transactions_Fragment());
        return fragments.size();
    }
}
