package com.reliability.ecommerce.service;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import com.reliability.ecommerce.dto.ProductRequest;
import com.reliability.ecommerce.dto.ProductResponse;
import com.reliability.ecommerce.entity.Product;
import com.reliability.ecommerce.entity.Role;
import com.reliability.ecommerce.entity.Seller;
import com.reliability.ecommerce.exception.ResourceNotFoundException;
import com.reliability.ecommerce.repository.ProductRepository;
import com.reliability.ecommerce.repository.SellerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;



@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SellerRepository sellerRepository;

    public ProductService(
            ProductRepository productRepository,
            SellerRepository sellerRepository) {

        this.productRepository = productRepository;
        this.sellerRepository = sellerRepository;
    }

    @CacheEvict(value = {"products", "product"}, allEntries = true)
    public ProductResponse createProduct(ProductRequest request) {

        Seller seller = sellerRepository.findById(request.getSellerId())
                .orElseThrow(() ->
                        new ResourceNotFoundException("Seller not found"));

        if (seller.getUser().getRole() != Role.SELLER) {
            throw new RuntimeException("User is not a seller");
        }

        Product product = new Product(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getCategory(),
                request.getBrand(),
                seller
        );

        Product savedProduct = productRepository.save(product);

        return mapToResponse(savedProduct);
    }
    
    @Cacheable(value = "product", key = "#id")
    public ProductResponse getProduct(Long id) {

        Product product = productRepository.findById(id)
                .orElseThrow(() ->
                        new ResourceNotFoundException("Product not found"));

        return mapToResponse(product);
    }

    @Cacheable(value = "products")
    public List<ProductResponse> getAllProducts() {

        return productRepository.findByActiveTrue()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    private ProductResponse mapToResponse(Product product) {

        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getCategory(),
                product.getBrand(),
                product.getSeller().getId(),
                product.getSeller().getBusinessName(),
                product.getActive()
        );
    }
}