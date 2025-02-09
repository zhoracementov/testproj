<template>
  <div id="app">
    <FileUpload @file-uploaded="fetchDocuments" /> 
    <DocumentList ref="documentList" />
  </div>
</template>

<script>
import FileUpload from '@/components/uploadFileComponent.vue';
import DocumentList from './components/getDocumentsComponent.vue';

export default {
  name: 'App',
  components: {
    FileUpload,
    DocumentList,
  }, 
  methods: {
    fetchDocuments() {
      this.$refs.documentList.fetchDocuments();
    },
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  margin-top: 60px;
}
</style>