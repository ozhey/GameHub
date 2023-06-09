import React from "react";
import './GameCard.css';

const GameCard = ({ game, setGame, setShowStats }) => {

    return (
        <article className="gamecard" onClick={() => setGame(game)}>
            <img className="gamecard__img" alt="game" src={require(`../../images/${game}.png`)}></img>
            <h2 className="gamecard__title">Play {game}</h2>
            <div className="gamecard__stats" onClick={() => setShowStats(game)}>View game statistics</div>
        </article>
    )
}

export default GameCard;