<template>
  <h1>Loading</h1>
</template>

<script>
import axios from "axios";
export default {
  computed: {
    jwtAuthToken: {
      get() {
        return this.$store.getters.jwtAuthToken;
      },
      set(value) {
        this.$store.dispatch("setJwtAuthToken", value);
      },
    },
    loggedIn: {
      get() {
        return this.$store.getters.loggedIn;
      },
      set(value) {
        this.$store.dispatch("setLoggedIn", value);
      },
    },

    loginModal: {
      get() {
        return this.$store.getters.loginModal;
      },
      set(value) {
        this.$store.dispatch("setLoginModal", value);
      },
    },
    profile: {
      get() {
        return this.$store.getters.profile;
      },
      set(value) {
        this.$store.dispatch("setProfile", value);
      },
    },
  },
  created() {
    this.loginModal = false;
    axios
      .post(process.env.VUE_APP_ACCOUNT + "getSNSToken", {})
      .then((res) => {
        this.loggedIn = res.data.data.nickname;
        this.jwtAuthToken = res.headers["jwt-auth-token"];
        axios
          .get(process.env.VUE_APP_ACCOUNT + "getUserInfo/" + this.loggedIn, {
            headers: {
              "jwt-auth-token": this.jwtAuthToken,
            },
          })
          .then((res) => {
            this.profile =
              process.env.VUE_APP_ACCOUNT +
              "downloadFile/" +
              res.data.data.id +
              ".jpg";
            console.log(this.profile);
            this.$router.push("/");
          });
      })
      .catch((err) => {
        console.log(err);
      });
  },
};
</script>

<style></style>
