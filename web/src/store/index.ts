import { InjectionKey } from 'vue';
import { createStore, useStore as baseUseStore, Store } from 'vuex';

// 创建唯一类型key
export const key: InjectionKey<Store<State>> = Symbol();

export interface State {
    login: Boolean,
    user: StoreState.User
}

export const store = createStore<State>({
    state: {
        login: false,
        user: null
    },
    getters: {},
    mutations: {}
})

export function useStore() {
    return baseUseStore(key)
}