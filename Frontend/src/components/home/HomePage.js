import React, { useEffect, useState, useRef } from "react";
import './HomePage.css';
import { SNAKE, TIC_TAC_TOE } from "../../consts";
import { getClientAndConnect } from '../../api/websocket/Websocket'
import GameCard from "../../components/game-card/GameCard"
import Spinner from "../../components/spinner/Spinner";
import Snake from '../../components/snake/Snake'
import TicTacToe from "../../components/tic-tac-toe/TicTacToe";
import WebsocketStatus from "../../components/websocket/WebsocketStatus";
import Statistics from "../statistics/Statistics";

const HomePage = ({ username }) => {
    const wsRef = useRef(null);
    const [isConnected, setIsConnected] = useState(false);
    const [wsError, setWsError] = useState(null);
    const [game, setGame] = useState(null);
    const [showGameStats, setShowGameStats] = useState(null);
    
    useEffect(() => {
        wsRef.current = (getClientAndConnect(setIsConnected, setWsError));
        return () => wsRef.current.disconnect();
    }, []);
    
    if (wsError) {
        return <div>Error: {wsError}</div>
    }
    
    const goBackHome = () => {
        setGame(null);
        setShowGameStats(null)
    }

    let gameComponent = <section className="gamecards" >
        <GameCard game={SNAKE} setGame={setGame} setShowStats={setShowGameStats} />
        <GameCard game={TIC_TAC_TOE} setGame={setGame} setShowStats={setShowGameStats} />
    </section>;
    if (showGameStats !== null) {
        gameComponent = <Statistics showGameStats={showGameStats} username={username} />
    } else if (game !== null && !isConnected) {
        gameComponent = <Spinner>Connecting...</Spinner>
    } else if (game === SNAKE) {
        gameComponent = <Snake wsRef={wsRef} username={username} />
    } else if (game === TIC_TAC_TOE) {
        gameComponent = <TicTacToe wsRef={wsRef} username={username} />
    }


    return (
        <>
            <h1 className="app__title" onClick={() => goBackHome()}>GameHub</h1>
            {gameComponent}
            <WebsocketStatus isConnected={isConnected} wsRef={wsRef} setIsConnected={setIsConnected} setWsError={setWsError}/>
        </>
    );
}

export default HomePage;
