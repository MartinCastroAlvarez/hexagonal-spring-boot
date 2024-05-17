// Store.tsx

import { createSlice, configureStore } from '@reduxjs/toolkit';

export const TOKEN_STORAGE_KEY = 'tokenKey';
export const USER_ID_STORAGE_KEY = 'userIdKey';
export const USER_NAME_STORAGE_KEY = 'userNameKey';
export const ROLE_STORAGE_KEY = 'userRole';

export const sessionSlice = createSlice({
  name: 'session',
  initialState: {
    id: localStorage.getItem(USER_ID_STORAGE_KEY) || null,
    name: localStorage.getItem(USER_NAME_STORAGE_KEY) || null,
    token: localStorage.getItem(TOKEN_STORAGE_KEY) || null,
    role: localStorage.getItem(ROLE_STORAGE_KEY) || null,
  },
  reducers: {
    login: (state, action) => {
      console.log(`SessionReducer.login: ${JSON.stringify(action.payload)}`);
      if (!action.payload.Token || !action.payload.userId || !action.payload.userName || !action.payload.userRole)
        throw new Error('Invalid login action');
      state.id = action.payload.userId;
      state.name = action.payload.userName;
      state.token = action.payload.Token;
      state.role = action.payload.userRole;
      localStorage.setItem(TOKEN_STORAGE_KEY, action.payload.Token);
      localStorage.setItem(USER_ID_STORAGE_KEY, action.payload.userId);
      localStorage.setItem(USER_NAME_STORAGE_KEY, action.payload.userName);
      localStorage.setItem(ROLE_STORAGE_KEY, action.payload.userRole);
    },
    logout: state => {
      console.log(`SessionReducer.logout`);
      state.id = null;
      state.name = null;
      state.token = null;
      state.role = null;
      localStorage.removeItem(TOKEN_STORAGE_KEY);
      localStorage.removeItem(USER_ID_STORAGE_KEY);
      localStorage.removeItem(USER_NAME_STORAGE_KEY);
      localStorage.removeItem(ROLE_STORAGE_KEY);
    },
  },
});

export const store = configureStore({
  reducer: {
    session: sessionSlice.reducer,
  },
});

export type RootState = ReturnType<typeof store.getState>;

export type AppDispatch = typeof store.dispatch;

export const { login, logout } = sessionSlice.actions;