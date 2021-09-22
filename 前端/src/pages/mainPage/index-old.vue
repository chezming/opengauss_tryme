<template>
  <div id="ctn">
    <div class="giao-rows">
      <div style="width: 50%" class="giao-margin-right-xs">
        <div class="giao-rows justify-between giao-margin-bottom-xs">
          <div class="giao-rows align-center">
            <el-avatar
              src="https://zos.alipayobjects.com/rmsportal/ODTLcjxAfvqbxHnVXCYX.png"
            ></el-avatar>
            <!-- <i class="iconfont icon-TPbzglg text-lg"></i> -->
            <span class="giao-margin-left-xs"
              >{{ currentUser.name }}，欢迎使用！</span
            >
            <div>
              <el-link icon="el-icon-warning-outline" type="primary" @click="tipsVisible = true"
                >操作说明</el-link
              >
            </div>
          </div>

          <div class="text-blue font-sm giao-rows align-center">
            <el-image style="width: 50px; height: 50px" :src="url"></el-image
            >Try me
          </div>
        </div>

        <div>
          <sql-editor
            @execClick="doExecuteSql"
            @saveClick="doSaveSql"
            @saveAsClick="doSaveAsSql"
            @cleanCllick="doCleanSql"
            :code-text="codeText"
            :is-import-sql="isImport"
            :code-hub-title="codeHubTitle"
          >
            <template v-slot:butRight>
              申请管理员权限：<el-button
                type="warning"
                :disabled="!canApply"
                @click="applyBtnClick"
                >{{ applyBtnName }}</el-button
              >
            </template>
          </sql-editor>
        </div>
      </div>

      <div style="width: 50%;" class="giao-margin-top-xs giao-padding-top-xl">
        <el-tabs v-model="activeName" type="card">
          <el-tab-pane label="运行结果" name="first">
            <!-- <div class="font-xs text-gray">{{ executeResult }}</div> -->
            <exec-result-view
              :dataList="executeResult"
              style="height:700px;overflow:scroll"
            />
          </el-tab-pane>
          <el-tab-pane label="代码仓库" name="second">
            <el-card
              class="box-card"
              v-for="(item, key) in sqlList"
              :key="key"
              shadow="never"
            >
              <div class="giao-rows justify-between">
                <div>
                  <div class="font-lg giao-margin-bottom-xs">
                    {{ item.title }}
                  </div>
                  <div class="font-xs">发布于：{{ item.updateTime }}</div>
                </div>

                <el-button-group class="giao-margin-right-xs">
                  <el-button
                    type="text"
                    icon="el-icon-download"
                    @click="importSQL(item)"
                    >导入</el-button
                  >

                  <el-popconfirm
                    title="确定删除吗？"
                    @confirm="deleteSQL(item)"
                  >
                    <el-button
                      type="text"
                      slot="reference"
                      icon="el-icon-delete"
                      >删除</el-button
                    ></el-popconfirm
                  >

                  <el-popover
                    placement="left"
                    :width="400"
                    trigger="hover"
                    :content="item.sql"
                  >
                    <template #reference>
                      <el-button type="text" icon="el-icon-view"
                        >预览</el-button
                      >
                    </template>
                  </el-popover>
                </el-button-group>
              </div>
            </el-card>
            <div class="giao-rows justify-center giao-margin-top-xs">
              <el-pagination
                background
                layout="prev, pager, next"
                :total="sqlTotal"
                size="10"
                :current-page.sync="sqlPage"
                @current-change="onCodeHubPageChange"
              >
              </el-pagination>
            </div>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>

    <el-dialog
      title="请输入标题"
      :visible.sync="sqlTitleDialogVisible"
      width="30%"
    >
      <el-form ref="saveForm" :model="saveForm" label-width="80px">
        <el-form-item label="标题">
          <el-input v-model="saveForm.title"></el-input>
        </el-form-item>
      </el-form>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="sqlTitleDialogVisible = false">取 消</el-button>
          <el-button type="primary" @click="saveSqlSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>

    <el-dialog
      title="操作说明"
      :visible.sync="tipsVisible"
      width="50%"
    >
      <div>
        <p>1.本系统基于OpenGauss V2.2.0</p>
        <p>2.可直接输入sql，点击“运行”按钮，查看运行结果</p>
        <p>3.每行请使用英文的逗号分隔表示结束。</p>
        <p>4.使用“-- ”来进行代码注释</p>
      
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="tipsVisible = false">关 闭</el-button>
        </span>
      </template>
    </el-dialog>

    <manager-approve
      :dialog-visible="approveDialogVisible"
      :apply-object="myApply"
      @diaClose="approveDialogVisible = false"
      @doApplyCompleted="onApplyCompleted"
    />
    <approve-view
      :dialog-visible="managerDialogVisible"
      @diaClose="managerDialogVisible = false"
      @doApproveCompleted="onApproveCompleted"
    />
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import { Message } from "element-ui";
import SqlEditor from "./components/sqlEditor-old.vue";
import {
  executeSql,
  saveSql,
  queryCodeHubPage,
  deleteSql,
  queryMyApply,
} from "@/service/api.js";

import execResultView from "./components/execResultView-old.vue";
import managerApprove from "./components/managerApprove.vue";
import approveView from "./components/approveView.vue";

