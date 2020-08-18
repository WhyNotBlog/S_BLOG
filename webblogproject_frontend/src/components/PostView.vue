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

    <v-layout row justify-start style="margin:auto">
      <v-flex
        v-for="(article) in articles"
        :key="article.articleid"
        v-show="article.category!=1 && article.category!=2"
        xl3
        lg4
        md6
        sm6
        xs12
      >
        <!-- <v-hover>
        <template v-slot:default="{ hover }">-->
        <div class="content">
          <v-card class="d-inline-block my-3" :width="moblieWidth" min-width="270px">
            <a @click="moveToArticle(article)">
              <div style="background-color:white">
                <v-img
                  contain
                  class="white--text align-end"
                  height="168px"
                  style="margin-top:16px;margin-left:16px;margin-right:16px; "
                  :src="imgSrc(article.articleid, article.thumbnail)"
                ></v-img>
              </div>
              <v-card-title
                class="card-title justify-center"
              >{{ article.title ? (article.title.length >10? article.title.slice(0, 10)+"...":article.title.slice(0, 10)) : "제목없음" }}</v-card-title>
            </a>

            <v-footer class="d-flex justify-space-around">
              <div style="font-family: Jua;">{{ article.editdate | dateToString }}</div>

              <div style="font-family: Jua;">조회수 : {{ article.hits }}</div>
            </v-footer>

            <v-card-actions>
              <div style="margin-left:8px">
                <v-icon style="margin-right:8px;color:black">face</v-icon>
                <span
                  style="font-family:Nanum Gothic Coding;font-size:17px"
                >{{ article.editornickname }}</span>
              </div>
              <v-spacer></v-spacer>
              <v-btn
                color="red accent-4"
                icon
                v-if="checkLiked(article)"
                @click.stop="changeLiked(article)"
              >
                <v-icon middle color="red accent-4" icon>mdi-heart</v-icon>
                <span class="like">{{ article.likecount }}</span>
              </v-btn>
              <v-btn color="red accent-4" icon v-else @click.stop="changeLiked(article)">
                <v-icon middle color="red accent-4">mdi-heart-outline</v-icon>
                <span class="like">{{ article.likecount }}</span>
              </v-btn>

              <v-btn color="orange" icon @click.stop="copyLink(article)" style="margin-right:8px">
                <v-icon middle color>mdi-share</v-icon>
              </v-btn>
            </v-card-actions>
            <!-- 
                <v-fade-transition>
                  <v-overlay v-if="hover" absolute color="#595959"></v-overlay>
            </v-fade-transition>-->
          </v-card>
        </div>
        <!-- </template>
        </v-hover>-->
      </v-flex>
    </v-layout>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      likeArticleCount: new Object(),
      user: new Object(),
      userLike: null,
      userLiked: new Array(),
      failImg: new Array(),
      snackbar: false,
      text: "",
      timeout: 5000,
      x: null,
      y: "top",
      mode: "",
    };
  },
  created() {
    this.updateTotalLike();
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
  },
  mounted() {
    this.updateTotalLike();
  },
  updated() {
    this.updateTotalLike();
  },
  props: { data: Array },
  computed: {
    articleIdList() {
      return this.data.map((article) => article.articleid);
    },
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

    articles() {
      //console.log(this.data);
      return this.data;
    },
    moblieWidth() {
      return window.innerWidth <= 500 ? "220px" : "280px";
    },
  },
  filters: {
    dateToString(date) {
      try {
        return (
          date.slice(0, 4) +
          "년 " +
          date.slice(5, 7) +
          "월 " +
          date.slice(8, 10) +
          "일"
        );
      } catch (e) {
        console.log("");
      }
    },
  },
  methods: {
    imgSrc(id, flag) {
      //console.log(flag);

      return flag
        ? process.env.VUE_APP_ARTICLE + "downloadThumbnail/" + id + ".jpg"
        : require("@/assets/basic.jpg");
    },

    updateTotalLike() {
      this.articleIdList.forEach((articleId) => {
        axios
          .post(
            process.env.VUE_APP_LIKE + "articlelist",
            {
              articleid: String(articleId),
            },
            {
              headers: {
                "jwt-auth-token": this.jwtAuthToken,
              },
            }
          )
          .then((res) => {
            this.likeArticleCount[articleId] = res.data.data[articleId];
          });
      });
    },
    copyLink(article) {
      const copyURL = document.createElement("input");
      copyURL.value = document.URL + `article/detail/${article.articleid}`;
      document.body.appendChild(copyURL);
      copyURL.select();
      document.execCommand("copy");
      document.body.removeChild(copyURL);

      this.text = "링크가 복사되었습니다!";
      this.snackbar = true;
    },

    moveToArticle(article) {
      this.$router.push({
        name: "Article",
        params: { articleId: article.articleid },
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
              console.log(res);
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
        } else {
          axios
            .delete(
              process.env.VUE_APP_LIKE +
                `delete/${this.userId}/${article.articleid}`,
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
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css?family=Jua:400");
@import url("https://fonts.googleapis.com/css?family=Nanum+Gothic+Coding:700");
.content {
  width: calc(100% - 2rem);
  transition-duration: 0.6s;
  margin: auto;
  display: flex;
  justify-content: center;
}

.content:hover {
  transform: translateY(-8px);
  box-shadow: rgba(0, 0, 0, 0.08);
}
.v-application a {
  color: black;
}

.like {
  font-family: Jua;
}
</style>
