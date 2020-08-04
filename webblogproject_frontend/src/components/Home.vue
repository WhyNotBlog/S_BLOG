<template>
  <div>
    <PostView :data="this.articles" />

    <v-btn class="align-self-center" color="purple" text width="5%">
      <b>더 보기</b>
    </v-btn>
  </div>
</template>

<script>
import axios from "axios";
import PostView from "@/components/PostView";

export default {
  name: "Home",
  data() {
    return {
      articles: new Array(),
    };
  },
  components: { PostView },
  methods: {
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
  },
  created() {
    axios
      .get(process.env.VUE_APP_ARTICLE + "searchBy/allarticle")
      .then((res) => {
        //console.log(res.data.data);
        this.articles = res.data.data;
      })
      .catch((e) => console.log(e));
  },
};
</script>
