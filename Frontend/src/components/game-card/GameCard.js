import React from "react";
import './GameCard.css';

const GameCard = ({ game, setGame }) => {

    return (
        <article className="gamecard" onClick={() => setGame(game)}>
            <img className="gamecard__img" src={require(`../../images/${game}.png`)}></img>
            <h2 className="gamecard__title">Play {game}</h2>
        </article>
    )
}

export default GameCard;