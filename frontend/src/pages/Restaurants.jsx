import React, { useState, useEffect } from 'react';
import { FaSearch, FaFilter } from 'react-icons/fa';
import RestaurantCard from '../components/RestaurantCard';
import { restaurantService } from '../services/restaurantService';
import { toast } from 'react-toastify';
import './Restaurants.css';

const Restaurants = () => {
  const [restaurants, setRestaurants] = useState([]);
  const [filteredRestaurants, setFilteredRestaurants] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [selectedCity, setSelectedCity] = useState('');

  useEffect(() => {
    fetchRestaurants();
  }, []);

  useEffect(() => {
    filterRestaurants();
  }, [searchTerm, selectedCity, restaurants]);

  const fetchRestaurants = async () => {
    try {
      const response = await restaurantService.getAllRestaurants();
      setRestaurants(response.data);
      setFilteredRestaurants(response.data);
    } catch (error) {
      toast.error('Failed to load restaurants');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const filterRestaurants = () => {
    let filtered = restaurants;

    if (searchTerm) {
      filtered = filtered.filter(restaurant =>
        restaurant.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        restaurant.description?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (selectedCity) {
      filtered = filtered.filter(restaurant =>
        restaurant.city === selectedCity
      );
    }

    setFilteredRestaurants(filtered);
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
    <div className="restaurants-page">
      <div className="page-hero">
        <div className="container">
          <h1>Discover Fine Dining</h1>
          <p>Browse our curated collection of premium restaurants</p>
        </div>
      </div>

      <div className="container">
        <div className="filters-section">
          <div className="search-box">
            <FaSearch />
            <input
              type="text"
              placeholder="Search restaurants..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>

          <div className="filter-box">
            <FaFilter />
            <select
              value={selectedCity}
              onChange={(e) => setSelectedCity(e.target.value)}
            >
              <option value="">All Cities</option>
              {cities.map(city => (
                <option key={city} value={city}>{city}</option>
              ))}
            </select>
          </div>
        </div>

        <div className="results-info">
          <p>{filteredRestaurants.length} restaurants found</p>
        </div>

        {filteredRestaurants.length === 0 ? (
          <div className="no-results">
            <h3>No restaurants found</h3>
            <p>Try adjusting your search or filters</p>
          </div>
        ) : (
          <div className="restaurants-grid">
            {filteredRestaurants.map(restaurant => (
              <RestaurantCard key={restaurant.id} restaurant={restaurant} />
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default Restaurants;
