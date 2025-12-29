import api from './api';

export const reservationService = {
    createReservation: (data) => api.post('/reservations', data),

    getMyReservations: () => {
        const user = JSON.parse(localStorage.getItem('user'));
        const userId = user?.id || 1;
        return api.get(`/reservations/user/${userId}`);
    },

    getReservationById: (id) => api.get(`/reservations/${id}`),

    updateReservation: (id, data) => api.put(`/reservations/${id}`, data),

    cancelReservation: (id, reason = 'Cancelled by user') =>
        api.delete(`/reservations/${id}?reason=${encodeURIComponent(reason)}`),

    confirmReservation: (id) => api.put(`/reservations/${id}/confirm`),

    isTableAvailable: (tableId, date) =>
        api.get(`/reservations/${tableId}/available/${date}`),

    getRestaurantReservations: (restaurantId) =>
        api.get(`/reservations/restaurant/${restaurantId}`),

    getReservationsByDate: (restaurantId, date) =>
        api.get(`/reservations/restaurant/${restaurantId}/date/${date}`),
};