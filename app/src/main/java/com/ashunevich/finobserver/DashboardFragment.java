package com.ashunevich.finobserver;



import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import com.ashunevich.finobserver.data.DashboardAccountItem;
import com.ashunevich.finobserver.data.DashboardSharedPrefManager;
import com.ashunevich.finobserver.adapters.DashboardRecyclerViewAdapater;
import com.ashunevich.finobserver.utility.ConstantsUtils;
import com.ashunevich.finobserver.viewmodel.RoomDashboardViewModel;
import com.ashunevich.finobserver.viewmodel.RoomTransactionsViewModel;
import com.ashunevich.finobserver.data.TransactionBoardItem;

import com.ashunevich.finobserver.utility.PostPOJO;
import com.ashunevich.finobserver.databinding.DashboardFragmentBinding;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.app.AlertDialog;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import static com.ashunevich.finobserver.utility.ConstantsUtils.BALANCE;
import static com.ashunevich.finobserver.utility.ConstantsUtils.DIALOG_STATIC;
import static com.ashunevich.finobserver.utility.ConstantsUtils.EXPENDITURES;
import static com.ashunevich.finobserver.utility.ConstantsUtils.INCOME;
import static com.ashunevich.finobserver.utility.ConstantsUtils.KEY_CREATE;
import static com.ashunevich.finobserver.utility.ConstantsUtils.KEY_UPDATE;
import static com.ashunevich.finobserver.utility.ConstantsUtils.TOTAL;
import static com.ashunevich.finobserver.utility.Utils.setTransferText;
import static com.ashunevich.finobserver.utility.Utils.stringDate;
import static com.ashunevich.finobserver.utility.Utils.intFromImageType;
import static com.ashunevich.finobserver.utility.Utils.stringExtractionFromDoubles;
import static com.ashunevich.finobserver.utility.Utils.stringFormat;
import static com.ashunevich.finobserver.utility.Utils.stringFromTextView;
import static com.ashunevich.finobserver.utility.Utils.stringFromObject;
import static com.ashunevich.finobserver.utility.Utils.stringSumFromDoubles;
import static com.ashunevich.finobserver.utility.Utils.doubleFromTextView;
import static com.ashunevich.finobserver.utility.Utils.genericDialogOptions;
import static com.ashunevich.finobserver.utility.Utils.initAfterDelay;
import static com.ashunevich.finobserver.utility.ViewUtils.uiShowSnackBar;
import static com.ashunevich.finobserver.utility.ViewUtils.uiShowView;

public class DashboardFragment extends Fragment {

    private DashboardFragmentBinding binding;
    private DashboardSharedPrefManager prefManager;
    ActivityResultLauncher<Intent> ResultLauncher;

    private final List<DashboardAccountItem> dashboardAccountItemList = new ArrayList<>();
    private DashboardRecyclerViewAdapater adapter;

    private RoomDashboardViewModel dashboardViewModel;
    private RoomTransactionsViewModel transactionsViewModel;

    private boolean rotationStatus = false;

    private final String DATE = stringDate ();
    private final String CURRENCY_ACCOUNT = "UAH";

