import { InjectionKey } from 'vue';
import { createStore, useStore as baseUseStore, Store } from 'vuex';
import mutations from './mutations'
import getters from './getters'
import actions from './actions'
import { useRoute } from 'vue-router';

// 创建唯一类型key
export const key: InjectionKey<Store<State>> = Symbol();

export interface State {
    token: string,
    user: StoreState.User
}

export const store = createStore<State>({
    state: {   
        token: '',
        user: null
    },
    getters,
    mutations,
    actions
})

export function useStore() {
    return baseUseStore(key)
}