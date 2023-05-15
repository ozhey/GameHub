import React, { useState } from "react";
import './Statistics.css';
import { SNAKE } from "../../consts";
import useFetch from '../../hooks/useFetch';
import Spinner from "../spinner/Spinner";
import {getStatisticsGetUrl} from '../../api/statistics/Statistics';
import './Table.css';

const Statistics = ({ setShowGameStats, showGameStats, username, game }) => {
    const [isLeaderboard, setIsLeaderboard] = useState(true);
    const [result, isLoading, error] = useFetch(getStatisticsGetUrl(showGameStats, isLeaderboard, username), null, [], isLeaderboard)

    if (error) {
        return <div>Error: {error.message}</div>;
    } else if (isLoading) {
        return <Spinner> Loading... </Spinner>;
    }

    let tableBody = result.map((row, index) => (
        <tr key={index}>
            <td>{index + 1}</td>
            {Object.values(row).map((col, index) => {
                if (isRegex(col)) {
                    return null;
                } else if (String(col) === 'null') {
                    col = '-';
                } else if (String(col) === 'true') {
                    col = 'Yes';
                } else if (String(col) === 'false') {
                    col = 'No';
                }
                return <td key={index}>{String(col)}</td>
            }
            )}
        </tr>
    ))

    let tableHeader;
    if (result.length) {
        tableHeader = Object.keys(result[0]).map(key => key === 'id' ? null : <th key={key}>{key}</th>);
    }

    return <div className="stats-container">
        <button className="stats-button" onClick={() => setIsLeaderboard(prev => !prev)}>
            {isLeaderboard ? 'Click to view personal game history' : 'Click to view leaderboards'}
        </button>
        <div className="table-responsive">
            <table>
                <thead>
                    <tr>
                        <th>#</th>
                        {tableHeader}
                    </tr>
                </thead>
                <tbody>
                    {tableBody}
                </tbody>
            </table>
        </div>
    </div>

}

function getPath(game, isLeaderboard, username) {
    const base = game === SNAKE ? '/snake/scores' : '/tic_tac_toe/scores';
    const suffix = isLeaderboard ? '/leaderboard' : `?username=${username}`
    return base + suffix;
}

function isRegex(str) {
    const regexExp = /^[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$/gi;
    return regexExp.test(str);
}

export default Statistics;