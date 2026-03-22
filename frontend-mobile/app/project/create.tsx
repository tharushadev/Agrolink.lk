import React, { useState } from 'react';
import { Alert, Image, Pressable, ScrollView, StyleSheet, Text, TextInput, View } from 'react-native';
import { useRouter } from 'expo-router';
import * as ImagePicker from 'expo-image-picker';
import { useAppState } from '@/src/context/AppContext';

export default function CreateProjectWizard() {
  const router = useRouter();
  const { createProject } = useAppState();
  const [step, setStep] = useState(1);

  const [projectTitle, setProjectTitle] = useState('');
  const [cropType, setCropType] = useState('Paddy');
  const [location, setLocation] = useState('');
  const [fundingGoal, setFundingGoal] = useState('');
  const [durationMonths, setDurationMonths] = useState('6');
  const [expectedRoi, setExpectedRoi] = useState('18%-24%');
  const [description, setDescription] = useState('');
  const [coverPhotoUri, setCoverPhotoUri] = useState<string | undefined>();

  const pickCover = async () => {
    const result = await ImagePicker.launchImageLibraryAsync({ mediaTypes: ['images'], quality: 0.6 });
    if (!result.canceled) {
      setCoverPhotoUri(result.assets[0].uri);
    }
  };

  const submit = () => {
    createProject({
      projectTitle,
      cropType,
      location,
      fundingGoal: Number(fundingGoal || 0),
      durationMonths: Number(durationMonths || 0),
      expectedRoi,
      description,
      coverPhotoUri,
    });
    Alert.alert('Project Submitted', 'Saved to ProjectContext and added to your dashboard.');
    router.replace('/(farmer)/home');
  };

  return (
    <ScrollView style={styles.screen} contentContainerStyle={styles.content}>
      <Text style={styles.title}>Create Project Wizard</Text>
      <Text style={styles.step}>Step {step} of 3</Text>

      {step === 1 && (
        <View>
          <TextInput style={styles.input} placeholder="Project Title" value={projectTitle} onChangeText={setProjectTitle} />
          <TextInput style={styles.input} placeholder="Crop Type" value={cropType} onChangeText={setCropType} />
          <TextInput style={styles.input} placeholder="Location" value={location} onChangeText={setLocation} />
        </View>
      )}

      {step === 2 && (
        <View>
          <TextInput
            style={styles.input}
            placeholder="Funding Goal (LKR)"
            value={fundingGoal}
            onChangeText={setFundingGoal}
            keyboardType="numeric"
          />
          <TextInput
            style={styles.input}
            placeholder="Duration (Months)"
            value={durationMonths}
            onChangeText={setDurationMonths}
            keyboardType="numeric"
          />
          <TextInput style={styles.input} placeholder="Expected ROI" value={expectedRoi} onChangeText={setExpectedRoi} />
        </View>
      )}

      {step === 3 && (
        <View>
          <TextInput
            style={[styles.input, styles.bigInput]}
            placeholder="Project Description"
            value={description}
            onChangeText={setDescription}
            multiline
          />
          <Pressable style={styles.altBtn} onPress={pickCover}>
            <Text style={styles.altText}>Upload Cover Photo</Text>
          </Pressable>
          {coverPhotoUri ? <Image source={{ uri: coverPhotoUri }} style={styles.preview} /> : null}
        </View>
      )}

      <View style={styles.row}>
        {step > 1 ? (
          <Pressable style={[styles.btn, styles.grayBtn]} onPress={() => setStep((s) => s - 1)}>
            <Text style={styles.grayText}>Back</Text>
          </Pressable>
        ) : null}

        {step < 3 ? (
          <Pressable style={styles.btn} onPress={() => setStep((s) => s + 1)}>
            <Text style={styles.btnText}>Next</Text>
          </Pressable>
        ) : (
          <Pressable style={styles.btn} onPress={submit}>
            <Text style={styles.btnText}>Submit Project</Text>
          </Pressable>
        )}
      </View>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec' },
  content: { padding: 16 },
  title: { fontSize: 28, fontWeight: '800', color: '#1f5d27' },
  step: { color: '#4f7652', marginBottom: 12, marginTop: 4 },
  input: {
    backgroundColor: '#fff',
    borderWidth: 1,
    borderColor: '#c5ddbf',
    borderRadius: 12,
    paddingHorizontal: 12,
    paddingVertical: 11,
    marginBottom: 10,
  },
  bigInput: { minHeight: 120, textAlignVertical: 'top' },
  altBtn: { backgroundColor: '#d8efd2', borderRadius: 12, paddingVertical: 11, alignItems: 'center' },
  altText: { color: '#245428', fontWeight: '700' },
  preview: { width: '100%', height: 170, borderRadius: 12, marginTop: 10 },
  row: { flexDirection: 'row', gap: 10, marginTop: 16 },
  btn: { flex: 1, backgroundColor: '#2e7d32', paddingVertical: 13, borderRadius: 12, alignItems: 'center' },
  btnText: { color: '#fff', fontWeight: '800' },
  grayBtn: { backgroundColor: '#e0eadf' },
  grayText: { color: '#3a5b3d', fontWeight: '800' },
});
