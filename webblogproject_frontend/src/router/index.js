import Vue from "vue";
import VueRouter from "vue-router";

//기본 페이지
import Home from "@/components/Home";
import Error from "@/components/404";

//포스팅
import Search from "@/components/article/Search";
import Category from "@/components/article/Category";
import Article from "@/components/article/Article";
import TempArticle from "@/components/article/TempArticle";
import TempList from "@/components/article/TempList";
import Post from "@/components/article/Post";
import Update from "@/components/article/Update";
import MDEditor from "@/components/article/MDEditor";
import CKEditor from "@/components/article/CKEditor";
import Card from "@/components/article/Card";

//사용자
import SNSLogin from "@/components/user/SNSLogin";
import Auth from "@/components/user/Auth";
import Info from "@/components/user/Info";
import Profile from "@/components/user/Profile";
import UserUpdate from "@/components/user/Update";
import Email from "@/components/user/Email";
import SNSRegist from "@/components/user/SNSRegist";
import SNSCombine from "@/components/user/SNSCombine";

//기타
import Survey from "@/components/Survey";

const originalPush = VueRouter.prototype.push;
VueRouter.prototype.push = function push(location) {
  return originalPush.call(this, location).catch((err) => err);
};

Vue.use(VueRouter);

const routes = [
  //기본
  {
    path: "/",
    name: "Home",
    component: Home,
  },

  {
    path: "/*",
    name: "Error",
    component: Error,
  },

  //포스팅
  {
    path: "/search/:type/:word",
    name: "Search",
    props: true,
    component: Search,
  },
  {
    path: "/category/:category",
    name: "Category",
    props: true,
    component: Category,
  },

  {
    path: "/article/detail/:articleId",
    name: "Article",
    component: Article,
    props: true,
  },

  {
    path: "/article/temp/:articleId",
    name: "TempArticle",
    component: TempArticle,
  },

  {
    path: "/article/temp",
    name: "TempList",
    component: TempList,
  },
  {
    path: "/article/post",
    name: "Post",
    component: Post,
  },

  {
    path: "/article/update",
    name: "Update",
    component: Update,
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
    path: "/card",
    name: "Card",
    component: Card,
  },

  //사용자
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
    path: "/user/info",
    name: "info",
    component: Info,
  },
  {
    path: "/user/update",
    name: "update",
    component: UserUpdate,
  },

  {
    path: "/user/email",
    name: "email",
    component: Email,
  },
  {
    path: "/user/snsRegist",
    name: "snsRegist",
    component: SNSRegist,
  },
  {
    path: "/user/snsCombine",
    name: "snsCombine",
    component: SNSCombine,
  },

  {
    path: "/user/profile/:id",
    name: "Profile",
    props: true,
    component: Profile,
  },

  //기타
  {
    path: "/survey",
    name: "Survey",
    component: Survey,
  },
];

const router = new VueRouter({
  mode: "history",
  base: process.env.BASE_URL,
  routes,
});

export default router;
