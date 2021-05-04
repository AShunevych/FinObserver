package com.ashunevich.finobserver.dashboard;



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


import com.ashunevich.finobserver.transactions.RoomTransactionsViewModel;
import com.ashunevich.finobserver.transactions.TransactionBoardItem;

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

import static com.ashunevich.finobserver.dashboard.DashboardUtils.BALANCE;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.EXPENDITURES;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.INCOME;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.KEY_CREATE;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.DIALOG_STATIC;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.KEY_UPDATE;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.PREFERENCE_NAME;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.TOTAL;

import static com.ashunevich.finobserver.dashboard.DashboardUtils.setTransferText;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.stringDate;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.intFromImageType;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.stringExtractionFromDoubles;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.stringFormat;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.stringFromTextView;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.stringFromObject;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.stringSumFromDoubles;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.doubleFromTextView;


public class DashboardFragment extends Fragment {

    private DashboardFragmentBinding binding;
    private DashboardUtilsSharedPref prefManager;
    ActivityResultLauncher<Intent> ResultLauncher;
    private DialogFragment newAccountDialogFragment;

    private final List<AccountItem> AccountItemList = new ArrayList<>();
    private RecyclerViewAdapter adapter;

    private RoomDashboardVewModel dashboardViewModel;
    RoomTransactionsViewModel transactionsViewModel;


    double incomeValue,expValue,balanceValue;

    String date = stringDate ();
    String currencyAccount = "UAH";

    //late init
    int accountID,accountImageType;
    String accountName,transactionType, accountTransactionCategory;
    double  accountTransactionEstimate, accountValue;


