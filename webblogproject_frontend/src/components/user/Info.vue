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

    <div style="text-align:center">
      <v-layout row wrap justify-space-around>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <h4>게시물</h4>
          <div>{{contentCnt}}</div>
        </v-flex>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <h4>팔로워</h4>

          <v-dialog v-model="followerModal" persistent width="650px">
            <template #activator="{ on: dialog, attrs}">
              <div text fab slot="activator" v-bind="attrs" v-on="{ ...dialog}">{{follower}}</div>
            </template>
            <Follow
              @update-follow="getCount"
              @close-modal="closeModal"
              type="Follower"
              :id="this.fileName"
            ></Follow>
          </v-dialog>
        </v-flex>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <h4>팔로잉</h4>
          <v-dialog v-model="followingModal" persistent width="650px">
            <template #activator="{ on: dialog, attrs}">
              <div text fab slot="activator" v-bind="attrs" v-on="{ ...dialog}">{{following}}</div>
            </template>

            <Follow
              @update-follow="getCount"
              @close-modal="closeModal2"
              type="Following"
              :id="this.fileName"
            ></Follow>
          </v-dialog>
        </v-flex>
      </v-layout>
      <br />
      <div>
        <v-btn
          class="editBtn"
          color="#9fa9d8"
          dark
          @click="moveUpdate"
          style="margin-right:10px"
        >프로필 편집</v-btn>
      </div>
    </div>
    <br />
    <br />
    <PostView :data="this.articles" />
  </div>
</template>

<script>
import axios from "axios";
import PostView from "@/components/PostView";
import Follow from "@/components/user/FollowList";

export default {
  name: "info",
  components: { PostView, Follow },
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

          axios
            .get(process.env.VUE_APP_ARTICLE + "user/" + data.id)
            .then((res) => {
              //console.log(res);
              this.contentCnt = res.data.data.length;
              this.articles = res.data.data;
            })
            .catch((err) => {
              console.log(err);
            });

          this.getCount();
        }
      })
      .catch(() => {});
  },

  methods: {
    getCount() {
      axios
        .get(process.env.VUE_APP_FOLLOW + "count/" + this.fileName)
        .then((res) => {
          //console.log(res);
          this.follower = res.data.data.follower;
          this.following = res.data.data.following;
        })
        .catch((err) => {
          console.log(err);
        });
    },
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
      articles: Array(),
      contentCnt: 0,
      following: 0,
      follower: 0,
      followerModal: false,
      followingModal: false,
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
  width: 96%;
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
