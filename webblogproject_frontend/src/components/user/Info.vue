<template>
  <div class="page">
    <br />
    <v-layout row justify-space-around class="backImg">
      <v-flex xs12 sm12 md5 lg5 xl5>
        <div class="profile">
          <v-avatar>
            <img :src="profile" />
          </v-avatar>
        </div>
      </v-flex>

      <v-flex xs12 sm12 md5 lg5 xl5>
        <div class="infoBox">
          <h2>
            {{ nickname }}
            <v-btn text v-show="gitUrl != ''" @click="moveGit">
              <v-icon>mdi-git</v-icon>
            </v-btn>
          </h2>

          <span>{{ introduce }}</span>
          <br />
        </div>
      </v-flex>
    </v-layout>
    <br />

    <v-divider style="margin:5px"></v-divider>

    <div style="text-align:center">
      <v-layout row wrap justify-space-around>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <h4>게시물</h4>
          <div>0</div>
        </v-flex>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <h4>팔로워</h4>
          <div>0</div>
        </v-flex>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <h4>팔로잉</h4>
          <div>0</div>
        </v-flex>
      </v-layout>
      <br />
      <div>
        <v-btn color="#9fa9d8" dark @click="moveUpdate">프로필 편집</v-btn>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "info",
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
    mobileView() {
      return window.innerWidth <= 500;
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
    if (this.loggedIn == null) {
      this.loginModal = true;
      this.$router.push("/");
    }

    axios
      .get(process.env.VUE_APP_ACCOUNT + "getUserInfo/" + this.loggedIn, {
        headers: {
          "jwt-auth-token": this.jwtAuthToken,
        },
      })
      .then((res) => {
        if (res.status) {
          let data = res.data.data;
          this.nickname = data.nickname;
          this.email = data.email;
          this.gitUrl = data.giturl;
          this.introduce = data.introduce;
          this.loggedIn = data.nickname;
          this.fileName = data.id;
        }
      })
      .catch(() => {});
  },

  methods: {
    moveUpdate() {
      this.$router.push("/user/update");
    },
    moveGit() {
      window.location.href = this.gitUrl;
    },
  },

  data() {
    return {
      imgSrc: "",
      fileName: "",
      selectedFile: "",
      nickname: "",
      password: "",
      passwordCheck: "",
      email: "",
      gitUrl: "",
      introduce: "",
    };
  },
};
</script>

<style scoped>
.v-avatar[data-v-2d7fd89c] {
  width: 150px !important;
  height: 150px !important;
  max-width: 150px !important;
}

.profile {
  display: flex;
  justify-content: center;
  min-width: 200px;
}

.v-btn {
  width: 120px;
  float: right;
}
@media screen and (max-width: 500px) {
  .v-text-field {
    width: 340px;
  }
  .v-btn {
    width: 170px;
  }
}

.input {
  width: 50%;
}

@media screen and (max-width: 350px) {
  .v-text-field {
    width: 270px;
  }
  .v-btn {
    width: 150px;
  }
}

@media screen and (max-width: 280px) {
  .v-text-field {
    width: 250px;
  }
  .v-btn {
    width: 120px;
  }
}

.backImg {
  background-image: url("../../assets/back.jpg");
  background-size: 150vh;
  padding: 20px;
}
</style>
