import React from 'react';
import './Spinner.css'; // import CSS file for styling the spinner

function Spinner(props) {
  return (
    <div className="spinner-container">
      <div className="spinner"></div>
      <div className="spinner-text">Searching for a match...</div>
    </div>
  );
}

export default Spinner;
