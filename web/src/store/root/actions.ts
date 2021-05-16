import * as mutations from './mutations'
import * as auth from '../../api/auth'
import { store } from '..'
import router from '../../router'

export const LOGIN = 'LOGIN'

const actions = {
    async [LOGIN](state: StoreState.RootState, loginObj: StoreState.Login) {
        store.commit('root/' + mutations.SET_LOADING, true)
        try{
            const { body } = await auth.login(loginObj)
            store.commit('root/' + mutations.SET_USER, body)
            store.commit('root/' + mutations.SET_TOKEN, 'ssss')
            store.commit('root/' + mutations.SET_LOADING, false)
            router.push('/')
        }catch(err){
            console.log(err)
        }
        store.commit('root/' + mutations.SET_LOADING, false)
    }
}
export default actions