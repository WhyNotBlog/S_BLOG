<template>
  <div>
    <br />
    <h1>개발자 성향 테스트</h1>
    <v-container>
      <survey :survey="survey" style="display:flex;justify-content: center;" />
    </v-container>
  </div>
</template>

<script>
var choices = [
  { pages: ["신경쓰인다", "이게 왜? 잠기기만 하면 된다"] },
  { pages: ["정리된 자료로 ppt 만들기", "자료조사 및 정리"] },
  { pages: ["시작 요일", "중복된 날짜"] },
  { pages: ["빨강", "초록"] },
  {
    pages: [
      "컬러풀한 화면을 보며 사용자가 보기 좋고 편리하게 정보 배치하기",
      "흑백으로 이루어진 데이터베이스에서 원하는 정보만 쏙 뽑아내서 정리하기",
    ],
  },
  { pages: ["보컬할래", "베이스기타 할래"] },

  { pages: ["잘 모르겠다 얼른 닫는다", "해커 빙의한다 나 좀 멋있는듯"] },

  {
    pages: [
      "전세계 컴퓨터에 침입하여 세계를 정복할 수 있는 AI 바이러스",
      "온천을 터뜨려 세계를 정복할 수 있는 거대로봇",
    ],
  },

  { pages: ["귀요미", "2"] },

  { pages: ["알람을 수시로 확인한다", "올리고 끝"] },

  { pages: ["공감해준다", "해답을 찾아준다"] },

  { pages: ["Yes", "No"] },

  { pages: ["솔직히 즐긴다. 짜릿해", "으으 부담스러워"] },
];

