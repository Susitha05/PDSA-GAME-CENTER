import React, { useState, useEffect } from 'react';

const TowerOfHanoiBase = () => {
  const [pegs, setPegs] = useState(3);
  const [disks, setDisks] = useState(5);
  const [currentMoves, setCurrentMoves] = useState(0);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState('');
  const [optimalMoves, setOptimalMoves] = useState(31);
  const [moveLog, setMoveLog] = useState([]);
  const [showLog, setShowLog] = useState(false);
  const [playerName, setPlayerName] = useState('');
  const [showNameModal, setShowNameModal] = useState(false);
  const [tempName, setTempName] = useState('');
  const [draggedDisk, setDraggedDisk] = useState(null);
  const [draggedFrom, setDraggedFrom] = useState(null);
  const [towers, setTowers] = useState({
    A: [4, 3, 2, 1, 0],
    B: [],
    C: []
  });

  useEffect(() => {
    // Check for cached player name
    const cachedName = localStorage.getItem('hanoiPlayerName');
    if (cachedName) {
      setPlayerName(cachedName);
    } else {
      setShowNameModal(true);
    }
  }, []);

  useEffect(() => {
    const optimal = calculateOptimalMoves(disks, pegs);
    setOptimalMoves(optimal);
    initializeGame();
  }, [disks, pegs]);

  const handleNameSubmit = () => {
    if (tempName.trim()) {
      setPlayerName(tempName.trim());
      localStorage.setItem('hanoiPlayerName', tempName.trim());
      setShowNameModal(false);
    }
  };

  const calculateOptimalMoves = (numDisks, numPegs) => {
    if (numPegs === 3) {
      return Math.pow(2, numDisks) - 1;
    } else {
      // Frame-Stewart algorithm approximation for 4 pegs
      let moves = Infinity;
      for (let k = 1; k < numDisks; k++) {
        const movesK = 2 * calculateOptimalMoves(k, 4) + calculateOptimalMoves(numDisks - k, 3);
        moves = Math.min(moves, movesK);
      }
      return moves;
    }
  };

  const initializeGame = () => {
    const diskArray = Array.from({ length: disks }, (_, i) => disks - i - 1);
    if (pegs === 3) {
      setTowers({ A: diskArray, B: [], C: [] });
    } else {
      setTowers({ A: diskArray, B: [], C: [], D: [] });
    }
    setCurrentMoves(0);
    setMoveLog([]);
  };

  const increaseDiskCount = () => {
    if (disks < 10) {
      setDisks(disks + 1);
    }
  };

  const decreaseDiskCount = () => {
    if (disks > 5) {
      setDisks(disks - 1);
    }
  };

  const handleRestart = () => {
    initializeGame();
    setError('');
  };

  const handleLog = () => {
    setShowLog(!showLog);
  };

  const handleDragStart = (disk, fromPeg) => {
    const pegDisks = towers[fromPeg];
    // Only allow dragging the top disk
    if (pegDisks[pegDisks.length - 1] === disk) {
      setDraggedDisk(disk);
      setDraggedFrom(fromPeg);
    }
  };

  const handleDragOver = (e) => {
    e.preventDefault();
  };

  const handleDrop = (toPeg) => {
    if (!draggedDisk || !draggedFrom || draggedFrom === toPeg) {
      setDraggedDisk(null);
      setDraggedFrom(null);
      return;
    }

    const newTowers = { ...towers };
    const targetPegDisks = newTowers[toPeg];
    
    // Check if move is valid (can't place larger disk on smaller disk)
    if (targetPegDisks.length > 0 && draggedDisk > targetPegDisks[targetPegDisks.length - 1]) {
      setError('Cannot place a larger disk on a smaller disk!');
      setTimeout(() => setError(''), 3000);
      setDraggedDisk(null);
      setDraggedFrom(null);
      return;
    }

    // Perform the move
    newTowers[draggedFrom] = newTowers[draggedFrom].filter(d => d !== draggedDisk);
    newTowers[toPeg].push(draggedDisk);
    setTowers(newTowers);
    setCurrentMoves(currentMoves + 1);
    
    // Add to move log
    const move = `${draggedFrom} → ${toPeg}`;
    setMoveLog([...moveLog, move]);

    // Check if game is won
    const destinationPeg = pegs === 3 ? 'C' : 'D';
    if (newTowers[destinationPeg].length === disks) {
      setTimeout(() => {
        alert(`Congratulations ${playerName}! You won in ${currentMoves + 1} moves! Optimal: ${optimalMoves}`);
      }, 100);
    }

    setDraggedDisk(null);
    setDraggedFrom(null);
    setError('');
  };

  const handleSolve = async () => {
    setLoading(true);
    setError('');
    
    try {
      const response = await fetch('http://localhost:8080/api/hanoi/solve', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          disks: disks,
          pegs: pegs,
        }),
      });
      
      if (!response.ok) {
        throw new Error('Failed to get solution');
      }
      
      const data = await response.json();
      setMoveLog(data.moves || []);
      setShowLog(true);
    } catch (err) {
      setError('Auto-solve feature: Backend connection required');
    } finally {
      setLoading(false);
    }
  };

  const renderDisk = (size, pegName) => {
    const colors = ['#10B981', '#EC4899', '#FBBF24', '#3B82F6', '#8B5CF6', '#EF4444', '#F59E0B', '#84CC16', '#14B8A6', '#06B6D4'];
    const width = 60 + (size * 25);
    const color = colors[size % colors.length];
    const isTopDisk = towers[pegName] && towers[pegName][towers[pegName].length - 1] === size;
    
    return (
      <div
        draggable={isTopDisk}
        onDragStart={(e) => {
          e.stopPropagation();
          handleDragStart(size, pegName);
        }}
        className={`rounded-full mx-auto transition-all duration-200 ${isTopDisk ? 'cursor-grab active:cursor-grabbing' : 'cursor-not-allowed'}`}
        style={{
          width: `${width}px`,
          height: '24px',
          backgroundColor: color,
          border: '3px solid rgba(255,255,255,0.4)',
          boxShadow: isTopDisk ? '0 4px 8px rgba(0,0,0,0.5)' : '0 2px 4px rgba(0,0,0,0.3)',
        }}
      />
    );
  };

  const renderTower = (pegLabel, pegDisks) => {
    const maxDisks = 10;
    const pegHeight = maxDisks * 28;
    
    return (
      <div 
        className="relative flex flex-col items-center"
        onDragOver={handleDragOver}
        onDrop={() => handleDrop(pegLabel)}
        style={{ width: '180px' }}
      >
        {/* Tower container with absolute positioning */}
        <div className="relative" style={{ width: '180px', height: `${pegHeight + 16}px` }}>
          {/* Vertical peg - behind disks */}
          <div 
            className="absolute w-3 bg-red-500 rounded-t-sm left-1/2 transform -translate-x-1/2" 
            style={{ height: `${pegHeight}px`, bottom: '16px', zIndex: 0 }}
          />
          
          {/* Disks container - on top of base */}
          <div className="absolute bottom-0 left-0 right-0 flex flex-col-reverse items-center" style={{ paddingBottom: '16px', zIndex: 10 }}>
            {pegDisks.map((diskSize, idx) => (
              <div key={idx} style={{ marginBottom: '4px' }}>
                {renderDisk(diskSize, pegLabel)}
              </div>
            ))}
          </div>
          
          {/* Base platform - on top */}
          <div className="absolute bottom-0 left-0 right-0 h-4 bg-red-500 rounded-lg shadow-lg" style={{ width: '180px', zIndex: 20 }} />
        </div>
      </div>
    );
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-950 via-blue-900 to-indigo-950 text-white flex items-center justify-center p-4">
      {/* Player Name Modal */}
      {showNameModal && (
        <div className="fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4">
          <div className="bg-gradient-to-br from-blue-800 to-blue-900 rounded-2xl p-8 shadow-2xl border-4 border-cyan-500/50 max-w-md w-full">
            <h2 className="text-3xl font-bold text-cyan-400 mb-6 text-center">Welcome to Tower of Hanoi!</h2>
            <div className="space-y-4">
              <div>
                <label className="block text-lg font-semibold mb-3 text-cyan-300">
                  Enter Your Name:
                </label>
                <input
                  type="text"
                  value={tempName}
                  onChange={(e) => setTempName(e.target.value)}
                  onKeyPress={(e) => e.key === 'Enter' && handleNameSubmit()}
                  placeholder="Your name"
                  className="w-full px-4 py-3 bg-blue-950 border-2 border-cyan-500/50 rounded-lg text-white placeholder-gray-400 focus:outline-none focus:border-cyan-400 transition-colors text-lg"
                  autoFocus
                />
              </div>
              <button
                onClick={handleNameSubmit}
                disabled={!tempName.trim()}
                className="w-full py-3 px-6 bg-gradient-to-r from-cyan-500 to-blue-500 hover:from-cyan-400 hover:to-blue-400 disabled:from-gray-600 disabled:to-gray-700 text-white font-bold text-xl rounded-lg shadow-lg transition-all disabled:cursor-not-allowed transform hover:scale-105"
              >
                Start Game
              </button>
            </div>
          </div>
        </div>
      )}

      <div className="w-full max-w-5xl">
        {/* Header */}
        <div className="text-center mb-6">
          <h1 className="text-6xl font-bold text-cyan-400 tracking-wider mb-4">
            Tower of Hanoi
          </h1>
          {playerName && (
            <p className="text-cyan-300 text-xl mb-2">
              Player: <span className="font-bold text-white">{playerName}</span>
            </p>
          )}
          <p className="text-white text-lg max-w-3xl mx-auto">
            The object of the game is to move all the disks over to Tower {pegs === 3 ? '3' : '4'} (drag and drop). But you cannot place a larger disk onto a smaller disk.
          </p>
        </div>

        {/* Game Container */}
        <div className="bg-blue-900/40 rounded-3xl p-8 border-4 border-cyan-500/40 shadow-2xl">
          {/* Control Panel */}
          <div className="flex flex-wrap items-center justify-between gap-4 mb-6 bg-blue-950/60 rounded-xl p-4 border-2 border-cyan-500/30">
            {/* Disk Controls */}
            <div className="flex items-center gap-3">
              <span className="text-white font-semibold text-lg">Disks:</span>
              <button
                onClick={decreaseDiskCount}
                disabled={disks <= 5}
                className="w-10 h-10 bg-blue-600 hover:bg-blue-500 disabled:bg-gray-600 disabled:cursor-not-allowed rounded-lg flex items-center justify-center text-white font-bold text-xl transition-colors border-2 border-cyan-500/50"
              >
                ▼
              </button>
              <span className="bg-blue-800 px-6 py-2 rounded-lg text-white font-bold text-xl min-w-[50px] text-center border-2 border-cyan-500/50">
                {disks}
              </span>
              <button
                onClick={increaseDiskCount}
                disabled={disks >= 10}
                className="w-10 h-10 bg-blue-600 hover:bg-blue-500 disabled:bg-gray-600 disabled:cursor-not-allowed rounded-lg flex items-center justify-center text-white font-bold text-xl transition-colors border-2 border-cyan-500/50"
              >
                ▲
              </button>
            </div>

            {/* Moves Display */}
            <div className="flex items-center gap-3">
              <span className="text-white font-semibold text-lg">Moves:</span>
              <span className="bg-blue-600 px-6 py-2 rounded-lg text-white font-bold text-xl min-w-[50px] text-center border-2 border-cyan-500/50">
                {currentMoves}
              </span>
            </div>

            {/* Action Buttons */}
            <div className="flex items-center gap-3">
              <button
                onClick={handleRestart}
                className="px-6 py-2 bg-cyan-600 hover:bg-cyan-500 text-white font-bold rounded-lg transition-colors border-2 border-cyan-400 shadow-lg"
              >
                Restart
              </button>
              <button
                onClick={handleLog}
                className="px-6 py-2 bg-cyan-600 hover:bg-cyan-500 text-white font-bold rounded-lg transition-colors border-2 border-cyan-400 shadow-lg"
              >
                Log
              </button>
              <button
                onClick={handleSolve}
                disabled={loading}
                className="px-6 py-2 bg-cyan-600 hover:bg-cyan-500 disabled:bg-gray-600 text-white font-bold rounded-lg transition-colors border-2 border-cyan-400 shadow-lg disabled:cursor-not-allowed"
              >
                {loading ? 'Solving...' : 'Solve!'}
              </button>
            </div>
          </div>

          {/* Tower Visualization */}
          <div className="bg-blue-950/30 rounded-2xl p-8 border-2 border-cyan-500/30 mb-4">
            <div className={`grid ${pegs === 3 ? 'grid-cols-3' : 'grid-cols-4'} gap-12 justify-items-center`}>
              {renderTower('A', towers.A)}
              {renderTower('B', towers.B)}
              {renderTower('C', towers.C)}
              {pegs === 4 && renderTower('D', towers.D)}
            </div>
          </div>

          {/* Minimum Moves Display */}
          <div className="text-center text-white text-xl font-semibold">
            Minimum Moves: <span className="text-cyan-400">{optimalMoves}</span>
          </div>
        </div>

        {/* Error Message */}
        {error && (
          <div className="mt-4 bg-red-900/80 border-2 border-red-500 text-white px-6 py-3 rounded-lg shadow-xl">
            <div className="flex items-center justify-center">
              <svg className="w-5 h-5 mr-2" fill="currentColor" viewBox="0 0 20 20">
                <path fillRule="evenodd" d="M10 18a8 8 0 100-16 8 8 0 000 16zM8.707 7.293a1 1 0 00-1.414 1.414L8.586 10l-1.293 1.293a1 1 0 101.414 1.414L10 11.414l1.293 1.293a1 1 0 001.414-1.414L11.414 10l1.293-1.293a1 1 0 00-1.414-1.414L10 8.586 8.707 7.293z" clipRule="evenodd" />
              </svg>
              <span className="font-semibold">{error}</span>
            </div>
          </div>
        )}

        {/* Move Log Modal */}
        {showLog && (
          <div className="mt-4 bg-blue-900/90 border-2 border-cyan-500 rounded-lg p-6 shadow-2xl max-h-96 overflow-y-auto">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-2xl font-bold text-cyan-400">Move Log</h3>
              <button
                onClick={() => setShowLog(false)}
                className="text-white hover:text-red-400 text-2xl font-bold"
              >
                ✕
              </button>
            </div>
            {moveLog.length === 0 ? (
              <p className="text-white text-center">No moves recorded yet</p>
            ) : (
              <div className="space-y-2">
                {moveLog.map((move, index) => (
                  <div key={index} className="bg-blue-950/50 px-4 py-2 rounded border border-cyan-500/30 text-white">
                    <span className="font-bold text-cyan-400">Move {index + 1}:</span> {move}
                  </div>
                ))}
              </div>
            )}
          </div>
        )}
      </div>
    </div>
  );
};

export default TowerOfHanoiBase;
