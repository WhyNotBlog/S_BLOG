<template>
  <div class="page">
    <br />
    <v-layout row justify-space-around class="backImg">
      <v-flex xs12 sm12 md3 lg3 xl3>
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
            <v-btn color="white" text v-show="gitUrl != ''" @click="moveGit">
              <img src="@/assets/github.svg" width="30px" />
            </v-btn>
          </h2>

          <span>{{ introduce }}</span>
          <br />
        </div>
      </v-flex>
    </v-layout>

    <br />

    <v-divider style="margin:5px auto; width:96%"></v-divider>

    <div style="text-align:center;">
      <v-layout row wrap justify-space-around>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <h4>게시물</h4>
          <div>{{ contentCnt }}</div>
        </v-flex>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <h4>팔로워</h4>

          <v-dialog v-model="followerModal" width="650px">
            <template #activator="{ on: dialog , attrs}">
              <div
                text
                fab
                slot="activator"
                v-bind="attrs"
                v-on="{ ...dialog }"
              >{{ followerList.length }}</div>
            </template>
            <Follow @close-modal="closeModal" type="Follower" :id="this.userId"></Follow>
          </v-dialog>
        </v-flex>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <h4>팔로잉</h4>
          <v-dialog v-model="followingModal" width="650px">
            <template #activator="{ on: dialog, attrs}">
              <div
                text
                fab
                slot="activator"
                v-bind="attrs"
                v-on="{ ...dialog }"
              >{{ followingList.length }}</div>
            </template>

            <Follow @close-modal="closeModal2" type="Following" :id="this.userId"></Follow>
          </v-dialog>
        </v-flex>
      </v-layout>
      <br />
      <div>
        <v-btn class="editBtn" color="#9fa9d8" dark @click="moveUpdate" style="margin-right:10px">
          <b>프로필 편집</b>
        </v-btn>
      </div>
    </div>
    <br />
    <br />

    <v-card height="100%" style="margin:auto; margin-left:15px; margin-right:15px">
      <v-tabs background-color="#f1f3f5" color="black">
        <v-tab>최근 게시물</v-tab>
        <v-tab>임시저장 글</v-tab>
        <v-tab>좋아요한 글</v-tab>

        <v-tab-item style="background-color:#f1f3f5">
          <br />
          <div class="post" style="margin-left:10px; margin-right:10px">
            <Card :data="this.articles" v-if="isCard" />
          </div>
          <br />
        </v-tab-item>

        <v-tab-item style="background-color:#f1f3f5">
          <br />
          <div class="post" style="margin-left:10px; margin-right:10px">
            <Card :data="this.tempArticles" v-if="isCard" />
          </div>
          <br />
        </v-tab-item>

        <v-tab-item style="background-color:#f1f3f5">
          <br />
          <div class="post" style="margin-left:10px; margin-right:10px">
            <Card :data="this.articles" v-if="isCard" />
          </div>
          <br />
        </v-tab-item>
      </v-tabs>
    </v-card>
  </div>
</template>


<script>
import axios from "axios";
import Follow from "@/components/user/FollowList";
import Card from "@/components/article/Card";

export default {
  name: "info",
  components: { Follow, Card },
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

    this.getFollowingList();

    axios
      .get(process.env.VUE_APP_ARTICLE + "user/" + this.userId + "/0")
      .then((res) => {
        this.contentCnt = res.data.data.totalElements;
        this.articles = res.data.data.content;
        this.isCard = true;
        //console.log(this.articles);
      });

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
        }
      });

    axios
      .get(process.env.VUE_APP_ARTICLETEMP + "user/" + this.userId, {
        headers: {
          "jwt-auth-token": this.jwtAuthToken,
        },
      })
      .then((res) => {
        this.tempArticles = res.data.data;
        console.log(this.tempArticles);
      });
  },

  methods: {
    moveUpdate() {
      this.$router.push("/user/update");
    },
    moveGit() {
      location.href = this.gitUrl;
    },
    closeModal() {
      this.followerModal = false;
    },
    closeModal2() {
      this.followingModal = false;
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
      isCard: false,
      imgSrc: "",
      fileName: "",
      selectedFile: "",
      nickname: "",
      password: "",
      passwordCheck: "",
      email: "",
      gitUrl: "",
      introduce: "",
      articles: new Array(),
      contentCnt: 0,
      followerModal: false,
      followingModal: false,
      page: 0,
      tempArticles: new Array(),
    };
  },
};
</script>

<style scoped>
.v-sheet.v-card:not(.v-sheet--outlined) {
  box-shadow: none;
}
.v-avatar {
  width: 150px !important;
  height: 150px !important;
  min-width: 170px;
}

.profile {
  display: flex;
  justify-content: center;
  min-width: 200px;
}

.editBtn {
  float: right;
}

.backImg {
  width: 80%;
  margin: auto;
  background-color: #b7b4da;
  border-radius: 2rem;
  background-size: 152vh;
  padding: 20px;
  box-shadow: 3px 3px 5px 1px #595959;
}

.infoBox {
  color: black;
}
</style>
