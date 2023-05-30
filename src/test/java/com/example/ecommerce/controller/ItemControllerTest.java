package com.example.ecommerce.controller;

import com.example.ecommerce.TestUtils;
import com.example.ecommerce.entity.Item;
import com.example.ecommerce.repository.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hibernate.validator.internal.util.Contracts.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class ItemControllerTest {

    private ItemController itemController;
    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setUp(){
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
        Item item = new Item();
        item.setId(1L);
        item.setName("Playstation 5");
        BigDecimal price = BigDecimal.valueOf(499.99);
        item.setPrice(price);
        item.setDescription("A Nice Console");

        Item item2 = new Item();
        item2.setId(2L);
        item2.setName("Xbox One");
        BigDecimal price2 = BigDecimal.valueOf(299.99);
        item2.setPrice(price2);
        item2.setDescription("A Nice Console");


        List<Item> itemList = Arrays.asList(item, item2);
        when(itemRepository.findAll()).thenReturn(itemList);
        when(itemRepository.findById(1L)).thenReturn(java.util.Optional.of(item));
        when(itemRepository.findByName("Xbox One")).thenReturn(Collections.singletonList(item2));
    }

    @Test
    public void getItemstest(){
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(2, items.size());
    }

    @Test
    public void getItemById(){
        ResponseEntity<Item> response = itemController.getItemById(1L);
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        Item item = response.getBody();
        assertNotNull(item);
        assertEquals("Playstation 5", item.getName());
        assertEquals(BigDecimal.valueOf(499.99), item.getPrice());
        assertEquals("A Nice Console", item.getDescription());
    }

    @Test
    public void getItemByIdWhenItemNotExists(){
        ResponseEntity<Item> response = itemController.getItemById(3L);
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemByName(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Xbox One");
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        List<Item> items = response.getBody();
        assertNotNull(items);
        assertEquals(1, items.size());
        Item item = items.get(0);
        assertEquals("Xbox One", item.getName());
        assertEquals(BigDecimal.valueOf(299.99), item.getPrice());
        assertEquals("A Nice Console", item.getDescription());
    }

    @Test
    public void getItemByNameWhenItemNotExists(){
        ResponseEntity<List<Item>> response = itemController.getItemsByName("Nintendo Switch");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

}
