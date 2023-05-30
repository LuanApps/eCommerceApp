package com.example.ecommerce.controller;

import com.example.ecommerce.TestUtils;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.entity.UserOrder;
import com.example.ecommerce.repository.OrderRepository;
import com.example.ecommerce.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderControllerTest {

    private OrderController orderController;
    private OrderRepository orderRepository = mock(OrderRepository.class);
    private UserRepository userRepository = mock(UserRepository.class);

    @Before
    public void setUp() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);

        List<Item> items = new ArrayList<Item>();

        Item item = new Item();
        item.setId(1L);
        item.setName("Playstation 5");
        BigDecimal price = BigDecimal.valueOf(499.99);
        item.setPrice(price);
        item.setDescription("A Nice Console");
        items.add(item);

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Xbox One");
        BigDecimal price2 = BigDecimal.valueOf(299.99);
        item2.setPrice(price2);
        item2.setDescription("A Nice Console");
        items.add(item2);

        User user = new User();
        Cart cart = new Cart();
        user.setId(1L);
        user.setUsername("userTest");
        user.setPassword("passwordTest");
        cart.setId(1L);
        cart.setUser(user);
        cart.setItems(items);
        BigDecimal total = BigDecimal.valueOf(799.98);
        cart.setTotal(total);
        user.setCart(cart);
        when(userRepository.findByUsername("userTest")).thenReturn(user);
    }

    @Test
    public void submitUserOrdertest(){
        ResponseEntity<UserOrder> response = orderController.submit("userTest");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        UserOrder order = response.getBody();
        assertNotNull(order);
        assertEquals(2, order.getItems().size());
        assertEquals(BigDecimal.valueOf(799.98), order.getTotal());
        assertEquals("Playstation 5", order.getItems().get(0).getName());
    }

    @Test
    public void submitOrderInvalidUserTest(){
        ResponseEntity<UserOrder> response = orderController.submit("userInvalid");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersForUserTest(){

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("userTest");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> userOrders = response.getBody();

        assertNotNull(userOrders);
        assertEquals(0, userOrders.size());
    }

    @Test
    public void getOrdersFrUserInvalidUserTest(){
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("invalidUser");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
