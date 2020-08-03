<template>
  <div>
    <br />
    <v-layout row wrap justify-center>
      <v-flex xs12 sm12 style="display:flex; justify-content:center">
        <v-text-field
          id="search"
          name="search"
          type="text"
          color="black"
          placeholder="검색어를 입력하세요"
          v-model="search"
          @change="searchContent"
          solo
          clearable
          single-line
        ></v-text-field>
      </v-flex>
    </v-layout>
  </div>
</template>

<script>
import axios from "axios";
export default {
  data() {
    return {
      search: "",
    };
  },
  methods: {
    searchContent() {
      console.log("search");
      axios
        .get(process.VUE_APP_ARTICLE + "/" + this.search)
        .then((res) => {
          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

<style scoped>
.v-text-field {
  max-width: 95%;
}
</style>
