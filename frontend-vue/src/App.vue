<template>
  <div id="app">
    <nav>
      <router-link 
        to="/" 
        v-if="$route.path !== '/'" 
        class="back-link">
        ← Back to Documents
      </router-link>
    </nav>
    <router-view />
  </div>
</template>

<script>
export default {
  name: 'App',
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px;
  text-align: center;
}

nav {
  margin-bottom: 30px;
  text-align: left;
}

.back-link {
  text-decoration: none;
  color: #3498db;
  font-weight: 500;
  transition: color 0.3s ease;
  display: inline-block;
  padding: 8px 15px;
  border-radius: 5px;
  background-color: #f8f9fa;
}

.back-link:hover {
  color: #2980b9;
  background-color: #e9ecef;
  text-decoration: none;
}
</style>