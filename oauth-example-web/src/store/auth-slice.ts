import { createSlice, PayloadAction } from "@reduxjs/toolkit"
import { User } from "@/type/user.ts"

interface AuthState {
  isAuthenticated: boolean
  user: User | null
  authorisationToken: string | null
}

const initialState: AuthState = {
  isAuthenticated: false,
  user: null,
  authorisationToken: null
}

const authSlice = createSlice({
  name: "auth",
  initialState,
  reducers: {
    loginSuccess(state, action: PayloadAction<{
      user: User
      authorisationToken: string
    }>) {
      state.isAuthenticated = true;
      state.user = action.payload.user;
      state.authorisationToken = action.payload.authorisationToken
    },
    logout(state) {
      state.isAuthenticated = false;
      state.user = null;
      state.authorisationToken = null
    },
  }
})

export const { loginSuccess, logout } = authSlice.actions
export default authSlice.reducer