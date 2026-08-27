package com.reliability.ecommerce.service;

import com.reliability.ecommerce.dto.SellerRequest;
import com.reliability.ecommerce.dto.SellerResponse;
import com.reliability.ecommerce.entity.Role;
import com.reliability.ecommerce.entity.Seller;
import com.reliability.ecommerce.entity.User;
import com.reliability.ecommerce.exception.ResourceNotFoundException;
import com.reliability.ecommerce.repository.SellerRepository;
import com.reliability.ecommerce.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class SellerService {

    private final SellerRepository sellerRepository;
    private final UserRepository userRepository;

    public SellerService(
            SellerRepository sellerRepository,
            UserRepository userRepository) {

        this.sellerRepository = sellerRepository;
        this.userRepository = userRepository;
    }

    public SellerResponse createSeller(SellerRequest request) {

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

        if (user.getRole() != Role.SELLER) {
            throw new RuntimeException(
                    "User must have SELLER role");
        }

        if (sellerRepository.findByUserId(user.getId()).isPresent()) {
            throw new RuntimeException(
                    "Seller profile already exists");
        }

        Seller seller = new Seller(
                user,
                request.getBusinessName()
        );

        Seller savedSeller = sellerRepository.save(seller);

        return mapToResponse(savedSeller);
    }

    public SellerResponse getSeller(Long sellerId) {

        Seller seller = sellerRepository.findById(sellerId)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller not found"));

        return mapToResponse(seller);
    }

    private SellerResponse mapToResponse(Seller seller) {

        return new SellerResponse(
                seller.getId(),
                seller.getUser().getId(),
                seller.getBusinessName(),
                seller.getTrustScore(),
                seller.getCancellationRate(),
                seller.getReturnRate(),
                seller.getComplaintRate()
        );
    }
}