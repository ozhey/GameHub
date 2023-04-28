import React, { useEffect, useRef, useState } from "react";
import { getClientAndConnect } from "../../websocket/Websocket";
import WebsocketStatus from "../common/WebsocketStatus";
import Board from "./Board";
import './TicTacToe.css';

const TicTacToe = () => {
    const wsRef = useRef(null);
    const waitingRoomSub = useRef(null);
    const [isConnected, setIsConnected] = useState(false);
    const [error, setError] = useState(null);
    const [roomId, setRoomId] = useState("");

    const onRoomFound = (roomId) => {
        waitingRoomSub.current.unsubscribe();
        setRoomId(roomId.body);
    }

    useEffect(() => {
        wsRef.current = (getClientAndConnect(setIsConnected, setError));
        return () => wsRef.current.disconnect();
    }, []);

    useEffect(() => {
        if (isConnected) {
            waitingRoomSub.current = wsRef.current.subscribe("/topic/ttt/waiting_room", onRoomFound)
        }
    }, [isConnected]);

    let body = <div>Error...</div>

    if (isConnected && roomId === "") {
        body = <div>Searching for a room...</div>
    } else if (isConnected && roomId !== "") {
        body = <Board wsRef={wsRef} roomId={roomId}/>
    } else {
        body = <div>Connecting...</div>
    }

    return <>
        {body}
        <WebsocketStatus isConnected={isConnected} />
    </>

}

export default TicTacToe;