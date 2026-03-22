import React, { createContext, useContext, useState } from 'react';

export type UserRole = 'FARMER' | 'INVESTOR';

export type AppUser = {
  firstName: string;
  lastName: string;
  phone: string;
  nic: string;
  role: UserRole;
  trustScore: number;
};

export type InvestmentRecord = {
  investorName: string;
  amount: number;
};

export type ProjectUpdate = {
  id: string;
  text: string;
  photoUri?: string;
  timestamp: string;
};

export type FarmProject = {
  id: string;
  ownerKey: string;
  projectTitle: string;
  cropType: string;
  location: string;
  fundingGoal: number;
  raisedAmount: number;
  durationMonths: number;
  expectedRoi: string;
  description: string;
  coverPhotoUri?: string;
  status: 'ACTIVE' | 'PENDING';
  risk: 'LOW' | 'MEDIUM' | 'HIGH';
  investors: InvestmentRecord[];
  updates: ProjectUpdate[];
};

type CreateProjectInput = {
  projectTitle: string;
  cropType: string;
  location: string;
  fundingGoal: number;
  durationMonths: number;
  expectedRoi: string;
  description: string;
  coverPhotoUri?: string;
};

type AppContextValue = {
  user: AppUser | null;
  projects: FarmProject[];
  login: (role: UserRole, email: string) => void;
  signup: (payload: Omit<AppUser, 'trustScore'>) => void;
  logout: () => void;
  createProject: (input: CreateProjectInput) => string;
  addProjectUpdate: (projectId: string, text: string, photoUri?: string) => void;
  investInProject: (projectId: string, amount: number, investorName: string) => void;
  linkBankAccount: (bank: string, account: string) => void;
  withdrawFunds: (amount: number) => void;
  linkedBankName: string | null;
  walletBalance: number;
};

const AppContext = createContext<AppContextValue | undefined>(undefined);

const seedProjects: FarmProject[] = [
  {
    id: 'proj-101',
    ownerKey: 'farmer:sample@agrolink.lk',
    projectTitle: 'Kandy Smart Paddy - Season 1',
    cropType: 'Paddy',
    location: 'Kandy',
    fundingGoal: 850000,
    raisedAmount: 460000,
    durationMonths: 6,
    expectedRoi: '18%-24%',
    description: 'IoT monitored paddy field with climate alerts and drip controls.',
    status: 'ACTIVE',
    risk: 'LOW',
    investors: [
      { investorName: 'Nimali Perera', amount: 120000 },
      { investorName: 'Arun Silva', amount: 80000 },
    ],
    updates: [
      { id: 'up-1', text: 'Land prepared and seedbeds completed.', timestamp: new Date().toISOString() },
    ],
  },
  {
    id: 'proj-102',
    ownerKey: 'farmer:sample@agrolink.lk',
    projectTitle: 'Polonnaruwa Maha Harvest',
    cropType: 'Paddy',
    location: 'Polonnaruwa',
    fundingGoal: 600000,
    raisedAmount: 170000,
    durationMonths: 5,
    expectedRoi: '14%-20%',
    description: 'Mechanized harvest plan with quality assurance checkpoints.',
    status: 'PENDING',
    risk: 'MEDIUM',
    investors: [],
    updates: [],
  },
  {
    id: 'proj-103',
    ownerKey: 'farmer:other@agrolink.lk',
    projectTitle: 'Anuradhapura Eco Rice',
    cropType: 'Paddy',
    location: 'Anuradhapura',
    fundingGoal: 950000,
    raisedAmount: 740000,
    durationMonths: 7,
    expectedRoi: '20%-28%',
    description: 'Sustainable cultivation with verified organic inputs.',
    status: 'ACTIVE',
    risk: 'LOW',
    investors: [
      { investorName: 'Kasun Jayasuriya', amount: 300000 },
      { investorName: 'Savi Fernando', amount: 140000 },
    ],
    updates: [
      { id: 'up-2', text: 'Irrigation cycle completed for zone A.', timestamp: new Date().toISOString() },
    ],
  },
];

export function AppProvider({ children }: { children: React.ReactNode }) {
  const [user, setUser] = useState<AppUser | null>(null);
  const [projects, setProjects] = useState<FarmProject[]>(seedProjects);
  const [linkedBankName, setLinkedBankName] = useState<string | null>(null);
  const [walletBalance, setWalletBalance] = useState<number>(120000);

  const login = (role: UserRole, email: string) => {
    const first = role === 'FARMER' ? 'Farmer' : 'Investor';
    setUser({
      firstName: first,
      lastName: 'User',
      phone: '0710000000',
      nic: role === 'FARMER' ? '199012345678' : '-',
      role,
      trustScore: role === 'FARMER' ? 78 : 60,
    });
  };

  const signup = (payload: Omit<AppUser, 'trustScore'>) => {
    setUser({ ...payload, trustScore: payload.role === 'FARMER' ? 68 : 60 });
  };

  const logout = () => setUser(null);

  const createProject = (input: CreateProjectInput) => {
    const projectId = `proj-${Date.now()}`;
    const ownerKey = `farmer:${(user?.phone ?? 'unknown')}`;
    const newProject: FarmProject = {
      id: projectId,
      ownerKey,
      projectTitle: input.projectTitle,
      cropType: input.cropType,
      location: input.location,
      fundingGoal: input.fundingGoal,
      raisedAmount: 0,
      durationMonths: input.durationMonths,
      expectedRoi: input.expectedRoi,
      description: input.description,
      coverPhotoUri: input.coverPhotoUri,
      status: 'PENDING',
      risk: 'MEDIUM',
      investors: [],
      updates: [],
    };

    setProjects((prev) => [newProject, ...prev]);
    return projectId;
  };

  const addProjectUpdate = (projectId: string, text: string, photoUri?: string) => {
    setProjects((prev) =>
      prev.map((project) => {
        if (project.id !== projectId) {
          return project;
        }

        const update: ProjectUpdate = {
          id: `up-${Date.now()}`,
          text,
          photoUri,
          timestamp: new Date().toISOString(),
        };

        return { ...project, updates: [update, ...project.updates] };
      }),
    );
  };

  const investInProject = (projectId: string, amount: number, investorName: string) => {
    setProjects((prev) =>
      prev.map((project) => {
        if (project.id !== projectId) {
          return project;
        }

        const nextRaised = project.raisedAmount + amount;
        return {
          ...project,
          raisedAmount: nextRaised,
          status: 'ACTIVE',
          investors: [{ investorName, amount }, ...project.investors],
        };
      }),
    );
  };

  const linkBankAccount = (bank: string, account: string) => {
    setLinkedBankName(`${bank} • ${account.slice(-4)}`);
  };

  const withdrawFunds = (amount: number) => {
    setWalletBalance((prev) => Math.max(0, prev - amount));
  };

  const value = {
    user,
    projects,
    login,
    signup,
    logout,
    createProject,
    addProjectUpdate,
    investInProject,
    linkBankAccount,
    withdrawFunds,
    linkedBankName,
    walletBalance,
  };

  return <AppContext.Provider value={value}>{children}</AppContext.Provider>;
}

export function useAppState() {
  const context = useContext(AppContext);
  if (!context) {
    throw new Error('useAppState must be used within AppProvider');
  }
  return context;
}
