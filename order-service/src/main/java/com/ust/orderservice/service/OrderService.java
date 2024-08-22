package com.ust.orderservice.service;

import com.ust.orderservice.domain.ItemStatus;
import com.ust.orderservice.domain.Order;
import com.ust.orderservice.domain.OrderItem;
import com.ust.orderservice.domain.OrderStatus;
import com.ust.orderservice.feignclient.InventoryClient;
import com.ust.orderservice.payload.ProductDto;
import com.ust.orderservice.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
@Slf4j
public class OrderService {

    private final OrderRepository orderRepository;

    private final InventoryClient inventoryClient;

    public Order createOrder(Order order) {
        List<OrderItem> li = order.getOrderItems();
        for(OrderItem item: li){
            ProductDto itm = inventoryClient.getProduct(item.getSkuCode());
            if(inventoryClient.isProductAvailable(item.getSkuCode(),item.getQuantity()).available()){
                item.setItemStatus(ItemStatus.IN_STOCK);
                item.setPrice(itm.price());
                order.setTotalPrice(order.getTotalPrice()+(item.getPrice()*item.getQuantity()));
            }
            else{
                log.debug("The item {} is out of stock, id ",item.getId() );
                item.setItemStatus(ItemStatus.OUT_OF_STOCK);
                //li.remove(item);
                order.setOrderItems(li);
            }
        }
        return orderRepository.save(order);
        //return result;
       // order = orderRepository.save(order);
        //return order;
    }

    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId).orElseThrow();
    }

    public Order checkOrder(Long id){
        Order order = orderRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Order not found")
        );
        List<OrderItem> li = order.getOrderItems();
        for(OrderItem item:li){
            if(inventoryClient.isProductAvailable(item.getSkuCode(),item.getQuantity()).available()){
                //inventoryClient.updateProductQuantity(item.getSkuCode(),item.getQuantity());
                item.setItemStatus(ItemStatus.IN_STOCK);
            }
            else{
                item.setItemStatus(ItemStatus.OUT_OF_STOCK);
            }
        }
        order.setOrderItems(li);
        return order;
    }

    public Order confirmOrder(Long id){
        Order order = orderRepository.findById(id).orElseThrow(
                ()-> new RuntimeException("Order not found")
        );
        List<OrderItem> li = order.getOrderItems();
        for(OrderItem item:li){
            if(inventoryClient.isProductAvailable(item.getSkuCode(),item.getQuantity()).available()){
                inventoryClient.updateProductQuantity(item.getSkuCode(),item.getQuantity());
            }
            else{
                item.setItemStatus(ItemStatus.OUT_OF_STOCK);
            }
        }
        order.setStatus(OrderStatus.CONFIRMED);
        return order;
    }
}
