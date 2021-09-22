/**
 * 全局配置文件
 */
 export default {
    title: "openGauss体验系统",
    logo: "S",
    indexTitle: 'openGauss体验系统',
    
    tokenHeader: 'Blade-Auth',
    //http的status默认放行列表
    statusWhiteList: [],
    //配置首页不可关闭
    isFirstPage: false,
    fistPage: {
      label: "首页",
      value: "/index",
      params: {},
      query: {},
      meta: {
        i18n: 'dashboard'
      },
      group: [],
      close: false
    },
    
  }
  