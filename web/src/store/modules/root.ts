import { VuexModule, Module, Action, Mutation, getModule } from 'vuex-module-decorators'
import * as auth from '/@/api/auth'
import router from '/@/router'
import store from '/@/store'

@Module({ namespaced: true, name: 'root', dynamic: true, store })
class Root extends VuexModule implements StoreState.RootState {
    public user: StoreState.User | null = null;
    public loading = false;
    public token = '';

    get TOKEN() {
        return this.token;
    }

    @Mutation
    private SET_USER(user: StoreState.User) {
        this.user = user
    }

    @Mutation
    private SET_TOKEN(token: string) {
        this.token = token
    }

    @Mutation
    private SET_LOADING(loading: boolean) {
        this.loading = loading
    }

    @Action
    public async login(loginObj: StoreState.Login) {
        this.SET_LOADING(true)
        try {
            const { body } = await auth.login(loginObj)
            this.SET_USER(body)
            this.SET_TOKEN('aaa')
            router.push('/')
        } catch (err) {
            console.log(err)
        }
        this.SET_LOADING(false)
    }
}

export const RootModule = getModule(Root)