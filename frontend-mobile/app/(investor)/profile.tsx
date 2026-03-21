import React from 'react';
import { Pressable, ScrollView, StyleSheet, Text } from 'react-native';
import { useRouter } from 'expo-router';
import { useAppState } from '@/src/context/AppContext';

export default function InvestorProfileScreen() {
  const router = useRouter();
  const { logout } = useAppState();

  return (
    <ScrollView style={styles.screen} contentContainerStyle={styles.content}>
      <Text style={styles.title}>Investor Profile</Text>
      <Pressable style={styles.item} onPress={() => router.push('/profile/payment')}>
        <Text style={styles.itemText}>Wallet / Payment</Text>
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
  title: { fontSize: 28, fontWeight: '800', color: '#1d5123', marginBottom: 14 },
  item: { backgroundColor: '#fff', borderRadius: 12, padding: 14, marginBottom: 10 },
  itemText: { color: '#1f4323', fontWeight: '700' },
  logout: { backgroundColor: '#fbe7e6' },
  logoutText: { color: '#8f2b22' },
});
