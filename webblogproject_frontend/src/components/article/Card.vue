<template>
  <carousel-3d class="card" :display="6">
    <slide v-for="(article, i) in articles" :index="i" :key="i">
      <template slot-scope="{ index, isCurrent, leftIndex, rightIndex }">
        <div
          :data-index="index"
          :class="{
            current: isCurrent,
            onLeft: leftIndex >= 0,
            onRight: rightIndex >= 0,
          }"
        >
          <v-card class="content" @click="goPost(isCurrent,article.articleid)">
            <v-img
              contain
              class="white--text align-end"
              height="135px"
              style="margin:15px"
              :src="imgSrc(article.articleid, article.thumbnail)"
            ></v-img>

            <v-card-title class="card-title justify-center">
              {{ article.title.slice(0, 10)
              }}{{ article.title.length > 10 ? "..." : "" }}
            </v-card-title>

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
              <div>
                <b>|</b>
              </div>
              <div>
                <b>조회수 : {{ article.hits }}</b>
              </div>
            </v-footer>
          </v-card>
        </div>
      </template>
    </slide>
  </carousel-3d>
</template>

<script>
import { Carousel3d, Slide } from "vue-carousel-3d";
export default {
  components: {
    Carousel3d,
    Slide,
  },
  props: { data: Array },
  computed: {
    articles() {
      return this.data;
    },
  },

  methods: {
    imgSrc(id, flag) {
      return flag
        ? process.env.VUE_APP_ARTICLE + "downloadThumbnail/" + id + ".jpg"
        : require("@/assets/basic.jpg");
    },
    goPost(isCurrent, id) {
      if (isCurrent) {
        this.$router.push("/article/detail/" + id);
      }
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
};
</script>

<style scoped>
.carousel-3d-slide {
  height: 265px !important;
  overflow: visible;
  border: none;
  background: white;
  border-radius: 4px;
  box-shadow: 0px 3px 1px -2px rgba(0, 0, 0, 0.2),
    0px 2px 2px 0px rgba(0, 0, 0, 0.14), 0px 1px 5px 0px rgba(0, 0, 0, 0.12);
}

.card:hover {
  cursor: pointer;
}

.v-sheet.v-card:not(.v-sheet--outlined) {
  box-shadow: none;
}
.v-sheet.v-card {
  border-radius: 4px;
}

@media screen and (max-width: 630px) {
  .carousel-3d-slide {
    width: 270px !important;
  }

  .carousel-3d-container {
    height: 270px !important;
  }
}
</style>
