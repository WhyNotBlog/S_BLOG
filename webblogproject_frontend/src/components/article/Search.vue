<template>
  <div>
    <br />
    <h1>검색</h1>
    <v-container>
      <v-layout row wrap justify-center>
        <v-flex xs3 sm3 md3 lg3 xl3>
          <v-select :items="types" label="유형" solo v-model="typeBox" style="margin-left:5px"></v-select>
        </v-flex>
        <v-flex xs8 sm8 md8 lg8 xl8>
          <v-text-field
            id="search"
            name="search"
            type="text"
            color="black"
            placeholder="검색어를 입력하세요"
            v-model="search"
            @keyup.enter="searchRoute"
            style="margin-left:5px"
            solo
            clearable
            single-line
          ></v-text-field>
        </v-flex>
      </v-layout>

      <div>
        <span v-show="isSearch" style="width:95%;margin-left:5%;font-size:18px">
          총
          <strong>{{ count }}</strong>개의 게시물이 있습니다!
        </span>
        <SearchView :data="this.articles" />
      </div>
      <infinite-loading
        ref="infiniteLoading"
        @infinite="infiniteHandler"
        spinner="waveDots"
        v-show="infinite"
      >
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
import axios from "axios";
import SearchView from "@/components/SearchView";
import InfiniteLoading from "vue-infinite-loading";

const api = "http://hn.algolia.com/api/v1/search_by_date?tags=story";

export default {
  watch: {
    $route() {
      this.infinite = true;
      this.isSearch = false;
      this.articles = new Array();
      this.page = 0;
      this.$refs.infiniteLoading.stateChanger.reset();
      //console.log(this.$route.params);
      this.type2 = this.$route.params.type;
      this.word2 = this.$route.params.word;
      this.search = this.$route.params.word;
      if (this.type == "title") {
        this.typeBox = "제목";
      } else if (this.type == "nickname") {
        this.typeBox = "닉네임";
      } else {
        this.typeBox = "태그";
      }
    },
  },
  data() {
    return {
      type2: "",
      word2: "",
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
    }
  },
  mounted() {
    this.type2 = this.type;
    this.word2 = this.word;
  },
  components: { SearchView, InfiniteLoading },
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
              "searchBy/" +
              this.type2 +
              "/" +
              this.word2 +
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
            this.isSearch = true;
            this.articles.push(...res.data.data.content);
            $state.loaded();
          });
      }, 200);
    },
  },
};
</script>

<style scoped>
.v-text-field {
  width: 95%;
}
</style>
