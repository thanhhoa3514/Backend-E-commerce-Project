package com.project.e_commerce.services.wishlist;

import com.project.e_commerce.dtos.wishlist.WishlistDTO;
import com.project.e_commerce.models.Wishlist;
import com.project.e_commerce.services.wishlist.command.IWishListCommandService;
import com.project.e_commerce.services.wishlist.mapper.IWishListMapperService;
import com.project.e_commerce.services.wishlist.queries.IWishListQueryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class WishListServiceImpl implements  IWishListService {
    private final IWishListCommandService wishlistCommandService;
    private final IWishListQueryService wishlistQueryService;
    private final IWishListMapperService wishlistMapperService;


    @Override
    @Transactional(readOnly = true)
    public List<WishlistDTO> getUserWishlists(Long userId) {
        log.debug("Getting wishlists for user ID: {}", userId);
        List<Wishlist> wishlists = wishlistQueryService.getWishlistsByUserId(userId);
        return wishlistMapperService.mapToDTOList(wishlists);
    }

    @Override
    @Transactional
    public WishlistDTO createWishlist(Long userId, String name) {
        log.debug("Creating wishlist for user ID: {}", userId);
        Wishlist wishlist = wishlistCommandService.createWishlist(userId, name);
        return wishlistMapperService.mapToDTO(wishlist);
    }

    @Override
    @Transactional
    public WishlistDTO addProductToWishlist(Long wishlistId, Long productId, Long userId) {
        log.debug("Adding product to wishlist for user ID: {}", userId);
        Wishlist wishlist = wishlistCommandService.addProductToWishlist(wishlistId, productId, userId);
        return wishlistMapperService.mapToDTO(wishlist);
    }

    @Override
    @Transactional
    public WishlistDTO removeProductFromWishlist(Long wishlistId, Long productId, Long userId) {
        log.debug("Removing product from wishlist for user ID: {}", userId);
        Wishlist wishlist = wishlistCommandService.removeProductFromWishlist(wishlistId, productId, userId);
        return wishlistMapperService.mapToDTO(wishlist);
    }

    @Override
    @Transactional
    public void deleteWishlist(Long wishlistId, Long userId) {
        log.debug("Deleting wishlist for user ID: {}", userId);
        wishlistCommandService.deleteWishlist(wishlistId, userId);

    }
}
