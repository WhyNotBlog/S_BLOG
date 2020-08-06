import Vue from "vue";
import VueRouter from "vue-router";
import Home from "@/components/Home";
import Search from "@/components/article/Search";
import Article from "@/components/article/Article";
import TempArticle from "@/components/article/TempArticle";
import TempList from "@/components/article/TempList";
import Post from "@/components/article/Post";
import Update from "@/components/article/Update";
import MDEditor from "@/components/article/MDEditor";
import CKEditor from "@/components/article/CKEditor";
import SNSLogin from "@/components/user/SNSLogin";
import Auth from "@/components/user/Auth";
import Info from "@/components/user/Info";
import UserUpdate from "@/components/user/Update";
import Email from "@/components/user/Email";
import SNSRegist from "@/components/user/SNSRegist";
import SNSCombine from "@/components/user/SNSCombine";
import Test from "@/components/article/test";

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

Vue.use(VueRouter);

const routes = [
  {
    path: "/test",
    name: "Test",
    component: Test,
  },
  {
    path: "/",
    name: "Home",
    component: Home,
  },
  {
    path: "/search/:tagKey",
    name: "Search",
    props: true,
    component: Search,
  },
  {
    path: "/article/update",
    name: "Update",
    component: Update,
    props: true,
  },
  {
    path: "/article/post",
    name: "Post",
    component: Post,
  },
  {
    path: "/article/detail/:articleId",
    name: "Article",
    component: Article,
    props: true,
  },
  {
    path: "/article/temp/",
    name: "TempList",
    component: TempList,
  },
  {
    path: "/article/temp/:articleId",
    name: "TempArticle",
    component: TempArticle,
  },
  {
    path: "/article/editor",
    name: "MDEditor",
    component: MDEditor,
  },
  {
    path: "/article/ckeditor",
    name: "CKEditor",
    component: CKEditor,
  },
  {
    path: "/user/snsLogin",
    name: "SNSLogin",
    component: SNSLogin,
  },
  {
    path: "/user/auth/:signupid/:token",
    name: "Auth",
    props: true,
    component: Auth,
  },
  {
    path: "/user/update",
    name: "update",
    component: UserUpdate,
  },
  {
    path: "/user/info",
    name: "info",
    component: Info,
  },
  {
    path: "/user/email",
    name: "email",
    component: Email,
  },
  {
    path: "/user/snsCombine",
    name: "snsCombine",
    component: SNSCombine,
  },
  {
    path: "/user/snsRegist",
    name: "snsRegist",
    component: SNSRegist,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
