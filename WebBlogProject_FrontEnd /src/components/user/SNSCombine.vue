<template>
  <v-dialog v-model="checkModal" persistent width="500">
    <v-card>
      <v-row align="center" justify="center" class="modal">
        <v-col class="modal">
          <v-card class="elevation-12">
            <v-toolbar color="black" dark flat>
              <v-icon left>supervisor_account</v-icon>
              <span>SNS 연동</span>
              <v-spacer></v-spacer>
              <v-btn text :small="true" @click="closeModal">
                <v-icon :small="true">close</v-icon>
              </v-btn>
            </v-toolbar>
            <v-card-text>
              <v-col>
                <v-form ref="form">
                  <v-checkbox color="green" :rules="[rules.checkRequired]">
                    <template v-slot:label>
                      <div @click.stop="">
                        이미 가입된 메일입니다. 연동하시겠습니까?
                      </div>
                    </template>
                  </v-checkbox>
                </v-form>
              </v-col>
            </v-card-text>

            <v-card-actions>
              <v-spacer></v-spacer>
              <v-btn color="black white--text" @click="joinHandler"
                >연동하기</v-btn
              >
            </v-card-actions>
          </v-card>
        </v-col>
      </v-row>
    </v-card>
  </v-dialog>
</template>

<script>
import axios from "axios";
export default {
  name: "snsCombine",
  computed: {
    loginModal: {
      get() {
        return this.$store.getters.loginModal;
      },
      set(value) {
        this.$store.dispatch("setLoginModal", value);
      },
    },
  },
  created() {
    this.loginModal = false;
    axios
      .post(process.env.VUE_APP_ACCOUNT + "getSNSData", {})
      .then((res) => {
        this.checkModal = true;
        this.loginkind = res.data.data.loginkind;
        this.email = res.data.data.email;
      })
      .catch((err) => {
        console.log(err);
      });
  },

  data() {
    return {
      loginkind: "",
      email: "",
      check: false,
      checkModal: false,
      rules: {
        checkRequired: (value) => !!value,
      },
    };
  },
  methods: {
    joinHandler() {
      if (this.$refs.form.validate()) {
        this.join();
      }
    },
    join() {
      axios
        .post(process.env.VUE_APP_ACCOUNT + "combineId", {
          email: this.email,
          loginkind: this.loginkind,
        })
        .then(() => {
          this.$router.push("/");
        });
    },

    closeModal() {
      this.nicknameModal = false;
      this.$router.push("/");
    },
  },
};
</script>

<style scoped>
.modal {
  padding: 0;
  margin: 0;
}
</style>
