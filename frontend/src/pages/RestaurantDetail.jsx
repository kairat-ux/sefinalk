import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { FaStar, FaMapMarkerAlt, FaPhone, FaEnvelope, FaClock, FaCalendarAlt, FaUsers } from 'react-icons/fa';
import { restaurantService } from '../services/restaurantService';
import { reservationService } from '../services/reservationService';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';
import './RestaurantDetail.css';

const RestaurantDetail = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const { user } = useAuth();
    const [restaurant, setRestaurant] = useState(null);
    const [loading, setLoading] = useState(true);
    const [bookingData, setBookingData] = useState({
        date: '',
        time: '',
        guests: 2
    });
    const [submitting, setSubmitting] = useState(false);

    useEffect(() => {
        fetchRestaurant();
    }, [id]);

    const fetchRestaurant = async () => {
        try {
            const response = await restaurantService.getRestaurantById(id);
            setRestaurant(response.data);
        } catch (error) {
            toast.error('Failed to load restaurant details');
            navigate('/restaurants');
        } finally {
            setLoading(false);
        }
    };

    const handleBookingChange = (e) => {
        setBookingData({
            ...bookingData,
            [e.target.name]: e.target.value
        });
    };

    const handleBooking = async (e) => {
        e.preventDefault();

        if (!user) {
            toast.info('Please login to make a reservation');
            navigate('/login');
            return;
        }

        if (!bookingData.date || !bookingData.time) {
            toast.error('Please select date and time');
            return;
        }

        setSubmitting(true);

        try {
            const [hours, minutes] = bookingData.time.split(':');
            const startHour = parseInt(hours);
            const endHour = (startHour + 2) % 24;
            const endTime = `${String(endHour).padStart(2, '0')}:${minutes}:00`;

            const reservationPayload = {
                restaurantId: parseInt(id),
                reservationDate: bookingData.date,
                startTime: `${bookingData.time}:00`,
                endTime: endTime,
                guestCount: parseInt(bookingData.guests),
                specialRequests: ''
            };

            console.log('Sending reservation:', reservationPayload);

            const response = await reservationService.createReservation(reservationPayload);
            console.log('Reservation response:', response.data);

            toast.success('Reservation created successfully!');
            navigate('/my-reservations');
        } catch (error) {
            console.error('Reservation error:', error.response?.data);
            toast.error(error.response?.data?.message || 'Failed to create reservation');
        } finally {
            setSubmitting(false);
        }
    };

    if (loading) {
        return (
            <div className="loading">
                <div className="spinner"></div>
            </div>
        );
    }

    if (!restaurant) {
        return null;
    }

    const defaultImage = 'https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=1200';

    return (
        <div className="restaurant-detail">
            <div className="detail-hero">
                <img
                    src={restaurant.imageUrl || defaultImage}
                    alt={restaurant.name}
                    onError={(e) => e.target.src = defaultImage}
                />
                <div className="detail-hero-overlay">
                    <div className="container">
                        <h1>{restaurant.name}</h1>
                        <div className="rating-badge">
                            <FaStar />
                            <span>{restaurant.rating || '5.0'}</span>
                            <span className="reviews">({restaurant.totalReviews || 0} reviews)</span>
                        </div>
                    </div>
                </div>
            </div>

            <div className="container">
                <div className="detail-content">
                    <div className="detail-main">
                        <section className="detail-section">
                            <h2>About</h2>
                            <p>{restaurant.description || 'Experience exceptional dining at this premier restaurant.'}</p>
                        </section>

                        <section className="detail-section">
                            <h2>Contact Information</h2>
                            <div className="contact-grid">
                                <div className="contact-item">
                                    <FaMapMarkerAlt />
                                    <div>
                                        <strong>Address</strong>
                                        <p>{restaurant.address}</p>
                                        <p>{restaurant.city}, {restaurant.zipCode}</p>
                                    </div>
                                </div>
                                <div className="contact-item">
                                    <FaPhone />
                                    <div>
                                        <strong>Phone</strong>
                                        <p>{restaurant.phone}</p>
                                    </div>
                                </div>
                                <div className="contact-item">
                                    <FaEnvelope />
                                    <div>
                                        <strong>Email</strong>
                                        <p>{restaurant.email}</p>
                                    </div>
                                </div>
                            </div>
                        </section>

                        <section className="detail-section">
                            <h2>Opening Hours</h2>
                            <div className="hours-grid">
                                <div className="hours-item">
                                    <span>Monday - Friday</span>
                                    <span>11:00 AM - 10:00 PM</span>
                                </div>
                                <div className="hours-item">
                                    <span>Saturday - Sunday</span>
                                    <span>10:00 AM - 11:00 PM</span>
                                </div>
                            </div>
                        </section>
                    </div>

                    <div className="detail-sidebar">
                        <div className="booking-card">
                            <h3>Make a Reservation</h3>
                            <form onSubmit={handleBooking} className="booking-form">
                                <div className="form-group">
                                    <label>
                                        <FaCalendarAlt />
                                        Date
                                    </label>
                                    <input
                                        type="date"
                                        name="date"
                                        value={bookingData.date}
                                        onChange={handleBookingChange}
                                        min={new Date().toISOString().split('T')[0]}
                                        required
                                    />
                                </div>

                                <div className="form-group">
                                    <label>
                                        <FaClock />
                                        Time
                                    </label>
                                    <select
                                        name="time"
                                        value={bookingData.time}
                                        onChange={handleBookingChange}
                                        required
                                    >
                                        <option value="">Select time</option>
                                        <option value="11:00">11:00 AM</option>
                                        <option value="12:00">12:00 PM</option>
                                        <option value="13:00">1:00 PM</option>
                                        <option value="14:00">2:00 PM</option>
                                        <option value="18:00">6:00 PM</option>
                                        <option value="19:00">7:00 PM</option>
                                        <option value="20:00">8:00 PM</option>
                                        <option value="21:00">9:00 PM</option>
                                    </select>
                                </div>

                                <div className="form-group">
                                    <label>
                                        <FaUsers />
                                        Number of Guests
                                    </label>
                                    <select
                                        name="guests"
                                        value={bookingData.guests}
                                        onChange={handleBookingChange}
                                        required
                                    >
                                        {[1, 2, 3, 4, 5, 6, 7, 8].map(num => (
                                            <option key={num} value={num}>{num} {num === 1 ? 'Guest' : 'Guests'}</option>
                                        ))}
                                    </select>
                                </div>

                                <button type="submit" className="btn btn-primary" disabled={submitting}>
                                    {submitting ? 'Booking...' : 'Reserve Table'}
                                </button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    );
};

export default RestaurantDetail;