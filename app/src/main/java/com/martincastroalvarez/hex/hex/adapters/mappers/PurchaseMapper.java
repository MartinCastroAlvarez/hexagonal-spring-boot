package com.martincastroalvarez.hex.hex.adapters.mappers;

import com.martincastroalvarez.hex.hex.adapters.entities.PurchaseEntity;
import com.martincastroalvarez.hex.hex.adapters.dto.PurchaseDTO;
import com.martincastroalvarez.hex.hex.domain.models.Purchase;
import java.math.BigDecimal;

public class PurchaseMapper {
    public static Purchase toPurchase(PurchaseEntity purchaseEntity) {
        Purchase purchase = new Purchase();
        purchase.setId(purchaseEntity.getId());
        purchase.setCost(purchaseEntity.getCost().doubleValue());
        purchase.setAmount(purchaseEntity.getAmount().doubleValue());
        purchase.setDatetime(purchaseEntity.getDatetime());
        purchase.setProduct(ProductMapper.toProduct(purchaseEntity.getProduct()));
        purchase.setProvider(UserMapper.toUser(purchaseEntity.getProvider()));
        return purchase;
    }

    public static PurchaseEntity toPurchaseEntity(Purchase purchase) {
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(purchase.getId());
        purchaseEntity.setCost(BigDecimal.valueOf(purchase.getCost()));
        purchaseEntity.setAmount(BigDecimal.valueOf(purchase.getAmount()));
        purchaseEntity.setDatetime(purchase.getDatetime());
        purchaseEntity.setProduct(ProductMapper.toProductEntity(purchase.getProduct()));
        purchaseEntity.setProvider(UserMapper.toUserEntity(purchase.getProvider()));
        return purchaseEntity;
    }

    public static PurchaseDTO toPurchaseDTO(Purchase purchase) {
        PurchaseDTO purchaseDTO = new PurchaseDTO();
        purchaseDTO.setId(purchase.getId());
        purchaseDTO.setCost(purchase.getCost());
        purchaseDTO.setSupplierId(purchase.getProvider().getId());
        purchaseDTO.setProductId(purchase.getProduct().getId());
        purchaseDTO.setAmount(purchase.getAmount());
        purchaseDTO.setDatetime(purchase.getDatetime());
        return purchaseDTO;
    }
}
