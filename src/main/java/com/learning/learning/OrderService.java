package com.learning.learning;

public class OrderService {
    private PaymentService paymentService;

    public  OrderService(PaymentService paymentService){
        this.paymentService = paymentService;
    }

    public void placeOrder(){
        // var paymentService = new AzampayPaymentService();
        paymentService.processPayment(20);
    }
}
