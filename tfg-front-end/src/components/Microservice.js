// src/components/Microservice.js
import React from 'react';
import { useDrag } from 'react-dnd';
import './Microservice.css';

const Microservice = ({ microservice }) => {
  const [{ isDragging }, drag] = useDrag(() => ({
    type: 'MICROSERVICE',
    item: { id: microservice.id },
    collect: (monitor) => ({
      isDragging: !!monitor.isDragging(),
    }),
  }));

  return (
    <div ref={drag} className={`microservice ${isDragging ? 'dragging' : ''}`}>
      <img src={microservice.image} alt={microservice.name} />
      <span>{microservice.name}</span>
    </div>
  );
};

export default Microservice;

