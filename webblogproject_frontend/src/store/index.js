import Vue from "vue";
import Vuex from "vuex";

import createPersistedState from "vuex-persistedstate";

Vue.use(Vuex);

const store = new Vuex.Store({
  plugins: [
    createPersistedState({
      storage: window.sessionStorage,
      paths: ["auth"],
    }),
  ],
  state: {
    registModal: false,
    loginModal: false,
    loggedIn: null,
    profile: `${require("@/assets/profile.svg")}`,
    jwtAuthToken: null,
    currentArticleId: new Number(),
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
    setCurrentArticleId: (state, payload) => {
      state.currentArticleId = payload;
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
    currentArticleId: (state) => {
      return state.currentArticleId;
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

    setCurrentArticleId: ({ commit }, payload) => {
      commit("setCurrentArticleId", payload);
    },
  },
});

export default store;
