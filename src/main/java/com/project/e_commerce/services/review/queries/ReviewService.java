package com.project.e_commerce.services.review.queries;

import com.project.e_commerce.dtos.review.ReviewDTO;
import com.project.e_commerce.dtos.review.ReviewRequestDTO;
import com.project.e_commerce.dtos.review.ReviewResponseDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.exceptions.InvalidDataException;
import com.project.e_commerce.models.Product;
import com.project.e_commerce.models.Review;
import com.project.e_commerce.models.User;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.repositories.ReviewRepository;
import com.project.e_commerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    @Cacheable(value = "productReviews", key = "#productId + '_' + #pageable.pageNumber + '_' + #pageable.pageSize")
    public ReviewResponseDTO getProductReviews(Long productId, Pageable pageable) {
        // Check if product exists
        if (!productRepository.existsById(productId)) {
            throw new DataNotFoundException("Product not found");
        }

        Page<Review> reviews = reviewRepository.findByProductId(productId, pageable);
        Double averageRating = reviewRepository.getAverageRatingByProductId(productId);
        Long reviewCount = reviewRepository.getReviewCountByProductId(productId);

        Page<ReviewDTO> reviewDTOs = reviews.map(this::mapToDTO);

        return ReviewResponseDTO.builder()
                .reviews(reviewDTOs.getContent())
                .currentPage(reviewDTOs.getNumber())
                .totalItems(reviewDTOs.getTotalElements())
                .totalPages(reviewDTOs.getTotalPages())
                .averageRating(averageRating != null ? averageRating : 0.0)
                .reviewCount(reviewCount)
                .build();
    }

    @Transactional
    @CacheEvict(value = "productReviews", allEntries = true)
    public ReviewDTO createReview(Long productId, Long userId, ReviewRequestDTO reviewRequest) {
        // Validate rating
        if (reviewRequest.getRating() < 1 || reviewRequest.getRating() > 5) {
            throw new InvalidDataException("Rating must be between 1 and 5");
        }

        // Check if product exists
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DataNotFoundException("Product not found"));

        // Check if user exists
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new DataNotFoundException("User not found"));

        // Check if user already reviewed this product
        reviewRepository.findByProductIdAndUserId(productId, userId).ifPresent(r -> {
            throw new InvalidDataException("You have already reviewed this product");
        });

        // Create review
        Review review = Review.builder()
                .product(product)
                .user(user)
                .rating(reviewRequest.getRating())
                .comment(reviewRequest.getComment())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        Review savedReview = reviewRepository.save(review);
        log.info("Created review for product: {} by user: {}", productId, userId);

        return mapToDTO(savedReview);
    }

    @Transactional
    @CacheEvict(value = "productReviews", allEntries = true)
    public ReviewDTO updateReview(Long productId, Long userId, ReviewRequestDTO reviewRequest) {
        // Validate rating
        if (reviewRequest.getRating() < 1 || reviewRequest.getRating() > 5) {
            throw new InvalidDataException("Rating must be between 1 and 5");
        }

        // Find existing review
        Review review = reviewRepository.findByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new DataNotFoundException("Review not found"));

        // Update review
        review.setRating(reviewRequest.getRating());
        review.setComment(reviewRequest.getComment());
        review.setUpdatedAt(LocalDateTime.now());

        Review updatedReview = reviewRepository.save(review);
        log.info("Updated review for product: {} by user: {}", productId, userId);

        return mapToDTO(updatedReview);
    }

    @Transactional
    @CacheEvict(value = "productReviews", allEntries = true)
    public void deleteReview(Long productId, Long userId) {
        Review review = reviewRepository.findByProductIdAndUserId(productId, userId)
                .orElseThrow(() -> new DataNotFoundException("Review not found"));

        reviewRepository.delete(review);
        log.info("Deleted review for product: {} by user: {}", productId, userId);
    }

    private ReviewDTO mapToDTO(Review review) {
        return ReviewDTO.builder()
                .id(review.getId())
                .productId(review.getProduct().getId())
                .userId(review.getUser().getId())
                .username(review.getUser().getUsername())
                .rating(review.getRating())
                .comment(review.getComment())
                .createdAt(review.getCreatedAt())
                .updatedAt(review.getUpdatedAt())
                .build();
    }
}