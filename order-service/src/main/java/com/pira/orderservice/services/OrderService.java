package com.pira.orderservice.services;

import com.pira.orderservice.dto.InventoryResponse;
import com.pira.orderservice.dto.OrderLineItemsDto;
import com.pira.orderservice.dto.OrderRequest;
import com.pira.orderservice.model.Order;
import com.pira.orderservice.model.OrderLineItems;
import com.pira.orderservice.repository.OrderRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class OrderService {
    private final OrderRepository orderRepository;
    private final WebClient.Builder webClientBuilder;
    @Autowired
    public OrderService(OrderRepository orderRepository, WebClient.Builder webClientBuilder){
        this.orderRepository = orderRepository;
        this.webClientBuilder = webClientBuilder;
    }

    public String placeOrder(OrderRequest orderRequest){
        String returnString = "Order does not complete";

        Order order = Order.builder()
                .orderNumber(UUID.randomUUID().toString())
                .orderLineItemsList(orderRequest.getOrderLineItemsListDto().stream().map(orderLineItemsDto -> mapToDto(orderLineItemsDto)).toList())
                .build();

        List<String> skuCodes = order.getOrderLineItemsList()
                                        .stream()
                                        .map(OrderLineItems::getSkuCode)
                                        .toList();

        //todo call the inventory service
        InventoryResponse[] inventoryResponses = webClientBuilder.build().get()
                                    .uri("http://inventory-service/api/inventory",uriBuilder -> uriBuilder.queryParam("skuCode",skuCodes).build())
                                    .retrieve()
                                    .bodyToMono(InventoryResponse[].class)
                                    .block();

        log.info("result============== {}",inventoryResponses.toString());

        boolean allProductsInStock = Arrays.stream(inventoryResponses).allMatch(inventoryResponse -> inventoryResponse.isInStock());

        if (allProductsInStock){
            try {
                orderRepository.save(order);
                returnString = "Order is completed";
            }catch (Error e){
                log.info("Order does not complete--{}",e);;
            }
        }else {
            log.info("Order does not complete");
        }

        log.info("result {}",allProductsInStock);

        return returnString;
    }

    private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto) {
        OrderLineItems orderLineItems = OrderLineItems.builder()
                .id(orderLineItemsDto.getId())
                .skuCode(orderLineItemsDto.getSkuCode())
                .price(orderLineItemsDto.getPrice())
                .quantity(orderLineItemsDto.getQuantity())
                .build();

        return orderLineItems;
    }
}
