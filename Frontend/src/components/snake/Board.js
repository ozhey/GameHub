import React, { useEffect, useRef, useState, useCallback } from "react";
import Scores from "./Scores";
import { useSwipeable } from "react-swipeable";
import { useWindowDimensions } from "../../hooks/useWindowDimensions";
const MAX_WINDOW_WIDTH = 600; // true maximum width is around 80% of the const value

const Board = ({ wsRef, roomId, setRoomId, username }) => {
    const canvasRef = useRef(null);
    const subRef = useRef(null);
    const [gameState, setGameState] = useState(null);
    const [canvas, setCanvas] = useState({ width: 0, height: 0, color: "oldlace", scale: 0 })
    const [canvasDimensions, setCanvasDimensions] = useState({ width: 0, height: 0, color: "oldlace", scale: 0 })
    const { height, width } = useWindowDimensions();
    const [userColor, setUserColor] = useState("");
    let gameResult;

    const swipeHandlers = useSwipeable({
        onSwipedLeft: () => handleKeyDown({ code: "ArrowLeft" }),
        onSwipedRight: () => handleKeyDown({ code: "ArrowRight" }),
        onSwipedUp: () => handleKeyDown({ code: "ArrowUp" }),
        onSwipedDown: () => handleKeyDown({ code: "ArrowDown" })
    });

    const handleKeyDown = (e) => {
        if (e.code === 'ArrowUp' || e.code === 'ArrowDown' || e.code === 'ArrowLeft' || e.code === 'ArrowRight') {
            if (e.preventDefault) e.preventDefault();
            wsRef.current.send(`/app/snake_room/${roomId}`, {}, JSON.stringify({ type: "changeDir", content: e.code }));
        }
    }

    const initBlankCanvas = useCallback(() => {
        const context = canvasRef.current.getContext("2d");
        context.fillStyle = canvas.color;
        context.fillRect(0, 0, canvas.width, canvas.height);
        context.setTransform(canvas.scale, 0, 0, canvas.scale, 0, 0);
        return context;
    }, [canvas],);

    // everytime the screen or game size changes, readjust the canvas's scale
    useEffect(() => {
        setCanvas(() => {
            const requiredWidth = Math.min(width, MAX_WINDOW_WIDTH) * 0.8;
            const scale = requiredWidth / canvasDimensions.width;
            return {
                width: requiredWidth,
                height: requiredWidth,
                color: canvasDimensions.color,
                scale: scale
            }
        })
    }, [width, height, canvasDimensions])

    // when the component loads, initialize a blank canvas
    useEffect(() => {
        initBlankCanvas();
    }, [initBlankCanvas])

    useEffect(() => {
        const onMessage = (payloadString) => {
            let payload = JSON.parse(payloadString.body);

            if (payload.snakes) { // gamestate message
                setGameState(payload);
                // if (payload.time === 0) {
                //     setCanvasDimensions(payload.canvas)
                // }
                return;
            }

            // else, payload is map of usernames and colors
            setUserColor(payload[username]);
        }

        subRef.current = wsRef.current.subscribe(`/topic/snake_room/${roomId}`, onMessage);
        wsRef.current.send(`/app/snake_room/${roomId}`, {}, JSON.stringify({ type: "registerPlayer", content: username }));
        return () => subRef.current.unsubscribe();
    }, [roomId, username, wsRef]);


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
        if (gameState && gameState.canvas && gameState.canvas.width !== canvas.width) {
            setCanvasDimensions(gameState.canvas);
        }
    }, [gameState, canvas, initBlankCanvas])


    const winner = gameState?.winner;
    if (winner === 'Tie') {
        gameResult = <h2 className='text-shadow'>The game ended with a tie!</h2>
    } else if (winner === "End") {
        gameResult = <h2 className='text-shadow'>Game ended</h2>
    } else if (winner) {
        gameResult = <h2 className='text-shadow'>The winner is <span style={{textShadow: getTextShadow(winner) }}>{winner}</span></h2>
    }

    let userColorElement;
    if (userColor) {
        userColorElement = <h2 style={{ margin: '10px 0px -15px' }} className='text-shadow'>Your color is <span style={{textShadow: getTextShadow(userColor) }}>{userColor}</span></h2>
    }

    return (
        <div className='snake-game'>
            <div role="button" tabIndex="0" onKeyDown={handleKeyDown}{...swipeHandlers} className='snake-container'>
                {userColorElement}
                <canvas style={{ display: 'block' }}
                    className='snake-canvas'
                    ref={canvasRef}
                    width={`${canvas.width}px`}
                    height={`${canvas.height}px`}
                />
                <div className='options'>
                    <button
                        disabled={(!gameState || gameState?.winner) ? false : true}
                        title={(!gameState || gameState?.winner) ? null : "The game has to end before a new one can be started"}
                        onClick={() => wsRef.current.send(`/app/snake_room/${roomId}`, {}, JSON.stringify({ type: "startGame" }))}
                        className='snake-button'>
                        New Game
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

function getTextShadow(color) {
    return `${color} 0 0 7px`
}

export default Board;