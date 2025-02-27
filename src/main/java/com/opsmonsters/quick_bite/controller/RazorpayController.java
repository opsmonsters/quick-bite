//package com.opsmonsters.quick_bite.controller;
//
//import com.opsmonsters.quick_bite.services.RazorpayService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.Map;
//
//@RestController
//@RequestMapping("/payments")
//public class RazorpayController {
//
//    private final RazorpayService razorpayService;
//
//    public RazorpayController(RazorpayService razorpayService) {
//        this.razorpayService = razorpayService;
//    }
//
//    @PostMapping("/create-order")
//    public ResponseEntity<Map<String, Object>> createOrder(@RequestBody Map<String, Object> requestData) {
//        Long orderId = Long.parseLong(requestData.get("orderId").toString());
//        double amount = Double.parseDouble(requestData.get("amount").toString());
//
//        Map<String, Object> response = razorpayService.createOrder(orderId, amount);
//        return ResponseEntity.ok(response);
//    }
//
//    @PostMapping("/verify")
//    public ResponseEntity<String> verifyPayment(@RequestParam String razorpayPaymentId,
//                                                @RequestParam String razorpayOrderId,
//                                                @RequestParam String razorpaySignature) {
//        boolean isValid = razorpayService.verifyPayment(razorpayPaymentId, razorpayOrderId, razorpaySignature);
//        return isValid ? ResponseEntity.ok("Payment Verified") : ResponseEntity.badRequest().body("Payment Verification Failed");
//    }
//}
