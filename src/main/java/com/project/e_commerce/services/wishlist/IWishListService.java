package com.project.e_commerce.services.wishlist;


import com.project.e_commerce.dtos.wishlist.WishlistDTO;

import java.util.List;

/**
 * Service interface for managing user wishlists
 */
public interface IWishListService {


    /**
     * Retrieves all wishlists for a user
     * @param userId the ID of the user
     * @return list of user's wishlists
     */
    List<WishlistDTO> getUserWishlists(Long userId);

    /**
     * Creates a new wishlist for a user
     * @param userId the ID of the user
     * @param name the name of the wishlist
     * @return the created wishlist
     */
    WishlistDTO createWishlist(Long userId, String name);

    /**
     * Adds a product to a wishlist
     * @param wishlistId the ID of the wishlist
     * @param productId the ID of the product
     * @param userId the ID of the user
     * @return the updated wishlist
     */
    WishlistDTO addProductToWishlist(Long wishlistId, Long productId, Long userId);

    /**
     * Removes a product from a wishlist
     * @param wishlistId the ID of the wishlist
     * @param productId the ID of the product
     * @param userId the ID of the user
     * @return the updated wishlist
     */
    WishlistDTO removeProductFromWishlist(Long wishlistId, Long productId, Long userId);

    /**
     * Deletes a wishlist
     * @param wishlistId the ID of the wishlist
     * @param userId the ID of the user
     */
    void deleteWishlist(Long wishlistId, Long userId);
}
