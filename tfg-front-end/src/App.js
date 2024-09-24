// src/App.js
import React from 'react';
import { DndProvider } from 'react-dnd';
import { HTML5Backend } from 'react-dnd-html5-backend';
import Sidebar from './components/Sidebar'; 
import Canvas from './components/Canvas'; 
import TopBar from './components/TopBar'; 
import './App.css'; 

const App = () => {
  return (
    <DndProvider backend={HTML5Backend}>
      <div className="app-container">
        <TopBar />
        <div className="main-content">
          <Sidebar />
          <Canvas />
        </div>
      </div>
    </DndProvider>
  );
};

export default App;

