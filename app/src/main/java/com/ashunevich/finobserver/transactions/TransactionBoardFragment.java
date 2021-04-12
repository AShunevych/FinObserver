package com.ashunevich.finobserver.transactions;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
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

import static com.ashunevich.finobserver.UtilsPackage.Utils.returnActiveChipId;


public class TransactionBoardFragment extends Fragment {
    private TransactionsFragmentBinding binding;

    private final List<TransactionBoardItem> listContentArr = new ArrayList<>();
    private ArrayList<TransactionBoardItem> filteredList;
    RecyclerViewAdapter adapter;

    RoomTransactionsViewModel modelDatabase;

    Boolean FILTER_TYPE = true;

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
        //fix for  bug that freeze app, when app launch after was closed, when user switching to Transaction tab
        initDelayForDatabaseRecView();
        super.onViewCreated(view, savedInstanceState);

        setListenerTextWatcher ();
        uiHandlerMechanism (false);
        setListenerCheckBox ();
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

    //UI
    private void uiHandlerMechanism(boolean bool){
        if(bool){
            clearText ();
            binding.monthChip.clearCheck ();
            binding.monthChip.check (binding.byDate.getId ());
            binding.monthChip.setVisibility (View.VISIBLE);
            binding.findByAccount.setVisibility (View.VISIBLE);
        }
        else {
            if(filteredList != null){
                filteredList.clear ();
                rewriteFilterRecView();
            }
            binding.monthChip.clearCheck ();
            binding.findByAccount.setVisibility (View.GONE);
            binding.monthChip.setVisibility (View.GONE);
        }

    }

    private void uiProgressBarStatus(){
        binding.progressBar.setVisibility(View.GONE);
        binding.loadingText.setVisibility(View.GONE);
    }

    //Recycler Utils
    private void initDatabaseRecView() {
        binding.transactionView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RecyclerViewAdapter(listContentArr);
        binding.transactionView.setAdapter(adapter);
        modelDatabase = new ViewModelProvider(requireActivity()).get(RoomTransactionsViewModel.class);
        initViewModel ();
        uiProgressBarStatus ();
    }

    private void initDelayForDatabaseRecView(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::initDatabaseRecView, 1000);
    }

    private void initViewModel() {
        modelDatabase.getAllTransactions ().observe (requireActivity (), transaction_items -> {
            adapter.updateItemList (transaction_items);
            binding.transactionView.smoothScrollToPosition (0);
        });
    }

    private void rewriteFilterRecView(){
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
