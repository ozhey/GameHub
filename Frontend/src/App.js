import React, { useEffect, useState, useRef } from "react";
import './App.css';
import { SNAKE, TIC_TAC_TOE } from "./consts";
import { getClientAndConnect } from "./websocket/Websocket";
import Spinner from "./components/common/Spinner";
import Snake from './components/snake/Snake'
import TicTacToe from "./components/tic-tac-toe/TicTacToe";
import WebsocketStatus from "./components/common/WebsocketStatus";

function App() {
  const wsRef = useRef(null);
  const [isConnected, setIsConnected] = useState(false);
  const [wsError, setWsError] = useState(null);
  const [game, setGame] = useState(null);

  useEffect(() => {
    wsRef.current = (getClientAndConnect(setIsConnected, setWsError));
    return () => wsRef.current.disconnect();
  }, []);

  if (wsError) {
    return <div>Error: {wsError}</div>
  }


  let gameComponent = null;
  if (!isConnected) {
    gameComponent = <Spinner>Connecting...</Spinner>
  } else if (game === SNAKE) {
    gameComponent = <Snake wsRef={wsRef} />
  } else if (game === TIC_TAC_TOE) {
    gameComponent = <TicTacToe wsRef={wsRef} />
  }


  return (
    <div className="App">
      <button onClick={() => setGame(SNAKE)}>
        Play {SNAKE}
      </button>
      <button onClick={() => setGame(TIC_TAC_TOE)}>
        Play {TIC_TAC_TOE}
      </button>
      {gameComponent}
      <WebsocketStatus isConnected={isConnected} />
    </div>
  );
}

export default App;
