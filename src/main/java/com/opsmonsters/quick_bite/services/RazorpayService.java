//package com.opsmonsters.quick_bite.services;
//
//import com.razorpay.Order;
//import com.razorpay.RazorpayClient;
//import com.razorpay.RazorpayException;
//import com.razorpay.Utils;
//import org.json.JSONObject;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.stereotype.Service;
//
//import javax.annotation.PostConstruct;
//import java.util.HashMap;
//import java.util.Map;
//
//@Service
//public class RazorpayService {
//
//    @Value("${razorpay.key_id}")
//    private String razorpayKeyId;
//
//    @Value("${razorpay.key_secret}")
//    private String razorpayKeySecret;
//
//    private RazorpayClient razorpayClient;
//
//
//    @PostConstruct
//    public void init() {
//        try {
//            this.razorpayClient = new RazorpayClient(razorpayKeyId, razorpayKeySecret);
//        } catch (RazorpayException e) {
//            throw new RuntimeException("Failed to initialize Razorpay client: " + e.getMessage(), e);
//        }
//    }
//
//
//    public Map<String, Object> createOrder(Long orderId, double amount) {
//        Map<String, Object> response = new HashMap<>();
//        try {
//            JSONObject orderRequest = new JSONObject();
//            orderRequest.put("amount", (int) (amount * 100));
//            orderRequest.put("currency", "INR");
//            orderRequest.put("receipt", "order_" + orderId);
//            orderRequest.put("payment_capture", 1);
//
//            Order razorpayOrder = razorpayClient.orders.create(orderRequest);
//
//            response.put("orderId", orderId);
//            response.put("razorpayOrderId", razorpayOrder.get("id"));
//            response.put("status", "created");
//            return response;
//        } catch (RazorpayException e) {
//            throw new RuntimeException("Error creating Razorpay order: " + e.getMessage(), e);
//        }
//    }
//
//
//    public boolean verifyPayment(String razorpayPaymentId, String razorpayOrderId, String razorpaySignature) {
//        try {
//            Map<String, String> attributes = new HashMap<>();
//            attributes.put("razorpay_order_id", razorpayOrderId);
//            attributes.put("razorpay_payment_id", razorpayPaymentId);
//            attributes.put("razorpay_signature", razorpaySignature);
//
//            Utils.verifyPaymentSignature((JSONObject) attributes, razorpayKeySecret);
//            return true;
//        } catch (RazorpayException e) {
//            throw new RuntimeException("Payment verification failed: " + e.getMessage(), e);
//        }
//    }
//
//}
