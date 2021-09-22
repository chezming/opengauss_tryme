import Vue from 'vue'
import App from './App.vue'

import router from './router'
import Vuex from 'vuex'
//通过Vue.use()来使用这个Vuex
Vue.use(Vuex)

import store from './store/index'

import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.use(Element)

import '@/assets/css/pc.css'
// import '@/assets/css/text.css'
import '@/assets/css/iconfont.css'
Vue.config.productionTip = false
// import VueCodeMirror from 'vue-codemirror'
// // import 'codemirror/lib/codemirror.css'
// Vue.use(VueCodeMirror)
new Vue({
  render: h => h(App),
  store:store,
  router,
}).$mount('#app')
