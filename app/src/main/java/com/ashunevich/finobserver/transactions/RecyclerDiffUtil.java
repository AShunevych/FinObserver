package com.ashunevich.finobserver.transactions;


import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

class RecyclerDiffUtil extends DiffUtil.Callback  {

    List<TransactionItem> oldList;

    public RecyclerDiffUtil(List<TransactionItem> oldList, List<TransactionItem>newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    List<TransactionItem> newList;

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
        return oldList.get(oldItemPosition).getItemID() == newList.get(newItemPosition).getItemID();
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
