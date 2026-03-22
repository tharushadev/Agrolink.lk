import React, { useMemo, useState } from 'react';
import { Alert, Image, Pressable, ScrollView, StyleSheet, Text, TextInput, View } from 'react-native';
import { useRouter } from 'expo-router';
import * as ImagePicker from 'expo-image-picker';
import * as DocumentPicker from 'expo-document-picker';
import { useAppState, UserRole } from '@/src/context/AppContext';

export default function SignupScreen() {
  const router = useRouter();
  const { signup } = useAppState();

  const [role, setRole] = useState<UserRole>('FARMER');
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [phone, setPhone] = useState('');
  const [nic, setNic] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [farmerPhoto, setFarmerPhoto] = useState<string | null>(null);
  const [gsDocName, setGsDocName] = useState<string | null>(null);

  const isFarmer = useMemo(() => role === 'FARMER', [role]);

  const pickPhoto = async () => {
    const result = await ImagePicker.launchImageLibraryAsync({ mediaTypes: ['images'], quality: 0.6 });
    if (!result.canceled) {
      setFarmerPhoto(result.assets[0].uri);
    }
  };

  const pickPdf = async () => {
    const result = await DocumentPicker.getDocumentAsync({ type: 'application/pdf' });
    if (!result.canceled) {
      setGsDocName(result.assets[0].name);
    }
  };

  const handleSignup = () => {
    if (password !== confirmPassword) {
      Alert.alert('Validation', 'Passwords do not match.');
      return;
    }

    if (isFarmer && (!farmerPhoto || !gsDocName)) {
      Alert.alert('Validation', 'Farmer photo and Grama Sevaka PDF are required.');
      return;
    }

    signup({
      firstName,
      lastName,
      phone,
      nic,
      role,
    });

    router.replace(isFarmer ? '/(farmer)/home' : '/(investor)/home');
  };

  return (
    <ScrollView contentContainerStyle={styles.container}>
      <Text style={styles.title}>Create Account</Text>

      <View style={styles.toggleRow}>
        {(['FARMER', 'INVESTOR'] as UserRole[]).map((item) => (
          <Pressable key={item} style={[styles.toggle, role === item && styles.toggleActive]} onPress={() => setRole(item)}>
            <Text style={[styles.toggleText, role === item && styles.toggleTextActive]}>{item}</Text>
          </Pressable>
        ))}
      </View>

      <TextInput style={styles.input} placeholder="First Name" value={firstName} onChangeText={setFirstName} />
      <TextInput style={styles.input} placeholder="Last Name" value={lastName} onChangeText={setLastName} />
      <TextInput style={styles.input} placeholder="Phone" value={phone} onChangeText={setPhone} keyboardType="phone-pad" />
      <TextInput style={styles.input} placeholder="NIC" value={nic} onChangeText={setNic} />
      <TextInput style={styles.input} placeholder="Password" value={password} onChangeText={setPassword} secureTextEntry />
      <TextInput
        style={styles.input}
        placeholder="Confirm Password"
        value={confirmPassword}
        onChangeText={setConfirmPassword}
        secureTextEntry
      />

      {isFarmer && (
        <View style={styles.farmerBox}>
          <Text style={styles.sectionTitle}>Farmer Documents</Text>
          <Pressable style={styles.altBtn} onPress={pickPhoto}>
            <Text style={styles.altBtnText}>Upload Farmer Photo</Text>
          </Pressable>
          {farmerPhoto ? <Image source={{ uri: farmerPhoto }} style={styles.preview} /> : null}

          <Pressable style={styles.altBtn} onPress={pickPdf}>
            <Text style={styles.altBtnText}>Upload Grama Sevaka PDF</Text>
          </Pressable>
          <Text style={styles.docText}>{gsDocName ?? 'No document selected'}</Text>
        </View>
      )}

      <Pressable style={styles.primaryBtn} onPress={handleSignup}>
        <Text style={styles.primaryBtnText}>Sign Up</Text>
      </Pressable>

      <Pressable onPress={() => router.push('/login')}>
        <Text style={styles.loginLink}>Login</Text>
      </Pressable>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  container: { padding: 20, backgroundColor: '#f1fbf1', flexGrow: 1 },
  title: { fontSize: 32, fontWeight: '800', color: '#1f6324', marginBottom: 14 },
  toggleRow: { flexDirection: 'row', backgroundColor: '#dcefd5', borderRadius: 12, padding: 4, marginBottom: 14 },
  toggle: { flex: 1, alignItems: 'center', borderRadius: 10, paddingVertical: 10 },
  toggleActive: { backgroundColor: '#2e7d32' },
  toggleText: { color: '#2e5630', fontWeight: '700' },
  toggleTextActive: { color: '#fff' },
  input: {
    borderWidth: 1,
    borderColor: '#c4d8be',
    borderRadius: 12,
    backgroundColor: '#fff',
    paddingHorizontal: 14,
    paddingVertical: 12,
    marginBottom: 10,
  },
  farmerBox: { marginTop: 6, marginBottom: 14, backgroundColor: '#eaf8e8', borderRadius: 12, padding: 12 },
  sectionTitle: { fontWeight: '800', color: '#245829', marginBottom: 10 },
  altBtn: { backgroundColor: '#d2eecb', borderRadius: 10, paddingVertical: 10, alignItems: 'center', marginBottom: 10 },
  altBtnText: { color: '#214e23', fontWeight: '700' },
  preview: { width: '100%', height: 150, borderRadius: 10, marginBottom: 10 },
  docText: { color: '#3c5b3f', fontSize: 12 },
  primaryBtn: { backgroundColor: '#2e7d32', borderRadius: 12, paddingVertical: 14, alignItems: 'center', marginTop: 4 },
  primaryBtnText: { color: '#fff', fontWeight: '800', fontSize: 16 },
  loginLink: { textAlign: 'center', marginTop: 12, color: '#245926', fontWeight: '700' },
});
