package com.ashunevich.finobserver.TransactionsPackage;

import android.annotation.SuppressLint;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.ashunevich.finobserver.DashboardAccountPackage.DashboardFragment;
import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.TransactionDialogBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class TransactionSetNew extends AppCompatActivity {
    private TransactionDialogBinding binding;
    EventBus bus;
    String typeChip = null;
    String categoryChip = null;
    Double valueChip = 0.0;
    String transactionAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = TransactionDialogBinding.inflate(getLayoutInflater());

        String [] incomeArray = new String[] {"Salary","Investment","Credit","Other"};
        String [] expendituresArray = new String[] {"Lifestyle","Mobile Phone","Car","Food","Cafe","Internet","Housing","Investment","Banking","Other"};

        ArrayList<String> arrayList = getIntent().getStringArrayListExtra("AccountTypes");

        createChips(incomeArray,binding.IncomeChipGroup);
        createChips(expendituresArray,binding.SpendingChipGroup);
        binding.IncomeChipGroup.setVisibility(View.GONE);
        binding.SpendingChipGroup.setVisibility(View.GONE);
        View view = binding.getRoot();
        setContentView(view);
        setChipsGroupListener();
        binding.resumeDialog.setOnClickListener(v -> onOkResult());
        binding.cancelDialog.setOnClickListener(v -> onCancelResult());
        if(arrayList != null){
            setSpinner(arrayList);
        }
        else{
            Toast.makeText(this,"Accounts not found",Toast.LENGTH_SHORT).show();
        }
    }

    private int returnActiveChipId(ChipGroup chipGroup){
        int idChip = 0;
        List<Integer> ids = chipGroup.getCheckedChipIds();
            for (Integer id:ids){
                Chip chip = chipGroup.findViewById(id);
                idChip = chip.getId();
            }
        return idChip;
    }

     // (TODO) adapter should fill from DashBoarddFragmentValues.
    private void setSpinner(ArrayList<String> array){
           ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, array);
           binding.ActiveAccounts.setAdapter(adapter);
    }

    // TEST DELETE LATER
    private void onOkResult(){
        typeChip = returnChipText(binding.transactionType);
        valueChip = Double.valueOf(binding.transactionEstimate.getText().toString());
       transactionAccount = binding.ActiveAccounts.getSelectedItem().toString();
        Intent previousScreen = new Intent(getApplicationContext(), DashboardFragment.class);
        previousScreen.putExtra("Type",typeChip);
        previousScreen.putExtra("Value",valueChip);
        previousScreen.putExtra("Category",categoryChip);
        previousScreen.putExtra("Account",transactionAccount);
        postValue(String.valueOf(valueChip),transactionAccount,categoryChip,typeChip);
        setResult(1000,previousScreen);
        finish();
    }

    private void onCancelResult(){
        Intent previousScreen = new Intent(getApplicationContext(), DashboardFragment.class);
        setResult(RESULT_CANCELED,previousScreen);
        finish();
    }


    private String returnChipText(ChipGroup chipGroup){
        String text = null;
        List<Integer> ids = chipGroup.getCheckedChipIds();
        for (Integer id:ids){
            Chip chip = chipGroup.findViewById(id);
            text = chip.getText().toString();
        }
        return text;
    }

    private void setChipsGroupListener(){
        binding.transactionType.setOnCheckedChangeListener((group, checkedId) -> {
                           if(returnActiveChipId(group) == R.id.incomeChip){
                               binding.incomeChip.setCheckedIcon(ContextCompat.getDrawable(this,R.drawable.ic_arrow_drop_up));
                               binding.SpendingChipGroup.setVisibility(View.GONE);
                               binding.IncomeChipGroup.setVisibility(View.VISIBLE);
                               setChipGroupUncheck(binding.SpendingChipGroup);
                           }
                           else{
                               binding.spendingChip.setCheckedIcon(ContextCompat.getDrawable(this,R.drawable.ic_arrow_drop_up));
                               binding.SpendingChipGroup.setVisibility(View.VISIBLE);
                               binding.IncomeChipGroup.setVisibility(View.GONE);
                               setChipGroupUncheck(binding.IncomeChipGroup);
                           }
        });

        binding.IncomeChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
         //   (DONE) return value of IncomeChipGroup
            categoryChip = returnChipText(group);
        });


        binding.SpendingChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            //   (DONE) return value of SpendingChipGroup
            categoryChip = returnChipText(group);
        });
    }
    private void setChipGroupUncheck(ChipGroup chipGroup){
       chipGroup.clearCheck();
    }



    private void createChips(String [] categories,ChipGroup chipGroup){
        for (String category :
                categories) {
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

    public void postValue(String transactionValue, String transactionAccount, String transactionCategory,String transactionType ) {
        //   (TODO) send values to parent activity.
        bus.post(new TransactionNewItem(transactionValue,transactionAccount,transactionCategory,transactionType));
    }



}
