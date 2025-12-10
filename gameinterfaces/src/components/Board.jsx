import React from 'react';

const Board = ({ size, snakes, ladders, playerPos }) => {
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
                            {playerPos === num && <div className="player-token" />}
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

                    // Asset Selection Logic
                    // 1, 2 = Short (150-500px, roughly 1-3 rows?)
                    // 3, 4 = Long (1000px+, can stretch)

                    // Logic: If rowDiff <= 3, use Short [1, 2]
                    // If rowDiff > 3, use Long [3, 4]

                    let asset;
                    // Seeded random pick to separate instances
                    const seed = (s.start * 13 + s.end * 7 + i) % 2;

                    if (rowDiff <= 3) {
                        asset = seed === 0 ? `${process.env.PUBLIC_URL}/assets/1.png` : `${process.env.PUBLIC_URL}/assets/2.png`;
                    } else {
                        asset = seed === 0 ? `${process.env.PUBLIC_URL}/assets/3.png` : `${process.env.PUBLIC_URL}/assets/4.png`;
                    }

                    const dx = end.x - start.x;
                    const dy = end.y - start.y;
                    const length = Math.sqrt(dx * dx + dy * dy);
                    const angle = Math.atan2(dy, dx) * (180 / Math.PI) - 90;

                    const midX = (start.x + end.x) / 2;
                    const midY = (start.y + end.y) / 2;

                    // Width Logic:
                    // Short assets (1,2) are 150-500px length. 
                    // Long assets (3,4) are 1000px length.
                    // We should keep them relatively proportional.
                    // But for "clean cartoon" look, maybe fixed width is safe if file aspect ratio is good.
                    // Let's us e 60% of cell size.
                    const cellSize = 600 / size;
                    const assetWidth = cellSize * 0.45; // Reduced for less overlap

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
                                height: `${length}%`,
                                transform: `translate(-50%, -50%) rotate(${angle}deg)`,
                                transformOrigin: 'center center',
                                filter: 'drop-shadow(2px 2px 2px rgba(0,0,0,0.3))',
                                zIndex: 5, // Snakes below ladders
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
                    const rowDiff = endRow - startRow; // Positive for Ladder

                    // 5, 6 = Short
                    // 7, 8 = Long

                    let asset;
                    const seed = (l.start * 11 + l.end * 3 + i) % 2;

                    if (rowDiff <= 3) {
                        asset = seed === 0 ? `${process.env.PUBLIC_URL}/assets/5.png` : `${process.env.PUBLIC_URL}/assets/6.png`;
                    } else {
                        asset = seed === 0 ? `${process.env.PUBLIC_URL}/assets/7.png` : `${process.env.PUBLIC_URL}/assets/8.png`;
                    }

                    const dx = end.x - start.x;
                    const dy = end.y - start.y;
                    const length = Math.sqrt(dx * dx + dy * dy);
                    const angle = Math.atan2(dy, dx) * (180 / Math.PI) - 90;

                    const midX = (start.x + end.x) / 2;
                    const midY = (start.y + end.y) / 2;

                    const cellSize = 600 / size;
                    const assetWidth = cellSize * 0.45; // Reduced for less overlap

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
                                height: `${length}%`,
                                transform: `translate(-50%, -50%) rotate(${angle}deg)`,
                                transformOrigin: 'center center',
                                filter: 'drop-shadow(2px 2px 2px rgba(0,0,0,0.3))',
                                zIndex: 10, // Ladders above snakes
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
