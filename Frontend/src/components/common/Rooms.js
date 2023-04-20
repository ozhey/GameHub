import React, { useEffect, useState } from "react";
import './Rooms.css'
import { NUM_OF_ROOMS } from "../../consts";
const Rooms = ({ setRoomNum }) => {

    const roomElements = [];

    for (let i = 1; i <= NUM_OF_ROOMS; i++) {
        roomElements.push(
            <div key={i} onClick={() => setRoomNum(i)}>Room {i}</div>
        );
    }

    return (
        <section className="rooms">
            {roomElements}
        </section>
    )


}

export default Rooms;