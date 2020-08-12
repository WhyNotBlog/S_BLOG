<template>
  <v-layout row justify-start style="margin:auto">
    <v-flex
      v-for="tempArticle in tempArticles"
      :key="tempArticle.articleid"
      xl3
      lg4
      md6
      sm6
      xs12
    >
      <div class="content">
        <v-card class="d-inline-block my-3" :min-width="moblieWidth">
          <v-card-title
            @click="moveToTempArticle(tempArticle)"
            class="card-title justify-center"
            >{{ tempArticle.title }}</v-card-title
          >

          <v-card-text class="card-text text--primary">
            <div class="text-center">
              {{ tempArticle.content.slice(0, 20)
              }}{{ tempArticle.content.length > 20 ? "..." : "" }}
            </div>
          </v-card-text>
          <v-footer class="d-flex justify-space-around">
            <div>
              <b>{{ tempArticle.editdate | dateToString }}</b>
            </div>
            <div>
              <b>|</b>
            </div>
            <div>
              <b>{{ tempArticle.editornickname }}</b>
            </div>
          </v-footer>
        </v-card>
      </div>
    </v-flex>
  </v-layout>
</template>

<script>
import axios from "axios";
import { mapActions } from "vuex";

export default {
  name: "TempList",
  data() {
    return {
      user: new Object(),
      tempArticles: new Object(),
    };
  },
  created() {
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
            .get(process.env.VUE_APP_ARTICLETEMP + "user/" + this.user.id, {
              headers: {
                "jwt-auth-token": this.jwtAuthToken,
              },
            })
            .then((res) => {
              this.tempArticles = res.data.data;
            });
        }
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
    moblieWidth() {
      return window.innerWidth <= 500 ? "220px" : "275px";
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
    ...mapActions(["setCurrentTempArticle"]),
    moveToTempArticle(tempArticle) {
      this.setCurrentTempArticle(tempArticle);
      this.$router.push({
        name: "TempArticle",
        params: { articleId: tempArticle.articleid },
      });
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
