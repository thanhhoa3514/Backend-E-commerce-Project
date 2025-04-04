package com.project.e_commerce.controllers;

import com.project.e_commerce.dtos.review.ReviewDTO;
import com.project.e_commerce.dtos.review.ReviewRequestDTO;
import com.project.e_commerce.dtos.review.ReviewResponseDTO;
import com.project.e_commerce.models.User;
//import com.project.e_commerce.services.review.ReviewService;
import com.project.e_commerce.services.review.queries.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("${api.prefix}/products/{productId}/reviews")
@RequiredArgsConstructor
public class ReviewController {
    private final com.project.e_commerce.services.review.queries.ReviewService reviewService;

    @GetMapping
    public ResponseEntity<ReviewResponseDTO> getProductReviews(
            @PathVariable Long productId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String direction) {

        Sort.Direction sortDirection = direction.equalsIgnoreCase("asc") ?
                Sort.Direction.ASC : Sort.Direction.DESC;

        Pageable pageable = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));
        ReviewResponseDTO response = reviewService.getProductReviews(productId, pageable);

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<ReviewDTO> createReview(
            @PathVariable Long productId,
            @Valid @RequestBody ReviewRequestDTO reviewRequest) {

        User currentUser = getCurrentUser();
        ReviewDTO review = reviewService.createReview(productId, currentUser.getId(), reviewRequest);

        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<ReviewDTO> updateReview(
            @PathVariable Long productId,
            @Valid @RequestBody ReviewRequestDTO reviewRequest) {

        User currentUser = getCurrentUser();
        ReviewDTO review = reviewService.updateReview(productId, currentUser.getId(), reviewRequest);

        return ResponseEntity.ok(review);
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteReview(@PathVariable Long productId) {
        User currentUser = getCurrentUser();
        reviewService.deleteReview(productId, currentUser.getId());

        return ResponseEntity.noContent().build();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}