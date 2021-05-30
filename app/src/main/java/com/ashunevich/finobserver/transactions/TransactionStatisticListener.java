package com.ashunevich.finobserver.transactions;

interface TransactionStatisticListener {
    void onReturned(TransactionStatisticItem item);
}
