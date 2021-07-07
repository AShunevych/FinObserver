package com.ashunevich.finobserver.data;

import com.ashunevich.finobserver.data.TransactionStatisticItem;

public interface TransactionStatisticListener {
    void onReturned(TransactionStatisticItem item);
}
