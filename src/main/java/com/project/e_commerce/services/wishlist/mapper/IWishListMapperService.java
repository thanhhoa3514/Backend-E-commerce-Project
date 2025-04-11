package com.project.e_commerce.services.wishlist.mapper;


import com.project.e_commerce.dtos.wishlist.WishlistDTO;
import com.project.e_commerce.models.Wishlist;

import java.util.List;

/**
 * Mapper service interface for converting between Wishlist entities and DTOs
 */
public interface IWishListMapperService {

    /**
     * Maps a Wishlist entity to a WishlistDTO
     * @param wishlist the wishlist entity
     * @return the wishlist DTO
     */
    WishlistDTO mapToDTO(Wishlist wishlist);

    /**
     * Maps a list of Wishlist entities to a list of WishlistDTOs
     * @param wishlists the list of wishlist entities
     * @return the list of wishlist DTOs
     */
    List<WishlistDTO> mapToDTOList(List<Wishlist> wishlists);
}
