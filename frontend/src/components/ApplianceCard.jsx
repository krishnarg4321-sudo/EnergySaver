import React, { useState } from 'react';
import styles from '../styles/ApplianceCard.module.css';

const ApplianceCard = ({ appliance, onDelete }) => {
  const [showConfirm, setShowConfirm] = useState(false);

  const handleDeleteClick = () => {
    setShowConfirm(true);
  };

  const handleConfirmDelete = () => {
    onDelete(appliance.id);
    setShowConfirm(false);
  };

  const handleCancelDelete = () => {
    setShowConfirm(false);
  };

  return (
    <div className={styles.card}>
      <div className={styles.cardHeader}>
        <h3 className={styles.applianceName}>
          {appliance.effectiveName || appliance.name}
        </h3>
        <span className={styles.category}>{appliance.category}</span>
      </div>
      <div className={styles.cardBody}>
        <div className={styles.info}>
          <span className={styles.label}>Rated Power:</span>
          <span className={styles.value}>{appliance.effectiveRatedWatts || appliance.ratedWatts}W</span>
        </div>
      </div>
      {!showConfirm ? (
        <button onClick={handleDeleteClick} className={styles.deleteBtn}>
          Delete
        </button>
      ) : (
        <div className={styles.confirmBox}>
          <p className={styles.confirmText}>Are you sure?</p>
          <div className={styles.confirmButtons}>
            <button onClick={handleConfirmDelete} className={styles.confirmBtn}>
              Yes
            </button>
            <button onClick={handleCancelDelete} className={styles.cancelBtn}>
              No
            </button>
          </div>
        </div>
      )}
    </div>
  );
};

export default ApplianceCard;
