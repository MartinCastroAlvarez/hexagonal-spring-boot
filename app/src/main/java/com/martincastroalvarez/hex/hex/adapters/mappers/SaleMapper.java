package com.martincastroalvarez.hex.hex.adapters.mappers;

import com.martincastroalvarez.hex.hex.adapters.entities.SaleEntity;
import com.martincastroalvarez.hex.hex.adapters.dto.SaleDTO;
import com.martincastroalvarez.hex.hex.domain.models.Sale;
import java.math.BigDecimal;

public class SaleMapper {
    public static Sale toSale(SaleEntity saleEntity) {
        Sale sale = new Sale();
        sale.setId(saleEntity.getId());
        sale.setPrice(saleEntity.getPrice().doubleValue());
        sale.setAmount(saleEntity.getAmount().doubleValue());
        sale.setDatetime(saleEntity.getDatetime());
        sale.setProduct(ProductMapper.toProduct(saleEntity.getProduct()));
        sale.setSalesman(UserMapper.toUser(saleEntity.getSalesman()));
        return sale;
    }

    public static SaleEntity toSaleEntity(Sale sale) {
        SaleEntity saleEntity = new SaleEntity();
        saleEntity.setId(sale.getId());
        saleEntity.setPrice(BigDecimal.valueOf(sale.getPrice()));
        saleEntity.setAmount(BigDecimal.valueOf(sale.getAmount()));
        saleEntity.setDatetime(sale.getDatetime());
        saleEntity.setProduct(ProductMapper.toProductEntity(sale.getProduct()));
        saleEntity.setSalesman(UserMapper.toUserEntity(sale.getSalesman()));
        return saleEntity;
    }

    public static SaleDTO toSaleDTO(Sale sale) {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setId(sale.getId());
        saleDTO.setPrice(sale.getPrice());
        saleDTO.setSalesmanId(sale.getSalesman().getId());
        saleDTO.setProductId(sale.getProduct().getId());
        saleDTO.setAmount(sale.getAmount());
        saleDTO.setDatetime(sale.getDatetime());
        return saleDTO;
    }
}