    //late init
    protected int accountID,accountImageType;
    protected String accountName,transactionType, accountTransactionCategory;
    protected double  accountTransactionEstimate, accountValue;
    protected double incomeValue,expValue,balanceValue;
    protected Animation fabOpen,fabClose;

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
                        if(transactionType.matches(getResources ().getString (R.string.income))
                                || transactionType.matches(getResources ().getString (R.string.exp)) ){
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
        initAnimations();
        initClickListeners();
        initPrefManager();
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        initSetRecView();
        initLoadData ();
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
            String drawablePos = result.getString("accountImageName");
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

            if(rotationStatus){
                uiFabAnimation();
            }

        });
    }

    private void initAnimations(){
        fabOpen = AnimationUtils.loadAnimation (requireContext (),R.anim.anim_fab_open);
        fabClose = AnimationUtils.loadAnimation (requireContext (),R.anim.anim_fab_close);
    }

    private void initSetRecView(){
        binding.accountView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new DashboardRecyclerViewAdapater (dashboardAccountItemList, getParentFragmentManager());
        binding.accountView.setAdapter(adapter);
        initRecViewTouchHelper ();
        uiShowView(binding.accountView);
    }

    private void initConnectViewModel(){
        transactionsViewModel = new ViewModelProvider(requireActivity()).get(RoomTransactionsViewModel.class);
        dashboardViewModel = new ViewModelProvider(requireActivity()).get(RoomDashboardViewModel.class);
        dashboardViewModel.getAllAccounts().observe(requireActivity(), accounts -> adapter.updateList(accounts));
    }


    private void initLoadData(){
        initAfterDelay(this::initConnectViewModel,250);
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

    private void initClickListeners(){
        binding.actionButton.setOnClickListener(view -> uiFabAnimation ());
        binding.addAccount.setOnClickListener(view -> startDialogFragment ());
        binding.newTransactionDialog.setOnClickListener(view -> startNewTransaction ());
        binding.deleteAccountData.setOnClickListener(view -> startAlertDialog ());
    }

    private void initPrefManager(){
        prefManager = new DashboardSharedPrefManager (requireActivity(), ConstantsUtils.PREFERENCE_NAME);
        uiSetSharedPrefValues ();
    }

    //activities and dialogs
    private void startNewTransaction(){
        Intent intent = new Intent(getContext(), DashboardNewTransaction.class);

        //FIX later
        ArrayList<String> idLists = new ArrayList<>();
        ArrayList<String> namesLists = new ArrayList<>();
        ArrayList<String> valuesLists = new ArrayList<>();
        if (binding.accountView.getChildCount() !=0) {
            for (int i=0;i <binding.accountView.getChildCount();i++) {
                DashboardAccountItem account = adapter.getAccountAtPosition(i);
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

    private void startDialogFragment(){
        DialogFragment newAccountDialogFragment = new DashboardNewAccountDialog ();
        Bundle bundle = new Bundle();
        bundle.putString("operationKey", KEY_CREATE);
        newAccountDialogFragment.setArguments(bundle);
        newAccountDialogFragment.show(requireActivity().getSupportFragmentManager(), "createDialog");
    }

    private void startAlertDialog(){
        AlertDialog.Builder builder = genericDialogOptions (requireContext (),"WARNING",
                "You are going to delete all accounts. Proceed?");

        builder.setPositiveButton("YES", (dialogInterface, i) -> roomDeleteAllAccounts());
        builder.setNegativeButton("NO", (dialogInterface, i) -> dialogInterface.cancel());
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
        uiFabAnimation ();
    }

    //Room operations
    private void roomUpdateAccount(int accountId, String accountName, double accountValue,
                                   String accountCurrency, String accountDrawablePos){
        dashboardViewModel.update(new DashboardAccountItem (accountId,accountName,accountValue,accountCurrency,accountDrawablePos));
        uiUpdateWithDelay ();
    }

    private void roomUpdateAccountAfterTransaction(int accountID, double accountValue){
        dashboardViewModel.updateAccountAfterTransaction(accountID,accountValue);
        uiUpdateWithDelay ();
    }

    private void roomInsertAccount(String accountName, double accountValue, String accountCurrency, String accountDrawablePos ){
        dashboardViewModel.insert(new DashboardAccountItem (accountName,accountValue,accountCurrency,accountDrawablePos));
        uiUpdateWithDelay ();
    }

    private void roomDeleteAccount(int pos){
        dashboardViewModel.delete(adapter.getAccountAtPosition(pos));
        uiUpdateWithDelay ();
    }

    private void roomDeleteAllAccounts(){
        dashboardViewModel.deleteAll ();
        uiShowSnackBar (binding.DashboardLayout,"All accounts were deleted.");
        uiUpdateWithDelay ();
    }

    //UI
    private void uiUpdateVisibleSum(){
        binding.totalBalanceValue.setText(adapter.getSumOfAllItems (binding.accountView));
        binding.accountView.smoothScrollToPosition(0);
        uiUpdateSharedPref ();
    }

    private void uiUpdateWithDelay(){
        initAfterDelay(this::uiUpdateVisibleSum, 500);
    }

    private void uiFabAnimation(){
        if(rotationStatus){
            uiCloseAnimation();
        }
        else{
            uiOpenAnimation();
        }
        uiFabAnimationRotate(rotationStatus);
    }

    private void uiCloseAnimation(){
        uiFabStartAnimation (binding.addAccount,fabClose);
        uiFabStartAnimation (binding.deleteAccountData,fabClose);
        rotationStatus = false;
    }

    private void uiOpenAnimation(){
        uiFabStartAnimation (binding.addAccount,fabOpen);
        uiFabStartAnimation (binding.deleteAccountData,fabOpen);
        rotationStatus = true;
    }

    private void uiFabAnimationRotate(boolean buttonStatus){
            if (buttonStatus){
                uiChangeDrawableTo(R.drawable.ic_more_ico_gorizontal);
            } else {
                uiChangeDrawableTo(R.drawable.ic_more_ico_vertical);
            }
        ObjectAnimator.ofFloat(binding.actionButton, "rotation", 0f, 90f).setDuration(200).start();
    }

    private void uiFabStartAnimation(FloatingActionButton button, Animation animation){
        button.startAnimation(animation);
    }

    private void uiChangeDrawableTo(int drawableID){
        binding.actionButton.setImageDrawable
                (ContextCompat.getDrawable(requireContext (),drawableID));
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
                accountTransactionEstimate,CURRENCY_ACCOUNT, DATE,accountImageType,transactionType));

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
                transferValue,CURRENCY_ACCOUNT, DATE,imageType,transactionType));
    }

    }





