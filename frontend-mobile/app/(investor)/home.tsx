import React from 'react';
import { FlatList, Pressable, ScrollView, StyleSheet, Text } from 'react-native';
import { useRouter } from 'expo-router';
import { useAppState } from '@/src/context/AppContext';

export default function InvestorHomeScreen() {
  const router = useRouter();
  const { projects } = useAppState();

  return (
    <ScrollView style={styles.screen} contentContainerStyle={styles.content}>
      <Text style={styles.title}>Investor Dashboard</Text>
      <Text style={styles.subtitle}>Live market overview and funded opportunities</Text>

      <Pressable style={styles.aiBtn} onPress={() => router.push('/portfolio/ai')}>
        <Text style={styles.aiText}>AI Portfolio</Text>
      </Pressable>

      <Text style={styles.section}>All Farmer Projects</Text>
      <FlatList
        data={projects}
        keyExtractor={(item) => item.id}
        scrollEnabled={false}
        renderItem={({ item }) => (
          <Pressable style={styles.card} onPress={() => router.push(`/investment/${item.id}`)}>
            <Text style={styles.cardTitle}>{item.projectTitle}</Text>
            <Text style={styles.cardText}>{item.location} • ROI {item.expectedRoi}</Text>
            <Text style={styles.cardText}>Raised LKR {item.raisedAmount.toLocaleString()} / {item.fundingGoal.toLocaleString()}</Text>
          </Pressable>
        )}
      />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec' },
  content: { padding: 16, paddingBottom: 24 },
  title: { fontSize: 28, fontWeight: '800', color: '#1e5224' },
  subtitle: { color: '#4f7152', marginTop: 4, marginBottom: 12 },
  aiBtn: { backgroundColor: '#2e7d32', borderRadius: 12, paddingVertical: 13, alignItems: 'center' },
  aiText: { color: '#fff', fontWeight: '900', fontSize: 16 },
  section: { marginTop: 16, marginBottom: 8, color: '#2e5732', fontWeight: '800', fontSize: 17 },
  card: { backgroundColor: '#fff', borderRadius: 12, padding: 14, marginBottom: 10 },
  cardTitle: { color: '#1f4022', fontWeight: '800' },
  cardText: { color: '#4f7053', marginTop: 6 },
});
