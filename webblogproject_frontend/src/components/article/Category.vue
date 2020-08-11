<template>
  <div>
    <PostView :data="this.articles" />
    <infinite-loading
      ref="InfiniteLoading"
      @infinite="infiniteHandler"
      spinner="waveDots"
    >
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
  watch: {
    $route() {
      this.articles = new Array();
      this.page = 0;
      this.$refs.infiniteLoading.$emit("$InfiniteLoading:reset");
      this.categoryNum = this.$route.params.category;
    },
  },
  components: { PostView, InfiniteLoading },
  data() {
    return {
      page: 0,
      articles: new Array(),
    };
  },
  props: ["category"],
  computed: {
    categoryNum() {
      return this.category;
    },
  },
  methods: {
    infiniteHandler($state) {
      setTimeout(() => {
        axios
          .get(
            process.env.VUE_APP_ARTICLE +
              "searchBy/category/" +
              this.categoryNum +
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

<style></style>
