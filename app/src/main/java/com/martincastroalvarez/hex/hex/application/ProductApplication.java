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
import com.martincastroalvarez.hex.hex.domain.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Service
public class ProductApplication extends HexagonalApplication implements ProductService {
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private PurchaseRepository purchaseRepository;

    @Autowired
    private UserRepository userRepository;

    private final List<String> PRODUCT_SORT_KEYS = Arrays.asList("id", "name");
    private final List<String> SALE_SORT_KEYS = Arrays.asList("id", "product", "salesman", "datetime", "price");
    private final List<String> PURCHASE_SORT_KEYS = Arrays.asList("id", "product", "provider", "datetime", "cost");

    @Override
    public Sale getSale(Integer saleId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Getting sale. Id: %d", saleId));
        return saleRepository.get(saleId).orElseThrow(() -> new SaleNotFoundException());
    }

    @Override
    public Purchase getPurchase(Integer purchaseId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Getting purchase. Id: %d", purchaseId));
        return purchaseRepository.get(purchaseId).orElseThrow(() -> new PurchaseNotFoundException());
    }

    @Override
    public List<Product> listProducts(Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing products. Page: %d, Size: %d, Sort: %s, Ascending: %b", page, size, sort, asc));
        return productRepository.get(getPageRequest(page, size, sort, asc, PRODUCT_SORT_KEYS)).getContent();
    }

    @Override
    public List<Product> listProducts(String query, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Searching products. Query: %s, Page: %d, Size: %d, Sort: %s, Ascending: %b", query, page, size, sort, asc));
        return productRepository.get(query, getPageRequest(page, size, sort, asc, PRODUCT_SORT_KEYS)).getContent();
    }

    @Override
    public Product createProduct(String name, Double price) throws HexagonalValidationException {
        logger.info(String.format("Creating product. Name: %s, Price: %f", name, price));
        if (name == null || name.length() < 3 || name.length() > 50)
            throw new InvalidNameException();
        if (price == null || price < 0)
            throw new InvalidPriceException();
        if (productRepository.get(name).isPresent())
            throw new NameAlreadyTakenException();
        Product product = new Product();
        product.setIsActive(true);
        product.setName(name);
        product.setPrice(price);
        return productRepository.save(product);
    }

    @Override
    public Product getProduct(Integer productId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Getting product. Id: %d", productId));
        return productRepository.get(productId).orElseThrow(() -> new ProductNotFoundException());
    }

    @Override
    public Product deleteProduct(Integer productId) throws HexagonalEntityNotFoundException {
        logger.info(String.format("Deleting product. Id: %d", productId));
        Product product = productRepository.get(productId).orElseThrow(() -> new ProductNotFoundException());
        product.setIsActive(false);
        productRepository.save(product);
        return product;
    }

