import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import Navbar from '../components/Navbar';
import SuggestionCard from '../components/SuggestionCard';
import ProgressIndicator from '../components/ProgressIndicator';
import suggestionService from '../services/suggestionService';
import styles from '../styles/Suggestions.module.css';

const Suggestions = () => {
  const { token } = useAuth();
  const [loading, setLoading] = useState(true);
  const [suggestions, setSuggestions] = useState([]);
  const [error, setError] = useState('');

  useEffect(() => {
    if (token) {
      loadSuggestions();
    }
  }, [token]);

  const loadSuggestions = async () => {
    setLoading(true);
    setError('');
    try {
      const data = await suggestionService.getSuggestions();
      setSuggestions(data);
    } catch (err) {
      setError(err.message || 'Failed to load suggestions');
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className={styles.container}>
      <Navbar />
      <div className={styles.content}>
        <header className={styles.header}>
          <h1 className={styles.title}>AI Suggestions</h1>
          <p className={styles.subtitle}>
            Personalized recommendations to optimize your energy consumption
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
                <button onClick={loadSuggestions} className={styles.retryButton}>
                  Try Again
                </button>
              </div>
            </div>
          </div>
        )}

        {!loading && !error && suggestions.length > 0 && (
          <div className={styles.suggestionsGrid}>
            {suggestions.map((suggestion, index) => (
              <SuggestionCard key={index} suggestion={suggestion} />
            ))}
          </div>
        )}

        {!loading && !error && suggestions.length === 0 && (
          <div className={styles.emptyState}>
            <div className={styles.emptyCard}>
              <div className={styles.emptyIcon}>✓</div>
              <h2 className={styles.emptyTitle}>No Suggestions Available</h2>
              <p className={styles.emptyText}>
                Great job! Your energy consumption is well managed. Keep logging
                your usage to receive personalized insights and recommendations.
              </p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default Suggestions;
