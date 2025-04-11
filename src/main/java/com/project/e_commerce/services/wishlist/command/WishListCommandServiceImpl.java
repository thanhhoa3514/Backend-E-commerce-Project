package com.project.e_commerce.services.wishlist.command;

import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.Wishlist;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.repositories.UserRepository;
import com.project.e_commerce.repositories.WishlistRepository;
import com.project.e_commerce.services.wishlist.IWishListService;
import com.project.e_commerce.services.wishlist.queries.IWishListQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
@Slf4j
public class WishListCommandServiceImpl implements IWishListCommandService {
    private final WishlistRepository wishlistRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final IWishListQueryService wishlistQueryService;

    @Override
    @Transactional
    public Wishlist createWishlist(Long userId, String name) {
        log.debug("Creating wishlist '{}' for user ID: {}", name, userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .name(name)
                .createdAt(LocalDateTime.now())
                .build();

        Wishlist savedWishlist = wishlistRepository.save(wishlist);
        log.info("Created wishlist: {} for user: {}", savedWishlist.getId(), userId);
        return savedWishlist;
    }

    @Override
    @Transactional
    public Wishlist addProductToWishlist(Long wishlistId, Long productId, Long userId) {
        log.debug("Adding product ID: {} to wishlist ID: {}", productId, wishlistId);
        Wishlist wishlist = wishlistQueryService.getWishlistByIdAndUserId(wishlistId, userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        wishlist.getProducts().add(product);
        Wishlist updatedWishlist = wishlistRepository.save(wishlist);
        log.info("Added product: {} to wishlist: {}", productId, wishlistId);
        return updatedWishlist;
    }

    @Override
    @Transactional
    public Wishlist removeProductFromWishlist(Long wishlistId, Long productId, Long userId) {
        log.debug("Removing product ID: {} from wishlist ID: {}", productId, wishlistId);
        Wishlist wishlist = wishlistQueryService.getWishlistByIdAndUserId(wishlistId, userId);

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        wishlist.getProducts().remove(product);
        Wishlist updatedWishlist = wishlistRepository.save(wishlist);
        log.info("Removed product: {} from wishlist: {}", productId, wishlistId);
        return updatedWishlist;
    }

    @Override
    @Transactional
    public void deleteWishlist(Long wishlistId, Long userId) {
        log.debug("Deleting wishlist ID: {} for user ID: {}", wishlistId, userId);
        Wishlist wishlist = wishlistQueryService.getWishlistByIdAndUserId(wishlistId, userId);

        wishlistRepository.delete(wishlist);
        log.info("Deleted wishlist: {}", wishlistId);
    }
}
