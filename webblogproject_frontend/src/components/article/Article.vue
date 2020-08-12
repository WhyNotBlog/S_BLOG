<template>
  <div>
    <v-snackbar
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

    <br />
    <div
      class="text-xl-h2 text-lg-h2 text-md-h3 text-sm-h4 text-h5 font-weight-bold text-center"
      id="title"
    >
      {{ article.title }}
    </div>
    <hr class="my-5" />
    <div id="body">
      <div class="font-weight-bold text-center">
        카테고리 : {{ bigCategory }} - {{ middleCategory }} -
        {{ smallCategory }}
      </div>
      <div class="font-weight-bold text-center">
        작성자 : {{ article.editornickname }}
        <v-btn text @click="checkFollow" v-if="user.id != article.writerid">
          <v-icon>{{ followOrUnfollow }}</v-icon>
        </v-btn>
        | 작성일 :
        {{ article.editdate | dateToString }} | 조회수 : {{ article.hits }} |
        <v-btn
          color="red accent-4"
          icon
          v-if="article.isLiked"
          @click="changeLiked()"
        >
          <v-icon middle color="red accent-4">mdi-heart</v-icon>
        </v-btn>
        <v-btn color="red accent-4" icon v-else @click="changeLiked()">
          <v-icon middle color="red accent-4">mdi-heart-outline</v-icon>
        </v-btn>
        <div
          class="d-inline-block"
          v-if="loggedIn !== null && user.id === article.writerid"
        >
          <v-btn color="black accent-4" icon @click="updateArticle()">
            <v-icon middle color="black accent-4"
              >mdi-file-document-edit</v-icon
            >
          </v-btn>
          <v-btn color="black accent-4" icon @click="deleteArticle()">
            <v-icon middle color="black accent-4">mdi-delete</v-icon>
          </v-btn>
        </div>
      </div>
      <div class="text-center" id="catalogue">
        <label>태그 :</label>
        <v-chip
          class="ma-2 text-button chip-btn"
          color="secondary"
          v-for="tag in tags"
          :key="tag.tagid"
          @click="searchTag(tag.tagname)"
          >#{{ tag.tagname }}</v-chip
        >
      </div>
      <div
        class="text-xl-body-1 text-lg-body-1 text-md-body-1 text-sm-body-2 text-body-2 text-center my-5"
        id="text"
      >
        <viewer
          id="markdown-viewer"
          :initialValue="article.content"
          :value="viewerText"
          height="500px"
        />
      </div>
    </div>
    <Comment :articleId="article.articleid" />
  </div>
</template>

<script>
import "codemirror/lib/codemirror.css";
import "@toast-ui/editor/dist/toastui-editor.css";
import { Viewer } from "@toast-ui/vue-editor";

import axios from "axios";
import Comment from "./Comment";

