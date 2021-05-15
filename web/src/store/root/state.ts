import storage from '../../utils/storage'

const state: StoreState.RootState = {
    user: null,
    token: storage.getSession('TOKEN', null)
}

export default state