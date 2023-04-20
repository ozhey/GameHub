import React, { useEffect, useState } from "react";
import './App.css';
import {SNAKE, TIC_TAC_TOE} from "./consts";
import Snake from './components/snake/Snake'

function App() {
  const [game, setGame] = useState(null);

  let gameComponent = <></>;
  if (game === SNAKE) {
    gameComponent = <Snake />
  } else if (game === TIC_TAC_TOE) {
    gameComponent = <></>
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
    </div>
  );
}

export default App;
