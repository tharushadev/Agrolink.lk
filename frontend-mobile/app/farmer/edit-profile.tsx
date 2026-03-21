import React, { useState } from 'react';
import { Alert, Pressable, StyleSheet, Text, TextInput, View } from 'react-native';
import { useAppState } from '@/src/context/AppContext';

export default function EditProfileScreen() {
  const { user } = useAppState();
  const [firstName, setFirstName] = useState(user?.firstName ?? '');
  const [lastName, setLastName] = useState(user?.lastName ?? '');

  return (
    <View style={styles.screen}>
      <Text style={styles.title}>Edit Personal Details</Text>
      <TextInput style={styles.input} value={firstName} onChangeText={setFirstName} placeholder="First Name" />
      <TextInput style={styles.input} value={lastName} onChangeText={setLastName} placeholder="Last Name" />
      <Pressable style={styles.btn} onPress={() => Alert.alert('Saved', 'Profile update simulated in this stage.') }>
        <Text style={styles.btnText}>Save Changes</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec', padding: 16 },
  title: { fontSize: 28, fontWeight: '800', color: '#1f5424', marginBottom: 12 },
  input: {
    borderWidth: 1,
    borderColor: '#c4d8be',
    borderRadius: 12,
    backgroundColor: '#fff',
    paddingHorizontal: 12,
    paddingVertical: 12,
    marginBottom: 10,
  },
  btn: { backgroundColor: '#2e7d32', borderRadius: 12, paddingVertical: 13, alignItems: 'center' },
  btnText: { color: '#fff', fontWeight: '800' },
});
