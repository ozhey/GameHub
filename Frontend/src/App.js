import React, { useEffect, useState, useRef } from "react";
import './App.css';
import LoginForm from "./components/login/Login";
import HomePage from "./components/home/HomePage";

function App() {
  const [username, setUsername] = useState("");

  let component = <LoginForm setUsername={setUsername} />

  if (username) {
    component = <HomePage username={username} />
  }

  return (
    <div className="App">
      {component}
    </div>
  );
}

export default App;
