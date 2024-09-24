import React, { useState } from 'react';
import './TopBar.css';

const TopBar = () => {
  const [showModal, setShowModal] = useState(false);

  const handleLogoutClick = () => {
    setShowModal(true);
  };

  const handleConfirmLogout = () => {
    console.log('Sesión cerrada');
    setShowModal(false);
    // Aquí puedes agregar la lógica para redirigir o cerrar sesión
  };

  const handleCancelLogout = () => {
    setShowModal(false);
  };

  return (
    <>
      <div className="top-bar">
        <div className="top-bar-options">
          <div className="option">Documentación</div>
          <div className="option">Guía de uso</div>
          <div className="option">Perfil</div>
        </div>
        <button className="logout-button" onClick={handleLogoutClick}>
          Cerrar sesión
        </button>
      </div>
      
      {showModal && (
        <div className="modal-overlay">
          <div className="modal-content">
            <p>¿Estás seguro de que deseas cerrar sesión?</p>
            <button className="confirm-button" onClick={handleConfirmLogout}>
              Sí
            </button>
            <button className="cancel-button" onClick={handleCancelLogout}>
              No
            </button>
          </div>
        </div>
      )}
    </>
  );
};

export default TopBar;

