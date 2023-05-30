package com.example.ecommerce.controller;

import com.example.ecommerce.TestUtils;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.model.requests.ModifyCartRequest;
import com.example.ecommerce.repository.CartRepository;
import com.example.ecommerce.repository.ItemRepository;
import com.example.ecommerce.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Optional;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CartControllerTest {

    private CartController cartController;
    private UserRepository userRepository = mock(UserRepository.class);
    private CartRepository cartRepository = mock(CartRepository.class);
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        User user = new User();
        user.setId(1L);
        user.setUsername("userTest");
        user.setPassword("passwordTest");
        Cart cart = new Cart();
        user.setCart(cart);
        when(userRepository.findByUsername("userTest")).thenReturn(user);

        Item item = new Item();
        item.setId(1L);
        item.setName("GameBoy");
        BigDecimal price = BigDecimal.valueOf(49.99);
        item.setPrice(price);
        item.setDescription("A Nice Portable Console");
        when(itemRepository.findById(1L)).thenReturn(Optional.of(item));
    }

    @Test
    public void addToCartTest(){
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("userTest");
        request.setItemId(1L);
        request.setQuantity(2);
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().getItems().size());
    }

    @Test
    public void addToCartWhenUserInvalid() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("invalidUser");
        request.setItemId(1L);
        request.setQuantity(2);
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void addToCartWhenInvalidItem() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("userTest");
        request.setItemId(3L);
        request.setQuantity(2);
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromcart() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("userTest");
        request.setItemId(1L);
        request.setQuantity(2);
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cart = response.getBody();

        assertEquals(2, cart.getItems().size());
        assertEquals(BigDecimal.valueOf(99.98), cart.getTotal());

        ModifyCartRequest modifyRequest = new ModifyCartRequest();
        modifyRequest.setUsername("userTest");
        modifyRequest.setItemId(1L);
        modifyRequest.setQuantity(2);
        ResponseEntity<Cart> modifyResponse = cartController.removeFromcart(modifyRequest);

        assertNotNull(response);
        assertEquals(200, modifyResponse.getStatusCodeValue());
        Cart modifiedCart = modifyResponse.getBody();
        assertEquals(0, modifiedCart.getItems().size());
        assertEquals(0, BigDecimal.valueOf(0.00).compareTo(modifiedCart.getTotal()));
    }

    @Test
    public void removeFromcartWhenInvalidUser() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("userInvalid");
        request.setItemId(1L);
        request.setQuantity(2);
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void removeFromcartWhenInvalidItemId() {
        ModifyCartRequest request = new ModifyCartRequest();
        request.setUsername("userTest");
        request.setItemId(5L);
        request.setQuantity(2);
        ResponseEntity<Cart> response = cartController.addTocart(request);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
