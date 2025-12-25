import React, { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { FaUsers, FaUtensils, FaCalendarCheck, FaChartLine } from 'react-icons/fa';
import { adminService } from '../../services/adminService';
import { toast } from 'react-toastify';
import './AdminDashboard.css';

const AdminDashboard = () => {
  const [stats, setStats] = useState({
    usersCount: 0,
    restaurantsCount: 0
  });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    fetchStats();
  }, []);

  const fetchStats = async () => {
    try {
      const [usersResponse, restaurantsResponse] = await Promise.all([
        adminService.getUsersCount(),
        adminService.getRestaurantsCount()
      ]);

      setStats({
        usersCount: usersResponse.data,
        restaurantsCount: restaurantsResponse.data
      });
    } catch (error) {
      toast.error('Failed to load statistics');
    } finally {
      setLoading(false);
    }
  };

  const quickActions = [
    {
      title: 'Manage Users',
      icon: <FaUsers />,
      link: '/admin/users',
      color: '#3b82f6'
    },
    {
      title: 'Manage Restaurants',
      icon: <FaUtensils />,
      link: '/admin/restaurants',
      color: '#10b981'
    },
    {
      title: 'View Reservations',
      icon: <FaCalendarCheck />,
      link: '/admin/reservations',
      color: '#f59e0b'
    },
    {
      title: 'Analytics',
      icon: <FaChartLine />,
      link: '/admin/analytics',
      color: '#8b5cf6'
    }
  ];

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="admin-dashboard">
      <div className="page-hero">
        <div className="container">
          <h1>Admin Dashboard</h1>
          <p>Manage your restaurant reservation system</p>
        </div>
      </div>

      <div className="container">
        <div className="stats-grid">
          <div className="stat-card">
            <div className="stat-icon" style={{ background: 'linear-gradient(135deg, #3b82f6, #2563eb)' }}>
              <FaUsers />
            </div>
            <div className="stat-content">
              <h3>{stats.usersCount}</h3>
              <p>Total Users</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon" style={{ background: 'linear-gradient(135deg, #10b981, #059669)' }}>
              <FaUtensils />
            </div>
            <div className="stat-content">
              <h3>{stats.restaurantsCount}</h3>
              <p>Total Restaurants</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon" style={{ background: 'linear-gradient(135deg, #f59e0b, #d97706)' }}>
              <FaCalendarCheck />
            </div>
            <div className="stat-content">
              <h3>--</h3>
              <p>Active Reservations</p>
            </div>
          </div>

          <div className="stat-card">
            <div className="stat-icon" style={{ background: 'linear-gradient(135deg, var(--gold), var(--dark-gold))' }}>
              <FaChartLine />
            </div>
            <div className="stat-content">
              <h3>--</h3>
              <p>Monthly Revenue</p>
            </div>
          </div>
        </div>

        <div className="quick-actions-section">
          <h2>Quick Actions</h2>
          <div className="quick-actions-grid">
            {quickActions.map((action, index) => (
              <Link
                key={index}
                to={action.link}
                className="action-card"
                style={{ '--action-color': action.color }}
              >
                <div className="action-icon">{action.icon}</div>
                <h3>{action.title}</h3>
              </Link>
            ))}
          </div>
        </div>

        <div className="recent-activity">
          <h2>Recent Activity</h2>
          <div className="activity-list">
            <div className="activity-item">
              <div className="activity-icon">
                <FaUsers />
              </div>
              <div className="activity-content">
                <p><strong>New user registered:</strong> John Doe</p>
                <span>2 hours ago</span>
              </div>
            </div>

            <div className="activity-item">
              <div className="activity-icon">
                <FaUtensils />
              </div>
              <div className="activity-content">
                <p><strong>New restaurant added:</strong> Le Gourmet</p>
                <span>5 hours ago</span>
              </div>
            </div>

            <div className="activity-item">
              <div className="activity-icon">
                <FaCalendarCheck />
              </div>
              <div className="activity-content">
                <p><strong>Reservation confirmed:</strong> #12345</p>
                <span>1 day ago</span>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
};

export default AdminDashboard;