    //Lifecycle
    public DashboardFragment() {
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
                            resultOnStandardOperations (data,transactionType);
                        }
                        else {
                            resultOnTransferOperation (data,transactionType);
                        }
                        uiUpdateWithDelay ();
                    }
                });
        if (!EventBus.getDefault().isRegistered(this)) { EventBus.getDefault().register(this); }

        if(adapter != null){
            uiUpdateWithDelay ();
        }
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

        initClickListeners();
        initPrefManager();

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initViewModels();

        initRecView ();
        initDialogFragmentListener ();

        super.onViewCreated(view, savedInstanceState);
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


    //init methods
    private void initDialogFragmentListener(){

        getParentFragmentManager().setFragmentResultListener(DIALOG_STATIC, getViewLifecycleOwner(), (requestKey, result) -> {
            String operationType = result.getString("operationType");
            Log.d("OPERATION KEY", operationType);
            String name = result.getString("accountName");
            double value = result.getDouble("accountValue");
            String currency = result.getString("accountCurrency");
            int drawablePos = result.getInt("accountDrawablePos");
            if(operationType.matches(KEY_UPDATE)){
                int id = result.getInt("accountID");
                roomUpdateAccount (id,name,value,currency,drawablePos);
            }
            else if (operationType.matches(KEY_CREATE)){
                roomInsertAccount (name,value,currency,drawablePos);
            }
            else{
                Toast.makeText(requireContext(),"Operation Canceled",Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void initRecView(){
        binding.accountView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new RecyclerViewAdapter(AccountItemList,getParentFragmentManager());
        dashboardViewModel.getAllAccounts().observe(requireActivity(), accounts -> adapter.updateList(accounts));
        binding.accountView.setAdapter(adapter);
        initRecViewTouchHelper ();
    }

    private void initRecViewTouchHelper() {
        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
               roomDeleteAccount (viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(binding.accountView);
    }

    private void initViewModels(){
        transactionsViewModel = new ViewModelProvider(requireActivity()).get(RoomTransactionsViewModel.class);
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(RoomDashboardVewModel.class);
    }

    private void initClickListeners(){
        binding.newAccount.setOnClickListener(view -> {
            newAccountDialogFragment = new AccountDialog();
            Bundle bundle = new Bundle();
            bundle.putString("operationKey",KEY_CREATE);
            newAccountDialogFragment.setArguments(bundle);
            newAccountDialogFragment.show(requireActivity().getSupportFragmentManager(), "createDialog");
        });

        binding.newTransactionDialog.setOnClickListener(view -> startNewTransaction ());
    }

    private void initPrefManager(){
        prefManager = new DashboardUtilsSharedPref(requireActivity(), PREFERENCE_NAME);
        uiSetSharedPrefValues ();
    }
    //
    private void startNewTransaction(){
        Intent intent = new Intent(getContext(), DashboardNewTransaction.class);

        //int updatedID, String updatedName, double updatedValue, String updatedCurrency
        ArrayList<String> idLists = new ArrayList<>();
        ArrayList<String> namesLists = new ArrayList<>();
        ArrayList<String> valuesLists = new ArrayList<>();
        if (binding.accountView.getChildCount() !=0) {
            for (int i=0;i <binding.accountView.getChildCount();i++) {
                AccountItem account = adapter.getAccountAtPosition(i);
                namesLists.add(account.getAccountName());
                idLists.add(String.valueOf(account.getAccountID()));
                valuesLists.add(String.valueOf(account.getAccountValue()));
            }
            intent.putStringArrayListExtra("AccountNames", namesLists);
            intent.putStringArrayListExtra("AccountIDs", idLists);
            intent.putStringArrayListExtra("AccountValues", valuesLists);
            ResultLauncher.launch(intent);
        } else {
            Toast.makeText(requireContext(),"There are no active accounts.",Toast.LENGTH_SHORT).show();
        }
    }

    //Room operations
    private void roomUpdateAccount(int accountId, String accountName, double accountValue,
                                   String accountCurrency, int accountDrawablePos){
        dashboardViewModel.update(new AccountItem(accountId,accountName,accountValue,accountCurrency,accountDrawablePos));
        uiUpdateWithDelay ();
    }

    private void roomUpdateAccountAfterTransaction(int accountID, double accountValue){
        dashboardViewModel.updateAccountAfterTransaction(accountID,accountValue);
        uiUpdateWithDelay ();
    }

    private void roomInsertAccount(String accountName, double accountValue, String accountCurrency, int accountDrawablePos ){
        dashboardViewModel.insert(new AccountItem(accountName,accountValue,accountCurrency,accountDrawablePos));
        uiUpdateWithDelay ();
    }

    private void roomDeleteAccount(int pos){
        dashboardViewModel.delete(adapter.getAccountAtPosition(pos));
        uiUpdateWithDelay ();
    }

    //UI
    private void uiUpdateVisibleSum(){
        binding.totalBalanceValue.setText(adapter.getSumOfAllItems(binding.accountView));
        binding.accountView.smoothScrollToPosition(0);
        uiUpdateSharedPref ();
    }

    private void uiUpdateWithDelay(){
        final Handler handler = new Handler(Looper.getMainLooper());
        handler.postDelayed(this::uiUpdateVisibleSum, 500);
    }

    private void uiSetSharedPrefValues(){
        binding.balanceView.setText(stringFormat(prefManager.getValue(BALANCE,"0.0")));
        binding.incomeView.setText(stringFormat(prefManager.getValue(INCOME,"0.0")));
        binding.expendView.setText(stringFormat(prefManager.getValue(EXPENDITURES,"0.0")));
        binding.totalBalanceValue.setText(stringFormat(prefManager.getValue(TOTAL,"0.0")));
    }

    private void uiUpdateSharedPref(){
        prefManager.setValue(BALANCE, stringFromTextView (binding.balanceView));
        prefManager.setValue(INCOME, stringFromTextView (binding.incomeView));
        prefManager.setValue(EXPENDITURES, stringFromTextView (binding.expendView));
        prefManager.setValue(TOTAL, stringFromTextView (binding.totalBalanceValue));
    }

    //EventBus event
    @Subscribe
    public void eventReceiver(PostPOJO postPOJO){
        String zeroString = stringFromObject (postPOJO);
        binding.balanceView.setText(zeroString);
        binding.incomeView.setText(zeroString);
        binding.expendView.setText(zeroString);
        binding.totalBalanceValue.setText(zeroString);
        uiUpdateSharedPref ();
    }

    //Result handlers
    private void resultOnStandardOperations(Intent intent, String transactionType){
        accountID = intent.getIntExtra("ID",0);
        accountName = intent.getStringExtra("Account");
        accountTransactionEstimate = intent.getDoubleExtra("Estimate", 0);
        accountTransactionCategory = intent.getStringExtra("Category");
        accountValue = intent.getDoubleExtra("Value", 0);
        accountImageType = intFromImageType (transactionType);

        incomeValue = doubleFromTextView (binding.incomeView);
        expValue = doubleFromTextView (binding.expendView);
        balanceValue = doubleFromTextView (binding.balanceView);

        if (transactionType.matches("Income")) {
            binding.incomeView.setText(stringSumFromDoubles (accountTransactionEstimate,incomeValue));
            binding.balanceView.setText(stringSumFromDoubles (accountTransactionEstimate,balanceValue));
        } else {
            binding.expendView.setText(stringSumFromDoubles (accountTransactionEstimate,expValue));
            binding.balanceView.setText(stringExtractionFromDoubles (accountTransactionEstimate,balanceValue));
        }

        roomUpdateAccountAfterTransaction(accountID,accountValue);

        transactionsViewModel.insert(new TransactionBoardItem(accountName,accountTransactionCategory,
                accountTransactionEstimate,currencyAccount,date,accountImageType));

    }

    private void resultOnTransferOperation(Intent intent, String transactionType){

        int basicAccountID = intent.getIntExtra("basicAccountID",0);
        int targetAccountID = intent.getIntExtra("targetAccountID",0);

        String basicAccountName  = intent.getStringExtra("basicAccountName");
        String targetAccountName  = intent.getStringExtra("targetAccountName");

        double transferValue = intent.getDoubleExtra("transferValue",0);
        double newBasicAccountValue = intent.getDoubleExtra("newBasicAccountValue", 0);
        double newTargetAccountValue = intent.getDoubleExtra("newTargetAccountValue", 0);

        int imageType = intFromImageType (transactionType);

       roomUpdateAccountAfterTransaction (basicAccountID,newBasicAccountValue);
       roomUpdateAccountAfterTransaction (targetAccountID,newTargetAccountValue);

        transactionsViewModel.insert(new TransactionBoardItem(targetAccountName,setTransferText(basicAccountName),
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





