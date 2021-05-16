import request from '/@/utils/request'

export const login = (data: any) =>
    request({
        url: '/auth/login',
        method: 'post',
        data
    }) as any
