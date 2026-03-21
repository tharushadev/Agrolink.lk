import React, { useState } from 'react';
import { Modal, Pressable, StyleSheet, Text, View } from 'react-native';

export default function AboutScreen() {
  const [open, setOpen] = useState(false);
  const [docType, setDocType] = useState<'Privacy Policy' | 'Terms'>('Privacy Policy');

  const openDoc = (type: 'Privacy Policy' | 'Terms') => {
    setDocType(type);
    setOpen(true);
  };

  return (
    <View style={styles.screen}>
      <Text style={styles.title}>About / Legal</Text>
      <Pressable style={styles.item} onPress={() => openDoc('Privacy Policy')}>
        <Text style={styles.itemText}>Privacy Policy</Text>
      </Pressable>
      <Pressable style={styles.item} onPress={() => openDoc('Terms')}>
        <Text style={styles.itemText}>Terms</Text>
      </Pressable>

      <Modal visible={open} transparent animationType="fade" onRequestClose={() => setOpen(false)}>
        <View style={styles.modalWrap}>
          <View style={styles.modalCard}>
            <Text style={styles.modalTitle}>{docType}</Text>
            <Text style={styles.body}>
              Sri Lanka PDPA-aligned summary: AgroLink processes identity and funding data for onboarding, compliance, and investment
              transactions. Users may request correction or deletion where legally permitted.
            </Text>
            <Pressable style={styles.btn} onPress={() => setOpen(false)}>
              <Text style={styles.btnText}>Close</Text>
            </Pressable>
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec', padding: 16 },
  title: { fontSize: 28, fontWeight: '800', color: '#205327', marginBottom: 12 },
  item: { backgroundColor: '#fff', borderRadius: 12, padding: 14, marginBottom: 10 },
  itemText: { color: '#23482a', fontWeight: '700' },
  modalWrap: { flex: 1, justifyContent: 'center', alignItems: 'center', backgroundColor: 'rgba(0,0,0,0.35)' },
  modalCard: { backgroundColor: '#fff', borderRadius: 14, padding: 16, width: '88%' },
  modalTitle: { fontSize: 20, fontWeight: '800', color: '#1f5125', marginBottom: 10 },
  body: { color: '#3c5f40', lineHeight: 21 },
  btn: { marginTop: 14, backgroundColor: '#2e7d32', borderRadius: 10, paddingVertical: 11, alignItems: 'center' },
  btnText: { color: '#fff', fontWeight: '800' },
});
