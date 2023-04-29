import React from 'react';
import './Spinner.css'; // import CSS file for styling the spinner

function Spinner({ children }) {

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
