<template>
  <div>
    <h2>Список документов</h2>
    <ul>
      <li v-for="document in documents" :key="document.id">
        <strong>{{ document.fileName }}</strong> - {{ document.description }}
        <button @click="downloadDocument(document.id)">Скачать</button>
        <button @click="deleteDocument(document.id)">Удалить</button>
      </li>
    </ul>
  </div>
</template>

  <script>
  import api from '@/services/api';
  
  export default {
    data() {
    return {
      documents: [],
    };
  },
  async created() {
    await this.fetchDocuments();
  },
  methods: {
    async fetchDocuments() {
      try {
        const response = await api.getDocuments();
        this.documents = response.data;
      } catch (error) {
        console.error('Ошибка при загрузке документов:', error);
        alert('Ошибка при загрузке документов: ' + error.message);
      }
    },
    async downloadDocument(id) {
      try {
        const response = await api.downloadDocument(id);
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement('a');
        link.href = url;
        link.setAttribute('download', this.documents.find(doc => doc.id === id).fileName);
        document.body.appendChild(link);
        link.click();
        link.remove();
      } catch (error) {
        console.error('Ошибка при скачивании документа:', error);
        alert('Ошибка при скачивании документа: ' + error.message);
      }
    },
    async deleteDocument(id) {
      try {
        await api.deleteDocument(id);
        alert('Документ успешно удален!');
        await this.fetchDocuments();
      } catch (error) {
        console.error('Ошибка при удалении документа:', error);
        alert('Ошибка при удалении документа: ' + error.message);
      }
    },
  },
  };
  </script>

<style scoped>
ul {
  list-style-type: none;
  padding: 0;
}

li {
  margin: 10px 0;
  padding: 10px;
  border: 1px solid #ccc;
  border-radius: 5px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

button {
  margin-left: 10px;
  padding: 5px 10px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
}

button:hover {
  background-color: #369f6e;
}
</style>