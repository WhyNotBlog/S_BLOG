<template>
  <div>
    <v-snackbar
      v-model="snackbar"
      :bottom="y === 'bottom'"
      color="#9FA9D8"
      :left="x === 'left'"
      :multi-line="mode === 'multi-line'"
      :right="x === 'right'"
      :timeout="timeout"
      :top="y === 'top'"
      :vertical="mode === 'vertical'"
    >
      {{text}}
      <template v-slot:action="{ attrs }">
        <v-btn dark text v-bind="attrs" @click="snackbar = false">닫기</v-btn>
      </template>
    </v-snackbar>
    <v-container>
      <v-row>
        <v-col>
          <v-form ref="form" v-model="valid" lazy-validation>
            <div id="title">
              <v-text-field
                color="secondary"
                v-model="title"
                :rules="titleRules"
                :counter="30"
                label="제목"
                data-vv-name="title"
                required
                autofocus
              ></v-text-field>
            </div>
            <br />

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
            <div id="thumbnail">
              <v-file-input label="썸네일" v-model="thumbnail" filled prepend-icon="mdi-camera"></v-file-input>
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
                :rules="tagsRules"
                data-vv-name="tag"
                color="secondary"
                style="width:50%; height:5%;"
              ></v-text-field>
              <v-btn color="secondary" class="d-inline-block mx-2 mr-4" @click="addTag">태그 추가</v-btn>
            </div>
          </v-form>
          <br />

          <div class="text-center" id="btn">
            <v-btn color="warning" class="mr-4" @click="reset">초기화</v-btn>
            <v-btn color="secondary" class="mr-4" @click="saveTempArticle">임시저장</v-btn>
            <v-btn color="success" class="mr-4" @click="validateSubmit">글 쓰기</v-btn>
          </div>
        </v-col>
      </v-row>
    </v-container>
  </div>
</template>

<script>
import "codemirror/lib/codemirror.css";
import "@toast-ui/editor/dist/toastui-editor.css";
import { Editor } from "@toast-ui/vue-editor";
import axios from "axios";
import { mapActions } from "vuex";

export default {
  value: true,
  name: "Post",
  data() {
    return {
      user: new Object(),
      dialog: false,
      valid: true,
      article: new Object(),
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
        () => {
          let regExp = /[{}[\]/?.,;:|)*~`!^\-_+<>@#$%&\\=('"]/gi;
          return (
            !regExp.test(this.tag) || "태그에는 특수문자가 포함될 수 없습니다."
          );
        },
        () => !this.tags.includes(this.tag) || "이미 추가된 태그입니다.",
        () =>
          (this.tags && this.tags.length <= 5) ||
          "태그는 최대 5개까지만 추가가 가능합니다.",
        () => !(this.tags.length === 0) || "최소 한개의 태그를 추가해야합니다!",
      ],
      title: "",
      thumbnail: new Object(),
      content: "",
      editornickname: "",
      articleid: "",
      bigCategories: new Array(),
      middleCategories: new Array(),
      smallCategories: new Array(),
      bigCategory: new String(),
      middleCategory: new String(),
      smallCategory: new Object(),
      categoryInt: 0,
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
      snackbar: false,
      text: "",
      timeout: 5000,
      x: null,
      y: "top",
      mode: "",
    };
  },
  methods: {
    validateSubmit() {
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
      let regExp = /[{}[\]/?.,;:|)*~`!^\-_+<>@#$%&\\=('"]/gi;
      if (this.tag && !regExp.test(this.tag)) {
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
      console.log(this.thumbnail.name);
      axios
        .post(process.env.VUE_APP_ARTICLE + "regist", {
          title: this.title,
          content: this.editorMarkdown,
          editornickname: this.loggedIn,
          category: this.categoryInt,
          modify: this.modify,
          writerid: this.user.id,
          thumbnail: this.thumbnail.name != null ? true : false,
        })
        .then((res) => {
          let data = res.data.data;
          this.article = data;
          this.setCurrentArticle(this.article);
          this.articleid = data.articleid;
          //console.log(this.articleid);
          if (this.thumbnail.name != null) this.addItem();
          axios
            .post(process.env.VUE_APP_TAG + "regist", {
              articleid: data.articleid,
              tags: String(this.tags),
            })
            .then(() => {
              this.$router.push({
                name: "Article",
                params: { articleId: this.article.articleid },
              });
            });
        });
    },
    saveTempArticle() {
      axios
        .post(process.env.VUE_APP_ARTICLETEMP + "regist", {
          title: this.title,
          content: this.editorMarkdown,
          editornickname: this.loggedIn,
          category: this.categoryInt,
          modify: this.modify,
          writerid: this.user.id,
        })
        .then((res) => {
          let data = res.data.data;
          axios
            .post(process.env.VUE_APP_TAGTEMP + "regist", {
              articletempid: data.articleid,
              tagtemps: String(this.tags),
            })
            .then(() => {
              this.text = "임시저장에 성공했습니다.";
              this.snackbar = true;
              this.$router.push({ name: "TempList" });
            });
        });
    },
    ...mapActions(["setCurrentArticle"]),
    mdChange() {
      let html = this.$refs.tuiEditor.invoke("getHtml");
      let markdown = this.$refs.tuiEditor.invoke("getMarkdown");
      this.editorHtml = html;
      this.editorMarkdown = markdown;
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
      console.log(this.categoryInt);
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
      console.log(this.categoryInt);
    },
    changeSmallCategory() {
      this.categoryInt = this.smallCategory;
      console.log(this.categoryInt);
    },

    addItem() {
      const data = new FormData(); // 서버로 전송할 폼데이터
      const file = this.thumbnail; // 선택된 파일객체
      data.append("file", file); // 폼데이터에 파일을 추가
      console.log(data);
      //   데이터를 서버로 전송하는 코드 추가
      axios
        .post(process.env.VUE_APP_ARTICLE + "uploadThumbnail", data, {
          headers: {
            "Content-Type": "multipart/form-data",
            articleNum: this.articleid,
          },
        })
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  components: {
    editor: Editor,
  },
  created() {
    this.bigCategories = this.$store.state.bigCategories;
    this.bigCategory = this.bigCategories[0];
    this.middleCategories = this.$store.state.middleCategories[0];
    (this.middleCategory = this.middleCategories[0]),
      (this.smallCategories = this.$store.state.smallCategories[0][0]);
    this.smallCategory = this.smallCategories[0];
    axios
      .get(process.env.VUE_APP_ACCOUNT + "getUserInfo/" + this.loggedIn, {
        headers: {
          "jwt-auth-token": this.jwtAuthToken,
        },
      })
      .then((res) => {
        if (res.status) {
          let data = res.data.data;
          this.user = data;
        }
      });
  },
  computed: {
    jwtAuthToken: {
      get() {
        return this.$store.getters.jwtAuthToken;
      },
      set(value) {
        this.$store.dispatch("setJwtAuthToken", value);
      },
    },
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

<style scoped>
.v-select,
.v-text-field {
  margin-left: 10px;
}
</style>
