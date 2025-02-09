<template>
  <div id="app">
    <FileUpload />
  </div>
</template>

<script>
import FileUpload from '@/components/uploadFileComponent.vue';

export default {
  name: 'App',
  components: {
    FileUpload,
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