package com.farmabook.farmAbook.dto;

import java.util.List;

public class PaymentUpdateRequest {
    private List<Long> workerIds;
    private boolean paymentDone;

    // getters & setters
    public List<Long> getWorkerIds() { return workerIds; }
    public void setWorkerIds(List<Long> workerIds) { this.workerIds = workerIds; }
    public boolean isPaymentDone() { return paymentDone; }
    public void setPaymentDone(boolean paymentDone) { this.paymentDone = paymentDone; }
}
