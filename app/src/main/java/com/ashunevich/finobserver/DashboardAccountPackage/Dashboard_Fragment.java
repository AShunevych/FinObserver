package com.ashunevich.finobserver.DashboardAccountPackage;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.ashunevich.finobserver.AccountPackage.Account_Item;
import com.ashunevich.finobserver.AccountPackage.Account_NewItemDialog;
import com.ashunevich.finobserver.AccountPackage.Account_NewtItem;
import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.TransactionsPackage.Transaction_Item;
import com.ashunevich.finobserver.TransactionsPackage.Transaction_AddTransaction;
import com.ashunevich.finobserver.TransactionsPackage.Transaction_ViewModel;

import com.ashunevich.finobserver.databinding.DashboardFragmentBinding;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static android.app.Activity.RESULT_CANCELED;


public class Dashboard_Fragment extends Fragment {
    EventBus bus;
    private DashboardFragmentBinding binding;
    DialogFragment newAccountDialogFragment;
    private final ArrayList<Account_Item> AccountItemList = new ArrayList<>();
    ArrayList<Transaction_Item> listTransactions = new ArrayList<>();
    Dashboard_RecyclevrViewAdapter adapter;
     Double incomeValue;
     Double expValue;
     Double balanceValue;
    Handler handler = new Handler();
    Transaction_ViewModel model;



    public Dashboard_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (!EventBus.getDefault().isRegistered(this))
            EventBus.getDefault().register(this);
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
            newAccountDialogFragment = new Account_NewItemDialog();
            newAccountDialogFragment.show(Objects.requireNonNull(getActivity()).getSupportFragmentManager(), "newAccountDialogFragment");
        });

        binding.newTransactionDialog.setOnClickListener(view -> newTransaction());
            binding.incomeView.setText(String.valueOf(0.0));
            binding.expendView.setText(String.valueOf(0.0));
            binding.balanceView.setText(String.valueOf(0.0));

        bus = EventBus.getDefault();
        handler.post(updateLog);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(requireActivity()).get(Transaction_ViewModel.class);
        setRecyclerView();
     //   testItem();
        super.onViewCreated(view, savedInstanceState);
    }

    private void setRecyclerView(){
        binding.accountView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new Dashboard_RecyclevrViewAdapter(AccountItemList);
        adapter.setListContent(AccountItemList);
        binding.accountView.setAdapter(adapter);

        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new Dashboard_RecyclerViewSwipeAdapter(adapter));
        itemTouchHelper.attachToRecyclerView(binding.accountView);
    }



    private void newTransaction(){
        Intent intent = new Intent(getContext(), Transaction_AddTransaction.class);
        ArrayList<String> arrayList = new ArrayList<>();
           for (int i=0;i <binding.accountView.getChildCount();i++) {
               RecyclerView.ViewHolder holder = binding.accountView.getChildViewHolder(binding.accountView.getChildAt(i));
               TextView view = holder.itemView.findViewById(R.id.accountType);
               arrayList.add(view.getText().toString());
               if (arrayList.size() != 0) {
                   intent.putStringArrayListExtra("AccountTypes", arrayList);
               } else {
                   Log.d("List is ", String.valueOf(arrayList.size()));
               }
           }
                startActivityForResult(intent,1000);
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
     EventBus.getDefault().unregister(this);
        handler.removeCallbacksAndMessages(null);
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        handler.removeCallbacksAndMessages(null);
        binding = null;
        super.onDestroyView();
    }


    //TEST
    //DELETE LATER
    private void testItem(){
        Account_Item newAccountItem = new Account_Item() ;
        newAccountItem.setImage(ContextCompat.getDrawable(requireContext(),R.drawable.ic_bank_balance));
        newAccountItem.setAccountType("PrivatBank");
        newAccountItem.setAccountValue(250.00);
        newAccountItem.setAccountCurrency("UAH");
        AccountItemList.add(newAccountItem);
        adapter.notifyItemChanged(adapter.getItemCount());
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setRecyclerViewItem(Account_NewtItem receivedItem)  {
        Account_Item newAccountItem = new Account_Item() ;
        newAccountItem.setImage(receivedItem.getImage());
        newAccountItem.setAccountType(receivedItem.getAccountType());
        newAccountItem.setAccountValue(receivedItem.getAccountValue());
        newAccountItem.setAccountCurrency(receivedItem.getAccountCurrency());
        AccountItemList.add(newAccountItem);
        adapter.notifyItemChanged(adapter.getItemCount());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(resultCode != RESULT_CANCELED) {
            assert data != null;
            String transactionType = data.getStringExtra("Type");
            String transactionAccount = data.getStringExtra("Account");
            String transactionCategory = data.getStringExtra("Category");
            Double transactionValue = data.getDoubleExtra("Value", 0);

            setResult (transactionType,transactionValue);


            Transaction_Item item = new Transaction_Item(getDate(),transactionAccount,"UAH",
                    String.valueOf(transactionValue),transactionCategory,getTypeImage(transactionType),listTransactions.size()+1);
            listTransactions.add(0,item);
            model.setSelected(listTransactions);


        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    //Utils method

    private Drawable getTypeImage(String type){
        Drawable drawable;
        if(type.matches("Income")){
            drawable = ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.ic_arrow_drop_up);
        }
        else {
            drawable = ContextCompat.getDrawable(Objects.requireNonNull(getContext()),R.drawable.ic_arrow_drop_down);
        }
        return drawable;
    }

    private void setResult(String type,Double result){
        incomeValue = stringToDouble(binding.incomeView);
        expValue = stringToDouble(binding.expendView);
        balanceValue = stringToDouble(binding.balanceView);

        if (type.matches("Income")) {
            binding.incomeView.setText(String.valueOf(result + incomeValue));
            binding.balanceView.setText(String.valueOf(balanceValue + result));
        } else {
            binding.expendView.setText(String.valueOf(result + expValue));
            binding.balanceView.setText(String.valueOf(balanceValue - result));
        }

    }

    private static String getDate(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy", Locale.getDefault());
        return df.format(c);
    }

    private Double stringToDouble(TextView view){
        return Double.parseDouble(view.getText().toString());
    }

    private final Runnable updateLog = new Runnable() {
        public void run() {
            try {
                binding.balanceValue.setText(adapter.summAllItemsValue(binding.accountView));
                handler.postDelayed(this, 1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };




    // TODO (1) Implement account mechanism :
    // DONE  (1.1) Add/Remove account --> RecyclerView, DialogFragment, Implement EventBus
    // DONE (1.2) Count all accounts balance
    // DONE (1.2.1) Count accounts balance when account removed
    // TODO  (1.3) Permanent account holder
    // DONE  (1.4) LiveData to TransactionFragment
    //DONE  (1.5) Make LiveData observe pernament



}
