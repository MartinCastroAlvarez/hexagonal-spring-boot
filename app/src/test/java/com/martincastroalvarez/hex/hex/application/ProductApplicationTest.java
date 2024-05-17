package com.martincastroalvarez.hex.hex.application;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.Product;
import com.martincastroalvarez.hex.hex.domain.models.Purchase;
import com.martincastroalvarez.hex.hex.domain.models.Sale;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.ports.out.ProductRepository;
import com.martincastroalvarez.hex.hex.domain.ports.out.SaleRepository;
import com.martincastroalvarez.hex.hex.domain.ports.out.PurchaseRepository;
import com.martincastroalvarez.hex.hex.domain.ports.out.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageImpl;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class ProductApplicationTest {
    @Mock
    private ProductRepository productRepository;

    @Mock
    private SaleRepository saleRepository;

    @Mock
    private PurchaseRepository purchaseRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private ProductApplication productApplication;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testListProducts() throws HexagonalPaginationException {
        List<Product> productList = Collections.singletonList(new Product());
        when(productRepository.get(any(Pageable.class))).thenReturn(new PageImpl<>(productList));
        List<Product> result = productApplication.listProducts(1, 10, "id", true);
        assertEquals(productList, result);
        verify(productRepository, times(1)).get(any(Pageable.class));
    }

    @Test
    void testCreateProduct() throws HexagonalValidationException {
        String name = "Test Product";
        Double price = 10.0;
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);
        when(productRepository.save(any())).thenReturn(product);
        Product result = productApplication.createProduct(name, price);
        assertEquals(product, result);
        verify(productRepository, times(1)).save(any());
    }

    @Test
    void testGetProduct() throws HexagonalEntityNotFoundException {
        int productId = 1;
        Product product = new Product();
        when(productRepository.get(productId)).thenReturn(Optional.of(product));
        Product result = productApplication.getProduct(productId);
        assertEquals(product, result);
        verify(productRepository, times(1)).get(productId);
    }

    @Test
    void testDeleteProduct() throws HexagonalEntityNotFoundException {
        int productId = 1;
        Product product = new Product();
        when(productRepository.get(productId)).thenReturn(Optional.of(product));
        Product result = productApplication.deleteProduct(productId);
        assertEquals(product, result);
        assertEquals(false, product.getIsActive());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testUpdateProduct() throws HexagonalEntityNotFoundException, HexagonalValidationException {
        int productId = 1;
        String newName = "New Name";
        Double newPrice = 15.0;
        Product product = new Product();
        product.setIsActive(true);
        when(productRepository.get(productId)).thenReturn(Optional.of(product));
        when(productRepository.get(newName)).thenReturn(Optional.empty());
        Product result = productApplication.updateProduct(productId, newName, newPrice);
        assertEquals(newName, result.getName());
        assertEquals(newPrice, result.getPrice());
        verify(productRepository, times(1)).save(product);
    }

    @Test
    void testListSalesByProduct() throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        int productId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(90);
        LocalDateTime endDate = LocalDateTime.now();
        List<Sale> salesList = Collections.singletonList(new Sale());
        Product product = new Product();
        when(productRepository.get(eq(productId))).thenReturn(Optional.of(product));
        when(saleRepository.get(eq(product), eq(startDate), eq(endDate), any(Pageable.class))).thenReturn(new PageImpl<>(salesList));
        List<Sale> result = productApplication.listSalesByProduct(productId, startDate, endDate, 1, 10, "datetime", true);
        assertEquals(salesList, result);
        verify(saleRepository, times(1)).get(eq(product), eq(startDate), eq(endDate), any(Pageable.class));
    }

    @Test
    void testListSalesBySalesman() throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        int salesmanId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(90);
        LocalDateTime endDate = LocalDateTime.now();
        List<Sale> salesList = Collections.singletonList(new Sale());
        User salesman = new User();
        when(userRepository.get(eq(salesmanId))).thenReturn(Optional.of(salesman));
        when(saleRepository.get(eq(salesman), eq(startDate), eq(endDate), any(Pageable.class))).thenReturn(new PageImpl<>(salesList));
        List<Sale> result = productApplication.listSalesBySalesman(salesmanId, startDate, endDate, 1, 10, "datetime", true);
        assertEquals(salesList, result);
        verify(saleRepository, times(1)).get(eq(salesman), eq(startDate), eq(endDate), any(Pageable.class));
    }

    @Test
    void testListPurchasesByProduct() throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        int productId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(90);
        LocalDateTime endDate = LocalDateTime.now();
        List<Purchase> purchasesList = Collections.singletonList(new Purchase());
        Product product = new Product();
        when(productRepository.get(productId)).thenReturn(Optional.of(product));
        // Ensure all arguments in the method call use matchers
        when(purchaseRepository.get(eq(product), eq(startDate), eq(endDate), any(Pageable.class))).thenReturn(new PageImpl<>(purchasesList));
        List<Purchase> result = productApplication.listPurchasesByProduct(productId, startDate, endDate, 1, 10, "datetime", true);
        assertEquals(purchasesList, result);
        verify(purchaseRepository, times(1)).get(eq(product), eq(startDate), eq(endDate), any(Pageable.class));
    }

    @Test
    void testListPurchasesByProvider() throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        int providerId = 1;
        LocalDateTime startDate = LocalDateTime.now().minusDays(90);
        LocalDateTime endDate = LocalDateTime.now();
        List<Purchase> purchasesList = Collections.singletonList(new Purchase());
        User provider = new User();
        when(userRepository.get(eq(providerId))).thenReturn(Optional.of(provider));
        when(purchaseRepository.get(eq(provider), eq(startDate), eq(endDate), any(Pageable.class))).thenReturn(new PageImpl<>(purchasesList));
        List<Purchase> result = productApplication.listPurchasesByProvider(providerId, startDate, endDate, 1, 10, "datetime", true);
        assertEquals(purchasesList, result);
        verify(purchaseRepository, times(1)).get(eq(provider), eq(startDate), eq(endDate), any(Pageable.class));
    }
}
