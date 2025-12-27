import React, { useState } from 'react';
import { Link, useNavigate } from 'react-router-dom';
import { FaUserCircle, FaCalendarAlt, FaSignOutAlt, FaBars, FaTimes, FaUserShield, FaUtensils, FaCalendarCheck, FaStar, FaChartLine, FaMoneyBillWave } from 'react-icons/fa';
import { useAuth } from '../context/AuthContext';
import NotificationDropdown from './NotificationDropdown';
import './Header.css';

const Header = () => {
  const { user, logout } = useAuth();
  const navigate = useNavigate();
  const [menuOpen, setMenuOpen] = useState(false);

  const handleLogout = () => {
    logout();
    navigate('/');
    setMenuOpen(false);
  };

  return (
    <header className="header">
      <div className="container header-container">
        <Link to="/" className="logo">
          <div className="logo-icon">LR</div>
          <div className="logo-text">
            <span className="logo-main">Luxury Reservations</span>
            <span className="logo-sub">Fine Dining Experience</span>
          </div>
        </Link>

        <button className="mobile-menu-btn" onClick={() => setMenuOpen(!menuOpen)}>
          {menuOpen ? <FaTimes /> : <FaBars />}
        </button>

        <nav className={`nav ${menuOpen ? 'nav-open' : ''}`}>
          <Link to="/restaurants" className="nav-link" onClick={() => setMenuOpen(false)}>
            Restaurants
          </Link>
          <Link to="/about" className="nav-link" onClick={() => setMenuOpen(false)}>
            About Us
          </Link>
          <Link to="/contact" className="nav-link" onClick={() => setMenuOpen(false)}>
            Contact
          </Link>

          {user && <NotificationDropdown />}

          {user ? (
            <div className="user-menu">
              <button className="user-menu-btn">
                <FaUserCircle />
                <span>{user.firstName}</span>
                {user.role && <span className="user-role-badge">{user.role}</span>}
              </button>
              <div className="user-dropdown">
                <Link to="/profile" className="dropdown-item" onClick={() => setMenuOpen(false)}>
                  <FaUserCircle /> Profile
                </Link>
                <Link to="/my-reservations" className="dropdown-item" onClick={() => setMenuOpen(false)}>
                  <FaCalendarAlt /> My Reservations
                </Link>
                <Link to="/my-payments" className="dropdown-item" onClick={() => setMenuOpen(false)}>
                  <FaMoneyBillWave /> My Payments
                </Link>

                {/* Admin Link */}
                {user.role === 'ADMIN' && (
                  <>
                    <div className="dropdown-divider"></div>
                    <Link to="/admin" className="dropdown-item admin-link" onClick={() => setMenuOpen(false)}>
                      <FaUserShield /> Admin Dashboard
                    </Link>
                    <Link to="/admin/users" className="dropdown-item" onClick={() => setMenuOpen(false)}>
                      <FaUserCircle /> Manage Users
                    </Link>
                    <Link to="/admin/reservations" className="dropdown-item" onClick={() => setMenuOpen(false)}>
                      <FaCalendarCheck /> Reservations
                    </Link>
                    <Link to="/admin/reviews" className="dropdown-item" onClick={() => setMenuOpen(false)}>
                      <FaStar /> Reviews
                    </Link>
                  </>
                )}

                {/* Owner Link */}
                {(user.role === 'OWNER' || user.role === 'ADMIN') && (
                  <>
                    <div className="dropdown-divider"></div>
                    <Link to="/owner/restaurants" className="dropdown-item owner-link" onClick={() => setMenuOpen(false)}>
                      <FaUtensils /> My Restaurants
                    </Link>
                  </>
                )}

                <div className="dropdown-divider"></div>
                <button onClick={handleLogout} className="dropdown-item logout-item">
                  <FaSignOutAlt /> Logout
                </button>
              </div>
            </div>
          ) : (
            <div className="auth-buttons">
              <Link to="/login" className="btn btn-outline" onClick={() => setMenuOpen(false)}>
                Sign In
              </Link>
              <Link to="/register" className="btn btn-primary" onClick={() => setMenuOpen(false)}>
                Sign Up
              </Link>
            </div>
          )}
        </nav>
      </div>
    </header>
  );
};

export default Header;
