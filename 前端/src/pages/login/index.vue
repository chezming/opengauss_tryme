<template>
  <div>页面正在跳转中，请稍后...</div>
</template>

<script>
import qs from "qs";
import { giteeLogin } from "../../service/api.js";

export default {
  data() {
    return {
      code: "无",
      maiUrl: `index.html#/portal/`
    };
  },
  mounted() {
    const { code } = qs.parse(location.search.replace("?", ""));
    this.code = code;
    this.doLogin();
  },
  methods: {
    doLogin() {
      if (!this.code || this.code === "") {
        // 拿不到code就直接调回到登录页
        this.$router.push("/");
      }
      giteeLogin({ code: this.code }).then((res) => {
        if(res.code === 200){
          this.$store.commit("setUser", res.result);
          location.href = this.maiUrl;
          // this.$router.push("/mainPage");
        }
      });
    },
  },
};
</script>

<style></style>
