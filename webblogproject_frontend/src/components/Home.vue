<template>
  <div>
    <v-tabs
      v-model="active"
      background-color="#f1f3f5"
      color="black"
      style="margin-left:10px;margin-top:10px;margin-right:5px"
    >
      <v-tab>
        <v-icon style="margin-right:5px">trending_up</v-icon>트랜딩
      </v-tab>
      <v-tab>
        <v-icon style="margin-right:5px">schedule</v-icon>최근 게시물
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
        <PostView :data="this.articles2" />
      </v-tab-item>

      <v-tab-item class="tab">
        <br />
        <PostView :data="this.articles3" />
      </v-tab-item>
    </v-tabs>

    <infinite-loading @infinite="infiniteHandler" spinner="waveDots">
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
      articles2: new Array(),
      articles3: new Array(),
      page: 0,
      active: 0,
    };
  },

  components: { PostView, InfiniteLoading },
  // methods: {
  // changeLiked(id) {
  //   // axios.post 추가
  //   this.articles.filter((article) => {
  //     if (article.id === id) {
  //       article.isLiked = !article.isLiked;
  //     }
  //   });
  // },
  // addArticles() {
  //   axios.get()
  //   .then((res) => this.articles.push())
  //   .catch(e => console.log(e));
  // },
  //},

  methods: {
    infiniteHandler($state) {
      setTimeout(() => {
        axios
          .get(
            process.env.VUE_APP_ARTICLE + "searchBy/allarticle/1/" + this.page
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

        axios
          .get(
            process.env.VUE_APP_ARTICLE + "searchBy/allarticle/0/" + this.page
          )
          .then((res) => {
            //console.log(res.data.data);
            if (!res.data.data.empty) {
              this.page += 1;
              this.articles2.push(...res.data.data.content);
              $state.loaded();
            } else {
              $state.complete();
            }
          });

        axios
          .get(
            process.env.VUE_APP_ARTICLE + "searchBy/allarticle/2/" + this.page
          )
          .then((res) => {
            //console.log(res.data.data);
            if (!res.data.data.empty) {
              this.page += 1;
              this.articles3.push(...res.data.data.content);
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
</style>