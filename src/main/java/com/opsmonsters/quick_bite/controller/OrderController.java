package com.opsmonsters.quick_bite.controller;

import com.opsmonsters.quick_bite.dto.OrderDto;
import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.services.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/auth/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/buyNow")
    public ResponseEntity<ResponseDto> placeOrder(@RequestBody OrderDto orderDto) {
        if (orderDto.getUserId() == null || orderDto.getCartId() == null) {
            return ResponseEntity.badRequest().body(new ResponseDto(400, "User ID and Cart Details ID are required!", null));
        }
        ResponseDto response = orderService.placeOrder(orderDto.getUserId(), orderDto.getCartId());
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }



    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDto>> getUserOrders(@PathVariable Long userId) {
        return ResponseEntity.ok(orderService.getUserOrders(userId));
    }

    @GetMapping("/{orderId}")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long orderId) {
        return ResponseEntity.ok(orderService.getOrderById(orderId));
    }

    @PutMapping("/update/{orderId}")
    public ResponseEntity<ResponseDto> updateOrder(@PathVariable Long orderId, @RequestParam String newPaymentMethod) {
        ResponseDto response = orderService.updateOrder(orderId, newPaymentMethod);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }

    @DeleteMapping("/delete/{orderId}")
    public ResponseEntity<ResponseDto> deleteOrder(@PathVariable Long orderId) {
        ResponseDto response = orderService.deleteOrder(orderId);
        return ResponseEntity.status(response.getStatusCode()).body(response);
    }
}
