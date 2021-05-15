import { State, store } from '../store'
import * as types from './mutations-types'

export default {
    [types.SET_USER](state: State, user: StoreState.User) {
        state.user = user
    },
    [types.SET_TOKEN](state: State, token: string) {
        state.token = token
    }
}