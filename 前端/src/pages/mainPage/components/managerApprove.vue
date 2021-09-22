<template>
  <div>
    <el-dialog
      title="申请管理员权限"
      :visible.sync="visible"
      width="30%"
      @close="diaClose"
    >
      <el-form ref="saveForm" :model="saveForm" label-width="80px">
        <el-form-item label="申请理由">
          <el-input
            v-model="saveForm.reason"
            type="textarea"
            :rows="4"
          ></el-input>
        </el-form-item>
        <el-form-item
          label="驳回原因"
          v-if="applyObject && applyObject.status === '2'"
        >
          <el-input
            v-model="applyObject.rejectReason"
            type="textarea"
            readonly=""
            :rows="4"
          ></el-input>
        </el-form-item>
      </el-form>
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
import { applyManager, reapplyManager } from "@/service/api.js";
import { Message } from "element-ui";
import { mapGetters } from "vuex";

export default {
  setup() {},
  data() {
    return {
      saveForm: {
        reason: "",
      },
      visible: false,
    };
  },
  props: ["dialogVisible", "applyObject"],
  watch: {
    dialogVisible: {
      immediate: true,
      handler(val) {
        this.visible = val;
      },
    },
  },
  computed: {
    ...mapGetters(["currentUser"]),
  },
  methods: {
    diaClose() {
      this.$emit("diaClose");
    },
    saveSubmit() {
      this.$emit("diaClose");
      const params = {
        applyReason: this.saveForm.reason,
        gitId: this.currentUser.id,
        gitName: this.currentUser.name,
      };
      if (this.applyObject && this.applyObject.id !== "") {
        params.id = this.applyObject.id;
        reapplyManager(params).then((res) => {
          if (res.code === 200) {
            Message.success("操作成功");
            this.$emit("doApplyCompleted");
          } else {
            Message.error(res.message || "系统异常");
          }
        });
      } else {
        applyManager(params).then((res) => {
          if (res.code === 200) {
            Message.success("操作成功");
            this.$emit("doApplyCompleted");
          } else {
            Message.error(res.message || "系统异常");
          }
        });
      }
    },
  },
};
</script>
