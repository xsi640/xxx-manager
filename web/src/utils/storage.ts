interface StorageObj {
    set: (key: string, val: string) => void
    get: (key: string, defaultVal: any | null) => any | null
    setSession: (key: string, val: string) => void
    getSession: (key: string, defaultVal: any | null) => any | null
}

const STORAGE_KEY = "xxx-manager-storage"

const storage: StorageObj = {
    set: (key, val) => {
        const store = localStorage.getItem(STORAGE_KEY)
        let data = new Map()
        if (store && store !== 'undefined') {
            data = {
                ...JSON.parse(store)
            }
        }
        data.set(key, val)
        localStorage.setItem(STORAGE_KEY, JSON.stringify(data))
    },

    get: (key, defaultVal) => {
        const store = localStorage.getItem(STORAGE_KEY)
        if (store && store !== 'undefined') {
            let data = JSON.parse(store) as Map<string, any>
            if (data.has(key)) {
                return data.get(key)
            }
        }
        return defaultVal
    },

    setSession: (key, val) => {
        sessionStorage.setItem(key, val)
    },
    getSession: (key, defaultVal) => {
        const val = sessionStorage.getItem(key)
        return val ? val : defaultVal
    }
}

export default storage