package com.project.e_commerce.services.wishlist.mapper;

import com.project.e_commerce.dtos.wishlist.WishlistDTO;
import com.project.e_commerce.dtos.wishlist.WishlistItemDTO;
import com.project.e_commerce.models.Wishlist;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class WishListMapperServiceImpl implements IWishListMapperService{
    @Override
    public WishlistDTO mapToDTO(Wishlist wishlist) {
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

    @Override
    public List<WishlistDTO> mapToDTOList(List<Wishlist> wishlists) {
        return wishlists.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }
}
