import React, { useEffect, useRef } from "react";
import Spinner from "../spinner/Spinner";
import './TicTacToe.css';

const WaitingRoom = ({ wsRef, setRoomId }) => {
    const waitingRoomSub = useRef(null);


    useEffect(() => {
        const onRoomFound = (roomId) => {
            setRoomId(roomId.body);
        }

        waitingRoomSub.current = wsRef.current.subscribe("/topic/ttt/waiting_room", onRoomFound)
        return () => waitingRoomSub.current.unsubscribe();
    }, [setRoomId, wsRef]);

    return <Spinner> Searching for a match... </Spinner>



}

export default WaitingRoom;