<template>
  <div class="main">
    <br />
    <img src="@/assets/loading.svg" width="200px" />
  </div>
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

    userId: {
      get() {
        return this.$store.getters.userId;
      },
      set(value) {
        this.$store.dispatch("setUserId", value);
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

    followingList: {
      get() {
        return this.$store.getters.followingList;
      },
      set(value) {
        this.$store.dispatch("setFollowingList", value);
      },
    },
    followerList: {
      get() {
        return this.$store.getters.followerList;
      },
      set(value) {
        this.$store.dispatch("setFollowerList", value);
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
            //console.log(res.data.data.id);
            this.userId = res.data.data.id;
            this.profile =
              process.env.VUE_APP_ACCOUNT +
              "downloadFile/" +
              res.data.data.id +
              ".jpg";
            this.getFollowingList();
            //console.log(this.profile);
            this.$router.push("/");
          });
      })
      .catch((err) => {
        console.log(err);
      });
  },

  methods: {
    getFollowingList() {
      axios
        .get(process.env.VUE_APP_FOLLOW + "followingList/" + this.userId)
        .then((res) => {
          this.followingList = res.data.data;
        });

      axios
        .get(process.env.VUE_APP_FOLLOW + "followList/" + this.userId)
        .then((res) => {
          this.followerList = res.data.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

<style scoped>
.main {
  text-align: center;
}
</style>
