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


import static com.ashunevich.finobserver.dashboard.DashboardUtils.extractDouble;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.getSelectedItemFromSpinner;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.returnActiveChipId;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.returnChipText;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.setChipGroupUncheck;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.stringToDouble;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.stringToInteger;
import static com.ashunevich.finobserver.UtilsPackage.Utils.getSelectedItemOnSpinnerPosition;
import static com.ashunevich.finobserver.dashboard.DashboardUtils.sumDouble;

public class DashboardNewTransaction extends AppCompatActivity {
    private TransactionDialogBinding binding;
    String typeChip, categoryChip ;

    Double transactionEstimate = 0.0;

    String transactionAccount, targetAccount;
    int basicAccountID,targetAccountID;
    int basicAccountImagePos, targetAccountImagePos;
    double basicValue, targetValue, newBasicAccountValue,newTargetAccountValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TransactionDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getIntents();
        setUIStatusOnStart();

        setTextWatcher();
        setChipsOnStart();
        setChipsGroupListener();
        setCLickListeners();

    }


    //init methods
    private void getIntents(){
        setSpinner(getIntent().getStringArrayListExtra("AccountNames"));
        setAdditionalInfo(getIntent().getStringArrayListExtra("AccountIDs"),
                getIntent().getStringArrayListExtra("AccountValues"),
                getIntent().getStringArrayListExtra("AccountImages"));
    }

    private void setAdditionalInfo(ArrayList<String> idArray,
                                   ArrayList<String> valueArray,
                                   ArrayList<String> imagesArray){
        binding.ActiveAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int spinnerPos = getSelectedItemOnSpinnerPosition(binding.ActiveAccounts);
                basicAccountID = stringToInteger(idArray.get(spinnerPos));
                basicValue = stringToDouble(valueArray.get(spinnerPos));
                basicAccountImagePos = stringToInteger(imagesArray.get(spinnerPos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.targetAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int spinnerPos = getSelectedItemOnSpinnerPosition(binding.targetAccount);
                targetAccountID = stringToInteger(idArray.get(spinnerPos));
                targetValue = stringToDouble(valueArray.get(spinnerPos));
                targetAccountImagePos = stringToInteger(imagesArray.get(spinnerPos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void setChipsOnStart(){
        chipFactory(getResources().getStringArray(R.array.expendituresCategory),binding.SpendingChipGroup);
        chipFactory(getResources().getStringArray(R.array.incomeCategory),binding.IncomeChipGroup);
    }

    //UI

    private void setSpinner(ArrayList<String> array){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, array);
        binding.ActiveAccounts.setAdapter(adapter);
        binding.targetAccount.setAdapter(adapter);
    }

    private void setUIStatusOnStart(){
        binding.IncomeChipGroup.setVisibility(View.GONE);
        binding.SpendingChipGroup.setVisibility(View.GONE);
        binding.targetAccount.setVisibility(View.GONE);
        binding.resumeDialog.setEnabled(false);
        binding.transactionType.setVisibility(View.INVISIBLE);
    }

    private void chipViewHandlerPos(int UIHandlingPos){
        switch (UIHandlingPos){
            case 1 :
                binding.IncomeChipGroup.setVisibility(View.VISIBLE);
                binding.SpendingChipGroup.setVisibility(View.GONE);
                binding.targetAccount.setVisibility(View.GONE);
                binding.categoryTextView.setText(getResources().getString(R.string.cat));
                setChipGroupUncheck(binding.SpendingChipGroup);
                break;
            case 2:
                binding.SpendingChipGroup.setVisibility(View.VISIBLE);
                binding.IncomeChipGroup.setVisibility(View.GONE);
                binding.targetAccount.setVisibility(View.GONE);
                binding.categoryTextView.setText(getResources().getString(R.string.cat));
                setChipGroupUncheck(binding.IncomeChipGroup);
                break;
            case 3 :
                binding.SpendingChipGroup.setVisibility(View.GONE);
                binding.IncomeChipGroup.setVisibility(View.GONE);
                binding.targetAccount.setVisibility(View.VISIBLE);
                binding.categoryTextView.setText(getResources().getString(R.string.target));
                setChipGroupUncheck(binding.IncomeChipGroup);
                setChipGroupUncheck(binding.SpendingChipGroup);
                break;
            case 4:
                binding.SpendingChipGroup.setVisibility(View.GONE);
                binding.IncomeChipGroup.setVisibility(View.GONE);
                setChipGroupUncheck(binding.IncomeChipGroup);
                setChipGroupUncheck(binding.SpendingChipGroup);
                setChipGroupUncheck(binding.transactionType);
                binding.categoryTextView.setText(getResources().getString(R.string.cat));
                binding.transactionType.setVisibility(View.INVISIBLE);
                binding.targetAccount.setVisibility(View.GONE);
                binding.resumeDialog.setEnabled(false);
        }
    }

    //listeners
    @SuppressLint("NonConstantResourceId")
    private void setChipsGroupListener(){
        binding.transactionType.setOnCheckedChangeListener((group, checkedId) -> {
            switch (returnActiveChipId(group)){
                case R.id.incomeChip : chipViewHandlerPos(1);break;
                case R.id.spendingChip : chipViewHandlerPos(2);break;
                case R.id.transferChip : chipViewHandlerPos(3); enableSubmitButton();break;
            }
        });

        binding.IncomeChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            categoryChip = returnChipText(group);
            enableSubmitButton();
        });

        binding.SpendingChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            categoryChip = returnChipText(group);
            enableSubmitButton();
        });

    }

    private void setTextWatcher(){
        binding.transactionEstimate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                enableChipWhenValueEntered(binding.transactionEstimate);
            }
        });
    }
    
    private void setCLickListeners(){
        binding.resumeDialog.setOnClickListener(v -> onSubmitAction());
        binding.cancelDialog.setOnClickListener(v -> onCancelResult());
    }

    //Result handlers
    private void onSubmitAction() {
        transactionEstimate = stringToDouble(binding.transactionEstimate.getText().toString());
        transactionAccount = getSelectedItemFromSpinner(binding.ActiveAccounts);
        targetAccount = getSelectedItemFromSpinner(binding.targetAccount);
        typeChip = returnChipText(binding.transactionType);

        if (typeChip.matches(getResources().getString(R.string.transfer))) {
            transferResult();
        } else {
            incomeExpResult();
        }
    }

    private void transferResult(){
        Intent previousScreen = new Intent(getApplicationContext(), DashboardFragment.class);

        newBasicAccountValue = basicValue - transactionEstimate;
        newTargetAccountValue = targetValue + transactionEstimate;

        previousScreen.putExtra("Type",typeChip);

        previousScreen.putExtra("basicAccountID", basicAccountID);
        previousScreen.putExtra("targetAccountID", targetAccountID);

        previousScreen.putExtra("basicAccountImagePos", basicAccountImagePos);
        previousScreen.putExtra("targetAccountImagePos", targetAccountImagePos);

        previousScreen.putExtra("basicAccountName",transactionAccount);
        previousScreen.putExtra("targetAccountName",targetAccount);

        previousScreen.putExtra("Category",categoryChip);

        previousScreen.putExtra("transferValue", transactionEstimate);//updatedValue
        previousScreen.putExtra("newBasicAccountValue",newBasicAccountValue);//updatedValue
        previousScreen.putExtra("newTargetAccountValue",newTargetAccountValue);//updatedValue
        if(basicAccountID == targetAccountID){
            Toast.makeText(this, "Same account", Toast.LENGTH_SHORT).show();
        }
        else {
        setResult(RESULT_OK,previousScreen);finish();
        }
    }

    private void incomeExpResult(){
        ////int updatedID, String updatedName, double updatedValue, int updatedImagePos
        //for update
        transactionEstimate = stringToDouble(binding.transactionEstimate.getText().toString());
        transactionAccount = getSelectedItemFromSpinner(binding.ActiveAccounts);
        Intent previousScreen = new Intent(getApplicationContext(), DashboardFragment.class);

        if(typeChip.matches("Income")){
            newBasicAccountValue = sumDouble(basicValue,transactionEstimate);
        }
        else{
            newBasicAccountValue = extractDouble(basicValue,transactionEstimate);
        }

            previousScreen.putExtra("ID", basicAccountID); //updatedID
            previousScreen.putExtra("Account",transactionAccount);//updatedName
            previousScreen.putExtra("Estimate",transactionEstimate);//updatedValue
            previousScreen.putExtra("ImagePos", basicAccountImagePos);//updatedImagePos
            previousScreen.putExtra("Category",categoryChip);
            previousScreen.putExtra("Type",typeChip);
            previousScreen.putExtra("Value", newBasicAccountValue);
            setResult(RESULT_OK,previousScreen);
            finish();
    }

    private void onCancelResult(){
        Intent previousScreen = new Intent(getApplicationContext(), DashboardFragment.class);
        setResult(RESULT_CANCELED,previousScreen);
        finish();
    }


    //Utils
    private String getText(EditText text) {
        return text.getText().toString().trim();
    }

    private void chipFactory(String [] list, ChipGroup chipGroup){
        for (String category :
                list)
        {
            @SuppressLint("InflateParams") Chip mChip = (Chip) this.getLayoutInflater().inflate(R.layout.transaction_chip_item, null, false);
            mChip.setText(category);
            int paddingDp = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, getResources().getDisplayMetrics()
            );
            mChip.setPadding(paddingDp, 0, paddingDp, 0);
            mChip.setOnCheckedChangeListener((compoundButton, b) -> {
                List<Integer> ids = chipGroup.getCheckedChipIds();
                for (Integer id : ids) {
                    Chip chip = chipGroup.findViewById(id);
                    String text = chip.getText().toString();
                    Log.d ("SELECTED CHIP VALUE IS",text);
                }
            });
            chipGroup.addView(mChip);
        }
    }

    //switches
    private void enableSubmitButton() {
        binding.resumeDialog.setEnabled(true);
    }

    private void enableChipWhenValueEntered(EditText editText){
        if(getText(editText).length() > 0){
            binding.transactionType.setVisibility(View.VISIBLE);
        }
        else{
            chipViewHandlerPos(4);
        }

    }



}
