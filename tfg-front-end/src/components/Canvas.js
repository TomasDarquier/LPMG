import React, { useState } from 'react';
import { useDrop } from 'react-dnd';
import Xarrow from 'react-xarrows';
import './Canvas.css';

const Canvas = () => {
  const [items, setItems] = useState([]);
  const [connections, setConnections] = useState([]);
  const [selectedItem, setSelectedItem] = useState(null);

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
      { id, x, y },
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

  const connectItems = (startId, endId) => {
    setConnections((prevConnections) => [
      ...prevConnections,
      { startId, endId },
    ]);
  };

  return (
    <div ref={drop} className={`canvas ${isOver ? 'over' : ''}`}>
      {items.map((item, index) => (
        <div
          key={index}
          id={`item-${item.id}`}
          className="draggable-item"
          style={{ left: item.x, top: item.y }}
          onClick={() => handleItemClick(item.id)}
        >
          Microservicio {item.id}
        </div>
      ))}
      {connections.map((conn, index) => (
        <Xarrow
          key={index}
          start={`item-${conn.startId}`}
          end={`item-${conn.endId}`}
        />
      ))}
    </div>
  );
};

export default Canvas;

