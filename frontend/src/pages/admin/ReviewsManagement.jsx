import React, { useState, useEffect } from 'react';
import { FaStar, FaCheck, FaTimes, FaTrash } from 'react-icons/fa';
import api from '../../services/api';
import { toast } from 'react-toastify';
import './ReviewsManagement.css';

const ReviewsManagement = () => {
  const [reviews, setReviews] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('all'); // all, pending, approved

  useEffect(() => {
    fetchReviews();
  }, [filter]);

  const fetchReviews = async () => {
    setLoading(true);
    try {
      let response;
      if (filter === 'pending') {
        response = await api.get('/reviews/pending');
      } else {
        // Fetch all reviews
        response = await api.get('/reviews');
      }

      let filteredReviews = response.data;
      if (filter === 'approved') {
        filteredReviews = filteredReviews.filter(r => r.isApproved === true);
      }

      setReviews(filteredReviews);
    } catch (error) {
      toast.error('Failed to load reviews');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleApprove = async (id) => {
    try {
      await api.put(`/reviews/${id}/approve`);
      toast.success('Review approved successfully');
      fetchReviews();
    } catch (error) {
      toast.error('Failed to approve review');
    }
  };

  const handleReject = async (id) => {
    try {
      await api.put(`/reviews/${id}/reject`);
      toast.success('Review rejected successfully');
      fetchReviews();
    } catch (error) {
      toast.error('Failed to reject review');
    }
  };

  const handleDelete = async (id) => {
    if (!window.confirm('Are you sure you want to delete this review?')) {
      return;
    }

    try {
      await api.delete(`/reviews/${id}`);
      toast.success('Review deleted successfully');
      fetchReviews();
    } catch (error) {
      toast.error('Failed to delete review');
    }
  };

  const renderStars = (rating) => {
    return [...Array(5)].map((_, index) => (
      <FaStar
        key={index}
        className={index < rating ? 'star-filled' : 'star-empty'}
      />
    ));
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="reviews-management">
      <div className="page-hero">
        <div className="container">
          <h1>Reviews Management</h1>
          <p>Moderate and manage restaurant reviews</p>
        </div>
      </div>

      <div className="container">
        <div className="filters">
          <button
            className={filter === 'all' ? 'filter-btn active' : 'filter-btn'}
            onClick={() => setFilter('all')}
          >
            All Reviews
          </button>
          <button
            className={filter === 'pending' ? 'filter-btn active' : 'filter-btn'}
            onClick={() => setFilter('pending')}
          >
            Pending Approval
          </button>
          <button
            className={filter === 'approved' ? 'filter-btn active' : 'filter-btn'}
            onClick={() => setFilter('approved')}
          >
            Approved
          </button>
        </div>

        {reviews.length === 0 ? (
          <div className="empty-state">
            <p>No reviews found</p>
          </div>
        ) : (
          <div className="reviews-grid">
            {reviews.map((review) => (
              <div key={review.id} className="review-card">
                <div className="review-header">
                  <div className="review-rating">
                    {renderStars(review.rating)}
                    <span className="rating-number">{review.rating}/5</span>
                  </div>
                  <div className={`review-status ${review.isApproved ? 'approved' : 'pending'}`}>
                    {review.isApproved ? 'Approved' : 'Pending'}
                  </div>
                </div>

                <div className="review-content">
                  <p className="review-comment">{review.comment}</p>
                  <div className="review-meta">
                    <span>Restaurant ID: {review.restaurantId}</span>
                    <span>User ID: {review.userId}</span>
                    <span>{new Date(review.createdAt).toLocaleDateString()}</span>
                  </div>
                </div>

                <div className="review-actions">
                  {!review.isApproved && (
                    <button
                      className="btn-approve"
                      onClick={() => handleApprove(review.id)}
                    >
                      <FaCheck /> Approve
                    </button>
                  )}
                  {!review.isApproved && (
                    <button
                      className="btn-reject"
                      onClick={() => handleReject(review.id)}
                    >
                      <FaTimes /> Reject
                    </button>
                  )}
                  <button
                    className="btn-delete"
                    onClick={() => handleDelete(review.id)}
                  >
                    <FaTrash /> Delete
                  </button>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default ReviewsManagement;
