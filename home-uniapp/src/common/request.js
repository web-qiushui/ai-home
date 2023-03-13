import config from '../common/config';

function _request(params, key) {
    return new Promise((resolve, reject) => {
        const requestTask = uni.request({
            url: config.host + params.url,
            data: params.data,
            method: params.method || 'GET',
            success(res) {
                resolve(res);
            },
            fail() {
                reject({
                    msg: '请求失败',
                    url: config.host + url,
                    method: params.method || 'GET',
                    data
                });
            }
        });
    });
}

function request(params, key) {
    return _request(params, key)
}

function get(params, key) {
    const method = 'GET';
    return _request({...params, method}, key);
}

function post(params, key) {
    const method = 'POST';
    return _request({ ...params, method }, key);
}

function put(params, key) {
    const method = 'PUT';
    return _request({ ...params, method }, key);
}

function _delete(params, key) {
    const method = 'DELETE';
    return _request({ ...params, method }, key); 
}

function abort(key) { }

request.get = get;
request.post = post;
request.put = put;
request.delete = _delete;
request.abort = abort;

export default request;
