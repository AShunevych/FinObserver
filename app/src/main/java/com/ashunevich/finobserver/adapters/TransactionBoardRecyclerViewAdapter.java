package com.ashunevich.finobserver.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.TransactionItemBinding;
import com.ashunevich.finobserver.data.TransactionBoardItem;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

public class TransactionBoardRecyclerViewAdapter extends RecyclerView.Adapter<TransactionBoardRecyclerViewAdapter.TransactionViewHolder>  {
    private List<TransactionBoardItem> pad_list;
    Context context;

    public TransactionBoardRecyclerViewAdapter(List<TransactionBoardItem> data){
        this.pad_list = data;
    }
    //This method inflates view present in the RecyclerView
    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new TransactionViewHolder (TransactionItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final TransactionViewHolder holder, int position) {
        final TransactionBoardItem item  = pad_list.get(position);
      holder.binding.transactionTypeImage.setImageDrawable(getTypeImage(item.getImageInt()));
         holder.binding.transactionValue.setText(String.valueOf(item.getTransactionValue()));
        holder.binding.transactionCurrency.setText(item.getTransactionCurrency());
        holder.binding.tranactionAccountFrom.setText(item.getTransactionAccount());
        holder.binding.transactionCategory.setText(item.getTransactionCategory());
        holder.binding.transactionDate.setText(item.getTransactionDate());
    }




    @Override
    public int getItemCount() {
        return pad_list.size();
    }

    //View holder class, where all view components are defined
    @VisibleForTesting
    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        private final TransactionItemBinding binding;
        public TransactionViewHolder(TransactionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    public Drawable getTypeImage(int i) {

        if(i == 0){
            return ContextCompat.getDrawable(context, R.drawable.ic_arrow_up_ico);
        }
        else if (i==1){
            return ContextCompat.getDrawable(context, R.drawable.ic_arrow_down_ico);
        }
        else{
            return ContextCompat.getDrawable(context, R.drawable.ic_transfer_ico);
        }
    }

    //Setting the arraylist
     public void updateItemList(List<TransactionBoardItem> items){
         final RecyclerViewDiffUtil<TransactionBoardItem> diffCallback = new RecyclerViewDiffUtil<> (this.pad_list, items);
         final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);


         this.pad_list.clear ();
        this.pad_list.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    public void filter(List<TransactionBoardItem> filter) {
        this.pad_list = filter;
        notifyDataSetChanged ();
    }


}

