<template>
  <div>
    <h1 class="display-2">Loading...</h1>
  </div>
</template>

<script>
import axios from "axios";
export default {
  computed: {
    loginModal: {
      get() {
        return this.$store.getters.loginModal;
      },
      set(value) {
        this.$store.dispatch("setLoginModal", value);
      },
    },
  },
  props: ["signupid", "token"],
  created() {
    axios
      .get(
        process.env.VUE_APP_ACCOUNT + "authEmail/" +
          this.signupid +
          "/" +
          this.token
      )
      .then(() => {
        this.loginModal = true;
        this.$router.push("/");
      })
      .catch((err) => {
        console.log(err);
      });
  },
};
</script>

<style scoped>
h1 {
  text-align: center;
}
</style>
