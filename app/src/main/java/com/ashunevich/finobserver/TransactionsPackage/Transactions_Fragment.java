package com.ashunevich.finobserver.TransactionsPackage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashunevich.finobserver.UtilsPackage.TransactionViewModel;
import com.ashunevich.finobserver.databinding.TransactionsFragmentBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

public class Transactions_Fragment extends Fragment {
    private TransactionsFragmentBinding binding;
    private final ArrayList<Transaction_Item> listContentArr = new ArrayList<>();
    Transaction_RecyclerViewAdapter adapter;
    TransactionViewModel model;


    public Transactions_Fragment() {
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
        model = new ViewModelProvider(requireActivity()).get(TransactionViewModel.class);
        Observer<ArrayList<Transaction_Item>> observer = (Observer<ArrayList<Transaction_Item>>) this::initObserve;
        model.getSelected().observeForever(observer);
        super.onViewCreated(view, savedInstanceState);
    }
    //init RecyclerView
    private void initRecView(){
        binding.transactionView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new Transaction_RecyclerViewAdapter(listContentArr);
        adapter.setListContent(listContentArr);
        binding.transactionView.setAdapter(adapter);
    }

    //observe data from Fragment A and create object based on it
    private void initObserve(ArrayList<Transaction_Item> list){
        adapter.updateItemList(list);


        /*
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
             */
       // });
        }


    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
    
}
