package com.project.e_commerce.services.wishlist.queries;

import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Wishlist;
import com.project.e_commerce.repositories.WishlistRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@RequiredArgsConstructor
@Slf4j
public class WishListQueryServiceImpl implements  IWishListQueryService {
    private final WishlistRepository wishlistRepository;

    @Override
    @Transactional(readOnly = true)
    public List<Wishlist> getWishlistsByUserId(Long userId) {
        log.debug("Fetching wishlists for user ID: {}", userId);
        return wishlistRepository.findByUserId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public Wishlist getWishlistByIdAndUserId(Long wishlistId, Long userId) {
        log.debug("Fetching wishlist ID: {} for user ID: {}", wishlistId, userId);
        return wishlistRepository.findByIdAndUserId(wishlistId, userId)
                .orElseThrow(() -> new DataNotFoundException("Wishlist not found"));
    }
}
