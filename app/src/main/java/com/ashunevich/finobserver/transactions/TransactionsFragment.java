package com.ashunevich.finobserver.transactions;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.TransactionsFragmentBinding;

import java.util.ArrayList;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.ashunevich.finobserver.transactions.TransactionsUtils.hideProgressBar;

public class TransactionsFragment extends Fragment {
    private TransactionsFragmentBinding binding;
    private final List<TransactionItem> listContentArr = new ArrayList<>();
    RecyclerAdapter adapter;
    RoomTransactionsViewModel modelDatabase;
    Boolean FILTER_TYPE = true;

    public TransactionsFragment() {
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

        setSpinnerListeners();
        setTextWatcher();
        setSpinner();
        UICondition(false);
        setCheckBoxListener();
    }
    //init RecyclerView
    private void initRecView() {
        binding.transactionView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RecyclerAdapter(listContentArr);
        binding.transactionView.setAdapter(adapter);
        modelDatabase = new ViewModelProvider(requireActivity()).get(RoomTransactionsViewModel.class);
        modelDatabase.getAllTransactions().observe(requireActivity(), transaction_items -> {
            adapter.updateItemList(transaction_items);
            binding.transactionView.smoothScrollToPosition(0);
        });
        hideProgressBar(binding.progressBar,binding.loadingText);
        Log.d("ITEMS COUNT",String.valueOf(adapter.getItemCount()));
    }



    private void setTextWatcher(){
        binding.filterPlainText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                setFilter(editable.toString(),FILTER_TYPE);
            }
        });
    }

    private void setSpinner(){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(),
                R.layout.support_simple_spinner_dropdown_item, getResources().getStringArray(R.array.filterTypes));
        binding.filterType.setAdapter(adapter);
    }



    private void setSpinnerListeners(){
        binding.filterType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i){
                    case 1: FILTER_TYPE = true;
                    case 2: FILTER_TYPE = false;
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    private void UICondition(boolean bool){
        if(bool){
            binding.filterType.setVisibility(View.VISIBLE);
            binding.filterPlainText.setVisibility(View.VISIBLE);
        }
        else {
            binding.filterType.setVisibility(View.GONE);
            binding.filterPlainText.setVisibility(View.GONE);
            binding.filterPlainText.getText().clear();
        }
    }

    private void setCheckBoxListener(){
        binding.checkBox.setOnCheckedChangeListener((compoundButton, b) -> UICondition(b));
    }

    private void setFilter(String text, Boolean bool){
        ArrayList<TransactionItem> filteredList = new ArrayList<>();

        for(TransactionItem item:listContentArr){
            if(bool){
                if (item.getTransactionDate().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
            else {
                if (item.getTransactionAccount().toLowerCase().contains(text.toLowerCase())) {
                    filteredList.add(item);
                }
            }
        }
        adapter.filter(filteredList);
    }


    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }


}
