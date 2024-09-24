import React, { useState } from 'react';
import './Navbar.css';

const Navbar = () => {
  const [showLogoutPopup, setShowLogoutPopup] = useState(false);

  const handleLogoutClick = () => {
    setShowLogoutPopup(true);  // Mostrar el pop-up
  };

  const confirmLogout = () => {
    console.log('Cerrando sesión...');
    setShowLogoutPopup(false);  // Cerrar el pop-up
    // Aquí podrías manejar la lógica real de cerrar sesión
  };

  const cancelLogout = () => {
    setShowLogoutPopup(false);  // Cerrar el pop-up si cancelas
  };

  const goToDocumentation = () => {
    console.log('Ir a Documentación');
    // Lógica para redirigir a la documentación
  };

  const goToUserGuide = () => {
    console.log('Ir a Guía de uso');
    // Lógica para redirigir a la guía de uso
  };

  const goToProfile = () => {
    console.log('Ir al Perfil');
    // Lógica para redirigir al perfil del usuario
  };

  return (
    <div className="navbar">
      <div className="nav-left">
        <div className="nav-item" onClick={goToDocumentation}>Documentación</div>
        <div className="nav-item" onClick={goToUserGuide}>Guía de uso</div>
        <div className="nav-item" onClick={goToProfile}>Perfil</div>
      </div>
      <button className="logout-button" onClick={handleLogoutClick}>Cerrar sesión</button>
      
      {showLogoutPopup && (
        <div className="logout-popup">
          <p>¿Estás seguro que deseas cerrar sesión?</p>
          <button onClick={confirmLogout}>Sí</button>
          <button onClick={cancelLogout}>No</button>
        </div>
      )}
    </div>
  );
};

export default Navbar;

