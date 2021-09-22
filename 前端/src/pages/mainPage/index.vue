<template>
  <div class="container">
    <el-container>
      <el-header>
        <div class="bg-blue info">{{ currentUser.name }}，欢迎使用！</div>
        <div class="bg-blue w120">
          <el-button
            type="text"
            icon="el-icon-question"
            @click="tipsVisible = true"
            style="color: #ffffff"
            >操作说明</el-button
          >
        </div>
        <div class="bg-blue w120 ml-4 time">{{ time }}</div>
        <div class="bg-blue w120 ml-4">
          <el-button class="custom-btn" @click="onDelay">延时</el-button>
        </div>
        <div class="bg-blue w120 ml-4">
          <el-button class="custom-btn" @click="onEnd">结束</el-button>
        </div>
      </el-header>
      <el-container>
        <el-aside width="300px">
          <el-tabs v-model="activeName1" type="card" class="left-tabs">
            <el-tab-pane label="数据库" name="databases">
              <div class="tree-wrapper">
                <div class="refresh-btn">
                  <el-button @click="onRefresh">刷新</el-button>
                </div>

                <el-tree
                  ref="repoTree"
                  lazy
                  node-key="id"
                  :props="repoProps"
                  :load="loadRepoNode"
                >
                  <span class="custom-tree-node" slot-scope="{ node, data }">
                    <img
                      v-if="node.level === 1"
                      class="tree-icon"
                      :src="require('../../assets/img/repo.png')"
                      alt=""
                    />
                    <span
                      style="
                        display: inline-block;
                        width: 120px;
                        overflow: hidden;
                        text-overflow: ellipsis;
                        vertical-align: middle;
                      "
                      >{{ data.title
                      }}<i
                        v-if="node.level === 1"
                        @click.stop="refreshRepoNode(node)"
                        style="padding: 2px 4px; color: #0e52b6"
                        class="el-icon-refresh"
                      ></i
                    ></span>
                    <span v-if="node.level === 2">
                      <el-button-group class="giao-margin-right-xs">
                        <el-button type="text" @click="importSQL(data)"
                          >导入</el-button
                        >

                        <el-popconfirm
                          title="确定删除吗？"
                          @confirm="deleteSQL(data)"
                        >
                          <el-button type="text" slot="reference"
                            >删除</el-button
                          ></el-popconfirm
                        >

                        <el-popover
                          placement="left"
                          :width="400"
                          trigger="hover"
                          :content="data.sql"
                        >
                          <template #reference>
                            <el-button type="text">预览</el-button>
                          </template>
                        </el-popover>
                      </el-button-group>
                    </span>
                  </span>
                </el-tree>

                <el-tree
                  ref="databaseTree"
                  node-key="id"
                  lazy
                  :props="defaultProps"
                  :load="loadDatabasesNode"
                >
                  <span class="custom-tree-node" slot-scope="{ node }">
                    <img
                      v-if="node.level < 4"
                      class="tree-icon"
                      :src="
                        node.level > 2
                          ? getIconSrc(node)
                          : iconArray[node.level - 1]
                      "
                      alt=""
                    />
                    <span
                      >{{ node.label }}
                      <i
                        v-if="node.level < 4"
                        @click.stop="refreshDatabasesNode(node)"
                        style="padding: 2px 4px; color: #0e52b6"
                        class="el-icon-refresh"
                      ></i>
                    </span>
                  </span>
                </el-tree>
              </div>
            </el-tab-pane>
            <el-tab-pane label="智能客服" name="intelligentCustomer">
              <div class="chat">
                <template v-for="(item, index) in chatList">
                  <div :key="index">
                    <div v-if="item.zn" class="zn">
                      <img
                        src="../../assets/img/zn.png"
                        style="width: 30px; height: 30px"
                        alt=""
                      />
                      <div v-if="item.zn.chat_answers" class="pop-wrapper">
                        <div class="pop"></div>
                        <div class="pop-content">
                          {{ item.zn.chat_answers.answer }}
                        </div>
                      </div>
                      <template v-if="item.zn.qabot_answers">
                        <div
                          v-if="
                            item.zn.qabot_answers.answers &&
                            item.zn.qabot_answers.answers.length
                          "
                          class="pop-wrapper"
                        >
                          <div class="pop"></div>
                          <div class="pop-content">
                            <div
                              v-for="(v, i) in item.zn.qabot_answers.answers"
                              :key="i"
                            >
                              <div
                                class="title"
                                style="color: #0e52b6; text-align: center"
                              >
                                {{ v.st_question }}
                              </div>
                              <div class="content" style="font-size: 14px">
                                {{ v.answer }}
                              </div>
                            </div>
                          </div>
                        </div>
                        <div
                          v-if="
                            item.zn.qabot_answers.recommend_answers &&
                            item.zn.qabot_answers.recommend_answers.length
                          "
                          class="pop-wrapper"
                        >
                          <div class="pop"></div>
                          <div class="pop-content">
                            <div>{{ recommendText }}</div>
                            <div
                              v-for="(v, i) in item.zn.qabot_answers
                                .recommend_answers"
                              :key="i"
                              style="
                                color: #0e52b6;
                                cursor: pointer;
                                font-size: 14px;
                              "
                              @click="handleRecommend(v.st_question)"
                            >
                              {{ v.st_question }}
                            </div>
                          </div>
                        </div>
                      </template>
                    </div>
                    <div v-if="item.me" class="me">
                      <div class="pop-wrapper">
                        <div class="pop"></div>
                        <div class="pop-content">{{ item.me }}</div>
                      </div>
                      <img
                        src="../../assets/img/me.png"
                        style="width: 30px; height: 30px"
                        alt=""
                      />
                    </div>
                  </div>
                </template>
              </div>
              <div class="chat-input">
                <el-input
                  v-model="question"
                  type="textarea"
                  resize="none"
                  :min="2"
                  placeholder="请输入问题"
                ></el-input>
                <el-button
                  slot="append"
                  :disabled="!question"
                  @click="sendQuestion"
                  >发送</el-button
                >
              </div>
            </el-tab-pane>
          </el-tabs>
        </el-aside>
        <el-main>
          <el-container class="sql-wrapper">
            <sql-editor
              @execClick="doExecuteSql"
              @saveClick="doSaveSql"
              @saveAsClick="doSaveAsSql"
              @cleanCllick="doCleanSql"
              :code-text="codeText"
              :is-import-sql="isImport"
              :code-hub-title="codeHubTitle"
              :dbNameList="dbNameList"
            >
              <!-- <template v-slot:butRight>
                申请管理员权限：<el-button
                  type="warning"
                  :disabled="!canApply"
                  @click="applyBtnClick"
                  >{{ applyBtnName }}</el-button
                >
              </template> -->
            </sql-editor>

            <!-- 运行结果 -->
            <el-tabs v-model="activeName" type="card">
              <el-tab-pane label="运行结果" name="first">
                <exec-result-view :dataList="executeResult" />
              </el-tab-pane>
            </el-tabs>
          </el-container>
        </el-main>
      </el-container>
    </el-container>

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
    <el-dialog title="操作说明" :visible.sync="tipsVisible" width="50%">
      <div>
        <p>1.本系统基于OpenGauss 2.0.1</p>
        <p>2.可直接输入sql，点击“运行”按钮，查看运行结果</p>
        <p>3.每行请使用英文的逗号分隔表示结束。</p>
        <p>4.使用“-- ”来进行代码注释</p>
      </div>
      <template #footer>
        <span class="dialog-footer">
          <el-button type="primary" @click="tipsVisible = false"
            >关 闭</el-button
          >
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
import SqlEditor from "./components/sqlEditor.vue";
import {
  executeSql,
  saveSql,
  queryCodeHubPage,
  deleteSql,
  queryMyApply,
  questionQuery,
  queryDatabases,
  createDocker,
  cutOffTime,
  deleteDocker,
} from "@/service/api.js";

