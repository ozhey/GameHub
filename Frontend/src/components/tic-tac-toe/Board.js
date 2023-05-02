import { useState, useEffect, useRef } from 'react';
import './TicTacToe.css'
import Squares from './Squares';


const Board = ({ wsRef, roomId, setRoomId, username }) => {
    const subRef = useRef(null);
    const [squares, setSquares] = useState([['-', '-', '-'], ['-', '-', '-'], ['-', '-', '-']]);
    const [nextSymbol, setNextSymbol] = useState('X');
    const [winner, setWinner] = useState('-');
    const [players, setPlayers] = useState({});

    const onMessage = (payloadString) => {
        if (payloadString.body === "end") {
            console.log("The opponet has left the match. Returning to the waiting room.")
            setRoomId("");
            return;
        }

        let payload = JSON.parse(payloadString.body);
        let squares = payload.board.map(str => str.split(''));
        setPlayers(payload.players);
        setSquares(squares);
        setNextSymbol(payload.nextSymbol);
        setWinner(payload.winner);
    }

    useEffect(() => {
        subRef.current = wsRef.current.subscribe(`/topic/ttt_room/${roomId}`, onMessage)
        wsRef.current.send(`/app/ttt_room/${roomId}`, {}, JSON.stringify({ command: "registerPlayer", content: username }));
        return () => subRef.current.unsubscribe();
    }, []);

    const onPlay = (i, j) => {
        if (squares[i][j] !== '-' || winner !== '-') {
            return;
        }
        wsRef.current.send(`/app/ttt_room/${roomId}`, {}, JSON.stringify({
            command: "makeMove",
            row: i,
            col: j
        }))
    }

    const restart = () => {
        wsRef.current.send(`/app/ttt_room/${roomId}`, {}, JSON.stringify({
            command: "startGame"
        }))
    }

    let status;
    let winnerName = players[winner];
    let nextPlayerName = players[nextSymbol];
    if (nextPlayerName === username) {
        status = <div className="status">It's your turn</div>
    }
    if (nextPlayerName !== username) {
        status = <div className="status">{nextPlayerName}'s turn</div>
    }
    if (winnerName !== undefined && winnerName === username) {
        status = <div className="status">You won!</div>
    }
    if (winnerName !== undefined && winnerName !== username) {
        status = <div className="status">{winnerName} won!</div>
    }

    if (winner !== '-') {
        console.log("disabled");
    }

    return (
        <div className="game">
            <div className="game-board">
                {status}
                <Squares squares={squares} onPlay={onPlay} />
                <div className="game-info">
                    <button
                        disabled={winner === '-' ? true : false}
                        title={winner === '-' ? "The game has to end before a new one can be started" : null}
                        onClick={() => restart()}>
                        New Game
                    </button>
                </div>
            </div>
        </div>
    );
}

export default Board;