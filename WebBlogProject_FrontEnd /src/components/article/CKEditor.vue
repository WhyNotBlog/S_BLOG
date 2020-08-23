<template>
  <div id="CKEditor">
      <ckeditor :editor="editor" v-model="editorData" :config="editorConfig"></ckeditor>
  </div>
</template>

<script src="../node_modules/@ckeditor/ckeditor5-build-classic/build/ckeditor.js"></script>
<script src="../node_modules/@ckeditor/ckeditor5-vue/dist/ckeditor.js"></script>

<script>
import ClassicEditor from '@ckeditor/ckeditor5-build-classic';

export default {
    name : 'CKEditor',
    data() {
        return {
            editor: ClassicEditor,
                editorData: '<p>Content of the editor.</p>',
                editorConfig: {
                    // The configuration of the editor.
                }
        }
    },
    methods : {

    },
    components : {

    }
}
</script>

<style>

</style>