import React from "react"
import { getClientAndConnect } from '../../api/websocket/Websocket'

const WebsocketStatus = ({ isConnected, wsRef, setIsConnected, setWsError }) => {
    const circleSize = 20;
    const circleStyle = {
        position: "fixed",
        bottom: 20,
        right: 20,
        width: circleSize,
        height: circleSize,
        borderRadius: "50%",
        backgroundColor: isConnected ? "green" : "red",
        boxShadow: "0 2px 6px rgba(0, 0, 0, 0.3)",
        cursor: 'pointer'
    };

    const reconnect = () => {
        if (!isConnected) {
            wsRef.current = (getClientAndConnect(setIsConnected, setWsError));
        }
    }

    return <div onClick={() => reconnect()} style={circleStyle}></div>;
}

export default WebsocketStatus; 