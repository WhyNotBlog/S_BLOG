<template>
  <div class="side">
    <v-list>
      <v-list subheader>
        <v-subheader>Guide</v-subheader>
        <v-divider></v-divider>
        <v-list-item v-for="item in guide" :key="item.text" :to="item.route">
          <v-list-item-content>
            <v-list-item-title v-html="item.text"></v-list-item-title>
          </v-list-item-content>

          <v-list-item-action>
            <v-icon>{{ item.icon }}</v-icon>
          </v-list-item-action>
        </v-list-item>
      </v-list>

      <v-list subheader>
        <v-subheader>Category</v-subheader>
        <v-divider></v-divider>
        <v-list-group v-for="item in category" :key="item.text">
          <template v-slot:activator>
            <v-list-item-title>{{ item.text }}</v-list-item-title>
          </template>

          <v-list-group
            no-action
            sub-group
            v-for="subItem in item.items"
            :key="subItem.title"
            :to="subItem.route"
          >
            <template v-slot:activator>
              <v-list-item-content>
                <v-list-item-title>{{ subItem.title }}</v-list-item-title>
              </v-list-item-content>
            </template>

            <v-list-item
              v-for="subItem2 in subItem.items"
              :key="subItem2.title"
              :to="subItem2.route"
            >
              <v-list-item-content>
                <v-list-item-title>{{ subItem2.title }}</v-list-item-title>
              </v-list-item-content>
            </v-list-item>
          </v-list-group>
        </v-list-group>
      </v-list>

      <v-list subheader>
        <v-subheader>Tag</v-subheader>
        <v-divider style="margin-bottom:10px"></v-divider>
        <span v-for="item in tag" :key="item.tagname">
          <v-btn
            style="margin:3px; border-radius: 2rem;"
            dark
            color="#9FA9D8"
            @click="tagSearch(item.tagname)"
            >#{{ item.tagname }}</v-btn
          >
        </span>
      </v-list>
      <br />
    </v-list>
  </div>
</template>

<script>
import axios from "axios";
export default {
  methods: {
    tagSearch(tag) {
      this.$router.push("/search/tag/" + tag);
    },
  },
  created() {
    axios
      .get(process.env.VUE_APP_TAG + "tentaglist")
      .then((res) => {
        //console.log(res.data.data);
        this.tag = res.data.data;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  data() {
    return {
      category: [
        {
          icon: "format_shapes",
          text: "알고리즘",
          items: [
            {
              title: "기본 문제",
              icon: "mdi-login",
              route: "/search",
            },
            {
              title: "기출 문제",
              icon: "mdi-shape-plus",
              route: "/search",
              items: [
                {
                  title: "삼성",
                  icon: "mdi-shape-plus",
                  route: "/search",
                },
                {
                  title: "카카오",
                  icon: "mdi-shape-plus",
                  route: "/search",
                },
                {
                  title: "기타",
                  icon: "mdi-shape-plus",
                  route: "/search",
                },
              ],
            },
          ],
        },
        {
          icon: "web",
          text: "웹",
          items: [
            {
              title: "FrontEnd",
              icon: "mdi-television-guide",
              route: "/search",
              items: [
                {
                  title: "Vue",
                  icon: "mdi-television-guide",
                  route: "/search",
                },

                {
                  title: "React",
                  icon: "mdi-television-guide",
                  route: "/search",
                },
                {
                  title: "Angular",
                  icon: "mdi-television-guide",
                  route: "/search",
                },

                {
                  title: "HTML",
                  icon: "mdi-television-guide",
                  route: "/search",
                },

                {
                  title: "CSS",
                  icon: "mdi-television-guide",
                  route: "/search",
                },

                {
                  title: "JavaScript",
                  icon: "mdi-television-guide",
                  route: "/search",
                },
              ],
            },
            {
              title: "BackEnd",
              icon: "mdi-image-filter-drama",
              route: "/search",
              items: [
                {
                  title: "Spring",
                  icon: "mdi-image-filter-drama",
                  route: "/search",
                },
                {
                  title: "Django",
                  icon: "mdi-image-filter-drama",
                  route: "/search",
                },
                {
                  title: "Node js",
                  icon: "mdi-image-filter-drama",
                  route: "/search",
                },
                {
                  title: "Flask",
                  icon: "mdi-image-filter-drama",
                  route: "/search",
                },
              ],
            },
          ],
        },
        {
          icon: "apps",
          text: "어플",
          items: [
            {
              title: "Android",
              icon: "android",
              route: "/search",
              items: [
                {
                  title: "Java",
                  icon: "android",
                  route: "/search",
                },
                {
                  title: "Kotlin",
                  icon: "android",
                  route: "/search",
                },
              ],
            },
            {
              title: "iOS",
              icon: "mdi-apple",
              route: "/search",
              items: [
                {
                  title: "Swift",
                  icon: "mdi-apple",
                  route: "/search",
                },
              ],
            },
          ],
        },
      ],
      guide: [
        { icon: "record_voice_over", text: "직무소개" },
        { icon: "create", text: "적성검사" },
        { icon: "emoji_people", text: "로드맵" },
      ],
      tag: [],
    };
  },
};
</script>

<style scoped>
.side {
  width: 220px;
  position: fixed;
  z-index: 100;
  top: 75px;
  right: 0;
  bottom: 40px;
  overflow-x: hidden;
  overflow-y: auto;
  background-color: #f1f3f5;
}

@media screen and (max-width: 960px) {
  .side {
    width: 100%;
    position: inherit;
    top: 10px;
    z-index: 100;
    overflow-x: hidden;
    overflow-y: auto;
    background-color: #f1f3f5;
  }
}
.theme--light.v-list {
  background-color: #f1f3f5;
}
.v-application .primary--text {
  color: black !important;
}

.v-list {
  padding: 0;
}
</style>
