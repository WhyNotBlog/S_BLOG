<template>
  <div>
    <PostView :data="this.articles" />

    <infinite-loading @infinite="infiniteHandler">
      <div slot="no-more">마지막 글입니다.</div>
      <div slot="no-results">
        <div class="no_result">
          <div class="icon_img"></div>
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
  created() {},

  methods: {
    infiniteHandler($state) {
      setTimeout(() => {
        axios
          .get(process.env.VUE_APP_ARTICLE + "searchBy/allarticle/" + this.page)
          .then((res) => {
            console.log(res.data.data.content);
            if (res.data.data.content.length) {
              this.page += 1;
              this.articles.push(...res.data.data.content);
              $state.loaded();
            } else {
              $state.complete();
            }
          });
      }, 500);
    },
  },
};
</script>
