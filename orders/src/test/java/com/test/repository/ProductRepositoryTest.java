package com.test.repository;


import com.test.entity.Order;
import com.test.entity.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void givenOrderObject_whenSave_thenReturnSavedOrder(){
        Product product1 = new Product(123L,"table",1L,5500.0);
        Product savedProduct = productRepository.save(product1);
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getId()).isGreaterThan(0);
    }

    @Test
    public void givenOrdersList_whenFindAll_thenReturnOrdersList(){
        Product product1 = new Product(123L,"table",1L,5500.0);
        Product product2 = new Product(12435L,"chair",4L,1500.0);
        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> productList = productRepository.findAll();

        assertThat(productList).isNotNull();
        assertThat(productList.size()).isEqualTo(2);
    }

    @Test
    public void givenOrderObject_whenFindById_thenReturnOrderObject(){
        Product product1 = new Product(123L,"table",1L,5500.0);
        productRepository.save(product1);

        Product productById = productRepository.findById(product1.getId()).get();

        assertThat(productById).isNotNull();
        assertThat(productById.getId()).isEqualTo(product1.getId());
    }

    @Test
    public void givenOrderId_whenFindAllByOrderId_thenReturnProductsList(){
        Order order = new Order("1234620240806",1500.0,"Nil","Moscow","Card","Deliver");
        orderRepository.save(order);
        Product product1 = new Product(123L,"table",1L,5500.0);
        product1.setOrder(order);
        Product product2 = new Product(12435L,"chair",4L,1500.0);
        product2.setOrder(order);
        productRepository.save(product1);
        productRepository.save(product2);

        List<Product> list = productRepository.findAllByOrder_Id(order.getId());

        assertThat(list).isNotNull();
        assertThat(list.size()).isEqualTo(2);
        assertThat(list.get(0).order_id()).isEqualTo(order.getId());
        assertThat(list.get(1).order_id()).isEqualTo(order.getId());

    }


}






