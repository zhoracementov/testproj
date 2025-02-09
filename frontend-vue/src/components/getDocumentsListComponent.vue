<template>
  <div>
    <h2>Список документов</h2>
    <table>
      <thead>
        <tr>
          <th>Имя файла</th>
          <th>Описание</th>
          <th>Размер файла (МБ)</th>
          <th>Дата загрузки</th>
          <th>Действия</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="document in documents" :key="document.id">
          <td>{{ document.fileName }}</td>
          <td>{{ document.description }}</td>
          <td>{{ document.fileSize.toFixed(2) }}</td>
          <td>{{ formatDate(document.uploadedAt) }}</td>
          <td>
            <router-link :to="'/documents/' + document.id">
              <button>Просмотр / Редактировать</button>
            </router-link>
            <button @click="downloadDocument(document.id)">Скачать</button>
            <button @click="deleteDocument(document.id)">Удалить</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import api from '@/services/api'; // Импортируем api.js

export default {
  data() {
    return {
      documents: [], // Список документов
    };
  },
  async created() {
    await this.fetchDocuments(); // Загружаем документы при создании компонента
  },
  methods: {
    // Метод для получения списка документов
    async fetchDocuments() {
      try {
        const response = await api.getDocuments();
        this.documents = response.data;
      } catch (error) {
        console.error('Ошибка при загрузке документов:', error);
        alert('Ошибка при загрузке документов: ' + error.message);
      }
    },

    // Метод для скачивания документа
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

    // Метод для удаления документа
    async deleteDocument(id) {
      try {
        await api.deleteDocument(id);
        alert('Документ успешно удален!');
        await this.fetchDocuments(); // Обновляем список документов после удаления
      } catch (error) {
        console.error('Ошибка при удалении документа:', error);
        alert('Ошибка при удалении документа: ' + error.message);
      }
    },

    // Метод для форматирования даты
    formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleString(); // Форматируем дату и время
    },
  },
};
</script>

<style scoped>
table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 20px;
}

th, td {
  padding: 10px;
  text-align: left;
  border-bottom: 1px solid #ddd;
}

th {
  background-color: #f4f4f4;
  font-weight: bold;
}

tr:hover {
  background-color: #f9f9f9;
}

button {
  margin-left: 5px;
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
