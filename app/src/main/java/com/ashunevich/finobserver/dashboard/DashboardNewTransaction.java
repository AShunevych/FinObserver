package com.ashunevich.finobserver.dashboard;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Toast;


import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.TransactionDialogBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static com.ashunevich.finobserver.utils.Utils.returnActiveChipId;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.doubleExtraction;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.stringAsTextFromSpinner;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.stringFromActiveChip;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.doubleFromString;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.intFromString;
import static com.ashunevich.finobserver.utils.Utils.getSelectedItemOnSpinnerPosition;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.doubleSum;
import static com.ashunevich.finobserver.utils.Utils.uiHideHideShow;
import static com.ashunevich.finobserver.utils.Utils.uiHideView;
import static com.ashunevich.finobserver.utils.Utils.uiShowView;
import static com.ashunevich.finobserver.utils.Utils.uiUncheckChipGroup;

public class DashboardNewTransaction extends AppCompatActivity {
    private TransactionDialogBinding binding;
    private String typeChip, categoryChip;

    private Double transactionEstimate = 0.0;

    private String transactionAccount, targetAccount;
    private int basicAccountID, targetAccountID;
    private double basicValue, targetValue, newBasicAccountValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        binding = TransactionDialogBinding.inflate (getLayoutInflater ());
        setContentView (binding.getRoot ());

        initIntents ();
        setUIStatusOnStart ();

