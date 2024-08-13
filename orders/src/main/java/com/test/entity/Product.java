package com.test.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long article_number;
    private String product_name;
    private Long quantity;
    private Double cost;

    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    public Product(Long article_number, String product_name, Long quantity, Double cost) {
        this.article_number = article_number;
        this.product_name = product_name;
        this.quantity = quantity;
        this.cost = cost;

    }
    public Long order_id(){
        return order.getId();
    }

    public Product(){}
    public String toString(){
        return article_number.toString() + " " + product_name;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getArticle_number() {
        return article_number;
    }

    public void setArticle_number(Long article_number) {
        this.article_number = article_number;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }



}