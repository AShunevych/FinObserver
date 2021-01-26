package com.ashunevich.finobserver.AccountPackage;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.UtilsPackage.CustomSpinnerAdapter;
import com.ashunevich.finobserver.databinding.DashboardNewAccountDialogBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

public class Account_NewItemDialog extends DialogFragment {
    private DashboardNewAccountDialogBinding binding;
    ArrayList<Drawable> images;
    EventBus bus;

    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        assert inflater != null;
        binding = DashboardNewAccountDialogBinding.inflate(inflater, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        binding.cancelButton.setOnClickListener(view -> onCancel(Objects.requireNonNull(getDialog())));
        binding.okButton.setOnClickListener(view -> onDismiss(Objects.requireNonNull(getDialog())));
        fillSpinner();
        bus = EventBus.getDefault();
        Objects.requireNonNull(getDialog()).setCanceledOnTouchOutside(true);
        return binding.getRoot();

    }


    public void postValue(Drawable drawable, String accountName,Double accountValue,String accountCurrency) {
        bus.post(new Account_NewtItem(drawable, accountName,accountValue,accountCurrency));
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private Drawable getDrawable(int position){
        return images.get(position);
    }



    @SuppressLint("UseCompatLoadingForDrawables")
    private void fillSpinner(){
        images = new ArrayList<>();
        images.add(getResources().getDrawable(R.drawable.ic_wallet_balance,null));
        images.add(getResources().getDrawable(R.drawable.ic_bank_balance,null));
        images.add(getResources().getDrawable(R.drawable.ic_other_balance,null));
        CustomSpinnerAdapter mCustomAdapter = new CustomSpinnerAdapter(requireContext(), images);
        binding.drawableSpinner.setAdapter(mCustomAdapter);
    }

    public void onDismiss(@NonNull DialogInterface dialog) {
       if(!TextUtils.isEmpty(binding.newAccountName.getText().toString())  && !TextUtils.isEmpty(binding.newAccountValue.getText().toString())){
           postValue(getDrawable(binding.drawableSpinner.getSelectedItemPosition()),binding.newAccountName.getText().toString(),
                   Double.valueOf(binding.newAccountValue.getText().toString()),"UAH");
        }
       else{
          if(TextUtils.isEmpty(binding.newAccountName.getText().toString())){
             binding.newAccountName.setError("Account Name cannot be empty");
          }
          else if(TextUtils.isEmpty(binding.newAccountValue.getText().toString())){
              binding.newAccountValue.setError("Account Value cannot be empty");
          }
          else if(TextUtils.isEmpty(binding.newAccountName.getText().toString()) && TextUtils.isEmpty(binding.newAccountValue.getText().toString())){
              binding.newAccountName.setError("Account Name cannot be empty");
              binding.newAccountValue.setError("Account Value cannot be empty");
          }
       }
        super.onDismiss(dialog);
    }

    public void onCancel(@NonNull DialogInterface dialog) {
        dialog.cancel();
        super.onCancel(dialog);
    }



}


