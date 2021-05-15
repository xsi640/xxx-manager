import Cookies from 'js-cookie'

const TOKEN_KEY = 'xxx-mananger-token'
export const getToken = () => Cookies.get(TOKEN_KEY)
export const setToken = (token:String) => Cookies.set(TOKEN_KEY, token)
export const removeToken = () => Cookies.remove(TOKEN_KEY)