package com.ashunevich.finobserver.TransactionsPackage;

import android.annotation.SuppressLint;

import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;


import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.TransactionDialogBinding;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import androidx.appcompat.app.AppCompatActivity;

import static java.security.AccessController.getContext;


public class TransactionNew extends AppCompatActivity {
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
        String [] accounts = new String[] {"Privat","Cash","BankX","CreditCard"};

        createChips(incomeArray,binding.IncomeChipGroup);
        createChips(expendituresArray,binding.SpendingChipGroup);
        binding.IncomeChipGroup.setVisibility(View.GONE);
        binding.SpendingChipGroup.setVisibility(View.GONE);
        View view = binding.getRoot();
        setContentView(view);
        setChipsGroupListener();
        binding.resumeDialog.setOnClickListener(v -> voidMakeToast());
        setSpinner(accounts);
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

    // TEST DELETE LATER
    private void setSpinner(String [] array){
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.support_simple_spinner_dropdown_item, array);
        binding.ActiveAccounts.setAdapter(adapter);
    }

    // TEST DELETE LATER
    private void voidMakeToast(){
        typeChip = returnChipText(binding.transactionType);
        valueChip = Double.valueOf(binding.transactionValue.getText().toString());
        transactionAccount = binding.ActiveAccounts.getSelectedItem().toString();
        String result = "TYPE -  " + typeChip + " - CATEGORY -  " + categoryChip + " - VALUE - " + valueChip + " - ACCOUNT - "  + transactionAccount ;
        Toast.makeText(this,result,Toast.LENGTH_SHORT).show();

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
                               binding.SpendingChipGroup.setVisibility(View.GONE);
                               binding.IncomeChipGroup.setVisibility(View.VISIBLE);
                               setChipGroupUncheck(binding.SpendingChipGroup);
                           }
                           else{
                               binding.SpendingChipGroup.setVisibility(View.VISIBLE);
                               binding.IncomeChipGroup.setVisibility(View.GONE);
                               setChipGroupUncheck(binding.IncomeChipGroup);
                           }
        });

        binding.IncomeChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
         //   (TODO) return value of IncomeChipGroup
            categoryChip = returnChipText(group);
        });


        binding.SpendingChipGroup.setOnCheckedChangeListener((group, checkedId) -> {
            //   (TODO) return value of SpendingChipGroup
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



    public void postValue(String transactionType,String transactionCategory,String transactionAccount,Double transactionValue ) {
        //   (TODO) send values to parent activity.
    }

}
