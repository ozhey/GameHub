import React, { useState } from "react";
import './App.css';
import LoginForm from "./components/login/Login";
import HomePage from "./components/home/HomePage";
import Bg from "./images/bg.jpg"

function App() {
  const [username, setUsername] = useState("");

  let component = <LoginForm setUsername={setUsername} />

  if (username) {
    component = <HomePage username={username} />
  }

  return (
    <div className="App" style={{
      backgroundImage: "url(" + Bg + ")",
      opacity: "1",
      backgroundPosition: 'center',
      backgroundSize: 'cover',
      backgroundRepeat: 'no-repeat'
    }}>
      {component}
    </div >
  );
}

export default App;
