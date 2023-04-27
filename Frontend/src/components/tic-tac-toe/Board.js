import { useState, useEffect, useRef } from 'react';
import './TicTacToe.css'
import Squares from './Squares';


const Board = ({ wsRef, roomId }) => {
    const subRef = useRef(null);
    const [squares, setSquares] = useState([['-','-','-'],['-','-','-'],['-','-','-']]);
    const [nextSymbol, setNextSymbol] = useState('X');
    const winner = calculateWinner(squares);

    const onMessage = (messagePayload) => {
        console.log(messagePayload);
    }

    useEffect(() => {
        subRef.current = wsRef.current.subscribe(`/topic/ttt_room/${roomId}`, onMessage)
        return () => subRef.current.unsubscribe();
    }, []);
    
    const onPlay = (i,j) => {
        console.log(i,j);
        if (squares[i][j] !== '-' || winner) {
            return;
        }
        setSquares((prevSquares => {
            prevSquares[i][j] = nextSymbol;
            return prevSquares;
        }))
        setNextSymbol((prevSymbol => prevSymbol === 'X' ? 'O' : 'X'))
    }

    const restart = () => {
        setSquares({});
        setNextSymbol('X');
    }

    let status;
    if (winner) {
        status = 'Winner: ' + winner;
    } else {
        status = 'Next player: ' + nextSymbol;
    }

    return (
        <div className="game">
            <div className="game-board">
                <div className="status">{status}</div>
                <Squares squares={squares} onPlay={onPlay} />
                <div className="game-info">
                    <button onClick={() => restart()}>Restart</button>
                </div>
            </div>
        </div>
    );
}

function calculateWinner(squares) {
    const lines = [
        [0, 1, 2],
        [3, 4, 5],
        [6, 7, 8],
        [0, 3, 6],
        [1, 4, 7],
        [2, 5, 8],
        [0, 4, 8],
        [2, 4, 6],
    ];
    for (let i = 0; i < lines.length; i++) {
        const [a, b, c] = lines[i];
        if (squares[a] && squares[a] === squares[b] && squares[a] === squares[c]) {
            return squares[a];
        }
    }
    return null;
}

export default Board;