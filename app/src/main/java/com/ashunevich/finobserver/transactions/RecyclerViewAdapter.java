package com.ashunevich.finobserver.transactions;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.ashunevich.finobserver.R;
import com.ashunevich.finobserver.databinding.TransactionItemBinding;
import com.ashunevich.finobserver.factories.FactoryRecViewDiffUtil;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder>  {
    private List<TransactionBoardItem> pad_list;
    Context context;

    public RecyclerViewAdapter(List<TransactionBoardItem> data){
        this.pad_list = data;
    }
    //This method inflates view present in the RecyclerView
    @NonNull
    @Override
    public RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        return new RecyclerViewAdapter.MyViewHolder(TransactionItemBinding.inflate(LayoutInflater.from(parent.getContext()),
                parent, false));
    }

    //Binding the data using get() method of POJO object
    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.MyViewHolder holder, int position) {
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
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private final TransactionItemBinding binding;
        public MyViewHolder(TransactionItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    private Drawable getTypeImage(int i) {

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
     void updateItemList(List<TransactionBoardItem> items){
         final FactoryRecViewDiffUtil<TransactionBoardItem> diffCallback = new FactoryRecViewDiffUtil<>(this.pad_list, items);
         final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);


         this.pad_list.clear ();
        this.pad_list.addAll(items);
        diffResult.dispatchUpdatesTo(this);
    }

    void filter(List<TransactionBoardItem> filter) {
        this.pad_list = filter;
        notifyDataSetChanged();
    }


}

