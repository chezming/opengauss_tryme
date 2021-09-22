import Vue from 'vue';
import login from './login.vue';
import router from './login.router';

import store from '../store/index'

import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'
Vue.use(Element)

// import store from './store';
Vue.config.productionTip = false;
new Vue({  
    router,  
    store:store,
    render: h => h(login),
}).$mount('#login');