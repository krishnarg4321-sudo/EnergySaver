import React, { useState, useEffect } from 'react';
import { useAuth } from '../context/AuthContext';
import Navbar from '../components/Navbar';
import ApplianceCard from '../components/ApplianceCard';
import AlertBanner from '../components/AlertBanner';
import ProgressIndicator from '../components/ProgressIndicator';
import applianceService from '../services/applianceService';
import consumptionService from '../services/consumptionService';
import alertService from '../services/alertService';
import styles from '../styles/Appliances.module.css';

const Appliances = () => {
  const { token } = useAuth();
  const [loading, setLoading] = useState(true);
  const [userAppliances, setUserAppliances] = useState([]);
  const [availableAppliances, setAvailableAppliances] = useState([]);
  const [alerts, setAlerts] = useState([]);
  const [error, setError] = useState('');
  const [success, setSuccess] = useState('');

  const [selectedAppliance, setSelectedAppliance] = useState('');
  const [customName, setCustomName] = useState('');
  const [ratedWatts, setRatedWatts] = useState('');
  const [addingAppliance, setAddingAppliance] = useState(false);

  const [logApplianceId, setLogApplianceId] = useState('');
  const [logDate, setLogDate] = useState('');
  const [hoursUsed, setHoursUsed] = useState('');
  const [loggingConsumption, setLoggingConsumption] = useState(false);

  useEffect(() => {
    if (token) {
      loadData();
    }
  }, [token]);

  const loadData = async () => {
    setLoading(true);
    try {
      const [userApps, availableApps, alertsData] = await Promise.all([
        applianceService.getUserAppliances(),
        applianceService.getAllAppliances(),
        alertService.getUserAlerts()
      ]);
      
      setUserAppliances(userApps);
      setAvailableAppliances(availableApps);
      setAlerts(alertsData);
    } catch (err) {
      setError(err.message || 'Failed to load data');
    } finally {
      setLoading(false);
    }
  };

  const handleAddAppliance = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!selectedAppliance || !customName.trim() || !ratedWatts) {
      setError('Please fill in all fields');
      return;
    }

    if (ratedWatts <= 0) {
      setError('Rated watts must be greater than 0');
      return;
    }

    setAddingAppliance(true);

    try {
      await applianceService.addAppliance(selectedAppliance, customName, parseFloat(ratedWatts));
      setSuccess('Appliance added successfully!');
      setSelectedAppliance('');
      setCustomName('');
      setRatedWatts('');
      loadData();
    } catch (err) {
      setError(err.message || 'Failed to add appliance');
    } finally {
      setAddingAppliance(false);
    }
  };

  const handleDeleteAppliance = async (userApplianceId) => {
    try {
      await applianceService.deleteAppliance(userApplianceId);
      setSuccess('Appliance deleted successfully!');
      loadData();
    } catch (err) {
      setError(err.message || 'Failed to delete appliance');
    }
  };

  const handleLogConsumption = async (e) => {
    e.preventDefault();
    setError('');
    setSuccess('');

    if (!logApplianceId || !logDate || !hoursUsed) {
      setError('Please fill in all consumption fields');
      return;
    }

    if (hoursUsed <= 0 || hoursUsed > 24) {
      setError('Hours used must be between 0 and 24');
      return;
    }

    setLoggingConsumption(true);

    try {
      await consumptionService.logConsumption(logApplianceId, logDate, parseFloat(hoursUsed));
      setSuccess('Consumption logged successfully!');
      setLogApplianceId('');
      setLogDate('');
      setHoursUsed('');
      loadData();
    } catch (err) {
      setError(err.message || 'Failed to log consumption');
    } finally {
      setLoggingConsumption(false);
    }
  };

  const handleAlertRespond = async (alertId, adjustmentPercentage) => {
    try {
      await alertService.respondToAlert(alertId, 'acknowledged', adjustmentPercentage);
      setSuccess('Alert response submitted!');
      loadData();
    } catch (err) {
      setError(err.message || 'Failed to respond to alert');
    }
  };

  if (loading) {
    return (
      <div className={styles.container}>
        <Navbar />
        <div className={styles.loadingContainer}>
          <ProgressIndicator />
        </div>
      </div>
    );
  }

  return (
    <div className={styles.container}>
      <Navbar />
      <div className={styles.content}>
        <AlertBanner alerts={alerts} onRespond={handleAlertRespond} />

        {error && (
          <div className={styles.errorBox}>
            <span className={styles.errorIcon}>!</span>
            <p className={styles.errorText}>{error}</p>
          </div>
        )}

        {success && (
          <div className={styles.successBox}>
            <span className={styles.successIcon}>✓</span>
            <p className={styles.successText}>{success}</p>
          </div>
        )}

        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>Add New Appliance</h2>
          <form onSubmit={handleAddAppliance} className={styles.form}>
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label className={styles.label}>Appliance Type</label>
                <select
                  value={selectedAppliance}
                  onChange={(e) => setSelectedAppliance(e.target.value)}
                  className={styles.select}
                  disabled={addingAppliance}
                >
                  <option value="">Select an appliance</option>
                  {availableAppliances.map((app) => (
                    <option key={app.id} value={app.id}>
                      {app.name} ({app.category})
                    </option>
                  ))}
                </select>
              </div>

              <div className={styles.formGroup}>
                <label className={styles.label}>Custom Name</label>
                <input
                  type="text"
                  value={customName}
                  onChange={(e) => setCustomName(e.target.value)}
                  className={styles.input}
                  placeholder="e.g., Living Room TV"
                  disabled={addingAppliance}
                />
              </div>

              <div className={styles.formGroup}>
                <label className={styles.label}>Rated Watts</label>
                <input
                  type="number"
                  value={ratedWatts}
                  onChange={(e) => setRatedWatts(e.target.value)}
                  className={styles.input}
                  placeholder="e.g., 100"
                  min="0"
                  step="0.01"
                  disabled={addingAppliance}
                />
              </div>

              <button
                type="submit"
                className={styles.submitButton}
                disabled={addingAppliance}
              >
                {addingAppliance ? 'Adding...' : 'Add Appliance'}
              </button>
            </div>
          </form>
        </section>

        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>Your Appliances</h2>
          {userAppliances.length === 0 ? (
            <div className={styles.emptyState}>
              <p className={styles.emptyText}>No appliances added yet</p>
            </div>
          ) : (
            <div className={styles.applianceGrid}>
              {userAppliances.map((appliance) => (
                <ApplianceCard
                  key={appliance.id}
                  appliance={{
                    id: appliance.id,
                    effectiveName: appliance.custom_name,
                    name: appliance.appliance_name,
                    category: appliance.appliance_category,
                    effectiveRatedWatts: appliance.rated_watts,
                    ratedWatts: appliance.rated_watts
                  }}
                  onDelete={handleDeleteAppliance}
                />
              ))}
            </div>
          )}
        </section>

        <section className={styles.section}>
          <h2 className={styles.sectionTitle}>Log Consumption</h2>
          <form onSubmit={handleLogConsumption} className={styles.form}>
            <div className={styles.formRow}>
              <div className={styles.formGroup}>
                <label className={styles.label}>Select Appliance</label>
                <select
                  value={logApplianceId}
                  onChange={(e) => setLogApplianceId(e.target.value)}
                  className={styles.select}
                  disabled={loggingConsumption}
                >
                  <option value="">Select an appliance</option>
                  {userAppliances.map((app) => (
                    <option key={app.id} value={app.id}>
                      {app.custom_name}
                    </option>
                  ))}
                </select>
              </div>

              <div className={styles.formGroup}>
                <label className={styles.label}>Date</label>
                <input
                  type="date"
                  value={logDate}
                  onChange={(e) => setLogDate(e.target.value)}
                  className={styles.input}
                  max={new Date().toISOString().split('T')[0]}
                  disabled={loggingConsumption}
                />
              </div>

              <div className={styles.formGroup}>
                <label className={styles.label}>Hours Used</label>
                <input
                  type="number"
                  value={hoursUsed}
                  onChange={(e) => setHoursUsed(e.target.value)}
                  className={styles.input}
                  placeholder="e.g., 5.5"
                  min="0"
                  max="24"
                  step="0.1"
                  disabled={loggingConsumption}
                />
              </div>

              <button
                type="submit"
                className={styles.submitButton}
                disabled={loggingConsumption}
              >
                {loggingConsumption ? 'Logging...' : 'Log Consumption'}
              </button>
            </div>
          </form>
        </section>
      </div>
    </div>
  );
};

export default Appliances;
