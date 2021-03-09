package com.ashunevich.finobserver.dashboard;

import java.util.List;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DiffUtil;

class RecyclerViewDiffUtil extends DiffUtil.Callback {

    List<AccountItem> oldList;

    public RecyclerViewDiffUtil(List<AccountItem> oldList, List<AccountItem>newList) {
        this.oldList = oldList;
        this.newList = newList;
    }

    List<AccountItem> newList;

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
        return oldList.get(oldItemPosition).getAccountID() == newList.get(newItemPosition).getAccountID();
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
