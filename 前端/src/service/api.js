import request from "./request.js";
import qs from "qs";

// 登录接口
export async function giteeLogin(params) {
  let temp = qs.stringify(params);
  return request({
    url: "/api/git/login?" + temp,
    method: "GET",
    data: {},
  });
}

// 测试用的登录接口
export async function normalLogin(params) {
  let temp = qs.stringify(params);
  return request({
    url: "/api/git/common/login?" + temp,
    method: "GET",
    data: {},
  });
}

// 执行sql语句
export async function executeSql(params) {
  return request({
    url: "/api/actuator/code",
    method: "POST",
    data: params,
  });
}

// 保存sql语句进入仓库
export async function saveSql(params) {
  return request({
    url: "/api/codehub/saveorupdate",
    method: "POST",
    data: params,
  });
}

// 根据id删除指定的代码仓库
export async function deleteSql(params) {
  let temp = qs.stringify(params);
  return request({
    url: "/api/codehub/delete?" + temp,
    method: "POST",
    data: {},
  });
}

// 查询代码仓库仓库
export async function queryCodeHubPage(params) {
  let temp = qs.stringify(params);
  return request({
    url: "/api/codehub/query/git?" + temp,
    method: "GET",
    data: {},
  });
}


// 提交使用管理员权限的申请
export async function applyManager(params) {
  return request({
    url: "/api/user/apply",
    method: "POST",
    data: params,
  });
}

// 被驳回后重新申请使用管理员权限的申请
export async function reapplyManager(params) {
  let temp = qs.stringify(params);
  return request({
    url: "/api/user/reApply?" + temp,
    method: "POST",
    data: {},
  });
}

// 管理员查询待审核的申请
export async function applyList(params) {
  let temp = qs.stringify(params);
  return request({
    url: "/api/user/apply/list?" + temp,
    method: "GET",
    data: {},
  });
}

// 权限申请审批
export async function applyApproval(params) {
  let temp = qs.stringify(params);
  return request({
    url: "/api/user/approval",
    method: "POST",
    data: params,
  });
}

// 查询我的申请状态
export async function queryMyApply(params) {
  return request({
    url: "/api/user/apply/" + params,
    method: "GET",
    data: {},
  });
}

// 智能客服
export async function questionQuery(params) {
  let temp = qs.stringify(params);
  return request({
    url: "/api/robot/chat?" + temp,
    method: "GET",
    data: {},
  });
}

// 数据库查询
export async function queryDatabases(params) {
  return request({
    url: "/api/infos/dats",
    method: "POST",
    data: params,
  });
}

// 创建docker
export async function createDocker(params) {
  let temp = qs.stringify(params);
  return request({
    url: "/api/user/docker/create?" + temp,
    method: "GET",
    data: params,
  });
}

// 删除docker
export async function deleteDocker(params) {
  let temp = qs.stringify(params);
  return request({
    url: "/api/user/docker/del?" + temp,
    method: "GET",
    data: params,
  });
}

// 截止时间
export async function cutOffTime(params) {
  let temp = qs.stringify(params);
  return request({
    url: "/api/user/docker/time?" + temp,
    method: "GET",
    data: params,
  });
}