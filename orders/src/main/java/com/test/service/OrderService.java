package com.test.service;

import com.test.dto.OrderDTO;
import com.test.dto.ProductDTO;
import com.test.entity.Order;
import com.test.entity.Product;
import com.test.repository.OrderRepository;
import com.test.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private ProductRepository productRepository;
    public RestTemplate restTemplate;

    @Value("${spring.application.microservicegeneration.url}")
    private String generationUrl;


    public OrderService(OrderRepository orderRepository,ProductRepository productRepository,RestTemplate restTemplate){
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.restTemplate = restTemplate;
    }

    public Pair<Order, List<Product>> createOrder(OrderDTO dto) {
        String num = restTemplate.getForObject(generationUrl, String.class);
        Order savedOrder = new Order();
        savedOrder.setOrder_number(num);
        savedOrder.setAmount(dto.amount());
        savedOrder.setReceiver(dto.getReceiver());
        savedOrder.setAddress(dto.getAddress());
        savedOrder.setPayment_type(dto.getPayment_type());
        savedOrder.setDelivery_type(dto.getDelivery_type());
        savedOrder.setDate(LocalDate.now());

        savedOrder = orderRepository.save(savedOrder);
        ProductDTO[] mas = dto.getMas();
        List<Product> products = new ArrayList<>();
        for(ProductDTO productDTO: mas){
            Product product = new Product(productDTO.getArticle_number(),productDTO.getProduct_name(),productDTO.getQuantity(),productDTO.getCost());
            product.setOrder(savedOrder);
            products.add(productRepository.save(product));
        }
        return Pair.of(savedOrder,products);
    }

    public List<Pair<Order, List<Product>>> getAllOrders() {
        List<Order> orders = orderRepository.findAll();
        List<Pair<Order, List<Product>>> pairs = new ArrayList<>();
        for (Order order: orders){
            Long id = order.getId();
            List<Product> products = productRepository.findAllByOrder_Id(id);
            Pair<Order, List<Product>> pair = Pair.of(order,products);
            pairs.add(pair);
        }
        return pairs;
    }
    public Pair<Order, List<Product>> getOrderById(Long id) {
        Optional<Order> ord = orderRepository.findById(id);
        if (ord.isPresent()){
            Order order = ord.get();
            List<Product> products = productRepository.findAllByOrder_Id(id);
            return Pair.of(order,products);
        }
        else return Pair.of(new Order(),new ArrayList<>());
    }

    public Pair<Order, List<Product>> getOrderByDateAndAmount(String tDate, Double tAmount) {
        List<Order> orders = orderRepository.findAll();
        Pair<Order, List<Product>> pair;
        Order order;
        List<Product> products;
        for(int i = 0; i < orders.size(); i++){
            order = orders.get(i);
            Long id = order.getId();
            LocalDate ourDate = order.getDate();
            LocalDate tempDate = LocalDate.parse(tDate);
            Double amount = order.getAmount();
            if (tempDate.equals(ourDate) && (amount > tAmount)){
                products = productRepository.findAllByOrder_Id(id);
                pair = Pair.of(order,products);
                return pair;
            }
        }
        return Pair.of(new Order(),new ArrayList<>());
    }
    public List<Pair<Order, List<Product>>> getOrdersByProductAndPeriod(Long article_number, String dateFrom, String dateTo) {
        List<Order> orders = orderRepository.findAll();
        List<Pair<Order, List<Product>>> pairs = new ArrayList<>();
        for (Order order: orders){
            Long id = order.getId();
            LocalDate date1 = LocalDate.parse(dateFrom);
            LocalDate date2 = LocalDate.parse(dateTo);
            LocalDate ourDate = order.getDate();
            if (ourDate.isAfter(date1) && ourDate.isBefore(date2)){
                List<Product> products = productRepository.findAllByOrder_Id(id);
                boolean key = true;
                for(int i = 0; i< products.size() && key;i++){
                    if(products.get(i).getArticle_number().equals(article_number)) key = false;
                }
                if (key){
                    Pair<Order, List<Product>> pair = Pair.of(order,products);
                    pairs.add(pair);
                }
            }
        }
        return pairs;
    }

}