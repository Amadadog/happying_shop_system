function loginByCode(data) {
    return $axios({
        'url': '/user/loginByCode',
        'method': 'post',
        data
    })
}
function loginApi(data) {
    return $axios({
        'url': '/user/loginByUser',
        'method': 'post',
        data
    })
}
function registerApi(data) {
    return $axios({
        'url': '/user/register',
        'method': 'post',
        data
    })
}

function loginoutApi() {
    return $axios({
        'url': '/user/loginout',
        'method': 'post',
    })
}

function sendMailCodeApi(data) {
    return $axios({
        'url': '/user/sendMailCode',
        'method': 'post',
        data
    })
}

  