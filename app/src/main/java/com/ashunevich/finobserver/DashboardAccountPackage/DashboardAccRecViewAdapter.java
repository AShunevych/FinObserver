package com.ashunevich.finobserver.DashboardAccountPackage;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.DashboardAccountItemBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DashboardAccRecViewAdapter extends RecyclerView.Adapter<DashboardAccRecViewAdapter.MyViewHolder>  {
    private ArrayList<AccountItem> pad_list;

    public DashboardAccRecViewAdapter(ArrayList<AccountItem> data){
        this.pad_list = data;
    }
    //This method inflates view present in the RecyclerView
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(DashboardAccountItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final AccountItem item  = pad_list.get(position);
        holder.binding.accountIco.setImageDrawable(item.getImage());
        holder.binding.accountType.setText(item.getAccountType());
        holder.binding.accountValue.setText(String.valueOf(item.getAccountValue()));
        holder.binding.accountCurrency.setText(item.getAccountCurrency());

        holder.binding.deleteAccount.setOnClickListener(view -> {
           // Log.d("Value at this pos",String.valueOf(thisItem.getAccountValue()));
                pad_list.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position,pad_list.size());
        });

        ///когда отнимается через интерефес отправляется новое значение в фрагнмент
    }


    //Setting the arraylist
    public void setListContent(ArrayList <AccountItem> pad_list) {
        this.pad_list = pad_list;
    }

    @Override
    public int getItemCount() {
        return pad_list.size();
    }

    //View holder class, where all view components are defined
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final DashboardAccountItemBinding binding;
        public MyViewHolder(DashboardAccountItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
