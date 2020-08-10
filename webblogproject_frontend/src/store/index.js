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
    bigCategories: [
      "알고리즘",
      "웹",
      "어플",
    ],
    smallCategories: [
      [{name : "Basic", value : 1}, {name : "기출문제", value : 2},],
      [{name : "Frontend", value : 3}, {name : "Backend", value : 4}],
      [{name : "Android", value : 5}, {name : "IOS", value : 6}],
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
