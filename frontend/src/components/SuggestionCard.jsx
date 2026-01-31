import React from 'react';
import styles from '../styles/SuggestionCard.module.css';

const SuggestionCard = ({ suggestion }) => {
  const getCardClass = () => {
    switch (suggestion.suggestionType?.toLowerCase()) {
      case 'reduction':
        return styles.cardReduction;
      case 'positive':
        return styles.cardPositive;
      case 'cleanup':
        return styles.cardCleanup;
      default:
        return styles.cardDefault;
    }
  };

  const getTypeLabel = () => {
    const type = suggestion.suggestionType?.toLowerCase();
    switch (type) {
      case 'reduction':
        return '⚠️ Needs Attention';
      case 'positive':
        return '✓ Good Performance';
      case 'cleanup':
        return '🔧 Maintenance';
      default:
        return '💡 Suggestion';
    }
  };

  return (
    <div className={`${styles.card} ${getCardClass()}`}>
      <div className={styles.cardHeader}>
        <span className={styles.typeLabel}>{getTypeLabel()}</span>
      </div>
      <h3 className={styles.applianceName}>{suggestion.applianceName}</h3>
      <div className={styles.cardBody}>
        <div className={styles.statRow}>
          <span className={styles.label}>Total Consumption:</span>
          <span className={styles.value}>
            {suggestion.totalConsumption ? `${suggestion.totalConsumption.toFixed(2)} Wh` : 'N/A'}
          </span>
        </div>
        {suggestion.percentile !== undefined && suggestion.percentile !== null && (
          <div className={styles.statRow}>
            <span className={styles.label}>Percentile:</span>
            <span className={styles.value}>{suggestion.percentile.toFixed(1)}%</span>
          </div>
        )}
      </div>
      <div className={styles.messageBox}>
        <p className={styles.message}>{suggestion.message}</p>
      </div>
    </div>
  );
};

export default SuggestionCard;
