<template>
  <div>
    <v-snackbar
      v-show="snackbar"
      v-model="snackbar"
      :bottom="y === 'bottom'"
      color="#9FA9D8"
      :left="x === 'left'"
      :multi-line="mode === 'multi-line'"
      :right="x === 'right'"
      :timeout="timeout"
      :top="y === 'top'"
      :vertical="mode === 'vertical'"
    >
      {{ text }}
      <template v-slot:action="{ attrs }">
        <v-btn dark text v-bind="attrs" @click="snackbar = false">닫기</v-btn>
      </template>
    </v-snackbar>
    <v-card>
      <v-row align="center" justify="center" class="loginModal">
        <v-col class="loginModal">
          <v-card class="elevation-12" style="background-color:#f1f3f5">
            <v-toolbar color="#595959" dark flat>
              <v-icon left>power_settings_new</v-icon>

              <v-spacer></v-spacer>
              <v-btn text :small="true" @click="closeModal">
                <v-icon :small="true">close</v-icon>
              </v-btn>
            </v-toolbar>
            <v-card-text>
              <v-form ref="form">
                <v-text-field
                  id="email"
                  label="Email"
                  name="email"
                  prepend-icon="mdi-account"
                  type="text"
                  color="black"
                  v-model="email"
                  :rules="[rules.emailRequired, rules.email]"
                ></v-text-field>

                <v-text-field
                  id="password"
                  label="Password"
                  name="password"
                  prepend-icon="mdi-lock"
                  type="password"
                  color="black"
                  v-model="password"
                  :rules="[rules.passwordRequired]"
                  @keyup.enter="loginHandler"
                ></v-text-field>
              </v-form>
            </v-card-text>

            <v-card-actions>
              <v-btn text @click="kakaoLogin">
                <img src="@/assets/kakao_btn5.png" class="btn" />
              </v-btn>
              <v-btn text @click="naverLogin">
                <img src="@/assets/naver_btn2.png" class="btn" />
              </v-btn>
              <v-spacer></v-spacer>
              <v-btn text @click="joinModalOpen">아직 회원이 아니신가요?</v-btn>
              <v-btn class="loginBtn" color="#9fa9d8" dark @click="loginHandler">
                <b>로그인</b>
              </v-btn>
            </v-card-actions>
            <br />
          </v-card>
        </v-col>
      </v-row>
    </v-card>
  </div>
</template>

<script>
import axios from "axios";
export default {
  name: "login",
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

    registModal: {
      get() {
        return this.$store.getters.registModal;
      },
      set(value) {
        this.$store.dispatch("setRegistModal", value);
      },
    },
    loginModal: {
      get() {
        return this.$store.getters.loginModal;
      },
      set(value) {
        this.$store.dispatch("setLoginModal", value);
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
  },

  methods: {
    joinModalOpen() {
      this.loginModal = false;
      this.registModal = true;
    },
    kakaoLogin() {
      location.href = process.env.VUE_APP_ACCOUNT + "kakaoLogin";
    },
    naverLogin() {
      location.href = process.env.VUE_APP_ACCOUNT + "naverlogin";
    },
    loginHandler() {
      if (this.$refs.form.validate()) {
        this.login();
      }
    },
    login() {
      axios
        .post(process.env.VUE_APP_ACCOUNT + "login", {
          email: this.email,
          password: this.password,
        })
        .then((res) => {
          if (res.status) {
            this.loggedIn = res.data.data.nickname;
            this.userId = res.data.data.id;

            this.profile =
              process.env.VUE_APP_ACCOUNT +
              "downloadFile/" +
              this.userId +
              ".jpg";

            this.getFollowingList();
            this.jwtAuthToken = res.headers["jwt-auth-token"];

            this.$emit("login-success");
            this.$router.push("/");
          }
        })
        .catch(() => {
          this.text = "아이디 또는 비밀번호를 다시 확인해주세요!";
          this.snackbar = true;
        });
    },
    closeModal() {
      this.$emit("close-modal");
    },
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
  data() {
    return {
      email: "",
      password: "",
      following: new Array(),
      rules: {
        emailRequired: (value) => !!value || "이메일은 필수값입니다.",
        passwordRequired: (value) => !!value || "패스워드는 필수값입니다.",
        email: (value) =>
          /^((?!\.)[\w-_.]*[^.])(@\w+)(\.\w+(\.\w+)?[^.\W])$/.test(value) ||
          "이메일 형식이 아닙니다.",
      },
      snackbar: false,
      text: "",
      timeout: 5000,
      x: null,
      y: "top",
      mode: "",
    };
  },
};
</script>
<style scoped>
.loginModal {
  padding: 0;
  margin: 0;
}

.btn {
  width: 45px;
  background: none;
}
@media screen and (max-width: 500px) {
  .v-text-field {
    width: 100%;
  }

  .v-card__actions {
    display: block;
  }
}

.theme--light.v-btn:hover::before,
.theme--light.v-btn:focus::before,
.theme--dark.v-btn:hover::before,
.theme--dark.v-btn:focus::before {
  opacity: 0;
}
.v-text-field input {
  margin-top: 4px !important;
  padding-left: 8px !important;
}

.loginBtn:hover {
  opacity: 0.7;
}
</style>
