import Vue from "vue";
import Vuex from "vuex";

import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

const store = new Vuex.Store({
  plugins: [
    createPersistedState({
      storage: window.sessionStorage,
    }),
  ],
  state: {
    followingList: new Array(),
    followerList: new Array(),
    registModal: false,
    loginModal: false,
    loggedIn: null,
    userId: null,
    updateArticleId: null,
    profile: `${require("@/assets/profile.svg")}`,
    jwtAuthToken: null,

    currentTempArticle: new Object(),
    bigCategories: ["알고리즘", "웹 개발자", "어플 개발자"],
    middleCategories: [
      ["기본 문제", "기출 문제"],
      ["FrontEnd", "BackEnd"],
      ["Android", "IOS"],
    ],
    smallCategories: [
      [
        [{ name: "기초", value: 111 }],
        [
          { name: "삼성", value: 121 },
          { name: "카카오", value: 122 },
          { name: "기타", value: 123 },
        ],
      ],
      [
        [
          { name: "Vue", value: 211 },
          { name: "React", value: 212 },
          { name: "Angular", value: 213 },
          { name: "HTML", value: 214 },
          { name: "CSS", value: 215 },
          { name: "JavaScript", value: 216 },
          { name: "JQuery", value: 217 },
          { name: "JSP", value: 218 },
        ],
        [
          { name: "Spring", value: 221 },
          { name: "Django", value: 222 },
          { name: "Node", value: 223 },
          { name: "Flask", value: 224 },
        ],
      ],
      [
        [
          { name: "Java", value: 311 },
          { name: "Kotlin", value: 312 },
        ],
        [{ name: "Swift", value: 321 }],
      ],
    ],
  },
  mutations: {
    setUpdateArticleId(state, payload) {
      state.updateArticleId = payload;
    },

    setFollowingList(state, payload) {
      state.followingList = payload;
    },
    setFollowerList(state, payload) {
      state.followerList = payload;
    },
    setJwtAuthToken(state, payload) {
      state.jwtAuthToken = payload;
    },
    setLoggedIn(state, payload) {
      state.loggedIn = payload;
    },
    setProfile(state, payload) {
      state.profile = payload;
    },
    setUserId(state, payload) {
      state.userId = payload;
    },

    setRegistModal(state, payload) {
      state.registModal = payload;
    },
    setLoginModal(state, payload) {
      state.loginModal = payload;
    },

    setCurrentTempArticle: (state, payload) => {
      state.currentTempArticle = payload;
    },
  },
  getters: {
    updateArticleId(state) {
      return state.updateArticleId;
    },

    followerList(state) {
      return state.followerList;
    },
    followingList(state) {
      return state.followingList;
    },
    jwtAuthToken(state) {
      return state.jwtAuthToken;
    },
    loggedIn(state) {
      return state.loggedIn;
    },
    profile(state) {
      return state.profile;
    },
    userId(state) {
      return state.userId;
    },

    registModal(state) {
      return state.registModal;
    },
    loginModal(state) {
      return state.loginModal;
    },

    currentTempArticle: (state) => {
      return state.currentTempArticle;
    },
  },
  actions: {
    setUpdateArticleId({ commit }, payload) {
      commit("setUpdateArticleId", payload);
    },

    setFollowerList({ commit }, payload) {
      commit("setFollowerList", payload);
    },
    setFollowingList({ commit }, payload) {
      commit("setFollowingList", payload);
    },
    setJwtAuthToken({ commit }, payload) {
      commit("setJwtAuthToken", payload);
    },
    setLoggedIn({ commit }, payload) {
      commit("setLoggedIn", payload);
    },
    setProfile({ commit }, payload) {
      commit("setProfile", payload);
    },
    setUserId({ commit }, payload) {
      commit("setUserId", payload);
    },

    setRegistModal({ commit }, payload) {
      commit("setRegistModal", payload);
    },
    setLoginModal({ commit }, payload) {
      commit("setLoginModal", payload);
    },

    setCurrentTempArticle: ({ commit }, payload) => {
      commit("setCurrentTempArticle", payload);
    },
  },
});

export default store;
