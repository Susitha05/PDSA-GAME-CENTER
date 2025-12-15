import React from 'react';

const Board = ({ size, snakes, ladders, playerPos, computerPos }) => {
    const totalCells = size * size;
    const cells = Array.from({ length: totalCells }, (_, i) => totalCells - i);

    // Helper to get Center coordinates of a cell (1-based index)
    const getLogicCol = (cell, size) => {
        return (cell - 1) % size; // Simple 0-based col index (left to right)
    };

    const getCoordinates = (cell) => {
        let r = Math.floor((cell - 1) / size);
        let c = (cell - 1) % size;

        if (r % 2 === 1) {
            c = size - 1 - c;
        }

        const x = (c + 0.5) * (100 / size);
        const y = (size - 1 - r + 0.5) * (100 / size);

        return { x, y };
    };

    return (
        <div className="board-container" style={{ width: '600px', height: '600px' }}>
            <div
                className="board-grid"
                style={{
                    gridTemplateColumns: `repeat(${size}, 1fr)`,
                    gridTemplateRows: `repeat(${size}, 1fr)`,
                    height: '100%'
                }}
            >
                {/* Render Cells in Reverse Order for Visual Layout */}
                {(() => {
                    const visualCells = [];
                    for (let r = size - 1; r >= 0; r--) {
                        const rowCells = [];
                        for (let c = 0; c < size; c++) {
                            let val;
                            if (r % 2 === 0) { // Even row index from bottom (0, 2, 4...) -> Left to Right
                                val = r * size + c + 1;
                            } else { // Odd row -> Right to Left
                                val = r * size + (size - 1 - c) + 1;
                            }
                            rowCells.push(val);
                        }
                        visualCells.push(...rowCells);
                    }

                    return visualCells.map((num) => (
                        <div key={num} className={`cell ${num % 2 === 0 ? 'dark' : ''}`}>
                            <span className="number">{num}</span>
                            <div className="tokens-container">
                                {playerPos === num && <div className="player-token" title="You" />}
                                {computerPos === num && <div className="computer-token" title="Computer" />}
                            </div>
                        </div>
                    ));
                })()}
            </div>

            {/* Image Overlay for Snakes and Ladders */}
            <div className="overlay-container" style={{ position: 'absolute', top: 0, left: 0, width: '100%', height: '100%', pointerEvents: 'none', overflow: 'hidden' }}>
                {snakes.map((s, i) => {
                    const start = getCoordinates(s.start);
                    const end = getCoordinates(s.end);

                    const startRow = Math.floor((s.start - 1) / size);
                    const endRow = Math.floor((s.end - 1) / size);
                    const rowDiff = startRow - endRow;

                    // Asset Selection:
                    // - 300px assets for short snakes (rowDiff <= 3)
                    // - 1000px assets for long snakes (rowDiff > 3)
                    const isShort = rowDiff <= 3;
                    let asset;
                    const seed = (s.start * 13 + s.end * 7 + i) % 2;

                    if (isShort) {
                        asset = seed === 0
                            ? `${process.env.PUBLIC_URL}/assets/300pxSnake.png`
                            : `${process.env.PUBLIC_URL}/assets/300pxSnake2.png`;
                    } else {
                        asset = seed === 0
                            ? `${process.env.PUBLIC_URL}/assets/1000pxSnake.png`
                            : `${process.env.PUBLIC_URL}/assets/1000pxSnake2.png`;
                    }

                    const dx = end.x - start.x;
                    const dy = end.y - start.y;
                    const visualLength = Math.sqrt(dx * dx + dy * dy);
                    const angle = Math.atan2(dy, dx) * (180 / Math.PI) - 90;

                    const midX = (start.x + end.x) / 2;
                    const midY = (start.y + end.y) / 2;

                    const cellSize = 600 / size;
                    // For 300px: use fixed dimensions to avoid stretching
                    // For 1000px: scale to fit the gap
                    const assetWidth = isShort ? cellSize * 0.55 : cellSize * 0.45;
                    // Height: use natural proportions for short, stretch for long
                    const assetHeight = isShort
                        ? Math.min(visualLength, 35) // Cap height for 300px assets
                        : visualLength;

                    return (
                        <img
                            key={`s-${i}`}
                            src={asset}
                            alt="snake"
                            style={{
                                position: 'absolute',
                                top: `${midY}%`,
                                left: `${midX}%`,
                                width: `${assetWidth}px`,
                                height: `${assetHeight}%`,
                                objectFit: 'contain',
                                transform: `translate(-50%, -50%) rotate(${angle}deg)`,
                                transformOrigin: 'center center',
                                filter: 'drop-shadow(2px 2px 3px rgba(0,0,0,0.4))',
                                zIndex: 5,
                                opacity: 0.95
                            }}
                        />
                    );
                })}
                {ladders.map((l, i) => {
                    const start = getCoordinates(l.start);
                    const end = getCoordinates(l.end);

                    const startRow = Math.floor((l.start - 1) / size);
                    const endRow = Math.floor((l.end - 1) / size);
                    const rowDiff = endRow - startRow;

                    // Asset Selection:
                    // - 300px assets for short ladders (rowDiff <= 3)
                    // - 1000px assets for long ladders (rowDiff > 3)
                    const isShort = rowDiff <= 3;
                    let asset;
                    const seed = (l.start * 11 + l.end * 3 + i) % 2;

                    if (isShort) {
                        asset = seed === 0
                            ? `${process.env.PUBLIC_URL}/assets/300pxLadder.png`
                            : `${process.env.PUBLIC_URL}/assets/300pxLadder2.png`;
                    } else {
                        asset = seed === 0
                            ? `${process.env.PUBLIC_URL}/assets/1000pxLadder.png`
                            : `${process.env.PUBLIC_URL}/assets/1000pxLadder2.png`;
                    }

                    const dx = end.x - start.x;
                    const dy = end.y - start.y;
                    const visualLength = Math.sqrt(dx * dx + dy * dy);
                    const angle = Math.atan2(dy, dx) * (180 / Math.PI) - 90;

                    const midX = (start.x + end.x) / 2;
                    const midY = (start.y + end.y) / 2;

                    const cellSize = 600 / size;
                    const assetWidth = isShort ? cellSize * 0.55 : cellSize * 0.45;
                    const assetHeight = isShort
                        ? Math.min(visualLength, 35)
                        : visualLength;

                    return (
                        <img
                            key={`l-${i}`}
                            src={asset}
                            alt="ladder"
                            style={{
                                position: 'absolute',
                                top: `${midY}%`,
                                left: `${midX}%`,
                                width: `${assetWidth}px`,
                                height: `${assetHeight}%`,
                                objectFit: 'contain',
                                transform: `translate(-50%, -50%) rotate(${angle}deg)`,
                                transformOrigin: 'center center',
                                filter: 'drop-shadow(2px 2px 3px rgba(0,0,0,0.4))',
                                zIndex: 10,
                                opacity: 0.95
                            }}
                        />
                    );
                })}
            </div>
        </div>
    );
};

export default Board;
