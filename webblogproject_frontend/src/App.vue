<template>
  <v-app style="background-color:#f1f3f5; height:100%">
    <Header style="z-index:100" />
    <Sidebar class="sidebar" />
    <div class="main2">
      <router-view />
    </div>
    <br />
    <br />
    <br />
    <v-footer
      absolute
      class="font-weight-medium"
      style="background-color:#595959;color:white;z-index:2"
    >
      <v-col class="text-center" cols="12">
        {{ new Date().getFullYear() }},
        <v-icon color="white" size="16px">copyright</v-icon>
        <strong>외않되</strong>
      </v-col>
    </v-footer>
  </v-app>
</template>

<script>
import Header from "@/components/Header";
import Sidebar from "@/components/Sidebar";

export default {
  name: "App",

  components: {
    Header,
    Sidebar,
  },
};
</script>
<style>
@import url(//fonts.googleapis.com/earlyaccess/notosanskr.css);
* {
  font-family: "Noto Sans KR", sans-serif;
}

.main2 {
  margin-top: 10px;
  margin-bottom: 10px;
  margin-right: 230px;
}

@media screen and (max-width: 960px) {
  .main2 {
    margin-top: 0;
    margin-right: 0;
    margin-bottom: 10px;
  }
}

.v-subheader {
  height: 34px;
}

.layout {
  flex-wrap: wrap;
}
</style>
