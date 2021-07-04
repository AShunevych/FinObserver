package com.ashunevich.finobserver.transactions;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.TransactionsFragmentBinding;

import java.util.ArrayList;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.ashunevich.finobserver.utils.Utils.initAfterDelay;
import static com.ashunevich.finobserver.utils.Utils.returnActiveChipId;
import static com.ashunevich.finobserver.utils.Utils.uiHideHideShow;
import static com.ashunevich.finobserver.utils.Utils.uiHideView;
import static com.ashunevich.finobserver.utils.Utils.uiShowView;
import static com.ashunevich.finobserver.utils.Utils.uiUncheckChipGroup;


public class TransactionBoardFragment extends Fragment {
    private TransactionsFragmentBinding binding;

    private final List<TransactionBoardItem> listContentArr = new ArrayList<>();
    private ArrayList<TransactionBoardItem> filteredList;
    private  RecyclerViewAdapter adapter;

    private  RoomTransactionsViewModel modelDatabase;

    private Boolean FILTER_TYPE = true;

    public TransactionBoardFragment() {
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

        initRecView();
        initDelayForDatabaseRecView();

        setListenerTextWatcher ();
        setButtonListener();
        uiHandlerMechanism (false);
        setListenerCheckBox ();

        super.onViewCreated(view, savedInstanceState);
    }


    //Listeners
    private void setListenerTextWatcher(){
        binding.findByAccount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filterMechanism (editable.toString(),FILTER_TYPE);
            }
        });
    }

    @SuppressLint("NonConstantResourceId")
    private void setListenerCheckBox(){
        binding.checkBox.setOnCheckedChangeListener((compoundButton, b) -> uiHandlerMechanism (b));
        binding.monthChip.setOnCheckedChangeListener ((group, checkedId) -> {
            switch (returnActiveChipId(group)){
                case R.id.byDate : FILTER_TYPE = true;clearText();break;
                case R.id.byAccount : FILTER_TYPE = false;clearText();break;
            }
        });

    }

    private void setButtonListener(){
        binding.statisticButton.setOnClickListener (view -> startActivity(new Intent(getActivity (),TransactionStatisticActivity.class)));
    }
    //UI
    private void uiHandlerMechanism(boolean bool){
        if(bool){
            clearText ();
            uiUncheckChipGroup(binding.monthChip);
            binding.monthChip.check (binding.byDate.getId ());
            uiShowView (binding.monthChip);
            uiShowView (binding.findByAccount);
        }
        else {
            if(filteredList != null){
                filteredList.clear ();
                initFilter ();
            }
            uiUncheckChipGroup(binding.monthChip);
            uiHideView (binding.findByAccount);
            uiHideView (binding.monthChip);
        }

    }

    private void uiSetStartStatus(){
        uiHideHideShow (binding.progressBar,binding.loadingText,binding.transactionView);
    }

    //Recycler Utils
    private void initRecView(){
        binding.transactionView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RecyclerViewAdapter(listContentArr);
        binding.transactionView.setAdapter(adapter);
    }

    private void initDatabaseRecView() {
        modelDatabase = new ViewModelProvider(requireActivity()).get(RoomTransactionsViewModel.class);

        modelDatabase.getAllTransactions ().observe (requireActivity (), transaction_items -> {
            adapter.updateItemList (transaction_items);
            binding.transactionView.smoothScrollToPosition (0);
        });
        uiSetStartStatus ();
    }

    private void initDelayForDatabaseRecView(){
        initAfterDelay(this::initDatabaseRecView, 400);
    }

    private void initFilter(){
        modelDatabase.getAllTransactions ().observe (requireActivity (), transaction_items -> {
            adapter.filter (transaction_items);
            binding.transactionView.smoothScrollToPosition (0);
        });
    }

    //filter
    private void filterMechanism (String text, Boolean bool){
        filteredList = new ArrayList<>();
        for(TransactionBoardItem item:listContentArr){
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


    //Utils
    private void clearText(){
        binding.findByAccount.setText ("");
    }


    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }


}
