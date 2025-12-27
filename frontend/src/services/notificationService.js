import api from './api';

export const notificationService = {
  getUserNotifications: () => api.get('/notifications/user'),
  getUnreadNotifications: () => api.get('/notifications/user/unread'),
  markAsRead: (id) => api.put(`/notifications/${id}/read`),
  markAllAsRead: () => api.put('/notifications/read-all'),
  deleteNotification: (id) => api.delete(`/notifications/${id}`),
};
