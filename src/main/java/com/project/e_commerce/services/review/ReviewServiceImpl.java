package com.project.e_commerce.services.review;

import com.project.e_commerce.dtos.review.ReviewDTO;
import com.project.e_commerce.dtos.review.ReviewResponseDTO;
import com.project.e_commerce.exceptions.DataNotFoundException;
import com.project.e_commerce.models.Review;
import com.project.e_commerce.models.product.Product;
import com.project.e_commerce.models.user.User;
import com.project.e_commerce.repositories.ProductRepository;
import com.project.e_commerce.repositories.ReviewRepository;
import com.project.e_commerce.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@RequiredArgsConstructor
@Service
public class ReviewServiceImpl implements IReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    @Transactional
    public Review insertComment(ReviewDTO reviewDTO) {
        User user = userRepository.findById(reviewDTO.getUserId()).orElse(null);
        Product product = productRepository.findById(reviewDTO.getProductId()).orElse(null);
        if (user == null || product == null) {
            throw new IllegalArgumentException("User or product not found");
        }
        Review newReview = Review.builder()
                .user(user)
                .product(product)
                .comment(reviewDTO.getComment())
                .build();
        return reviewRepository.save(newReview);
    }

    @Override
    @Transactional
    public void deleteComment(Long commentId) {
        reviewRepository.deleteById(commentId);
    }


    @Override
    @Transactional
    public void updateComment(Long id, ReviewDTO reviewDTO) throws DataNotFoundException {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new DataNotFoundException("Comment not found"));
        existingReview.setComment(reviewDTO.getComment());
        existingReview.setRating(reviewDTO.getRating());

        reviewRepository.save(existingReview);
    }

    @Override
    public List<ReviewResponseDTO> getCommentsByUserAndProduct(Long userId, Long productId) {
        // List<Review> reviews = reviewRepository.findByUserIdAndProductId(userId, productId);
        // return reviews.stream()
        //         .map(review -> ReviewResponseDTO.fromComment(review))
        //         .collect(Collectors.toList());

                return List.of();
    }

    @Override
    public List<ReviewResponseDTO> getCommentsByProduct(Long productId) {
        return List.of();
    }
}
