import React, { useState } from 'react';
import { Alert, Modal, Pressable, StyleSheet, Text, TextInput, View } from 'react-native';
import { useAppState } from '@/src/context/AppContext';

export default function PaymentScreen() {
  const { linkBankAccount, withdrawFunds, linkedBankName, walletBalance } = useAppState();
  const [linkOpen, setLinkOpen] = useState(false);
  const [withdrawOpen, setWithdrawOpen] = useState(false);
  const [bankName, setBankName] = useState('');
  const [account, setAccount] = useState('');
  const [amount, setAmount] = useState('');

  const submitLink = () => {
    if (!bankName || !account) {
      Alert.alert('Validation', 'Enter bank and account number.');
      return;
    }
    linkBankAccount(bankName, account);
    setLinkOpen(false);
  };

  const submitWithdraw = () => {
    const value = Number(amount);
    if (!Number.isFinite(value) || value <= 0) {
      Alert.alert('Validation', 'Enter a valid amount.');
      return;
    }
    withdrawFunds(value);
    setWithdrawOpen(false);
  };

  return (
    <View style={styles.screen}>
      <Text style={styles.title}>Wallet / Payment</Text>
      <View style={styles.card}>
        <Text style={styles.line}>Linked Bank: {linkedBankName ?? 'Not linked yet'}</Text>
        <Text style={styles.line}>Wallet Balance: LKR {walletBalance.toLocaleString()}</Text>
      </View>

      <Pressable style={styles.btn} onPress={() => setLinkOpen(true)}>
        <Text style={styles.btnText}>Link Bank Account</Text>
      </Pressable>
      <Pressable style={styles.btn} onPress={() => setWithdrawOpen(true)}>
        <Text style={styles.btnText}>Withdraw Funds</Text>
      </Pressable>

      <Modal visible={linkOpen} transparent animationType="slide" onRequestClose={() => setLinkOpen(false)}>
        <View style={styles.modalWrap}>
          <View style={styles.modalCard}>
            <Text style={styles.modalTitle}>Link Bank Account</Text>
            <TextInput style={styles.input} placeholder="Bank Name" value={bankName} onChangeText={setBankName} />
            <TextInput style={styles.input} placeholder="Account Number" value={account} onChangeText={setAccount} />
            <Pressable style={styles.btn} onPress={submitLink}><Text style={styles.btnText}>Save</Text></Pressable>
          </View>
        </View>
      </Modal>

      <Modal visible={withdrawOpen} transparent animationType="slide" onRequestClose={() => setWithdrawOpen(false)}>
        <View style={styles.modalWrap}>
          <View style={styles.modalCard}>
            <Text style={styles.modalTitle}>Withdraw Funds</Text>
            <TextInput
              style={styles.input}
              placeholder="Amount in LKR"
              value={amount}
              onChangeText={setAmount}
              keyboardType="numeric"
            />
            <Pressable style={styles.btn} onPress={submitWithdraw}><Text style={styles.btnText}>Withdraw</Text></Pressable>
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec', padding: 16 },
  title: { fontSize: 28, fontWeight: '800', color: '#205327', marginBottom: 12 },
  card: { backgroundColor: '#fff', borderRadius: 12, padding: 14, marginBottom: 12 },
  line: { color: '#355839', marginBottom: 6, fontWeight: '700' },
  btn: { backgroundColor: '#2e7d32', borderRadius: 12, paddingVertical: 12, alignItems: 'center', marginBottom: 10 },
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
});
