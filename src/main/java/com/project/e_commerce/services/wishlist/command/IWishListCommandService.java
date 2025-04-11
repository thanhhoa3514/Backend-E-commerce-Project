package com.project.e_commerce.services.wishlist.command;


import com.project.e_commerce.models.Wishlist;

/**
 * Command service interface for wishlist operations that modify data
 */
public interface IWishListCommandService {
    /**
     * Creates a new wishlist for a user
     * @param userId the ID of the user
     * @param name the name of the wishlist
     * @return the created wishlist
     */
    Wishlist createWishlist(Long userId, String name);

    /**
     * Adds a product to a wishlist
     * @param wishlistId the ID of the wishlist
     * @param productId the ID of the product
     * @param userId the ID of the user
     * @return the updated wishlist
     */
    Wishlist addProductToWishlist(Long wishlistId, Long productId, Long userId);

    /**
     * Removes a product from a wishlist
     * @param wishlistId the ID of the wishlist
     * @param productId the ID of the product
     * @param userId the ID of the user
     * @return the updated wishlist
     */
    Wishlist removeProductFromWishlist(Long wishlistId, Long productId, Long userId);

    /**
     * Deletes a wishlist
     * @param wishlistId the ID of the wishlist
     * @param userId the ID of the user
     */
    void deleteWishlist(Long wishlistId, Long userId);
}
