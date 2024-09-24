import React from 'react';
import { useDrag } from 'react-dnd';
import styled from 'styled-components';

const DraggableItemContainer = styled.div`
  padding: 10px;
  margin: 10px 0;
  background-color: #e0e0e0;
  cursor: pointer;
  text-align: center;
`;

function DraggableItem({ name }) {
  const [{ isDragging }, drag] = useDrag({
    type: 'component',
    item: { name },
    collect: (monitor) => ({
      isDragging: !!monitor.isDragging(),
    }),
  });

  return (
    <DraggableItemContainer ref={drag} style={{ opacity: isDragging ? 0.5 : 1 }}>
      {name}
    </DraggableItemContainer>
  );
}

export default DraggableItem;

