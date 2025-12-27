import React, { useState, useEffect } from 'react';
import { FaMoneyBillWave, FaCheckCircle, FaTimesCircle, FaClock, FaUndo } from 'react-icons/fa';
import { paymentService } from '../services/paymentService';
import { useAuth } from '../context/AuthContext';
import { toast } from 'react-toastify';
import { format } from 'date-fns';
import './MyPayments.css';

const MyPayments = () => {
  const { user } = useAuth();
  const [payments, setPayments] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (user) {
      fetchPayments();
    }
  }, [user]);

  const fetchPayments = async () => {
    try {
      const response = await paymentService.getUserPayments(user.id);
      setPayments(response.data);
    } catch (error) {
      toast.error('Failed to load payments');
      console.error(error);
    } finally {
      setLoading(false);
    }
  };

  const getStatusIcon = (status) => {
    switch (status) {
      case 'COMPLETED':
        return <FaCheckCircle className="status-icon success" />;
      case 'PENDING':
        return <FaClock className="status-icon warning" />;
      case 'FAILED':
        return <FaTimesCircle className="status-icon error" />;
      case 'REFUNDED':
        return <FaUndo className="status-icon info" />;
      default:
        return <FaClock className="status-icon" />;
    }
  };

  const getPaymentMethodLabel = (method) => {
    switch (method) {
      case 'CARD':
        return '💳 Card';
      case 'CASH':
        return '💵 Cash';
      case 'TRANSFER':
        return '🏦 Bank Transfer';
      default:
        return method;
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
    <div className="my-payments">
      <div className="page-hero">
        <div className="container">
          <h1>My Payments</h1>
          <p>View your payment history</p>
        </div>
      </div>

      <div className="container">
        {payments.length === 0 ? (
          <div className="empty-state">
            <FaMoneyBillWave className="empty-icon" />
            <h2>No Payments Yet</h2>
            <p>Your payment history will appear here</p>
          </div>
        ) : (
          <div className="payments-grid">
            {payments.map(payment => (
              <div key={payment.id} className={`payment-card status-${payment.status.toLowerCase()}`}>
                <div className="payment-header">
                  <div className="payment-status">
                    {getStatusIcon(payment.status)}
                    <span className="status-label">{payment.status}</span>
                  </div>
                  <div className="payment-amount">
                    {payment.amount} {payment.currency}
                  </div>
                </div>

                <div className="payment-details">
                  <div className="payment-row">
                    <span className="label">Transaction ID:</span>
                    <span className="value">{payment.transactionId}</span>
                  </div>
                  <div className="payment-row">
                    <span className="label">Payment Method:</span>
                    <span className="value">{getPaymentMethodLabel(payment.paymentMethod)}</span>
                  </div>
                  <div className="payment-row">
                    <span className="label">Reservation ID:</span>
                    <span className="value">#{payment.reservationId}</span>
                  </div>
                  <div className="payment-row">
                    <span className="label">Date:</span>
                    <span className="value">
                      {format(new Date(payment.createdAt), 'MMM dd, yyyy HH:mm')}
                    </span>
                  </div>
                </div>
              </div>
            ))}
          </div>
        )}
      </div>
    </div>
  );
};

export default MyPayments;
