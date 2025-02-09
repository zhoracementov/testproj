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
        <div class="card-header" :style="{ backgroundColor: getFormatColor(getFileFormat(document.fileName)) }">
          <p class="format-text">
            {{ getFileFormat(document.fileName) }}
          </p>
        </div>
        <div class="card-title">
          <h3>{{ document.fileName }}</h3>
        </div>
        <div class="card-footer">
          <div class="info">
            <p class="info-text">{{ document.fileSize.toFixed(2) }} Mb · {{ formatDate(document.uploadedAt) }}</p>
          </div>
          <div class="actions">
            <i @click.stop="downloadDocument(document.id)" class="fas fa-download action-icon"></i>
            <i @click.stop="deleteDocument(document.id)" class="fas fa-trash action-icon"></i>
          </div>
        </div>
        
        <!-- Tooltip with description -->
        <div class="tooltip" v-if="document.description">
          {{ document.description }}
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
    getFormatColor(format) {
      const formatColors = {
        PDF: '#FF6347',     // Tomato
        DOCX: '#4682B4',    // SteelBlue
        JPG: '#FFD700',     // Gold
        PNG: '#32CD32',     // LimeGreen
        TXT: '#8A2BE2',     // BlueViolet
        XLSX: '#20B2AA',    // LightSeaGreen
        default: '#A9A9A9', // DarkGray for unknown formats
      };
      return formatColors[format] || formatColors.default;
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
  border-radius: 8px;
  box-shadow: 0 6px 20px rgba(0, 0, 0, 0.3);
  cursor: pointer;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  transition: transform 0.2s ease, box-shadow 0.2s ease;
  position: relative; /* Для позиционирования tooltip */
}

.document-card:hover {
  transform: translateY(-12px);
  box-shadow: 0 12px 40px rgba(0, 0, 0, 0.4);
}

.card-header {
  width: 100%;
  height: 200px;
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
  padding: 5px;
  border-radius: 5px;
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

.info-text {
  font-size: 14px;
  font-weight: 500;  /* Сделать текст жирнее */
  color: #333;
  font-family: 'Arial', sans-serif; /* Более современный шрифт */
}

.actions {
  display: flex;
  gap: 15px;
  margin-left: auto;
}

.action-icon {
  font-size: 18px;
  color: #bdbdbd;
  cursor: pointer;
  transition: color 0.3s ease;
}

.action-icon:hover {
  color: #bdbdbd;
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

/* Стиль для мини-окна с описанием */
.tooltip {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  background-color: rgba(0, 0, 0, 0.7);
  color: white;
  padding: 10px;
  border-radius: 5px;
  max-width: 180px;
  font-size: 12px;
  word-wrap: break-word;
  display: none;
}

.document-card:hover .tooltip {
  display: block; /* Показываем tooltip при наведении */
}
</style>
