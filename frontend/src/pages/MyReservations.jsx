    import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaCalendarAlt, FaClock, FaUsers, FaMapMarkerAlt, FaCheckCircle, FaHourglassHalf, FaTimesCircle } from 'react-icons/fa';
import { reservationService } from '../services/reservationService';
import { format } from 'date-fns';
import { toast } from 'react-toastify';
import './MyReservations.css';

const MyReservations = () => {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('all');

  useEffect(() => {
    fetchReservations();
  }, []);

  const fetchReservations = async () => {
    try {
      const response = await reservationService.getMyReservations();
      setReservations(response.data);
    } catch (error) {
      toast.error('Failed to load reservations');
    } finally {
      setLoading(false);
    }
  };

  const handleCancelReservation = async (id) => {
    if (!window.confirm('Are you sure you want to cancel this reservation?')) {
      return;
    }

    try {
      await reservationService.cancelReservation(id);
      toast.success('Reservation cancelled successfully');
      fetchReservations();
    } catch (error) {
      toast.error('Failed to cancel reservation');
    }
  };

  const getStatusIcon = (status) => {
    switch (status?.toLowerCase()) {
      case 'confirmed':
        return <FaCheckCircle className="status-icon confirmed" />;
      case 'pending':
        return <FaHourglassHalf className="status-icon pending" />;
      case 'cancelled':
        return <FaTimesCircle className="status-icon cancelled" />;
      default:
        return <FaHourglassHalf className="status-icon pending" />;
    }
  };

  const filteredReservations = reservations.filter(reservation => {
    if (filter === 'all') return true;
    return reservation.status?.toLowerCase() === filter;
  });

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="my-reservations">
      <div className="page-hero">
        <div className="container">
          <h1>My Reservations</h1>
          <p>Manage your restaurant bookings</p>
        </div>
      </div>

      <div className="container">
        <div className="filter-tabs">
          <button
            className={`filter-tab ${filter === 'all' ? 'active' : ''}`}
            onClick={() => setFilter('all')}
          >
            All Reservations
          </button>
          <button
            className={`filter-tab ${filter === 'pending' ? 'active' : ''}`}
            onClick={() => setFilter('pending')}
          >
            Pending
          </button>
          <button
            className={`filter-tab ${filter === 'confirmed' ? 'active' : ''}`}
            onClick={() => setFilter('confirmed')}
          >
            Confirmed
          </button>
          <button
            className={`filter-tab ${filter === 'cancelled' ? 'active' : ''}`}
            onClick={() => setFilter('cancelled')}
          >
            Cancelled
          </button>
        </div>

        {filteredReservations.length === 0 ? (
          <div className="no-reservations">
            <h3>No reservations found</h3>
            <p>Start exploring restaurants and make your first reservation</p>
            <Link to="/restaurants" className="btn btn-primary">
              Browse Restaurants
            </Link>
          </div>
        ) : (
          <div className="reservations-list">
            {filteredReservations.map(reservation => (
              <div key={reservation.id} className="reservation-card">
                <div className="reservation-header">
                  <div>
                    <h3>{reservation.restaurantName || 'Restaurant'}</h3>
                    <div className="reservation-location">
                      <FaMapMarkerAlt />
                      <span>{reservation.restaurantCity || 'Location'}</span>
                    </div>
                  </div>
                  <div className="reservation-status">
                    {getStatusIcon(reservation.status)}
                    <span className={`status-text ${reservation.status?.toLowerCase()}`}>
                      {reservation.status || 'Pending'}
                    </span>
                  </div>
                </div>

                <div className="reservation-details">
                  <div className="detail-item">
                    <FaCalendarAlt />
                    <div>
                      <strong>Date</strong>
                      <span>
                        {reservation.reservationTime
                          ? format(new Date(reservation.reservationTime), 'MMMM dd, yyyy')
                          : 'TBD'}
                      </span>
                    </div>
                  </div>

                  <div className="detail-item">
                    <FaClock />
                    <div>
                      <strong>Time</strong>
                      <span>
                        {reservation.reservationTime
                          ? format(new Date(reservation.reservationTime), 'h:mm a')
                          : 'TBD'}
                      </span>
                    </div>
                  </div>

                  <div className="detail-item">
                    <FaUsers />
                    <div>
                      <strong>Guests</strong>
                      <span>{reservation.numberOfGuests || 2} {reservation.numberOfGuests === 1 ? 'Guest' : 'Guests'}</span>
                    </div>
                  </div>
                </div>

                {reservation.specialRequests && (
                  <div className="special-requests">
                    <strong>Special Requests:</strong>
                    <p>{reservation.specialRequests}</p>
                  </div>
                )}

                <div className="reservation-actions">
                  <Link
                    to={`/restaurants/${reservation.restaurantId}`}
                    className="btn btn-outline"
                  >
                    View Restaurant
                  </Link>

                  {reservation.status?.toLowerCase() !== 'cancelled' && (
                    <button
                      onClick={() => handleCancelReservation(reservation.id)}
                      className="btn btn-cancel"
                    >
                      Cancel Reservation
                    </button>
                  )}
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default MyReservations;
