<template>
  <div>
    <div class="giao-rows justify-between">
      <div class="margin-bottom-xs">
        <el-button
          type="primary"
          icon="el-icon-video-play"
          @click="doExecuteSql"
          >运行</el-button
        >
        <el-button
          type="success"
          icon="el-icon-document-checked"
          @click="doSaveSql"
          >保存</el-button
        >
        <el-button
          v-if="isImportSql"
          type="success"
          icon="el-icon-document-checked"
          @click="doSaveAsSql"
          >另存为</el-button
        >
        <el-button type="danger" icon="el-icon-magic-stick" @click="formatSql"
          >格式化</el-button
        >
        <!-- <el-button type="info" icon="el-icon-search">代码检查</el-button> -->
        <el-button icon="el-icon-delete" @click="cleanSqlContent"
          >清空内容</el-button
        >
      </div>

      <div>
        <slot name="butRight"></slot>
        <!-- <el-button type="warning">申请管理员</el-button> -->
      </div>
    </div>
    <div v-if="isImportSql">当前代码仓库标题：{{ codeHubTitle }}</div>
    <div id="editor" ref="editor" class="monaco-editor" onblur="editorBlur" />
  </div>
</template>

<script>
// 引入全局实例
import * as monaco from "monaco-editor/esm/vs/editor/editor.api.js";
import { format } from "sql-formatter";

// 尝试获取全局实例

export default {
  name: "sqlEditor",
  components: {},
  props: {
    // 外部传入的内容，用于实现双向绑定
    codeText: String,
    // 外部传入的语法类型
    language: {
      type: String,
      default: "sql",
    },
    isImportSql: {
      type: Boolean,
      default: false,
    },
    codeHubTitle: String,
  },
  data() {
    return {
      // 内部真实的内容
      code: "",
      // 编辑器实例
      editor: null,
    };
  },
  watch: {
    codeText: {
      immediate: true,
      handler(newVal, oldVal) {
        if (this.editor && newVal != oldVal) {
          this.editor.setValue(newVal.toString());
        }
      },
    },
  },
  mounted() {
    // 初始化
    this.initEditor();
  },
  methods: {
    editorBlur() {
      console.log("editorBlur");
    },
    cleanSqlContent() {
      this.editor.setValue("");
      this.$emit("cleanCllick");
      
    },
    getSql() {
      let sql = this.editor.getValue() || "";
      sql = sql.trim();
      if (sql === "") {
        Message.error("请输入sql语句");
      }
      return sql;
    },
    doSaveAsSql() {
      const sql = this.getSql();
      if (sql === "") {
        return;
      }
      this.$emit("saveAsClick", sql);
    },
    doSaveSql() {
      const sql = this.getSql();
      if (sql === "") {
        return;
      }
      this.$emit("saveClick", sql);
    },
    doExecuteSql() {
      const sql = this.getSql();
      if (sql === "") {
        return;
      }
      this.$emit("execClick", sql);
    },
    initEditor() {
      let that = this;
      this.editor = monaco.editor.create(that.$refs.editor, {
        value: that.codeText || that.code,
        language: "sql",
        theme: "vs-dark",
        lineNumbers: "on",
        selectOnLineNumbers: true, //显示行号
        roundedSelection: false,
        readOnly: false, // 只读
        cursorStyle: "line", //光标样式
        automaticLayout: true, //自动布局
        glyphMargin: false, //字形边缘
        useTabStops: false,
        fontSize: 14, //字体大小
        autoIndent: true, //自动布局
        quickSuggestionsDelay: 500, //代码提示延时
        minimap: {
          enabled: false, // 不要小地图
        },
      });

      // that.editor.onDidChangeModelContent((event) => {
      //   const sql = that.getSql();
      //   if (sql === "") {
      //     return;
      //   }
      //   that.$emit("sqlChange", sql);
      // });

      //编辑器随窗口自适应
      window.addEventListener("resize", function() {
        that.editor.layout();
      });

      this.codeSuggestion();
    },
    codeSuggestion() {
      monaco.languages.registerCompletionItemProvider("sql", {
        provideCompletionItems: function(model, position) {
          // 获取当前行数
          const line = position.lineNumber;

          // 获取当前列数
          const column = position.column;
          // 获取当前输入行的所有内容
          const content = model.getLineContent(line);
          // 通过下标来获取当前光标后一个内容，即为刚输入的内容
          const sym = content[column - 2];
          var textUntilPosition = model.getValueInRange({
            startLineNumber: 1,
            startColumn: 1,
            endLineNumber: position.lineNumber,
            endColumn: position.column,
          });
          var word = model.getWordUntilPosition(position);
          var range = {
            startLineNumber: position.lineNumber,
            endLineNumber: position.lineNumber,
            startColumn: word.startColumn,
            endColumn: word.endColumn,
          };
          //---------------------------------------------------
          //上面的代码仅仅是为了获取sym，即提示符
          //---------------------------------------------------
          var suggestions = [];
          if (sym == "$") {
            //...
            //拦截到用户输入$，开始设置提示内容，同else中代码一致，自行拓展
          } else {
            //直接提示，以下为sql语句关键词提示
            var sqlStr = [
              "SELECT",
              "FROM",
              "WHERE",
              "AND",
              "OR",
              "LIMIT",
              "ORDER BY",
              "GROUP BY",
              "DROP",
              "LIKE"
            ];
            for (var i in sqlStr) {
              suggestions.push({
                label: sqlStr[i], // 显示的提示内容
                kind: monaco.languages.CompletionItemKind["Function"], // 用来显示提示内容后的不同的图标
                insertText: sqlStr[i], // 选择后粘贴到编辑器中的文字
                detail: "", // 提示内容后的说明
                range: range,
              });
            }
          }
          return {
            suggestions: suggestions,
          };
        },
        triggerCharacters: ["$", ""],
      });
    },
    formatSql() {
      let sqlContent = this.editor.getValue();
      this.editor.setValue(format(sqlContent));
    },
  },
};
</script>

<style lang="css" scoped>
.monaco-editor {
  /* width: 600px; */
  height: 700px;
  margin-top: 5px;
}
.CodeMirror {
  border: 1px solid black;
  height: 800px;
}

.CodeMirror-scroll {
  height: 800px;
  overflow-y: hidden;
  overflow-x: auto;
}
</style>
