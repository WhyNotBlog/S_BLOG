<template>
  <v-card>
    <v-row align="center" justify="center" class="modal">
      <v-col class="modal">
        <v-card class="elevation-12">
          <v-layout style="height:280px">
            <v-flex sm5 xs5 md5 style="background-color:#595959;">
              <v-btn dark text :small="true" @click="closeModal">
                <v-icon :small="true">close</v-icon>
              </v-btn>
              <br />
              <br />

              <div class="logo">
                <img src="@/assets/logo.png" />
                <h3 style="color:white;">{{title}}</h3>
              </div>
              <br />
            </v-flex>

            <v-flex sm7 xs7 md7>
              <v-list>
                <v-list-item v-for="item in person" :key="item.id" class="list">
                  <v-list-item-content>
                    <v-list-item-title>{{item.nickname}}</v-list-item-title>
                  </v-list-item-content>

                  <v-list-item-action>
                    <v-btn text @click="unFollow(item)" v-if="btnF(item)">삭제</v-btn>
                    <v-btn text @click="follow(item)" v-else>팔로우</v-btn>
                  </v-list-item-action>
                </v-list-item>
              </v-list>
            </v-flex>
          </v-layout>
        </v-card>
      </v-col>
    </v-row>
  </v-card>
</template>

<script>
import axios from "axios";
export default {
  props: ["type", "id"],
  methods: {
    closeModal() {
      this.$emit("close-modal");
    },

    btnF(item) {
      let bool = false;
      this.following.forEach((element) => {
        if (element.id == item.id) {
          bool = true;
        }
      });
      return bool;
    },
    follow(item) {
      axios
        .post(process.env.VUE_APP_FOLLOW + "regist", {
          targetid: item.id,
          userid: this.id,
        })
        .then(() => {})
        .catch((err) => {
          console.log(err);
        });
    },
    getList() {
      axios
        .get(process.env.VUE_APP_FOLLOW + "followList/" + this.id)
        .then((res) => {
          this.follower = res.data.data;
          if (this.type == "Follower") {
            this.person = this.follower;
          }
        })
        .catch((err) => {
          console.log(err);
        });

      axios
        .get(process.env.VUE_APP_FOLLOW + "followingList/" + this.id)
        .then((res) => {
          this.following = res.data.data;
          if (this.type == "Following") {
            this.person = this.following;
          }
        })
        .then(() => {
          this.getList();
          this.$emit("update-follow");
        })
        .catch((err) => {
          console.log(err);
        });
    },

    unFollow(item) {
      axios
        .delete(
          process.env.VUE_APP_FOLLOW + "delete/" + this.id + "/" + item.id
        )
        .then(() => {
          this.getList();
          this.$emit("update-follow");
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  data() {
    return {
      follower: new Array(),
      following: new Array(),
      person: new Array(),
      title: "",
    };
  },

  created() {
    if (this.type == "Follower") {
      this.title = "나를 팔로우한 친구들";
    } else {
      this.title = "내가 팔로우한 친구들";
    }
    this.getList();
  },
};
</script>

<style scoped>
.modal {
  padding: 0;
  margin: 0;
}

.logo {
  text-align: center;
  position: absolute;
  left: 21%;
  top: 50%;
  transform: translate(-50%, -50%);
}

.v-btn {
  float: right;
}

img {
  width: 200px;
}

.list {
  max-width: 100%;
  height: 0px;
  max-height: 0px;
  border: solid;
  border-color: rgba(0, 0, 0, 0.12);
  border-width: 0 0 thin 0;
  transition: inherit;
}
@media screen and (max-width: 570px) {
  h3 {
    font-size: 14px;
  }
  img {
    width: 150px;
  }
}

@media screen and (max-width: 400px) {
  h3 {
    font-size: 12px;
  }

  img {
    width: 110px;
  }
}
</style>