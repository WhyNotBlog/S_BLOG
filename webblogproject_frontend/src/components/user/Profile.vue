<template>
  <div class="page">
    <br />
    <v-layout row justify-space-around class="backImg">
      <v-flex xs12 sm12 md3 lg3 xl3>
        <div class="profile">
          <v-avatar>
            <img :src="imgSrc" @error="imgError" />
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

    <div style="text-align:center">
      <v-layout row wrap justify-space-around>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <h4>게시물</h4>
          <div>{{ contentCnt }}</div>
        </v-flex>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <h4>팔로워</h4>

          <v-dialog v-model="followerModal" width="650px">
            <template #activator="{ on: dialog, attrs}">
              <div text fab slot="activator" v-bind="attrs" v-on="{ ...dialog }">
                {{ userFollowerList.length }}
              </div>
            </template>
            <Follow
              @close-modal="closeModal"
              type="Follower"
              :nickname="this.nickname"
              :followerList="this.userFollowerList"
            ></Follow>
          </v-dialog>
        </v-flex>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <h4>팔로잉</h4>
          <v-dialog v-model="followingModal" width="650px">
            <template #activator="{ on: dialog, attrs}">
              <div text fab slot="activator" v-bind="attrs" v-on="{ ...dialog }">
                {{ userFollowingList.length }}
              </div>
            </template>

            <Follow
              @close-modal="closeModal2"
              type="Following"
              :nickname="this.nickname"
              :followingList="this.userFollowingList"
            ></Follow>
          </v-dialog>
        </v-flex>
      </v-layout>
    </div>
    <br />
    <br />
    <v-card height="100%" style="margin:auto; width:80%">
      <v-tabs background-color="#f1f3f5" color="black">
        <v-tab>최근 게시물</v-tab>

        <v-tab-item class="tab">
          <br />
          <div class="post" style="margin-left:10px; margin-right:10px">
            <Card :data="this.articles" v-if="isCard" :isTemp="false" />
          </div>
          <br />
        </v-tab-item>
      </v-tabs>
    </v-card>
  </div>
</template>
<script src="https://unpkg.com/vue-infinite-loading@2.4.4/dist/vue-infinite-loading.js"></script>

<script>
import axios from "axios";
import PostView from "@/components/PostView";
import Follow from "@/components/user/UserFollowList";
import Card from "@/components/article/Card";

export default {
  name: "Profile",
  props: ["id"],
  components: { PostView, Follow, Card },
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
  },

  created() {
    this.getFollowingList();

    axios.get(process.env.VUE_APP_ARTICLE + "user/" + this.id + "/0").then((res) => {
      this.contentCnt = res.data.data.totalElements;
      this.articles = res.data.data.content;
      this.isCard = true;
      //console.log(this.articles);
    });

    axios.get(process.env.VUE_APP_ACCOUNT + "getUserInfoById/" + this.id).then((res) => {
      if (res.status) {
        let data = res.data.data;
        this.nickname = data.nickname;
        this.email = data.email;
        this.gitUrl = data.giturl;
        this.imgSrc = process.env.VUE_APP_ACCOUNT + "downloadFile/" + this.id + ".jpg";
        this.introduce = data.introduce;
      }
    });
  },

  methods: {
    imgError() {
      //console.log("err");
      this.imgSrc = `${require("@/assets/profile.svg")}`;
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
      axios.get(process.env.VUE_APP_FOLLOW + "followingList/" + this.id).then((res) => {
        this.userFollowingList = res.data.data;
      });

      axios.get(process.env.VUE_APP_FOLLOW + "followList/" + this.id).then((res) => {
        this.userFollowerList = res.data.data;
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
      userFollowerList: new Array(),
      userFollowingList: new Array(),
    };
  },
};
</script>

<style scoped>
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

.v-sheet.v-card:not(.v-sheet--outlined) {
  box-shadow: none;
}
</style>
