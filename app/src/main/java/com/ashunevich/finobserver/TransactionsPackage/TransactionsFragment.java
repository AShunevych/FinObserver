package com.ashunevich.finobserver.TransactionsPackage;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashunevich.finobserver.DashboardAccountPackage.AccountItem;
import com.ashunevich.finobserver.DashboardAccountPackage.AccountNewtItem;
import com.ashunevich.finobserver.DashboardAccountPackage.DashboardAccRecViewAdapter;
import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.UtilsPackage.TransactionViewModel;
import com.ashunevich.finobserver.UtilsPackage.Utils;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_CANCELED;

public class TransactionsFragment extends Fragment {
    private TransactionsFragmentBinding binding;
    private final ArrayList<TransactionItem> listContentArr = new ArrayList<>();
    TransactionRecViewAdapter adapter;
    TransactionViewModel model;


    public TransactionsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    //, String account, String transactionCategory, Double value


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = TransactionsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initRecView();
        initObserve();
       super.onViewCreated(view, savedInstanceState);
    }
    //init RecyclerView
    private void initRecView(){
        binding.transactionView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new TransactionRecViewAdapter(listContentArr);
        adapter.setListContent(listContentArr);
        binding.transactionView.setAdapter(adapter);
    }

    //observe data from Fragment A and create object based on it
    private void initObserve(){
        model = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
        model.getSelected().observe(getViewLifecycleOwner(), item -> {
            TransactionItem newAccountItem = new TransactionItem() ;
            newAccountItem.setImage(Image(item.getTransactionType()));
            newAccountItem.setTransactionValue(item.getTransactionValue());
            newAccountItem.setTransactionCurrency("UAH");
            newAccountItem.setTransactionCategory(item.getTransactionCategory());
            newAccountItem.setTransactionAccount(item.getTransactionAccount());
            listContentArr.add(0,newAccountItem);
            adapter.notifyDataSetChanged();
        });
        }


    private Drawable Image(String type){
        Drawable drawable;
        if(type.matches("Income")){
            drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_arrow_drop_up);
        }
        else {
            drawable = ContextCompat.getDrawable(getContext(),R.drawable.ic_arrow_drop_down);
        }
        return drawable;
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
    
}
