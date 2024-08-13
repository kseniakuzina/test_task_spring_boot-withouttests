package com.test.dto;


import static java.lang.Math.round;

public class OrderDTO {
    private String receiver;
    private String address;
    private String payment_type;
    private String delivery_type;
    private ProductDTO[] mas;

    public OrderDTO(String receiver,String address, String payment_type, String delivery_type, ProductDTO[] mas) {
        this.address = address;
        this.receiver = receiver;
        this.payment_type = payment_type;
        this.delivery_type = delivery_type;
        this.mas = mas;
    }

    public Double amount(){
        double amount = 0.0;
        for(ProductDTO productDTO: mas){
            amount += productDTO.getQuantity()*productDTO.getCost();
        }
        return Math.round (amount * 100.0) / 100.0;
    }

    public ProductDTO[] getMas() {
        return mas;
    }

    public String getReceiver() {
        return receiver;
    }

    public String getAddress() {
        return address;
    }

    public String getPayment_type() {
        return payment_type;
    }


    public String getDelivery_type() {
        return delivery_type;
    }

}
