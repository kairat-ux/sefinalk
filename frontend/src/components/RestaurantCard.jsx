import React from 'react';
import { Link } from 'react-router-dom';
import { FaStar, FaMapMarkerAlt, FaPhone } from 'react-icons/fa';
import './RestaurantCard.css';

const RestaurantCard = ({ restaurant }) => {
  const defaultImage = 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=800';

  return (
    <div className="restaurant-card">
      <div className="card-image">
        <img
          src={restaurant.imageUrl || defaultImage}
          alt={restaurant.name}
          onError={(e) => e.target.src = defaultImage}
        />
        <div className="card-badge">
          <FaStar />
          <span>{restaurant.rating || '5.0'}</span>
        </div>
      </div>

      <div className="card-content">
        <h3 className="card-title">{restaurant.name}</h3>

        <p className="card-description">
          {restaurant.description?.substring(0, 100)}
          {restaurant.description?.length > 100 ? '...' : ''}
        </p>

        <div className="card-info">
          <div className="info-item">
            <FaMapMarkerAlt />
            <span>{restaurant.city}</span>
          </div>
          {restaurant.phone && (
            <div className="info-item">
              <FaPhone />
              <span>{restaurant.phone}</span>
            </div>
          )}
        </div>

        <div className="card-meta">
          <span className="reviews-count">{restaurant.totalReviews || 0} reviews</span>
          {restaurant.cuisineType && (
            <span className="cuisine-type">{restaurant.cuisineType}</span>
          )}
        </div>

        <Link to={`/restaurants/${restaurant.id}`} className="btn btn-primary card-btn">
          View Details & Book
        </Link>
      </div>
    </div>
  );
};

export default RestaurantCard;
