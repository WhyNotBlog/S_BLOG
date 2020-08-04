<template>
  <v-app style="background-color:#f1f3f5; height:100%">
    <header>
      <Header />
    </header>
    <v-container fluid>
      <v-layout justify-space-around>
        <v-flex xs8 sm8 md8 lg9>
          <router-view />
        </v-flex>
        <v-flex xs2 sm2 md2 lg2>
          <div class="sidebar">
            <Sidebar />
          </div>
        </v-flex>
      </v-layout>
    </v-container>
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

.sidebar {
  position: fixed;
  margin-right: 10px;
}

@media screen and (max-width: 700px) {
  .sidebar {
    display: none;
  }
}
</style>
