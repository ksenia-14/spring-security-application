package com.example.springsecurityapplication.controllers;

import com.example.springsecurityapplication.models.Cart;
import com.example.springsecurityapplication.models.Order;
import com.example.springsecurityapplication.models.Person;
import com.example.springsecurityapplication.models.Product;
import com.example.springsecurityapplication.repositories.CartRepository;
import com.example.springsecurityapplication.repositories.OrderRepository;
import com.example.springsecurityapplication.responses.LoginResponse;
import com.example.springsecurityapplication.services.PersonService;
import com.example.springsecurityapplication.services.ProductService;
import com.example.springsecurityapplication.token.JWTTokenHelper;
import org.aspectj.weaver.ast.Or;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/user")
public class UserController {
    private final JWTTokenHelper jWTTokenHelper;
    private final PersonService personService;
    private final ProductService productService;
    private final OrderRepository orderRepository;
    private final CartRepository cartRepository;

    public UserController(JWTTokenHelper jWTTokenHelper, PersonService personService, ProductService productService, OrderRepository orderRepository, CartRepository cartRepository) {
        this.jWTTokenHelper = jWTTokenHelper;
        this.personService = personService;
        this.productService = productService;
        this.orderRepository = orderRepository;
        this.cartRepository = cartRepository;
    }

    @GetMapping("/product/add_cart/{id}")
    public ResponseEntity<?> addToCart(HttpServletRequest request, @PathVariable("id") int idProduct) {
        String authToken = jWTTokenHelper.getToken(request);
        String login = jWTTokenHelper.getUsernameFromToken(authToken);
        Person person = personService.findByLogin(login);
        Product product = productService.getProductId(idProduct);
        Cart cart = new Cart(person, product);
        cartRepository.save(cart);
        return ResponseEntity.ok("Ok");
    }

    @GetMapping("/product/cart")
    public ResponseEntity<?> getCart(HttpServletRequest request) {
        String authToken = jWTTokenHelper.getToken(request);
        String login = jWTTokenHelper.getUsernameFromToken(authToken);
        Person person = personService.findByLogin(login);

        List<Cart> productCart =  cartRepository.findByPersonId(person.getId());

        return ResponseEntity.ok(productCart);
    }

    @GetMapping("/product/delete_cart/{id}")
    public ResponseEntity<?> deleteToCart(HttpServletRequest request, @PathVariable("id") int idCart) {
        cartRepository.deleteById(idCart);
        return ResponseEntity.ok("Ok");
    }

    @GetMapping("/product/make_order")
    public ResponseEntity<?> makeOrder(HttpServletRequest request) {
        String authToken = jWTTokenHelper.getToken(request);
        String login = jWTTokenHelper.getUsernameFromToken(authToken);
        Person person = personService.findByLogin(login);

        List<Cart> cartList =  cartRepository.findByPersonId(person.getId());

        List<Product> productsList = new ArrayList<>();
        // Получаем продукты из корзины по id
        for (Cart cart: cartList) {
            productsList.add(productService.getProductId(cart.getProduct().getId()));
        }

        float price = 0;
        for (Product product: productsList){
            price += product.getPrice();
        }

        String uuid = UUID.randomUUID().toString();
        for (Product product: productsList){
            Order newOrder = new Order(product, person, (float) product.getPrice(), uuid, "В обработке");
            orderRepository.save(newOrder);
            cartRepository.deleteCartByProductId(product.getId());
        }

        return ResponseEntity.ok("Ok");
    }

    @GetMapping("/product/orders")
    public ResponseEntity<?> getOrders(HttpServletRequest request) {
        String authToken = jWTTokenHelper.getToken(request);
        String login = jWTTokenHelper.getUsernameFromToken(authToken);
        Person person = personService.findByLogin(login);

        List<Order> orderList = orderRepository.findByPerson(person);

        return ResponseEntity.ok(orderList);
    }
}
