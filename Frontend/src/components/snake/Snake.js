import React, { useState } from "react";
import Rooms from "./Rooms";
import Board from "./Board";
import './Snake.css';

const Snake = ({ wsRef, username }) => {
    const [roomId, setRoomId] = useState("");

    if (roomId === "") {
        return <Rooms setRoomNum={setRoomId} />
    }

    return <Board wsRef={wsRef} roomId={roomId} setRoomId={setRoomId} username={username}/>

}

export default Snake;