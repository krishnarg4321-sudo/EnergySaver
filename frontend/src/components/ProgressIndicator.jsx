import React from 'react';
import styles from '../styles/ProgressIndicator.module.css';

const ProgressIndicator = () => {
  return (
    <div className={styles.container}>
      <div className={styles.spinner}>
        <div className={styles.spinnerInner}></div>
      </div>
      <p className={styles.loadingText}>Loading...</p>
    </div>
  );
};

export default ProgressIndicator;
