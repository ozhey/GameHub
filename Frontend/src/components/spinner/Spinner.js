import React from 'react';
import './Spinner.css';

const Spinner = ({ children }) => {

  return (
    <div className="spinner-container">
      <div className="spinner"></div>
      <div className="spinner-text">
        {children}
      </div>
    </div>
  );
}

export default Spinner;
