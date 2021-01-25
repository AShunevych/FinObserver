package com.ashunevich.finobserver.TransactionsPackage;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ashunevich.finobserver.DashboardAccountPackage.AccountItem;
import com.ashunevich.finobserver.UtilsPackage.TransactionDiffUtilCallback;
import com.ashunevich.finobserver.databinding.TransactionItemBinding;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionRecViewAdapter extends RecyclerView.Adapter<TransactionRecViewAdapter.MyViewHolder>  {
    private ArrayList<TransactionItem> pad_list;
    //   private PassBalanceValue mListerner;
    public TransactionRecViewAdapter(ArrayList<TransactionItem> data){
        this.pad_list = data;
    }
    //This method inflates view present in the RecyclerView
    @NonNull
    @Override
    public TransactionRecViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TransactionRecViewAdapter.MyViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final TransactionRecViewAdapter.MyViewHolder holder, int position) {
        final TransactionItem item  = pad_list.get(position);
        holder.binding.transactionID.setText(String.valueOf(item.getItemIID()));
        holder.binding.transactionTypeImage.setImageDrawable(item.getImage());
        holder.binding.transactionValue.setText(item.getTransactionValue());
        holder.binding.transactionCurrency.setText(String.valueOf(item.getTransactionCurrency()));
        holder.binding.tranactionAccountFrom.setText(item.getTransactionAccount());
        holder.binding.transactionCategory.setText(item.getTransactionCategory());
        holder.binding.transactionDate.setText(item.getTransactionDate());
    }


    //Setting the arraylist
    public void setListContent(ArrayList <TransactionItem> pad_list) {
        this.pad_list = pad_list;
    }

    @Override
    public int getItemCount() {
        return pad_list.size();
    }

    //View holder class, where all view components are defined
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TransactionItemBinding binding;
        public MyViewHolder(TransactionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public void updateItemList(ArrayList<TransactionItem> items){
        final TransactionDiffUtilCallback diffCallback = new TransactionDiffUtilCallback(this.pad_list, items);
        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);

        this.pad_list.clear();
        this.pad_list.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

/*
    public interface PassBalanceValue {
        void passData(Double value);
    }
*/
}

