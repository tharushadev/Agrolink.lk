import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

export default function FarmerAnalyticsScreen() {
  return (
    <View style={styles.screen}>
      <Text style={styles.title}>Farm Analytics</Text>
      <View style={styles.card}>
        <Text style={styles.kpi}>Projected ROI: 21%</Text>
        <Text style={styles.kpi}>Yield Trend: +12% vs last cycle</Text>
        <Text style={styles.kpi}>Moisture Risk: Moderate</Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec', padding: 16 },
  title: { fontSize: 28, fontWeight: '800', color: '#1d5223', marginBottom: 12 },
  card: { backgroundColor: '#fff', borderRadius: 14, padding: 14 },
  kpi: { color: '#284e2c', marginBottom: 8, fontWeight: '700' },
});
