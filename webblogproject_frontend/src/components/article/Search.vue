<template>
  <div>
    <br />
    <v-layout row wrap justify-center>
      <v-flex xs12 sm12 style="display:flex; justify-content:center">
        <v-text-field
          id="search"
          name="search"
          type="text"
          color="black"
          placeholder="검색어를 입력하세요"
          v-model="search"
          @change="searchContent"
          solo
          clearable
          single-line
        ></v-text-field>
      </v-flex>
    </v-layout>
    <span v-show="isSearch">
      총
      <strong>{{count}}</strong>개의 게시물이 있습니다!
    </span>
    <PostView :data="this.articles" />
  </div>
</template>

<script>
import axios from "axios";
import PostView from "@/components/PostView";

export default {
  data() {
    return {
      search: "",
      articles: new Array(),
      count: 0,
      isSearch: false,
    };
  },
  components: { PostView },
  methods: {
    searchContent() {
      //console.log(this.search);
      axios
        .get(process.env.VUE_APP_ARTICLE + "searchBy/title/" + this.search)
        .then((res) => {
          //console.log(res);
          this.isSearch = true;
          this.count = res.data.data.length;
          this.articles = res.data.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

<style scoped>
.v-text-field {
  max-width: 95%;
}
</style>
