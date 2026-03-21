import React from 'react';
import { StyleSheet, Text, View } from 'react-native';

export default function ChatListScreen() {
  return (
    <View style={styles.screen}>
      <Text style={styles.title}>Chat List</Text>
      <Text style={styles.subtitle}>Messaging is mocked for this stage. Live chat channel appears here.</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec', padding: 16 },
  title: { fontSize: 28, fontWeight: '800', color: '#1f5324', marginBottom: 8 },
  subtitle: { color: '#4f6f51' },
});
