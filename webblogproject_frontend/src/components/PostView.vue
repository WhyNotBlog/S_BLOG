<template>
  <v-layout row justify-start style="margin:auto">
    <v-flex v-for="article in articles" :key="article.articleid" xl3 lg4 md6 sm6 xs12>
      <div class="content">
        <v-card class="d-inline-block my-3" :min-width="moblieWidth">
          <v-img class="white--text align-end" height="168px" src="@/assets/basic.jpg"></v-img>
          <v-card-title @click="moveToArticle(article)" class="card-title justify-center">
            {{ article.title.slice(0, 10)
            }}{{ article.title.length > 10 ? "..." : "" }}
          </v-card-title>

          <v-card-text class="card-text text--primary">
            <div class="text-center">
              {{ article.content.slice(0, 20)
              }}{{ article.content.length > 20 ? "..." : "" }}
            </div>
          </v-card-text>
          <v-footer class="d-flex justify-space-around">
            <div>
              <b>{{ article.editdate | dateToString }}</b>
            </div>
            <div>
              <b>|</b>
            </div>
            <div>
              <b>{{ article.editornickname }}</b>
            </div>
          </v-footer>

          <v-card-actions class="justify-space-around">
            <v-btn color="orange" icon @click="copyLink(article.articleid)">
              <v-icon middle color>mdi-share</v-icon>
            </v-btn>
            <v-btn
              color="red accent-4"
              icon
              v-if="article.isLiked"
              @click="changeLiked(article.articleid)"
            >
              <v-icon middle color="red accent-4">mdi-heart</v-icon>
            </v-btn>
            <v-btn color="red accent-4" icon v-else @click="changeLiked(article.articleid)">
              <v-icon middle color="red accent-4">mdi-heart-outline</v-icon>
            </v-btn>
          </v-card-actions>
        </v-card>
      </div>
    </v-flex>
  </v-layout>
</template>

<script>
import axios from 'axios';
import { mapActions } from "vuex";
export default {
  data() {
    return {
      articleList : null,
      user : new Object(),
      userLiked : new Array(),
    }
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
            axios.get(process.env.VUE_APP_LIKE + `userlike/${this.user.id}`)
            .then(res => this.userLiked = res.data.data)
            .catch(e => console.log(e))
            }
            })
    }
  },
  props: { data: Array },
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
    articles() {
      return this.data;
    },
    moblieWidth() {
      return window.innerWidth <= 500 ? "220px" : "270px";
    },
  },
  filters: {
    dateToString(date) {
      try {
        return date.slice(0, 10);
      } catch (e) {
        console.log("");
      }
    },
  },
  methods: {
    copyLink(id) {
      const copyURL = document.createElement("input");
      copyURL.value = document.URL + `article/detail/${id}`;
      document.body.appendChild(copyURL);
      copyURL.select();
      document.execCommand("copy");
      document.body.removeChild(copyURL);
      alert("링크가 복사되었습니다!");
    },

    ...mapActions(["setCurrentArticle"]),

    moveToArticle(article) {
      this.setCurrentArticle(article);
      this.$router.push({
        name: "Article",
        params: { articleId: article.articleid },
      });
    },

    changeLiked(id) {
      console.log(id)
    },
  },
};
</script>

<style scoped>
.content {
  width: calc(100% - 2rem);
  transition-duration: 0.6s;
}

.content:hover {
  transform: translate3d(-4px, -4px, -4px);
  box-shadow: rgba(0, 0, 0, 0.08);
}
</style>