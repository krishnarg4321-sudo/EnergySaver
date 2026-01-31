import React, { useState } from 'react';
import styles from '../styles/AlertBanner.module.css';

const AlertBanner = ({ alerts, onRespond }) => {
  const [responding, setResponding] = useState(null);
  const [reductionPercentage, setReductionPercentage] = useState(10);

  const unreadAlerts = alerts?.filter(alert => !alert.responded) || [];

  if (unreadAlerts.length === 0) {
    return null;
  }

  const handleRespondClick = (alertId) => {
    setResponding(alertId);
  };

  const handleSubmitResponse = async (alert) => {
    await onRespond(alert.id, reductionPercentage);
    setResponding(null);
    setReductionPercentage(10);
  };

  const handleCancelResponse = () => {
    setResponding(null);
    setReductionPercentage(10);
  };

  return (
    <div className={styles.bannerContainer}>
      {unreadAlerts.map(alert => (
        <div key={alert.id} className={styles.banner}>
          <div className={styles.bannerContent}>
            <div className={styles.alertIcon}>⚠️</div>
            <div className={styles.alertInfo}>
              <h4 className={styles.alertTitle}>Energy Alert</h4>
              <p className={styles.alertMessage}>{alert.message}</p>
              <p className={styles.alertDetails}>
                Appliance: <strong>{alert.applianceName}</strong>
              </p>
              {alert.hourlyConsumption && (
                <p className={styles.alertDetails}>
                  Hourly Consumption: <strong>{alert.hourlyConsumption.toFixed(2)} Wh</strong>
                </p>
              )}
            </div>
          </div>
          
          {responding === alert.id ? (
            <div className={styles.responseBox}>
              <label className={styles.sliderLabel}>
                Reduction Target: {reductionPercentage}%
              </label>
              <input
                type="range"
                min="5"
                max="50"
                step="5"
                value={reductionPercentage}
                onChange={(e) => setReductionPercentage(Number(e.target.value))}
                className={styles.slider}
              />
              <div className={styles.responseButtons}>
                <button
                  onClick={() => handleSubmitResponse(alert)}
                  className={styles.submitBtn}
                >
                  Submit
                </button>
                <button
                  onClick={handleCancelResponse}
                  className={styles.cancelBtn}
                >
                  Cancel
                </button>
              </div>
            </div>
          ) : (
            <button
              onClick={() => handleRespondClick(alert.id)}
              className={styles.respondBtn}
            >
              Respond
            </button>
          )}
        </div>
      ))}
    </div>
  );
};

export default AlertBanner;
