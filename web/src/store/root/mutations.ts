import { State, store } from '..'

export const SET_TOKEN = 'SET_TOKEN'
export const SET_USER = 'SET_USER'

export default {
    [SET_USER](state: State, user: StoreState.User) {
        state.user = user
    },
    [SET_TOKEN](state: State, token: string) {
        state.token = token
    }
}