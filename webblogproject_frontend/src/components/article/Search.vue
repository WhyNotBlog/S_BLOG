<template>
  <div>
    <br />
    <v-layout row wrap justify-center>
      <v-flex xs3 sm3 md3 lg3 xl3>
        <v-select :items="types" label="유형" solo v-model="typeBox"></v-select>
      </v-flex>
      <v-flex xs8 sm8 md8 lg8 xl8>
        <v-text-field
          id="search"
          name="search"
          type="text"
          color="black"
          placeholder="검색어를 입력하세요"
          v-model="search"
          @change="searchRoute"
          solo
          clearable
          single-line
        ></v-text-field>
      </v-flex>
    </v-layout>

    <span v-show="isSearch">
      총
      <strong>{{ count }}</strong
      >개의 게시물이 있습니다!
    </span>
    <PostView :data="this.articles" />

    <infinite-loading
      @infinite="infiniteHandler"
      spinner="waveDots"
      v-show="infinite"
    >
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
  data() {
    return {
      infinite: true,
      search: "",
      articles: new Array(),
      count: 0,
      isSearch: false,
      typeBox: "제목",
      typeEn: "",
      page: 0,
      order: "",
      types: ["제목", "닉네임", "태그"],
    };
  },
  created() {
    if (this.type == "title") {
      this.typeBox = "제목";
    } else if (this.type == "nickname") {
      this.typeBox = "닉네임";
    } else {
      this.typeBox = "태그";
    }
    if (this.word == "blank") this.search = "";
    else {
      this.search = this.word;
      //console.log(this.search);
      if (this.typeBox == "제목") {
        this.order = "searchBy/title/";
      } else if (this.typeBox == "닉네임") {
        this.order = "searchBy/nickname/";
      } else {
        this.order = "searchby/tag/";
      }
    }
  },
  components: { PostView, InfiniteLoading },
  props: ["type", "word"],
  methods: {
    searchRoute() {
      if (this.search == "") return;

      if (this.typeBox == "제목") {
        this.typeEn = "title";
      } else if (this.typeBox == "닉네임") {
        this.typeEn = "nickname";
      } else {
        this.typeEn = "tag";
      }
      this.$router.push("/search/" + this.typeEn + "/" + this.search);
    },

    infiniteHandler($state) {
      if (this.search == "") {
        this.infinite = false;
        return;
      }
      setTimeout(() => {
        axios
          .get(
            process.env.VUE_APP_ARTICLE +
              this.order +
              this.search +
              "/" +
              this.page
          )
          .then((res) => {
            this.count = res.data.data.totalElements;
            // console.log(res.data.data);
            if (!res.data.data.empty) {
              this.page += 1;
              this.isSearch = true;

              this.articles.push(...res.data.data.content);
              $state.loaded();
            } else {
              $state.complete();
            }
          });
      }, 100);
    },
  },
};
</script>

<style scoped>
.v-text-field {
  max-width: 95%;
}
</style>
