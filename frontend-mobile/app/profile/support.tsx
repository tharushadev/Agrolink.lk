import React from 'react';
import { Linking, Pressable, StyleSheet, Text, View } from 'react-native';
import { useRouter } from 'expo-router';

export default function SupportScreen() {
  const router = useRouter();

  return (
    <View style={styles.screen}>
      <Text style={styles.title}>Help & Support</Text>
      <Pressable style={styles.item} onPress={() => Linking.openURL('tel:+94112345678')}>
        <Text style={styles.itemText}>Call Us</Text>
      </Pressable>
      <Pressable style={styles.item} onPress={() => Linking.openURL('mailto:support@agrolink.lk')}>
        <Text style={styles.itemText}>Email</Text>
      </Pressable>
      <Pressable style={styles.item} onPress={() => router.push('/chat/index')}>
        <Text style={styles.itemText}>Live Chat</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec', padding: 16 },
  title: { fontSize: 28, fontWeight: '800', color: '#205227', marginBottom: 12 },
  item: { backgroundColor: '#fff', borderRadius: 12, padding: 14, marginBottom: 10 },
  itemText: { color: '#25482a', fontWeight: '700' },
});
