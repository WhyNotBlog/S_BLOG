<template>
  <div>
    <br />
    <h1 v-if="categoryInt==1" style="width:95%;margin-left:5%">직무 소개</h1>
    <h1 v-else style="width:95%;margin-left:5%">로드맵</h1>
    <v-container>
      <SearchView :data="this.articles" />

      <infinite-loading ref="infiniteLoading" @infinite="infiniteHandler" spinner="waveDots">
        <div slot="no-more"></div>
        <div slot="no-results">
          <div class="no_result">
            <span>조회 결과가 없습니다.</span>
          </div>
        </div>
      </infinite-loading>
    </v-container>
  </div>
</template>
<script src="https://unpkg.com/vue-infinite-loading@2.4.4/dist/vue-infinite-loading.js"></script>


<script>
import SearchView from "@/components/SearchView";
import InfiniteLoading from "vue-infinite-loading";
import axios from "axios";
export default {
  watch: {
    $route() {
      this.articles = new Array();
      this.page = 0;
      this.$refs.infiniteLoading.stateChanger.reset();
      //console.log(this.$route.params);
      if (this.$route.params.category == "roadmap") this.categoryInt = 2;
      else this.categoryInt = 1;
    },
  },
  components: { SearchView, InfiniteLoading },
  props: ["category"],
  created() {
    if (this.category == "roadmap") this.categoryInt = 2;
    else this.categoryInt = 1;
  },
  data() {
    return {
      text: "",
      categoryInt: 1,
      page: 0,
      articles: new Array(),
    };
  },
  methods: {
    infiniteHandler($state) {
      if (this.search == "") {
        this.infinite = false;
        return;
      }
      setTimeout(() => {
        axios
          .get(
            process.env.VUE_APP_ARTICLE +
              "searchBy/category/" +
              this.categoryInt +
              "/" +
              this.page
          )
          .then((res) => {
            this.count = res.data.data.totalElements;
            //console.log(res.data.data);
            if (res.data.data.content == null || res.data.data.empty) {
              $state.complete();
              return;
            }

            this.page += 1;
            this.articles.push(...res.data.data.content);
            $state.loaded();
          });
      }, 200);
    },
  },
};
</script>

<style>
</style>