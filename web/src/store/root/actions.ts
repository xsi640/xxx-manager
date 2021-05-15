import { State, store } from '..'
import * as mutations from './mutations'

import * as auth from '../../api/auth'
import router from '../../router'

export const LOGIN = 'LOGIN'

const actions = {
    async [LOGIN](state: State, req: any) {
        const { data } = await auth.login(req)
        store.commit(mutations.SET_USER, data)
        store.commit(mutations.SET_TOKEN, 'ssss')
        router.push('/')
    }
}
export default actions