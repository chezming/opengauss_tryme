<template>
  <div class="portal">
    <div class="box">
      <!-- <div class="title">沙箱实验室优势</div>
            <div class="sub-title">量身定制，满足广泛业务需求</div>
            <div class="content">
                <div class="content-item">
                    <img src="" alt="">
                    <div class="desc">体验真实云场景</div>
                    <div class="sub-desc">模拟真实操作场景，快速调动资源，深度学习和使用华为云服务</div>
                </div>
                <div class="content-item">
                    <img src="" alt="">
                    <div class="desc">随时随地做实验</div>
                    <div class="sub-desc">搭配完善环境配置，不受地域限制，线上体验触手可及</div>
                </div>
                <div class="content-item">
                    <img src="" alt="">
                    <div class="desc">由浅入深易上手</div>
                    <div class="sub-desc">详细实验手册，手把手指导手册，直观实验报告，助力把控实验进程</div>
                </div>
            </div> -->
      <div class="content">
        <img src="../../assets/img/portal.png" alt="" />
      </div>
      <div class="btn">
        <el-button plain @click="goMainPage">沙箱体验</el-button>
        <el-button plain @click="goMainPageOld">快速体验</el-button>
      </div>
    </div>
  </div>
</template>
<script>
export default {
  data() {
    return {};
  },
  methods: {
    goMainPage() {
      this.$router.push("/MainPage");
    },
    goMainPageOld() {
      this.$router.push("/MainPageOld");
    },
  },
};
</script>
<style>
.portal .box {
  width: 1200px;
  margin: 50px auto;
  /* border: 1px solid #999999; */
  text-align: center;
}
.portal .box .title {
  margin: 50px 0 10px;
  font-size: 20px;
  font-weight: bold;
  color: #333333;
}
.portal .box .sub-title {
  margin-bottom: 30px;
  font-size: 14px;
  color: #666666;
}
.portal .box .content {
  display: flex;
  justify-content: space-around;
}
.portal .box .content-item {
  width: 250px;
  padding: 30px;
  box-shadow: 0 0 6px #ececec;
}
.portal .box .content-item img {
  width: 180px;
  height: 180px;
}
.portal .box .content-item .desc {
  margin-bottom: 8px;
  font-size: 14px;
  color: #333333;
}
.portal .box .content-item .sub-desc {
  font-size: 12px;
  color: #666666;
}
.portal .box .btn {
  margin: 20px;
}
.portal .box .el-button {
  color: #5d626c;
  border-color: #5d626c;
}
.portal .box .el-button.is-plain:focus,
.portal .box .el-button.is-plain:hover {
  color: #409eff;
  border-color: #409eff;
}
</style>