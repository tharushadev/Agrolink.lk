import React, { useMemo, useState } from 'react';
import { Alert, Modal, Pressable, StyleSheet, Text, TextInput, View } from 'react-native';
import { useLocalSearchParams } from 'expo-router';
import { useAppState } from '@/src/context/AppContext';

export default function InvestmentDetailsScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const { projects, investInProject } = useAppState();
  const [open, setOpen] = useState(false);
  const [amount, setAmount] = useState('25000');

  const project = useMemo(() => projects.find((item) => item.id === id), [id, projects]);

  if (!project) {
    return (
      <View style={styles.screen}>
        <Text style={styles.title}>Project not found</Text>
      </View>
    );
  }

  const investNow = () => {
    const numeric = Number(amount);
    if (!Number.isFinite(numeric) || numeric <= 0) {
      Alert.alert('Invalid amount', 'Enter a valid LKR amount.');
      return;
    }

    investInProject(project.id, numeric, 'You');
    setOpen(false);
    Alert.alert('Payment Simulation', 'Investment complete and raised amount updated.');
  };

  return (
    <View style={styles.screen}>
      <View style={styles.hero}>
        <Text style={styles.heroText}>Farm Photo Placeholder</Text>
      </View>
      <Text style={styles.title}>{project.projectTitle}</Text>
      <Text style={styles.meta}>AI Risk Score: {project.risk}</Text>
      <Text style={styles.meta}>Expected ROI: {project.expectedRoi}</Text>
      <Text style={styles.meta}>Raised LKR {project.raisedAmount.toLocaleString()} / {project.fundingGoal.toLocaleString()}</Text>

      <Pressable style={styles.btn} onPress={() => setOpen(true)}>
        <Text style={styles.btnText}>Invest Now</Text>
      </Pressable>

      <Modal visible={open} transparent animationType="slide" onRequestClose={() => setOpen(false)}>
        <View style={styles.modalWrap}>
          <View style={styles.modalCard}>
            <Text style={styles.modalTitle}>Payment Gateway Simulation</Text>
            <TextInput style={styles.input} value={amount} onChangeText={setAmount} keyboardType="numeric" />
            <Pressable style={styles.btn} onPress={investNow}>
              <Text style={styles.btnText}>Confirm Investment</Text>
            </Pressable>
            <Pressable onPress={() => setOpen(false)}>
              <Text style={styles.close}>Cancel</Text>
            </Pressable>
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec', padding: 16 },
  hero: { height: 180, borderRadius: 14, backgroundColor: '#bfdcbb', alignItems: 'center', justifyContent: 'center' },
  heroText: { color: '#3f5f41', fontWeight: '700' },
  title: { marginTop: 14, fontSize: 24, fontWeight: '800', color: '#1d4b22' },
  meta: { color: '#4f6f52', marginTop: 8 },
  btn: { marginTop: 16, backgroundColor: '#2e7d32', borderRadius: 12, paddingVertical: 13, alignItems: 'center' },
  btnText: { color: '#fff', fontWeight: '800' },
  modalWrap: { flex: 1, justifyContent: 'flex-end', backgroundColor: 'rgba(0,0,0,0.35)' },
  modalCard: { backgroundColor: '#fff', borderTopLeftRadius: 18, borderTopRightRadius: 18, padding: 16 },
  modalTitle: { fontSize: 20, fontWeight: '800', color: '#1f5125', marginBottom: 10 },
  input: {
    borderWidth: 1,
    borderColor: '#c4d8be',
    borderRadius: 10,
    paddingHorizontal: 12,
    paddingVertical: 11,
    marginBottom: 10,
  },
  close: { textAlign: 'center', marginTop: 10, color: '#56725a', fontWeight: '700' },
});
