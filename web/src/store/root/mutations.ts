import storage from '../../utils/storage'

export const SET_TOKEN = 'SET_TOKEN'
export const SET_USER = 'SET_USER'
export const SET_LOADING = 'SET_LOADING'

export default {
    [SET_USER](state: StoreState.RootState, user: StoreState.User) {
        state.user = user
    },
    [SET_TOKEN](state: StoreState.RootState, token: string) {
        storage.setSession('TOKEN', token)
        state.token = token
    },
    [SET_LOADING](state: StoreState.RootState, loading: boolean) {
        state.loading = loading
    }
}