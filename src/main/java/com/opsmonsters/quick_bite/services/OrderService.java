package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.OrderDto;
import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.models.*;
import com.opsmonsters.quick_bite.repositories.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepo orderRepo;
    private final CartRepo cartRepo;
    private final UserRepo userRepo;

    public OrderService(OrderRepo orderRepo, CartRepo cartRepo, UserRepo userRepo) {
        this.orderRepo = orderRepo;
        this.cartRepo = cartRepo;
        this.userRepo = userRepo;
    }

    @Transactional
    public ResponseDto placeOrder(Long userId, Long cartId) {

        Users user = userRepo.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User with ID " + userId + " not found!"));


        Cart cart = cartRepo.findById(cartId)
                .orElseThrow(() -> new IllegalArgumentException("Cart with ID " + cartId + " not found!"));


        Long storedCartId = cart.getCartId();

        Order order = new Order(user, cart, "Cash on Delivery");
        orderRepo.save(order);


        order.setCart(null);
        orderRepo.save(order);

        cartRepo.delete(cart);

        return new ResponseDto(200, "Order placed successfully!", convertToDto(order, storedCartId));
    }

    public List<OrderDto> getUserOrders(Long userId) {
        return orderRepo.findByUser_UserId(userId)
                .stream()
                .map(order -> convertToDto(order, order.getCart() != null ? order.getCart().getCartId() : null))
                .collect(Collectors.toList());
    }

    public OrderDto getOrderById(Long orderId) {
        return orderRepo.findById(orderId)
                .map(order -> convertToDto(order, order.getCart() != null ? order.getCart().getCartId() : null))
                .orElseThrow(() -> new IllegalArgumentException("Order with ID " + orderId + " not found!"));
    }

    @Transactional
    public ResponseDto updateOrder(Long orderId, String newPaymentMethod) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found!"));
        order.setPaymentMethod(newPaymentMethod);
        orderRepo.save(order);
        return new ResponseDto(200, "Order updated successfully!", convertToDto(order, order.getCart() != null ? order.getCart().getCartId() : null));
    }

    @Transactional
    public ResponseDto deleteOrder(Long orderId) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found!"));
        orderRepo.delete(order);
        return new ResponseDto(200, "Order deleted successfully!", null);
    }


    private OrderDto convertToDto(Order order, Long cartId) {
        return new OrderDto(
                order.getOrderId(),
                order.getUser().getUserId(),
                cartId,
                order.getPaymentMethod(),
                order.getOrderDate()
        );
    }
}
