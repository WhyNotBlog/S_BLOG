<template>
  <div id="app">
    <el-table :data="list" height="624" border>
      <el-table-column prop="title" label="Hacker News Title"></el-table-column>
      <el-table-column prop="author" label="Author" width="125"></el-table-column>

      <infinite-loading
        slot="append"
        @infinite="infiniteHandler"
        force-use-infinite-wrapper=".el-table__body-wrapper"
      ></infinite-loading>
    </el-table>
  </div>
</template>

<script src="https://cdnjs.cloudflare.com/ajax/libs/vue/2.5.17/vue.min.js"></script>
<script src="https://unpkg.com/vue-infinite-loading@2.4.4/dist/vue-infinite-loading.js"></script>
<script src="https://unpkg.com/element-ui@2.4.8/lib/index.js"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/axios/0.18.0/axios.min.js"></script>

<script>
const api = "http://hn.algolia.com/api/v1/search_by_date?tags=story";
import axios from "axios";

export default {
  data() {
    return {
      page: 1,
      list: [],
    };
  },
  methods: {
    infiniteHandler($state) {
      axios
        .get(api, {
          params: {
            page: this.page,
          },
        })
        .then(({ data }) => {
          if (data.hits.length) {
            this.page += 1;
            this.list = this.list.concat(data.hits);
            $state.loaded();
          } else {
            $state.complete();
          }
        });
    },
  },
};
</script>