var json = {
  firstPageIsStarted: true,
  startSurveyText: "시작하기",
  pages: [
    {
      questions: [
        {
          type: "image",
          name: "banner0",
          imageLink: "https://blog.hyperiondev.com/wp-content/uploads/2018/10/Blog-Gif.gif",
        },
        {
          type: "html",
          html:
            "<b style='font-size:1rem'>과연 나의 개발자 성향은 무엇일까?<br>프론트엔드(FRONT END)? 백엔드(BACK END)? 풀스택(FULL STACK)?<br>무엇이 나올지 기대되지 않나요? 나의 개발자의 길을 찾아서..!</b> <br>*본 테스트는 개발자의 길에 앞서 가볍게 보는 성향검사입니다.",
        },
      ],
    },
    {
      questions: [
        {
          type: "image",
          name: "banner1",
          imageLink: require("@/assets/survey01.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "1",
          isRequired: true,
          title: "배가아파서 화장실에 간 당신 눈앞에 보이는 문고리의 모습",
          choices: ["이게 왜? 잠기기만 하면 된다", "신경쓰인다"],
        },
      ],
    },
    {
      questions: [
        {
          type: "image",
          name: "banner2",
          imageLink: require("@/assets/survey02.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "2",
          isRequired: true,
          title: "조별과제에서 당신의 역할은?",
          choices: ["자료조사 및 정리", "정리된 자료로 ppt 만들기"],
        },
      ],
    },

    {
      questions: [
        {
          type: "image",
          name: "banner3",
          imageLink: require("@/assets/survey03.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "3",
          isRequired: true,
          title: "달력을 봤는데 뭔가 이상한 것을 발견했어요. 가장 먼저 눈에 들어오는 것은?",
          choices: ["시작 요일", "중복된 날짜"],
        },
      ],
    },

    {
      questions: [
        {
          type: "image",
          name: "banner4",
          imageLink: require("@/assets/survey04.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "4",
          isRequired: true,
          title: "무슨 색인가요?",
          choices: ["빨강", "초록"],
        },
      ],
    },
    {
      questions: [
        {
          type: "image",
          name: "banner5",
          imageLink: require("@/assets/survey05.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "5",
          isRequired: true,
          title: "이 중에서 운명적인 이끌림이 느껴지는것은? ",
          choices: [
            "컬러풀한 화면을 보며 사용자가 보기 좋고 편리하게 정보 배치하기",
            "흑백으로 이루어진 데이터베이스에서 원하는 정보만 쏙 뽑아내서 정리하기",
          ],
        },
      ],
    },

    {
      questions: [
        {
          type: "image",
          name: "banner6",
          imageLink: require("@/assets/survey06.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "6",
          isRequired: true,
          title: "밴드에 들어가고 싶어요",
          choices: ["베이스기타 할래", "보컬할래"],
        },
      ],
    },

    {
      questions: [
        {
          type: "image",
          name: "banner7",
          imageLink: require("@/assets/survey07.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "7",
          isRequired: true,
          title: "이 화면을 보고 드는 생각은?",
          choices: ["잘 모르겠다 얼른 닫는다", "해커 빙의한다 나 좀 멋있는듯"],
        },
      ],
    },

    {
      questions: [
        {
          type: "image",
          name: "banner8",
          imageLink: require("@/assets/survey08.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "8",
          isRequired: true,
          title: "둘 중 한가지를 만들수 있다면?",
          choices: [
            "전세계 컴퓨터에 침입하여 세계를 정복할 수 있는 AI 바이러스",
            "온천을 터뜨려 세계를 정복할 수 있는 거대로봇",
          ],
        },
      ],
    },

    {
      questions: [
        {
          type: "image",
          name: "banner9",
          imageLink: require("@/assets/survey09.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "9",
          isRequired: true,
          title: "1더하기1 은?",
          choices: ["2", "귀요미"],
        },
      ],
    },

    {
      questions: [
        {
          type: "image",
          name: "banner10",
          imageLink: require("@/assets/survey10.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "10",
          isRequired: true,
          title: "SNS에 글 사진을 올리면?",
          choices: ["알람을 수시로 확인한다", "올리고 끝"],
        },
      ],
    },
    {
      questions: [
        {
          type: "image",
          name: "banner11",
          imageLink: require("@/assets/survey11.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "11",
          isRequired: true,
          title: "고민하는 친구가 있을때 당신은?",
          choices: ["공감해준다", "해답을 찾아준다"],
        },
      ],
    },

    {
      questions: [
        {
          type: "image",
          name: "banner12",
          imageLink: require("@/assets/survey12.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "12",
          isRequired: true,
          title: "미적감각이 있다는 이야기를 종종 듣는다",
          choices: ["Yes", "No"],
        },
      ],
    },

    {
      questions: [
        {
          type: "image",
          name: "banner13",
          imageLink: require("@/assets/survey13.png"),
          imageWidth: "200",
        },

        {
          type: "radiogroup",
          name: "13",
          isRequired: true,
          title: "사람들이 주목할때",
          choices: ["솔직히 즐긴다. 짜릿해", "으으 부담스러워"],
        },
      ],
    },
  ],
};
import "survey-vue/survey.css";
let Survey = SurveyVue.Survey; // surveyJS에서 Survey 컴포넌트만 따로 빼낸다
import * as SurveyVue from "survey-vue"; // surveyJS를 import한다

var model = new SurveyVue.Model(json);

export default {
  data() {
    return {
      survey: model,
      resultModal: false,
    };
  },
  mounted() {
    this.survey.pageNextText = "다음";
    this.survey.pagePrevText = "이전";
    this.survey.completeText = "결과보기";

    this.survey.render();

    for (let index = 0; index <= 13; index++) {
      this.survey.getQuestionByName("banner" + index).imageWidth = "100%";
      this.survey.getQuestionByName("banner" + index).imageHeight = "100%";
    }

    this.survey.onComplete.add((result) => {
      let front = 0;
      let back = 0;
      for (let key in result.data) {
        if (choices[Number(key) - 1].pages[0] == result.data[key]) {
          front++;
        } else {
          back++;
        }
      }

      if (front - back > 4) {
        this.survey.completedHtml =
          "<h2 style='font-size:2rem'>나의 성향 결과는..? <b style='color:#726a95;'> 프론트엔드</b> 입니다!</h2><br><img style='margin-bottom:5px;' width=90% src='https://miro.medium.com/max/1400/1*XR3rTO1O_RM69jFDcez7cw.gif' /><br><div>사용자에게 직접 눈으로 보이고 다르는 부분</div><div>즉, 현재 화면에서 보이는 글꼴, 색상, 메뉴, 그림 등 모든 것을 만드는 분야입니다.</div><div>대표적으로는 HTML, CSS, JavaScript 언어로 구성되어 있으며 이를 활용하여 개발합니다.</div>";
      } else if (back - front > 4) {
        this.survey.completedHtml =
          "<h2 style='font-size:2rem'>나의 성향 결과는..?<b style='color:#726a95;'> 백엔드</b> 입니다!</h2><br><img style='margin-bottom:5px;' width=90% src='http://www.ixploretech.com/img/electronics.gif' /><br><div>사용자가 다루는 부분에서 요청을 처리하고 그 결과를 프론트엔드에게 전달해 주는 서버의 역할</div><div>즉, 이는 눈에 보이지 않는 서버 부분과 데이터를 저장하는 공간이 DB 부분입니다.</div><div>대표적으로는 JavaScript, Python, Java, PHP, Ruby 등의 언어 활용 제작,</div><div>DB는 MySQL, MongoDB 등을 활용하여 개발합니다.</div>";
      } else {
        this.survey.completedHtml =
          "<h2 style='font-size:2rem'>나의 성향 결과는..? <b style='color:#726a95;'> 풀스택</b> 입니다!</h2><br><img style='margin-bottom:5px;' width=90% src='http://www.ixploretech.com/img/gif9.gif'><br><div>프론트엔드와 백엔드 개발을 통해서 웹 or 앱이 나오게 됩니다.</div><div>이 과정에서 각 분야의 프로그래밍 언어와</div><div>이를 쉽게 만들 수 있게 도와주는 도구인 프레임워크와 DB에 대한 지식이 필요합니다.</div><div>그리고 프론트엔드/백엔드 개발을 모두 합쳐서 풀스택이라고 하고</div><div>이를 할 수 있는 개발자를 풀스택 개발자라고 합니다.</div>";
      }
    });
  },
  components: { Survey },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Sunflower:wght@300&display=swap");
.sv_custom_header {
  display: none;
}

.sv_main {
  background: none;
}

.sv_q,
.sv_qstn,
.sv_row,
.sv_nav {
  min-width: 100% !important;
  font-size: 1rem;
}
.sv_container {
  width: 50vw;
  height: 100%;
}

#sq_102,
#sq_104,
#sq_106,
#sq_108,
#sq_110,
#sq_112,
#sq_114,
#sq_116,
#sq_118,
#sq_120,
#sq_122,
#sq_124,
#sq_126,
#sq_128 {
  display: flex !important;
  justify-content: center;
}

@media screen and (max-width: 1020px) {
  .sv_container {
    width: 90vw;
    height: 100%;
  }
}

.sv_main .sv_container .sv_body .sv_p_root .sv_q .sv_completed_page {
  padding: 1em;
}

h1 {
  font-family: "Sunflower";
  margin-bottom: 10px;
  margin-left: 5%;
}

.sv_body {
  border-top: 3px solid #726a95 !important;
  border-radius: 4px;
  box-shadow: 0 3px 6px 1px #ccc;
}

.sv_main input[type="button"],
.sv_main button,
.sv_main input[type="button"]:hover,
.sv_main button:hover {
  background-color: #726a95;
}

.sv_main,
.sv_container,
.sv_body,
.sv_p_root,
.sv_q {
  padding: 0.5em 1em 1em 1em;
}
</style>
