import React, { useMemo } from 'react';
import { FlatList, Pressable, StyleSheet, Text, View } from 'react-native';
import { useRouter } from 'expo-router';
import { useAppState } from '@/src/context/AppContext';

export default function FarmerProjectsScreen() {
  const router = useRouter();
  const { projects, user } = useAppState();

  const myProjects = useMemo(
    () => projects.filter((project) => project.ownerKey.includes(user?.phone ?? '') && (project.status === 'ACTIVE' || project.status === 'PENDING')),
    [projects, user?.phone],
  );

  return (
    <View style={styles.screen}>
      <Text style={styles.title}>My Projects</Text>
      <FlatList
        data={myProjects}
        keyExtractor={(item) => item.id}
        renderItem={({ item }) => (
          <Pressable style={styles.card} onPress={() => router.push(`/farmer/project-manage/${item.id}`)}>
            <Text style={styles.cardTitle}>{item.projectTitle}</Text>
            <Text style={styles.cardText}>Status: {item.status}</Text>
            <Text style={styles.cardText}>LKR {item.raisedAmount.toLocaleString()} / {item.fundingGoal.toLocaleString()}</Text>
          </Pressable>
        )}
        ListEmptyComponent={<Text style={styles.empty}>No projects yet. Create one now.</Text>}
      />

      <Pressable style={styles.fab} onPress={() => router.push('/project/create')}>
        <Text style={styles.fabText}>+</Text>
      </Pressable>
    </View>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec', padding: 16 },
  title: { fontSize: 28, fontWeight: '800', color: '#205527', marginBottom: 14 },
  card: { backgroundColor: '#fff', borderRadius: 12, padding: 14, marginBottom: 10 },
  cardTitle: { fontWeight: '800', color: '#203d22', marginBottom: 6 },
  cardText: { color: '#4f7052' },
  empty: { color: '#5e7a60', marginTop: 20 },
  fab: {
    position: 'absolute',
    right: 18,
    bottom: 24,
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: '#2e7d32',
    alignItems: 'center',
    justifyContent: 'center',
    elevation: 4,
  },
  fabText: { color: '#fff', fontSize: 32, marginTop: -2 },
});
