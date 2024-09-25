import React, { useState } from 'react';
import { useDrop } from 'react-dnd';
import Xarrow from 'react-xarrows'; // Usamos Xarrow para las líneas de conexión
import './Canvas.css';

// Icono de configuración (puedes usar una imagen o un ícono SVG)
import { FaCog } from 'react-icons/fa';

const Canvas = () => {
  const [items, setItems] = useState([]);
  const [connections, setConnections] = useState([]);
  const [selectedItem, setSelectedItem] = useState(null);
  const [configItem, setConfigItem] = useState(null); // Estado para el ítem a configurar

  // Estado para los botones "prender/apagar"
  const [configServerActive, setConfigServerActive] = useState(false);
  const [apiGatewayActive, setApiGatewayActive] = useState(false);
  const [jwtSecurityActive, setJwtSecurityActive] = useState(false);

  const [{ isOver }, drop] = useDrop(() => ({
    accept: 'MICROSERVICE',
    drop: (item, monitor) => {
      const offset = monitor.getClientOffset();
      addItem(item.id, offset.x, offset.y);
    },
    collect: (monitor) => ({
      isOver: monitor.isOver(),
    }),
  }));

  const addItem = (id, x, y) => {
    setItems((prevItems) => [
      ...prevItems,
      { id: `${id}-${Date.now()}`, x, y, config: {} }, // Añadimos un ID único y estado de configuración
    ]);
  };

  const handleItemClick = (itemId) => {
    if (selectedItem) {
      if (selectedItem !== itemId) {
        connectItems(selectedItem, itemId);
      }
      setSelectedItem(null);
    } else {
      setSelectedItem(itemId);
    }
  };

  const openConfigModal = (itemId) => {
    setConfigItem(itemId); // Abre el modal de configuración para el ítem seleccionado
  };

  const closeConfigModal = () => {
    setConfigItem(null); // Cierra el modal de configuración
  };

const saveConfig = (itemId, config) => {
  setItems((prevItems) =>
    prevItems.map((item) =>
      item.id === itemId ? { ...item, config } : item
    )
  );
  // El modal se mantiene abierto después de guardar
};

const handleSelectChange = (itemId, option, value) => {
  // Al cambiar la opción, actualiza la configuración pero no cierra el modal
  setItems((prevItems) =>
    prevItems.map((item) =>
      item.id === itemId
        ? { ...item, config: { ...item.config, [option]: value } }
        : item
    )
  );
};

  const connectItems = (startId, endId) => {
    setConnections((prevConnections) => [
      ...prevConnections,
      { startId, endId },
    ]);
  };

  const removeConnection = (startId, endId) => {
    setConnections((prevConnections) =>
      prevConnections.filter(
        (conn) =>
          !(conn.startId === startId && conn.endId === endId) &&
          !(conn.startId === endId && conn.endId === startId) // Eliminar en ambos sentidos
      )
    );
  };

  const handleConnectionClick = (startId, endId) => {
    // Eliminar la conexión al hacer clic
    removeConnection(startId, endId);
  };

  const handleGenerateCode = () => {
    console.log('Generando código...');
  };

  return (
    <div className="canvas-container">
      <div ref={drop} className={`canvas ${isOver ? 'over' : ''}`}>
        {/* Botones en la parte superior izquierda */}
        <div className="toggle-buttons">
          <button
            className={configServerActive ? 'active' : ''}
            onClick={() => setConfigServerActive(!configServerActive)}
          >
            Config Server
          </button>
          <button
            className={apiGatewayActive ? 'active' : ''}
            onClick={() => setApiGatewayActive(!apiGatewayActive)}
          >
            API Gateway
          </button>
          <button
            className={jwtSecurityActive ? 'active' : ''}
            onClick={() => setJwtSecurityActive(!jwtSecurityActive)}
          >
            JWT Security
          </button>
        </div>

        {/* Elementos en el canvas */}
        {items.map((item, index) => (
          <div
            key={index}
            id={`item-${item.id}`}
            className="draggable-item"
            style={{ left: item.x, top: item.y }}
            onClick={() => handleItemClick(item.id)}
          >
            <div className="microservice">
              Microservicio {item.id}
              {/* Ícono de configuración visible al pasar el mouse */}
              <FaCog
                className="config-icon"
                onClick={() => openConfigModal(item.id)}
              />
            </div>
          </div>
        ))}

        {/* Conexiones en el canvas (líneas sin dirección) */}
        {connections.map((conn, index) => (
          <Xarrow
            key={index}
            start={`item-${conn.startId}`}
            end={`item-${conn.endId}`}
            showHead={false} // Desactivar la punta de la flecha
            strokeWidth={2} // Grosor de la línea
            onClick={() => handleConnectionClick(conn.startId, conn.endId)} // Eliminar conexión al hacer clic
          />
        ))}
      </div>

{/* Modal de configuración */}
{configItem !== null && (
  <div className="modal">
    <div className="modal-content">
      <h3>Configuración del Microservicio {configItem}</h3>
      <label>
        Opción 1:
        <select
          value={
            items.find((item) => item.id === configItem)?.config.option1 ||
            ''
          }
          onChange={(e) =>
            handleSelectChange(configItem, 'option1', e.target.value)
          }
        >
          <option value="opcion1">Opción 1</option>
          <option value="opcion2">Opción 2</option>
          <option value="opcion3">Opción 3</option>
        </select>
      </label>
      <br />
      <label>
        Opción 2:
        <select
          value={
            items.find((item) => item.id === configItem)?.config.option2 ||
            ''
          }
          onChange={(e) =>
            handleSelectChange(configItem, 'option2', e.target.value)
          }
        >
          <option value="opcion1">Opción 1</option>
          <option value="opcion2">Opción 2</option>
          <option value="opcion3">Opción 3</option>
        </select>
      </label>
      <br />
      {/* Botones de Cancelar y Aceptar */}
      <div className="modal-buttons">
        <button onClick={closeConfigModal}>Aceptar</button>
      </div>
    </div>
  </div>
)}

      {/* Botón para generar código */}
      <button className="generate-code-btn" onClick={handleGenerateCode}>
        Generar Código
      </button>
    </div>
  );
};

export default Canvas;

