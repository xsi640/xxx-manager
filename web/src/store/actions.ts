import { State, store } from '../store'
import * as mutations from '../store/mutations-types'
import * as action from '../store/actions-types'
import * as auth from '../api/auth'
import router from '../router'

const actions = {
    async [action.LOGIN](state: State, req: any) {
        const { data } = await auth.login(req)
        store.commit(mutations.SET_USER, data)
        store.commit(mutations.SET_TOKEN, 'ssss')
        router.push('/')
    }
}
export default actions