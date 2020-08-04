<template>
  <div>
    <v-container fluid>
      <v-row>
        <v-col>
          <v-form class="mx-10 full-width" ref="form" v-model="valid" lazy-validation>
            <div class="d-flex" id="title">
              <v-text-field
                class="mx-3"
                color="secondary"
                style="width:60%;"
                v-model="title"
                :rules="titleRules"
                :counter="30"
                label="Title"
                data-vv-name="title"
                required
                autofocus
              ></v-text-field>

              <v-select
                class="d-inline-block mx-3"
                id="selectedCategory"
                :items="categories"
                label="Category"
                color="secondary"
                outlined
                v-model="category"
              ></v-select>
            </div>

            <div id="content">
              <editor 
              :value="editorText"
              :options="editorOptions"
              :html="editorHtml"
              :visible="editorVisible"
              previewStyle="vertical"
              initialEditType="wysiwyg"
              :plugins="editorPlugin"
              ref="tuiEditor"
              height="500px"
              mode="wysiwyg"
              @change="mdChange"
              />
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
              >#{{ tag }}</v-chip>
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
              <v-btn color="secondary" class="d-inline-block mx-2 mr-4" @click="addTag">태그 추가</v-btn>
            </div>
          </v-form>

          <div class="text-center" id="btn">
            <v-btn color="success" class="mr-4" @click="validate">Submit</v-btn>
            <v-btn color="warning" class="mr-4" @click="reset">Reset</v-btn>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import 'codemirror/lib/codemirror.css';
import '@toast-ui/editor/dist/toastui-editor.css';
import { Editor } from '@toast-ui/vue-editor';
import axios from "axios";
import { mapActions } from "vuex";

export default {
  value: true,
  name: "Post",
  data() {
    return {
      dialog: false,
      valid: true,
      titleRules: [
        (v) => !!v || "제목은 반드시 작성해야합니다.",
        (v) => (v && v.length <= 30) || "제목은 30글자 이하여야 합니다.",
      ],
      contentRules: [
        (v) => !!v || "내용을 반드시 작성해야합니다.",
        (v) =>
          (v && v.length <= 3000) ||
          "내용은 최대 3,000자까지 작성이 가능합니다.",
      ],
      tag: new String(),
      tags: new Array(),
      tagsSelected: new Array(),
      tagsRules: [
        () => !(this.tags.length === 0) || "최소 한개의 태그를 추가해야합니다!",
        () => !this.tags.includes(this.tag) || "이미 추가된 태그입니다.",
        () =>
          (this.tags && this.tags.length <= 5) ||
          "태그는 최대 5개까지만 추가가 가능합니다.",
      ],
      isMD: false,
      contentBtn: "마크다운으로",
      title: "",
      content: "",
      editornickname: "",
      categories: new Array(),
      category: new String(),
      modify: 0,

      editorText: "",
      editorOptions: {
        hideModeSwitch: true,
      },
      editorHtml: "",
      editorMarkdown: "",
      editorVisible: true,
      editorPlugin: [],
      viewerText: "",
    };
  },
  methods: {
    validate() {
      if (this.$refs.form.validate()) {
        this.postArticle();
      }
    },
    reset() {
      this.tags = new Array();
      this.$refs.form.reset();
    },
    selectIndex(tag) {
      return this.tags.indexOf(tag);
    },
    addTag() {
      if (this.tag) {
        if (!this.tags.includes(this.tag)) {
          if (this.tags.length < 5) {
            this.tagsSelected.push(true);
            this.tags.push(this.tag);
            this.tag = "";
          } else {
            if (this.tags.length === 5) {
              this.tags.push("limiter");
              this.tagsSelected.push(false);

              setTimeout(() => {
                this.tags.splice(5, 1);
                this.tagsSelected.splice(5, 1);
              }, 1);
            }

            this.tag = "";
          }
        }
      }
    },
    closeTag(tagIndex) {
      if (this.tags) {
        this.tags.splice(tagIndex, 1);
        this.tagsSelected.splice(tagIndex, 1);
      }
    },
    postArticle() {
      axios
        .get(process.env.VUE_APP_ARTICLE + "searchBy/allarticle")
        .then((res) => {
          let lastArticleId = 0;
          if (res.data.data.length !== 0) {
            lastArticleId = res.data.data[res.data.data.length - 1].articleid;
          }
          axios
            .post(process.env.VUE_APP_ARTICLE + "regist", {
              title: this.title,
              content: this.content,
              editornickname: this.loggedIn,
              category: this.category,
              modify: this.modify,
            })
            .then((res) => {
              console.log(res);
              axios
                .post(process.env.VUE_APP_TAG + "regist", {
                  articleid: lastArticleId + 1,
                  tags: String(this.tags),
                })
                .then((res) => {
                  console.log(res);
                  this.setCurrentArticleId(lastArticleId + 1);
                  this.$router.push({
                    name: "Article",
                    params: { articleId: lastArticleId + 1 },
                  });
                })
                .catch((e) => console.log(e));
            })
            .catch((e) => console.log(e));
        })
        .catch((e) => console.log(e));
    },
    ...mapActions(["setCurrentArticleId"]),
    changeContent() {
      if (!this.isMD) {
        this.editorText = "";
        this.contentBtn = "일반 편집기로";
      } else if (this.isMD) {
        this.content = "";
        this.contentBtn = "마크다운으로";
      }

      this.isMD = !this.isMD;
    },
    mdChange() {
      let html = this.$refs.tuiEditor.invoke("getHtml");
      let markdown = this.$refs.tuiEditor.invoke("getMarkdown");
      this.editorHtml = html;
      this.editorMarkdown = markdown;
    },
  },
  components: {
    editor : Editor,
  },
  created() {
    this.categories = this.$store.state.categories;
    this.category = this.categories[0];
  },
  computed: {
    loggedIn: {
      get() {
        return this.$store.getters.loggedIn;
      },
      set(value) {
        this.$store.dispatch("setLoggedIn", value);
      },
    },
  },
};
</script>

<style></style>