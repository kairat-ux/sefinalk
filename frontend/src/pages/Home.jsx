import React from 'react';
import { Link } from 'react-router-dom';
import { FaSearch, FaCalendarCheck, FaStar, FaUtensils, FaClock, FaAward } from 'react-icons/fa';
import './Home.css';

const Home = () => {
  const features = [
    {
      icon: <FaSearch />,
      title: 'Discover Restaurants',
      description: 'Browse through our curated selection of premium dining establishments'
    },
    {
      icon: <FaCalendarCheck />,
      title: 'Easy Booking',
      description: 'Reserve your table in seconds with our seamless booking system'
    },
    {
      icon: <FaStar />,
      title: 'Verified Reviews',
      description: 'Read authentic reviews from our community of food enthusiasts'
    },
    {
      icon: <FaAward />,
      title: 'Exclusive Access',
      description: 'Get priority reservations at the most sought-after restaurants'
    }
  ];

  const popularCuisines = [
    { name: 'Italian', image: 'https://images.unsplash.com/photo-1498579485796-98be8e9f54b3?w=400' },
    { name: 'Japanese', image: 'https://images.unsplash.com/photo-1579584425555-c3ce17fd4351?w=400' },
    { name: 'French', image: 'https://images.unsplash.com/photo-1551024506-0bccd828d307?w=400' },
    { name: 'Steakhouse', image: 'https://images.unsplash.com/photo-1600891964092-4316c288032e?w=400' }
  ];

  return (
    <div className="home">
      {/* Hero Section */}
      <section className="hero">
        <div className="hero-overlay"></div>
        <div className="hero-content">
          <h1 className="hero-title">
            Experience Fine Dining
            <span className="text-gold"> Redefined</span>
          </h1>
          <p className="hero-subtitle">
            Discover and reserve tables at the world's most exclusive restaurants
          </p>
          <div className="hero-buttons">
            <Link to="/restaurants" className="btn btn-primary btn-large">
              <FaUtensils />
              Explore Restaurants
            </Link>
            <Link to="/about" className="btn btn-secondary btn-large">
              Learn More
            </Link>
          </div>
        </div>
        <div className="hero-scroll">
          <span>Scroll to explore</span>
        </div>
      </section>

      {/* Features Section */}
      <section className="section features-section">
        <div className="container">
          <div className="section-header text-center">
            <h2>Why Choose Luxury Reservations</h2>
            <p>Elevate your dining experience with our premium services</p>
          </div>

          <div className="features-grid">
            {features.map((feature, index) => (
              <div key={index} className="feature-card">
                <div className="feature-icon">{feature.icon}</div>
                <h3>{feature.title}</h3>
                <p>{feature.description}</p>
              </div>
            ))}
          </div>
        </div>
      </section>

      {/* Popular Cuisines */}
      <section className="section cuisines-section">
        <div className="container">
          <div className="section-header text-center">
            <h2>Popular Cuisines</h2>
            <p>Explore diverse culinary traditions from around the world</p>
          </div>

          <div className="cuisines-grid">
            {popularCuisines.map((cuisine, index) => (
              <Link to="/restaurants" key={index} className="cuisine-card">
                <img src={cuisine.image} alt={cuisine.name} />
                <div className="cuisine-overlay">
                  <h3>{cuisine.name}</h3>
                </div>
              </Link>
            ))}
          </div>
        </div>
      </section>

      {/* Stats Section */}
      <section className="stats-section">
        <div className="container">
          <div className="stats-grid">
            <div className="stat-item">
              <FaUtensils className="stat-icon" />
              <h3>500+</h3>
              <p>Premium Restaurants</p>
            </div>
            <div className="stat-item">
              <FaCalendarCheck className="stat-icon" />
              <h3>100K+</h3>
              <p>Reservations Made</p>
            </div>
            <div className="stat-item">
              <FaStar className="stat-icon" />
              <h3>50K+</h3>
              <p>Happy Customers</p>
            </div>
            <div className="stat-item">
              <FaClock className="stat-icon" />
              <h3>24/7</h3>
              <p>Customer Support</p>
            </div>
          </div>
        </div>
      </section>

      {/* CTA Section */}
      <section className="cta-section">
        <div className="container">
          <div className="cta-content">
            <h2>Ready to Experience Fine Dining?</h2>
            <p>Join thousands of food enthusiasts and start your culinary journey today</p>
            <Link to="/register" className="btn btn-primary btn-large">
              Get Started Now
            </Link>
          </div>
        </div>
      </section>
    </div>
  );
};

export default Home;
