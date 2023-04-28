import { useState, useEffect, useRef } from 'react';
import './TicTacToe.css'
import Squares from './Squares';


const Board = ({ wsRef, roomId }) => {
    const subRef = useRef(null);
    const [squares, setSquares] = useState([['-', '-', '-'], ['-', '-', '-'], ['-', '-', '-']]);
    const [nextSymbol, setNextSymbol] = useState('X');
    const [winner, setWinner] = useState('-');

    const onMessage = (payloadString) => {
        let payload = JSON.parse(payloadString.body);
        let squares = payload.board.map(str => str.split(''));
        setSquares(squares);
        setNextSymbol(payload.nextSymbol);
        setWinner(payload.winner);
    }

    useEffect(() => {
        subRef.current = wsRef.current.subscribe(`/topic/ttt_room/${roomId}`, onMessage)
        return () => subRef.current.unsubscribe();
    }, []);

    const onPlay = (i, j) => {
        if (squares[i][j] !== '-' || winner !== '-') {
            return;
        }
        wsRef.current.send(`/app/ttt_room/${roomId}`, {}, JSON.stringify({
            command: "move",
            row: i,
            col: j
        }))
    }

    const restart = () => {
        wsRef.current.send(`/app/ttt_room/${roomId}`, {}, JSON.stringify({
            command: "start"
        }))
    }

    let status;
    if (winner !== '-') {
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

export default Board;