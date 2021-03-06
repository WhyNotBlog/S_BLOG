<template>
  <nav>
    <v-toolbar dark color="#595959">
      <v-app-bar-nav-icon @click="navActive"></v-app-bar-nav-icon>

      <v-btn @click="moveHome" :ripple="false" class="mainbtn">
        <img src="@/assets/logo.png" width="115px" />
      </v-btn>

      <v-spacer></v-spacer>

      <v-tooltip bottom color="secondary">
        <template #activator="{ on}">
          <v-btn class="icons" text fab slot="activator" @click="moveSearch" min-width="56px">
            <v-icon v-on="on">search</v-icon>
          </v-btn>
        </template>
        <span>검색</span>
      </v-tooltip>

      <div v-if="loggedIn != null">
        <v-tooltip bottom color="secondary">
          <template #activator="{ on}">
            <v-avatar
              style="border:1px solid"
              class="icon2"
              slot="activator"
              @click="moveInfo"
              width="35px"
              height="35px"
              min-width="35px"
            >
              <img class="myprofile" v-on="on" :src="profile" @error="imgError" width="35px" />
            </v-avatar>
          </template>
          <span>내 정보</span>
        </v-tooltip>

        <v-tooltip bottom color="secondary">
          <template #activator="{ on}">
            <v-btn class="icon2" text fab slot="activator" @click="movePost" min-width="56px">
              <v-icon v-on="on">mdi-pencil</v-icon>
            </v-btn>
          </template>
          <span>글 작성</span>
        </v-tooltip>

        <v-btn color="#9FA9D8" slot="activator" @click="logout" :small="true">
          <b>logout</b>
        </v-btn>
      </div>
      <div v-else>
        <v-dialog v-model="loginModal" persistent width="500px">
          <template #activator="{ on:dialog, attrs}">
            <v-tooltip bottom color="secondary">
              <template #activator="{ on: tooltip}">
                <v-btn
                  text
                  fab
                  slot="activator"
                  v-bind="attrs"
                  v-on="{ ...dialog, ...tooltip }"
                  min-width="56px"
                >
                  <v-icon>power_settings_new</v-icon>
                </v-btn>
              </template>
              <span>로그인</span>
            </v-tooltip>
          </template>
          <login @close-modal="closeModal" @login-success="closeModal"></login>
        </v-dialog>

        <v-dialog v-model="registModal" persistent width="500px">
          <template #activator="{ on: dialog, attrs}">
            <v-tooltip bottom color="secondary">
              <template #activator="{ on: tooltip }">
                <v-btn text fab slot="activator" v-bind="attrs" v-on="{ ...dialog, ...tooltip }">
                  <v-icon>person_add</v-icon>
                </v-btn>
              </template>
              <span>회원가입</span>
            </v-tooltip>
          </template>
          <register @close-modal="closeModal2" @email-success="closeModal2"></register>
        </v-dialog>
      </div>
    </v-toolbar>

    <v-navigation-drawer app color="#595959" v-model="drawer" temporary>
      <v-list>
        <v-list style="text-align:center; color:white">
          <div v-if="loggedIn">
            <v-avatar class="hImg">
              <img :src="profile" @error="imgError" />
            </v-avatar>
            <br />
            <br />
            <span>
              <strong>{{ loggedIn }}</strong>님 환영합니다.
            </span>
          </div>
          <div v-else>
            <img src="@/assets/logo.png" width="130px" />
            <br />

            <v-btn text color="white" @click="loginModal = true">S_Blog 시작하기</v-btn>
            <br />
          </div>
        </v-list>

        <v-divider></v-divider>

        <br />

        <v-list>
          <v-list-item v-for="item in links" :key="item.text" :to="item.route" style="color:white">
            <v-list-item-content>
              <v-list-item-title v-html="item.text" style="color:white"></v-list-item-title>
            </v-list-item-content>

            <v-list-item-action>
              <v-icon style="color:white">{{ item.icon }}</v-icon>
            </v-list-item-action>
          </v-list-item>
        </v-list>

        <v-divider></v-divider>

        <br />
        <div style="display:flex; justify-content:center">
          <v-btn v-for="icon in icons" :key="icon" class="mx-3 white--text" icon>
            <v-icon size="24px">{{ icon }}</v-icon>
          </v-btn>
        </div>
        <v-col class="text-center" cols="12" style="color:white">
          {{ new Date().getFullYear() }},
          <v-icon color="white" size="16px">copyright</v-icon>
          <strong>외않되</strong>
        </v-col>
      </v-list>
    </v-navigation-drawer>
  </nav>
