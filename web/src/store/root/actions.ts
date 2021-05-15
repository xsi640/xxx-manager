import * as mutations from './mutations'
import * as auth from '../../api/auth'
import { store } from '..'
import router from '../../router'

export const LOGIN = 'LOGIN'

const actions = {
    async [LOGIN](state: StoreState.RootState, loginObj: StoreState.Login) {
        const { body } = await auth.login(loginObj)
        console.log(body)
        store.commit('root/' + mutations.SET_USER, body)
        store.commit('root/' + mutations.SET_TOKEN, 'ssss')
        router.push('/')
    }
}
export default actions