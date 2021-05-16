declare namespace StoreState {

    interface RootState {
        token: string | null,
        loading: boolean,
        user: User | null
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