<template>
  <div></div>
</template>

<script>
import axios from "axios";
import { mapActions } from "vuex";

export default {
  name: "CheatArticle",
  props: {
    articleId: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      article: new Object(),
    };
  },
  methods: {
    ...mapActions(["setCurrentArticle"]),
  },
  created() {
    axios
      .get(process.env.VUE_APP_ARTICLE + "detail/" + `${this.articleId}`)
      .then((res) => {
        this.article = res.data.data;
        this.setCurrentArticle(this.article);
        this.$router.push({
          name: "Article",
          params: { articleId: this.article.articleid },
        });
      });
  },
};
</script>

<style></style>
