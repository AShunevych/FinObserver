package com.ashunevich.finobserver.transactions;

public interface TransactionStatisticListener {
    void onReturned(TransactionStatisticItem item);
}
