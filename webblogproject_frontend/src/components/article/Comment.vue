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
      {{ text }}
      <template v-slot:action="{ attrs }">
        <v-btn dark text v-bind="attrs" @click="snackbar = false">닫기</v-btn>
      </template>
    </v-snackbar>

    <div
      v-for="comment in comments"
      :key="comment.commentid"
      class="d-block text-center"
      id="comment-list"
    >
      <div class="d-flex justify-space-around" :id="'comment' + comment.commentid" v-if="!needUpdate[comment.commentid]">
        <div>{{ comment.commentcontent }}</div>
        <div>
          {{ comment.commentornickname }} |
          {{ comment.commentdate | dateToString }}
          <div
            class="d-inline-block"
            v-if="loggedIn != null && loggedIn === comment.commentornickname"
          >
            |
            <v-btn color="black accent-4" icon @click="changeComment(comment)">
              <v-icon middle color="black accent-4">mdi-pencil-box-outline</v-icon>
            </v-btn>
            <v-btn color="red accent-4" icon @click="deleteComment(comment)">
              <v-icon middle color="red accent-4">mdi-alpha-x-box-outline</v-icon>
            </v-btn>
          </div>
        </div>
      </div>
      <div v-else>
          <v-form
            ref="form"
            v-model="valid"
            style="display:flex;justify-content: center;"
            lazy-validation
          >
            <v-textarea
              prepend-inner-icon="mdi-comment"
              class="mx-2"
              rows="1"
              color="secondary"
              auto-grow
              v-model="willUpdatedComment"
            ></v-textarea>

            <v-btn class="d-inline mx-1 my-auto" color="secondary" @click="updateComment(comment)">댓글 수정</v-btn>
            <v-btn class="d-inline mx-1 my-auto" color="secondary" @click="cancleUpdateComment(comment)">취소</v-btn>
          </v-form>
      </div>
    </div>
    <v-form
      ref="form"
      v-model="valid"
      style="display:flex;justify-content: center;"
      lazy-validation
    >
      <v-textarea
        prepend-inner-icon="mdi-comment"
        class="mx-2"
        rows="1"
        color="secondary"
        auto-grow
        v-model="comment"
      ></v-textarea>

      <v-btn class="d-inline mx-1 my-auto" color="secondary" @click="validate">댓글 작성</v-btn>
    </v-form>
  </div>
</template>

<script>
import axios from "axios";

export default {
  name: "Comment",
  data() {
    return {
      valid: true,
      comment: "",
      commentRules: [
        (v) => v.length !== 0 || "내용을 반드시 작성해야합니다.",
        () =>
          this.loggedIn !== null || "로그인을 해야 댓글을 작성할 수 있습니다.",
      ],
      comments: new Object(),
      needUpdate: new Array(),
      willUpdatedComment: "",
      willUpdatedCommentCopy: "",
      snackbar: false,
      text: "",
      timeout: 5000,
      x: null,
      y: "top",
      mode: "",
    };
  },
  props: ["articleId"],
  methods: {
    validate() {
      this.postComment();
    },
    postComment() {
      if (this.loggedIn !== null) {
        if (this.comment == "") {
          this.text = "댓글은 내용을 반드시 작성해야합니다.";
          this.snackbar = true;
          return;
        }

        axios
          .post(
            process.env.VUE_APP_COMMENT + "regist",
            {
              articleid: this.articleId,
              commentcontent: this.comment,
              commentornickname: this.loggedIn,
            },
            {
              headers: {
                "jwt-auth-token": this.jwtAuthToken,
              },
            }
          )
          .then(() => {
            axios
              .get(process.env.VUE_APP_COMMENT + "article/" + this.articleId, {
                headers: {
                  "jwt-auth-token": this.jwtAuthToken,
                },
              })
              .then((res) => {
                this.comments = res.data.data;
                this.comments.forEach(
                (comment) => (this.needUpdate[comment.commentid] = false)
                 );
                this.comment = "";
              });
          });
      } else {
        this.text = "댓글은 로그인해야 작성할 수 있습니다.";
        this.snackbar = true;
      }
    },
    deleteComment(currentComment) {
      axios
        .delete(
          process.env.VUE_APP_COMMENT + "delete/" + currentComment.commentid,
          {
            headers: {
              "jwt-auth-token": this.jwtAuthToken,
            },
          }
        )
        .then(() => {
          this.text = "댓글 삭제에 성공했습니다.";
          this.snackbar = true;
          axios
            .get(process.env.VUE_APP_COMMENT + "article/" + this.articleId, {
              headers: {
                "jwt-auth-token": this.jwtAuthToken,
              },
            })
            .then((res) => {
              this.comments = res.data.data;
              this.comments.forEach(
              (comment) => (this.needUpdate[comment.commentid] = false)
                 );
            });
        });
    },
    changeComment(currentComment) {
      if (this.needUpdate[currentComment.commentid] === false) {
        let tempList = [...this.needUpdate];
        tempList.splice(currentComment.commentid, 1, true);
        this.needUpdate = tempList;
        this.willUpdatedComment = currentComment.commentcontent;
        this.willUpdatedCommentCopy = currentComment.commentcontent;
      }
    },
    cancleUpdateComment(currentComment) {
      let tempList = [...this.needUpdate];
      tempList.splice(currentComment.commentid, 1, false);
      this.needUpdate = tempList;
    },
    updateComment(comment) {
      axios
        .put(
          process.env.VUE_APP_COMMENT + "update",
          {
            articleid: comment.articleid,
            commentid : comment.commentid,
            commentcontent: this.willUpdatedComment,
            commentornickname: comment.commentornickname,
          },
          {
            headers: {
              "jwt-auth-token": this.jwtAuthToken,
            },
          }
        )
        .then(() => {
            axios
              .get(process.env.VUE_APP_COMMENT + "article/" + this.articleId, {
                headers: {
                  "jwt-auth-token": this.jwtAuthToken,
                },
              })
              .then((res) => {
                this.comments = res.data.data;
                this.comments.forEach(
                (comment) => (this.needUpdate[comment.commentid] = false)
                 );
                this.comment = "";
              });
          });
    },
  },
  component: {},
  computed: {
    loggedIn: {
      get() {
        return this.$store.getters.loggedIn;
      },
      set(value) {
        this.$store.dispatch("setLoggedIn", value);
      },
    },
    jwtAuthToken: {
      get() {
        return this.$store.getters.jwtAuthToken;
      },
      set(value) {
        this.$store.dispatch("setJwtAuthToken", value);
      },
    },
  },

  created() {
    axios
      .get(process.env.VUE_APP_COMMENT + "article/" + this.articleId)
      .then((res) => {
        this.comments = res.data.data;
        this.comments.forEach(
          (comment) => (this.needUpdate[comment.commentid] = false)
        );
        //console.log(this.needUpdate);
        //console.log(this.comments);
      });
  },
  filters: {
    dateToString(date) {
      try {
        return date.slice(0, 10);
      } catch (e) {
        console.log("");
      }
    },
  },
};
</script>

<style></style>
