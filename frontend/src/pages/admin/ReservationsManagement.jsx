import React, { useState, useEffect } from 'react';
import { FaCalendarCheck, FaTimes, FaEye } from 'react-icons/fa';
import api from '../../services/api';
import { toast } from 'react-toastify';
import './ReservationsManagement.css';

const ReservationsManagement = () => {
  const [reservations, setReservations] = useState([]);
  const [loading, setLoading] = useState(true);
  const [filter, setFilter] = useState('all'); // all, confirmed, pending, cancelled

  useEffect(() => {
    fetchReservations();
  }, []);

  const fetchReservations = async () => {
    setLoading(true);
    try {
      // Fetch all restaurants first
      const restaurantsResponse = await api.get('/restaurants');
      const allReservations = [];

      // Fetch reservations for each restaurant
      for (const restaurant of restaurantsResponse.data) {
        try {
          const reservationsResponse = await api.get(`/reservations/restaurant/${restaurant.id}`);
          const reservationsWithRestaurant = reservationsResponse.data.map(r => ({
            ...r,
            restaurantName: restaurant.name
          }));
          allReservations.push(...reservationsWithRestaurant);
        } catch (error) {
          console.error(`Error fetching reservations for restaurant ${restaurant.id}:`, error);
        }
      }

      setReservations(allReservations);
    } catch (error) {
      toast.error('Failed to load reservations');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const handleCancelReservation = async (id) => {
    if (!window.confirm('Are you sure you want to cancel this reservation?')) {
      return;
    }

    try {
      await api.delete(`/reservations/${id}?reason=Cancelled by admin`);
      toast.success('Reservation cancelled successfully');
      fetchReservations();
    } catch (error) {
      toast.error('Failed to cancel reservation');
    }
  };

  const getFilteredReservations = () => {
    if (filter === 'all') return reservations;
    return reservations.filter(r => r.status.toLowerCase() === filter.toLowerCase());
  };

  const getStatusClass = (status) => {
    switch (status.toLowerCase()) {
      case 'confirmed':
        return 'status-confirmed';
      case 'pending':
        return 'status-pending';
      case 'cancelled':
        return 'status-cancelled';
      case 'completed':
        return 'status-completed';
      default:
        return '';
    }
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  const filteredReservations = getFilteredReservations();

  return (
    <div className="reservations-management">
      <div className="page-hero">
        <div className="container">
          <h1>Reservations Management</h1>
          <p>View and manage all restaurant reservations</p>
        </div>
      </div>

      <div className="container">
        <div className="filters">
          <button
            className={filter === 'all' ? 'filter-btn active' : 'filter-btn'}
            onClick={() => setFilter('all')}
          >
            All Reservations ({reservations.length})
          </button>
          <button
            className={filter === 'confirmed' ? 'filter-btn active' : 'filter-btn'}
            onClick={() => setFilter('confirmed')}
          >
            Confirmed ({reservations.filter(r => r.status.toLowerCase() === 'confirmed').length})
          </button>
          <button
            className={filter === 'pending' ? 'filter-btn active' : 'filter-btn'}
            onClick={() => setFilter('pending')}
          >
            Pending ({reservations.filter(r => r.status.toLowerCase() === 'pending').length})
          </button>
          <button
            className={filter === 'cancelled' ? 'filter-btn active' : 'filter-btn'}
            onClick={() => setFilter('cancelled')}
          >
            Cancelled ({reservations.filter(r => r.status.toLowerCase() === 'cancelled').length})
          </button>
        </div>

        {filteredReservations.length === 0 ? (
          <div className="empty-state">
            <FaCalendarCheck size={48} />
            <p>No reservations found</p>
          </div>
        ) : (
          <div className="table-container">
            <table className="reservations-table">
              <thead>
                <tr>
                  <th>ID</th>
                  <th>Restaurant</th>
                  <th>Date</th>
                  <th>Time</th>
                  <th>Guests</th>
                  <th>Status</th>
                  <th>Created</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredReservations.map((reservation) => (
                  <tr key={reservation.id}>
                    <td>#{reservation.id}</td>
                    <td>{reservation.restaurantName || `Restaurant ${reservation.restaurantId}`}</td>
                    <td>{new Date(reservation.reservationDate).toLocaleDateString()}</td>
                    <td>
                      {reservation.startTime} - {reservation.endTime}
                    </td>
                    <td>{reservation.guestCount} guests</td>
                    <td>
                      <span className={`status-badge ${getStatusClass(reservation.status)}`}>
                        {reservation.status}
                      </span>
                    </td>
                    <td>{new Date(reservation.createdAt).toLocaleDateString()}</td>
                    <td>
                      <div className="action-buttons">
                        {reservation.status.toLowerCase() !== 'cancelled' && (
                          <button
                            className="btn-cancel"
                            onClick={() => handleCancelReservation(reservation.id)}
                            title="Cancel Reservation"
                          >
                            <FaTimes />
                          </button>
                        )}
                      </div>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </div>
        )}
      </div>
    </div>
  );
};

export default ReservationsManagement;
