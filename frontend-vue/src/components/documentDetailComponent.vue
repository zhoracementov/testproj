<template>
  <div class="document-detail-container">
    <h2>Документ: {{ originalFileName }}</h2>

    <div class="field">
      <label for="fileName">Название файла:</label>
      <input
        id="fileName"
        v-model="document.fileName"
        type="text"
        class="input-field"
        placeholder="Введите новое название файла"
      />
    </div>

    <div class="field">
      <label for="description">Описание документа:</label>
      <textarea
        id="description"
        v-model="document.description"
        class="textarea-field"
        rows="4"
        placeholder="Введите новое описание документа"
      ></textarea>
    </div>

    <div class="actions">
      <button class="primary-button" @click="updateDocument">Обновить документ</button>
      <button class="secondary-button" @click="downloadDocument">Скачать документ</button>
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
    };
  },
  async created() {
    const id = this.$route.params.id;
    try {
      const response = await api.getDocumentById(id);
      this.document = response.data;
      this.originalFileName = this.document.fileName;
    } catch (error) {
      console.error("Ошибка загрузки документа:", error);
    }
  },
  methods: {
    async updateDocument() {
      if (!this.validateFileName(this.document.fileName)) return;
      try {
        const payload = {
          fileName: this.document.fileName,
          description: this.document.description,
        };
        await api.updateDocument(this.document.id, payload);
        alert("Документ успешно обновлен!");
        this.originalFileName = this.document.fileName;
      } catch (error) {
        console.error("Ошибка обновления документа:", error);
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
      const validPattern = /^[\p{L}\d._-]+$/u;
      if (!validPattern.test(fileName)) {
        alert(
          "Имя файла содержит недопустимые символы. Разрешены буквы, цифры, точки, подчеркивания и дефисы."
        );
        return false;
      }
      return true;
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
}
button {
  margin-right: 12px;
  padding: 12px 18px;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
}
button:hover {
  background-color: #369f6e;
}
.secondary-button {
  background-color: #ddd;
  color: #333;
}
.secondary-button:hover {
  background-color: #bbb;
}
</style>
