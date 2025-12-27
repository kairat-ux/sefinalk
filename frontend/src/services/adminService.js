import api from './api';

export const adminService = {
  // Users Management
  getAllUsers: () => api.get('/users'),
  getUserById: (id) => api.get(`/users/${id}`),
  blockUser: (id) => api.put(`/users/${id}/block`),
  unblockUser: (id) => api.put(`/users/${id}/unblock`),
  updateUserRole: (id, role) => api.put(`/users/${id}/role`, { role }),
  deleteUser: (id) => api.delete(`/users/${id}`),

  // Restaurants Management
  getAllRestaurants: () => api.get('/restaurants'),
  deleteRestaurant: (id) => api.delete(`/restaurants/${id}`),

  // Reservations Management
  getAllReservations: async () => {
    const restaurants = await api.get('/restaurants');
    let allReservations = [];

    for (const restaurant of restaurants.data) {
      try {
        const reservations = await api.get(`/reservations/restaurant/${restaurant.id}`);
        allReservations = [...allReservations, ...reservations.data];
      } catch (error) {
        console.error(`Error fetching reservations for restaurant ${restaurant.id}:`, error);
      }
    }

    return { data: allReservations };
  },

  getRestaurantReservations: (restaurantId) => api.get(`/reservations/restaurant/${restaurantId}`),

  // Statistics
  getUsersCount: () => api.get('/admin/stats/users-count'),
  getRestaurantsCount: () => api.get('/admin/stats/restaurants-count'),
};