        setTextWatcher ();
        initCreateChipOnStart ();
        setChipsGroupListener ();
        setCLickListeners ();

    }

    //init methods
    private void initIntents() {
        setSpinner (getIntent ().getStringArrayListExtra ("AccountNames"));
        initBundleFromActivity (getIntent ().getStringArrayListExtra ("AccountIDs"),
                getIntent ().getStringArrayListExtra ("AccountValues"));
    }

    private void initBundleFromActivity(ArrayList<String> idArray,
                                        ArrayList<String> valueArray) {
        binding.ActiveAccounts.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int spinnerPos = getSelectedItemOnSpinnerPosition (binding.ActiveAccounts);
                basicAccountID = intFromString (idArray.get (spinnerPos));
                basicValue = doubleFromString (valueArray.get (spinnerPos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.targetAccount.setOnItemSelectedListener (new AdapterView.OnItemSelectedListener () {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int spinnerPos = getSelectedItemOnSpinnerPosition (binding.targetAccount);
                targetAccountID = intFromString (idArray.get (spinnerPos));
                targetValue = doubleFromString (valueArray.get (spinnerPos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void initCreateChipOnStart(){
        utilsChipFactory (getResources().getStringArray(R.array.Expenses),binding.SpendingChipGroup);
        utilsChipFactory (getResources().getStringArray(R.array.Income),binding.IncomeChipGroup);
    }

    //UI
    private void setSpinner(ArrayList<String> array) {
        ArrayAdapter<String> adapter = new ArrayAdapter<> (this, R.layout.support_simple_spinner_dropdown_item, array);
        binding.ActiveAccounts.setAdapter (adapter);
        binding.targetAccount.setAdapter (adapter);
    }

    private void setUIStatusOnStart() {
        uiHideView (binding.SpendingChipGroup);
        uiHideView (binding.IncomeChipGroup);
        uiHideView (binding.targetAccount);
        uiDisableSubmitButton ();
        binding.transactionType.setVisibility (View.INVISIBLE);
        uiUncheckChipGroup (binding.IncomeChipGroup);
        uiUncheckChipGroup (binding.SpendingChipGroup);
        uiUncheckChipGroup (binding.transactionType);
    }

    private void setChipViewHandlerPos(int UIHandlingPos) {
        switch (UIHandlingPos) {
            case 1:
                uiDisableSubmitButton ();
                uiHideHideShow(binding.SpendingChipGroup,binding.targetAccount,binding.IncomeChipGroup);
                uiSetText (getResources ().getString (R.string.cat));
                uiUncheckChipGroup (binding.SpendingChipGroup);
                break;
            case 2:
                uiDisableSubmitButton ();
                uiHideHideShow(binding.IncomeChipGroup,binding.targetAccount,binding.SpendingChipGroup);
                uiSetText (getResources ().getString (R.string.cat));
                uiUncheckChipGroup (binding.IncomeChipGroup);
                break;
            case 3:
                uiHideHideShow(binding.SpendingChipGroup,binding.IncomeChipGroup,binding.targetAccount);
                uiSetText (getResources ().getString (R.string.target));
                uiUncheckChipGroup (binding.IncomeChipGroup);
                uiUncheckChipGroup (binding.SpendingChipGroup);
                break;
            case 4:
                setUIStatusOnStart ();
                uiSetText (getResources ().getString (R.string.cat));
                break;
        }
    }

    //listeners
    @SuppressLint("NonConstantResourceId")
    private void setChipsGroupListener() {
        binding.transactionType.setOnCheckedChangeListener ((group, checkedId) -> {
            switch (returnActiveChipId (group)) {
                case R.id.incomeChip: setChipViewHandlerPos (1);
                    break;
                case R.id.spendingChip: setChipViewHandlerPos (2);
                    break;
                case R.id.transferChip: setChipViewHandlerPos (3);
                    break;
            }
        });

        binding.IncomeChipGroup.setOnCheckedChangeListener ((group, checkedId) -> {
            categoryChip = stringFromActiveChip (group);
            uiEnableSubmitButton ();
        });

        binding.SpendingChipGroup.setOnCheckedChangeListener ((group, checkedId) -> {
            categoryChip = stringFromActiveChip (group);
            uiEnableSubmitButton ();
        });
    }

    private void setTextWatcher() {
        binding.transactionEstimate.addTextChangedListener (new TextWatcher () {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                uiChipValueStatus (binding.transactionEstimate);
            }
        });
    }

    private void setCLickListeners() {
        binding.resumeDialog.setOnClickListener (v -> resultOnSubmit ());
        binding.cancelDialog.setOnClickListener (v -> resultCancelOperation ());
    }

    //Result handlers
    private void resultOnSubmit() {
        transactionEstimate = doubleFromString (binding.transactionEstimate.getText ().toString ());
        transactionAccount = stringAsTextFromSpinner (binding.ActiveAccounts);
        targetAccount = stringAsTextFromSpinner (binding.targetAccount);
        typeChip = stringFromActiveChip (binding.transactionType);

        if (typeChip.matches (getResources ().getString (R.string.transfer))) {
            resultSetTransferOperation ();
        } else {
            resultBasicOperation ();
        }
    }

    private void resultSetTransferOperation() {
        Intent previousScreen = new Intent (getApplicationContext (), DashboardFragment.class);

        newBasicAccountValue = basicValue - transactionEstimate;
        double newTargetAccountValue = targetValue + transactionEstimate;

        previousScreen.putExtra ("Type", typeChip);

        previousScreen.putExtra ("basicAccountID", basicAccountID);
        previousScreen.putExtra ("targetAccountID", targetAccountID);

        previousScreen.putExtra ("basicAccountName", transactionAccount);
        previousScreen.putExtra ("targetAccountName", targetAccount);

        previousScreen.putExtra ("Category", categoryChip);

        previousScreen.putExtra ("transferValue", transactionEstimate);
        previousScreen.putExtra ("newBasicAccountValue", newBasicAccountValue);
        previousScreen.putExtra ("newTargetAccountValue", newTargetAccountValue);
        if (basicAccountID == targetAccountID) {
            Toast.makeText (this, "Same account", Toast.LENGTH_SHORT).show ();
        } else {
            setResult (RESULT_OK, previousScreen);
            finish ();
        }
    }

    private void resultBasicOperation() {
        ////int updatedID, String updatedName, double updatedValue, int updatedImagePos
        //for update
        transactionEstimate = doubleFromString (binding.transactionEstimate.getText ().toString ());
        transactionAccount = stringAsTextFromSpinner (binding.ActiveAccounts);
        Intent previousScreen = new Intent (getApplicationContext (), DashboardFragment.class);

        if (typeChip.matches ("Income")) {
            newBasicAccountValue = doubleSum (basicValue, transactionEstimate);
        } else {
            newBasicAccountValue = doubleExtraction (basicValue, transactionEstimate);
        }

        previousScreen.putExtra ("ID", basicAccountID); //updatedID
        previousScreen.putExtra ("Account", transactionAccount);//updatedName
        previousScreen.putExtra ("Estimate", transactionEstimate);//updatedValue
        previousScreen.putExtra ("Category", categoryChip);
        previousScreen.putExtra ("Type", typeChip);
        previousScreen.putExtra ("Value", newBasicAccountValue);
        setResult (RESULT_OK, previousScreen);
        finish ();
    }

    private void resultCancelOperation() {
        Intent previousScreen = new Intent (getApplicationContext (), DashboardFragment.class);
        setResult (RESULT_CANCELED, previousScreen);
        finish ();
    }

    //Utils
    private String utilsGetText(EditText text) {
        return text.getText ().toString ().trim ();
    }

    private void utilsChipFactory(String[] list, ChipGroup chipGroup) {
        for (String category :
                list) {
            @SuppressLint("InflateParams") Chip mChip = (Chip) this.getLayoutInflater ().inflate (R.layout.custom_chip_item, null, false);
            mChip.setText (category);
            int paddingDp = (int) TypedValue.applyDimension (TypedValue.COMPLEX_UNIT_DIP, 10, getResources ().getDisplayMetrics ()
            );
            mChip.setPadding (paddingDp, 0, paddingDp, 0);
            mChip.setOnCheckedChangeListener ((compoundButton, b) -> {
                List<Integer> ids = chipGroup.getCheckedChipIds ();
                for (Integer id : ids) {
                    Chip chip = chipGroup.findViewById (id);
                    String text = chip.getText ().toString ();
                    Log.d ("SELECTED CHIP VALUE IS", text);
                }
            });
            chipGroup.addView (mChip);
        }
    }

    //switches
    private void uiEnableSubmitButton() {
        binding.resumeDialog.setEnabled (true);
    }

    private void uiDisableSubmitButton() {
        binding.resumeDialog.setEnabled (false);
    }

    private void uiSetText(String text) {
        binding.categoryTextView.setText (text);
    }

    private void uiChipValueStatus(EditText editText){
        if(utilsGetText (editText).length() > 0){
            uiShowView (binding.transactionType);
        }
        else{
            setChipViewHandlerPos (4);
            uiDisableSubmitButton();
        }

    }

}
