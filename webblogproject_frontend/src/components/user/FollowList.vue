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
                  <h3 style="color:white">팔로워</h3>
                </div>
                <br />
              </div>
            </v-flex>

            <v-flex sm7 xs7 md7>
              <v-list>
                <v-list-item v-for="item in person" :key="item.id">
                  <v-list-item-content>
                    <v-list-item-title v-html="item.text">{{item.nickname}}</v-list-item-title>
                  </v-list-item-content>

                  <v-list-item-action>
                    <v-icon>{{item.icon}}</v-icon>
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
  props: ["key", "id"],
  methods: {
    closeModal() {
      this.$emit("close-modal");
    },
  },
  data() {
    return {
      person: [],
    };
  },

  created() {
    if (this.key == "follower") {
      axios
        .get(process.env.VUE_APP_FOLLOW + "followList/" + this.id)
        .then((res) => {
          //console.log(res);
          this.person = res.data.data;
        })
        .catch((err) => {
          console.log(err);
        });
    } else {
      axios
        .get(process.env.VUE_APP_FOLLOW + "followingList/" + this.id)
        .then((res) => {
          //console.log(res);
          this.person = res.data.data;
        })
        .catch((err) => {
          console.log(err);
        });
    }
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