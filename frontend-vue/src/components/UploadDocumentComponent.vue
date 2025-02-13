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

    <div class="field" v-if="file">
      <label for="tags">Tags:</label>
      <div class="tags-container">
        <div class="tag-chip" v-for="(tag, index) in tags" :key="index">
          {{ tag }}
          <span class="remove-tag" @click="removeTag(index)">×</span>
        </div>
      </div>
      <input
        id="tags"
        v-model="newTag"
        type="text"
        class="input-field"
        placeholder="Add a new tag"
        @keyup.enter="addTag"
      />
      <small class="hint">Press Enter to add a tag</small>
    </div>

    <div class="bottom-container" v-if="file">
      <div class="status-message" :class="{ 'error-message': uploadError }">
        <i class="fas" :class="uploadError ? 'fa-exclamation-circle' : 'fa-file-alt'"></i>
        {{ statusMessage }}
      </div>
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
      tags: [],
      newTag: "",
      uploadError: false,
      statusMessage: "File selected. Click upload button for saving file",
    };
  },
  methods: {
    handleFileSelection(event) {
      const file = event.target.files[0];
      if (file) {
        this.file = file;
        this.fileName = file.name.replace(/\.[^/.]+$/, "");
        this.description = "";
        this.tags = [];
        this.newTag = "";
        this.uploadError = false;
        this.statusMessage = "File selected. Click upload button for saving file";
      }
    },
    addTag() {
      const trimmedTag = this.newTag.trim();
      if (trimmedTag && trimmedTag.length <= 7 && !this.tags.includes(trimmedTag)) {
        this.tags.push(trimmedTag);
        this.newTag = "";
      } else if (trimmedTag.length > 7) {
        alert("Tag length should not exceed 7 characters.");
      }
    },
    removeTag(index) {
      this.tags.splice(index, 1);
    },
    async uploadDocument() {
      if (!this.file) {
        alert("Please choose a file to upload.");
        return;
      }
      this.uploadError = false;
      this.statusMessage = "Uploading file...";
      try {
        const ext = this.file.name.slice(this.file.name.lastIndexOf("."));
        const renamedFile = new File([this.file], this.fileName + ext, {
          type: this.file.type,
        });
        await api.uploadDocument(renamedFile, this.description, this.tags);
        this.statusMessage = "File uploaded successfully!";
        alert("Document uploaded successfully!");
        this.file = null;
        this.fileName = "";
        this.description = "";
        this.tags = [];
        this.newTag = "";
      } catch (error) {
        this.uploadError = true;
        this.statusMessage = "Error uploading file. Please try again.";
        console.error("Error uploading file:", error);
      }
    },
  },
};
</script>

<style scoped>
.tags-container {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 10px;
}
.tag-chip {
  background: linear-gradient(135deg, #8e44ad, #3498db);
  color: white;
  padding: 5px 10px;
  border-radius: 16px;
  font-size: 14px;
  display: flex;
  align-items: center;
}
.remove-tag {
  margin-left: 8px;
  cursor: pointer;
  color: white;
  font-weight: bold;
}
.remove-tag:hover {
  color: #ff4d4d;
}
.hint {
  font-size: 12px;
  color: #888;
}

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
}

.actions {
  display: flex;
  justify-content: center;
  width: 100%;
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

button {
  margin-top: 15px;
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 20px;
  padding-top: 10px;
  border-top: 1px solid #ccc;
  white-space: nowrap;
}

.status-message {
  color: #42b983;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.error-message {
  color: #ff4d4f;
}

.error-message i {
  color: #ff4d4f;
}

.primary-button {
  padding: 8px 16px;
  background-color: #3498db;
  color: white;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.primary-button:hover {
  background-color: #2980b9;
}

</style>
  