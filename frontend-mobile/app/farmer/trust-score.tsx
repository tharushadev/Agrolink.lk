import React from 'react';
import { StyleSheet, Text, View } from 'react-native';
import { useAppState } from '@/src/context/AppContext';

export default function TrustScoreScreen() {
  const { user } = useAppState();

  return (
    <View style={styles.screen}>
      <Text style={styles.title}>Trust Score Breakdown</Text>
      <View style={styles.card}>
        <Text style={styles.score}>{user?.trustScore ?? 0} / 100</Text>
        <Text style={styles.item}>NIC Verification: +30</Text>
        <Text style={styles.item}>Harvest Proof Updates: +22</Text>
        <Text style={styles.item}>Investor Feedback: +16</Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec', padding: 16 },
  title: { fontSize: 28, fontWeight: '800', color: '#205327', marginBottom: 12 },
  card: { backgroundColor: '#fff', borderRadius: 12, padding: 14 },
  score: { fontSize: 30, fontWeight: '900', color: '#2e7d32', marginBottom: 8 },
  item: { color: '#355a38', marginBottom: 6, fontWeight: '700' },
});
