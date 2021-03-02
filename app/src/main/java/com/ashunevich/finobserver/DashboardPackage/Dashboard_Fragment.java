package com.ashunevich.finobserver.DashboardPackage;



import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;


import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Toast;


import com.ashunevich.finobserver.TransactionsPackage.RoomTransactions_ViewModel;
import com.ashunevich.finobserver.TransactionsPackage.Transaction_Item;
import com.ashunevich.finobserver.TransactionsPackage.Transaction_CreateTransaction;

import com.ashunevich.finobserver.UtilsPackage.PostPOJO;
import com.ashunevich.finobserver.databinding.DashboardFragmentBinding;


import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

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

import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.BALANCE;
import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.EXPENDITURES;
import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.INCOME;
import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.KEY_CREATE;
import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.KEY_DIALOG;
import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.KEY_UPDATE;


import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.getDate;
import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.getImageInt;
import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.returnString;
import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.returnStringFromObj;
import static com.ashunevich.finobserver.DashboardPackage.Utils_Dashboard.textToDouble;


public class Dashboard_Fragment extends Fragment {

    private DashboardFragmentBinding binding;
    DialogFragment newAccountDialogFragment;
    private final List<Dashboard_Account> AccountItemList = new ArrayList<>();

    private RoomDashboard_VewModel dashboardViewModel;

    RecyclerView_Adapter adapter;
    double incomeValue,expValue,balanceValue;
    RoomTransactions_ViewModel model;
    ActivityResultLauncher<Intent> ResultLauncher;
    private Dashboard_SharedPrefManager prefManager;
    String date = getDate();


