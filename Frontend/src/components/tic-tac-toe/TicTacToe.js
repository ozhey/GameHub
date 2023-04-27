import { useState } from 'react';
import './TicTacToe.css'

function Square({ value, onSquareClick }) {
    return (
        <button className={`square ${value === 'X' ? 'x' : value === 'O' ? 'o' : ''}`} onClick={onSquareClick}>
            {value}
        </button>
    );
}

function Board({ squares, onPlay }) {
    return (
        <>
            <div className="board-row">
                <Square value={squares[0]} onSquareClick={() => onPlay(0)} />
                <Square value={squares[1]} onSquareClick={() => onPlay(1)} />
                <Square value={squares[2]} onSquareClick={() => onPlay(2)} />
            </div>
            <div className="board-row">
                <Square value={squares[3]} onSquareClick={() => onPlay(3)} />
                <Square value={squares[4]} onSquareClick={() => onPlay(4)} />
                <Square value={squares[5]} onSquareClick={() => onPlay(5)} />
            </div>
            <div className="board-row">
                <Square value={squares[6]} onSquareClick={() => onPlay(6)} />
                <Square value={squares[7]} onSquareClick={() => onPlay(7)} />
                <Square value={squares[8]} onSquareClick={() => onPlay(8)} />
            </div>
        </>
    );
}

export default function TicTacToe() {
    const [squares, setSquares] = useState({});
    const [nextSymbol, setNextSymbol] = useState('X');
    const winner = calculateWinner(squares);

    const onPlay = (i) => {
        if (squares[i] || winner) {
            return;
        }
        setSquares((prevSquares => {
            prevSquares[i] = nextSymbol;
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
                <Board squares={squares} onPlay={onPlay} />
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