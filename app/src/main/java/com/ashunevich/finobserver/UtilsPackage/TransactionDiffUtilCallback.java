package com.ashunevich.finobserver.UtilsPackage;

import com.ashunevich.finobserver.TransactionsPackage.TransactionItem;

import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

public class TransactionDiffUtilCallback extends DiffUtil.Callback {

    ArrayList<TransactionItem> oldList;

    public TransactionDiffUtilCallback(ArrayList<TransactionItem> oldList, ArrayList<TransactionItem> newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    ArrayList<TransactionItem> newList;

    @Override
    public int getOldListSize() {
        return oldList.size();
    }

    @Override
    public int getNewListSize() {
        return newList.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).getItemIID() == newList.get(newItemPosition).getItemIID();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldList.get(oldItemPosition).equals(newList.get(newItemPosition));
    }

    @Nullable
    @Override
    public Object getChangePayload(int oldItemPosition, int newItemPosition) {
        //you can return particular field for changed item.
        return super.getChangePayload(oldItemPosition, newItemPosition);
    }
}
