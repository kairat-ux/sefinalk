import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaPlus, FaUtensils, FaEdit, FaTrash, FaStar, FaMapMarkerAlt } from 'react-icons/fa';
import { ownerService } from '../../services/ownerService';
import { toast } from 'react-toastify';
import './OwnerDashboard.css';

const OwnerDashboard = () => {
  const [restaurants, setRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchMyRestaurants();
  }, []);

  const fetchMyRestaurants = async () => {
    try {
      const response = await ownerService.getMyRestaurants();
      setRestaurants(response.data);
    } catch (error) {
      toast.error('Failed to load your restaurants');
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteRestaurant = async (id) => {
    if (!window.confirm('Are you sure you want to delete this restaurant?')) return;

    try {
      await ownerService.deleteRestaurant(id);
      toast.success('Restaurant deleted successfully');
      fetchMyRestaurants();
    } catch (error) {
      toast.error('Failed to delete restaurant');
    }
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="owner-dashboard">
      <div className="page-hero">
        <div className="container">
          <div className="hero-content-flex">
            <div>
              <h1>My Restaurants</h1>
              <p>Manage your restaurant listings</p>
            </div>
            <Link to="/owner/restaurants/add" className="btn btn-primary">
              <FaPlus />
              Add New Restaurant
            </Link>
          </div>
        </div>
      </div>

      <div className="container">
        {restaurants.length === 0 ? (
          <div className="no-restaurants">
            <FaUtensils className="empty-icon" />
            <h3>No restaurants yet</h3>
            <p>Start by adding your first restaurant</p>
            <Link to="/owner/restaurants/add" className="btn btn-primary">
              <FaPlus />
              Add Restaurant
            </Link>
          </div>
        ) : (
          <div className="owner-restaurants-grid">
            {restaurants.map(restaurant => (
              <div key={restaurant.id} className="owner-restaurant-card">
                <div className="owner-card-header">
                  <h3>{restaurant.name}</h3>
                  <div className="rating-display">
                    <FaStar />
                    <span>{restaurant.rating || '5.0'}</span>
                  </div>
                </div>

                <div className="owner-card-body">
                  <div className="info-item">
                    <FaMapMarkerAlt />
                    <span>{restaurant.city}</span>
                  </div>

                  <p className="description">
                    {restaurant.description?.substring(0, 120)}
                    {restaurant.description?.length > 120 ? '...' : ''}
                  </p>

                  <div className="stats-row">
                    <div className="stat">
                      <strong>{restaurant.totalReviews || 0}</strong>
                      <span>Reviews</span>
                    </div>
                    <div className="stat">
                      <strong>{restaurant.isActive ? 'Active' : 'Inactive'}</strong>
                      <span>Status</span>
                    </div>
                  </div>
                </div>

                <div className="owner-card-footer">
                  <Link
                    to={`/restaurants/${restaurant.id}`}
                    className="action-btn view-btn"
                  >
                    View
                  </Link>
                  <Link
                    to={`/owner/restaurants/${restaurant.id}/edit`}
                    className="action-btn edit-btn"
                  >
                    <FaEdit />
                    Edit
                  </Link>
                  <button
                    onClick={() => handleDeleteRestaurant(restaurant.id)}
                    className="action-btn delete-btn"
                  >
                    <FaTrash />
                    Delete
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

export default OwnerDashboard;
