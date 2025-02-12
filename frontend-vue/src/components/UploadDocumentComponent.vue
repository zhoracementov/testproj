<template>
    <div class="document-upload-container">
      <h2>Upload New Document</h2>
  
      <!-- Кнопка для выбора файла с иконкой -->
      <div class="field">
        <label for="fileUpload" class="upload-label">
          <i class="fas fa-cloud-upload-alt"></i> Choose File
        </label>
        <input
          id="fileUpload"
          ref="fileInput"
          type="file"
          @change="handleFileSelection"
          class="file-input"
        />
      </div>
  
      <!-- Поля для ввода названия и описания -->
      <div class="field">
        <label for="fileName">File Name:</label>
        <input
          id="fileName"
          v-model="fileName"
          type="text"
          class="input-field"
          placeholder="Enter file name"
          :disabled="!file"
        />
      </div>
  
      <div class="field">
        <label for="description">Description:</label>
        <textarea
          id="description"
          v-model="description"
          class="textarea-field"
          rows="4"
          placeholder="Enter description"
          :disabled="!file"
        ></textarea>
      </div>
  
      <div class="actions">
        <button class="primary-button" @click="uploadDocument">Upload</button>
      </div>
    </div>
  </template>
  
  <script>
  import api from "@/services/api";
  
  export default {
    data() {
      return {
        fileName: "",
        description: "",
        file: null,
      };
    },
    methods: {
      triggerFileInput() {
        this.resetData();
        this.$refs.fileInput.click();
      },
      handleFileSelection(event) {
        const file = event.target.files[0];
        if (file) {
          this.file = file;
          this.fileName = file.name.replace(/\.[^/.]+$/, "");
          this.description = "";
        }
      },
      goBack() {
        this.$router.go(-1);
      },
      resetData() {
        this.file = null;
        this.fileName = null;
        this.description = null;
      },
      async uploadDocument() {
        if (!this.file) {
          alert("Please choose a file to upload.");
          return;
        }
  
        try {

            const ext = this.file.name.slice(this.file.name.lastIndexOf('.'));
            const renamedFile = new File([this.file], this.fileName + ext, {
            type: this.file.type,
          });

          await api.uploadDocument(renamedFile, this.description);
          alert("Document uploaded successfully!");
            
          this.resetData();
          
          this.goBack();
          
        } catch (error) {
            if (error.response) {
              if (error.response.status === 415) {
                alert("Ошибка: неподдерживаемый формат файла.");
              }
              else if (error.response.status == 413) {
                alert("Error: too much file size");
              } else {
                alert(`Ошибка загрузки файла: ${error.response.data || error.message}`);
              }
            } else {
              console.error("Ошибка при загрузке файла. Возможно он был слишком большим:", error);
              alert("Произошла ошибка при загрузке файла. Возможно он был слишком большим");
            }}
      },
    },
  };
  </script>
  
  <style scoped>
  .document-upload-container {
    max-width: 600px;
    margin: 0 auto;
    padding: 25px;
    background-color: #e8e8e8;
    border-radius: 10px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  }
  
  h2 {
    font-size: 24px;
    margin-bottom: 25px;
    color: #222;
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
    color: #444;
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
  
  .textarea-field {
    resize: none;
  }
  
  .file-input {
    display: none;
  }
  
  .upload-label {
    display: inline-block;
    background-color: #3498db;
    color: white;
    padding: 12px 24px;
    font-size: 16px;
    border-radius: 6px;
    cursor: pointer;
    transition: background-color 0.3s;
  }
  
  .upload-label:hover {
    background-color: #2980b9;
  }
  
  .upload-label i {
    margin-right: 10px;
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

  </style>
  