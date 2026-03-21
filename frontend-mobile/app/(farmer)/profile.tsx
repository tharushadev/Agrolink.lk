import React from 'react';
import { Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { useRouter } from 'expo-router';
import { useAppState } from '@/src/context/AppContext';

export default function FarmerProfileScreen() {
  const router = useRouter();
  const { user, projects, logout } = useAppState();

  const totalInvestors = projects.reduce((sum, project) => sum + project.investors.length, 0);

  return (
    <ScrollView style={styles.screen} contentContainerStyle={styles.content}>
      <Text style={styles.title}>Farmer Profile Hub</Text>

      <View style={styles.summaryCard}>
        <Text style={styles.summaryTitle}>{user?.firstName} {user?.lastName}</Text>
        <Text style={styles.summaryText}>Trust Score: {user?.trustScore ?? 0}</Text>
        <Text style={styles.summaryText}>Total Investors: {totalInvestors}</Text>
      </View>

      <Pressable style={styles.item} onPress={() => router.push('/farmer/trust-score')}>
        <Text style={styles.itemText}>Trust Score</Text>
      </Pressable>
      <Pressable style={styles.item} onPress={() => router.push('/farmer/edit-profile')}>
        <Text style={styles.itemText}>Edit Personal Details</Text>
      </Pressable>
      <Pressable style={styles.item} onPress={() => router.push('/farmer/security')}>
        <Text style={styles.itemText}>Security & Password</Text>
      </Pressable>
      <Pressable style={styles.item} onPress={() => router.push('/profile/payment')}>
        <Text style={styles.itemText}>Payment Account</Text>
      </Pressable>
      <Pressable style={styles.item} onPress={() => router.push('/profile/support')}>
        <Text style={styles.itemText}>Help & Support</Text>
      </Pressable>
      <Pressable style={styles.item} onPress={() => router.push('/profile/about')}>
        <Text style={styles.itemText}>About / Legal</Text>
      </Pressable>

      <Pressable
        style={[styles.item, styles.logout]}
        onPress={() => {
          logout();
          router.replace('/login');
        }}>
        <Text style={[styles.itemText, styles.logoutText]}>Log Out</Text>
      </Pressable>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec' },
  content: { padding: 16 },
  title: { fontSize: 28, fontWeight: '800', color: '#205527', marginBottom: 14 },
  summaryCard: { backgroundColor: '#2e7d32', borderRadius: 14, padding: 14, marginBottom: 14 },
  summaryTitle: { color: '#fff', fontWeight: '800', fontSize: 20, marginBottom: 8 },
  summaryText: { color: '#e0f4df', marginBottom: 4 },
  item: { backgroundColor: '#fff', borderRadius: 12, padding: 14, marginBottom: 10 },
  itemText: { color: '#1e4323', fontWeight: '700' },
  logout: { backgroundColor: '#fbe7e6', marginTop: 8 },
  logoutText: { color: '#8f2b22' },
});
