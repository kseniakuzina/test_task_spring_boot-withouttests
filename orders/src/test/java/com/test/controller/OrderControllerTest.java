package com.test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.dto.OrderDTO;
import com.test.dto.ProductDTO;
import com.test.entity.Order;
import com.test.entity.Product;
import com.test.repository.OrderRepository;
import com.test.repository.ProductRepository;
import com.test.service.OrderService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.client.RestTemplate;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void givenListOfOrders_whenGetAllOrders_thenReturnOrdersList() throws Exception{
        productRepository.deleteAll();
        orderRepository.deleteAll();

        List<Order> orders = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        Order order1 = new Order("1234520240806",11500.0,"Mark","Moscow","Card","Deliver");
        orders.add(order1);

        Product product1 = new Product(123L,"lipstick",1L,5500.0);
        product1.setOrder(order1);
        Product product2 = new Product(12435L,"chair",4L,1500.0);
        product2.setOrder(order1);
        products.add(product1);
        products.add(product2);


        Order order2 = new Order("4543220240806",5500.0,"John","Moscow","Card","Deliver");
        orders.add(order2);

        Product product3 = new Product(1243L,"table",4L,1500.0);
        product3.setOrder(order2);
        products.add(product3);

        orderRepository.saveAll(orders);
        productRepository.saveAll(products);


        ResultActions response = mockMvc.perform(get("/orders"));


        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.size()",
                        is(orders.size())))
                .andExpect(jsonPath("$[0].first.receiver",
                        is(order1.getReceiver())))
                .andExpect(jsonPath("$[1].second[0].product_name",
                        is(product3.getProduct_name())));

    }

    @Test
    public void givenID_whenGetOrderById_thenReturnOrder() throws Exception{
        productRepository.deleteAll();
        orderRepository.deleteAll();

        List<Product> products = new ArrayList<>();
        Order order = new Order("1234520240806",11500.0,"Mark","Moscow","Card","Deliver");
        orderRepository.save(order);

        Product product1 = new Product(123L,"lipstick",1L,5500.0);
        product1.setOrder(order);
        products.add(product1);
        Product product2 = new Product(12435L,"chair",4L,1500.0);
        product2.setOrder(order);
        products.add(product2);
        productRepository.saveAll(products);

        ResultActions response = mockMvc.perform(get("/orders/{id}",order.getId()));

        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.first.address",
                        is(order.getAddress())))
                .andExpect(jsonPath("$.first.receiver",
                        is(order.getReceiver())))
                .andExpect(jsonPath("$.first.payment_type",
                        is(order.getPayment_type())))
                .andExpect(jsonPath("$.first.delivery_type",
                        is(order.getDelivery_type())))
                .andExpect(jsonPath("$.second.size()",
                        is(products.size())))
                .andExpect(jsonPath("$.second[0].product_name",
                        is(product1.getProduct_name())));
    }

    @Test
    public void givenDateAndAmount_whenGetOrderByDateAndAmount_thenReturnOrder() throws Exception{
        productRepository.deleteAll();
        orderRepository.deleteAll();

        List<Order> orders = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        Order order1 = new Order("1234520240806",11500.0,"Mark","Moscow","Card","Deliver");
        LocalDate date1 = LocalDate.of(Integer.parseInt("2024"), Integer.parseInt("8"), Integer.parseInt("1"));
        order1.setDate(date1);
        orders.add(order1);


        Product product1 = new Product(123L,"lipstick",1L,5500.0);
        product1.setOrder(order1);
        Product product2 = new Product(12435L,"chair",4L,1500.0);
        product2.setOrder(order1);
        products.add(product1);
        products.add(product2);


        Order order2 = new Order("4543220240806",5500.0,"John","Moscow","Card","Deliver");
        LocalDate date2 = LocalDate.of(Integer.parseInt("2024"), Integer.parseInt("8"), Integer.parseInt("5"));
        order2.setDate(date2);
        orders.add(order2);

        Product product3 = new Product(1243L,"table",4L,1500.0);
        product3.setOrder(order2);
        products.add(product3);

        orderRepository.saveAll(orders);
        productRepository.saveAll(products);


        ResultActions response = mockMvc.perform(get("/orders/{date}/{amount}", order1.stringDate(),order1.getAmount() - 1.0));


        response.andExpect(status().isOk())
                .andDo(print())

                .andExpect(jsonPath("$.first.receiver",
                        is(order1.getReceiver())))
                .andExpect(jsonPath("$.second[0].product_name",
                        is(product1.getProduct_name())));

    }

    @Test
    public void givenProductNameAndPeriod_whenGetOrdersByProductAndPeriod_thenReturnOrdersList() throws Exception{
        productRepository.deleteAll();
        orderRepository.deleteAll();

        List<Order> orders = new ArrayList<>();
        List<Product> products = new ArrayList<>();

        Order order1 = new Order("1234520240806",11500.0,"Mark","Moscow","Card","Deliver");
        LocalDate date1 = LocalDate.of(Integer.parseInt("2024"), Integer.parseInt("8"), Integer.parseInt("1"));
        order1.setDate(date1);
        orders.add(order1);

        Product product1 = new Product(123L,"lipstick",1L,5500.0);
        product1.setOrder(order1);
        Product product2 = new Product(5555L,"chair",4L,1500.0);
        product2.setOrder(order1);
        products.add(product1);
        products.add(product2);


        Order order2 = new Order("4543220240806",5500.0,"John","Moscow","Card","Deliver");
        LocalDate date2 = LocalDate.of(Integer.parseInt("2024"), Integer.parseInt("8"), Integer.parseInt("5"));
        order2.setDate(date2);
        orders.add(order2);

        Product product3 = new Product(1243L,"table",4L,1500.0);
        product3.setOrder(order2);
        products.add(product3);

        orderRepository.saveAll(orders);
        productRepository.saveAll(products);


        ResultActions response = mockMvc.perform(get("/orders/{article_number}/{dateFrom}/{dateTo}",5555L,"2024-08-04","2024-08-06"));


        response.andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$[0].first.receiver",
                        is(order2.getReceiver())))
                .andExpect(jsonPath("$[0].second[0].product_name",
                        is(product3.getProduct_name())));

    }
    //new branch
    /*
    //данный тест работает только при запущенном сервисе генерации номеров заказов
    @Test
    public void givenOrderObject_whenCreateOrder_thenReturnSavedOrder() throws Exception{
        ProductDTO productDTO1 = new ProductDTO(123L,"carrot",1L,5500.0);
        ProductDTO productDTO2 = new ProductDTO(12435L,"chair",4L,1500.0);

        ProductDTO[] mas = new ProductDTO[]{productDTO1, productDTO2};
        OrderDTO dto = new OrderDTO("Mike", "Moscow", "Card","Deliver",mas);

        ResultActions response = mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));


        response.andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.first.address",
                        is(dto.getAddress())))
                .andExpect(jsonPath("$.first.receiver",
                        is(dto.getReceiver())))
                .andExpect(jsonPath("$.first.payment_type",
                        is(dto.getPayment_type())))
                .andExpect(jsonPath("$.first.delivery_type",
                        is(dto.getDelivery_type())))
                .andExpect(jsonPath("$.second.size()",
                        is(mas.length)))
                .andExpect(jsonPath("$.second[0].product_name",
                        is(productDTO1.getProduct_name())));
    }
    */
}
