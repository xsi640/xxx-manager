import axios from 'axios'
import { message } from 'ant-design-vue'
import { store } from '../store'

const service = axios.create({
    baseURL: '/api/v1/',
    timeout: 5000
})

service.interceptors.request.use(
    (config) => {
        console.log(store)
        if (store.getters.token) {
            config.headers['X-Access-Token'] = store.getters.token
        }
        console.log(config)
        return config
    },
    (error) => {
        Promise.reject(error)
    }
)

service.interceptors.response.use(
    (response) => {
        console.log('response', response)
        const res = response.data
        const status = res.status
        if (status != 0 && status != 200) {
            message.error(res.message);
            return Promise.reject(new Error(res.message || 'Error'))
        }
        return res
    },
    (error) => {
        message.error(error.message);
        return Promise.reject(error)
    }
)

export default service