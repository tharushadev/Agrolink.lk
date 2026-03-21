import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

export default function AIPortfolioScreen() {
  return (
    <View style={styles.screen}>
      <Text style={styles.title}>AI Portfolio Generator</Text>
      <View style={styles.card}>
        <Text style={styles.line}>Suggested Mix:</Text>
        <Text style={styles.line}>1. Low Risk Paddy - 50%</Text>
        <Text style={styles.line}>2. Medium Risk Expansion - 30%</Text>
        <Text style={styles.line}>3. High Yield Trial Plot - 20%</Text>
      </View>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec', padding: 16 },
  title: { fontSize: 28, fontWeight: '800', color: '#1d4f22', marginBottom: 12 },
  card: { backgroundColor: '#fff', borderRadius: 12, padding: 14 },
  line: { color: '#2f5833', marginBottom: 7, fontWeight: '700' },
});
