package com.test.service;

import com.test.dto.OrderDTO;
import com.test.dto.ProductDTO;
import com.test.entity.Order;
import com.test.entity.Product;
import com.test.repository.OrderRepository;
import com.test.repository.ProductRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.util.Pair;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;


@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private OrderService orderService;

    @Value("${spring.application.microservicegeneration.url}")
    private String generationUrl;

    @Test
    public void ShouldFindAllOrders(){
        Pair<Order, List<Product>> orderWithProducts = getOrderWithProducts();
        Order order2 = new Order("4543220240806",2345.45,"Alex","Moscow","Card","Deliver");
        order2.setId(2l);
        Product product3 = new Product(453123L,"bed",2L,15500.0);
        product3.setOrder(order2);

        Mockito.when(orderRepository.findAll()).thenReturn(List.of(orderWithProducts.getFirst(),order2));
        Mockito.when(productRepository.findAllByOrder_Id(1L)).thenReturn(orderWithProducts.getSecond());
        Mockito.when(productRepository.findAllByOrder_Id(2L)).thenReturn(List.of(product3));

        List<Pair<Order, List<Product>>> orders = orderService.getAllOrders();

        Assertions.assertThat(orders).isNotNull();
        Assertions.assertThat(orders.size()).isEqualTo(2);
        Assertions.assertThat(orders.get(0).getSecond().size()).isGreaterThan(0);
        Assertions.assertThat(orders.get(1).getSecond().size()).isGreaterThan(0);

    }
    @Test
    public void ShouldFindOrderById(){
        Pair<Order, List<Product>> orderWithProducts = getOrderWithProducts();
        Mockito.when(orderRepository.findById(1L)).thenReturn(Optional.of(orderWithProducts.getFirst()));
        Mockito.when(productRepository.findAllByOrder_Id(1L)).thenReturn(orderWithProducts.getSecond());

        Pair<Order, List<Product>> pair = orderService.getOrderById(1L);

        Assertions.assertThat(pair).isNotNull();
        Assertions.assertThat(pair.getFirst()).isNotNull();
        Assertions.assertThat(pair.getFirst().getId()).isEqualTo(1L);
        Assertions.assertThat(pair.getSecond()).isNotNull();
        Assertions.assertThat(pair.getSecond().size()).isGreaterThan(0);

    }
    @Test
    public void ShouldFindOrderByDateAndAmount(){
        Pair<Order, List<Product>> orderWithProducts = getOrderWithProducts();
        Order order = orderWithProducts.getFirst();
        LocalDate date = LocalDate.of(Integer.parseInt("2024"), Integer.parseInt("8"), Integer.parseInt("1"));
        order.setDate(date);

        Order order2 = new Order("4543220240806",5500.0,"Alex","Moscow","Card","Deliver");
        order2.setId(2l);
        LocalDate date1 = LocalDate.of(Integer.parseInt("2024"), Integer.parseInt("8"), Integer.parseInt("5"));
        order2.setDate(date1);
        Product product3 = new Product(4444L,"bed",1L,5500.0);
        product3.setOrder(order2);


        Mockito.when(orderRepository.findAll()).thenReturn(List.of(order,order2));
        Mockito.when(productRepository.findAllByOrder_Id(2L)).thenReturn(List.of(product3));

        Pair<Order, List<Product>> pair = orderService.getOrderByDateAndAmount(order2.stringDate(),order2.getAmount() - 1.0);

        Assertions.assertThat(pair).isNotNull();
        Assertions.assertThat(pair.getFirst()).isEqualTo(order2);
        Assertions.assertThat(pair.getSecond()).isEqualTo(List.of(product3));

    }

    @Test
    public void ShouldFindOrderByProductNameAndPeriod(){
        Pair<Order, List<Product>> orderWithProducts = getOrderWithProducts();
        Order order = orderWithProducts.getFirst();
        LocalDate date = LocalDate.of(Integer.parseInt("2024"), Integer.parseInt("8"), Integer.parseInt("1"));
        order.setDate(date);

        Order order2 = new Order("4543220240806",5500.0,"Alex","Moscow","Card","Deliver");
        order2.setId(2l);
        LocalDate date1 = LocalDate.of(Integer.parseInt("2024"), Integer.parseInt("8"), Integer.parseInt("5"));
        order2.setDate(date1);
        Product product3 = new Product(4444L,"bed",1L,5500.0);
        product3.setOrder(order2);


        Mockito.when(orderRepository.findAll()).thenReturn(List.of(order,order2));
        Mockito.when(productRepository.findAllByOrder_Id(2L)).thenReturn(List.of(product3));

        List<Pair<Order, List<Product>>> list = orderService.getOrdersByProductAndPeriod(12435L,"2024-08-04","2024-08-06");

        Assertions.assertThat(list).isNotNull();
        Assertions.assertThat(list.size()).isGreaterThan(0);
        Assertions.assertThat(list.get(0).getFirst()).isEqualTo(order2);
        Assertions.assertThat(list.get(0).getSecond()).isEqualTo(List.of(product3));
    }

    private Pair<Order, List<Product>> getOrderWithProducts(){
        Order order1 = new Order("1234520240806",11500.0,"Mike","Moscow","Card","Deliver");
        order1.setId(1l);
        Product product1 = new Product(123L,"table",1L,5500.0);
        product1.setOrder(order1);
        Product product2 = new Product(12435L,"chair",4L,1500.0);
        product2.setOrder(order1);
        return Pair.of(order1, List.of(product1,product2));
    }


    @Test
    public void ShouldCreateOrder(){
        ProductDTO[] mas = new ProductDTO[]{
                new ProductDTO(123L,"table",1L,5500.0),
        };
        OrderDTO dto = new OrderDTO("Mike", "Moscow", "Card","Deliver",mas);

        String num = "1234520240806";
        Order savedOrder = new Order();
        savedOrder.setOrder_number(num);
        savedOrder.setAmount(dto.amount());
        savedOrder.setReceiver(dto.getReceiver());
        savedOrder.setAddress(dto.getAddress());
        savedOrder.setPayment_type(dto.getPayment_type());
        savedOrder.setDelivery_type(dto.getDelivery_type());
        savedOrder.setDate(LocalDate.now());

        Order orderWithId = savedOrder;
        orderWithId.setId(1l);
        Product product1 = new Product(123L,"table",1L,5500.0);
        product1.setOrder(orderWithId);

        Mockito.when(orderRepository.save(any(Order.class))).thenReturn(orderWithId);
        Mockito.when(productRepository.save(any(Product.class))).thenReturn(product1);
        Mockito.when(restTemplate.getForObject(generationUrl, String.class)).thenReturn(num);

        Pair<Order, List<Product>> pair = orderService.createOrder(dto);

        Assertions.assertThat(pair).isNotNull();
        Assertions.assertThat(pair.getFirst()).isNotNull();
        Assertions.assertThat(pair.getFirst()).isEqualTo(orderWithId);
        Assertions.assertThat(pair.getSecond()).isNotNull();
        Assertions.assertThat(pair.getSecond()).isEqualTo(List.of(product1));
        Assertions.assertThat(pair.getSecond().size()).isGreaterThan(0);
    }


}
