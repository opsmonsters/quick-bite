package com.opsmonsters.quick_bite.services;

import com.opsmonsters.quick_bite.dto.PromoCodeDto;
import com.opsmonsters.quick_bite.dto.ResponseDto;
import com.opsmonsters.quick_bite.models.Cart;
import com.opsmonsters.quick_bite.models.CartDetails;
import com.opsmonsters.quick_bite.models.Product;
import com.opsmonsters.quick_bite.models.PromoCode;
import com.opsmonsters.quick_bite.repositories.CartRepo;
import com.opsmonsters.quick_bite.repositories.ProductRepo;
import com.opsmonsters.quick_bite.repositories.PromoCodeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PromoCodeService {

    @Autowired
    private PromoCodeRepo promoCodeRepo;

    @Autowired
    private CartRepo cartRepo;
    @Autowired
    private ProductRepo productRepo;



    public PromoCode createPromoCode(PromoCodeDto promoCodeDto) {
        PromoCode promoCode = new PromoCode();
        promoCode.setPromoName(promoCodeDto.getPromoName());
        promoCode.setCouponAmount(promoCodeDto.getCouponAmount());
        promoCode.setMinOrderAmount(promoCodeDto.getMinOrderAmount());
        promoCode.setStartDate(promoCodeDto.getStartDate());
        promoCode.setExpiryDate(promoCodeDto.getExpiryDate());


        if (promoCodeDto.getProductId() != null) {
            Optional<Product> productOpt = productRepo.findById(promoCodeDto.getProductId());
            if (productOpt.isPresent()) {
                promoCode.setProduct(productOpt.get());
            } else {
                throw new IllegalArgumentException("Invalid product ID: " + promoCodeDto.getProductId());
            }
        } else {
            throw new IllegalArgumentException("Product ID cannot be null for promo codes.");
        }

        return promoCodeRepo.save(promoCode);
    }


    public ResponseDto applyPromoCode(Long cartId, String code) {
        Optional<Cart> cartOpt = cartRepo.findById(cartId);
        Optional<PromoCode> promoOpt = promoCodeRepo.findByPromoName(code);

        if (cartOpt.isEmpty() || promoOpt.isEmpty()) {
            return new ResponseDto(400, "Invalid cart or promo code.");
        }

        Cart cart = cartOpt.get();
        PromoCode promo = promoOpt.get();

        if (!promo.isValid()) {
            return new ResponseDto(400, "Promo code expired or not valid yet.");
        }

        if (cart.getPromoCode() != null) {
            return new ResponseDto(400, "Promo code already applied.");
        }

        double discountAmount = calculateDiscount(cart, promo);

        if (discountAmount > 0) {
            cart.setPromoCode(promo);
            cart.applyDiscount(discountAmount);
            cartRepo.save(cart);
            return new ResponseDto(200, "Promo code applied successfully!", discountAmount);
        }

        return new ResponseDto(400, "Promo code conditions not met.");
    }

    private double calculateDiscount(Cart cart, PromoCode promo) {
        if (promo.getProduct() != null) {
            return calculateProductDiscount(cart, promo);
        } else if (promo.getMinOrderAmount() != null && cart.getGrandTotalPrice() >= promo.getMinOrderAmount()) {
            return promo.getCouponAmount();
        }
        return 0.0;
    }
    public List<PromoCodeDto> getUserPromoCodes(Long userId) {
        List<PromoCode> promoCodes = promoCodeRepo.findAll();
        return promoCodes.stream().map(this::convertToDto).toList();
    }

    private PromoCodeDto convertToDto(PromoCode promoCode) {
        PromoCodeDto dto = new PromoCodeDto();
        dto.setPromoName(promoCode.getPromoName());
        dto.setCouponAmount(promoCode.getCouponAmount());
        dto.setMinOrderAmount(promoCode.getMinOrderAmount());
        dto.setStartDate(promoCode.getStartDate());
        dto.setExpiryDate(promoCode.getExpiryDate());
        if (promoCode.getProduct() != null) {
            dto.setProductId(promoCode.getProduct().getProductId());
        }
        return dto;
    }

    private double calculateProductDiscount(Cart cart, PromoCode promo) {
        return cart.getCartDetails().stream()
                .filter(cd -> cd.getProduct().equals(promo.getProduct()))
                .mapToDouble(cd -> promo.getCouponAmount())
                .findFirst()
                .orElse(0.0);
    }

    public List<PromoCode> getAllPromoCodes() {
        return promoCodeRepo.findAll();
    }

    public boolean isPromoCodeValid(String promoName) {
        return promoCodeRepo.findByPromoName(promoName).map(PromoCode::isValid).orElse(false);
    }

    public void deletePromoCode(Long promoId) {
        promoCodeRepo.deleteById(promoId);
    }
}
