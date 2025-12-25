import api from './api';

export const reservationService = {
  createReservation: (data) => api.post('/reservations', data),

  getMyReservations: () => api.get('/reservations/my'),

  getReservationById: (id) => api.get(`/reservations/${id}`),

  updateReservation: (id, data) => api.put(`/reservations/${id}`, data),

  cancelReservation: (id) => api.delete(`/reservations/${id}`),

  confirmReservation: (id) => api.put(`/reservations/${id}/confirm`),
};
