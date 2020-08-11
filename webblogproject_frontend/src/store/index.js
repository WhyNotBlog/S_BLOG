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
    registModal: false,
    loginModal: false,
    loggedIn: null,
    userId: null,
    profile: `${require("@/assets/profile.svg")}`,
    jwtAuthToken: null,
    currentArticle: new Object(),
    currentTempArticle: new Object(),
    bigCategories : [
      '알고리즘',
      '웹 개발자',
      '어플 개발자'
    ],
    middleCategories: [
      ["기본 문제",
      "기출 문제"],
      ["프론트엔드",
      "백엔드",],
      ["안드로이드",
      "IOS",],
    ],
    smallCategories: [[
      [{name : "", value : 11},],
      [{name : "삼성", value : 12},{name : "카카오", value : 13},{name : "기타", value : 14},],
    ],
    [
      [{name : "Vue", value : 21}, {name : "React", value : 22}, {name : "Angular", value : 23},
      {name : "HTML", value : 24}, {name : "CSS", value : 25}, {name : "JavaScript", value : 26},
      {name : "JQuery", value : 27}, {name : "JSP", value : 28},],
      [{name : "Spring", value : 31}, {name : "Django", value : 32}, {name : "Node", value : 33},
      {name : "Flask", value : 34},],
    ],
    [
      [{name : "Java", value : 41}, {name : "Kotlin", value : 42},],
      [{name : "Swift", value : 43},],
    ],
    ]
  },
  mutations: {
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
    setCurrentArticle: (state, payload) => {
      state.currentArticle = payload;
    },
    setCurrentTempArticle: (state, payload) => {
      state.currentTempArticle = payload;
    },
  },
  getters: {
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
    currentArticle: (state) => {
      return state.currentArticle;
    },
    currentTempArticle: (state) => {
      return state.currentTempArticle;
    },
  },
  actions: {
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
    setCurrentArticle: ({ commit }, payload) => {
      commit("setCurrentArticle", payload);
    },
    setCurrentTempArticle: ({ commit }, payload) => {
      commit("setCurrentTempArticle", payload);
    },
  },
});

export default store;
