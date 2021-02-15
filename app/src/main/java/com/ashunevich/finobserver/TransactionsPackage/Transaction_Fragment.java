package com.ashunevich.finobserver.TransactionsPackage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.ashunevich.finobserver.databinding.TransactionsFragmentBinding;

import java.util.ArrayList;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class Transaction_Fragment extends Fragment {
    private TransactionsFragmentBinding binding;
    private final List<Transaction_Item> listContentArr = new ArrayList<>();
    RecyclerView_Adapter adapter;
    RoomTransactions_ViewModel modelDatabase;

    public Transaction_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }




    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert inflater != null;
        binding = TransactionsFragmentBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        //fix for  bug that freeze app, when app launch after was closed, when user switching to Transaction tab
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::initRecView, 1000);
        super.onViewCreated(view, savedInstanceState);
    }
    //init RecyclerView
    private void initRecView() {
        binding.transactionView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, true));
        adapter = new RecyclerView_Adapter(listContentArr);
        binding.transactionView.setAdapter(adapter);
        modelDatabase = new ViewModelProvider(requireActivity()).get(RoomTransactions_ViewModel.class);
        modelDatabase.getAllTransactions().observe(requireActivity(), transaction_items -> {
            adapter.updateItemList(transaction_items);
            binding.transactionView.smoothScrollToPosition(transaction_items.size());
        });
        Transaction_Utils.hideProgressBar(binding.progressBar,binding.loadingText);
    }








    //observe data from Fragment A and create object based on it



    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
    
}
