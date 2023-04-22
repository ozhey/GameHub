import React, { useEffect, useRef, useState, useCallback } from "react";
import Scores from "./Scores";
import { useWindowDimensions } from "../../hooks/CustomHooks";
import { getClientAndConnect } from "../../websocket/Websocket";
import Rooms from "../common/Rooms";
import WebsocketStatus from "../common/WebsocketStatus";
const MAX_WINDOW_WIDTH = 600; //The true maximum width is around 80% of the const value

const Board = ({ wsRef, roomId, setRoomId }) => {
    const canvasRef = useRef(null);
    const subRef = useRef(null);
    const [gameState, setGameState] = useState(null);
    const [canvas, setCanvas] = useState({ width: 480, height: 480, color: "oldlace", scale: 20 })
    const [winner, setWinner] = useState();
    const { height, width } = useWindowDimensions();
    let gameResult;

    const onMessage = (payloadString) => {
        let payload = JSON.parse(payloadString.body);
        setGameState(payload);
        setCanvas(payload.canvas);
    }

    const handleKeyDown = (e) => {
        if (e.preventDefault && (e.code === 'ArrowUp' || e.code === 'ArrowDown' || e.code === 'ArrowLeft' || e.code === 'ArrowRight')) {
            e.preventDefault();
        }
        wsRef.current.send(`/app/snake_room/${roomId}`, {}, e.code)

    }

    const initBlankCanvas = useCallback(
        () => {
            const context = canvasRef.current.getContext("2d");
            context.fillStyle = canvas.color;
            context.fillRect(0, 0, canvas.width, canvas.height);
            return context;
        },
        [canvas],
    );

    useEffect(() => {
        subRef.current = wsRef.current.subscribe(`/topic/snake_room/${roomId}`, onMessage)
        wsRef.current.send(`/app/snake_room/${roomId}`, {}, "Enter room")
        return () => subRef.current.unsubscribe();
    }, []);

    useEffect(() => {
        setCanvas(prevCanvas => {
            const maxWidth = Math.min(width, MAX_WINDOW_WIDTH);
            const multiplier = prevCanvas.width / maxWidth;
            return {
                width: maxWidth * 0.8,
                height: prevCanvas.height / multiplier * 0.8,
                color: prevCanvas.color,
                scale: prevCanvas.scale / multiplier * 0.8
            }
        })
    }, [width, height])

    useEffect(() => {
        const context = initBlankCanvas();
        context.setTransform(canvas.scale, 0, 0, canvas.scale, 0, 0);
    }, [canvas, initBlankCanvas])

    useEffect(() => {
        if (gameState) {
            const { snakes, apple } = gameState;
            const context = initBlankCanvas();
            Object.values(snakes).forEach(snake => {
                context.fillStyle = snake.color;
                snake.body.forEach((segment) => context.fillRect(segment.x, segment.y, 1, 1))
            })
            context.fillStyle = 'red';
            context.beginPath();
            context.arc(apple.x + 0.5, apple.y + 0.575, 0.425, 0, 2 * Math.PI);
            context.closePath();
            context.fill();
            context.fillStyle = 'green';
            context.fillRect(apple.x + 0.5, apple.y, 0.3, 0.15);
        }
    }, [gameState, canvas, initBlankCanvas])


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
                        onClick={() => wsRef.current.send(`/app/snake_room/${roomId}`, {}, "start")}
                        className='button-red'>Start game
                    </button>
                    <button
                        onClick={() => setRoomId(0)} // go back to rooms page
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