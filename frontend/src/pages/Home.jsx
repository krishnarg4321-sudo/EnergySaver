import React from 'react';
import { Link } from 'react-router-dom';
import { useAuth } from '../context/AuthContext';
import Navbar from '../components/Navbar';
import styles from '../styles/Home.module.css';

const Home = () => {
  const { isAuthenticated } = useAuth();

  return (
    <div className={styles.container}>
      <Navbar />
      <div className={styles.content}>
        <header className={styles.header}>
          <h1 className={styles.title}>Energy Monitor</h1>
          <p className={styles.subtitle}>
            Track, analyze, and optimize your energy consumption
          </p>
        </header>

        <section className={styles.featureGrid}>
          <div className={styles.featureCard}>
            <h3 className={styles.featureTitle}>Track Appliances</h3>
            <p className={styles.featureDescription}>
              Monitor your home appliances and log their daily energy consumption
              with precision and ease.
            </p>
          </div>

          <div className={styles.featureCard}>
            <h3 className={styles.featureTitle}>Analyze Statistics</h3>
            <p className={styles.featureDescription}>
              View detailed weekly and monthly statistics to understand your
              energy usage patterns over time.
            </p>
          </div>

          <div className={styles.featureCard}>
            <h3 className={styles.featureTitle}>AI Suggestions</h3>
            <p className={styles.featureDescription}>
              Receive personalized recommendations to reduce energy consumption
              and save on electricity bills.
            </p>
          </div>

          <div className={styles.featureCard}>
            <h3 className={styles.featureTitle}>Smart Alerts</h3>
            <p className={styles.featureDescription}>
              Get notified when unusual consumption patterns are detected to
              prevent energy waste.
            </p>
          </div>
        </section>

        <section className={styles.achievementSection}>
          <h2 className={styles.sectionTitle}>Why Choose Energy Monitor?</h2>
          <div className={styles.achievementGrid}>
            <div className={styles.achievementCard}>
              <div className={styles.achievementNumber}>24/7</div>
              <p className={styles.achievementLabel}>Continuous Monitoring</p>
            </div>

            <div className={styles.achievementCard}>
              <div className={styles.achievementNumber}>100%</div>
              <p className={styles.achievementLabel}>Accurate Tracking</p>
            </div>

            <div className={styles.achievementCard}>
              <div className={styles.achievementNumber}>AI</div>
              <p className={styles.achievementLabel}>Powered Insights</p>
            </div>
          </div>
        </section>

        <section className={styles.ctaSection}>
          {!isAuthenticated ? (
            <div className={styles.ctaButtons}>
              <Link to="/login" className={styles.ctaButton}>
                Login
              </Link>
              <Link to="/register" className={styles.ctaButtonSecondary}>
                Register
              </Link>
            </div>
          ) : (
            <div className={styles.ctaButtons}>
              <Link to="/appliances" className={styles.ctaButton}>
                Go to Dashboard
              </Link>
            </div>
          )}
        </section>
      </div>
    </div>
  );
};

export default Home;
