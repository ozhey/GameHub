import React, { useEffect, useRef, useState } from "react";
import { getClientAndConnect } from "../../websocket/Websocket";
import Rooms from "./Rooms";
import WebsocketStatus from "../common/WebsocketStatus";
import Board from "./Board";
import './Snake.css';

const Snake = () => {
    const wsRef = useRef(null);
    const [isConnected, setIsConnected] = useState(false);
    const [error, setError] = useState(null);
    const [roomId, setRoomId] = useState("");

    useEffect(() => {
        wsRef.current = (getClientAndConnect(setIsConnected, setError));
        return () => wsRef.current.disconnect();
    }, []);

    let body = <div>Error...</div>

    if (isConnected && roomId === "") {
        body = <Rooms setRoomNum={setRoomId} />
    } else if (isConnected && roomId !== "") {
        body = <Board wsRef={wsRef} roomId={roomId} setRoomId={setRoomId} />
    } else {
        body = <div>Connecting...</div>
    }

    return <>
        {body}
        <WebsocketStatus isConnected={isConnected} />
    </>

}

export default Snake;