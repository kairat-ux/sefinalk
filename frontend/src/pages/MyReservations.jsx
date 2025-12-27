    import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaCalendarAlt, FaClock, FaUsers, FaMapMarkerAlt, FaCheckCircle, FaHourglassHalf, FaTimesCircle, FaMoneyBillWave } from 'react-icons/fa';
import { reservationService } from '../services/reservationService';
import { paymentService } from '../services/paymentService';
import { format } from 'date-fns';
import { toast } from 'react-toastify';
import './MyReservations.css';

const MyReservations = () => {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('all');
  const [payments, setPayments] = useState({});
  const [payingReservation, setPayingReservation] = useState(null);

  useEffect(() => {
    fetchReservations();
    fetchPayments();
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

  const fetchPayments = async () => {
    try {
      const user = JSON.parse(localStorage.getItem('user'));
      if (user && user.id) {
        const response = await paymentService.getUserPayments(user.id);
        const paymentMap = {};
        response.data.forEach(payment => {
          paymentMap[payment.reservationId] = payment;
        });
        setPayments(paymentMap);
      }
    } catch (error) {
      console.error('Failed to load payments:', error);
    }
  };

  const handlePayNow = async (reservation) => {
    if (!window.confirm(`Оплатить резервацию на сумму 5000 KZT?`)) {
      return;
    }

    setPayingReservation(reservation.id);
    try {
      const paymentData = {
        reservationId: reservation.id,
        amount: 5000.00,
        paymentMethod: 'CARD'
      };

      const response = await paymentService.createPayment(paymentData);
      toast.success('Оплата создана успешно!');

      // Auto-complete the payment
      await paymentService.completePayment(response.data.id);
      toast.success('Оплата завершена!');

      fetchPayments();
    } catch (error) {
      toast.error('Ошибка при оплате');
      console.error('Payment error:', error);
    } finally {
      setPayingReservation(null);
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

                  {reservation.status?.toLowerCase() === 'confirmed' && !payments[reservation.id] && (
                    <button
                      onClick={() => handlePayNow(reservation)}
                      className="btn btn-primary"
                      disabled={payingReservation === reservation.id}
                    >
                      <FaMoneyBillWave /> {payingReservation === reservation.id ? 'Processing...' : 'Pay Now (5000 KZT)'}
                    </button>
                  )}

                  {payments[reservation.id]?.status === 'COMPLETED' && (
                    <span className="payment-badge paid">
                      <FaCheckCircle /> Paid
                    </span>
                  )}

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
