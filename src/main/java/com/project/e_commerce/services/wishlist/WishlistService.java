package com.project.e_commerce.services.wishlist;

import com.project.e_commerce.dtos.wishlist.WishlistDTO;
import com.project.e_commerce.dtos.wishlist.WishlistItemDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.models.Wishlist;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.repositories.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishlistService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<WishlistDTO> getUserWishlists(Long userId) {
        List<Wishlist> wishlists = wishlistRepository.findByUserId(userId);
        return wishlists.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public WishlistDTO createWishlist(Long userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .name(name)
                .createdAt(LocalDateTime.now())
                .build();

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        log.info("Created wishlist: {} for user: {}", savedWishlist.getId(), userId);
        return mapToDTO(savedWishlist);
    }

    @Transactional
    public WishlistDTO addProductToWishlist(Long wishlistId, Long productId, Long userId) {
        Wishlist wishlist = wishlistRepository.findByIdAndUserId(wishlistId, userId)
                .orElseThrow(() -> new DataNotFoundException("Wishlist not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        wishlist.getProducts().add(product);
        Wishlist updatedWishlist = wishlistRepository.save(wishlist);
        log.info("Added product: {} to wishlist: {}", productId, wishlistId);
        return mapToDTO(updatedWishlist);
    }

    @Transactional
    public WishlistDTO removeProductFromWishlist(Long wishlistId, Long productId, Long userId) {
        Wishlist wishlist = wishlistRepository.findByIdAndUserId(wishlistId, userId)
                .orElseThrow(() -> new DataNotFoundException("Wishlist not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        wishlist.getProducts().remove(product);
        Wishlist updatedWishlist = wishlistRepository.save(wishlist);
        log.info("Removed product: {} from wishlist: {}", productId, wishlistId);
        return mapToDTO(updatedWishlist);
    }

    @Transactional
    public void deleteWishlist(Long wishlistId, Long userId) {
        Wishlist wishlist = wishlistRepository.findByIdAndUserId(wishlistId, userId)
                .orElseThrow(() -> new DataNotFoundException("Wishlist not found"));

        wishlistRepository.delete(wishlist);
        log.info("Deleted wishlist: {}", wishlistId);
    }

    private WishlistDTO mapToDTO(Wishlist wishlist) {
        List<WishlistItemDTO> items = wishlist.getProducts().stream()
                .map(product -> WishlistItemDTO.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .price(product.getPrice())
                        .thumbnail(product.getThumbnail())
                        .build())
                .collect(Collectors.toList());

        return WishlistDTO.builder()
                .id(wishlist.getId())
                .name(wishlist.getName())
                .createdAt(wishlist.getCreatedAt())
                .items(items)
                .build();
    }
}