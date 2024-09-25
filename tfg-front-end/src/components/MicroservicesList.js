import React from 'react';
import Microservice from './Microservice'; // El componente Microservice que ya tienes

const MicroservicesList = () => {
  const microservices = [
    { id: 1, name: 'Auth Service', image: '/images/test-image.png' },
    { id: 2, name: 'Payment Service', image: '/images/payment-service.png' },
    { id: 3, name: 'User Service', image: '/images/user-service.png' },
    // Agrega más microservicios según necesites
  ];

  return (
    <div className="microservices-list">
      {microservices.map((microservice) => (
        <Microservice key={microservice.id} microservice={microservice} />
      ))}
    </div>
  );
};

export default MicroservicesList;

