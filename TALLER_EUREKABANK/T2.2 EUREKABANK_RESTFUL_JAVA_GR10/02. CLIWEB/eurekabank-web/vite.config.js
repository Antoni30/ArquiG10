// vite.config.js
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react';

export default defineConfig({
  plugins: [react()],
  server: {
    port: 5173, // opcional, pero fijo
    proxy: {
      '/api': {
        target: 'http://10.240.0.218:8080',
        changeOrigin: true,
        rewrite: (path) => path.replace(/^\/api/, '/Rest_Eurekabank/api'),
      },
    },
  },
    build: {
    outDir: 'dist'
  }
});