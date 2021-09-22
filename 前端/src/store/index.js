import Vue from "vue";
import Vuex from "vuex";

Vue.use(Vuex);

export default new Vuex.Store({
  state: {
    userInfo: JSON.parse(sessionStorage.getItem(`userInfo`)) || {},
  },
  getters: {
    currentUser: (state) => {
      return state.userInfo;
    },
  },
  mutations: {
    setUser(state, v) {
      sessionStorage.setItem(`userInfo`, JSON.stringify(v));
      state.userInfo = v;
    },
  },
  actions: {},
  modules: {},
});
