<template>
  <div>
    <br />
    <v-container>
      <v-layout justify-space-between id="category">
        <v-flex sm4 md4>
          <v-select
            id="selectedBigCategory"
            :items="bigCategories"
            label="대분류"
            color="secondary"
            outlined
            v-model="bigCategory"
            @change="changeBigCategory"
          ></v-select>
        </v-flex>

        <v-flex sm4 md4>
          <v-select
            id="selectedMiddleCategory"
            :items="middleCategories"
            label="중분류"
            color="secondary"
            outlined
            v-model="middleCategory"
            @change="changeMiddleCategory"
          ></v-select>
        </v-flex>

        <v-flex sm4 md4>
          <v-select
            id="selectedSmallCategory"
            :items="smallCategories"
            item-text="name"
            item-value="value"
            label="소분류"
            color="secondary"
            outlined
            v-model="smallCategory"
            @change="changeSmallCategory"
          ></v-select>
        </v-flex>
      </v-layout>
    </v-container>

    <PostView :data="this.articles" />
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
  watch: {
    $route() {
      this.articles = new Array();
      this.page = 0;
      this.$refs.infiniteLoading.stateChanger.reset();
      this.categoryInt = this.$route.params.category;
      this.categorySelect();
    },
  },
  components: { PostView, InfiniteLoading },
  data() {
    return {
      bigCategories: new Array(),
      middleCategories: new Array(),
      smallCategories: new Array(),
      bigCategory: new String(),
      middleCategory: new String(),
      smallCategory: new Object(),
      page: 0,
      articles: new Array(),
      categoryInt: 0,
    };
  },
  props: ["category"],

  mounted() {
    this.categoryInt = this.category;
    this.categorySelect();
  },

  methods: {
    categorySelect() {
      const bigCategoryIndex = parseInt(String(this.categoryInt)[0]) - 1;
      const middleCategoryIndex = parseInt(String(this.categoryInt)[1]) - 1;
      const smallCategoryIndex = parseInt(String(this.categoryInt)[2]) - 1;
      this.bigCategories = this.$store.state.bigCategories;
      this.middleCategories = this.$store.state.middleCategories[
        bigCategoryIndex
      ];
      this.smallCategories = this.$store.state.smallCategories[
        bigCategoryIndex
      ][middleCategoryIndex];

      this.bigCategory = this.bigCategories[bigCategoryIndex];
      this.middleCategory = this.middleCategories[middleCategoryIndex];
      this.smallCategory = this.smallCategories[smallCategoryIndex];
    },

    infiniteHandler($state) {
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

    changeBigCategory() {
      let categoryIndexBig = this.bigCategories.indexOf(this.bigCategory);
      this.middleCategories = this.$store.state.middleCategories[
        categoryIndexBig
      ];
      this.middleCategory = this.middleCategories[0];
      this.smallCategories = this.$store.state.smallCategories[
        categoryIndexBig
      ][0];
      this.smallCategory = this.smallCategories[0].value;
      this.categoryInt = this.smallCategory;
      //console.log(this.categoryInt);

      this.$router.push("/category/" + this.categoryInt);
    },
    changeMiddleCategory() {
      let categoryIndexBig = this.bigCategories.indexOf(this.bigCategory);
      let categoryIndexMiddle = this.middleCategories.indexOf(
        this.middleCategory
      );
      this.smallCategories = this.$store.state.smallCategories[
        categoryIndexBig
      ][categoryIndexMiddle];
      this.smallCategory = this.smallCategories[0].value;
      this.categoryInt = this.smallCategory;
      //console.log(this.categoryInt);
      this.$router.push("/category/" + this.categoryInt);
    },
    changeSmallCategory() {
      this.categoryInt = this.smallCategory;
      //console.log(this.categoryInt);
      this.$router.push("/category/" + this.categoryInt);
    },
  },
};
</script>

<style scoped>
.v-select {
  margin-left: 15px;
}
</style>
