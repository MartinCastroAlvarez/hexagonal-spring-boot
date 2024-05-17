package com.martincastroalvarez.hex.hex.adapters.web;

import com.martincastroalvarez.hex.hex.adapters.dto.ProductDTO;
import com.martincastroalvarez.hex.hex.domain.models.Product;
import com.martincastroalvarez.hex.hex.domain.services.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateProduct() {
        ProductRequest request = new ProductRequest();
        request.setName("Test Product");
        request.setPrice(10.0);
        Product product = new Product();
        product.setId(1);
        product.setName(request.getName());
        product.setPrice(request.getPrice());
        when(productService.createProduct(request.getName(), request.getPrice())).thenReturn(product);
        ResponseEntity<ProductDTO> responseEntity = productController.createProduct(request);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
    }

    @Test
    void testDeleteProduct() {
        Integer productId = 1;
        ResponseEntity<Void> responseEntity = productController.deleteProduct(productId);
        verify(productService, times(1)).deleteProduct(productId);
        assertEquals(HttpStatus.NO_CONTENT, responseEntity.getStatusCode());
    }

    @Test
    void testGetProduct() {
        Integer productId = 1;
        Product product = new Product();
        product.setId(productId);
        when(productService.getProduct(productId)).thenReturn(product);
        ResponseEntity<ProductDTO> responseEntity = productController.getProduct(productId);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
    }
}