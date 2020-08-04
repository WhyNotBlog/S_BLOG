<template>
  <div>
    <div
      class="text-xl-h2 text-lg-h2 text-md-h3 text-sm-h4 text-h5 font-weight-bold text-center"
      id="title"
    >{{ article.title }}</div>
    <hr class="my-5" />
    <div id="body">
      <div class="font-weight-bold text-center">
        작성자 : {{ article.editornickname }} |
        작성일 : {{ article.editdate | dateToString }} |
        <v-btn color="red accent-4" icon v-if="article.isLiked" @click="changeLiked()">
          <v-icon middle color="red accent-4">mdi-heart</v-icon>
        </v-btn>
        <v-btn color="red accent-4" icon v-else @click="changeLiked()">
          <v-icon middle color="red accent-4">mdi-heart-outline</v-icon>
        </v-btn>
        <div class="d-inline-block" v-if="(loggedIn !== null && loggedIn === article.editornickname)">
          <v-btn color="black accent-4" icon @click="updateArticle()">
            <v-icon middle color="black accent-4">mdi-file-document-edit</v-icon>
          </v-btn>
          <v-btn color="black accent-4" icon  @click="deleteArticle()">
            <v-icon middle color="black accent-4">mdi-delete</v-icon>
          </v-btn>
        </div>
      </div>
      <div class="text-center" id="catalogue">
        <label>태그 :</label>
        <v-chip
          class="ma-2 text-button chip-btn"
          color="secondary"
          v-for="tag in tags"
          :key="tag.tagid"
        >#{{ tag.tagname }}</v-chip>
      </div>
      <div
        class="text-xl-body-1 text-lg-body-1 text-md-body-1 text-sm-body-2 text-body-2 text-center my-5"
        id="text"
      >
      <viewer 
    :initialValue="article.content"
    :value="viewerText"
    height="500px"
    />
      </div>
    </div>
      <Comment :articleId="article.articleid" />
  </div>
</template>

<script>
import 'codemirror/lib/codemirror.css';
import '@toast-ui/editor/dist/toastui-editor.css';
import { Viewer } from '@toast-ui/vue-editor';

import axios from 'axios';
import Comment from './Comment';

export default {
  name: "Article",
  data() {
    return {
      article : new Object(
      ),
      tags : new Array(),
    };
  },
  components : {
    Comment,
    viewer : Viewer,
  },
  created() {
    axios.get(process.env.VUE_APP_ARTICLE + this.$route.params.articleId)
    .then((res) => {
      this.article = res.data.data;
      axios.get(process.env.VUE_APP_TAG + "taglist/" + this.article.articleid)
        .then((res) => {
          let tagData = res.data.data;
          this.tags = tagData;          
        })
        .catch((e) => console.log(e));
    })
    .catch((e) => console.log(e));

  },
  mounted() {

  },
  computed : {
    loggedIn: {
      get() {
        return this.$store.getters.loggedIn;
      },
      set(value) {
        this.$store.dispatch("setLoggedIn", value);
      },
    },
  },
  methods: {
    changeLiked() {
      // axios.post 추가
      this.article.isLiked = !this.article.isLiked;
    },
    updateArticle() {
      this.$router.push({ name: "Update" });
    },
    deleteArticle() {
       axios.delete(process.env.VUE_APP_ARTICLE + `delete/${this.article.articleid}`, { data : { articleid : this.article.articleid }
          })
       .then(res => {
         if(res.status === 200){
           alert('게시글 삭제에 성공했습니다!');
           this.$router.push({ name : 'Home' });
         }
       })
       .catch(e => console.log(e))
    },
  },
  filters: {
    dateToString(date) {
      try {
      return date.slice(0,10)
      }
      catch(e) {
        console.log('')        
      }
    },
  },
};
</script>

<style>
.chip-btn :hover {
  cursor: pointer;
}
</style>
