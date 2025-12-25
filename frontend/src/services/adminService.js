import api from './api';

export const adminService = {
  // Users Management
  getAllUsers: () => api.get('/admin/users'),
  getUserById: (id) => api.get(`/admin/users/${id}`),
  blockUser: (id) => api.put(`/admin/users/${id}/block`),
  unblockUser: (id) => api.put(`/admin/users/${id}/unblock`),
  deleteUser: (id) => api.delete(`/admin/users/${id}`),

  // Restaurants Management
  getAllRestaurants: () => api.get('/admin/restaurants'),
  deleteRestaurant: (id) => api.delete(`/admin/restaurants/${id}`),

  // Statistics
  getUsersCount: () => api.get('/admin/stats/users-count'),
  getRestaurantsCount: () => api.get('/admin/stats/restaurants-count'),
};
