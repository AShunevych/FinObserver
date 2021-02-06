package com.ashunevich.finobserver.DashboardAccountPackage;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;

import android.view.ViewGroup;

import android.widget.TextView;


import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.DashboardAccountItemBinding;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;


public class Dashboard_RecyclerViewAdapter extends RecyclerView.Adapter<Dashboard_RecyclerViewAdapter.MyViewHolder>  {
    private List<Dashboard_Account> pad_list;
    Context context;
    FragmentManager manager;


    public Dashboard_RecyclerViewAdapter(List<Dashboard_Account> data, FragmentManager manager){
            this.manager = manager;
        this.pad_list = data;
    }
    //This method inflates view present in the RecyclerView
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new MyViewHolder(DashboardAccountItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        if(pad_list!=null){
            final Dashboard_Account account  = pad_list.get(position);
            holder.binding.accountIco.setImageDrawable(returnDrawableByID(account.getImageID()));
            holder.binding.accountType.setText(account.getAccountName());
            holder.binding.accountValue.setText(String.valueOf(account.getAccountValue()));
            holder.binding.accountCurrency.setText(account.getAccountCurrency());

                //TEST
            holder.binding.redactButon.setOnClickListener(view -> {
                DialogFragment UpdateAccountDialogFragment = new Dashboard_DialogUpdateAccount();
                setBundleArgs(UpdateAccountDialogFragment,position);
                UpdateAccountDialogFragment.show(manager,"UpdateDialog");
            });
        }

    }

   private void setBundleArgs (DialogFragment fragment, int position){
        Bundle bundle = new Bundle();
        Dashboard_Account account = getAccountAtPosition(position);
       bundle.putInt("accountID",account.getAccountID());
       bundle.putString("accountName",account.getAccountName());
       bundle.putDouble("accountValue",account.getAccountValue());
       bundle.putString("accountCurrency",account.getAccountCurrency());
       bundle.putInt("imageID",account.getImageID());
       fragment.setArguments(bundle);
   }



    private Drawable returnDrawableByID(int i){
        switch (i){
            case 0 : return ContextCompat.getDrawable(context,R.drawable.ic_wallet_balance);
            case 1 : return ContextCompat.getDrawable(context,R.drawable.ic_bank_balance);
            default: return ContextCompat.getDrawable(context,R.drawable.ic_other_balance);
        }
    }

    protected void updateList(List<Dashboard_Account> accounts){
        final Dashboard_DiffUtil diffCallback = new Dashboard_DiffUtil(this.pad_list, accounts);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.pad_list.clear();
        this.pad_list.addAll(accounts);
        diffResult.dispatchUpdatesTo(this);
    }

    //Setting the arraylist
    public void setListContent(List <Dashboard_Account> pad_list) {
        this.pad_list = pad_list;
        notifyDataSetChanged();
    }

    public Dashboard_Account getAccountAtPosition (int position) {
        return pad_list.get(position);
    }

    @Override
    public int getItemCount() {
        if (pad_list != null)
            return pad_list.size();
        else return 0;
    }

    //View holder class, where all view components are defined
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final DashboardAccountItemBinding binding;
        public MyViewHolder(DashboardAccountItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
//
    
    protected String summAllItemsValue(RecyclerView viewRec){
        double totalPrice = 0;
        for (int i = 0; i<viewRec.getChildCount(); i++)
        {
            RecyclerView.ViewHolder holder = viewRec.getChildViewHolder(viewRec.getChildAt(i));
            TextView view = holder.itemView.findViewById(R.id.accountValue);
            totalPrice += Double.parseDouble(view.getText().toString());
        }
        return String.valueOf(totalPrice);
    }

}
