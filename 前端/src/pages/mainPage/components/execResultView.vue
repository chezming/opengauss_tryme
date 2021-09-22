<template>
  <div class="sql-result">
    <div v-if="!dataList || dataList.length === 0">
      {{ statusText }}
    </div>
    <el-card
      v-else
      class="box-card"
      v-for="(item, index) in dataList"
      :key="index"
      shadow="never"
    >
      <template #header>
        <div class="card-header">
          <span>语句{{ index + 1 }}</span>
        </div>
      </template>
      <div>
        <div class="font-xs text-gray">{{ item.code }}</div>
        <div v-if="item.type && item.type.toUpperCase() === 'SELECT'">
          <el-table :data="item.table" style="width: 100%" v-if="item.success">
            <el-table-column
              v-for="(col, idx) in calcTableColumn(item.table)"
              :key="idx"
              :prop="col.prop"
              :label="col.label"
              width="180"
            >
            </el-table-column>
          </el-table>
          <div v-else>
            <div class="font-xs text-gray">
              {{ item.result | errorTextFilter }}
            </div>
          </div>
          <div class="font-xs text-gray">执行时间：{{ item.time }}</div>
        </div>
        <div v-else>
          <div class="font-xs text-gray">{{ item.result }}</div>
          <div class="font-xs text-gray">执行时间：{{ item.time }}</div>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: "execResultView",
  setup() {},
  props: ["dataList"],
  data() {
    return {
      statusText: "请在输入框输入sql，点击运行，即可查看运行结果",
    };
  },
  filters: {
    errorTextFilter: function (v) {
      if (!v) {
        return "-";
      }
      const idx = v.indexOf("]");
      return v.substring(idx + 1);
    },
  },
  methods: {
    calcTableColumn(rows) {
      let list = [];
      if (!rows || rows.length === 0) {
        return list;
      }
      for (let key in rows[0]) {
        const item = {
          prop: key,
          label: key,
        };
        list.push(item);
      }
      return list;
    },
  },
};
</script>
<style>
.sql-result {
  min-height: 180px;
  border: 1px solid #dddddd;
  border-top: 0;
  border-bottom-left-radius: 4px;
  border-bottom-right-radius: 4px;
  padding: 10px;
}
</style>
