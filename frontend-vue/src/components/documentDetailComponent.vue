<template>
  <div class="document-detail-container">
    <h2>{{ removeFileExtension(originalFileName) }}</h2>

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
        placeholder="Enter a new description">
      </textarea>
    </div>

    <div class="field">
      <label for="tags">Edit tags:</label>
      <div class="tags-container">
        <div class="tag-chip" v-for="(tag, index) in document.tags" :key="index">
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
      document: {
        tags: []
      },
      originalFileName: "",
      originalDescription: "",
      originalTags: [],
      newTag: ""
    };
  },
  async created() {
    const id = this.$route.params.id;
    try {
      const response = await api.getDocumentById(id);
      this.document = response.data;
      this.originalFileName = this.document.fileName;
      this.originalDescription = this.document.description;
      this.originalTags = [...this.document.tags]; // Copy tags on load
    } catch (error) {
      console.error("Error loading document:", error);
    }
  },
  methods: {
    removeFileExtension(fileName) {
      return fileName.substring(0, fileName.lastIndexOf('.')) || fileName;
    },
    async updateDocument() {
      if (
        this.document.fileName === this.originalFileName &&
        this.document.description === this.originalDescription &&
        JSON.stringify(this.document.tags) === JSON.stringify(this.originalTags)
      ) {
        alert("No changes to update.");
        return;
      }

      try {
        const payload = {
          fileName: this.document.fileName,
          description: this.document.description,
          tags: this.document.tags
        };
        await api.updateDocument(this.document.id, payload);
        alert("Document updated successfully!");
        this.originalFileName = this.document.fileName;
        this.originalDescription = this.document.description;
        this.originalTags = [...this.document.tags]; // Update saved tags
      } catch (error) {
        console.error("Error updating document:", error);
      }
    },
    addTag() {
      const trimmedTag = this.newTag.trim();
      if (trimmedTag && !this.document.tags.includes(trimmedTag)) {
        this.document.tags.push(trimmedTag);
        this.newTag = "";
      }
    },
    removeTag(index) {
      this.document.tags.splice(index, 1);
    },
    resetChanges() {
      this.document.fileName = this.originalFileName;
      this.document.description = this.originalDescription;
      this.document.tags = [...this.originalTags]; // Reset tags
    },
    goBack() {
      this.$router.go(-1);
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
        console.error("Error downloading document:", error);
      }
    },
    async deleteDocument() {
      const confirmDelete = confirm("Are you sure you want to delete this document?");
      if (!confirmDelete) return;

      try {
        await api.deleteDocument(this.document.id);
        alert("Document deleted successfully!");
        this.$router.go(-1);
      } catch (error) {
        console.error("Error deleting document:", error);
      }
    }
  }
};
</script>

<style scoped>
.document-detail-container {
  max-width: 600px;
  margin: 0 auto;
  padding: 25px;
  background-color: #e8e8e8;
  border-radius: 10px;
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
  position: relative;
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
.secondary-button {
  border-color: #bbb;
}
.secondary-button:hover {
  background-color: #f4f4f4;
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