import React from "react";
import './Snake.css'

const Scores = ({ gameState }) => {
    if (!gameState) {
        return null;
    }
    const scores = Object.values(gameState.snakes).map((snake) =>
        <div key={snake.color} style={{ backgroundColor: snake.color }} className='score-item'>{snake.score}</div>)

    return (
        <>
            <h2 className='text-shadow'>{scores.length > 1 ? 'Scores:' : 'Score:'}</h2>
            <div className='flex-scores'>
                {scores}
            </div>
        </>
    )
}

export default Scores;