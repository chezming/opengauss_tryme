<template>
  <div>
    <el-dialog
      title="审核申请"
      :visible.sync="visible"
      width="50%"
      @close="diaClose"
      @open="onOpen"
    >
      <el-row>
        <el-col :span="8">
          <div v-if="dataList.length === 0">
            <el-empty description="暂无数据"></el-empty>
          </div>
          <div el-else>
            <el-menu  class="el-menu-vertical-demo" @select="onMenuSelect">
              <el-menu-item :index="item.id" v-for="(item, index) in dataList"
            :key="index">
                <i class="el-icon-menu"></i>
                <span slot="title"> {{ item.gitName }}的申请</span>
              </el-menu-item>
            </el-menu>
          </div>
          <!-- <div
            v-else
            v-for="(item, index) in dataList"
            :key="index"
            @click="itemClick(item)"
          >
            {{ item.gitName }}申请
          </div> -->
        </el-col>
        <el-col :span="16">
          <div v-if="currentItem"  style="margin-left: 10px">
            <div>
              <p>{{ currentItem.gitName }}, 申请理由：</p>
              <el-input
                readonly
                type="textarea"
                :rows="2"
                v-model="currentItem.applyReason"
              />
            </div>
            <div>
              <p>审批意见：</p>
              <el-form ref="saveForm" :model="saveForm">
                <el-form-item>
                  <el-switch
                    v-model="saveForm.status"
                    active-text="通过"
                    inactive-text="驳回"
                    active-value="1"
                    inactive-value="2"
                  ></el-switch>
                </el-form-item>
                <el-form-item v-if="saveForm.status === '1'" label="有效期">
                  <el-date-picker
                    v-model="saveForm.validTime"
                    type="date"
                    value-format="yyyy-MM-dd HH:mm:ss"
                    placeholder="选择日期"
                  >
                  </el-date-picker
                ></el-form-item>
                <el-form-item v-else label="驳回原因">
                  <el-input
                    type="textarea"
                    :rows="2"
                    v-model="saveForm.rejectReason"
                  />
                </el-form-item>
              </el-form>
            </div>
          </div>
        </el-col>
      </el-row>
      <template #footer>
        <span class="dialog-footer">
          <el-button @click="visible = false">取 消</el-button>
          <el-button type="primary" @click="saveSubmit">确 定</el-button>
        </span>
      </template>
    </el-dialog>
  </div>
</template>

<script>
import { applyList, applyApproval } from "@/service/api.js";
import { mapGetters } from "vuex";
import { Message } from "element-ui";

export default {
  setup() {},
  data() {
    return {
      saveForm: {
        status: "1",
        validTime: "",
        rejectReason: "",
      },
      visible: false,
      pageInfo: {
        current: 1,
        size: 10,
      },
      dataList: [],
      total: 0,
      currentItem: null,
    };
  },
  props: ["dialogVisible"],
  watch: {
    dialogVisible: {
      immediate: true,
      handler(val) {
        this.visible = val;
      },
    },
  },
  mounted() {},
  computed: {
    ...mapGetters(["currentUser"]),
  },
  methods: {
    onMenuSelect(index, indexPath){
      const item = this.dataList.find(it=>it.id === index)
      if(item){
        this.currentItem = item
      }
    },
    onOpen() {
      this.currentItem = null
      this.loadList();
    },
    loadList(page) {
      applyList(page).then((res) => {
        if (res.code === 200) {
          this.total = res.result.total;
          this.dataList = res.result.records;
        }
      });
    },
    diaClose() {
      this.$emit("diaClose");
    },
    saveSubmit() {
      if (!this.currentItem) {
        Message.error("请选择一条记录再操作");
        return;
      }
      this.$emit("diaClose");
      const params = {
        ...this.saveForm,
        id: this.currentItem.id,
        gitId: this.currentItem.gitId,
      };
      applyApproval(params).then((res) => {
        if (res.code === 200) {
          Message.success("操作成功");
          this.$emit("doApproveCompleted");
        } else {
          Message.error(res.message || "系统异常");
        }
      });
    },
    itemClick(item) {
      this.currentItem = item;
    },
  },
};
</script>
