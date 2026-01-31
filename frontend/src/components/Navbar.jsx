import React, { useContext } from 'react';
import { Link } from 'react-router-dom';
import { AuthContext } from '../context/AuthContext';
import styles from '../styles/Navbar.module.css';

const Navbar = () => {
  const { user, logout } = useContext(AuthContext);

  const handleLogout = () => {
    logout();
  };

  return (
    <nav className={styles.navbar}>
      <div className={styles.navContent}>
        {user && <span className={styles.username}>Welcome, {user.username}</span>}
        <ul className={styles.navLinks}>
          <li><Link to="/" className={styles.navLink}>Home</Link></li>
          <li><Link to="/appliances" className={styles.navLink}>Appliances</Link></li>
          <li><Link to="/weekly-stats" className={styles.navLink}>Weekly Stats</Link></li>
          <li><Link to="/monthly-stats" className={styles.navLink}>Monthly Stats</Link></li>
          <li><Link to="/suggestions" className={styles.navLink}>Suggestions</Link></li>
          {user && (
            <li>
              <button onClick={handleLogout} className={styles.logoutBtn}>
                Logout
              </button>
            </li>
          )}
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;