import execResultView from "./components/execResultView.vue";
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
      root_node1: "", // 代码仓库根节点
      root_node2: "", // 代码仓库一级节点
      root_node: "", // 数据库根节点
      root_resolve: "",
      timer: null,
      time: "",
      hour: "",
      min: "",
      sec: "",
      cutOffTime: "",
      question: "",
      recommendText: "您好，为您推荐以下问题",
      chatList: [
        {
          zn: {
            qabot_answers: {
              recommend_answers: [
                {
                  st_question: "如何创建数据库？",
                },
                {
                  st_question: "如何删除数据库？",
                },
              ],
            },
          },
          me: "",
        },
      ],
      activeName1: "databases",
      activeName2: "first",
      iconArray: [
        require("../../assets/img/root.png"),
        require("../../assets/img/module.png"),
      ],
      dbNameList: [],
      currentDataBaseName: "",
      moduleChild: [
        {
          label: "表",
          icon: "table.png",
          children: [],
        },
        {
          label: "视图",
          icon: "view.png",
          children: [],
        },
        {
          label: "函数",
          icon: "function.png",
          children: [],
        },
      ],
      defaultProps: {
        children: "children",
        label: "label",
        isLeaf: "leaf",
      },
      repoProps: {
        children: "children",
        label: "title",
        isLeaf: "leaf",
      },
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
      applyStatusDict: { 0: "审核中", 1: "通过", 2: "驳回" },
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
    this.getMyApplyStatus();
    createDocker({ gitId: this.currentUser.id }).then(() => {
      cutOffTime({ gitId: this.currentUser.id }).then((res) => {
        if (res.code === 200) {
          this.cutOffTime = res.result;
          this.handleCutOffTime(res.result);
        }
      });
    });
    this.timer = setInterval(() => {
      this.handleCutOffTime(this.cutOffTime);
    }, 1000);
    this.codeText = `-- 查看openGauss版本;
select version();

--  当前库查看;
select current_database();

-- 创建数据库;
create database test11;

-- 创建表;
CREATE TABLE "lwq11" (
  "name" varchar(20) COLLATE "pg_catalog"."default" NOT NULL,
  "age" int4,
  "caree" varchar(10) COLLATE "pg_catalog"."default"
);

-- 变更表结构;
ALTER table "lwq11" add  sex  varchar(255) DEFAULT NULL;

-- 插入数据;
INSERT INTO "lwq11"("name", "age", "caree", "sex") 
VALUES ('szgx', 18, 'caree', 'F') ;

-- 选择数据;
select * from "lwq11" where name='szgx';`;
  },
  destroyed() {
    clearInterval(this.timer);
  },
  methods: {
    onRefresh() {
      const node1 = this.$refs.repoTree.getNode(this.root_node1);
      node1.loaded = false;
      node1.expand();
      const node = this.$refs.databaseTree.getNode(this.root_node);
      node.loaded = false;
      node.expand();
    },
    handleCutOffTime(cutOffTime) {
      if (!cutOffTime) {
        this.time = "00:00:00";
        return false;
      }
      const date = new Date();
      const temp = cutOffTime - date.getTime();
      if (temp <= 0) {
        this.time = "00:00:00";
        this.$message.info("试用时间到");
        clearInterval(this.timer);
        return false;
      }
      this.hour = parseInt((temp / 1000 / 60 / 60) % 24, 10);
      this.min = parseInt((temp / 1000 / 60) % 60, 10);
      this.sec = parseInt((temp / 1000) % 60, 10);
      this.time =
        (this.hour < 10 ? "0" + this.hour : this.hour) +
        ":" +
        (this.min < 10 ? "0" + this.min : this.min) +
        ":" +
        (this.sec < 10 ? "0" + this.sec : this.sec);
    },
    getIconSrc(node) {
      switch (node.label) {
        case "表":
          return require("../../assets/img/table.png");
        case "视图":
          return require("../../assets/img/view.png");
        case "函数":
          return require("../../assets/img/function.png");
        default:
          return require("../../assets/img/table.png");
      }
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
    deleteSQL(item) {
      deleteSql({ id: item.id }).then((res) => {
        if (res.code === 200) {
          Message.success("操作成功");
          this.sqlPage = 1;
          this.refreshRepoNode(this.root_node2);
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
            this.refreshRepoNode(this.root_node2);
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
          this.sqlPage = 1;
          this.refreshRepoNode(this.root_node2);
        }
      });
    },
    doExecuteSql(arg) {
      const { sql, dbName } = arg;
      const params = {
        code: sql,
        gitId: this.currentUser.id,
        dbName,
        simple: 0,
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
    onEnd() {
      const h = this.$createElement;
      this.$msgbox({
        title: "提示",
        message: h("p", null, [
          h(
            "div",
            { style: "color: #333333;font-size: 20px;text-indent:14px;" },
            [
              h("i", {
                class: "el-icon-warning",
                style: "color:#FA9841;font-size:20px",
              }),
              "您确定要结束实验？",
            ]
          ),
          h(
            "div",
            { style: "color: #999999;font-size: 14px;text-indent:28px;" },
            "退出后系统将清理体验环境，再次进入时将重新开始实验（保留代码仓库）"
          ),
        ]),
        showCancelButton: true,
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        beforeClose: (action, instance, done) => {
          if (action === "confirm") {
            deleteDocker({ gitId: this.currentUser.id }).then((res) => {
              if (res.code === 200) {
                this.$message({
                  type: "success",
                  message: "操作成功!",
                });
                this.$router.go(-1);
              }
            });
          } else {
            done();
          }
        },
      }).catch(() => {
        this.$message({
          type: "info",
          message: "取消结束",
        });
      });
    },
    onDelay() {
      const h = this.$createElement;
      this.$msgbox({
        title: "提示",
        message: h("p", null, [
          h(
            "div",
            { style: "color: #333333;font-size: 16px;text-indent:14px;" },
            [
              h("i", {
                class: "el-icon-warning",
                style: "color:#FA9841;font-size:20px",
              }),
              `距离体验结束还有${this.hour}小时${this.min}分钟，是否需要再延时180分钟？`,
            ]
          ),
        ]),
        showCancelButton: true,
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        beforeClose: (action, instance, done) => {
          if (action === "confirm") {
            // instance.confirmButtonLoading = true;
            // instance.confirmButtonText = "执行中...";
            // setTimeout(() => {
            //   done();
            //   setTimeout(() => {
            //     instance.confirmButtonLoading = false;
            //   }, 300);
            // }, 3000);
            done();
          } else {
            done();
          }
        },
      })
        .then(() => {
          this.$message({
            type: "success",
            message: "操作成功!",
          });
        })
        .catch(() => {
          this.$message({
            type: "info",
            message: "取消延迟",
          });
        });
    },
    handleRecommend(str) {
      this.question = str;
      this.sendQuestion();
    },
    sendQuestion() {
      this.chatList.push({ zn: "", me: this.question });
      const params = {
        gitId: this.currentUser.id,
        question: this.question,
      };
      questionQuery(params).then((res) => {
        const result = JSON.parse(res.result);
        this.chatList.push({ zn: result, me: "" });
      });
    },
    refreshRepoNode(arg) {
      const node = this.$refs.repoTree.getNode(arg);
      node.loaded = false;
      node.expand();
    },
    loadRepoNode(node, resolve) {
      if (node.level === 0) {
        this.root_node1 = node;
        return resolve([{ title: "代码仓库", children: [] }]);
      } else if (node.level === 1) {
        this.root_node2 = node;
      } else {
        return resolve([]);
      }
      const params = {
        gitId: this.currentUser.id,
        pageNum: 1,
        delFlag: 0,
      };
      queryCodeHubPage(params).then((res) => {
        if (res) {
          const result = res.result.data;
          result.forEach((item) => {
            item.leaf = true;
          });
          resolve(result);
        } else {
          return resolve([]);
        }
      });
    },
    refreshDatabasesNode(arg) {
      const node = this.$refs.databaseTree.getNode(arg);
      node.loaded = false;
      node.expand();
    },
    loadDatabasesNode(node, resolve) {
      const params = {
        gitId: this.currentUser.id,
        queryType: node.level,
      };
      if (node.level === 0) {
        this.root_node = node;
        this.root_resolve = resolve;
        params.queryType = 0;
      } else if (node.level === 1) {
        this.currentDataBaseName = node.label;
        params.datName = node.label;
      } else if (node.level === 2) {
        params.queryType = node.level;
        params.datName = this.currentDataBaseName;
        this.schemaName = node.label;
        return resolve(this.moduleChild);
      } else {
        if (node.label === "表") {
          params.queryType = 2;
        } else if (node.label === "视图") {
          params.queryType = 3;
        } else {
          params.queryType = 4;
        }
        params.datName = this.currentDataBaseName;
        params.schemaName = this.schemaName;
      }
      queryDatabases(params).then((res) => {
        if (res) {
          const result = res.result;
          const temp = [];
          if (Array.isArray(result)) {
            if (node.level === 0) {
              this.dbNameList = result;
            }
            result.forEach((item) => {
              if (node.level > 1) {
                temp.push({
                  label: item,
                  level: node.level,
                  leaf: true,
                });
              } else {
                temp.push({ label: item, level: node.level });
              }
            });
          }
          resolve(temp);
        } else {
          return resolve([]);
        }
      });
    },
  },
};
</script>

<style>
.chat {
  padding: 10px;
  min-height: 260px;
  height: calc(100% - 114px);
  overflow-y: auto;
}
.chat-input {
  position: absolute;
  bottom: 0;
  margin: 0 10px;
  padding-bottom: 10px;
  background-color: #ffffff;
}
.chat-input .el-button {
  margin-top: 4px;
  padding: 6px 8px;
}
.zn,
.me {
  display: flex;
  margin-bottom: 8px;
}
.me {
  justify-content: flex-end;
}
.pop-wrapper {
  display: flex;
}
.me .pop-wrapper {
  flex-direction: row-reverse;
}
.zn .pop {
  width: 0px;
  height: 0px;
  border-top: 8px solid transparent;
  border-left: 8px solid transparent;
  border-right: 8px solid #f5f5f5;
  border-bottom: 8px solid transparent;
  margin-top: 9px;
}
.zn .pop-content,
.me .pop-content {
  width: 150px;
  min-height: 24px;
  background-color: #f5f5f5;
  border-radius: 10px;
  padding: 5px 12px;
  color: #333333;
  font-size: 16px;
  word-break: break-all;
}
.me .pop-content {
  background-color: #cccccc;
}
.me .pop {
  width: 0px;
  height: 0px;
  border-top: 8px solid transparent;
  border-left: 8px solid #cccccc;
  border-right: 8px solid transparent;
  border-bottom: 8px solid transparent;
  margin-top: 9px;
}
#ctn {
  width: 100%;
  height: 50vh;
}
.el-card__body {
  padding: 10px !important;
}
.container {
  background-color: #f9f9f9;
}
.el-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 50px !important;
}
.el-header .info {
  flex: 1;
  padding-left: 10px;
  /* height: 40px;
    line-height: 40px; */
}
.el-header .time {
  font-weight: bold;
}
.el-header .w120 {
  width: 120px;
  text-align: center;
}
.el-header .ml-4 {
  margin-left: 4px;
}
.el-header .bg-blue {
  background-color: #0e52b6;
  color: #ffffff;
  height: 100%;
  line-height: 50px;
}
.el-header .bg-blue .custom-btn {
  padding: 6px 12px;
  background-color: #df3528;
  color: #ffffff;
}
.el-aside {
  height: calc(100vh - 58px);
  padding: 0 0 0 20px;
}
.left-tabs {
  margin-top: 20px !important;
  height: calc(100% - 20px);
}
.left-tabs > .el-tabs__header .el-tabs__nav {
  width: 100%;
}
.left-tabs > .el-tabs__header .el-tabs__item {
  color: #0e52b6;
  font-weight: bold;
  border-color: #0e52b6;
  background-color: #ffffff;
  width: 50%;
  height: 28px;
  line-height: 28px;
  text-align: center;
}
.left-tabs > .el-tabs__header .el-tabs__item:hover {
  background-color: #0e52b6;
  color: #ffffff;
}
.left-tabs > .el-tabs__header .el-tabs__item.is-active {
  border-bottom-color: #0e52b6;
  background-color: #0e52b6;
  color: #ffffff;
}
.left-tabs .el-tabs__content {
  height: calc(100% - 40px);
  background-color: #ffffff;
  position: relative;
}
.left-tabs .el-tabs__content .el-tab-pane {
  height: 100%;
}
.left-tabs .el-tabs__content .tree-wrapper {
  padding: 10px;
  height: calc(100% - 36px);
  overflow: auto;
}
.el-main {
  overflow: hidden;
}
.el-main .el-container {
  display: flex;
  flex-direction: column;
}
.el-tabs {
  margin-top: 10px;
}
.el-tabs--card > .el-tabs__header {
  margin-bottom: 0;
}
.el-tabs--card .el-tabs__nav {
  background-color: #ffffff;
}
.el-tabs--card .el-tabs__content {
  background-color: #ffffff;
}
.el-tree .root-icon {
  background-image: url("../../assets/img/root.png");
  background-repeat: no-repeat;
  display: inline-block;
  width: 22px;
  height: 22px;
  vertical-align: middle;
}
.el-tree .tree-icon {
  width: 20px;
  height: 20px;
  vertical-align: sub;
}
.refresh-btn .el-button {
  padding: 6px 8px;
  margin-bottom: 8px;
}
</style>
