import { InjectionKey } from 'vue';
import { createStore, createLogger } from 'vuex';
import root from './root'

// 创建唯一类型key
const debug = process.env.NODE_ENV !== 'production'

export const store = createStore({
    modules: {
        [root.name]: root.module
    },
    strict: debug,
    plugins: debug ? [createLogger()] : [],// debug add logger module
})