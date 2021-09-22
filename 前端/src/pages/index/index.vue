<template>
  <!-- <div>

  <div class="title-div">
    {{title}}
  </div>
    <div class="logo-div">
      <img src="https://share-test-0002.oss-cn-guangzhou.aliyuncs.com/opengauss/giao.png" />
      <img src="https://share-test-0002.oss-cn-guangzhou.aliyuncs.com/opengauss/openGauss-logo.png" />
    </div>
   <div class="giao-rows justify-center align-center margin-top">
      <el-button type="danger" class="margin-right" @click="goGiteeLogin()"
        > <i class="iconfont icon-gitee"></i> 码云快捷登录</el-button
      >

    </div>

  
  </div> -->

  
  <div>
    
 <div class="bg" :style="{backgroundImage:'url('+bgImage+')'}">  </div>
      <div class="login"  >

         <div class="head" :style="{backgroundImage:'url('+headImage+')'}"></div>



        <div class="message text-black">{{title}}</div>
        <div class="form">
         <el-button type="danger" class="margin-right" @click="goGiteeLogin()"
        > <i class="iconfont icon-gitee"></i> 码云快捷登录</el-button
      >
      <el-button type="primary" @click="goIndex" v-if="!isProd"><i class="iconfont icon-openGauss" ></i> 直接进入系统</el-button>
        </div>
    </div>
  
    </div>
</template>

<script>
import qs from "qs";
import { normalLogin,giteeLogin  } from "@/service/api.js";
import { mapMutations } from "vuex";

export default {
  data() {
    return {
     
      code: "",
      title: "欢迎在线体验OpenGauss",
      maiUrl: `index.html#/portal/`,
            url: require("@/assets/img/picture.png"),
          headImage: "https://share-test-0002.oss-cn-guangzhou.aliyuncs.com/opengauss/giao.png",
               bgImage: require("@/assets/img/bg.jpg")
    };
  },
  computed: {
    isProd:function(){ 
      return process.env.NODE_ENV === "test"
    }
  },
  mounted () {
    const { code } = qs.parse(location.search.replace("?", ""));
    this.code = code;
    this.doLogin();
  },
  methods: {
    ...mapMutations(["setUser"]),
    doLogin() {
      if (!this.code || this.code === "") {
        return
      }
      giteeLogin({ code: this.code }).then((res) => {
        if(res.code === 200){
          this.$store.commit("setUser", res.result);
          location.href = this.maiUrl;
          // this.$router.push("/mainPage");
        }
      });
    },
    goGiteeLogin() {
      const client_id = process.env.VUE_APP_GITEE_CLIENT_ID
      const redict_url = process.env.VUE_APP_GITEE_REDICT_URL
      const url =
        `https://gitee.com/oauth/authorize?client_id=${client_id}&redirect_uri=${redict_url}&response_type=code`;
      location.href = url;
    },
    goIndex() {
      const params = {
        gitId: 12345,
        login: "童真住",
      };
      normalLogin(params).then((res) => {
        if (res.code === 200) {
          this.$store.commit("setUser", res.result);
          location.href = this.maiUrl;
        }
      });
      // this.$router.push("/mainPage");
    },
  },
};
</script>

<style lang="css" scoped>
   .bg {
        /* background-color: #000; */
        position: fixed;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        z-index: 0;
        background-size: cover;
        background-repeat: no-repeat;
    }


.giao-rows {
    display: flex;
    flex-direction: row !important;
  }

   .justify-center {
    justify-content: center;
  }

  .mt {
    margin-top: 5rpx;
  }

   .login {
        position: fixed;
        left: 0;
        right: 0;
        top: 0;
        bottom: 0;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        color: white;
        margin-top: -100px;
        z-index: 99;
        backdrop-filter: blur(20px);
    }

    .head {
        background-size: 100% auto;
        background-position: center center;
        height: 150px;
        width: 150px;
        border-radius: 100%;
        box-shadow: 0px 0px 5px 5px rgba(0, 0, 0, 0.1);
        margin-top: -50px;
    }

    .message {
        margin-top: 20px;
        font-size: 20px;
        text-shadow: 0px 0px 2px 2px rgba(0, 0, 0, 0.3);
        color: #eee;
        margin-bottom: 50px;
    }

    input {
        color: white;
        outline: none;
        border: none;
        margin: 5px;
        font-size: 16px;
        background-color: rgba(255, 255, 255, 0.3);
        padding: 8px 24px;
        border-radius: 20px;
        box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
    }

    ::-webkit-input-placeholder {
        color: #fff;
    }

    ::-moz-placeholder {
        color: #fff;
    }

    :-ms-input-placeholder {
        color: #fff;
    }

    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        vertical-align: middle;
    }

    .item {
        vertical-align: middle;
    }

    .item .iconfont {
        vertical-align: middle;
        display: inline-block;
        background-color: rgba(255, 255, 255, 0.3);
        font-size: 18px;
        border-radius: 100%;
        width: 36px;
        height: 36px;
        text-align: center;
        line-height: 36px;
        cursor: pointer;
        box-shadow: 0px 0px 5px rgba(0, 0, 0, 0.1);
    }

    .item .iconfont:hover {
        background-color: rgba(255, 255, 255, 0.5);
    }

    body, html {
    width: 100%;
    height: 100%;
    margin: 0;
    padding: 0;
    background-color: #000;
}
.el-button--danger {
    color: #FFF;
    background-color: red;
    border-color: red;
}
</style>>

