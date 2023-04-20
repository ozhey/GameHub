import React, { useEffect, useRef, useState } from "react";
import Scores from "./Scores";
import { useWindowDimensions } from "../../hooks/CustomHooks"
import { getClientAndConnect } from "../../websocket/Websocket";
import Rooms from "../common/Rooms";
import WebsocketStatus from "../common/WebsocketStatus";

const Board = ({ wsRef, roomNum, setRoomNum }) => {
    const canvasRef = useRef(null);
    const subRef = useRef(null);
    const [gameState, setGameState] = useState(null);
    const [canvas, setCanvas] = useState({ width: 480, height: 480, color: "oldlace", scale: 20 })
    const [winner, setWinner] = useState();
    const { height, width } = useWindowDimensions();
    let gameResult;

    const onMessage = (payloadString) => {
        let payload = JSON.parse(payloadString);
    }

    const handleKeyDown = (e) => {
        if (e.preventDefault && (e.code === 'ArrowUp' || e.code === 'ArrowDown' || e.code === 'ArrowLeft' || e.code === 'ArrowRight')) {
            e.preventDefault();
        }
        wsRef.current.send(`/app/snake_room/${roomNum}`, {}, e.code )

    }

    useEffect(() => {
        subRef.current = wsRef.current.subscribe(`topic/snake_room/${roomNum}`, onMessage)
        return () => subRef.current.unsubscribe();
    }, []);

    if (winner === 'Tie') {
        gameResult = <h2 className='text-shadow'>The game ended with a tie!</h2>
    } else if (winner) {
        gameResult = <h2 className='text-shadow'>The last survivor is <span style={{ color: winner }}>{winner}</span></h2>
    }

    return (
        <div className='snake-game'>
            <div role="button" tabIndex="0" onKeyDown={handleKeyDown} className='snake-container'>
                <h2 style={{ margin: '10px 0px -15px' }} className='text-shadow'>Your color is <span style={{ color: 'red' }}>red</span></h2>
                <canvas style={{ display: 'block' }}
                    className='snake-canvas'
                    ref={canvasRef}
                    width={`${canvas.width}px`}
                    height={`${canvas.height}px`}
                />
                <div className='options'>
                    <button
                        onClick={() => wsRef.current.send(`/app/snake_room/${roomNum}`, {}, "start")}
                        className='button-red'>Start game
                    </button>
                    <button
                        onClick={() => setRoomNum(0)} // go back to rooms page
                        className='button-red'>Back to menu
                    </button>
                </div>
                {gameResult}
                <Scores gameState={gameState} />
            </div>
        </div>
    )

}

export default Board;