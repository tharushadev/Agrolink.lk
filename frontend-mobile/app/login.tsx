import React, { useState } from 'react';
import { ActivityIndicator, Alert, Pressable, StyleSheet, Text, TextInput, View } from 'react-native';
import { useRouter } from 'expo-router';
import { useAppState, UserRole } from '@/src/context/AppContext';

export default function LoginScreen() {
  const router = useRouter();
  const { login } = useAppState();

  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [role, setRole] = useState<UserRole>('FARMER');
  const [loading, setLoading] = useState(false);

  const handleLogin = () => {
    setLoading(true);
    setTimeout(() => {
      login(role, email);
      setLoading(false);
      router.replace(role === 'FARMER' ? '/(farmer)/home' : '/(investor)/home');
    }, 1500);
  };

  return (
    <View style={styles.screen}>
      <Text style={styles.title}>Welcome Back</Text>
      <Text style={styles.subtitle}>Login to AgroLink</Text>

      <View style={styles.toggleRow}>
        {(['FARMER', 'INVESTOR'] as UserRole[]).map((item) => (
          <Pressable
            key={item}
            style={[styles.toggle, role === item && styles.toggleActive]}
            onPress={() => setRole(item)}>
            <Text style={[styles.toggleText, role === item && styles.toggleTextActive]}>{item}</Text>
          </Pressable>
        ))}
      </View>

      <TextInput
        placeholder="Email"
        placeholderTextColor="#7b8a7b"
        style={styles.input}
        value={email}
        onChangeText={setEmail}
        keyboardType="email-address"
        autoCapitalize="none"
      />
      <TextInput
        placeholder="Password"
        placeholderTextColor="#7b8a7b"
        style={styles.input}
        value={password}
        onChangeText={setPassword}
        secureTextEntry
      />

      <Pressable onPress={() => Alert.alert('Forgot Password', 'This is a simulated flow in this build.')}>
        <Text style={styles.link}>Forgot Password?</Text>
      </Pressable>

      <Pressable style={styles.primaryBtn} onPress={handleLogin} disabled={loading}>
        {loading ? <ActivityIndicator color="#fff" /> : <Text style={styles.primaryText}>Login</Text>}
      </Pressable>

      <Pressable onPress={() => router.push('/signup')}>
        <Text style={styles.secondaryLink}>Sign Up</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#f2fbef', padding: 22, justifyContent: 'center' },
  title: { fontSize: 34, fontWeight: '800', color: '#1b5e20' },
  subtitle: { fontSize: 16, color: '#315332', marginBottom: 20 },
  toggleRow: { flexDirection: 'row', marginBottom: 18, backgroundColor: '#dcefd5', borderRadius: 12, padding: 4 },
  toggle: { flex: 1, borderRadius: 10, paddingVertical: 10, alignItems: 'center' },
  toggleActive: { backgroundColor: '#2e7d32' },
  toggleText: { color: '#3a5a3b', fontWeight: '700' },
  toggleTextActive: { color: '#fff' },
  input: {
    borderWidth: 1,
    borderColor: '#c5ddbf',
    backgroundColor: '#fff',
    borderRadius: 12,
    paddingHorizontal: 14,
    paddingVertical: 12,
    marginBottom: 12,
    color: '#1f2f1f',
  },
  link: { textAlign: 'right', marginBottom: 16, color: '#2f6f2f', fontWeight: '600' },
  primaryBtn: { backgroundColor: '#2e7d32', borderRadius: 14, paddingVertical: 14, alignItems: 'center', marginBottom: 14 },
  primaryText: { color: '#fff', fontWeight: '800', fontSize: 16 },
  secondaryLink: { textAlign: 'center', color: '#215b24', fontWeight: '700' },
});
