import { useStore } from 'vuex'
import { store } from '..'

const getters = {
    user: (state: StoreState.RootState) => state.user,
    token: (state: StoreState.RootState) => state.token
}

export default getters