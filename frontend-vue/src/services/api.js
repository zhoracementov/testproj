import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://localhost:8080',
    headers: {
        'Content-Type': 'application/json',
    },
});

export default {
    uploadDocument(file, description) {
        const formData = new FormData();
        formData.append('file', file);
        formData.append('description', description);

        return apiClient.post('/documents/upload', formData, {
            headers: {
                'Content-Type': 'multipart/form-data',
            },
        });
    },

    getDocuments() {
        return apiClient.get('/documents');
    },

    getDocumentById(id) {
        return apiClient.get(`/documents/${id}`);
    },

    updateDocument(id, data) {
        return apiClient.put(`/documents/${id}`, data, {
            headers: {
              'Content-Type': 'application/json',
            },
          });
    },

    deleteDocument(id) {
        return apiClient.delete(`/documents/${id}`);
    },

    downloadDocument(id) {
        return apiClient.get(`/documents/${id}/download`, {
            responseType: 'blob',
        });
    },
};