import { createStore, createLogger } from 'vuex';
import { RootModule } from './modules/root'

// 创建唯一类型key
const debug = process.env.NODE_ENV !== 'production'

export const store = createStore({
    // modules: [
    //     RootModule
    // ],
    strict: debug,
    plugins: debug ? [createLogger()] : [],// debug add logger module
})