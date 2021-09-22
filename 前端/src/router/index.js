import Vue from "vue";
import Router from "vue-router";

const Index = () => import("@/pages/index/index.vue");
const Loading = () => import("@/pages/index/Loading.vue");
// const Login = () => import("@/pages/login/index.vue");
const MainPage = () => import("@/pages/mainPage/index.vue");
const MainPageOld = () => import("@/pages/mainPage/index-old.vue");
const Portal = () => import("@/pages/mainPage/portal.vue")

Vue.use(Router);
const originalPush = Router.prototype.push;
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

const routes = [
  {
    path: "/",
    name: "loading",
    component: Loading,
  },
  {
    path: "/login",
    name: "Index",
    component: Index,
  },

  // { path: "/login", component: Login },
  { path: "/portal", component: Portal},
  { path: "/mainPage", component: MainPage },
  { path: "/MainPageOld", component: MainPageOld },
];


export default new Router({
  routes
});
