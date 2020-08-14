<template>
  <v-card>
    <v-row align="center" justify="center" class="modal">
      <v-col class="modal">
        <v-card class="elevation-12" style="background-color:#f1f3f5">
          <v-layout style="height:280px; ">
            <v-flex sm5 xs5 md5 style="background-color:#595959;">
              <v-btn dark text :small="true" @click="closeModal">
                <v-icon :small="true">close</v-icon>
              </v-btn>
              <br />
              <br />

              <div class="logo">
                <img class="image" src="@/assets/logo.png" />
                <h3 style="color:white;">{{ title }}</h3>
              </div>
              <br />
            </v-flex>

            <v-flex sm7 xs7 md7>
              <v-list>
                <v-list-item
                  v-for="(item, index) in person"
                  :key="item.id"
                  class="list"
                >
                  <v-list-item-content>
                    <v-list-item-title>
                      <v-avatar style="margin-right:10px;">
                        <img :src="imgSrc(item.id, item.picture)" />
                      </v-avatar>

                      {{ item.nickname }}
                    </v-list-item-title>
                  </v-list-item-content>

                  <v-list-item-action>
                    <v-btn text @click="unFollow(item, index)" v-if="btnF(item)"
                      >삭제</v-btn
                    >
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
    imgSrc(id, flag) {
      //console.log(flag);
      return flag
        ? process.env.VUE_APP_ACCOUNT + "downloadFile/" + id + ".jpg"
        : require("@/assets/profile.svg");
    },
    closeModal() {
      this.$emit("close-modal");
    },

    btnF(item) {
      let bool = false;
      this.followingList.forEach((element) => {
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
        .then((res) => {
          this.followingList.push({
            id: res.data.data.id,
            nickname: res.data.data.nickname,
          });
        })
        .catch((err) => {
          console.log(err);
        });
    },

    unFollow(item, i) {
      if (this.type == "Follower") {
        axios
          .delete(
            process.env.VUE_APP_FOLLOW + "delete/" + item.id + "/" + this.id
          )
          .then(() => {
            this.followerList.splice(i, 1);
            this.person = this.followerList;
            this.followerList = this.person;
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        axios
          .delete(
            process.env.VUE_APP_FOLLOW + "delete/" + this.id + "/" + item.id
          )
          .then(() => {
            this.followingList.splice(i, 1);
            this.person = this.followingList;
            //console.log(this.followingList);
            this.followingList = this.person;
          })
          .catch((err) => {
            console.log(err);
          });
      }
    },
  },
  data() {
    return {
      person: new Array(),
      title: "",
    };
  },

  created() {
    if (this.type == "Follower") {
      this.title = "나를 팔로우한 친구들";
      this.person = this.followerList;
    } else {
      this.title = "내가 팔로우한 친구들";
      this.person = this.followingList;
    }

    console.log(this.followerList);
  },

  computed: {
    followingList: {
      get() {
        return this.$store.getters.followingList;
      },
      set(value) {
        this.$store.dispatch("setFollowingList", value);
      },
    },
    followerList: {
      get() {
        return this.$store.getters.followerList;
      },
      set(value) {
        this.$store.dispatch("setFollowerList", value);
      },
    },
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
.theme--light.v-list {
  background: #f1f3f5;
}

.v-btn {
  float: right;
}

.image {
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
  .image {
    width: 150px;
  }
}

@media screen and (max-width: 400px) {
  h3 {
    font-size: 12px;
  }

  .image {
    width: 110px;
  }
}
</style>
