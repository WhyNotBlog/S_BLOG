import Vue from "vue";
import App from "./App.vue";
import router from "./router";
import vuetify from "./plugins/vuetify";
import store from "./store";
import "material-design-icons-iconfont/dist/material-design-icons.css";

Vue.config.productionTip = false;

let overrideRipple = {
  directives: {
    ripple: {
      inserted: () => {},
    },
  },
};
Vue.mixin(overrideRipple);

new Vue({
  router,
  vuetify,
  store,
  render: (h) => h(App),
}).$mount("#app");
