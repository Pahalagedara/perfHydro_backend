package com.pira.productservice.service;

import com.pira.productservice.dto.ProductRequest;
import com.pira.productservice.dto.ProductResponse;
import com.pira.productservice.model.Product;
import com.pira.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class ProductService {
    private final ProductRepository productRepository;
   @Autowired
    public ProductService(ProductRepository productRepository){
        this.productRepository = productRepository;
    }

    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("save the product - {}",product.getId());
    }

    public List<ProductResponse> getAllProducts(){
       List<Product> products = productRepository.findAll();

       List<ProductResponse> productResponses = new ArrayList<>();
       products.forEach(e->{
           ProductResponse pr = ProductResponse.builder()
                   .name(e.getName())
                   .description(e.getDescription())
                   .price(e.getPrice())
                   .build();
           productResponses.add(pr);
       });
       return productResponses;
    }
}