export default {
  name: "Article",
  data() {
    return {
      user: new Object(),
      article: new Object(),
      tags: new Array(),
      viewerText: "",
      userLike: null,
      articleLiked: null,
      userLiked: new Array(),
      categories: new Array(),
      category: new String(),
      categoryInt: new Number(),
      followOrUnfollow: "mdi-account-plus",
      index: 0,
      snackbar: false,
      text: "",
      timeout: 5000,
      x: null,
      y: "top",
      mode: "",
    };
  },
  components: {
    Comment,
    viewer: Viewer,
  },
  created() {
    if (this.loggedIn !== null) {
      axios
        .get(process.env.VUE_APP_ACCOUNT + "getUserInfo/" + this.loggedIn, {
          headers: {
            "jwt-auth-token": this.jwtAuthToken,
          },
        })
        .then((res) => {
          if (res.status) {
            let data = res.data.data;
            this.user = data;
          }
        });
    }

    this.article = this.$store.state.currentArticle;
    
    if (this.article.articleid !== Number(this.$route.params.articleId)) {
      this.$router.push({
        name: "CheatArticle",
        props: { articleId: Number(this.$route.params.articleid) },
      });
    }

    this.categoryInt = this.article.category;

    let bigCategoryIndex = parseInt(String(this.categoryInt)[0]) - 1;
    let middleCategoryIndex = parseInt(String(this.categoryInt)[1]) - 1;
    let smallCategoryIndex = parseInt(String(this.categoryInt)[2]) - 1;

    this.bigCategories = this.$store.state.bigCategories;
    this.middleCategories = this.$store.state.middleCategories;
    this.smallCategories = this.$store.state.smallCategories;

    this.bigCategory = this.bigCategories[bigCategoryIndex];
    this.middleCategory = this.middleCategories[bigCategoryIndex][
      middleCategoryIndex
    ];
    this.smallCategory = this.smallCategories[bigCategoryIndex][
      middleCategoryIndex
    ][smallCategoryIndex].name;

    this.followingList.forEach((element, index) => {
      if (element.id == this.article.writerid) {
        this.followOrUnfollow = "mdi-account-remove";
        this.index = index;
      }
    });

    axios.get(process.env.VUE_APP_ARTICLE + "visit/" + this.article.articleid)
    .then((res) => {
      console.log(res);
      this.article.hits += 1;
    });
    
    axios
      .get(process.env.VUE_APP_TAG + "taglist/" + this.article.articleid)
      .then((res) => {
        let tagData = res.data.data;
        this.tags = tagData;
      });
  },
  mounted() {},
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
    followingList: {
      get() {
        return this.$store.getters.followingList;
      },
      set(value) {
        this.$store.dispatch("setFollowingList", value);
      },
    },
  },
  methods: {
    checkFollow() {
      if (this.loggedIn == null) {
        this.text = "로그인시 팔로우 가능합니다!";
        this.snackbar = true;

        return;
      }

      if (this.followOrUnfollow == "mdi-account-remove") {
        this.unFollow();
        this.followOrUnfollow = "mdi-account-plus";
      } else {
        this.follow();
        this.followOrUnfollow = "mdi-account-remove";
      }
    },
    follow() {
      axios
        .post(
          process.env.VUE_APP_FOLLOW + "regist",
          {
            targetid: this.article.writerid,
            userid: this.userId,
          },
          {
            headers: {
              "jwt-auth-token": this.jwtAuthToken,
            },
          }
        )
        .then((res) => {
          this.followingList.push({
            id: res.data.data.id,
            nickname: res.data.data.nickname,
          });
        })
        .catch((err) => {
          console.log(err);
        });
    },
    unFollow() {
      axios
        .delete(
          process.env.VUE_APP_FOLLOW +
            "delete/" +
            this.userId +
            "/" +
            this.article.writerid,

          {
            headers: {
              "jwt-auth-token": this.jwtAuthToken,
            },
          }
        )
        .then(() => {
          this.followingList.splice(this.index, 1);
        });
    },
    searchTag(tag) {
      this.$router.push("/search/tag/" + tag);
    },
    updateArticle() {
      this.$router.push({ name: "Update" });
    },
    deleteArticle() {
      axios
        .delete(
          process.env.VUE_APP_ARTICLE + `delete/${this.article.articleid}`,
          {
            data: { articleid: this.article.articleid },
            headers: {
              "jwt-auth-token": this.jwtAuthToken,
            },
          }
        )
        .then((res) => {
          if (res.status === 200) {
            this.text = "게시글 삭제에 성공했습니다!";
            this.snackbar = true;

            this.$router.push({ name: "Home" });
          }
        });
    },
    checkLiked(id) {
      if (this.loggedIn !== null) return this.userLiked.includes(id);
      else return false;
    },

    changeLiked(id) {
      if (this.loggedIn !== null) {
        if (!this.checkLiked(id)) {
          axios
            .post(
              process.env.VUE_APP_LIKE + "regist",
              {
                userid: this.user.id,
                articleid: id,
              },
              {
                headers: {
                  "jwt-auth-token": this.jwtAuthToken,
                },
              }
            )
            .then(() => {
              axios
                .get(
                  process.env.VUE_APP_LIKE +
                    `article/${this.article.articleid}`,
                  {
                    headers: {
                      "jwt-auth-token": this.jwtAuthToken,
                    },
                  }
                )
                .then((res) => {
                  this.articleLiked = res.data.data;
                });
            });
        } else {
          axios
            .delete(process.env.VUE_APP_LIKE + `delete/${this.user.id}/${id}`, {
              data: { userid: this.user.id, articleid: id },
              headers: {
                "jwt-auth-token": this.jwtAuthToken,
              },
            })
            .then(() => {
              axios
                .get(
                  process.env.VUE_APP_LIKE +
                    `article/${this.article.articleid}`,
                  {
                    headers: {
                      "jwt-auth-token": this.jwtAuthToken,
                    },
                  }
                )
                .then((res) => {
                  this.articleLiked = res.data.data;
                });
            });
        }
      } else {
        this.text = "좋아요 기능을 사용하려면 로그인을 해야합니다.";
        this.snackbar = true;
      }
    },
  },
  filters: {
    dateToString(date) {
      try {
        return date.slice(0, 10);
      } catch (e) {
        console.log(e);
      }
    },
  },
};
</script>

<style>
.chip-btn :hover {
  cursor: pointer;
}
</style>
