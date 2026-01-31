import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import Navbar from '../components/Navbar';
import ChartMonthly from '../components/ChartMonthly';
import ProgressIndicator from '../components/ProgressIndicator';
import statsService from '../services/statsService';
import styles from '../styles/MonthlyStats.module.css';

const MonthlyStats = () => {
  const { token } = useAuth();
  const [loading, setLoading] = useState(true);
  const [monthlyData, setMonthlyData] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    if (token) {
      loadMonthlyStats();
    }
  }, [token]);

  const loadMonthlyStats = async () => {
    setLoading(true);
    setError('');
    try {
      const data = await statsService.getMonthlyStats();
      setMonthlyData(data);
    } catch (err) {
      setError(err.message || 'Failed to load monthly statistics');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <Navbar />
      <div className={styles.content}>
        <header className={styles.header}>
          <h1 className={styles.title}>Monthly Statistics</h1>
          <p className={styles.subtitle}>
            View your energy consumption for the current month
          </p>
        </header>

        {loading && (
          <div className={styles.loadingContainer}>
            <ProgressIndicator />
          </div>
        )}

        {error && !loading && (
          <div className={styles.errorContainer}>
            <div className={styles.errorBox}>
              <span className={styles.errorIcon}>!</span>
              <div>
                <p className={styles.errorText}>{error}</p>
                <button onClick={loadMonthlyStats} className={styles.retryButton}>
                  Try Again
                </button>
              </div>
            </div>
          </div>
        )}

        {!loading && !error && monthlyData && (
          <div className={styles.chartContainer}>
            <ChartMonthly monthlyData={monthlyData} />
          </div>
        )}

        {!loading && !error && !monthlyData && (
          <div className={styles.emptyState}>
            <p className={styles.emptyText}>
              No monthly statistics available. Start logging your consumption!
            </p>
          </div>
        )}
      </div>
    </div>
  );
};

export default MonthlyStats;
