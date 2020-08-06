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
    profile: `${require("@/assets/profile.svg")}`,
    jwtAuthToken: null,
    currentArticle: new Object(),
    currentTempArticle: new Object(),
    categories: [
      "알고리즘/Basic",
      "알고리즘/기출 문제",
      "웹/Frontend",
      "웹/Backend",
      "어플/Andriod",
      "어플/IOS",
    ],
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
