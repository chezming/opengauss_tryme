<template>
  <div>
    <div class="giao-rows justify-between">
      <div class="giao-margin-bottom-xs">
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

    <div id="editor" ref="editor" class="monaco-editor" >
  </div>
   
                   
                   </div>
</template>

<script>
// 引入全局实例
// import * as monaco from "monaco-editor";
import { format } from 'sql-formatter';
import * as monaco from 'monaco-editor/esm/vs/editor/editor.main.js'
import { language as mysqlLanguage } from 'monaco-editor/esm/vs/basic-languages/mysql/mysql.js';

monaco.languages.registerCompletionItemProvider('mysql', {
    provideCompletionItems: function(model, position) {
        // get editor content before the pointer
        var textUntilPosition = model.getValueInRange({
            startLineNumber: position.lineNumber,
            startColumn: 1,
            endLineNumber: position.lineNumber,
            endColumn: position.column
        });
        var match = textUntilPosition.match(/(\S+)$/);
        if (!match) return [];
        match = match[0].toUpperCase();
        var suggestions = [];
        mysqlLanguage.keywords.forEach(item => {
            if (item.indexOf(match) !== -1) {
                suggestions.push({
                    label: item,
                    kind: monaco.languages.CompletionItemKind.Keyword,
                    insertText: item
                });
            }
        });
        mysqlLanguage.operators.forEach(item => {
            if (item.indexOf(match) !== -1) {
                suggestions.push({
                    label: item,
                    kind: monaco.languages.CompletionItemKind.Operator,
                    insertText: item
                });
            }
        });
        mysqlLanguage.builtinFunctions.forEach(item => {
            if (item.indexOf(match) !== -1) {
                suggestions.push({
                    label: item,
                    kind: monaco.languages.CompletionItemKind.Function,
                    insertText: item
                });
            }
        });
        return {
            suggestions
        };
    }
});

export default {
  name: "sqlEditor",
  components: { },
  props: {
    // 外部传入的内容，用于实现双向绑定
    codeText: String,
    // 外部传入的语法类型
    language: {
      type: String,
      default: "sql",
    },
    
  },
  data() {
    return {
      // 内部真实的内容
      code: "",
      // 编辑器实例
      editor: null,
    

 

       
      dialogTableVisible:false,
      operateFlag: '',
      formLabelWidth:'120px' ,//diaglog宽度
      value:'',//选择框的值
   
    };
  },
  watch: {
    codeText: {
      immediate: true,
      handler(newVal, oldVal) {
        if (this.editor && newVal !== oldVal) {
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
    cleanSqlContent() {
      this.editor.setValue("");
    },
    getSql() {
      let sql = this.editor.getValue() || "";
      sql = sql.trim();
      if (sql === "") {
        Message.error("请输入sql语句");
      }
      return sql;
    },
    doSaveSql() {
      const sql = this.getSql(); console.log('doSaveSql', sql)
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
         automaticLayout: true,
      });

      //编辑器随窗口自适应
      window.addEventListener("resize", function () {
        that.editor.layout()
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
