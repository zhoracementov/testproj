<template>
  <div class="document-detail-container">
    <h2>{{ originalFileName }}</h2>

    <div class="field">
      <label for="fileName">Edit file name:</label>
      <input
        id="fileName"
        v-model="document.fileName"
        type="text"
        class="input-field"
        placeholder="Enter a new file name"
      />
    </div>

    <div class="field">
      <label for="description">Edit description:</label>
      <textarea
        id="description"
        v-model="document.description"
        class="textarea-field"
        rows="4"
        placeholder="Enter a new description"
      ></textarea>
    </div>

    <div class="actions">
      <button class="primary-button" @click="updateDocument">Update</button>
      <button class="secondary-button" @click="resetChanges">Reset</button>
      <button class="back-button" @click="goBack">Back</button>
    </div>

    <div class="action-icons">
      <i class="fas fa-download download-icon" @click="downloadDocument"></i>
      <i class="fas fa-trash delete-icon" @click="deleteDocument"></i>
    </div>
  </div>
</template>

<script>
import api from "@/services/api";

export default {
  data() {
    return {
      document: null,
      originalFileName: "",
      originalDescription: "",
    };
  },
  async created() {
    const id = this.$route.params.id;
    try {
      const response = await api.getDocumentById(id);
      this.document = response.data;
      this.originalFileName = this.document.fileName;
      this.originalDescription = this.document.description;
    } catch (error) {
      console.error("Ошибка загрузки документа:", error);
    }
  },
  methods: {
    async updateDocument() {
      // Проверка на изменения
      if (
        this.document.fileName === this.originalFileName &&
        this.document.description === this.originalDescription
      ) {
        alert("Нет изменений для обновления.");
        return; // Если изменений нет, операция не выполняется
      }

      // Валидация имени файла
      if (!this.validateFileName(this.document.fileName)) return;

      try {
        const payload = {
          fileName: this.document.fileName,
          description: this.document.description,
        };
        await api.updateDocument(this.document.id, payload);
        alert("Документ успешно обновлен!");
        this.originalFileName = this.document.fileName;
        this.originalDescription = this.document.description;
      } catch (error) {
        console.error("Ошибка обновления документа:", error);
      }
    },
    async deleteDocument() {
      const confirmDelete = confirm("Вы уверены, что хотите удалить этот документ?");
      if (!confirmDelete) return;

      try {
        await api.deleteDocument(this.document.id);
        alert("Документ успешно удален!");
        
        this.$router.go(-1);
        goBack();
      } catch (error) {
        console.error("Ошибка удаления документа:", error);
      }
    },
    async downloadDocument() {
      try {
        const response = await api.downloadDocument(this.document.id);
        const url = window.URL.createObjectURL(new Blob([response.data]));
        const link = document.createElement("a");
        link.href = url;
        link.setAttribute("download", this.document.fileName);
        document.body.appendChild(link);
        link.click();
      } catch (error) {
        console.error("Ошибка скачивания документа:", error);
      }
    },
    validateFileName(fileName) {
      const validPattern = /^[\p{L}\d\s._-]+$/u;
      if (!validPattern.test(fileName)) {
        alert(
          "Имя файла содержит недопустимые символы. Разрешены буквы, цифры, точки, подчеркивания и дефисы."
        );
        return false;
      }
      return true;
    },
    resetChanges() {
      this.document.fileName = this.originalFileName;
      this.document.description = this.originalDescription;
    },
    goBack() {
      this.$router.go(-1);
    },
  },
};
</script>

<style scoped>
.document-detail-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 25px;
  background-color: #f9f9f9;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  position: relative;
}
h2 {
  font-size: 24px;
  margin-bottom: 25px;
  color: #333;
  text-align: center;
  font-weight: 600;
}
.field {
  margin-bottom: 20px;
}
label {
  display: block;
  font-weight: bold;
  margin-bottom: 8px;
  color: #555;
}
.input-field,
.textarea-field {
  width: 100%;
  padding: 12px;
  border: 1px solid #ccc;
  border-radius: 6px;
  font-size: 16px;
  box-sizing: border-box;
  transition: border-color 0.3s;
}
.input-field:focus,
.textarea-field:focus {
  border-color: #42b983;
  outline: none;
  box-shadow: 0 0 4px rgba(66, 185, 131, 0.4);
}
.actions {
  margin-top: 25px;
  text-align: center;
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 15px;
}
button {
  margin-right: 12px;
  padding: 12px 18px;
  border: 1px solid #ccc;
  background-color: transparent;
  color: #333;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}
button:hover {
  background-color: #eee;
}
.secondary-button {
  border-color: #bbb;
}
.secondary-button:hover {
  background-color: #f4f4f4;
}
.danger-button {
  border-color: #f44336;
  color: #f44336;
}
.danger-button:hover {
  background-color: #f44336;
  color: white;
}
.back-button {
  border-color: #f44336;
  color: #f44336;
}
.back-button:hover {
  background-color: #f44336;
  color: white;
}

.action-icons {
  position: absolute;
  bottom: 20px;
  right: 20px;
  display: flex;
  gap: 15px;
  align-items: center;
}

.download-icon,
.delete-icon {
  font-size: 30px;
  color: #777;
  cursor: pointer;
}

.download-icon:hover,
.delete-icon:hover {
  color: #555;
}
</style>
