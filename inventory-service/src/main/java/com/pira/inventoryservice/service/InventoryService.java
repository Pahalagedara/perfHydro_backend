package com.pira.inventoryservice.service;

import com.pira.inventoryservice.dto.InventoryResponse;
import com.pira.inventoryservice.repository.InventoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class InventoryService {
    private final InventoryRepository inventoryRepository;

    @Autowired
    public InventoryService(InventoryRepository inventoryRepository){
        this.inventoryRepository = inventoryRepository;
    }
    @Transactional(readOnly = true)
    public List<InventoryResponse> isInStock(List<String> skuCode){
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                    InventoryResponse.builder()
                            .skuCode(inventory.getSkuCode())
                            .isInStock(inventory.getQuantity() > 0)
                            .build()
                ).toList();
    }

    @Transactional
    public List<InventoryResponse> getAllInInventory(){
        return inventoryRepository.findAll().stream().map(inventory -> InventoryResponse.builder()
                .isInStock(inventory.getQuantity() > 0)
                .skuCode(inventory.getSkuCode())
                .build()
        ).toList();
    }

}
