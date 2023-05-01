import React, { useState } from "react";
import './Statistics.css';
import { API_ADDRESS, API_ADDRESS_BASE_PATH } from "../../consts";
import useFetch from '../../hooks/useFetch';
import { Table, Thead, Tbody, Tr, Th, Td } from 'react-super-responsive-table';
import './Table.css';

const Statistics = ({ setShowStats, username }) => {
    const [isLeaderboard, setIsLeaderboard] = useState(true);
    const [result, isLoading, error] = useFetch(`${API_ADDRESS}${API_ADDRESS_BASE_PATH}/snake/scores/leaderboard`, null, [], isLeaderboard)

    console.log(result);

    if (error) {
        return <div>Error: {error.message}</div>;
    } else if (isLoading) {
        return <div>Loading...</div>;
    }

    let tableBody = <Tbody>
        {result.map((row, index) => (
            <Tr key={index}>
                {Object.values(row).map((col, index) => (
                    isRegex(col) ? null : <Td key={index}>{col}</Td>
                ))}
            </Tr>
        ))}
    </Tbody>

    let tableHeader;
    if (isLeaderboard) {
        tableHeader = <Thead>
            <Tr>
                <Th>Username</Th>
                <Th>Apples Eaten</Th>
                <Th>Games Played</Th>
                <Th>Online Games Played</Th>
                <Th>Online Games Won</Th>
            </Tr>
        </Thead>
    } else {
        tableHeader = <Thead>
            <Tr>
                <Th>Apples Eaten</Th>
                <Th>Online?</Th>
                <Th>Win?</Th>
            </Tr>
        </Thead>
    }

    return <div>
        <Table>
            {tableHeader}
            {tableBody}
        </Table>
    </div>

}

function isRegex(str) {
    const regexExp = /^[0-9a-fA-F]{8}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{4}\b-[0-9a-fA-F]{12}$/gi;
    return regexExp.test(str);
}

export default Statistics;