export default {
  components: {
    SqlEditor,
    execResultView,
    managerApprove,
    approveView,
  },
  data() {
    return {
      tipsVisible: false,
      approveDialogVisible: false,
      sqlTitleDialogVisible: false,
      managerDialogVisible: false,
      saveForm: {
        title: "",
      },

      executeResult: [],
      activeName: "first",
      codeText: "",
      currentSql: "",
      isImport: false,
      codeHubItem: null,

      dialogTableVisible: false,
      url: require("@/assets/img/picture.png"),

      ...{
        sqlList: [],
        sqlTotal: 0,
        sqlPage: 1,
      },

      myApply: null,
      applyStatusDict: { "0": "审核中", "1": "通过", "2": "驳回" },
    };
  },
  computed: {
    ...mapGetters(["currentUser"]),
    codeHubTitle() {
      return this.codeHubItem ? this.codeHubItem.title : "";
    },
    canApply() {
      return (
        (this.currentUser && this.currentUser.roleCode === "ROLE_ADMIN") ||
        !this.myApply ||
        this.myApply.status !== "0"
      );
    },
    applyBtnName() {
      let txt = "申请";
      const user = this.currentUser;
      if (user && user.roleCode === "ROLE_ADMIN") {
        return "审核";
      }
      if (!user || !this.myApply) {
        return txt;
      } else {
        if (user.roleCode === "ROLE_NORMAL") {
          // TODO 接口返回的数据还不全，没有失效时间，需要增加这个是否已经失效的判断，不然不能重新发起申请
          txt = this.applyStatusDict[this.myApply.status];
        } else {
          txt = "审核";
        }
        return txt;
      }
    },
  },
  mounted() {
    this.loadCodeHubData(this.sqlPage);
    this.getMyApplyStatus();
  },
  methods: {
    onCodeHubPageChange(val) {
      // 代码仓库分页
      this.loadCodeHubData(val);
    },
    onApproveCompleted() {
      //审核完成刷新待审核列表
    },
    onApplyCompleted() {
      // 提交申请成功，要重新查询申请状态
      this.getMyApplyStatus();
    },
    getMyApplyStatus() {
      queryMyApply(this.currentUser.id).then((res) => {
        if (res.code === 200) {
          this.myApply = res.result;
        }
      });
    },
    applyBtnClick() {
      const user = this.currentUser;
      if (!user) {
        return;
      }
      if (user.roleCode === "ROLE_ADMIN") {
        this.managerDialogVisible = true;
      } else {
        if (this.myApply && this.myApply.status === "1") {
          this.$alert(
            `您体验管理权限的申请已通过审核，有效期至${this.myApply.validTime}，感谢您的使用。`,
            "系统提示",
            {
              confirmButtonText: "确定",
            }
          );
        } else {
          this.approveDialogVisible = true;
        }
      }
    },
    loadCodeHubData(page) {
      const params = {
        gitId: this.currentUser.id,
        pageNum: page,
        delFlag: 0,
      };
      queryCodeHubPage(params).then((res) => {
        if (res.code === 200) {
          this.sqlList = res.result.data;
          this.sqlTotal = res.result.total;
        }
      });
    },
    deleteSQL(item) {
      deleteSql({ id: item.id }).then((res) => {
        if (res.code === 200) {
          Message.success("操作成功");
          this.sqlPage = 1;
          this.loadCodeHubData(this.sqlPage);
        } else {
          Message.error(res.message);
        }
      });
    },
    doSaveAsSql(sql) {
      this.sqlTitleDialogVisible = true;
      this.currentSql = sql;
    },
    doSaveSql(sql) {
      console.log("doSaveSql", this.isImport);
      if (!this.isImport) {
        this.sqlTitleDialogVisible = true;
        this.currentSql = sql;
      } else {
        let params = {
          gitId: this.currentUser.id,
          sql: sql,
          id: this.codeHubItem.id,
          title: this.codeHubItem.title,
        };
        saveSql(params).then((res) => {
          if (res.code === 200) {
            Message("保存成功");
            // 保存成功刷新列表数据
            this.loadCodeHubData(1);
          }
        });
      }
    },
    saveSqlSubmit() {
      this.sqlTitleDialogVisible = false;
      let params = {
        ...this.saveForm,
        gitId: this.currentUser.id,
        sql: this.currentSql,
      };

      saveSql(params).then((res) => {
        if (res.code === 200) {
          Message("保存成功");
          // 保存成功刷新列表数据
          this.loadCodeHubData(1);
        }
      });
    },
    doExecuteSql(sql) {
      const params = {
        code: sql,
        gitId: this.currentUser.id,
        simple: 1
      };
      executeSql(params).then((res) => {
        if (res.code === 200) {
          this.activeName = "first"; // 总是展示运行结果的tab
          this.executeResult = res.result;
        }
      });
    },
    onSqlChange(sql) {
      this.codeText = sql;
    },
    doCleanSql() {
      console.log("doCleanSql");
      this.codeText = "";
    },
    importSQL(item) {
      this.codeText = item.sql;
      this.codeHubItem = item;
      this.isImport = true;
      Message.success("导入成功");
    },
  },
};
</script>

<style>
#ctn {
  width: 100%;
  height: 50vh;
}
.el-card__body {
  padding: 10px !important;
}
</style>
