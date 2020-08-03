<template>
    <div>
        <viewer 
        :initialValue="viewerInitText"
        :value="viewerText"
        height="500px"
        />
    </div>
</template>

<script>
import 'codemirror/lib/codemirror.css';
import '@toast-ui/editor/dist/toastui-editor.css';
import { Viewer } from '@toast-ui/vue-editor';

export default {
    name : 'MDPriview',
    data() {
        return {
            viewerText : '',
        }
    },
    components : {
        'viewer' : Viewer,
    },
    props : {
        viewerInitText : {
            type : String,
            required : true,
        }
    },
    methods : {

    },
}
</script>

<style>

</style>