import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import Navbar from '../components/Navbar';
import ChartWeekly from '../components/ChartWeekly';
import ProgressIndicator from '../components/ProgressIndicator';
import statsService from '../services/statsService';
import styles from '../styles/WeeklyStats.module.css';

const WeeklyStats = () => {
  const { token } = useAuth();
  const [loading, setLoading] = useState(true);
  const [weeklyData, setWeeklyData] = useState(null);
  const [error, setError] = useState('');

  useEffect(() => {
    if (token) {
      loadWeeklyStats();
    }
  }, [token]);

  const loadWeeklyStats = async () => {
    setLoading(true);
    setError('');
    try {
      const data = await statsService.getWeeklyStats();
      setWeeklyData(data);
    } catch (err) {
      setError(err.message || 'Failed to load weekly statistics');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <Navbar />
      <div className={styles.content}>
        <header className={styles.header}>
          <h1 className={styles.title}>Weekly Statistics</h1>
          <p className={styles.subtitle}>
            View your energy consumption for the past 7 days
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
                <button onClick={loadWeeklyStats} className={styles.retryButton}>
                  Try Again
                </button>
              </div>
            </div>
          </div>
        )}

        {!loading && !error && weeklyData && (
          <div className={styles.chartContainer}>
            <ChartWeekly weeklyData={weeklyData} />
          </div>
        )}

        {!loading && !error && !weeklyData && (
          <div className={styles.emptyState}>
            <p className={styles.emptyText}>
              No weekly statistics available. Start logging your consumption!
            </p>
          </div>
        )}
      </div>
    </div>
  );
};

export default WeeklyStats;
