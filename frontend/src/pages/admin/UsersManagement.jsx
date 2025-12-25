import React, { useState, useEffect } from 'react';
import { FaSearch, FaUserCheck, FaUserTimes, FaTrash, FaUser } from 'react-icons/fa';
import { adminService } from '../../services/adminService';
import { toast } from 'react-toastify';
import { format } from 'date-fns';
import './UsersManagement.css';

const UsersManagement = () => {
  const [users, setUsers] = useState([]);
  const [filteredUsers, setFilteredUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchTerm, setSearchTerm] = useState('');
  const [roleFilter, setRoleFilter] = useState('');

  useEffect(() => {
    fetchUsers();
  }, []);

  useEffect(() => {
    filterUsers();
  }, [searchTerm, roleFilter, users]);

  const fetchUsers = async () => {
    try {
      const response = await adminService.getAllUsers();
      setUsers(response.data);
      setFilteredUsers(response.data);
    } catch (error) {
      toast.error('Failed to load users');
    } finally {
      setLoading(false);
    }
  };

  const filterUsers = () => {
    let filtered = users;

    if (searchTerm) {
      filtered = filtered.filter(user =>
        user.email?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.firstName?.toLowerCase().includes(searchTerm.toLowerCase()) ||
        user.lastName?.toLowerCase().includes(searchTerm.toLowerCase())
      );
    }

    if (roleFilter) {
      filtered = filtered.filter(user => user.role === roleFilter);
    }

    setFilteredUsers(filtered);
  };

  const handleBlockUser = async (id) => {
    if (!window.confirm('Are you sure you want to block this user?')) return;

    try {
      await adminService.blockUser(id);
      toast.success('User blocked successfully');
      fetchUsers();
    } catch (error) {
      toast.error('Failed to block user');
    }
  };

  const handleUnblockUser = async (id) => {
    try {
      await adminService.unblockUser(id);
      toast.success('User unblocked successfully');
      fetchUsers();
    } catch (error) {
      toast.error('Failed to unblock user');
    }
  };

  const handleDeleteUser = async (id) => {
    if (!window.confirm('Are you sure you want to delete this user? This action cannot be undone.')) return;

    try {
      await adminService.deleteUser(id);
      toast.success('User deleted successfully');
      fetchUsers();
    } catch (error) {
      toast.error('Failed to delete user');
    }
  };

  if (loading) {
    return (
      <div className="loading">
        <div className="spinner"></div>
      </div>
    );
  }

  return (
    <div className="users-management">
      <div className="page-hero">
        <div className="container">
          <h1>Users Management</h1>
          <p>Manage all registered users</p>
        </div>
      </div>

      <div className="container">
        <div className="management-header">
          <div className="search-box">
            <FaSearch />
            <input
              type="text"
              placeholder="Search by name or email..."
              value={searchTerm}
              onChange={(e) => setSearchTerm(e.target.value)}
            />
          </div>

          <select
            value={roleFilter}
            onChange={(e) => setRoleFilter(e.target.value)}
            className="role-filter"
          >
            <option value="">All Roles</option>
            <option value="USER">Users</option>
            <option value="OWNER">Owners</option>
            <option value="ADMIN">Admins</option>
          </select>
        </div>

        <div className="results-info">
          <p>{filteredUsers.length} users found</p>
        </div>

        <div className="users-table-container">
          <table className="users-table">
            <thead>
              <tr>
                <th>ID</th>
                <th>Name</th>
                <th>Email</th>
                <th>Phone</th>
                <th>Role</th>
                <th>Status</th>
                <th>Created</th>
                <th>Actions</th>
              </tr>
            </thead>
            <tbody>
              {filteredUsers.map(user => (
                <tr key={user.id}>
                  <td>{user.id}</td>
                  <td>
                    <div className="user-name">
                      <FaUser />
                      {user.firstName} {user.lastName}
                    </div>
                  </td>
                  <td>{user.email}</td>
                  <td>{user.phone || 'N/A'}</td>
                  <td>
                    <span className={`role-badge ${user.role?.toLowerCase()}`}>
                      {user.role}
                    </span>
                  </td>
                  <td>
                    <span className={`status-badge ${user.isActive ? 'active' : 'inactive'}`}>
                      {user.isActive ? 'Active' : 'Inactive'}
                    </span>
                  </td>
                  <td>
                    {user.createdAt ? format(new Date(user.createdAt), 'MMM dd, yyyy') : 'N/A'}
                  </td>
                  <td>
                    <div className="action-buttons">
                      {user.isActive ? (
                        <button
                          onClick={() => handleBlockUser(user.id)}
                          className="action-btn block-btn"
                          title="Block User"
                        >
                          <FaUserTimes />
                        </button>
                      ) : (
                        <button
                          onClick={() => handleUnblockUser(user.id)}
                          className="action-btn unblock-btn"
                          title="Unblock User"
                        >
                          <FaUserCheck />
                        </button>
                      )}
                      <button
                        onClick={() => handleDeleteUser(user.id)}
                        className="action-btn delete-btn"
                        title="Delete User"
                      >
                        <FaTrash />
                      </button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>

        {filteredUsers.length === 0 && (
          <div className="no-results">
            <p>No users found matching your criteria</p>
          </div>
        )}
      </div>
    </div>
  );
};

export default UsersManagement;
