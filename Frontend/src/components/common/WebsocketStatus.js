import React from "react"

const WebsocketStatus = ({ isConnected }) => {
    const circleSize = 20;
    const circleStyle = {
        position: "fixed",
        bottom: 20,
        right: 20,
        width: circleSize,
        height: circleSize,
        borderRadius: "50%",
        backgroundColor: isConnected ? "green" : "red",
        boxShadow: "0 2px 6px rgba(0, 0, 0, 0.3)"
    };

    return <div style={circleStyle}></div>;
}

export default WebsocketStatus; 