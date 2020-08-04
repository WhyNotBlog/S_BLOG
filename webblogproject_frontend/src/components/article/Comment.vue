<template>
  <div>
    <div v-for="comment in comments" :key="comment.commentid" class="d-block text-center" id="comment-list">
        <div class="d-flex justify-space-around" :id="'comment' + comment.commentid">
            <div>{{ comment.commentcontent }}</div>
            <div>{{ comment.commentornickname }} | {{ comment.commentdate | dateToString }} 
              <div class="d-inline-block" v-if="(loggedIn != null && loggedIn === comment.commentornickname)">
                 | 
                <v-btn color="black accent-4" icon>
                    <v-icon middle color="black accent-4">mdi-pencil-box-outline</v-icon>
                </v-btn>
                <v-btn color="red accent-4" icon  @click="deleteComment(comment)">
                    <v-icon middle color="red accent-4">mdi-alpha-x-box-outline</v-icon>
                </v-btn>
              </div>
            </div>
        </div>
    </div>
    <v-form class="d-inline-block mx-10" ref="form" v-model="valid" style="width : 80%;" lazy-validation>
     <v-textarea
        prepend-inner-icon="mdi-comment"
        class="mx-2"
        label="Comment"
        :rules="commentRules"
        rows="1"
        color="secondary"
        auto-grow
        v-model="comment"
    ></v-textarea>
    </v-form>
    <v-btn
        class="d-inline mx-1 my-auto"
        color="secondary"
        @click="validate"
        >
        댓글 작성
        </v-btn>
  </div>
</template>

<script>
import axios from 'axios';

export default {
    name : 'Comment',
    data() {
        return {
            valid : true,
            comment : '',
            commentRules : [(v) => v.length !== 0 || "내용을 반드시 작성해야합니다.",
                            () => this.loggedIn !== null || "로그인을 해야 댓글을 작성할 수 있습니다."],
            comments : new Object(),
            willUpdatedComment : '',
            willUpdatedCommentCopy : new Object(),
        }
    },
    methods : {
        validate() {
      if (this.$refs.form.validate()) {
      this.postComment();
          }
        },
        postComment() {
            if (this.loggedIn !== null) {
            axios.post(process.env.VUE_APP_COMMENT + 'regist', {
                articleid : this.$store.state.currentArticleId,
                commentcontent : this.comment,
                commentornickname : this.loggedIn,
            })
            .then((res) => {
                axios.get(process.env.VUE_APP_COMMENT + 'article/' + this.$store.state.currentArticleId)
                .then((res) => {
                this.comments = res.data.data;
                })
                .catch((e) => console.log(e));
                this.comment = '',
                console.log(res.status);
                })
            .catch((e) => console.log(e))
            }
        },
        deleteComment(currentComment) {
            axios.delete(process.env.VUE_APP_COMMENT + 'delete/' + currentComment.commentid)
            .then((res) => {
                alert('댓글 삭제에 성공했습니다.');
                axios.get(process.env.VUE_APP_COMMENT + 'article/' + this.$store.state.currentArticleId)
                .then((res) => {
                this.comments = res.data.data;
                })
                .catch((e) => console.log(e));
                console.log(res.status);
            })
            .catch(e => console.log(e))
        },
        // updateComment(currentComment) {
        //     this.willUpdatedCommentCopy = currentComment;
        //     let willChangedComment = document.querySelector(`#comment${currentComment.commentid}`);
        //     willChangedComment.innerHTML = '';
        //     const commentUpdateInput = document.createElement("input");
        //     commentUpdateInput.setAttribute('v-model', 'willUpdatedComment')
        //     const commentUpdateButton = document.createElement("button");
        //     commentUpdateButton.innerText = '댓글 수정'
        //     commentUpdateButton.setAttribute('type', 'submit')
        //     willChangedComment.append(commentUpdateInput, commentUpdateButton);
        // }
    },
    component : {

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
    props : {
        articleId : {
            type : Number
        }
    },
    created() {
        axios.get(process.env.VUE_APP_COMMENT + 'article/' + this.$store.state.currentArticleId)
        .then((res) => {
        this.comments = res.data.data;
    });
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
}
</script>

<style>

</style>