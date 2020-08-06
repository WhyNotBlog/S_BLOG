<template>
  <v-card>
    <v-row align="center" justify="center" class="modal">
      <v-col class="modal">
        <v-card class="elevation-12" style="background-color:#f1f3f5">
          <v-layout>
            <v-flex sm5 xs5 md5>
              <div style="background-color:#595959">
                <v-btn dark text :small="true" @click="closeModal">
                  <v-icon :small="true">close</v-icon>
                </v-btn>
                <br />
                <br />
                <div class="logo">
                  <img src="@/assets/logo.png" width="200px" />
                  <h3 style="color:white">{{type}}</h3>
                </div>
                <br />
              </div>
            </v-flex>

            <v-flex sm7 xs7 md7>
              <v-list>
                <v-list-item v-for="item in person" :key="item.id">
                  <v-list-item-content>
                    <v-list-item-title>{{item.nickname}}</v-list-item-title>
                  </v-list-item-content>

                  <v-list-item-action>
                    <v-icon>person</v-icon>
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
  },
  data() {
    return {
      follower: new Array(),
      following: new Array(),
      person: new Array(),
    };
  },

  created() {
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
      .catch((err) => {
        console.log(err);
      });
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
}

.v-btn {
  float: right;
}
</style>