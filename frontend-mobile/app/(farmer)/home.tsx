import React, { useMemo } from 'react';
import { FlatList, Pressable, ScrollView, StyleSheet, Text, View } from 'react-native';
import { useRouter } from 'expo-router';
import { useAppState } from '@/src/context/AppContext';

export default function FarmerHomeScreen() {
  const router = useRouter();
  const { user, projects } = useAppState();

  const mine = useMemo(() => projects.filter((p) => p.ownerKey.includes(user?.phone ?? '')), [projects, user?.phone]);
  const raised = mine.reduce((sum, p) => sum + p.raisedAmount, 0);

  return (
    <ScrollView style={styles.screen} contentContainerStyle={styles.content}>
      <View style={styles.headerRow}>
        <View>
          <Text style={styles.title}>Farmer Dashboard</Text>
          <Text style={styles.subtitle}>Welcome back, {user?.firstName ?? 'Farmer'}</Text>
        </View>
        <Pressable style={styles.avatar} onPress={() => router.push('/(farmer)/profile')}>
          <Text style={styles.avatarText}>{(user?.firstName ?? 'F').slice(0, 1)}</Text>
        </Pressable>
      </View>

      <View style={styles.kpiCard}>
        <Text style={styles.kpiLabel}>Total LKR Raised</Text>
        <Text style={styles.kpiValue}>LKR {raised.toLocaleString()}</Text>
      </View>

      <View style={styles.alertCard}>
        <Text style={styles.alertTitle}>AI Weather Alerts</Text>
        <Text style={styles.alertText}>Low rainfall risk next 48h in Kandy. Suggested: adjust irrigation cycle.</Text>
      </View>

      <View style={styles.btnRow}>
        <Pressable style={styles.actionBtn} onPress={() => router.push('/project/create')}>
          <Text style={styles.actionText}>Add Project</Text>
        </Pressable>
        <Pressable style={styles.actionBtn} onPress={() => mine[0] && router.push(`/farmer/project-manage/${mine[0].id}`)}>
          <Text style={styles.actionText}>Post Update</Text>
        </Pressable>
        <Pressable style={styles.actionBtn} onPress={() => router.push('/farmer/analytics')}>
          <Text style={styles.actionText}>Analytics</Text>
        </Pressable>
      </View>

      <Text style={styles.sectionTitle}>Active Projects</Text>
      <FlatList
        data={mine}
        keyExtractor={(item) => item.id}
        scrollEnabled={false}
        renderItem={({ item }) => (
          <Pressable style={styles.projectCard} onPress={() => router.push(`/farmer/project-manage/${item.id}`)}>
            <Text style={styles.projectTitle}>{item.projectTitle}</Text>
            <Text style={styles.projectMeta}>{item.location} • {item.cropType}</Text>
            <Text style={styles.projectMeta}>Raised LKR {item.raisedAmount.toLocaleString()} / {item.fundingGoal.toLocaleString()}</Text>
          </Pressable>
        )}
      />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  screen: { flex: 1, backgroundColor: '#eef9ec' },
  content: { padding: 16, paddingBottom: 24 },
  headerRow: { flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' },
  title: { fontSize: 28, fontWeight: '800', color: '#184f1e' },
  subtitle: { color: '#3d6b43', marginTop: 4 },
  avatar: { width: 42, height: 42, borderRadius: 21, backgroundColor: '#2e7d32', alignItems: 'center', justifyContent: 'center' },
  avatarText: { color: '#fff', fontWeight: '800' },
  kpiCard: { backgroundColor: '#2e7d32', borderRadius: 16, padding: 16, marginTop: 16 },
  kpiLabel: { color: '#dff4dc', fontWeight: '700' },
  kpiValue: { color: '#fff', fontSize: 28, fontWeight: '900', marginTop: 8 },
  alertCard: { marginTop: 14, backgroundColor: '#fff8df', borderRadius: 14, padding: 14 },
  alertTitle: { color: '#7c5a00', fontWeight: '800', marginBottom: 6 },
  alertText: { color: '#6b551e', lineHeight: 20 },
  btnRow: { marginTop: 14, flexDirection: 'row', gap: 8 },
  actionBtn: { flex: 1, backgroundColor: '#d6efd1', borderRadius: 12, paddingVertical: 10, alignItems: 'center' },
  actionText: { color: '#255c2a', fontWeight: '800', fontSize: 12 },
  sectionTitle: { marginTop: 18, marginBottom: 10, fontWeight: '800', color: '#25532a', fontSize: 18 },
  projectCard: { backgroundColor: '#fff', borderRadius: 12, padding: 14, marginBottom: 10 },
  projectTitle: { fontWeight: '800', color: '#1c4121' },
  projectMeta: { color: '#507053', marginTop: 6 },
});
