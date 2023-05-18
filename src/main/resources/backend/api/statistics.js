// 查询套餐销量接口
const getStatisticsSetmealSales = (params) => {
    return $axios({
        url: '/statistics/setmeal/sales',
        method: 'get',
        params
    })
}
// 查询商品销量接口
const getStatisticsDishSales = (params) => {
    return $axios({
        url: '/statistics/dish/sales',
        method: 'get',
        params
    })
}
// 查询接口访问量
const getVisits = (params) => {
    return $axios({
        url: '/statistics/visits',
        method: 'get',
        params
    })
}
// 查询累积销售金额
const getMoney = (params) => {
    return $axios({
        url: '/statistics/money',
        method: 'get',
        params
    })
}
// 查询注册用户
const getUsers = (params) => {
    return $axios({
        url: '/statistics/user/numbers',
        method: 'get',
        params
    })
}
// 查询注册用户
const getRegisterUsers = (params) => {
    return $axios({
        url: '/statistics/registerNumbers',
        method: 'get',
        params
    })
}
// 查询销量前十
const getTop10Sales = (params) => {
    return $axios({
        url: '/statistics/top10Sales',
        method: 'get',
        params
    })
}
