<template>
  <v-dialog v-model="nicknameModal" persistent width="500">
    <v-card>
      <v-row align="center" justify="center" class="modal">
        <v-col class="modal">
          <v-card class="elevation-12">
            <v-toolbar color="black" dark flat>
              <v-icon left>how_to_reg</v-icon>
              <v-spacer></v-spacer>
              <v-btn text :small="true" @click="closeModal">
                <v-icon :small="true">close</v-icon>
              </v-btn>
            </v-toolbar>
            <v-card-text>
              <v-form ref="form">
                <v-text-field
                  id="nickname"
                  ref="nickname"
                  label="Nickname"
                  name="nickname"
                  prepend-icon="mdi-account-circle"
                  type="text"
                  color="black"
                  v-model="nickname"
                  :rules="[rules.nicknameRequired, rules.nicknameCheck]"
                  :append-icon="nicknameIcon"
                  @click:append="searchNickname"
                ></v-text-field>
              </v-form>
            </v-card-text>
            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="black white--text" @click="joinHandler">회원가입</v-btn>
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from "axios";
export default {
  name: "snsRegist",
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
  created() {
    this.loginModal = false;
    axios
      .post(process.env.VUE_APP_ACCOUNT + "getSNSData", {})
      .then((res) => {
        this.nicknameModal = true;
        this.nickname = res.data.data.nickname;
        this.email = res.data.data.email;
        this.loginkind = res.data.data.loginkind;
      })
      .catch((err) => {
        console.log(err);
      });
  },

  data() {
    return {
      nickname: "",
      email: "",
      loginkind: "",
      nicknameModal: false,
      nicknameIcon: "mdi-checkbox-multiple-blank-outline",
      rules: {
        nicknameRequired: (value) => !!value || "닉네임은 필수값입니다.",
        nicknameCheck: () => false || "닉네임은 중복확인이 필요합니다.",
      },
    };
  },
  methods: {
    searchNickname() {
      axios
        .get(process.env.VUE_APP_ACCOUNT + "findNickname/" + this.nickname)
        .then((res) => {
          if (res.data.data == 0) {
            alert("이미 존재하는 닉네임입니다.");
            this.nickname = "";
            //this.$refs.nickname.focus();
            this.nicknameIcon = "mdi-checkbox-multiple-blank-outline";
            return;
          }

          this.nicknameIcon = "mdi-checkbox-multiple-marked";
          this.rules.nicknameCheck = true;
        });
    },
    joinHandler() {
      if (this.$refs.form.validate()) {
        this.join();
      }
    },
    join() {
      axios
        .post(process.env.VUE_APP_ACCOUNT + "snsRegist", {
          email: this.email,
          nickname: this.nickname,
          loginkind: this.loginkind,
        })
        .then(() => {
          this.$router.push("/");
        });
    },

    closeModal() {
      this.nicknameModal = false;
      this.$router.push("/");
    },
  },
};
</script>

<style scoped>
.modal {
  padding: 0;
  margin: 0;
}
</style>
