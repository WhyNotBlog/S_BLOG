<template>
  <carousel-3d>
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
          <v-card>
            <v-img
              contain
              class="white--text align-end"
              height="134px"
              style="margin-top:16px;margin-left:16px;margin-right:16px; "
              :src="imgSrc(article.articleid, article.thumbnail)"
            ></v-img>

            <v-card-title class="card-title justify-center">
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
                <b>{{ article.editdate }}</b>
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
  },
};
</script>

<style></style>
