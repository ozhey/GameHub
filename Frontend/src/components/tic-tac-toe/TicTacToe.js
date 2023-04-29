import React, { useState } from "react";
import Board from "./Board";
import './TicTacToe.css';
import WaitingRoom from "./WaitingRoom";

const TicTacToe = ({ wsRef }) => {
    const [roomId, setRoomId] = useState("");

    if (roomId === "") {
        return <WaitingRoom wsRef={wsRef} setRoomId={setRoomId} />
    }

    return <Board wsRef={wsRef} roomId={roomId} setRoomId={setRoomId} />

}

export default TicTacToe;