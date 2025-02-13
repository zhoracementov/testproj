<template>
  <div class="document-upload-container" :class="{ 'expanded': file }">
    <h2>{{ fileName || 'Upload New Document' }}</h2>

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

    <div class="field" v-if="file">
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

    <div class="field" v-if="file">
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

    <div class="bottom-container" v-if="file">
      <div class="status-message" :class="{ 'error-message': uploadError }">
        <i class="fas" :class="uploadError ? 'fa-exclamation-circle' : 'fa-file-alt'"></i>
        {{ statusMessage }}
      </div>
      <div class="actions">
        <button class="primary-button" @click="uploadDocument">Upload</button>
      </div>
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
      uploadError: false,
      statusMessage: "File selected. Click upload button for saving file",
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
        if (!this.isFileAccessible(file)) {
          this.uploadError = true;
          this.statusMessage = "Error: File is not accessible. Please choose another file.";
          return;
        }

        this.file = file;
        this.fileName = file.name.replace(/\.[^/.]+$/, "");
        this.description = "";
        this.uploadError = false;
        this.statusMessage = "File selected. Click upload button for saving file";
      }
    },
    isFileAccessible(file) {
      try {
        const reader = new FileReader();
        reader.readAsArrayBuffer(file);
        return true;
      } catch (error) {
        console.error("File is not accessible:", error);
        return false;
      }
    },
    goBack() {
      this.$router.go(-1);
    },
    resetData() {
      this.file = null;
      this.fileName = null;
      this.description = null;
      this.uploadError = false;
      this.statusMessage = "";
    },
    async uploadDocument() {
      if (!this.file) {
        alert("Please choose a file to upload.");
        return;
      }

      if (!this.isFileAccessible(this.file)) {
        this.uploadError = true;
        this.statusMessage = "Error: File is not accessible. Please choose another file.";
        return;
      }

      this.uploadError = false;
      this.statusMessage = "Uploading file...";

      try {
        const ext = this.file.name.slice(this.file.name.lastIndexOf("."));
        const renamedFile = new File([this.file], this.fileName + ext, {
          type: this.file.type,
        });

        let tags = ["test1", "ttt1"];

        await api.uploadDocument(renamedFile, this.description, tags);
        this.statusMessage = "File uploaded successfully!";
        alert("Document uploaded successfully!");

        this.resetData();
        this.goBack();
      } catch (error) {
        this.uploadError = true;
        if (error.response) {
          if (error.response.status === 415) {
            this.statusMessage = "Error: Unsupported file format.";
          } else if (error.response.status === 413) {
            this.statusMessage = "Error: File size is too large.";
          } else {
            this.statusMessage = `Error: ${error.response.data || error.message}`;
          }
        } else {
          this.statusMessage = "Error: Failed to upload file. Please try again.";
          console.error("Error uploading file:", error);
        }
      }
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
  position: relative;
  min-height: 200px;
  transition: min-height 0.3s ease;
}

.document-upload-container.expanded {
  min-height: 400px;
}

.status-message {
  color: #42b983;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  margin-left: 35px;
  margin-top: 8px;
}

.error-message {
  color: #ff4d4f;
}

.error-message i {
  color: #ff4d4f;
}

.status-message {
  color: #42b983;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  margin-left: 35px;
  margin-top: 8px;
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
  margin: 0;
  display: flex;
  align-items: center;
}

button {
  margin-top: 10px;
  padding: 6px 20px;
  border: 1px solid #ccc;
  color: white;
  border-radius: 8px;
  font-size: 16px;
  cursor: pointer;
  transition: background-color 0.3s;
  background-color: #3498db;
}

.bottom-container {
  position: absolute;
  left: 10px;
  bottom: 25px;
  width: calc(100% - 50px);
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 20px;
}

button:hover {
  background-color: #2980b9;
}

</style>
  