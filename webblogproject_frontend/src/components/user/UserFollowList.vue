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
                <v-list-item v-for="item in person" :key="item.id" class="list">
                  <v-list-item-content>
                    <v-list-item-title>
                      <v-avatar style="margin-right:10px;">
                        <img :src="imgSrc(item.id, item.picture)" />
                      </v-avatar>
                      {{ item.nickname }}
                    </v-list-item-title>
                  </v-list-item-content>
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
export default {
  props: ["type", "nickname", "followingList", "followerList"],
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
  },
  data() {
    return {
      person: new Array(),
      title: "",
    };
  },

  created() {
    if (this.type == "Follower") {
      this.title = this.nickname + "님을 팔로우한 친구들";
      this.person = this.userFollowerList;
    } else {
      this.title = this.nickname + "님이 팔로우한 친구들";
      this.person = this.userFollowingList;
    }

    //console.log(this.followingList);
  },

  computed: {
    userFollowingList() {
      return this.followingList;
    },
    userFollowerList() {
      return this.followerList;
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
