package com.ashunevich.finobserver.TransactionsPackage;

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


import com.ashunevich.finobserver.DashboardPackage.Dashboard_Fragment;
import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.TransactionDialogBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;

import static com.ashunevich.finobserver.TransactionsPackage.Transaction_Utils.returnActiveChipId;
import static com.ashunevich.finobserver.TransactionsPackage.Transaction_Utils.setChipGroupUncheck;

public class Transaction_CreateTransaction extends AppCompatActivity {
    private TransactionDialogBinding binding;
    String typeChip ;
    String categoryChip ;
    Double transactionValue = 0.0;
    String transactionAccount, targetAccount;
    int basicAccountID,targetAccountID;
    int basicAccountImagePos, targetAccountImagePos;
    double basicValue, targetValue, newBasicAccountValue,newTargetAccountValue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TransactionDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setTextWatcher();
        createChips();
        setChipsGroupListener();
        getIntents();
        setUIStatusOnStart();

        binding.resumeDialog.setOnClickListener(v -> onSubmitAction());
        binding.cancelDialog.setOnClickListener(v -> onCancelResult());

    }

    private void getIntents(){
        setSpinner(getIntent().getStringArrayListExtra("AccountNames"));
        setAdditionalInfo(getIntent().getStringArrayListExtra("AccountIDs"),
                getIntent().getStringArrayListExtra("AccountValues"),
                getIntent().getStringArrayListExtra("AccountImages"));
    }

    private void setUIStatusOnStart(){
        binding.IncomeChipGroup.setVisibility(View.GONE);
        binding.SpendingChipGroup.setVisibility(View.GONE);
        binding.targetAccount.setVisibility(View.GONE);
        binding.resumeDialog.setEnabled(false);
        binding.transactionType.setVisibility(View.INVISIBLE);
    }

    private void setAdditionalInfo(ArrayList<String> idArray,
                                   ArrayList<String> valueArray,
                                   ArrayList<String> imagesArray){
        binding.ActiveAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int spinnerPos = Transaction_Utils.SpinnerPosition(binding.ActiveAccounts);
                basicAccountID = Integer.parseInt(idArray.get(spinnerPos));
                basicValue = Double.parseDouble(valueArray.get(spinnerPos));
                basicAccountImagePos = Integer.parseInt(imagesArray.get(spinnerPos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        binding.targetAccount.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int spinnerPos = Transaction_Utils.SpinnerPosition(binding.targetAccount);
                targetAccountID = Integer.parseInt(idArray.get(spinnerPos));
                targetValue = Double.parseDouble(valueArray.get(spinnerPos));
                targetAccountImagePos = Integer.parseInt(imagesArray.get(spinnerPos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }


    private void createChips(){
        createChips(getResources().getStringArray(R.array.expendituresCategory),binding.SpendingChipGroup);
        createChips(getResources().getStringArray(R.array.incomeCategory),binding.IncomeChipGroup);
    }

    private void setSpinner(ArrayList<String> array){
           ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, array);
           binding.ActiveAccounts.setAdapter(adapter);
         binding.targetAccount.setAdapter(adapter);
    }

    // TEST DELETE LATER
    private void onSubmitAction() {
        transactionValue = Double.valueOf(binding.transactionEstimate.getText().toString());
        transactionAccount = binding.ActiveAccounts.getSelectedItem().toString();
        targetAccount = binding.targetAccount.getSelectedItem().toString();
        typeChip = Transaction_Utils.returnChipText(binding.transactionType);

        if (typeChip.matches(getResources().getString(R.string.transfer))) {
            transferResult();
        } else {
            incomeExpResult();
        }
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




    private void transferResult(){
        Intent previousScreen = new Intent(getApplicationContext(), Dashboard_Fragment.class);

            newBasicAccountValue = basicValue - transactionValue;
            newTargetAccountValue = targetValue + transactionValue;

            previousScreen.putExtra("Type",typeChip);

            previousScreen.putExtra("basicAccountID", basicAccountID);
            previousScreen.putExtra("targetAccountID", targetAccountID);

            previousScreen.putExtra("basicAccountImagePos", basicAccountImagePos);
            previousScreen.putExtra("targetAccountImagePos", targetAccountImagePos);

            previousScreen.putExtra("basicAccountName",transactionAccount);
            previousScreen.putExtra("targetAccountName",targetAccount);

            previousScreen.putExtra("Category",categoryChip);

            previousScreen.putExtra("transferValue",transactionValue);//updatedValue
            previousScreen.putExtra("newBasicAccountValue",newBasicAccountValue);//updatedValue
            previousScreen.putExtra("newTargetAccountValue",newTargetAccountValue);//updatedValue
            if(basicAccountID == targetAccountID){
                Toast.makeText(this, "Same account", Toast.LENGTH_SHORT).show();
            }
            else {
            setResult(RESULT_OK,previousScreen);
            finish();
            }

    }

    private void incomeExpResult(){
        ////int updatedID, String updatedName, double updatedValue, int updatedImagePos
        //for update
        transactionValue = Double.valueOf(binding.transactionEstimate.getText().toString());
        transactionAccount = binding.ActiveAccounts.getSelectedItem().toString();
        Intent previousScreen = new Intent(getApplicationContext(), Dashboard_Fragment.class);

            previousScreen.putExtra("ID", basicAccountID); //updatedID
            previousScreen.putExtra("Account",transactionAccount);//updatedName
            previousScreen.putExtra("BasicValue",basicValue);//updatedValue
            previousScreen.putExtra("ImagePos", basicAccountImagePos);//updatedImagePos
            previousScreen.putExtra("Category",categoryChip);
            previousScreen.putExtra("Type",typeChip);
            previousScreen.putExtra("Value", transactionValue);
            setResult(RESULT_OK,previousScreen);
            finish();

    }


    private void onCancelResult(){
        Intent previousScreen = new Intent(getApplicationContext(), Dashboard_Fragment.class);
        setResult(RESULT_CANCELED,previousScreen);
        finish();
    }

    @SuppressLint("NonConstantResourceId")
    private void setChipsGroupListener(){
        binding.transactionType.setOnCheckedChangeListener((group, checkedId) -> {
                        switch (returnActiveChipId(group)){
                            case R.id.incomeChip :  chipViewHandlerPos(1);break;
                            case R.id.spendingChip : chipViewHandlerPos(2);break;
                            case R.id.transferChip : chipViewHandlerPos(3);
                                enableSubmitButton();break;
                        }
        });

        binding.IncomeChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            categoryChip = Transaction_Utils.returnChipText(group);
            enableSubmitButton();
        });

        binding.SpendingChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            categoryChip = Transaction_Utils.returnChipText(group);
            enableSubmitButton();
        });

    }

    private void enableSubmitButton() {
        binding.resumeDialog.setEnabled(true);
    }

    private void enableChipWhenValueEntered(EditText editText){
        if(getText(editText).length() > 0){
            binding.transactionType.setVisibility(View.VISIBLE);
        }
        if(getText(editText).length() == 0){
           chipViewHandlerPos(4);
        }

    }

    private String getText(EditText text) {
        return text.getText().toString().trim();
    }


        private void chipViewHandlerPos(int i){
        switch (i){
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


    private void createChips( String [] list,ChipGroup chipGroup){
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

}
