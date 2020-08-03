<template>
    <div>
        <form enctype="multipart/form-data">
            <input type="file" @change="fileUpload($event)" />
        </form>
        <button v-on:click="addItem">Submit</button>
    </div>
</template>

<script>
import axios from "axios";
export default {
    data() {
        return {
            selectedFile: "",
        };
    },
    methods: {
        fileUpload(event) {
            this.selectedFile = event.target.files[0];
            console.log(this.selectedFile);
        },
        addItem() {
            const data = new FormData(); // 서버로 전송할 폼데이터
            const file = this.selectedFile; // 선택된 파일객체
            data.append("file", file); // 폼데이터에 파일을 추가
            console.log(file);
            console.log(data);
            //   데이터를 서버로 전송하는 코드 추가
            axios
                .post("http://localhost:8081/article/upload", data, {
                    headers: { "Content-Type": "multipart/form-data" },
                })
                .then((res) => {
                    alert(res);
                })
                .catch((err) => {
                    alert(err);
                });
        },
    },
};
</script>

<style></style>
