import React, { useEffect } from 'react';
import { View, Image, StyleSheet, ActivityIndicator, Text } from 'react-native';
import { useRouter } from 'expo-router';

export default function SplashScreen() {
  const router = useRouter();

  useEffect(() => {
    // Wait 2.5 seconds, then go to Login
    const timer = setTimeout(() => {
      router.replace('/login');
    }, 2500);
    return () => clearTimeout(timer);
  }, [router]);

  return (
    <View style={styles.container}>
      <View style={styles.logoContainer}>
        <Image source={require('../src/assets/logo.png')} style={styles.logo} resizeMode="contain" />
        <Text style={styles.appName}>AGRO LINK</Text>
        <Text style={styles.tagline}>Future of Agri-Finance</Text>
      </View>

      <ActivityIndicator size="large" color="#e9ffe7" style={styles.loader} />
    </View>
  );
}

const styles = StyleSheet.create({
  container: { flex: 1, backgroundColor: '#2e7d32', justifyContent: 'center', alignItems: 'center' },
  logoContainer: { alignItems: 'center', marginBottom: 50 },
  logo: { width: 180, height: 180, marginBottom: 12 },
  appName: { fontSize: 42, fontWeight: '900', color: '#d4ffd2', letterSpacing: 2 },
  tagline: { fontSize: 20, color: '#f6ffe9', fontWeight: '700' },
  loader: { transform: [{ scale: 1.5 }] },
});