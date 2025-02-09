import { createRouter, createWebHistory } from 'vue-router';
import DocumentList from '@/components/getDocumentsListComponent.vue';
import DocumentDetail from '@/components/documentDetailComponent.vue';

const routes = [
  { path: '/', name: 'DocumentList', component: DocumentList },
  { path: '/documents/:id', name: 'DocumentDetail', component: DocumentDetail },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

export default router;
