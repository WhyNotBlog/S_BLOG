<template>
  <div>
    <br />
    <v-layout row wrap justify-center>
      <v-flex xs3 sm3 md3 lg3 xl3>
        <v-select :items="type" label="유형" solo v-model="typeBox"></v-select>
      </v-flex>
      <v-flex xs8 sm8 md8 lg8 xl8>
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
  created() {
    if (this.tagKey != "blank") {
      this.typeBox = "태그";
      this.search = this.tagKey;
      this.searchContent();
    }
  },

  watch: {
    tag() {
      this.typeBox = "태그";
      this.search = this.tagKey;
      this.searchContent();
    },
  },

  computed: {
    tag() {
      return this.tagKey;
    },
  },
  data() {
    return {
      search: "",
      articles: new Array(),
      count: 0,
      isSearch: false,
      isTag: false,
      typeBox: "제목",
      type: ["제목", "닉네임", "태그"],
    };
  },

  components: { PostView },
  props: ["tagKey"],
  methods: {
    searchContent() {
      //console.log(this.search);
      if (this.typeBox == "제목") {
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
      } else if (this.typeBox == "닉네임") {
        axios
          .get(process.env.VUE_APP_ARTICLE + "searchBy/nickname/" + this.search)
          .then((res) => {
            //console.log(res);
            this.isSearch = true;
            this.count = res.data.data.length;
            this.articles = res.data.data;
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        axios
          .get(process.env.VUE_APP_ARTICLE + "searchby/tag/" + this.search)
          .then((res) => {
            //console.log(res);
            this.isSearch = true;
            this.count = res.data.data.length;
            this.articles = res.data.data;
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
  },
};
</script>

<style scoped>
.v-text-field {
  max-width: 95%;
}
</style>
