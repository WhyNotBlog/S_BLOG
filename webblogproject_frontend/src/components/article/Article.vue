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
    >{{ article.title }}</div>
    <hr class="my-5" />
    <div id="body">
      <div class="font-weight-bold text-center" v-show="article.category!=1 && article.category!=2">
        카테고리 : {{ bigCategory }} - {{ middleCategory }} -
        {{ smallCategory }}
      </div>
      <div class="font-weight-bold text-center">
        작성자 :
        <v-btn text @click="goProfile(article.writerid)">
          <strong>{{ article.editornickname }}</strong>
        </v-btn>
        <v-avatar>
          <img :src="writerProfile" @error="imgError" />
        </v-avatar>

        <v-btn text @click="checkFollow" v-if="userId != article.writerid">
          <v-icon>{{ followOrUnfollow }}</v-icon>
        </v-btn>
        | 작성일 :
        {{ article.editdate | dateToString }} | 조회수 : {{ article.hits }} |
        <div class="d-inline-block" v-if="loggedIn !== null && userId === article.writerid">
          <v-btn color="black accent-4" icon @click="updateArticle()">
            <v-icon middle color="black accent-4">mdi-file-document-edit</v-icon>
          </v-btn>
          <v-btn color="black accent-4" icon @click="deleteArticle()">
            <v-icon middle color="black accent-4">mdi-delete</v-icon>
          </v-btn>
        </div>
        <div id="buttons">
          <v-btn
            color="red accent-4"
            icon
            v-if="checkLiked(article)"
            @click.stop="changeLiked(article)"
          >
            <v-icon middle color="red accent-4">mdi-heart</v-icon>
          </v-btn>
          <v-btn color="red accent-4" icon v-else @click="changeLiked(article)">
            <v-icon middle color="red accent-4">mdi-heart-outline</v-icon>
          </v-btn>
          {{ article.likecount }}명이 좋아합니다
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
        >#{{ tag.tagname }}</v-chip>
      </div>
      <div
        class="text-xl-body-1 text-lg-body-1 text-md-body-1 text-sm-body-2 text-body-2 text-center my-5"
        id="text"
      >
        <viewer
          v-if="article.content"
          id="markdown-viewer"
          :initialValue="article.content"
          :value="viewerText"
          height="500px"
        />
      </div>
    </div>
    <Comment :articleId="articleId" />
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
      bigCategory: "",
      middleCategory: "",
      smallCategory: "",
      followOrUnfollow: "mdi-account-plus",
      index: 0,
      snackbar: false,
      text: "",
      timeout: 5000,
      x: null,
      y: "top",
      mode: "",
      writerProfile: `${require("@/assets/profile.svg")}`,
    };
  },
  components: {
    Comment,
    viewer: Viewer,
  },
  props: ["articleId"],
  async created() {
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
            axios
              .get(process.env.VUE_APP_LIKE + `userlike/${this.user.id}`, {
                headers: {
                  "jwt-auth-token": this.jwtAuthToken,
                },
              })
              .then((res) => (this.userLiked = res.data.data));
          }
        });
    }

    await axios.get(process.env.VUE_APP_ARTICLE + "visit/" + this.articleId);

    const response = await axios
      .get(process.env.VUE_APP_ARTICLE + "detail/" + this.articleId)
      .catch((error) => {
        console.log(error);
      });

    this.article = response.data.data;
    this.categoryInt = this.article.category;
    this.writerProfile =
      process.env.VUE_APP_ACCOUNT +
      "downloadFile/" +
      this.article.writerid +
      ".jpg";

    if (this.categoryInt != 1 && this.categoryInt != 2) {
      const bigCategoryIndex = parseInt(String(this.categoryInt)[0]) - 1;
      const middleCategoryIndex = parseInt(String(this.categoryInt)[1]) - 1;
      const smallCategoryIndex = parseInt(String(this.categoryInt)[2]) - 1;

      const bigCategories = this.$store.state.bigCategories;
      const middleCategories = this.$store.state.middleCategories[
        bigCategoryIndex
      ];
      const smallCategories = this.$store.state.smallCategories[
        bigCategoryIndex
      ][middleCategoryIndex];

      this.bigCategory = bigCategories[bigCategoryIndex];
      this.middleCategory = middleCategories[middleCategoryIndex];
      this.smallCategory = smallCategories[smallCategoryIndex].name;
    }
    this.followingList.forEach((element, index) => {
      if (element.id == this.article.writerid) {
        this.followOrUnfollow = "mdi-account-remove";
        this.index = index;
      }
    });

    axios
      .get(process.env.VUE_APP_TAG + "taglist/" + this.articleId)
      .then((res) => {
        let tagData = res.data.data;
        this.tags = tagData;
      });
  },

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

    updateArticleId: {
      get() {
        return this.$store.getters.updateArticleId;
      },
      set(value) {
        this.$store.dispatch("setUpdateArticleId", value);
      },
    },
  },
  methods: {
    goProfile(writerid) {
      this.$router.push("/user/profile/" + writerid);
    },
    imgError() {
      //console.log("err");
      this.writerProfile = `${require("@/assets/profile.svg")}`;
    },
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
      this.updateArticleId = this.articleId;

      this.$router.push({
        name: "Update",
        props: { articleId: this.articleId },
      });
    },
    deleteArticle() {
      axios
        .delete(process.env.VUE_APP_ARTICLE + "delete/" + this.articleId, {
          data: { articleid: this.articleId },
          headers: {
            "jwt-auth-token": this.jwtAuthToken,
          },
        })
        .then((res) => {
          if (res.status === 200) {
            this.text = "게시글 삭제에 성공했습니다!";
            this.snackbar = true;

            setTimeout(() => {
              this.$router.push({ name: "Home" });
            }, 1000);
          }
        });
    },
    checkLiked(article) {
      if (this.loggedIn !== null)
        return this.userLiked.includes(article.articleid);
      else return false;
    },

    changeLiked(article) {
      if (this.loggedIn !== null) {
        if (!this.checkLiked(article)) {
          axios
            .post(
              process.env.VUE_APP_LIKE + "regist",
              {
                userid: this.userId,
                articleid: article.articleid,
              },
              {
                headers: {
                  "jwt-auth-token": this.jwtAuthToken,
                },
              }
            )
            .then((res) => {
              article.likecount = res.data.data;
              axios
                .get(process.env.VUE_APP_LIKE + `userlike/${this.user.id}`, {
                  headers: {
                    "jwt-auth-token": this.jwtAuthToken,
                  },
                })
                .then((res) => {
                  this.userLiked = res.data.data;
                });
            });
        } else {
          axios
            .delete(
              `${process.env.VUE_APP_LIKE}delete/${this.user.id}/${article.articleid}`,
              {
                data: { userid: this.userId, articleid: article.articleid },
                headers: {
                  "jwt-auth-token": this.jwtAuthToken,
                },
              }
            )
            .then((res) => {
              article.likecount = res.data.data;
              axios
                .get(process.env.VUE_APP_LIKE + `userlike/${this.userId}`, {
                  headers: {
                    "jwt-auth-token": this.jwtAuthToken,
                  },
                })
                .then((res) => {
                  this.userLiked = res.data.data;
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

#markdown-viewer {
  font-size: 18px !important;
  border: 1px solid #b3b3b3;
  border-radius: 4px;
  margin: auto;
  width: 90%;
  background-color: white;
}
</style>
