import React from 'react';
import { useDrag } from 'react-dnd';
import './Microservice.css';

const Microservice = ({ microservice }) => {
  const [{ isDragging }, drag] = useDrag(() => ({
    type: 'MICROSERVICE',
    item: { id: microservice.id, name: microservice.name, image: microservice.image }, // Agregar 'name' e 'image' al objeto
    collect: (monitor) => ({
      isDragging: !!monitor.isDragging(),
    }),
  }));

  return (
    <div ref={drag} className={`microservice ${isDragging ? 'dragging' : ''}`}>
      <img src={microservice.image} alt={microservice.name} style={{ width: '100px', height: '50px' }} /> {/* Imagen con relación 2:1 */}
      <span>{microservice.name}</span> {/* Mostrar nombre */}
    </div>
  );
};

export default Microservice;

