import React from 'react';
import { Link } from 'react-router-dom';
import { FaFacebookF, FaInstagram, FaTwitter, FaLinkedinIn, FaPhone, FaEnvelope, FaMapMarkerAlt } from 'react-icons/fa';
import './Footer.css';

const Footer = () => {
  return (
    <footer className="footer">
      <div className="footer-top">
        <div className="container">
          <div className="footer-grid">
            <div className="footer-col">
              <div className="footer-logo">
                <div className="logo-icon">LR</div>
                <h3>Luxury Reservations</h3>
              </div>
              <p className="footer-desc">
                Experience the finest dining establishments with seamless reservations.
                Your gateway to unforgettable culinary journeys.
              </p>
              <div className="social-links">
                <a href="#" className="social-link"><FaFacebookF /></a>
                <a href="#" className="social-link"><FaInstagram /></a>
                <a href="#" className="social-link"><FaTwitter /></a>
                <a href="#" className="social-link"><FaLinkedinIn /></a>
              </div>
            </div>

            <div className="footer-col">
              <h4>Quick Links</h4>
              <ul className="footer-links">
                <li><Link to="/restaurants">Find Restaurants</Link></li>
                <li><Link to="/about">About Us</Link></li>
                <li><Link to="/contact">Contact</Link></li>
                <li><Link to="/faq">FAQ</Link></li>
                <li><Link to="/careers">Careers</Link></li>
              </ul>
            </div>

            <div className="footer-col">
              <h4>For Restaurants</h4>
              <ul className="footer-links">
                <li><Link to="/partner">Become a Partner</Link></li>
                <li><Link to="/owner-login">Owner Login</Link></li>
                <li><Link to="/help">Help Center</Link></li>
                <li><Link to="/terms">Terms of Service</Link></li>
                <li><Link to="/privacy">Privacy Policy</Link></li>
              </ul>
            </div>

            <div className="footer-col">
              <h4>Contact Us</h4>
              <ul className="contact-info">
                <li>
                  <FaPhone />
                  <span>+1 (555) 123-4567</span>
                </li>
                <li>
                  <FaEnvelope />
                  <span>info@luxuryreservations.com</span>
                </li>
                <li>
                  <FaMapMarkerAlt />
                  <span>123 Fine Dining Street<br />New York, NY 10001</span>
                </li>
              </ul>
            </div>
          </div>
        </div>
      </div>

      <div className="footer-bottom">
        <div className="container">
          <div className="footer-bottom-content">
            <p>&copy; 2024 Luxury Reservations. All rights reserved.</p>
            <div className="footer-bottom-links">
              <Link to="/terms">Terms</Link>
              <Link to="/privacy">Privacy</Link>
              <Link to="/cookies">Cookies</Link>
            </div>
          </div>
        </div>
      </div>
    </footer>
  );
};

export default Footer;
