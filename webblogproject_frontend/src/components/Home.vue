<template>
  <div>
    <v-tabs
      v-model="active"
      background-color="#f1f3f5"
      color="black"
      style="margin-left:5px;margin-top:10px;margin-right:5px"
      @change="changeEvent($event)"
    >
      <v-tab>
        <v-icon style="margin-right:5px">trending_up</v-icon>트랜딩
      </v-tab>
      <v-tab>
        <v-icon style="margin-right:5px">schedule</v-icon>최근
      </v-tab>
      <v-tab>
        <v-icon style="margin-right:5px">favorite</v-icon>좋아요
      </v-tab>

      <v-tab-item class="tab">
        <br />
        <PostView :data="this.articles" />
      </v-tab-item>

      <v-tab-item class="tab">
        <br />
        <PostView :data="this.articles" />
      </v-tab-item>

      <v-tab-item class="tab">
        <br />
        <PostView :data="this.articles" />
      </v-tab-item>
    </v-tabs>

    <infinite-loading ref="infiniteLoading" @infinite="infiniteHandler" spinner="waveDots">
      <div slot="no-more"></div>
      <div slot="no-results">
        <div class="no_result">
          <span>조회 결과가 없습니다.</span>
        </div>
      </div>
    </infinite-loading>
  </div>
</template>

<script src="https://unpkg.com/vue-infinite-loading@2.4.4/dist/vue-infinite-loading.js"></script>

<script>
import axios from "axios";
import PostView from "@/components/PostView";
import InfiniteLoading from "vue-infinite-loading";

const api = "http://hn.algolia.com/api/v1/search_by_date?tags=story";
export default {
  name: "Home",
  data() {
    return {
      articles: new Array(),
      page: 0,
      active: 0,
    };
  },

  components: { PostView, InfiniteLoading },

  methods: {
    changeEvent() {
      //console.log(this.active);
      this.page = 0;
      this.articles = new Array();
      this.$refs.infiniteLoading.stateChanger.reset();
    },
    infiniteHandler($state) {
      setTimeout(() => {
        axios
          .get(
            process.env.VUE_APP_ARTICLE +
              "searchBy/allarticle/" +
              this.active +
              "/" +
              this.page
          )
          .then((res) => {
            //console.log(res.data.data);
            if (!res.data.data.empty) {
              this.page += 1;
              this.articles.push(...res.data.data.content);
              $state.loaded();
            } else {
              $state.complete();
            }
          });
      }, 200);
    },
  },
};
</script>

<style>
.tab {
  background-color: #f1f3f5;
}

@media screen and (max-width: 400px) {
  .v-tabs:not(.v-tabs--vertical).v-tabs--right
    > .v-slide-group--is-overflowing.v-tabs-bar--is-mobile:not(.v-slide-group--has-affixes)
    .v-slide-group__next,
  .v-tabs:not(.v-tabs--vertical):not(.v-tabs--right)
    > .v-slide-group--is-overflowing.v-tabs-bar--is-mobile:not(.v-slide-group--has-affixes)
    .v-slide-group__prev,
  .v-tabs:not(.v-tabs--vertical):not(.v-tabs--right)
    > .v-slide-group--is-overflowing.v-tabs-bar--is-mobile:not(.v-slide-group--has-affixes)
    .v-slide-group__prev {
    display: none !important;
  }
}
</style>