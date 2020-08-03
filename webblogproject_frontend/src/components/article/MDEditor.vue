<template>
  <div>
    <editor 
    :value="editorText"
    :options="editorOptions"
    :html="editorHtml"
    :visible="editorVisible"
    previewStyle="vertical"
    ref="tuiEditor"
    height="500px"
    mode="wysiwyg"
    @change="onchange"
    />

    <v-btn @click="renderHtml(editorHtml)">
        html 태그를 반영해보자!
    </v-btn>

    <div id="render">

    </div>
  </div>
</template>

<script>
import 'codemirror/lib/codemirror.css';
import '@toast-ui/editor/dist/toastui-editor.css';
import { Editor } from '@toast-ui/vue-editor';

export default {
    name : 'MDEditor',
    data () {
        return {
            editorText: '',
            editorOptions: {
                hideModeSwitch: true
            },
            editorHtml: '',
            editorMarkdown: '',
            editorVisible: true,
            viewerText : '',
        }
    },
    components : {
        'editor' : Editor,
    },
    methods : {
        onchange() {
            let html = this.$refs.tuiEditor.invoke('getHtml');
            let markdown = this.$refs.tuiEditor.invoke('getMarkdown');
            this.editorHtml = html;
            this.editorMarkdown = markdown;
        },
        renderHtml(editorHtml) {
            const renderSpot = document.getElementById('render');
            renderSpot.innerHTML = editorHtml;
            console.log(this.editorHtml);
            console.log(this.editorMarkdown);          
        }
    }
}
</script>

<style>

</style>