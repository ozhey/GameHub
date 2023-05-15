import { API_ADDRESS, API_ADDRESS_BASE_PATH, SNAKE } from "../../consts";

export function getStatisticsGetUrl(game, isLeaderboard, username) {
    const base = game === SNAKE ? '/snake/scores' : '/tic_tac_toe/scores';
    const suffix = isLeaderboard ? '/leaderboard' : `?username=${username}`;
    return API_ADDRESS + API_ADDRESS_BASE_PATH + base + suffix;
}