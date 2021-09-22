"use strict";
const MonacoEditorWebpackPlugin = require("monaco-editor-webpack-plugin");
const BundleAnalyzerPlugin = require('webpack-bundle-analyzer').BundleAnalyzerPlugin;
const CompressionPlugin = require('compression-webpack-plugin');
const productionGzipExtensions = /\.(js|css|json|txt|html|ico|svg)(\?.*)?$/i;

const path = require("path");

function resolve(dir) {
  return path.join(__dirname, "..", dir);
}

// 配置通过CDN引入的js/css
const cdn = {
  externals: {
    'vue': 'Vue',
    'element-ui': 'ELEMENT'
  },
  js: [
    'https://cdn.bootcss.com/vue/2.5.2/vue.min.js',
    'https://cdn.bootcss.com/element-ui/2.12.0/index.js'
  ],
  css: [
    'https://cdn.bootcdn.net/ajax/libs/element-ui/2.12.0/theme-chalk/index.css'
  ]
}


module.exports = {
  pages: {//配置多页面入口        
    login: {          
      entry: 'src/login/login.main.js',          
      template: 'public/login.html',        
    },        
    index: {          
      entry: 'src/main.js',          
      template: 'public/index.html',        
    },    
  },

  configureWebpack: {
    plugins: [new MonacoEditorWebpackPlugin(),
      new BundleAnalyzerPlugin(
          
       ),
       new CompressionPlugin({
        algorithm: 'gzip',
        test: productionGzipExtensions,
        threshold: 10240,
        minRatio: 0.8,
        deleteOriginalAssets: false
    })
],
    // 表示不需要webpack打包的文件 
  //  externals: process.env.NODE_ENV !== 'development' ? cdn.externals : {}
  },

   

  //开发模式反向代理配置，生产模式请使用Nginx部署并配置反向代理
  lintOnSave: false,
  productionSourceMap: false,
  
  // css: {
  //   requireModuleExtension: true, // 是否开启支持‘foo.module.css’样式
  //   extract: true, // 是否使用css分离插件 ExtractTextPlugin 抽离css
  //   sourceMap: process.env.NODE_ENV !== 'production' // 是否在构建样式地图，false将提高构建速度
  // },
  devServer: {
    port: 3000,
    proxy: {
      "/api": {
        //本地服务接口地址
        target: process.env.VUE_APP_BASE_URL,
        ws: true,
        pathRewrite: {
          "^/api": "/",
        },
      },
    },
  },
};
