package com.martincastroalvarez.hex.hex.adapters.mappers;

import com.martincastroalvarez.hex.hex.adapters.entities.ProductEntity;
import com.martincastroalvarez.hex.hex.adapters.dto.ProductDTO;
import com.martincastroalvarez.hex.hex.domain.models.Product;
import java.math.BigDecimal;

public class ProductMapper {
    public static Product toProduct(ProductEntity productEntity) {
        Product product = new Product();
        product.setId(productEntity.getId());
        product.setName(productEntity.getName());
        product.setIsActive(productEntity.getIsActive());
        product.setPrice(productEntity.getPrice().doubleValue());
        return product;
    }

    public static ProductEntity toProductEntity(Product product) {
        ProductEntity productEntity = new ProductEntity();
        productEntity.setId(product.getId());
        productEntity.setName(product.getName());
        productEntity.setIsActive(product.getIsActive());
        productEntity.setPrice(BigDecimal.valueOf(product.getPrice()));
        return productEntity;
    }

    public static ProductDTO toProductDTO(Product product) {
        ProductDTO productDTO = new ProductDTO();
        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setIsActive(product.getIsActive());
        productDTO.setPrice(product.getPrice());
        return productDTO;
    }
}
