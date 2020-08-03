<template>
  <v-card>
    <v-row align="center" justify="center" class="loginModal">
      <v-col class="loginModal">
        <v-card class="elevation-12" style="background-color:#f1f3f5">
          <v-toolbar color="#595959" dark flat>
            <v-icon left>person_add</v-icon>

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
                ref="email"
                prepend-icon="mdi-email"
                type="text"
                color="black"
                @change="reCheckEmail"
                v-model="email"
                :rules="[rules.emailRequired, rules.email, rules.emailCheck]"
                :append-icon="emailIcon"
                @click:append="searchEmail"
              ></v-text-field>

              <v-text-field
                id="nickname"
                ref="nickname"
                label="Nickname"
                name="nickname"
                prepend-icon="mdi-account-circle"
                type="text"
                color="black"
                @change="reCheckNickname"
                v-model="nickname"
                :rules="[rules.nicknameRequired, rules.nicknameCheck]"
                :append-icon="nicknameIcon"
                @click:append="searchNickname"
              ></v-text-field>

              <v-text-field
                id="password"
                label="Password"
                ref="password"
                name="password"
                prepend-icon="mdi-lock"
                type="password"
                color="black"
                v-model="password"
                :rules="[rules.passwordRequired, rules.password]"
              ></v-text-field>

              <v-text-field
                id="passwordCheck"
                label="Password Check"
                name="passwordCheck"
                prepend-icon="mdi-lock"
                type="password"
                color="black"
                v-model="passwordCheck"
                :rules="[rules.passwordCheckRequired, rules.passwordCheck]"
              ></v-text-field>

              <v-text-field
                id="gitUrl"
                ref="gitUrl"
                label="Git URL (Optional)"
                name="gitUrl"
                prepend-icon="mdi-git"
                type="text"
                color="black"
                v-model="gitUrl"
              ></v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn text @click="loginModalOpen">이미 계정이 있으신가요?</v-btn>
            <v-btn color="#595959" outlined @click="joinHandler">회원가입</v-btn>
          </v-card-actions>

          <br />
        </v-card>
      </v-col>
    </v-row>
  </v-card>
</template>

<script>
import axios from "axios";
export default {
  name: "regist",
  created() {
    this.loginModal = false;
  },
  computed: {
    loginModal: {
      get() {
        return this.$store.getters.loginModal;
      },
      set(value) {
        this.$store.dispatch("setLoginModal", value);
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
  },
  methods: {
    reCheckEmail() {
      if (this.email != this.emailChecked) {
        this.emailIcon = "mdi-checkbox-multiple-blank-outline";
        this.rules.emailCheck = () =>
          false || "이메일은 중복확인이 필요합니다.";
      }
    },
    reCheckNickname() {
      if (this.nickname != this.nicknameChecked) {
        this.nicknameIcon = "mdi-checkbox-multiple-blank-outline";
        this.rules.nicknameCheck = () =>
          false || "닉네임은 중복확인이 필요합니다.";
      }
    },
    loginModalOpen() {
      this.registModal = false;
      this.loginModal = true;
    },
    closeModal() {
      this.$emit("close-modal");
    },
    joinHandler() {
      if (this.$refs.form.validate()) {
        this.join();
      }
    },
    join() {
      axios
        .post(process.env.VUE_APP_ACCOUNT + "regist", {
          nickname: this.nickname,
          password: this.password,
          email: this.email,
          giturl: this.gitUrl,
        })
        .then((res) => {
          if (res.status) {
            this.$emit("email-success");
            this.$router.push("/user/email");
          }
        })
        .catch((err) => {
          console.log(err);
          alert("회원가입 실패");
        });
    },
    searchNickname() {
      if (this.nickname == "") {
        this.$refs.nickname.focus();
        return;
      }
      axios
        .get(process.env.VUE_APP_ACCOUNT + "findNickname/" + this.nickname)
        .then((res) => {
          if (res.data.data == 0) {
            alert("이미 존재하는 닉네임입니다.");
            this.nickname = "";
            this.$refs.nickname.focus();
            this.nicknameIcon = "mdi-checkbox-multiple-blank-outline";
            return;
          }
          this.nicknameIcon = "mdi-checkbox-multiple-marked";
          this.rules.nicknameCheck = true;
          this.nicknameChecked = this.nickname;
          this.$refs.password.focus();
        });
    },
    searchEmail() {
      if (this.email == "") {
        this.$refs.email.focus();
        return;
      }

      axios
        .get(process.env.VUE_APP_ACCOUNT + "findEmail/" + this.email)
        .then((res) => {
          if (res.data.data == 0) {
            alert("이미 존재하는 이메일입니다.");
            this.email = "";
            this.$refs.email.focus();
            this.emailIcon = "mdi-checkbox-multiple-blank-outline";
            return;
          }
          this.emailIcon = "mdi-checkbox-multiple-marked";
          this.rules.emailCheck = true;
          this.emailChecked = this.email;
          this.$refs.nickname.focus();
        });
    },
  },

  data() {
    return {
      nicknameIcon: "mdi-checkbox-multiple-blank-outline",
      emailIcon: "mdi-checkbox-multiple-blank-outline",
      nickname: "",
      password: "",
      passwordCheck: "",
      email: "",
      emailChecked: "",
      nicknameChecked: "",
      gitUrl: "",
      rules: {
        nicknameRequired: (value) => !!value || "닉네임은 필수값입니다.",
        passwordRequired: (value) => !!value || "패스워드는 필수값입니다.",
        passwordCheckRequired: (value) =>
          !!value || "패스워드 확인은 필수값입니다.",
        emailRequired: (value) => !!value || "이메일은 필수값입니다.",
        email: (value) =>
          /^((?!\.)[\w-_.]*[^.])(@\w+)(\.\w+(\.\w+)?[^.\W])$/.test(value) ||
          "이메일 형식이 아닙니다.",
        passwordCheck: (value) =>
          !(value != this.password) || "패스워드와 일치하지 않습니다.",
        password: (value) =>
          /^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{8,}$/.test(value) ||
          "패스워드는 8자리 이상의 문자, 숫자 조합이어야 합니다.",
        nicknameCheck: () => false || "닉네임은 중복확인이 필요합니다.",
        emailCheck: () => false || "이메일은 중복확인이 필요합니다.",
      },
    };
  },
};
</script>

<style scoped>
.loginModal {
  padding: 0;
  margin: 0;
}
.theme--light.v-btn:hover::before,
.theme--light.v-btn:focus::before,
.theme--dark.v-btn:hover::before,
.theme--dark.v-btn:focus::before {
  opacity: 0;
}
</style>
