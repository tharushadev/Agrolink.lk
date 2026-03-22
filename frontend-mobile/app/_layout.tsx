import { Stack } from "expo-router";
import { PaperProvider } from 'react-native-paper';
import { AppProvider } from '@/src/context/AppContext';

export default function RootLayout() {
  return (
    <PaperProvider>
      <AppProvider>
        <Stack screenOptions={{ headerShown: false }}>
          <Stack.Screen name="index" />
          <Stack.Screen name="login" />
          <Stack.Screen name="signup" />
          <Stack.Screen name="(farmer)" />
          <Stack.Screen name="(investor)" />
          <Stack.Screen name="project/create" />
          <Stack.Screen name="farmer/project-manage/[id]" />
          <Stack.Screen name="farmer/analytics" />
          <Stack.Screen name="farmer/trust-score" />
          <Stack.Screen name="farmer/edit-profile" />
          <Stack.Screen name="farmer/security" />
          <Stack.Screen name="profile/payment" />
          <Stack.Screen name="profile/support" />
          <Stack.Screen name="profile/about" />
          <Stack.Screen name="investment/[id]" />
          <Stack.Screen name="chat/index" />
          <Stack.Screen name="portfolio/ai" />
        </Stack>
      </AppProvider>
    </PaperProvider>
  );
}