    @Override
    public Product updateProduct(Integer productId, String name, Double price) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Updating product name. Id: %d, Name: %s", productId, name));
        if (price == null || price < 0)
            throw new InvalidPriceException();
        if (name == null || name.length() < 3 || name.length() > 50)
            throw new InvalidNameException();
        Product product = productRepository.get(productId).orElseThrow(() -> new ProductNotFoundException());
        if (!product.getIsActive())
            throw new ProductDisabledException();
        product.setName(name);
        product.setPrice(price);
        productRepository.save(product);
        return product;
    }

    @Override
    public Sale sellProduct(Integer salesmanId, Integer productId, Double amount, Double price) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Selling product. Salesman: %d, Product: %d, Amount: %f, Price: %f", salesmanId, productId, amount, price));
        Product product = productRepository.get(productId).orElseThrow(() -> new ProductNotFoundException());
        if (!product.getIsActive())
            throw new ProductDisabledException();
        User salesman = userRepository.get(salesmanId).orElseThrow(() -> new SalesmanNotFoundException());
        if (amount == null || amount < 0)
            throw new InvalidAmountException();
        if (price == null || price < 0)
            throw new InvalidPriceException();
        Sale sale = new Sale();
        sale.setSalesman(salesman);
        sale.setProduct(product);
        sale.setAmount(amount);
        sale.setPrice(price);
        sale.setDatetime(LocalDateTime.now());
        return saleRepository.save(sale);
    }

    @Override
    public List<Sale> listSales(LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalPaginationException {
        logger.info(String.format("Listing sales. Start Date: %s, End Date: %s, Page: %d, Size: %d, Sort: %s, Ascending: %b", startDate, endDate, page, size, sort, asc));
        if (startDate == null || endDate == null)
            return saleRepository.get(getPageRequest(page, size, sort, asc, SALE_SORT_KEYS)).getContent();
        return saleRepository.get(startDate, endDate, getPageRequest(page, size, sort, asc, SALE_SORT_KEYS)).getContent();
    }

    @Override
    public List<Sale> listSalesByProduct(Integer productId, LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        logger.info(String.format("Listing sales by product. Product: %d, Start Date: %s, End Date: %s, Page: %d, Size: %d, Sort: %s, Ascending: %b", productId, startDate, endDate, page, size, sort, asc));
        Product product = productRepository.get(productId).orElseThrow(() -> new ProductNotFoundException());
        if (startDate == null || endDate == null)
            return saleRepository.get(product, getPageRequest(page, size, sort, asc, SALE_SORT_KEYS)).getContent();
        return saleRepository.get(product, startDate, endDate, getPageRequest(page, size, sort, asc, SALE_SORT_KEYS)).getContent();
    }

    @Override
    public List<Sale> listSalesBySalesman(Integer salesmanId, LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        logger.info(String.format("Listing sales by salesman. Salesman: %d, Start Date: %s, End Date: %s, Page: %d, Size: %d, Sort: %s, Ascending: %b", salesmanId, startDate, endDate, page, size, sort, asc));
        User salesman = userRepository.get(salesmanId).orElseThrow(() -> new SalesmanNotFoundException());
        if (startDate == null || endDate == null)
            return saleRepository.get(salesman, getPageRequest(page, size, sort, asc, SALE_SORT_KEYS)).getContent();
        return saleRepository.get(salesman, startDate, endDate, getPageRequest(page, size, sort, asc, SALE_SORT_KEYS)).getContent();
    }

    @Override
    public Purchase purchaseProduct(Integer providerId, Integer productId, Double amount, Double cost) throws HexagonalEntityNotFoundException, HexagonalValidationException {
        logger.info(String.format("Purchase product. Provider: %d, Product: %d, Amount: %f, Cost: %f", providerId, productId, amount, cost));
        Product product = productRepository.get(productId).orElseThrow(() -> new ProductNotFoundException());
        if (!product.getIsActive())
            throw new ProductDisabledException();
        User provider = userRepository.get(providerId).orElseThrow(() -> new ProviderNotFoundException());
        if (amount == null || amount < 0)
            throw new InvalidAmountException();
        if (cost == null || cost < 0)
            throw new InvalidCostException();
        Purchase purchase = new Purchase();
        purchase.setProvider(provider);
        purchase.setProduct(product);
        purchase.setAmount(amount);
        purchase.setCost(cost);
        purchase.setDatetime(LocalDateTime.now());
        return purchaseRepository.save(purchase);
    }

    @Override
    public List<Purchase> listPurchases(LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        logger.info(String.format("Listing purchases. Start Date: %s, End Date: %s, Page: %d, Size: %d, Sort: %s, Ascending: %b", startDate, endDate, page, size, sort, asc));
        if (startDate == null || endDate == null)
            return purchaseRepository.get(getPageRequest(page, size, sort, asc, PURCHASE_SORT_KEYS)).getContent();
        return purchaseRepository.get(startDate, endDate, getPageRequest(page, size, sort, asc, PURCHASE_SORT_KEYS)).getContent();
    }

    @Override
    public List<Purchase> listPurchasesByProduct(Integer productId, LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        logger.info(String.format("Listing purchases by product. Product: %d, Start Date: %s, End Date: %s, Page: %d, Size: %d, Sort: %s, Ascending: %b", productId, startDate, endDate, page, size, sort, asc));
        Product product = productRepository.get(productId).orElseThrow(() -> new ProductNotFoundException());
        if (startDate == null || endDate == null)
            return purchaseRepository.get(product, getPageRequest(page, size, sort, asc, PURCHASE_SORT_KEYS)).getContent();
        return purchaseRepository.get(product, startDate, endDate, getPageRequest(page, size, sort, asc, PURCHASE_SORT_KEYS)).getContent();
    }

    @Override
    public List<Purchase> listPurchasesByProvider(Integer providerId, LocalDateTime startDate, LocalDateTime endDate, Integer page, Integer size, String sort, Boolean asc) throws HexagonalEntityNotFoundException, HexagonalPaginationException {
        logger.info(String.format("Listing purchases by provider. Provider: %d, Start Date: %s, End Date: %s, Page: %d, Size: %d, Sort: %s, Ascending: %b", providerId, startDate, endDate, page, size, sort, asc));
        User provider = userRepository.get(providerId).orElseThrow(() -> new ProviderNotFoundException());
        if (startDate == null || endDate == null)
            return purchaseRepository.get(provider, getPageRequest(page, size, sort, asc, PURCHASE_SORT_KEYS)).getContent();
        return purchaseRepository.get(provider, startDate, endDate, getPageRequest(page, size, sort, asc, PURCHASE_SORT_KEYS)).getContent();
    }
}
