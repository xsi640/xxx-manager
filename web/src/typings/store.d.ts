declare namespace StoreState {

    interface RootState {
        loading: boolean,
        user: User | null
        token: string
    }

    interface User {
        username: string,
        displayName: string
    }

    interface Login {
        username: string,
        password: string
    }
}