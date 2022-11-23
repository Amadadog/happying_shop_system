function loginApi(data) {
  return $axios({
    'url': '/happying_shop_system/employee/login',
    'method': 'post',
    data
  })
}

function logoutApi(){
  return $axios({
    'url': '/happying_shop_system/employee/logout',
    'method': 'post',
  })
}
