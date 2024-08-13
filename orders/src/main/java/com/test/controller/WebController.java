package com.test.controller;

import com.test.dto.OrderDTO;
import com.test.dto.ProductDTO;
import com.test.entity.Order;
import com.test.entity.Product;
import com.test.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order_service")
public class WebController {
    @Autowired
    OrderService orderService;

    @GetMapping
    public String homePage(Model model){
        return "home";
    }


    @GetMapping("/add")
    public String addingPage(Model model){
        return "adding";
    }
    @PostMapping("/add")
    public String createOrder(Model model, @RequestParam String receiver, @RequestParam String address,@RequestParam String payment_type,@RequestParam String delivery_type, @RequestParam(value="article_number") Long[] articles,@RequestParam(value="product_name") String[] products, @RequestParam(value="quantity") Long[] quantities,@RequestParam(value="cost") Double[] costs){
        ProductDTO[] mas = new ProductDTO[costs.length];
        for (int i = 0; i < costs.length; i++){
            ProductDTO productDTO = new ProductDTO(articles[i],products[i],quantities[i],costs[i]);
            mas[i] = productDTO;
        }
        OrderDTO dto = new OrderDTO(receiver,address,payment_type,delivery_type,mas);
        orderService.createOrder(dto);
        return "redirect:/order_service";
    }


    @GetMapping("/get_by_id")
    public String getByIdPage(Model model){
        return "getting_by_id";
    }
    @PostMapping("/get_by_id")
    public String postId(@RequestParam long id, Model model){
        return "redirect:/order_service/get_by_id/" + Long.toString(id);
    }

    @GetMapping("/get_by_id/{id}")
    public String getByIdPage2(@PathVariable(value = "id") Long id, Model model){
        Pair<Order, List<Product>> pair = orderService.getOrderById(id);
        model.addAttribute("pair", pair);
        return "getting_by_id_2";
    }


    @GetMapping("/get_by_date_and_amount")
    public String getByDateAndAmountPage(Model model){
        model.addAttribute("title", "Главная страница");
        return "getting_by_date_and_amount";
    }
    @PostMapping("/get_by_date_and_amount")
    public String postDateAndAmount(@RequestParam String date,@RequestParam double amount, Model model){
        return "redirect:/order_service/get_by_date_and_amount/" + date + "/" + amount;
    }

    @GetMapping("/get_by_date_and_amount/{date}/{amount}")
    public String getByDateAndAmountPage2(@PathVariable(value = "date") String date,@PathVariable(value = "amount") double amount, Model model){
        Pair<Order, List<Product>> pair = orderService.getOrderByDateAndAmount(date,amount);
        model.addAttribute("pair", pair);
        return "getting_by_date_and_amount_2";
    }

    @GetMapping("/get_by_product_and_period")
    public String getByProductAndPeriodPage(Model model){
        model.addAttribute("title", "Главная страница");
        return "getting_by_product_and_period";
    }
    @PostMapping("/get_by_product_and_period")
    public String postProductAndPeriod(@RequestParam Long product,@RequestParam String date1,@RequestParam String date2, Model model){
        return "redirect:/order_service/get_by_product_and_period/" + product + "/" + date1 + "/" + date2;
    }

    @GetMapping("/get_by_product_and_period/{product}/{date1}/{date2}")
    public String getByProductAndPeriodPage(@PathVariable(value = "product") Long product,@PathVariable(value = "date1") String date1, @PathVariable(value = "date2") String date2, Model model){

        List<Pair<Order, List<Product>>> list = orderService.getOrdersByProductAndPeriod(product,date1,date2);
        model.addAttribute("list", list);
        return "getting_by_product_and_period_2";
    }
}
