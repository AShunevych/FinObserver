package com.ashunevich.finobserver;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashunevich.finobserver.adapters.TransactionBoardRecyclerViewAdapter;
import com.ashunevich.finobserver.data.TransactionBoardItem;
import com.ashunevich.finobserver.databinding.TransactionsFragmentBinding;
import com.ashunevich.finobserver.utility.TextWatcherUtils;
import com.ashunevich.finobserver.viewmodel.RoomTransactionsViewModel;

import java.util.ArrayList;
import java.util.List;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import static com.ashunevich.finobserver.utility.Utils.initAfterDelay;
import static com.ashunevich.finobserver.utility.ViewUtils.uiReturnActiveChipId;
import static com.ashunevich.finobserver.utility.ViewUtils.uiHideHideShow;
import static com.ashunevich.finobserver.utility.ViewUtils.uiHideView;
import static com.ashunevich.finobserver.utility.ViewUtils.uiShowView;
import static com.ashunevich.finobserver.utility.ViewUtils.uiUncheckChipGroup;
import static com.ashunevich.finobserver.utility.ViewUtils.uiViewSetText;


public class TransactionBoardFragment extends Fragment {
    private TransactionsFragmentBinding binding;

    private final List<TransactionBoardItem> listContentArr = new ArrayList<>();
    private ArrayList<TransactionBoardItem> filteredList;
    private TransactionBoardRecyclerViewAdapter adapter;

    private RoomTransactionsViewModel modelDatabase;

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
        new TextWatcherUtils ().
                setAfterTextChangedWatcher (binding.findByAccount).
                setCallback (editable ->  filterMechanism (editable.toString(),FILTER_TYPE));
    }

    @SuppressLint("NonConstantResourceId")
    private void setListenerCheckBox(){
        binding.checkBox.setOnCheckedChangeListener((compoundButton, b) -> uiHandlerMechanism (b));
        binding.monthChip.setOnCheckedChangeListener ((group, checkedId) -> {
            switch (uiReturnActiveChipId (group)){
                case R.id.byDate : FILTER_TYPE = true;clearText();break;
                case R.id.byAccount : FILTER_TYPE = false;clearText();break;
            }
        });

    }

    private void setButtonListener(){
        binding.statisticButton.setOnClickListener (view -> startActivity(new Intent(getActivity (), TransactionStatisticActivity.class)));
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
        adapter = new TransactionBoardRecyclerViewAdapter (listContentArr);
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
        uiViewSetText (binding.findByAccount,"");
    }


    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }


}
