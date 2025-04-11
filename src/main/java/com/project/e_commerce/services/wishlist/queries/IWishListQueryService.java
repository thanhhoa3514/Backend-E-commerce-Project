package com.project.e_commerce.services.wishlist.queries;


import com.project.e_commerce.models.Wishlist;

import java.util.List;

/**
 * Query service interface for wishlist operations that retrieve data
 */
public interface IWishListQueryService {

    /**
     * Retrieves all wishlists for a user
     * @param userId the ID of the user
     * @return list of user's wishlists
     */
    List<Wishlist> getWishlistsByUserId(Long userId);

    /**
     * Retrieves a wishlist by ID and user ID
     * @param wishlistId the ID of the wishlist
     * @param userId the ID of the user
     * @return the wishlist
     */
    Wishlist getWishlistByIdAndUserId(Long wishlistId, Long userId);
}
