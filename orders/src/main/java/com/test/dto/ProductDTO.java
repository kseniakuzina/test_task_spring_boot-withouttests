package com.test.dto;

public class ProductDTO {
    private Long article_number;
    private String product_name;
    private Long quantity;
    private Double cost;

    public ProductDTO(Long article_number, String product_name, Long quantity, Double cost) {
        this.article_number = article_number;
        this.product_name = product_name;
        this.quantity = quantity;
        this.cost = cost;
    }

    public Long getArticle_number() {
        return article_number;
    }

    public String getProduct_name() {
        return product_name;
    }

    public Long getQuantity() {
        return quantity;
    }

    public Double getCost() {
        return cost;
    }
}
