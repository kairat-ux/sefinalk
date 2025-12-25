import api from './api';

export const restaurantService = {
  getAllRestaurants: () => api.get('/restaurants'),

  getRestaurantById: (id) => api.get(`/restaurants/${id}`),

  getRestaurantsByCity: (city) => api.get(`/restaurants/city/${city}`),

  createRestaurant: (data) => api.post('/restaurants', data),

  updateRestaurant: (id, data) => api.put(`/restaurants/${id}`, data),

  deleteRestaurant: (id) => api.delete(`/restaurants/${id}`),
};
