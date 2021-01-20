package com.ashunevich.finobserver.DashboardAccountPackage;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import com.ashunevich.finobserver.TransactionsPackage.TransactionNew;
import com.ashunevich.finobserver.databinding.DashboardFragmentBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;


public class DashboardFragment extends Fragment  {
    EventBus bus;
    private DashboardFragmentBinding binding;
    DialogFragment newAccountDialogFragment;
    private final ArrayList<AccountItem> listContentArr = new ArrayList<>();
    AccountItem newItem = new AccountItem() ;
    AccountRecyclerViewAdapter adapter;


    public DashboardFragment() {
        // Required empty public constructor
    }


    @Override
    public void onStart(){
        if (!EventBus.getDefault().isRegistered(this)) { EventBus.getDefault().register(this); }
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        binding = DashboardFragmentBinding.inflate(inflater, container, false);
        binding.newAccount.setOnClickListener(view -> {
            assert getFragmentManager() != null;
            newAccountDialogFragment = new AccountNewDialogFragment();
            newAccountDialogFragment.show(getFragmentManager(), "newAccountDialogFragment");
        });
        binding.newTransactionDialog.setOnClickListener
                (v ->
                        startActivity(new Intent(getActivity(), TransactionNew.class)));
        binding.accountView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new AccountRecyclerViewAdapter(listContentArr);
        adapter.setListContent(listContentArr);
        binding.accountView.setAdapter(adapter);
        bus = EventBus.getDefault();
        return binding.getRoot();
    }


    @Override
    public void onDetach() {
        if (EventBus.getDefault().isRegistered(this)) { EventBus.getDefault().unregister(this); }
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setRecyclerView(AccountNewtItem itemEvent){
        newItem.setImage(itemEvent.getImage());
        newItem.setAccountType(itemEvent.getAccountType());
        newItem.setAccountValue(itemEvent.getAccountValue());
        newItem.setAccountCurrency(itemEvent.getAccountCurrency());
        listContentArr.add(newItem);
        adapter.notifyItemInserted(adapter.getItemCount());
    }

    // POSSIBILITIES (1.2) Count all accounts balance
    // POSSIBILITIES (1.2.1) Count accounts balance when account removed
    /*
    private void sumBalance(Double Value){
        Double balance = Double.parseDouble(binding.balanceSum.getText().toString())+Value;
        binding.balanceSum.setText(String.valueOf(balance));
    }


    @Override
    public void passData(Double value) {
        binding.balanceSum.setText(String.valueOf(value));
    }

     */

    // TODO (1) Implement account mechanism :
    // DONE  (1.1) Add/Remove account --> RecyclerView, DialogFragment, Implement EventBus
    // TODO  (1.3) Permanent account holder



}
