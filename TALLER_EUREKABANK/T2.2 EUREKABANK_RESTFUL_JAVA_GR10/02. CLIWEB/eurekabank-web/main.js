// main.js
import { app, BrowserWindow } from 'electron';
import path from 'path';
import process from 'process';

// Obtener __dirname en ES modules
const __filename = new URL(import.meta.url).pathname;
const __dirname = path.dirname(__filename);

function createWindow() {
  const win = new BrowserWindow({
    width: 1200,
    height: 800,
    webPreferences: {
      contextIsolation: true,
      preload: path.join(__dirname, 'preload.js'),
    },
  });

  if (app.isPackaged) {
    // Producción: carga los archivos estáticos de la carpeta dist
    win.loadFile(path.join(__dirname, 'dist', 'index.html'));
  } else {
    // Desarrollo: carga desde el servidor de Vite
    win.loadURL('http://localhost:5173');
  }
}

app.whenReady().then(() => {
  createWindow();

  app.on('activate', () => {
    if (BrowserWindow.getAllWindows().length === 0) createWindow();
  });
});

app.on('window-all-closed', () => {
  if (process.platform !== 'darwin') app.quit();
});