</template>

<script>
import Login from "@/components/user/Login";
import Register from "@/components/user/Register";
//import axios from "axios";

export default {
  data() {
    return {
      icons: ["mdi-facebook", "mdi-twitter", "mdi-linkedin", "mdi-instagram"],

      drawer: false,
      links: [
        {
          icon: "home",
          text: "Home",
          route: "/",
        },
        { icon: "find_in_page", text: "Search", route: "/search/title/blank" },
        { icon: "account_circle", text: "MyPage", route: "/user/info" },
        { icon: "mdi-pencil", text: "Write", route: "/article/post" },
      ],
    };
  },
  components: { Login, Register },
  computed: {
    jwtAuthToken: {
      get() {
        return this.$store.getters.jwtAuthToken;
      },
      set(value) {
        this.$store.dispatch("setJwtAuthToken", value);
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

    loggedIn: {
      get() {
        return this.$store.getters.loggedIn;
      },
      set(value) {
        this.$store.dispatch("setLoggedIn", value);
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

  methods: {
    imgError() {
      //console.log("err");
      this.profile = `${require("@/assets/profile.svg")}`;
    },
    navActive() {
      this.drawer = !this.drawer;
    },
    moveHome() {
      this.$router.push("/");
    },
    moveSearch() {
      this.$router.push("/search/title/blank");
    },
    moveInfo() {
      this.$router.push("/user/info");
    },
    movePost() {
      this.$router.push({ name: "Post" });
    },
    closeModal() {
      this.loginModal = false;
    },
    closeModal2() {
      this.registModal = false;
    },
    logout() {
      this.loggedIn = null;
      this.jwtAuthToken = null;
      this.$router.push("/");
      // axios
      //   .post(
      //     process.env.VUE_APP_ACCOUNT + "logout",
      //     {},
      //     {
      //       headers: {
      //         "jwt-auth-token": this.jwtAuthToken,
      //       },
      //     }
      //   )
      //   .then((res) => {
      //     if (res.status) {
      //       this.loggedIn = null;
      //       this.jwtAuthToken = null;
      //       this.profile = `${require("@/assets/profile.svg")}`;
      //       this.userId = null;
      //       this.followingList = new Array();
      //       this.followerList = new Array();
      //       this.$router.push("/");
      //     }
      //   })
      //   .catch((err) => {
      //     console.log(err);
      //   });
    },
  },
};
</script>

<style scoped>
@media screen and (max-width: 500px) {
  .v-btn--fab.v-size--default {
    width: 38px;
  }

  .icons {
    display: none;
  }

  .v-dialog {
    width: 80% !important;
  }
}
@media screen and (max-width: 400px) {
  .icon2 {
    display: none;
  }
}

.theme--light.mainbtn:hover::before,
.theme--light.mainbtn:focus::before,
.theme--dark.mainbtn:hover::before,
.theme--dark.mainbtn:focus::before {
  opacity: 0;
}

.mainbtn {
  box-shadow: none;
}

.mainbtn:hover {
  cursor: pointer;
}

.v-application--is-ltr
  .v-toolbar__content
  > .v-btn.v-btn--icon:first-child
  + .v-toolbar__title,
.v-application--is-ltr
  .v-toolbar__extension
  > .v-btn.v-btn--icon:first-child
  + .v-toolbar__title {
  padding: 0;
}

.hImg {
  width: 100px !important;
  height: 100px !important;
}

.theme--light.v-btn:not(.v-btn--flat):not(.v-btn--text):not(.v-btn--outlined) {
  background: none;
}

.v-btn:not(.v-btn--round).v-size--default {
  padding: 0;
}

.myprofile:hover {
  cursor: pointer;
}
</style>
