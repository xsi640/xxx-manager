import { useStore } from 'vuex'
import { State } from '../store'

const getters = {
    user: (state: State) => state.user,
    token: (state: State) => state.token
}

export default getters