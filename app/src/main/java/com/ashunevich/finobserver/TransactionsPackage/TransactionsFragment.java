package com.ashunevich.finobserver.TransactionsPackage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashunevich.finobserver.DashboardAccountPackage.AccountItem;
import com.ashunevich.finobserver.DashboardAccountPackage.AccountNewtItem;
import com.ashunevich.finobserver.DashboardAccountPackage.DashboardAccRecViewAdapter;
import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.DashboardFragmentBinding;
import com.ashunevich.finobserver.databinding.TransactionsFragmentBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import static android.app.Activity.RESULT_CANCELED;

public class TransactionsFragment extends Fragment {
    private TransactionsFragmentBinding binding;
    private final ArrayList<TransactionItem> listContentArr = new ArrayList<>();
    TransactionItem item = new TransactionItem() ;
    TransactionRecViewAdapter adapter;
    EventBus bus;


    public TransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private String getDate(){
        @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("yyyy-MMM-dd");
        return format.format(Calendar.getInstance());
    }

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = TransactionsFragmentBinding.inflate(inflater, container, false);
        binding.transactionView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TransactionRecViewAdapter(listContentArr);
        adapter.setListContent(listContentArr);
        binding.transactionView.setAdapter(adapter);
        bus = EventBus.getDefault();
        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
    
}
