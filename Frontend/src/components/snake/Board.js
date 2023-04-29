import React, { useEffect, useRef, useState, useCallback } from "react";
import Scores from "./Scores";
import { useWindowDimensions } from "../../hooks/useWindowDimensions";
const MAX_WINDOW_WIDTH = 600; // true maximum width is around 80% of the const value

const Board = ({ wsRef, roomId, setRoomId }) => {
    const canvasRef = useRef(null);
    const subRef = useRef(null);
    const [gameState, setGameState] = useState(null);
    const [canvas, setCanvas] = useState({ width: 0, height: 0, color: "oldlace", scale: 0 })
    const [canvasDimensions, setCanvasDimensions] = useState({ width: 0, height: 0, color: "oldlace", scale: 0 })
    const { height, width } = useWindowDimensions();
    const [userColor, setUserColor] = useState("");
    const [username, setUsername] = useState(Math.random().toString(36).substring(2, 10)); // random name TODO: replace with real username
    let gameResult;

    const getAdjustedCanvas = (baseCanvas) => {
        const requiredWidth = Math.min(width, MAX_WINDOW_WIDTH) * 0.8;
        const scale = requiredWidth / baseCanvas.width;
        return {
            width: requiredWidth,
            height: requiredWidth,
            color: baseCanvas.color,
            scale: scale
        }
    }

    const onMessage = (payloadString) => {
        let payload = JSON.parse(payloadString.body);

        if (payload.snakes) { // gamestate message
            setGameState(payload);
            if (payload.time === 0 || canvasDimensions.width === 0) {
                setCanvasDimensions(payload.canvas)
            }
            return;
        }

        // else, payload is map of usernames and colors
        setUserColor(payload[username]);
    }

    useEffect(() => {
        setCanvas(getAdjustedCanvas(canvasDimensions))
    }, [canvasDimensions, width, height])

    const handleKeyDown = (e) => {
        if (e.preventDefault && (e.code === 'ArrowUp' || e.code === 'ArrowDown' || e.code === 'ArrowLeft' || e.code === 'ArrowRight')) {
            e.preventDefault();
            wsRef.current.send(`/app/snake_room/${roomId}`, {}, JSON.stringify({ type: "changeDir", content: e.code }));
        }
    }

    const initBlankCanvas = useCallback(() => {
        const context = canvasRef.current.getContext("2d");
        context.fillStyle = canvas.color;
        context.fillRect(0, 0, canvas.width, canvas.height);
        return context;
    }, [canvas],);

    useEffect(() => {
        console.log("snake mount");
        subRef.current = wsRef.current.subscribe(`/topic/snake_room/${roomId}`, onMessage);
        wsRef.current.send(`/app/snake_room/${roomId}`, {}, JSON.stringify({ type: "registerPlayer", content: username }));
        return () => subRef.current.unsubscribe();
    }, []);

    useEffect(() => {
        const context = initBlankCanvas();
        context.setTransform(canvas.scale, 0, 0, canvas.scale, 0, 0);
    }, [canvas, initBlankCanvas])

    useEffect(() => {
        if (gameState) {
            const { snakes, apple: { location: apple } } = gameState;
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


    const winner = gameState?.winner;
    if (winner === 'Tie') {
        gameResult = <h2 className='text-shadow'>The game ended with a tie!</h2>
    } else if (winner === "End") {
        gameResult = <h2 className='text-shadow'>Game ended</h2>
    } else if (winner) {
        gameResult = <h2 className='text-shadow'>The winner is <span style={{ color: winner }}>{winner}</span></h2>
    }

    let userColorElement;
    if (userColor) {
        userColorElement = <h2 style={{ margin: '10px 0px -15px' }} className='text-shadow'>Your color is <span style={{ color: userColor }}>{userColor}</span></h2>
    }

    return (
        <div className='snake-game'>
            <div role="button" tabIndex="0" onKeyDown={handleKeyDown} className='snake-container'>
                {userColorElement}
                <canvas style={{ display: 'block' }}
                    className='snake-canvas'
                    ref={canvasRef}
                    width={`${canvas.width}px`}
                    height={`${canvas.height}px`}
                />
                <div className='options'>
                    <button
                        onClick={() => wsRef.current.send(`/app/snake_room/${roomId}`, {}, JSON.stringify({ type: "startGame" }))}
                        className='snake-button'>Start game
                    </button>
                    <button
                        onClick={() => setRoomId("")} // go back to rooms page
                        className='snake-button'>Back to menu
                    </button>
                </div>
                {gameResult}
                <Scores gameState={gameState} />
            </div>
        </div>
    )

}

export default Board;