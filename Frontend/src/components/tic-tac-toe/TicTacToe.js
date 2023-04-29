import React, { useEffect, useRef, useState } from "react";
import { getClientAndConnect } from "../../websocket/Websocket";
import WebsocketStatus from "../websocket/WebsocketStatus";
import Board from "./Board";
import Spinner from "../spinner/Spinner";
import './TicTacToe.css';

const TicTacToe = ({ wsRef }) => {
    const [roomId, setRoomId] = useState("");
    const waitingRoomSub = useRef(null);

    const onRoomFound = (roomId) => {
        waitingRoomSub.current.unsubscribe();
        setRoomId(roomId.body);
    }

    useEffect(() => {
        if (roomId === "") {
            waitingRoomSub.current = wsRef.current.subscribe("/topic/ttt/waiting_room", onRoomFound)
        }
        return () => waitingRoomSub.current.unsubscribe();
    }, [roomId]);

    if (roomId === "") {
        return <Spinner> Searching for a match... </Spinner>
    }

    return <Board wsRef={wsRef} roomId={roomId} setRoomId={setRoomId} />

}

export default TicTacToe;