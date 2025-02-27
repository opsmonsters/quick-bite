package com.opsmonsters.quick_bite.controller;

import com.opsmonsters.quick_bite.dto.PromoCodeDto;
import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.models.PromoCode;
import com.opsmonsters.quick_bite.services.PromoCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController

public class PromoCodeController {

    private final PromoCodeService promoCodeService;

    public PromoCodeController(PromoCodeService promoCodeService) {
        this.promoCodeService = promoCodeService;
    }


    @PostMapping("/admin/promo/create")
    public ResponseEntity<PromoCode> createPromoCode(@RequestBody PromoCodeDto promoCodeDto) {
        return ResponseEntity.ok(promoCodeService.createPromoCode(promoCodeDto));
    }


    @PostMapping("/api/promo/apply/{cartId}/{promoName}")
    public ResponseEntity<ResponseDto> applyPromoCode(@PathVariable Long cartId, @PathVariable String promoName) {
        ResponseDto response = promoCodeService.applyPromoCode(cartId, promoName);
        HttpStatus status = HttpStatus.valueOf(response.getStatusCode());
        return ResponseEntity.status(status).body(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PromoCodeDto>> getUserPromoCodes(@PathVariable Long userId) {
        return ResponseEntity.ok(promoCodeService.getUserPromoCodes(userId));
    }

    @GetMapping
    public ResponseEntity<List<PromoCode>> getAllPromoCodes() {
        return ResponseEntity.ok(promoCodeService.getAllPromoCodes());
    }


    @GetMapping("/validate/{promoName}")
    public ResponseEntity<Boolean> validatePromoCode(@PathVariable String promoName) {
        boolean isValid = promoCodeService.isPromoCodeValid(promoName);
        return ResponseEntity.ok(isValid);
    }


    @DeleteMapping("/admin/promo/delete/{promoId}")
    public ResponseEntity<String> deletePromoCode(@PathVariable Long promoId) {
        promoCodeService.deletePromoCode(promoId);
        return ResponseEntity.ok("Promo code deleted successfully.");
    }
}
