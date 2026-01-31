import React from 'react';
import {
  PieChart,
  Pie,
  Cell,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';
import styles from '../styles/ChartWeekly.module.css';

const COLORS = ['#FFD700', '#FFA500', '#FF8C00', '#FF6347', '#FF4500', '#DC143C'];

const ChartWeekly = ({ weeklyData }) => {
  if (!weeklyData) return null;

  const { totalWh, weekOverWeekPercentage, pieChartData, barChartData } = weeklyData;

  const isIncrease = weekOverWeekPercentage > 0;
  const comparisonIcon = isIncrease ? '↑' : '↓';
  const comparisonClass = isIncrease ? styles.increase : styles.decrease;

  return (
    <div className={styles.chartContainer}>
      <div className={styles.header}>
        <h2 className={styles.title}>Weekly Statistics</h2>
        <div className={styles.stats}>
          <div className={styles.totalWh}>
            <span className={styles.label}>Total Energy:</span>
            <span className={styles.value}>{totalWh.toLocaleString()} Wh</span>
          </div>
          <div className={`${styles.comparison} ${comparisonClass}`}>
            <span className={styles.comparisonIcon}>{comparisonIcon}</span>
            <span className={styles.comparisonValue}>
              {Math.abs(weekOverWeekPercentage).toFixed(1)}%
            </span>
            <span className={styles.comparisonLabel}>vs last week</span>
          </div>
        </div>
      </div>

      <div className={styles.chartsGrid}>
        <div className={styles.chartWrapper}>
          <h3 className={styles.chartTitle}>Appliance Consumption Breakdown</h3>
          <ResponsiveContainer width="100%" height={300}>
            <PieChart>
              <Pie
                data={pieChartData}
                cx="50%"
                cy="50%"
                labelLine={false}
                label={({ name, percent }) => `${name}: ${(percent * 100).toFixed(0)}%`}
                outerRadius={80}
                fill="#8884d8"
                dataKey="value"
              >
                {pieChartData.map((entry, index) => (
                  <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                ))}
              </Pie>
              <Tooltip
                contentStyle={{
                  backgroundColor: 'rgba(0, 0, 0, 0.8)',
                  border: '1px solid #FFD700',
                  borderRadius: '8px',
                  color: '#FFD700'
                }}
              />
              <Legend
                wrapperStyle={{
                  color: '#FFD700'
                }}
              />
            </PieChart>
          </ResponsiveContainer>
        </div>

        <div className={styles.chartWrapper}>
          <h3 className={styles.chartTitle}>Daily Consumption</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={barChartData}>
              <CartesianGrid strokeDasharray="3 3" stroke="rgba(255, 215, 0, 0.1)" />
              <XAxis
                dataKey="day"
                stroke="#FFD700"
                style={{ fontSize: '12px' }}
              />
              <YAxis
                stroke="#FFD700"
                style={{ fontSize: '12px' }}
                label={{ value: 'Wh', angle: -90, position: 'insideLeft', fill: '#FFD700' }}
              />
              <Tooltip
                contentStyle={{
                  backgroundColor: 'rgba(0, 0, 0, 0.8)',
                  border: '1px solid #FFD700',
                  borderRadius: '8px',
                  color: '#FFD700'
                }}
              />
              <Legend
                wrapperStyle={{
                  color: '#FFD700'
                }}
              />
              <Bar dataKey="consumption" fill="#FFD700" radius={[8, 8, 0, 0]} />
            </BarChart>
          </ResponsiveContainer>
        </div>
      </div>
    </div>
  );
};

export default ChartWeekly;
