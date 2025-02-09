<template>
  <div>
    <h2>Documents</h2>
    <div class="document-cards">
      <div
        v-for="document in documents"
        :key="document.id"
        class="document-card"
        @click="viewDocument(document.id)"
      >
        <div class="card-header">
          <p class="format-text">{{ getFileFormat(document.fileName) }}</p>
        </div>
        <div class="card-title">
          <h3>{{ document.fileName }}</h3>
        </div>
        <div class="card-footer">
          <div class="info">
            <p>{{ document.fileSize.toFixed(2) }} МБ · {{ formatDate(document.uploadedAt) }}</p>
          </div>
          <div class="actions">
            <button @click.stop="downloadDocument(document.id)" class="action-button">Скачать</button>
            <button @click.stop="deleteDocument(document.id)" class="action-button">Удалить</button>
          </div>
        </div>
      </div>

      <div class="document-card empty-card" @click="triggerFileInput">
        <div class="add-icon">+</div>
        <input type="file" ref="fileInput" @change="handleFileSelection" style="display: none" />
      </div>
    </div>
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
    triggerFileInput() {
      this.$refs.fileInput.click();
    },
    async handleFileSelection(event) {
      const file = event.target.files[0];
      if (file) {
        try {
          await api.uploadDocument(file, "");
          alert('Документ успешно добавлен!');
          await this.fetchDocuments();
        } catch (error) {
          console.error('Ошибка при загрузке документа:', error);
          alert('Ошибка при загрузке документа: ' + error.message);
        }
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
     formatDate(dateString) {
      const date = new Date(dateString);
      return date.toLocaleDateString();
    },
    viewDocument(id) {
      this.$router.push(`/documents/${id}`);
    },
    getFileFormat(fileName) {
      return fileName.split('.').pop().toUpperCase();
    },
  },
};
</script>

<style scoped>
.document-cards {
  display: flex;
  flex-wrap: wrap;
  gap: 20px;
  justify-content: center;
}

.document-card {
  width: 200px;
  height: 300px;
  padding: 15px;
  background-color: #f9f9f9;
  border-radius: 8px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
}

.document-card:hover {
  transform: translateY(-12px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4);
}

.card-header {
  width: 100%;
  height: 200px;
  background-color: #0091ea;
  border-radius: 8px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.format-text {
  font-size: 40px;
  color: white;
  font-weight: bold;
  text-transform: uppercase;
}

.card-title {
  text-align: center;
  margin-top: 10px;
  margin-bottom: 5px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 100%;
}

.card-title h3 {
  font-size: 16px;
  color: #333;
  font-weight: bold;
  margin: 0; 
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 40px;
  padding: 0 5px;
}

.info {
  font-size: 12px;
  color: #555;
  display: flex;
  align-items: center;
  gap: 4px;
}

.actions {
  display: flex;
  gap: 5px;
  margin-left: auto;
}

.action-button {
  padding: 6px 8px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 5px;
  cursor: pointer;
  font-size: 12px;
}

.action-button:hover {
  background-color: #369f6e;
}

.empty-card {
  background-color: #e0e0e0;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  font-size: 48px;
  font-weight: bold;
  color: #777;
  transition: background-color 0.3s ease;
}

.empty-card:hover {
  background-color: #d6d6d6;
}

.add-icon {
  font-size: 48px;
  color: #777;
}
 </style>