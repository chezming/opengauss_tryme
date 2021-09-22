import axios from 'axios'
// import qs from 'qs'
import website from '../config/website';
import { Message, Loading  } from 'element-ui'
// import {Base64} from 'js-base64';
// import store from '@/store/index';
// import router from '@/router/index';
// const serverUrl = 'http://localhost:1888/'

// console.log('env:', process.env.NODE_ENV, process.env.VUE_APP_BASE_URL)


let token = 'bearer ' + sessionStorage.getItem("access_token")

//axios默认参数配置
axios.defaults.timeout = 1000 * 60;
//返回其他状态码
axios.defaults.validateStatus = function (status) {
  return status >= 200 && status <= 500;
};
//跨域请求，允许保存cookie
axios.defaults.withCredentials = true;
//loading配置
const loadingOption = {}
let loadingInstance;

//http request拦截
axios.interceptors.request.use(config => {
  
  const meta = (config.meta || {});
  const isToken = meta.isToken === false;
  const notShowLoading = meta.showLoading !== undefined && meta.showLoading === false;
  if(!notShowLoading){
     //开启 loading动画
    loadingInstance = Loading.service(loadingOption);
  }
 
//   config.headers['Authorization'] = `Basic ${Base64.encode(`${website.clientId}:${website.clientSecret}`)}`;
  //让每个请求携带token
  if (!isToken) {
    config.headers[website.tokenHeader] = token
  }
  //headers中配置text请求
  if (config.text === true) {
    config.headers["Content-Type"] = "text/plain";
  }
 
  return config
}, error => {
  return Promise.reject(error)
});

//http response 拦截
axios.interceptors.response.use(res => {
  //关闭 loading
  if(loadingInstance){
    loadingInstance.close()
  }
// console.log('axios',res)
  //获取状态码
  const status = res.data.code || res.status;
  const statusWhiteList = website.statusWhiteList || [];
  const message = res.data.message || res.data.error_description || '未知错误';
  //如果在白名单里则自行catch逻辑处理
  if (statusWhiteList.includes(status)) return Promise.reject(res);
  //如果是401则跳转到登录页面
  if (status === 401) store.dispatch('FedLogOut').then(() => router.push({path: '/login'}));
  // 如果请求为非200否者默认统一处理
  if (status !== 200) {
    Message({
        message: message,
        type: 'error'
      });
    return Promise.reject(new Error(message))
  }
  return res.data;
}, error => {
  if(loadingInstance){
    loadingInstance.close()
  }
  return Promise.reject(new Error(error));
});

// export default function request(url,method,data) {
//     if((method === 'GET') && data){
//       url += '?' + qs.stringify(data);
//     }
//     const headers = {};
//     headers['Authorization'] = 'Basic c2FiZXI6c2FiZXJfc2VjcmV0';
//     headers['Tenant-Id'] = '000000';
//     headers['Blade-Auth'] = token
//     return axios({
//         url:url,
//         method:method,
//         headers: headers,
//         data:data
//     }).then((res) => {
//       return res
//     })
//   }
// export {serverUrl}
export default axios;