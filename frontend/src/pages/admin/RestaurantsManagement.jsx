import React, { useState, useEffect } from 'react';
import { FaSearch, FaTrash, FaEye, FaMapMarkerAlt, FaStar } from 'react-icons/fa';
import { Link } from 'react-router-dom';
import { adminService } from '../../services/adminService';
import { toast } from 'react-toastify';
import { format } from 'date-fns';
import './RestaurantsManagement.css';

const RestaurantsManagement = () => {
  const [restaurants, setRestaurants] = useState([]);
  const [filteredRestaurants, setFilteredRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [cityFilter, setCityFilter] = useState('');

  useEffect(() => {
    fetchRestaurants();
  }, []);

  useEffect(() => {
    filterRestaurants();
  }, [searchTerm, cityFilter, restaurants]);

  const fetchRestaurants = async () => {
    try {
      const response = await adminService.getAllRestaurants();
      setRestaurants(response.data);
      setFilteredRestaurants(response.data);
    } catch (error) {
      toast.error('Failed to load restaurants');
    } finally {
      setLoading(false);
    }
  };

  const filterRestaurants = () => {
    let filtered = restaurants;

    if (searchTerm) {
      filtered = filtered.filter(restaurant =>
        restaurant.name?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        restaurant.description?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (cityFilter) {
      filtered = filtered.filter(restaurant => restaurant.city === cityFilter);
    }

    setFilteredRestaurants(filtered);
  };

  const handleDeleteRestaurant = async (id) => {
    if (!window.confirm('Are you sure you want to delete this restaurant?')) return;

    try {
      await adminService.deleteRestaurant(id);
      toast.success('Restaurant deleted successfully');
      fetchRestaurants();
    } catch (error) {
      toast.error('Failed to delete restaurant');
    }
  };

  const cities = [...new Set(restaurants.map(r => r.city))].filter(Boolean);

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="restaurants-management">
      <div className="page-hero">
        <div className="container">
          <h1>Restaurants Management</h1>
          <p>Manage all registered restaurants</p>
        </div>
      </div>

      <div className="container">
        <div className="management-header">
          <div className="search-box">
            <FaSearch />
            <input
              type="text"
              placeholder="Search restaurants..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>

          <select
            value={cityFilter}
            onChange={(e) => setCityFilter(e.target.value)}
            className="city-filter"
          >
            <option value="">All Cities</option>
            {cities.map(city => (
              <option key={city} value={city}>{city}</option>
            ))}
          </select>
        </div>

        <div className="results-info">
          <p>{filteredRestaurants.length} restaurants found</p>
        </div>

        <div className="restaurants-grid">
          {filteredRestaurants.map(restaurant => (
            <div key={restaurant.id} className="restaurant-admin-card">
              <div className="restaurant-admin-header">
                <h3>{restaurant.name}</h3>
                <div className="restaurant-rating">
                  <FaStar />
                  <span>{restaurant.rating || '5.0'}</span>
                </div>
              </div>

              <div className="restaurant-admin-info">
                <div className="info-row">
                  <FaMapMarkerAlt />
                  <span>{restaurant.city}</span>
                </div>
                <div className="info-row">
                  <span className="label">Address:</span>
                  <span>{restaurant.address}</span>
                </div>
                <div className="info-row">
                  <span className="label">Phone:</span>
                  <span>{restaurant.phone}</span>
                </div>
                <div className="info-row">
                  <span className="label">Email:</span>
                  <span>{restaurant.email || 'N/A'}</span>
                </div>
                <div className="info-row">
                  <span className="label">Reviews:</span>
                  <span>{restaurant.totalReviews || 0} reviews</span>
                </div>
                <div className="info-row">
                  <span className="label">Created:</span>
                  <span>
                    {restaurant.createdAt ? format(new Date(restaurant.createdAt), 'MMM dd, yyyy') : 'N/A'}
                  </span>
                </div>
              </div>

              <div className="restaurant-admin-actions">
                <Link
                  to={`/restaurants/${restaurant.id}`}
                  className="action-btn view-btn"
                  title="View Restaurant"
                >
                  <FaEye />
                  View
                </Link>
                <button
                  onClick={() => handleDeleteRestaurant(restaurant.id)}
                  className="action-btn delete-btn"
                  title="Delete Restaurant"
                >
                  <FaTrash />
                  Delete
                </button>
              </div>
            </div>
          ))}
        </div>

        {filteredRestaurants.length === 0 && (
          <div className="no-results">
            <p>No restaurants found matching your criteria</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default RestaurantsManagement;
