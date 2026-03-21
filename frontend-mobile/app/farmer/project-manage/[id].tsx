import React, { useMemo, useState } from 'react';
import { Alert, FlatList, Image, Modal, Pressable, StyleSheet, Text, TextInput, View } from 'react-native';
import { useLocalSearchParams, useRouter } from 'expo-router';
import * as ImagePicker from 'expo-image-picker';
import { useAppState } from '@/src/context/AppContext';

export default function ManageProjectScreen() {
  const { id } = useLocalSearchParams<{ id: string }>();
  const router = useRouter();
  const { projects, addProjectUpdate } = useAppState();

  const [open, setOpen] = useState(false);
  const [text, setText] = useState('');
  const [photoUri, setPhotoUri] = useState<string | undefined>();

  const project = useMemo(() => projects.find((item) => item.id === id), [id, projects]);

  const percentage = useMemo(() => {
    if (!project || project.fundingGoal === 0) {
      return 0;
    }
    return Math.min(100, Math.round((project.raisedAmount / project.fundingGoal) * 100));
  }, [project]);

  if (!project) {
    return (
      <View style={styles.screen}>
        <Text style={styles.title}>Project not found</Text>
      </View>
    );
  }

  const pickPhoto = async () => {
    const result = await ImagePicker.launchImageLibraryAsync({ mediaTypes: ['images'] });
    if (!result.canceled) {
      setPhotoUri(result.assets[0].uri);
    }
  };

  const postUpdate = () => {
    if (!text.trim()) {
      Alert.alert('Validation', 'Please type an update.');
      return;
    }

    addProjectUpdate(project.id, text.trim(), photoUri);
    setText('');
    setPhotoUri(undefined);
    setOpen(false);
  };

  return (
    <View style={styles.screen}>
      <Text style={styles.title}>{project.projectTitle}</Text>
      <Text style={styles.meta}>{project.location} • {project.cropType}</Text>

      <View style={styles.progressTrack}>
        <View style={[styles.progressFill, { width: `${percentage}%` }]} />
      </View>
      <Text style={styles.meta}>Funding Progress: {percentage}%</Text>

      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Investors</Text>
        {project.investors.length === 0 ? <Text style={styles.empty}>No investors yet</Text> : null}
        {project.investors.map((investor, index) => (
          <Text key={`${investor.investorName}-${index}`} style={styles.meta}>
            {investor.investorName} • LKR {investor.amount.toLocaleString()}
          </Text>
        ))}
      </View>

      <View style={styles.section}>
        <Text style={styles.sectionTitle}>Timeline Updates</Text>
        <FlatList
          data={project.updates}
          keyExtractor={(item) => item.id}
          renderItem={({ item }) => (
            <View style={styles.updateCard}>
              <Text style={styles.updateText}>{item.text}</Text>
              {item.photoUri ? <Image source={{ uri: item.photoUri }} style={styles.updateImage} /> : null}
              <Text style={styles.time}>{new Date(item.timestamp).toLocaleString()}</Text>
            </View>
          )}
          ListEmptyComponent={<Text style={styles.empty}>No updates yet</Text>}
        />
      </View>

      <View style={styles.row}>
        <Pressable style={styles.secondaryBtn} onPress={() => router.push('/chat/index')}>
          <Text style={styles.secondaryText}>Chat</Text>
        </Pressable>
        <Pressable style={styles.primaryBtn} onPress={() => setOpen(true)}>
          <Text style={styles.primaryText}>+ Add Update</Text>
        </Pressable>
      </View>

      <Modal visible={open} transparent animationType="slide" onRequestClose={() => setOpen(false)}>
        <View style={styles.modalWrap}>
          <View style={styles.modalCard}>
            <Text style={styles.modalTitle}>Post Project Update</Text>
            <TextInput
              style={styles.textArea}
              placeholder="e.g., Harvest Started"
              value={text}
              onChangeText={setText}
              multiline
            />
            <Pressable style={styles.secondaryBtn} onPress={pickPhoto}>
              <Text style={styles.secondaryText}>Attach Photo</Text>
            </Pressable>
            {photoUri ? <Image source={{ uri: photoUri }} style={styles.updateImage} /> : null}
            <Pressable style={styles.primaryBtn} onPress={postUpdate}>
              <Text style={styles.primaryText}>Post Update</Text>
            </Pressable>
            <Pressable onPress={() => setOpen(false)}>
              <Text style={styles.close}>Close</Text>
            </Pressable>
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec', padding: 16 },
  title: { fontSize: 24, fontWeight: '800', color: '#214f25' },
  meta: { color: '#4f6f51', marginTop: 6 },
  progressTrack: { marginTop: 14, height: 10, borderRadius: 8, backgroundColor: '#d7e7d5', overflow: 'hidden' },
  progressFill: { height: '100%', backgroundColor: '#2e7d32' },
  section: { marginTop: 18 },
  sectionTitle: { fontWeight: '800', color: '#234e28', marginBottom: 8 },
  updateCard: { backgroundColor: '#fff', borderRadius: 10, padding: 12, marginBottom: 8 },
  updateText: { color: '#223f25', fontWeight: '600' },
  updateImage: { width: '100%', height: 140, borderRadius: 10, marginTop: 8 },
  time: { color: '#6a826b', fontSize: 12, marginTop: 6 },
  empty: { color: '#6b826e' },
  row: { flexDirection: 'row', gap: 10, marginTop: 14 },
  primaryBtn: { flex: 1, backgroundColor: '#2e7d32', borderRadius: 10, paddingVertical: 12, alignItems: 'center' },
  primaryText: { color: '#fff', fontWeight: '800' },
  secondaryBtn: { flex: 1, backgroundColor: '#d6ecd0', borderRadius: 10, paddingVertical: 12, alignItems: 'center' },
  secondaryText: { color: '#26502a', fontWeight: '800' },
  modalWrap: { flex: 1, backgroundColor: 'rgba(0,0,0,0.35)', justifyContent: 'flex-end' },
  modalCard: {
    backgroundColor: '#fff',
    borderTopLeftRadius: 18,
    borderTopRightRadius: 18,
    padding: 16,
    minHeight: 320,
  },
  modalTitle: { fontSize: 20, fontWeight: '800', color: '#1f5126', marginBottom: 10 },
  textArea: {
    borderWidth: 1,
    borderColor: '#c8ddc2',
    borderRadius: 10,
    minHeight: 96,
    textAlignVertical: 'top',
    padding: 10,
    marginBottom: 10,
  },
  close: { marginTop: 10, textAlign: 'center', color: '#567259', fontWeight: '700' },
});
