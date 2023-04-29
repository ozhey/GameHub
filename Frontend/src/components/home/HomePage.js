import React, { useEffect, useState, useRef } from "react";
import './HomePage.css';
import { SNAKE, TIC_TAC_TOE } from "../../consts";
import { getClientAndConnect } from '../../api/websocket/Websocket'
import GameCard from "../../components/game-card/GameCard"
import Spinner from "../../components/spinner/Spinner";
import Snake from '../../components/snake/Snake'
import TicTacToe from "../../components/tic-tac-toe/TicTacToe";
import WebsocketStatus from "../../components/websocket/WebsocketStatus";

const HomePage = ({ username }) => {
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


    let gameComponent = <section className="gamecards" >
        <GameCard game={SNAKE} setGame={setGame} />
        <GameCard game={TIC_TAC_TOE} setGame={setGame} />
    </section>;
    if (game !== null && !isConnected) {
        gameComponent = <Spinner>Connecting...</Spinner>
    } else if (game === SNAKE) {
        gameComponent = <Snake wsRef={wsRef} username={username}/>
    } else if (game === TIC_TAC_TOE) {
        gameComponent = <TicTacToe wsRef={wsRef} username={username}/>
    }


    return (
        <>
            <h1 className="app__title" onClick={() => setGame("")}>GameHub</h1>
            {gameComponent}
            <WebsocketStatus isConnected={isConnected} />
        </>
    );
}

export default HomePage;