    int idAccount,imagePos,imageType;
    String transactionAccount,transactionType,transactionCategory;
    double  accountBasicValue, transactionValue;
    String currencyAccount = "UAH";

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
                        transactionType = data.getStringExtra("Type");
                        if(transactionType.matches("Income") || transactionType.matches("Expenditures") ){
                            onTransactionIncomeExp(data,transactionType);
                        }
                        else {
                            onTransferTransaction(data,transactionType);
                        }
                        countSumAfterDelay();
                    }
                });
        if (!EventBus.getDefault().isRegistered(this)) { EventBus.getDefault().register(this); }
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
            newAccountDialogFragment = new Dashboard_Account_Dialog();
            Bundle bundle = new Bundle();
            bundle.putString("operationKey",KEY_CREATE);
            newAccountDialogFragment.setArguments(bundle);
            newAccountDialogFragment.show(requireActivity().getSupportFragmentManager(), "createDialog");
        });

        prefManager = new Dashboard_SharedPrefManager(requireActivity(), Utils_Dashboard.PREFERENCE_NAME);

        binding.newTransactionDialog.setOnClickListener(view -> newTransaction());
        getSharedPrefValues();

        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        model = new ViewModelProvider(requireActivity()).get(RoomTransactions_ViewModel.class);
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(RoomDashboard_VewModel.class);
        setRecyclerView();

        setupFragmentResultListener();
         countSumAfterDelay();


        super.onViewCreated(view, savedInstanceState);
    }

    private void setupFragmentResultListener(){

        getParentFragmentManager().setFragmentResultListener(KEY_DIALOG, getViewLifecycleOwner(), (requestKey, result) -> {
            String operationType = result.getString("operationType");
            Log.d("OPERATION KEY", operationType);
            String name = result.getString("accountName");
            double value = result.getDouble("accountValue");
            String currency = result.getString("accountCurrency");
            int drawablePos = result.getInt("accountDrawablePos");
            if(operationType.matches(KEY_UPDATE)){
                int id = result.getInt("accountID");
                updateAccount(id,name,value,currency,drawablePos);
            }
            else{
                insertAccount(name,value,currency,drawablePos);
            }

        });
    }



    private void setRecyclerView(){
        binding.accountView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RecyclerView_Adapter(AccountItemList,getParentFragmentManager());
        dashboardViewModel.getAllAccounts().observe(requireActivity(), accounts -> adapter.updateList(accounts));
        binding.accountView.setAdapter(adapter);
        setupItemTouchHelper();
    }


    private void newTransaction(){
        Intent intent = new Intent(getContext(), Transaction_CreateTransaction.class);

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
        if (binding.accountView.getChildCount() !=0) {
            intent.putStringArrayListExtra("AccountNames", namesLists);
            intent.putStringArrayListExtra("AccountIDs", idLists);
            intent.putStringArrayListExtra("AccountValues", valuesLists);
            intent.putStringArrayListExtra("AccountImages", imagePos);
            ResultLauncher.launch(intent);
        } else {
            Toast.makeText(requireContext(),"There is no active accounts.",Toast.LENGTH_SHORT).show();
        }
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

    private void setupItemTouchHelper() {
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
               deleteAccount(viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(binding.accountView);
    }


    private void updateAccount(int accountId, String accountName, double accountValue, String accountCurrency, int accountDrawablePos){
        dashboardViewModel.update(new Dashboard_Account(accountId,accountName,accountValue,accountCurrency,accountDrawablePos));
        countSumAfterDelay();
    }

    private void insertAccount(String accountName,double accountValue, String accountCurrency,int accountDrawablePos ){
        dashboardViewModel.insert(new Dashboard_Account(accountName,accountValue,accountCurrency,accountDrawablePos));
        countSumAfterDelay();
    }

    private void deleteAccount(int pos){
        dashboardViewModel.delete(adapter.getAccountAtPosition(pos));
        countSumAfterDelay();
    }


    private void onIncExpTransactionResult(String type, double result, int id, String name, double basicValue, int imagePos){
        incomeValue = textToDouble(binding.incomeView);
        expValue = textToDouble(binding.expendView);
        balanceValue = textToDouble(binding.balanceView);
        double positiveValue = basicValue+result;
        double negativeValue = basicValue-result;

        if (type.matches("Income")) {
            updateAccount(id,name,positiveValue,currencyAccount,imagePos);
            binding.incomeView.setText(String.valueOf(result + incomeValue));
            binding.balanceView.setText(String.valueOf(balanceValue + result));
        } else {
            updateAccount(id,name,negativeValue,currencyAccount,imagePos);
            binding.expendView.setText(String.valueOf(result + expValue));
            binding.balanceView.setText(String.valueOf(balanceValue - result));
        }

        setSharedPrefValues();
    }

    private void countSum(){
        binding.totalBalanceValue.setText(adapter.summAllItemsValue(binding.accountView));
    }

    private void countSumAfterDelay(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::countSum, 500);
    }


    @Subscribe
    public void receiveEvent(PostPOJO postPOJO){
        String zeroString = returnStringFromObj(postPOJO);
        binding.balanceView.setText(zeroString);
        binding.incomeView.setText(zeroString);
        binding.expendView.setText(zeroString);
        binding.totalBalanceValue.setText(zeroString);
        setSharedPrefValues();
    }

    private void setSharedPrefValues(){
        prefManager.setValue(BALANCE, returnString(binding.balanceView));
        prefManager.setValue(BALANCE, returnString(binding.balanceView));
        prefManager.setValue(EXPENDITURES,returnString(binding.expendView));
    }

    private void getSharedPrefValues(){
        binding.balanceView.setText(prefManager.getValue(BALANCE,"0.0"));
        binding.incomeView.setText(prefManager.getValue(INCOME,"0.0"));
        binding.expendView.setText(prefManager.getValue(EXPENDITURES,"0.0"));
    }

    private void onTransactionIncomeExp(Intent intent,String transactionType){
        idAccount = intent.getIntExtra("ID",0);
        transactionAccount = intent.getStringExtra("Account");
        accountBasicValue = intent.getDoubleExtra("BasicValue",0);
        imagePos = intent.getIntExtra("ImagePos",0);
        transactionCategory = intent.getStringExtra("Category");
        transactionValue = intent.getDoubleExtra("Value", 0);
        imageType = getImageInt(transactionType);

        onIncExpTransactionResult(transactionType,transactionValue,idAccount,transactionAccount,accountBasicValue,imagePos);

        Transaction_Item item = new Transaction_Item(transactionAccount,transactionCategory,
                transactionValue,currencyAccount,date,imageType);
        model.insertTransAction(item);
    }

    private void onTransferTransaction(Intent intent,String transactionType){
        //String type,double result, int id, String name, double basicValue, int imagePos
      //updateResult(id,name,positiveValue,"UAH",imagePos);

        int basicAccountID = intent.getIntExtra("basicAccountID",0);
        int targetAccountID = intent.getIntExtra("targetAccountID",0);

        int basicAccountImagePos = intent.getIntExtra("basicAccountImagePos",0);
        int targetAccountImagePos = intent.getIntExtra("targetAccountImagePos",0);

        String basicAccountName  = intent.getStringExtra("basicAccountName");
        String targetAccountName  = intent.getStringExtra("targetAccountName");

        String transactionCategory = "Transfer from " + basicAccountName;

        double transferValue = intent.getDoubleExtra("transferValue",0);
        double newBasicAccountValue = intent.getDoubleExtra("newBasicAccountValue", 0);
        double newTargetAccountValue = intent.getDoubleExtra("newTargetAccountValue", 0);


        int imageType = getImageInt(transactionType);

        updateAccount(basicAccountID,basicAccountName,newBasicAccountValue,currencyAccount,basicAccountImagePos);
        updateAccount(targetAccountID,targetAccountName,newTargetAccountValue,currencyAccount,targetAccountImagePos);

        model.insertTransAction(new Transaction_Item(targetAccountName,transactionCategory,
                transferValue,currencyAccount,date,imageType));
    }

    }







/*
    //   (1) Implement account mechanism :
    //   (1.1) Add/Remove account --> RecyclerView, DialogFragment, Implement EventBus
    //   (1.2) Count all accounts balance
    //       (1.2.1) Count accounts balance when account removed
    //    (1.3) Permanent account holder with ROOM
    //        (1.3.1) Create Room persistance and insert data in it
    //        (1.3.2) remove all data and specific item from room and Recyclerview
    //        (1.3.3) update  data in the room
    //   (1.4) LiveData to TransactionFragment
    //   (1.5) Make LiveData observe pernament
    //    (1.6) Update item when accountValue change
    //   (1.7) Save in SharedPreferences all TextView;

 */





