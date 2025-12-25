import api from './api';

export const ownerService = {
  getMyRestaurants: () => api.get('/owner/restaurants'),

  createRestaurant: (data) => api.post('/owner/restaurants', data),

  updateRestaurant: (id, data) => api.put(`/owner/restaurants/${id}`, data),

  deleteRestaurant: (id) => api.delete(`/owner/restaurants/${id}`),
};
