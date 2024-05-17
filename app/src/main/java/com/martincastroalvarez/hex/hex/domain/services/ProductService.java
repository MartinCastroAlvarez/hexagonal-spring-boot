package com.martincastroalvarez.hex.hex.domain.services;

import com.martincastroalvarez.hex.hex.domain.exceptions.*;
import com.martincastroalvarez.hex.hex.domain.models.Product;
import com.martincastroalvarez.hex.hex.domain.models.Purchase;
import com.martincastroalvarez.hex.hex.domain.models.Sale;
import java.time.LocalDateTime;
import java.util.List;

public interface ProductService {
    Product getProduct(Integer productId) throws HexagonalEntityNotFoundException;
    Sale getSale(Integer saleID) throws HexagonalEntityNotFoundException;
    Purchase getPurchase(Integer purchaseId) throws HexagonalEntityNotFoundException;
    List<Product> listProducts(Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    List<Product> listProducts(String string, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException;
    Product createProduct(String name, Double price) throws HexagonalValidationException;
    Product deleteProduct(Integer productId) throws HexagonalEntityNotFoundException;
    Product updateProduct(Integer productId, String name, Double price) throws HexagonalEntityNotFoundException, HexagonalValidationException;
    Sale sellProduct(Integer salesmanId, Integer productId, Double amount, Double price) throws HexagonalEntityNotFoundException, HexagonalValidationException;
    List<Sale> listSales(LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException;
    List<Sale> listSalesByProduct(Integer productId, LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException;
    List<Sale> listSalesBySalesman(Integer salesmanId, LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException;
    Purchase purchaseProduct(Integer providerId, Integer productId, Double amount, Double cost) throws HexagonalEntityNotFoundException, HexagonalValidationException;
    List<Purchase> listPurchases(LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException;
    List<Purchase> listPurchasesByProduct(Integer productId, LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException;
    List<Purchase> listPurchasesByProvider(Integer providerId, LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException;
}
