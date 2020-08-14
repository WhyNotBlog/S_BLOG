<template>
  <div>
    <h1 style="text-align:center; margin-bottom:10px">개발자 성향 테스트</h1>
    <survey :survey="survey" style="display:flex;justify-content: center;" />
  </div>
</template>

<script>
var json = {
  showProgressBar: "bottom",

  pages: [
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
          name: "Quality",
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
          name: "Quality",
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
          name: "Quality",
          isRequired: true,
          title:
            "달력을 봤는데 뭔가 이상한 것을 발견했어요. 가장 먼저 눈에 들어오는 것은?",
          choices: ["다른 폰트", "중복된 날짜"],
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
          name: "Quality",
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
          name: "Quality",
          isRequired: true,
          title: "이 중에서 운명적인 이끌림이 느껴지는것은? ",
          choices: [
            "컬러풀한 화면에 편리하게 정보 배치하기",
            "흑백 DB에서 원하는 정보 정리하기",
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
          name: "Quality",
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
          name: "Quality",
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
          name: "Quality",
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
          name: "Quality",
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
          name: "Quality",
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
          name: "Quality",
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
          name: "Quality",
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
          name: "Quality",
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
    };
  },
  created() {
    this.survey.pageNextText = "다음";
    this.survey.pagePrevText = "이전";
    this.survey.completeText = "결과보기";

    for (let index = 1; index <= this.survey.pageCount; index++) {
      this.survey.getQuestionByName("banner" + index).imageWidth = "100%";
      this.survey.getQuestionByName("banner" + index).imageHeight = "100%";
    }
    this.survey.setd;
    this.survey.render();
  },
  components: { Survey },
};
</script>

<style>
.sv_custom_header {
  background: none !important;
}
</style>
