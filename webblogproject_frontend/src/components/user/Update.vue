<template>
  <div class="page">
    <br />
    <div v-show="mobileView">
      <div class="backImg">
        <v-layout column>
          <div class="profile">
            <v-avatar>
              <img :src="profile" @error="imgError" />
            </v-avatar>
          </div>
          <br />
          <div class="file">
            <form enctype="multipart/form-data">
              <v-file-input
                class="fileInput"
                label="File"
                @change="fileUpload($event)"
              ></v-file-input>
            </form>
            <v-btn text color="595959" v-on:click="addItem">등록하기</v-btn>
          </div>
        </v-layout>
      </div>
      <div>
        <br />
        <div class="main">
          <v-form ref="form">
            <v-text-field
              id="email"
              label="Email*"
              name="email"
              prepend-inner-icon="mdi-email"
              type="text"
              color="black"
              v-model="email"
              readonly
              filled
            ></v-text-field>

            <v-text-field
              id="nickname"
              label="Nickname*"
              name="nickname"
              prepend-inner-icon="mdi-account-circle"
              type="text"
              color="black"
              v-model="nickname"
              :rules="[rules.nicknameRequired, rules.nicknameCheck]"
              :append-icon="nicknameIcon"
              @change="reCheckNickname"
              @click:append="searchNickname"
            ></v-text-field>
            <v-text-field
              id="password"
              label="Password*"
              name="password"
              prepend-inner-icon="mdi-lock"
              type="password"
              color="black"
              v-model="password"
              :rules="[rules.passwordRequired, rules.password]"
            ></v-text-field>

            <v-text-field
              id="passwordCheck"
              label="Password Check*"
              name="passwordCheck"
              prepend-inner-icon="mdi-lock"
              type="password"
              color="black"
              v-model="passwordCheck"
              :rules="[rules.passwordCheckRequired, rules.passwordCheck]"
            ></v-text-field>

            <v-text-field
              id="gitUrl"
              label="Git URL"
              name="gitUrl"
              prepend-inner-icon="mdi-git"
              type="text"
              color="black"
              v-model="gitUrl"
            ></v-text-field>

            <v-textarea
              v-model="introduce"
              label="Introduce"
              prepend-inner-icon="mdi-comment-processing"
            ></v-textarea>
          </v-form>
        </div>

        <div class="btns">
          <v-btn text color="black" @click="dropHandler"
            >계정 탈퇴를 원하시나요?</v-btn
          >
          <br />
          <v-btn class="changeBtn" color="#9FA9D8" dark @click="updateHandler"
            >수정하기</v-btn
          >
        </div>
      </div>
    </div>

    <v-layout row justify-space-around v-show="!mobileView">
      <v-flex xs8 sm8 md4 lg4 xl4 class="backImg">
        <v-layout column>
          <div class="profile">
            <v-avatar>
              <img :src="profile" @error="imgError" />
            </v-avatar>
          </div>
          <br />
          <div class="file">
            <div class="fileData">
              <form enctype="multipart/form-data">
                <v-file-input
                  label="Profile Image"
                  @change="fileUpload($event)"
                ></v-file-input>
              </form>
            </div>
            <v-btn color="595959" text v-on:click="addItem">등록하기</v-btn>
          </div>
        </v-layout>
      </v-flex>

      <v-flex xs12 sm12 md5 lg5 xl5>
        <br />
        <div class="main">
          <v-form ref="form">
            <v-text-field
              id="email"
              label="Email*"
              name="email"
              prepend-inner-icon="mdi-email"
              type="text"
              color="black"
              append-icon="mdi-lock"
              v-model="email"
              readonly
              filled
            ></v-text-field>

            <v-text-field
              id="nickname"
              label="Nickname*"
              name="nickname"
              prepend-inner-icon="mdi-account-circle"
              type="text"
              color="black"
              v-model="nickname"
              :rules="[rules.nicknameRequired, rules.nicknameCheck]"
              :append-icon="nicknameIcon"
              @change="reCheckNickname"
              @click:append="searchNickname"
            ></v-text-field>
            <v-text-field
              id="password"
              label="Password*"
              name="password"
              prepend-inner-icon="mdi-lock"
              type="password"
              color="black"
              v-model="password"
              :rules="[rules.passwordRequired, rules.password]"
            ></v-text-field>

            <v-text-field
              id="passwordCheck"
              label="Password Check*"
              name="passwordCheck"
              prepend-inner-icon="mdi-lock"
              type="password"
              color="black"
              v-model="passwordCheck"
              :rules="[rules.passwordCheckRequired, rules.passwordCheck]"
            ></v-text-field>

            <v-text-field
              id="gitUrl"
              label="Git URL"
              name="gitUrl"
              prepend-inner-icon="mdi-git"
              type="text"
              color="black"
              v-model="gitUrl"
            ></v-text-field>

            <v-textarea
              v-model="introduce"
              label="Introduce"
              prepend-inner-icon="mdi-comment-processing"
            ></v-textarea>
          </v-form>
        </div>

        <div class="btns">
          <v-btn text color="black" @click="dropHandler"
            >계정 탈퇴를 원하시나요?</v-btn
          >
          <br />
          <v-btn class="changeBtn" color="#9FA9D8" dark @click="updateHandler"
            >수정하기</v-btn
          >
        </div>
      </v-flex>
    </v-layout>
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
    this.rules.nicknameCheck = true;

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
          this.nicknameChecked = this.nickname;
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
    dropHandler() {
      axios
        .delete(process.env.VUE_APP_ACCOUNT + "delete/" + this.loggedIn, {
          headers: {
            "jwt-auth-token": this.jwtAuthToken,
          },
        })
        .then((res) => {
          if (res.status) {
            alert("탈퇴 처리가 되었습니다!");
            this.loggedIn = null;

            this.jwtAuthToken = null;
            this.$router.push("/");
          }
        })
        .catch((err) => {
          console.log(err);
          alert("회원탈퇴 실패");
        });
    },
    updateHandler() {
      if (this.$refs.form.validate()) {
        this.update();
      }
    },

    imgError() {
      console.log("err");
      this.profile = `${require("@/assets/profile.svg")}`;
    },
    update() {
      axios
        .put(
          process.env.VUE_APP_ACCOUNT + "update",
          {
            nickname: this.nickname,
            password: this.password,
            email: this.email,
            giturl: this.gitUrl,
            introduce: this.introduce,
            id: this.fileName,
          },
          {
            headers: {
              "jwt-auth-token": this.jwtAuthToken,
            },
          }
        )
        .then((res) => {
          if (res.status) {
            this.loggedIn = this.nickname;
            alert("회원정보 수정이 완료되었습니다.");
            this.$router.push("/user/info");
          }
        })
        .catch((err) => {
          console.log(err);
          alert("회원정보 수정 실패");
        });
    },
    fileUpload(file) {
      this.selectedFile = file;
      //this.profile = URL.createObjectURL(file);
    },
    addItem() {
      const data = new FormData(); // 서버로 전송할 폼데이터
      const file = this.selectedFile; // 선택된 파일객체
      data.append("file", file); // 폼데이터에 파일을 추가
      console.log(this.fileName);
      console.log(data);
      //   데이터를 서버로 전송하는 코드 추가
      axios
        .post(process.env.VUE_APP_ACCOUNT + "upload", data, {
          headers: {
            "Content-Type": "multipart/form-data",
            "jwt-auth-token": this.jwtAuthToken,
            filename: this.fileName,
          },
        })
        .then(() => {
          this.profile =
            process.env.VUE_APP_ACCOUNT +
            "downloadFile/" +
            this.fileName +
            ".jpg";
          location.reload();
        })
        .catch((err) => {
          console.log(err);
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
        });
    },

    reCheckNickname() {
      if (this.nickname != this.nicknameChecked) {
        this.nicknameIcon = "mdi-checkbox-multiple-blank-outline";
        this.rules.nicknameCheck = () =>
          false || "닉네임은 중복확인이 필요합니다.";
      }
    },
  },

  data() {
    return {
      nicknameIcon: "mdi-checkbox-multiple-marked",
      nicknameChecked: "",
      imgSrc: "",
      fileName: "",
      selectedFile: "",
      nickname: "",
      password: "",
      passwordCheck: "",
      email: "",
      gitUrl: "",
      introduce: "",
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
      },
    };
  },
};
</script>

<style scoped>
.v-avatar[data-v-2d7fd89c] {
  width: 200px !important;
  height: 200px !important;
  max-width: 200px !important;
}

.theme--light.v-btn:hover::before,
.theme--light.v-btn:focus::before,
.theme--dark.v-btn:hover::before,
.theme--dark.v-btn:focus::before {
  opacity: 0;
}

.profile,
.fileData,
.main,
.btns {
  display: flex;
  justify-content: center;
}

.file {
  text-align: center;
}

.v-file-input {
  width: 200px !important;
  margin: auto;
}

.v-text-field {
  width: 400px;
}

.v-btn {
  margin: 5px;
  width: 190px;
}

@media screen and (max-width: 1000px) {
  .v-text-field {
    width: 380px;
  }
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
  .changeBtn {
    width: 130px;
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
.v-avatar {
  width: 150px !important;
  height: 150px !important;
  max-width: 150px;
}

.changeBtn:hover {
  opacity: 0.7;
}

.backImg {
  background-color: #b7b4da;
  border-radius: 2rem;
  background-size: 152vh;
  padding: 20px;
  box-shadow: 3px 3px 5px 1px #595959;
}
</style>
