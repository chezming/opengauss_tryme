import Vue from "vue";
import Router from "vue-router";

const Index = () => import("../pages/index/index.vue");
const Login = () => import("../pages/login/index.vue");

Vue.use(Router);
const originalPush = Router.prototype.push;
Router.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

const routes = [
    {
      path: "/",
      name: "Index",
      component: Index,
    },
    { path: "/login", component: Login },
   
  ];
  
  
  export default new Router({
    routes
  });