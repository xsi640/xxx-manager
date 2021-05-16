import { createStore, createLogger } from 'vuex';
import createPersistedState from 'vuex-persistedstate'

// 创建唯一类型key
const debug = process.env.NODE_ENV !== 'production'

export default createStore({
    strict: debug,
    plugins: debug ? [createLogger(), createPersistedState({storage:window.sessionStorage})] : [],// debug add logger module
})