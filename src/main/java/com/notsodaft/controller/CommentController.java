package com.notsodaft.controller;

import com.notsodaft.model.Comment;
import com.notsodaft.model.Review;
import com.notsodaft.repository.CommentRepository;
import com.notsodaft.service.ReviewService;

import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class CommentController{
    private final CommentRepository commentRepository;
    private final ReviewService reviewService;

    public CommentController(CommentRepository commentRepository, ReviewService reviewService){
        this.commentRepository = commentRepository;
        this.reviewService = reviewService;
    }


    @GetMapping("/{id}/comments")
    public List<Comment> getComments(@PathVariable Long id) {
        return commentRepository.findByReviewIdOrderByCreatedAtAsc(id);
    }


    @PostMapping("/{id}/comments")
    public Comment addComment(@PathVariable Long id, @RequestBody Map<String, String> body){
        Review review = reviewService.getById(id);

        Comment comment = new Comment();
        comment.setReview(review);
        comment.setGuestName(body.get("guestName"));
        comment.setText(body.get("text"));

        return commentRepository.save(comment);
    }
}