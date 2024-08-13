package com.test.repository;

import com.test.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
public class OrderRepositoryTest {
    @Autowired
    private OrderRepository orderRepository;

    private Order order1;
    @BeforeEach
    public void setup(){
         order1 = new Order("1234520240806",11500.0,"Mike","Moscow","Card","Deliver");

    }
    @Test
    public void givenOrderObject_whenSave_thenReturnSavedOrder(){
        Order savedOrder = orderRepository.save(order1);

        assertThat(savedOrder).isNotNull();
        assertThat(savedOrder.getId()).isGreaterThan(0);
    }

    @Test
    public void givenOrdersList_whenFindAll_thenReturnOrdersList(){
        Order order2 = new Order("1234620240806",1500.0,"Nil","Moscow","Card","Deliver");
        orderRepository.save(order1);
        orderRepository.save(order2);

        List<Order> orderList = orderRepository.findAll();

        assertThat(orderList).isNotNull();
        assertThat(orderList.size()).isEqualTo(2);
    }

    @Test
    public void givenOrderObject_whenFindById_thenReturnOrderObject(){
        orderRepository.save(order1);

        Order orderById = orderRepository.findById(order1.getId()).get();

        assertThat(orderById).isNotNull();
        assertThat(orderById.getId()).isEqualTo(order1.getId());
    }
}
