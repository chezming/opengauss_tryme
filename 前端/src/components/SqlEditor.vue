<template>
  <div class="in-coder-panel">
    <button @click="clickBtn"  >click</button>
    <div id="editor" ref="editor" class="monaco-editor" />
  </div>
</template>

<script>
// 引入全局实例
import * as monaco from "monaco-editor";
// import sqlFormatter from "sql-formatter";

// 尝试获取全局实例

export default {
  name: "SqlEditor",
  components: {},
  props: {
    // 外部传入的内容，用于实现双向绑定
    codeText: String,
    // 外部传入的语法类型
    language: {
      type: String,
      default: "sql",
    },
    // 主要配置
    editorOptions: {
      type: Object,
      default: function() {
        return {
          selectOnLineNumbers: true,
          roundedSelection: false,
          cursorStyle: "line", // 光标样式
          automaticLayout: false, // 自动布局
          glyphMargin: true, // 字形边缘
          useTabStops: false,
          fontSize: 28, // 字体大小
          autoIndent: false, // 自动布局
        };
      },
    },
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
        console.log("set value", newVal, oldVal, this.editor, newVal !== oldVal);
        
          if (this.editor && newVal !== oldVal) {
            // this.editor.setValue(newVal.toString());
            // this.code = newVal
            // this.initEditor()
          }
        
      },
    },
  },
  mounted() {
    // 初始化
    this.initEditor();
  },
  methods: {
    clickBtn(){console.log(this.editor)
      const v = this.editor.getValue()
      console.log(v)
      this.editor.setValue("aaaaa")
    },
    initEditor() {
      let that = this;
      this.editor = monaco.editor.create(that.$refs.editor, {
        value: that.codeText || that.code,
        language: "sql",
        theme: "vs-dark",
        selectOnLineNumbers: true, //显示行号
        roundedSelection: false,
        readOnly: false, // 只读
        cursorStyle: "line", //光标样式
        automaticLayout: true, //自动布局
        glyphMargin: true, //字形边缘
        useTabStops: false,
        fontSize: 14, //字体大小
        autoIndent: true, //自动布局
        quickSuggestionsDelay: 500, //代码提示延时
        minimap: {
          enabled: false, // 不要小地图
        },
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
        //   var textUntilPosition = model.getValueInRange({
        //     startLineNumber: 1,
        //     startColumn: 1,
        //     endLineNumber: position.lineNumber,
        //     endColumn: position.column,
        //   });
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
    formatterSql(){
    //   let sqlContent = this.editor.getValue()
    //   this.editor.setValue(sqlFormatter.format(sqlContent))
    },
  },
};
</script>

<style lang="css" scoped>
.monaco-editor {
  /* width: 600px; */
  height: 400px;
}
</style>
