<template>
  <div class="side">
    <v-list v-show="side">
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

    <v-btn text style="float:right" @click="hide"
      ><v-icon>{{ icon }}</v-icon></v-btn
    >
  </div>
</template>

<script>
import axios from "axios";
export default {
  methods: {
    tagSearch(tag) {
      this.$router.push("/search/tag/" + tag);
    },
    hide() {
      if (this.side) {
        this.side = false;
        this.icon = "keyboard_arrow_down";
      } else {
        this.side = true;
        this.icon = "keyboard_arrow_up";
      }
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
      icon: "keyboard_arrow_up",
      side: true,
      category: [
        {
          icon: "format_shapes",
          text: "알고리즘",
          items: [
            {
              title: "기본 문제",
              icon: "mdi-login",
              items: [
                {
                  title: "기초",
                  icon: "mdi-shape-plus",
                  route: "/category/111",
                },
              ],
            },
            {
              title: "기출 문제",
              icon: "mdi-shape-plus",
              items: [
                {
                  title: "삼성",
                  icon: "mdi-shape-plus",
                  route: "/category/121",
                },
                {
                  title: "카카오",
                  icon: "mdi-shape-plus",
                  route: "/category/122",
                },
                {
                  title: "기타",
                  icon: "mdi-shape-plus",
                  route: "/category/123",
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

              items: [
                {
                  title: "Vue",
                  icon: "mdi-television-guide",
                  route: "/category/211",
                },

                {
                  title: "React",
                  icon: "mdi-television-guide",
                  route: "/category/212",
                },
                {
                  title: "Angular",
                  icon: "mdi-television-guide",
                  route: "/category/213",
                },

                {
                  title: "HTML",
                  icon: "mdi-television-guide",
                  route: "/category/214",
                },

                {
                  title: "CSS",
                  icon: "mdi-television-guide",
                  route: "/category/215",
                },

                {
                  title: "JavaScript",
                  icon: "mdi-television-guide",
                  route: "/category/216",
                },

                {
                  title: "JQuery",
                  icon: "mdi-television-guide",
                  route: "/category/217",
                },

                {
                  title: "Jsp",
                  icon: "mdi-television-guide",
                  route: "/category/218",
                },
              ],
            },
            {
              title: "BackEnd",
              icon: "mdi-image-filter-drama",

              items: [
                {
                  title: "Spring",
                  icon: "mdi-image-filter-drama",
                  route: "/category/221",
                },
                {
                  title: "Django",
                  icon: "mdi-image-filter-drama",
                  route: "/category/222",
                },
                {
                  title: "Node js",
                  icon: "mdi-image-filter-drama",
                  route: "/category/223",
                },
                {
                  title: "Flask",
                  icon: "mdi-image-filter-drama",
                  route: "/category/224",
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

              items: [
                {
                  title: "Java",
                  icon: "android",
                  route: "/category/311",
                },
                {
                  title: "Kotlin",
                  icon: "android",
                  route: "/category/312",
                },
              ],
            },
            {
              title: "iOS",
              icon: "mdi-apple",

              items: [
                {
                  title: "Swift",
                  icon: "mdi-apple",
                  route: "/category/321",
                },
              ],
            },
          ],
        },
      ],
      guide: [
        { icon: "record_voice_over", text: "직무소개" },
        { icon: "create", text: "적성검사", route: "/survey" },
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
  z-index: 1;
  top: 75px;
  right: 5px;
  bottom: 40px;
  overflow-x: hidden;
  overflow-y: auto;
  background-color: #f1f3f5;
}

@media screen and (max-width: 960px) {
  .side {
    width: 98%;
    position: inherit;
    top: 10px;
    left: 5px;
    z-index: 1;
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
