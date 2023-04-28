const Square = ({ value, onClick }) => {
    return (
        <button className={`square ${value === 'X' ? 'x' : value === 'O' ? 'o' : ''}`} onClick={onClick}>
            {value !== '-' ? value : ''}
        </button>
    );
}

const Squares = ({ squares, onPlay }) => {

    return (
        <>
            <div className="board-row">
                <Square value={squares[0][0]} onClick={() => onPlay(0,0)} />
                <Square value={squares[0][1]} onClick={() => onPlay(0,1)} />
                <Square value={squares[0][2]} onClick={() => onPlay(0,2)} />
            </div>
            <div className="board-row">
                <Square value={squares[1][0]} onClick={() => onPlay(1,0)} />
                <Square value={squares[1][1]} onClick={() => onPlay(1,1)} />
                <Square value={squares[1][2]} onClick={() => onPlay(1,2)} />
            </div>
            <div className="board-row">
                <Square value={squares[2][0]} onClick={() => onPlay(2,0)} />
                <Square value={squares[2][1]} onClick={() => onPlay(2,1)} />
                <Square value={squares[2][2]} onClick={() => onPlay(2,2)} />
            </div>
        </>
    );
}

export default Squares;