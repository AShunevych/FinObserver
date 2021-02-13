package com.ashunevich.finobserver.TransactionsPackage;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.ashunevich.finobserver.DashboardPackage.Dashboard_Fragment;
import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.TransactionDialogBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import java.util.ArrayList;
import java.util.List;


import androidx.appcompat.app.AppCompatActivity;

public class Transaction_CreateTransaction extends AppCompatActivity {
    private TransactionDialogBinding binding;
    String typeChip = null;
    String categoryChip = null;
    Double valueChip = 0.0;
    String transactionAccount;
    int id;
    int imagePos;
    double basicValue;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TransactionDialogBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setChipVisibilityAtStart();
        createChips();
        setChipsGroupListener();
        getIntents();
        
        binding.resumeDialog.setOnClickListener(v -> onOkResult());
        binding.cancelDialog.setOnClickListener(v -> onCancelResult());


    }

    private void getIntents(){
        setSpinner(getIntent().getStringArrayListExtra("AccountNames"));
        setAdditionalInfo(getIntent().getStringArrayListExtra("AccountIDs"),
                getIntent().getStringArrayListExtra("AccountValues"),
                getIntent().getStringArrayListExtra("AccountImages"));
    }

    private void setChipVisibilityAtStart(){
        binding.IncomeChipGroup.setVisibility(View.GONE);
        binding.SpendingChipGroup.setVisibility(View.GONE);
    }

    private void setAdditionalInfo(ArrayList<String> idArray,
                                   ArrayList<String> valueArray,
                                   ArrayList<String> imagesArray){
        binding.ActiveAccounts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                int spinnerPos = Transaction_Utils.SpinnerPosition(binding.ActiveAccounts);
                id = Integer.parseInt(idArray.get(spinnerPos));
                basicValue = Double.parseDouble(valueArray.get(spinnerPos));
                imagePos = Integer.parseInt(imagesArray.get(spinnerPos));
                Log.d("id",idArray.get(spinnerPos));
                Log.d("basicValue",valueArray.get(spinnerPos));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }


    private void createChips(){
        createChips(getResources().getStringArray(R.array.incomeCategory),binding.IncomeChipGroup);
        createChips(getResources().getStringArray(R.array.expendituresCategory),binding.SpendingChipGroup);
    }

    private void setSpinner(ArrayList<String> array){
           ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, array);
           binding.ActiveAccounts.setAdapter(adapter);
    }

    // TEST DELETE LATER
    private void onOkResult(){
        typeChip = Transaction_Utils.returnChipText(binding.transactionType);
        valueChip = Double.valueOf(binding.transactionEstimate.getText().toString());
       transactionAccount = binding.ActiveAccounts.getSelectedItem().toString();
        Intent previousScreen = new Intent(getApplicationContext(), Dashboard_Fragment.class);
        if(typeChip != null && valueChip !=null && transactionAccount != null && categoryChip != null){
            ////int updatedID, String updatedName, double updatedValue, int updatedImagePos
            //for update
            previousScreen.putExtra("ID",id); //updatedID
            previousScreen.putExtra("Account",transactionAccount);//updatedName
            previousScreen.putExtra("BasicValue",basicValue);//updatedValue
            previousScreen.putExtra("ImagePos",imagePos);//updatedImagePos


            previousScreen.putExtra("Category",categoryChip);
            previousScreen.putExtra("Type",typeChip);
            previousScreen.putExtra("Value",valueChip);
            setResult(RESULT_OK,previousScreen);
            finish();
        }
        else{
            Toast.makeText(this,"Some fields are empty.Please check!",Toast.LENGTH_SHORT).show();
        }
    }

    private void onCancelResult(){
        Intent previousScreen = new Intent(getApplicationContext(), Dashboard_Fragment.class);
        setResult(RESULT_CANCELED,previousScreen);
        finish();
    }

    private void setChipsGroupListener(){
        binding.transactionType.setOnCheckedChangeListener((group, checkedId) -> {
                           if(Transaction_Utils.returnActiveChipId(group) == R.id.incomeChip){
                               binding.SpendingChipGroup.setVisibility(View.GONE);
                               binding.IncomeChipGroup.setVisibility(View.VISIBLE);
                               Transaction_Utils.setChipGroupUncheck(binding.SpendingChipGroup);
                           }
                           else{
                               binding.SpendingChipGroup.setVisibility(View.VISIBLE);
                               binding.IncomeChipGroup.setVisibility(View.GONE);
                               Transaction_Utils.setChipGroupUncheck(binding.IncomeChipGroup);
                           }
        });

        binding.IncomeChipGroup.setOnCheckedChangeListener((group, checkedId) -> categoryChip = Transaction_Utils.returnChipText(group));


        binding.SpendingChipGroup.setOnCheckedChangeListener((group, checkedId) -> categoryChip = Transaction_Utils.returnChipText(group));
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
