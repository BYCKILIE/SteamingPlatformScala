import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    // host: "localhost"
    // host: "192.168.0.132"
    host: "172.20.10.3"
  }
})
