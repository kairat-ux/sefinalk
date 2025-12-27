import api from './api';

export const paymentService = {
  createPayment: (data) => api.post('/payments', data),
  getPaymentById: (id) => api.get(`/payments/${id}`),
  getPaymentByReservation: (reservationId) => api.get(`/payments/reservation/${reservationId}`),
  getUserPayments: (userId) => api.get(`/payments/user/${userId}`),
  completePayment: (id) => api.put(`/payments/${id}/complete`),
  refundPayment: (id, reason) => api.put(`/payments/${id}/refund`, null, { params: { reason } }),
  calculateRefundAmount: (reservationId) => api.get(`/payments/reservation/${reservationId}/refund-amount`),
};
