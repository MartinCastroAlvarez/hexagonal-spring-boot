package com.martincastroalvarez.hex.hex.adapters.web;

import com.martincastroalvarez.hex.hex.adapters.dto.ProductDTO;
import com.martincastroalvarez.hex.hex.adapters.dto.PurchaseDTO;
import com.martincastroalvarez.hex.hex.adapters.dto.SaleDTO;
import com.martincastroalvarez.hex.hex.adapters.mappers.ProductMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.PurchaseMapper;
import com.martincastroalvarez.hex.hex.adapters.mappers.SaleMapper;
import com.martincastroalvarez.hex.hex.domain.models.Product;
import com.martincastroalvarez.hex.hex.domain.models.User;
import com.martincastroalvarez.hex.hex.domain.models.Purchase;
import com.martincastroalvarez.hex.hex.domain.models.Sale;
import com.martincastroalvarez.hex.hex.domain.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDateTime;
import java.util.stream.Collectors;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
public class ProductController extends HexagonalController {
    @Autowired
    private ProductService productService;

    @GetMapping("/auth/sales")
    public ResponseEntity<List<SaleDTO>> getSales(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        User user = getAuthenticatedUser();
        logger.info(String.format("Getting user sales: %s", user.getUsername()));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Sale> sales = productService.listSalesBySalesman(user.getId(), startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s sales", sales.size()));
        return ResponseEntity.ok(sales.stream().map(SaleMapper::toSaleDTO).collect(Collectors.toList()));
    }

    @GetMapping("/auth/purchases")
    public ResponseEntity<List<PurchaseDTO>> getPurchases(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        User user = getAuthenticatedUser();
        logger.info(String.format("Getting user purchases: %s", user.getUsername()));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Purchase> purchases = productService.listPurchasesByProvider(user.getId(), startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s purchases", purchases.size()));
        return ResponseEntity.ok(purchases.stream().map(PurchaseMapper::toPurchaseDTO).collect(Collectors.toList()));
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductDTO>> listProducts(
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc,
        @RequestParam(required = false) String query
    ) {
        logger.info(String.format("Listing products with page=%s, size=%s, sort=%s, asc=%s, query=%s", page, size, sort, asc, query));
        List<Product> products;
        if (query != null && !query.isEmpty())
            products = productService.listProducts(query, page, size, sort, asc);
        else
            products = productService.listProducts(page, size, sort, asc);
        logger.info(String.format("Found %s products", products.size()));
        return ResponseEntity.status(HttpStatus.OK).body(
            products.stream().map(ProductMapper::toProductDTO).collect(Collectors.toList())
        );
    }

    @GetMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> getProduct(@PathVariable Integer productId) {
        logger.info(String.format("Getting product with id=%s", productId));
        Product product = productService.getProduct(productId);
        logger.info(String.format("Product found with id=%s", productId));
        return ResponseEntity.ok(ProductMapper.toProductDTO(product));
    }

    @GetMapping("/sales/{saleId}")
    public ResponseEntity<SaleDTO> getSale(@PathVariable Integer saleId) {
        logger.info(String.format("Getting sale with id=%s", saleId));
        Sale sale = productService.getSale(saleId);
        logger.info(String.format("Sale found with id=%s", saleId));
        return ResponseEntity.ok(SaleMapper.toSaleDTO(sale));
    }

    @GetMapping("/purchases/{purchaseId}")
    public ResponseEntity<PurchaseDTO> getPurchase(@PathVariable Integer purchaseId) {
        logger.info(String.format("Getting purchase with id=%s", purchaseId));
        Purchase purchase = productService.getPurchase(purchaseId);
        logger.info(String.format("Purchase found with id=%s", purchaseId));
        return ResponseEntity.ok(PurchaseMapper.toPurchaseDTO(purchase));
    }

    @PostMapping("/products")
    public ResponseEntity<ProductDTO> createProduct(@RequestBody ProductRequest request) {
        logger.info(String.format("Creating product with name=%s, price=%s", request.getName(), request.getPrice()));
        Product product = productService.createProduct(request.getName(), request.getPrice());
        logger.info(String.format("Product created with id=%s", product.getId()));
        return ResponseEntity.status(HttpStatus.CREATED).body(ProductMapper.toProductDTO(product));
    }

    @PutMapping("/products/{productId}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Integer productId, @RequestBody ProductRequest request) {
        logger.info(String.format("Updating product with id=%s, name=%s, price=%s", productId, request.getName(), request.getPrice()));
        Product product = productService.updateProduct(productId, request.getName(), request.getPrice());
        logger.info(String.format("Product updated with id=%s", product.getId()));
        return ResponseEntity.ok(ProductMapper.toProductDTO(product));
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer productId) {
        logger.info(String.format("Deleting product with id=%s", productId));
        productService.deleteProduct(productId);
        logger.info(String.format("Product deleted with id=%s", productId));
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/sales")
    public ResponseEntity<List<SaleDTO>> listSalesByProductAndDateRange(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing sales with startDate=%s, endDate=%s, page=%s, size=%s, sort=%s, asc=%s", start, end, page, size, sort, asc));
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Sale> sales = productService.listSales(startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s sales", sales.size()));
        return ResponseEntity.status(HttpStatus.OK).body(
            sales.stream().map(SaleMapper::toSaleDTO).collect(Collectors.toList())
        );
    }

    @GetMapping("/users/{userId}/sales")
    public ResponseEntity<List<SaleDTO>> listSalesByUser(
        @PathVariable Integer userId,
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing sales with userId=%s startDate=%s, endDate=%s, page=%s, size=%s, sort=%s, asc=%s", userId, start, end, page, size, sort, asc));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Sale> sales = productService.listSalesBySalesman(userId, startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s sales", sales.size()));
        return ResponseEntity.status(HttpStatus.OK).body(
            sales.stream().map(SaleMapper::toSaleDTO).collect(Collectors.toList())
        );
    }

    @GetMapping("/users/{userId}/purchases")
    public ResponseEntity<List<PurchaseDTO>> listPurchasesByUser(
        @PathVariable Integer userId,
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing purchases with userId=%s startDate=%s, endDate=%s, page=%s, size=%s, sort=%s, asc=%s", userId, start, end, page, size, sort, asc));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Purchase> purchases = productService.listPurchasesByProvider(userId, startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s purchases", purchases.size()));
        return ResponseEntity.status(HttpStatus.OK).body(
            purchases.stream().map(PurchaseMapper::toPurchaseDTO).collect(Collectors.toList())
        );
    }

    @PostMapping("/sales/{productId}")
    public ResponseEntity<SaleDTO> sellProduct(@PathVariable Integer productId, @RequestBody SaleRequest request) {
        User user = getAuthenticatedUser();
        logger.info(String.format("Selling product with id=%s, salesmanId=%s, price=%s, amount=%s", productId, user.getId(), request.getPrice(), request.getAmount()));
        Sale sale = productService.sellProduct(user.getId(), productId, request.getAmount(), request.getPrice());
        logger.info(String.format("Product sold with id=%s", productId));
        return ResponseEntity.ok(SaleMapper.toSaleDTO(sale));
    }

    @GetMapping("/products/{productId}/sales")
    public ResponseEntity<List<SaleDTO>> listSalesByProduct(
        @PathVariable Integer productId,
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing sales with productId=%s startDate=%s, endDate=%s, page=%s, size=%s, sort=%s, asc=%s", productId, start, end, page, size, sort, asc));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Sale> sales = productService.listSalesByProduct(productId, startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s sales", sales.size()));
        return ResponseEntity.status(HttpStatus.OK).body(
            sales.stream().map(SaleMapper::toSaleDTO).collect(Collectors.toList())
        );
    }

    @GetMapping("/products/{productId}/purchases")
    public ResponseEntity<List<PurchaseDTO>> listPurchasesByProduct(
        @PathVariable Integer productId,
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing purchases with productId=%s startDate=%s, endDate=%s, page=%s, size=%s, sort=%s, asc=%s", productId, start, end, page, size, sort, asc));
        if (start == null || end == null) {
            LocalDateTime now = LocalDateTime.now();
            start = now.minusDays(90).format(DateTimeFormatter.ISO_DATE_TIME);
            end = now.format(DateTimeFormatter.ISO_DATE_TIME);
        }
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Purchase> purchases = productService.listPurchasesByProduct(productId, startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s purchases", purchases.size()));
        return ResponseEntity.status(HttpStatus.OK).body(
            purchases.stream().map(PurchaseMapper::toPurchaseDTO).collect(Collectors.toList())
        );
    }

    @GetMapping("/purchases")
    public ResponseEntity<List<PurchaseDTO>> listPurchasesByProductAndDateRange(
        @RequestParam(required = false) String start,
        @RequestParam(required = false) String end,
        @RequestParam(required = false) Integer page,
        @RequestParam(required = false) Integer size,
        @RequestParam(required = false) String sort,
        @RequestParam(required = false) Boolean asc
    ) {
        logger.info(String.format("Listing purchases with startDate=%s, endDate=%s, page=%s, size=%s, sort=%s, asc=%s", start, end, page, size, sort, asc));
        LocalDateTime startDate = LocalDateTime.parse(start, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        LocalDateTime endDate = LocalDateTime.parse(end, DateTimeFormatter.ISO_DATE_TIME).atOffset(ZoneOffset.UTC).toLocalDateTime();
        List<Purchase> purchases = productService.listPurchases(startDate, endDate, page, size, sort, asc);
        logger.info(String.format("Found %s purchases", purchases.size()));
        return ResponseEntity.status(HttpStatus.OK).body(purchases.stream().map(PurchaseMapper::toPurchaseDTO).collect(Collectors.toList()));
    }

    @PostMapping("/purchases/{productId}")
    public ResponseEntity<PurchaseDTO> buyProduct(@PathVariable Integer productId, @RequestBody PurchaseRequest request) {
        User user = getAuthenticatedUser();
        logger.info(String.format("Adding stock to product with id=%s, providerId=%s, cost=%s, amount=%s", productId, user.getId(), request.getCost(), request.getAmount()));
        Purchase purchase = productService.purchaseProduct(user.getId(), productId, request.getAmount(), request.getCost());
        logger.info(String.format("Stock added to product with id=%s", productId));
        return ResponseEntity.ok(PurchaseMapper.toPurchaseDTO(purchase));
    }
}

class ProductRequest {
    private String name;
    private Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}

class PurchaseRequest {
    private Double cost;
    private Double amount;

    public Double getCost() {
        return cost;
    }

    public void setCost(Double cost) {
        this.cost = cost;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }
}

class SaleRequest {
    private Double price;
    private Double amount;

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}