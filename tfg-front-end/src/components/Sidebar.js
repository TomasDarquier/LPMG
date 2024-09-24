// src/components/Sidebar.js
import React from 'react';
import Microservice from './Microservice'; 
import './Sidebar.css';

const Sidebar = () => {
  const microservices = [
    { id: 1, name: 'Microservicio 1', image: '/path/to/image1.png' },
    { id: 2, name: 'Microservicio 2', image: '/path/to/image2.png' },
    { id: 3, name: 'Microservicio 3', image: '/path/to/image3.png' },
  ];

  return (
    <div className="sidebar">
      {microservices.map(microservice => (
        <Microservice key={microservice.id} microservice={microservice} />
      ))}
    </div>
  );
};

export default Sidebar;

