import React from 'react';
import {
  LineChart,
  Line,
  BarChart,
  Bar,
  XAxis,
  YAxis,
  CartesianGrid,
  Tooltip,
  Legend,
  ResponsiveContainer
} from 'recharts';
import styles from '../styles/ChartMonthly.module.css';

const ChartMonthly = ({ monthlyData }) => {
  if (!monthlyData) return null;

  const { totalKwh, monthOverMonthPercentage, lineChartData, barChartData } = monthlyData;

  const isIncrease = monthOverMonthPercentage > 0;
  const comparisonIcon = isIncrease ? '↑' : '↓';
  const comparisonClass = isIncrease ? styles.increase : styles.decrease;

  return (
    <div className={styles.chartContainer}>
      <div className={styles.header}>
        <h2 className={styles.title}>Monthly Statistics</h2>
        <div className={styles.stats}>
          <div className={styles.totalKwh}>
            <span className={styles.label}>Total Energy:</span>
            <span className={styles.value}>{totalKwh.toFixed(2)} kWh</span>
          </div>
          <div className={`${styles.comparison} ${comparisonClass}`}>
            <span className={styles.comparisonIcon}>{comparisonIcon}</span>
            <span className={styles.comparisonValue}>
              {Math.abs(monthOverMonthPercentage).toFixed(1)}%
            </span>
            <span className={styles.comparisonLabel}>vs last month</span>
          </div>
        </div>
      </div>

      <div className={styles.chartsGrid}>
        <div className={styles.chartWrapper}>
          <h3 className={styles.chartTitle}>Daily Consumption Trend</h3>
          <ResponsiveContainer width="100%" height={300}>
            <LineChart data={lineChartData}>
              <CartesianGrid strokeDasharray="3 3" stroke="rgba(255, 215, 0, 0.1)" />
              <XAxis
                dataKey="day"
                stroke="#FFD700"
                style={{ fontSize: '12px' }}
              />
              <YAxis
                stroke="#FFD700"
                style={{ fontSize: '12px' }}
                label={{ value: 'kWh', angle: -90, position: 'insideLeft', fill: '#FFD700' }}
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
              <Line
                type="monotone"
                dataKey="consumption"
                stroke="#FFD700"
                strokeWidth={2}
                dot={{ fill: '#FFD700', r: 4 }}
                activeDot={{ r: 6 }}
              />
            </LineChart>
          </ResponsiveContainer>
        </div>

        <div className={styles.chartWrapper}>
          <h3 className={styles.chartTitle}>Current vs Previous Month</h3>
          <ResponsiveContainer width="100%" height={300}>
            <BarChart data={barChartData}>
              <CartesianGrid strokeDasharray="3 3" stroke="rgba(255, 215, 0, 0.1)" />
              <XAxis
                dataKey="month"
                stroke="#FFD700"
                style={{ fontSize: '12px' }}
              />
              <YAxis
                stroke="#FFD700"
                style={{ fontSize: '12px' }}
                label={{ value: 'kWh', angle: -90, position: 'insideLeft', fill: '#FFD700' }}
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

export default ChartMonthly;
