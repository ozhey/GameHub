import React, { useEffect, useRef, useState } from "react";
import { getClientAndConnect } from "../../websocket/Websocket";
import Rooms from "../common/Rooms";
import WebsocketStatus from "../common/WebsocketStatus";
import Board from "./Board";
import './Snake.css';

const Snake = () => {
    const wsRef = useRef(null);
    const [isConnected, setIsConnected] = useState(false);
    const [error, setError] = useState(null);
    const [roomNum, setRoomNum] = useState(0);

    useEffect(() => {
        wsRef.current = (getClientAndConnect(setIsConnected, setError));
        return () => wsRef.current.disconnect();
    }, []);

    let body = <div>Error...</div>

    if (isConnected && roomNum === 0) {
        body = <Rooms setRoomNum={setRoomNum} />
    } else if (isConnected && roomNum > 0) {
        body = <Board wsRef={wsRef} roomNum={roomNum} setRoomNum={setRoomNum} />
    } else {
        body = <div>Connecting...</div>
    }

    return <>
        {body}
        <button onClick={() => wsRef.current.send("/app/hello", {}, "hello!")}>
            Send message
        </button>
        <WebsocketStatus isConnected={isConnected} />
    </>



}

export default Snake;