import { Tabs } from 'expo-router';
import { MaterialCommunityIcons } from '@expo/vector-icons';

export default function InvestorTabsLayout() {
  return (
    <Tabs screenOptions={{ headerShown: false, tabBarActiveTintColor: '#156f2c', tabBarInactiveTintColor: '#648468' }}>
      <Tabs.Screen
        name="home"
        options={{
          title: 'Market',
          tabBarIcon: ({ color, size }) => <MaterialCommunityIcons name="finance" size={size} color={color} />,
        }}
      />
      <Tabs.Screen
        name="profile"
        options={{
          title: 'Profile',
          tabBarIcon: ({ color, size }) => <MaterialCommunityIcons name="account" size={size} color={color} />,
        }}
      />
    </Tabs>
  );
}
