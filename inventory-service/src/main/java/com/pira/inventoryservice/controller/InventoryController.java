package com.pira.inventoryservice.controller;

import com.pira.inventoryservice.dto.InventoryResponse;
import com.pira.inventoryservice.service.InventoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
public class InventoryController {
    private final InventoryService inventoryService;
    @Autowired
    public InventoryController(InventoryService inventoryService){
        this.inventoryService = inventoryService;
    }

    //http:localhost:8081/api/inventory?skuCode=something&skuCode=somethingTwo
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> isInStock(@RequestParam List<String> skuCode){
        return inventoryService.isInStock(skuCode);
    }

    @GetMapping("/list")
    @ResponseStatus(HttpStatus.OK)
    public List<InventoryResponse> getAllInInventory(){
        return inventoryService.getAllInInventory();
    }
}
