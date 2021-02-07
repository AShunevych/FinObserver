package com.ashunevich.finobserver.DashboardAccountPackage;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;



import com.ashunevich.finobserver.TransactionsPackage.Transaction_Item;
import com.ashunevich.finobserver.TransactionsPackage.Transaction_AddTransaction;
import com.ashunevich.finobserver.TransactionsPackage.Transaction_ViewModel;

import com.ashunevich.finobserver.databinding.DashboardFragmentBinding;



import java.util.ArrayList;
import java.util.List;



import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class Dashboard_Fragment extends Fragment  {

    private DashboardFragmentBinding binding;
    DialogFragment newAccountDialogFragment;
  private final List<Dashboard_Account> AccountItemList = new ArrayList<>();
    ArrayList<Transaction_Item> listTransactions = new ArrayList<>();
    private Dashboard_ViewModel dashboardViewModel;

    Dashboard_RecyclerViewAdapter adapter;
     Double incomeValue;
     Double expValue;
     Double balanceValue;
    Handler handler = new Handler();
    Transaction_ViewModel model;
    ActivityResultLauncher<Intent> ResultLauncher;



    public Dashboard_Fragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
         ResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        assert data != null;
                        int idAccount = data.getIntExtra("ID",0);
                        String transactionAccount = data.getStringExtra("Account");
                        double accountBasicValue = data.getDoubleExtra("BasicValue",0);
                        int imagePos = data.getIntExtra("ImagePos",0);

                        String transactionType = data.getStringExtra("Type");
                        String transactionCategory = data.getStringExtra("Category");
                        double transactionValue = data.getDoubleExtra("Value", 0);

                        setResult (transactionType,transactionValue,idAccount,transactionAccount,accountBasicValue,imagePos);


                        Transaction_Item item = new Transaction_Item(Dashboard_FragmentUtils.getDate(),transactionAccount,"UAH",
                                String.valueOf(transactionValue),transactionCategory,
                                Dashboard_FragmentUtils.getTypeImage(transactionType,requireContext()),listTransactions.size()+1);
                        listTransactions.add(0,item);
                        model.setSelected(listTransactions);
                    }
                });
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
            newAccountDialogFragment = new Dashboard_DialogCreateAccount();
            newAccountDialogFragment.show(requireActivity().getSupportFragmentManager(), "newAccountDialogFragment");
        });

        binding.buttonDelete.setOnClickListener(view -> dashboardViewModel.deleteAll());

        binding.newTransactionDialog.setOnClickListener(view -> newTransaction());
            binding.incomeView.setText(String.valueOf(0.0));
            binding.expendView.setText(String.valueOf(0.0));
            binding.balanceView.setText(String.valueOf(0.0));


        handler.post(updateLog);

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(requireActivity()).get(Transaction_ViewModel.class);
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(Dashboard_ViewModel.class);
        setRecyclerView();


        getParentFragmentManager().setFragmentResultListener("fragmentKey", getViewLifecycleOwner(), (requestKey, result) -> {
               String name = result.getString("nameResult");
               double value = result.getDouble("doubleResult");
               String currency = result.getString("currencyResult");
               int imageID = result.getInt("idResult");

               dashboardViewModel.insert(new Dashboard_Account(name,value,currency,imageID));
        });

        getParentFragmentManager().setFragmentResultListener("updateKey", getViewLifecycleOwner(), (requestKey, result) -> {
            int id = result.getInt("id");
            String name = result.getString("updatedName");
            double value = result.getDouble("updatedValue");
            String currency = result.getString("updatedCurrency");
            int imageID = result.getInt("updatedDrawable");
            updateResult(id,name,value,currency,imageID);
        });

        super.onViewCreated(view, savedInstanceState);
    }


    private void setRecyclerView(){
        binding.accountView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new Dashboard_RecyclerViewAdapter(AccountItemList,getParentFragmentManager());
        dashboardViewModel.getmAllAccounts().observe(requireActivity(), accounts -> adapter.updateList(accounts));
        binding.accountView.setAdapter(adapter);

        setupItemTouchHelper();
    }


    private void newTransaction(){
        Intent intent = new Intent(getContext(), Transaction_AddTransaction.class);

        //int updatedID, String updatedName, double updatedValue, String updatedCurrency, int updatedImagePos
        ArrayList<String> idLists = new ArrayList<>();
        ArrayList<String> namesLists = new ArrayList<>();
        ArrayList<String> valuesLists = new ArrayList<>();
        ArrayList<String> imagePos = new ArrayList<>();
           for (int i=0;i <binding.accountView.getChildCount();i++) {
               Dashboard_Account account = adapter.getAccountAtPosition(i);
               namesLists.add(account.getAccountName());
               idLists.add(String.valueOf(account.getAccountID()));
               valuesLists.add(String.valueOf(account.getAccountValue()));
               imagePos.add(String.valueOf(account.getImageID()));
           }
        if (namesLists.size() !=0 && idLists.size() !=0 && valuesLists.size() != 0) {
            intent.putStringArrayListExtra("AccountNames", namesLists);
            intent.putStringArrayListExtra("AccountIDs", idLists);
            intent.putStringArrayListExtra("AccountValues", valuesLists);
            intent.putStringArrayListExtra("AccountImages", imagePos);
            ResultLauncher.launch(intent);
        } else {
            Toast.makeText(requireContext(),"There is no active accounts.Please create",Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDetach() {
        handler.removeCallbacksAndMessages(null);
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }

    private void setupItemTouchHelper() {
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                Dashboard_Account account = adapter.getAccountAtPosition(position);
                dashboardViewModel.deleteAccount(account);
            }
        });
        helper.attachToRecyclerView(binding.accountView);
    }



    //Utils method

    private void updateResult(int updatedID, String updatedName, double updatedValue, String updatedCurrency, int updatedImagePos){
        dashboardViewModel.updateAccount(new Dashboard_Account(updatedID,updatedName,updatedValue,updatedCurrency,updatedImagePos));
    }


    private void setResult(String type,double result, int id, String name, double basicValue, int imagePos){
        incomeValue = Dashboard_FragmentUtils.stringToDouble(binding.incomeView);
        expValue = Dashboard_FragmentUtils.stringToDouble(binding.expendView);
        balanceValue = Dashboard_FragmentUtils.stringToDouble(binding.balanceView);
        double positiveValue = basicValue+result;
        double negativeValue = basicValue-result;

        if (type.matches("Income")) {
            updateResult(id,name,positiveValue,"UAH",imagePos);
            binding.incomeView.setText(String.valueOf(result + incomeValue));
            binding.balanceView.setText(String.valueOf(balanceValue + result));
        } else {
            updateResult(id,name,negativeValue,"UAH",imagePos);
            binding.expendView.setText(String.valueOf(result + expValue));
            binding.balanceView.setText(String.valueOf(balanceValue - result));
        }

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





    //  TODO (1) Implement account mechanism :
    //  DONE (1.1) Add/Remove account --> RecyclerView, DialogFragment, Implement EventBus
    //  DONE (1.2) Count all accounts balance
    //      DONE (1.2.1) Count accounts balance when account removed
    //  DONE  (1.3) Permanent account holder with ROOM
    //      DONE  (1.3.1) Create Room persistance and insert data in it
    //      DONE  (1.3.2) remove all data and specific item from room and Recyclerview
    //      DONE  (1.3.3) update  data in the room
    //  DONE (1.4) LiveData to TransactionFragment
    //  DONE (1.5) Make LiveData observe pernament
    //  DONE  (1.6) Update item when accountValue change
    //  TODO (1.7) Save in SharedPreferences all TextView;



}
