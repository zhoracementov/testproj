<template>
    <div>
      <input type="file" @change="handleFileUpload" />
      <input type="text" v-model="description" placeholder="Описание" />
      <button @click="uploadFile">Загрузить</button>
    </div>
  </template>
  
  <script>
  import api from '@/services/api';
  
  export default {
    data() {
      return {
        file: null,
        description: '',
      };
    },
    methods: {
      handleFileUpload(event) {
        this.file = event.target.files[0];
      },
      async uploadFile() {
        try {
          const response = await api.uploadDocument(this.file, this.description);
          console.log('Файл загружен:', response.data);
        } catch (error) {
          console.error('Ошибка при загрузке файла:', error);
        }
      },
    },
  };
  </script>