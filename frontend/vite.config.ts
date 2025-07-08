// vite.config.js
import { defineConfig } from 'vite';
import react from '@vitejs/plugin-react-swc';
import path from 'path';
import { componentTagger } from 'lovable-tagger';

export default defineConfig(({ mode }) => ({
  server: {
    host: '::',
    port: 8082,
  },
  plugins: [react(), mode === 'development' && componentTagger()].filter(Boolean),
  resolve: {
    alias: {
      '@': path.resolve(__dirname, './src'),
    },
  },
}));






// import { defineConfig } from "vite";
// import react from "@vitejs/plugin-react-swc";
// import path from "path";
// import { componentTagger } from "lovable-tagger";

// import { configDefaults } from 'vitest/config';


// export default defineConfig(({ mode }) => ({
//   server: {
//     host: "::",
//     port: 8082,
//   },
//   plugins: [
//     react(),
//     mode === 'development' && componentTagger(),
//   ].filter(Boolean),
//   resolve: {
//     alias: {
//       "@": path.resolve(__dirname, "./src"),
//     },
//   },
//   test: {
//     globals: true,
//     environment: "jsdom",
//     setupFiles: "./src/setUpTests.ts", // if you use setup
//     exclude: [...configDefaults.exclude, '**/e2e/**'],
//   },
// }));


// import { defineConfig } from 'vite';
// import react from '@vitejs/plugin-react-swc';
// import path from 'path';
// import { componentTagger } from 'lovable-tagger';

// export default defineConfig(({ mode }) => {
//   const isDev = mode === 'development' || mode === 'test';

//   return {
//     server: {
//       host: '::',
//       port: 8082,
//     },
//     plugins: [
//       react(),
//       isDev && componentTagger(),
//     ].filter(Boolean),
//     resolve: {
//       alias: {
//         '@': path.resolve(__dirname, './src'),
//       },
//     },
//     ...(isDev && {
//       test: {
//         globals: true,
//         environment: 'jsdom',
//         setupFiles: './src/setUpTests.ts',
//       }
//     })
//   };
// });


// import { defineConfig } from 'vite';
// import react from '@vitejs/plugin-react-swc';
// import path from 'path';
// import { componentTagger } from 'lovable-tagger';

// const isDev = process.env.NODE_ENV !== 'production';

// export default defineConfig({
//   server: {
//     host: '::',
//     port: 8082,
//   },
//   plugins: [
//     react(),
//     isDev && componentTagger()
//   ].filter(Boolean),
//   resolve: {
//     alias: {
//       '@': path.resolve(__dirname, './src'),
//     },
//   },
//   ...(isDev && {
//     test: {
//       globals: true,
//       environment: 'jsdom',
//       setupFiles: './src/setUpTests.ts',
//     }
//   }),
// });




