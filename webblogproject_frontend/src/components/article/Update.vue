<template>
  <div>
  <v-container fluid>
    <v-row>
      <v-col>
      <v-form class="mx-10 full-width"
      ref="form"
      v-model="valid"
      lazy-validation>
    <div id="title">
      <v-text-field
        v-model="title"
        :rules="titleRules"
        :counter="30"
        label="Title"
        data-vv-name="title"
        required
        autofocus
      ></v-text-field>
    </div>

    <div id="content">
      <v-textarea
        v-model="content"
        label="Content"
        :rules="contentRules"
        :counter="3000"
        data-vv-name="content"
        required
      ></v-textarea>
    </div>

    <div id="picture">
      <v-file-input id="pictureFile" chips multiple accept="image/*" label="File input"></v-file-input>
    </div>

    <div class="text-center" id="tags">
      <v-chip
        class="ma-2 text-button chip-btn"
        color="secondary"
        v-for="tag in tags"
        :key="selectIndex(tag)"
        v-show="tagsSelected[selectIndex(tag)]"
        close
        @click:close="closeTag(selectIndex(tag))"
      >
        #{{ tag }}
      </v-chip>
    </div>

    <div class="text-center" id="tag">
      <v-text-field
        id="tagInput"
        class="d-inline-block mx-2"
        v-model="tag"
        label="Tag"
        :rules="tagsRules"
        data-vv-name="tag"
        color="secondary"
        style="width:50%; height:5%;"
      ></v-text-field>
      <v-btn 
      color="secondary"
      class="d-inline-block mx-2 mr-4"
      @click="addTag"
      >
        태그 추가
      </v-btn>
    </div>

    <div class="text-center" id="btn">
      <v-btn
        :disabled="!valid"
        color="success"
        class="mr-4"
        @click="updateArticle"
      >
        Submit
      </v-btn>
  
      <v-btn
        color="warning"
        class="mr-4"
        @click="reset"
      >
        Reset
      </v-btn>
    </div>
      </v-form>
    </v-col>
    </v-row>
  </v-container>
  </div>
</template>

<script>
import axios from 'axios';

export default {
    value : true,
    name : "Post",
    data() {
        return {
    valid: true,
    titleRules: [
      v => !!v || '제목은 반드시 작성해야합니다.',
      v => (v && v.length <= 30) || '제목은 30글자 이하여야 합니다.',
    ],
    contentRules: [
        v => (!!v || '내용을 반드시 작성해야합니다.'),
        v => (v && v.length <= 3000) || '내용은 최대 3,000자까지 작성이 가능합니다.'
    ],
    tag : new String(),
    tags : new Array(),
    tagsSelected : new Array(),
    tagsRules : [
        () => !(this.tags.length === 0) || "최소 한개의 카테고리를 추가해야합니다!",
        () => !this.tags.includes(this.tag) || '이미 추가된 카테고리입니다.',
        () => (this.tags && this.tags.length <= 5) || '카테고리는 최대 5개까지만 추가가 가능합니다.'],
    articleid : null,
    title : '',
    content : '',
    editornickname : '',
    category : new String(),
    modify : 1,
      }
    },
    methods : {
    validate () {
      this.$refs.form.validate()
    },
    reset () {
      this.tags = new Array();
      this.$refs.form.reset()
    },
    selectIndex(tag) {
        return this.tags.indexOf(tag)
    },
    addTag() {
      if (this.tag) {

        if (!this.tags.includes(this.tag)) {

          if (this.tags.length < 5) {
          this.tagsSelected.push(true);
          this.tags.push(this.tag);
          this.tag = '';
          } else {
            if (this.tags.length === 5) {
            this.tags.push('limiter');
            this.tagsSelected.push(false);

            setTimeout(
              () => {
            this.tags.splice(5,1)
            this.tagsSelected.splice(5,1)
              }, 1)}

            this.tag = '';
            }
        }
        this.category = this.tags.toString();
        }
      },
    closeTag(tagIndex) {
      if (this.tags) {
        this.tags.splice(tagIndex,1)
        this.tagsSelected.splice(tagIndex,1)    
        }
      },
    updateArticle() {
      axios.put(process.env.VUE_APP_ARTICLE + 'update', { 
        articleid : this.articleid,
        title : this.title,
        content : this.content,
        editornickname : this.loggedIn,
        category : this.category,
        modify : this.modify
      })
      .then(res => {
        console.log(res);
        axios.put(process.env.VUE_APP_TAG + "update", {
          articleid : this.articleid,
          tags : String(this.tags),
        })
        .then((res) => {
          console.log(res);
          this.$router.push({name : 'Article', params : { articleId : this.articleid }})
        })
        .catch((e) => console.log(e))
        })
      .catch(e => console.log(e))
      },
    },
    components : {
    },
    created() {
        axios.get(process.env.VUE_APP_ARTICLE + this.$store.state.currentArticleId)
        .then((res) => {
        if (res.status) {
        let data = res.data.data;
        this.articleid = data.articleid;
        this.title = data.title;
        this.content = data.content;
        this.editornickname = data.editornickname;
        this.category = data.category;
        this.editdate = data.editdate;
        this.modify = data.modify;
        }

        axios.get(process.env.VUE_APP_TAG + "taglist/" + this.articleid)
        .then((res) => {
          let tagData = res.data.data;
          tagData.forEach(obj => {
            this.tags.push(obj.tagname)
            this.tagsSelected.push(true);
            });
        })
        .catch((e) => console.log(e))
        });
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
}
</script>

<style>

</style>