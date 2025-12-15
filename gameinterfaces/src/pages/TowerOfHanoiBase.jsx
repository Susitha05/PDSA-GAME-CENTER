import React, { useState, useEffect } from 'react';
import { hanoiApi } from '../services/TowerofHanoiAPI';

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
  const [showPegSelector, setShowPegSelector] = useState(false);
  const [showResults, setShowResults] = useState(false);
  const [tempName, setTempName] = useState('');
  const [draggedDisk, setDraggedDisk] = useState(null);
  const [draggedFrom, setDraggedFrom] = useState(null);
  const [roundId, setRoundId] = useState(null);
  const [gameStarted, setGameStarted] = useState(false);
  const [algorithmResults, setAlgorithmResults] = useState(null);
  const [towers, setTowers] = useState({
    A: [4, 3, 2, 1, 0],
    B: [],
    C: []
  });

  useEffect(() => {
    // Always show name modal on component mount
    setShowNameModal(true);
  }, []);

  useEffect(() => {
    const optimal = calculateOptimalMoves(disks, pegs);
    setOptimalMoves(optimal);
  }, [disks, pegs]);

  useEffect(() => {
    if (gameStarted) {
      initializeGame();
    }
  }, [gameStarted]);

  const handleNameSubmit = () => {
    if (tempName.trim()) {
      setPlayerName(tempName.trim());
      setShowNameModal(false);
      setShowPegSelector(true);
    }
  };

  const handlePegSelection = async (selectedPegs) => {
    setPegs(selectedPegs);
    setShowPegSelector(false);
    setLoading(true);
    
    try {
      const response = await hanoiApi.startGame();
      setRoundId(response.roundId);
      setDisks(response.diskCount);
      
      const diskArray = Array.from({ length: response.diskCount }, (_, i) => response.diskCount - i - 1);
      if (selectedPegs === 3) {
        setTowers({ A: diskArray, B: [], C: [] });
      } else {
        setTowers({ A: diskArray, B: [], C: [], D: [] });
      }
      setCurrentMoves(0);
      setMoveLog([]);
      
      setGameStarted(true);
    } catch (err) {
      setError('Failed to start game. Please check backend connection.');
      setTimeout(() => setError(''), 5000);
    } finally {
      setLoading(false);
    }
  };

  const calculateOptimalMoves = (numDisks, numPegs) => {
    if (numPegs === 3) {
      return Math.pow(2, numDisks) - 1;
    } else {
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

  const handleRestart = async () => {
    setLoading(true);
    setAlgorithmResults(null);
    setShowResults(false);
    
    try {
      const response = await hanoiApi.startGame();
      setRoundId(response.roundId);
      setDisks(response.diskCount);
      
      // Initialize towers immediately
      const diskArray = Array.from({ length: response.diskCount }, (_, i) => response.diskCount - i - 1);
      if (pegs === 3) {
        setTowers({ A: diskArray, B: [], C: [] });
      } else {
        setTowers({ A: diskArray, B: [], C: [], D: [] });
      }
      setCurrentMoves(0);
      setMoveLog([]);
      
      setError('');
    } catch (err) {
      setError('Failed to restart game. Please check backend connection.');
      setTimeout(() => setError(''), 5000);
    } finally {
      setLoading(false);
    }
  };

  const handleLog = () => {
    setShowLog(!showLog);
  };

  const handleDragStart = (disk, fromPeg) => {
    const pegDisks = towers[fromPeg];
    const isTopDisk = pegDisks[pegDisks.length - 1] === disk;
    console.log('Drag start - Disk:', disk, 'From peg:', fromPeg, 'Is top disk:', isTopDisk);
    
    if (isTopDisk) {
      setDraggedDisk(disk);
      setDraggedFrom(fromPeg);
      console.log('Drag started successfully');
    } else {
      console.log('Cannot drag - not the top disk');
    }
  };

  const handleDragOver = (e) => {
    e.preventDefault();
  };

  const handleDrop = (toPeg) => {
    console.log('Drop event triggered on peg:', toPeg);
    console.log('Dragged disk:', draggedDisk, 'from peg:', draggedFrom);
    
    if (!draggedDisk && draggedDisk !== 0) {
      console.log('No disk being dragged');
      setDraggedDisk(null);
      setDraggedFrom(null);
      return;
    }
    
    if (!draggedFrom) {
      console.log('No source peg');
      setDraggedDisk(null);
      setDraggedFrom(null);
      return;
    }
    
    if (draggedFrom === toPeg) {
      console.log('Same peg - no move');
      setDraggedDisk(null);
      setDraggedFrom(null);
      return;
    }

    const newTowers = {};
    Object.keys(towers).forEach(key => {
      newTowers[key] = [...towers[key]];
    });
    
    const targetPegDisks = newTowers[toPeg];
    
    if (targetPegDisks.length > 0 && draggedDisk > targetPegDisks[targetPegDisks.length - 1]) {
      console.log('Invalid move - larger disk on smaller disk');
      setError('Cannot place a larger disk on a smaller disk!');
      setTimeout(() => setError(''), 3000);
      setDraggedDisk(null);
      setDraggedFrom(null);
      return;
    }

    newTowers[draggedFrom] = newTowers[draggedFrom].filter(d => d !== draggedDisk);
    newTowers[toPeg] = [...newTowers[toPeg], draggedDisk];
    
    console.log('Move successful:', draggedFrom, '→', toPeg);
    console.log('New towers state:', newTowers);
    
    setTowers(newTowers);
    setCurrentMoves(currentMoves + 1);
    
    const move = `${draggedFrom} → ${toPeg}`;
    const updatedMoveLog = [...moveLog, move];
    setMoveLog(updatedMoveLog);

    const destinationPeg = pegs === 3 ? 'C' : 'D';
    if (newTowers[destinationPeg].length === disks) {
      console.log('Puzzle solved!');
      console.log('Total moves including this one:', updatedMoveLog.length);
      // Don't auto-submit - let user click Submit button
      setError('Congratulations! Puzzle solved! Click "Submit Solution" to save your score.');
      setTimeout(() => setError(''), 5000);
    }

    setDraggedDisk(null);
    setDraggedFrom(null);
    setError('');
  };

  const handleSubmitSolution = async () => {
    if (!roundId || moveLog.length === 0) {
      setError('No moves to submit!');
      setTimeout(() => setError(''), 3000);
      return;
    }

    if (!playerName || playerName.trim() === '') {
      setError('Player name is required!');
      setTimeout(() => setError(''), 3000);
      return;
    }

    setLoading(true);
    
    try {
      console.log('Submitting moves:', moveLog);
      console.log('Move log sample:', moveLog.slice(0, 3));
      const response = await hanoiApi.submitSolution(roundId, playerName, disks, pegs, moveLog);
      console.log('Submit solution response:', response);
      setAlgorithmResults(response.algorithmResults);
      
      if (response.isValid) {
        setShowResults(true);
      } else {
        setError(response.message || 'Invalid solution!');
        setTimeout(() => setError(''), 5000);
      }
    } catch (err) {
      console.error('Submit error:', err);
      const errorMessage = err.message || 'Failed to submit solution. Please check backend connection.';
      setError(errorMessage);
      setTimeout(() => setError(''), 8000);
    } finally {
      setLoading(false);
    }
  };

  const handleViewAlgorithms = () => {
    setShowResults(!showResults);
  };

  const renderDisk = (size, pegName) => {
    const colors = ['#10B981', '#EC4899', '#FBBF24', '#3B82F6', '#8B5CF6', '#EF4444', '#F59E0B', '#84CC16', '#14B8A6', '#06B6D4'];
    const width = 60 + (size * 25);
    const color = colors[size % colors.length];
    const isTopDisk = towers[pegName] && towers[pegName][towers[pegName].length - 1] === size;
    const isBeingDragged = draggedDisk === size && draggedFrom === pegName;
    
    return (
      <div
        draggable={isTopDisk}
        onDragStart={(e) => {
          e.stopPropagation();
          handleDragStart(size, pegName);
        }}
        onDragEnd={(e) => {
          e.stopPropagation();
          console.log('Drag ended');
        }}
        className={`rounded-full mx-auto transition-all duration-200 ${isTopDisk ? 'cursor-grab active:cursor-grabbing' : 'cursor-not-allowed'}`}
        style={{
          width: `${width}px`,
          height: '24px',
          backgroundColor: color,
          border: '3px solid rgba(255,255,255,0.4)',
          boxShadow: isTopDisk ? '0 4px 8px rgba(0,0,0,0.5)' : '0 2px 4px rgba(0,0,0,0.3)',
          opacity: isBeingDragged ? 0.5 : 1,
        }}
      />
    );
  };

  const renderTower = (pegLabel, pegDisks) => {
    const maxDisks = 10;
    const pegHeight = maxDisks * 28;
    
    const disksArray = pegDisks || [];
    
    return (
      <div 
        className="relative flex flex-col items-center"
        onDragOver={handleDragOver}
        onDrop={(e) => {
          e.preventDefault();
          e.stopPropagation();
          handleDrop(pegLabel);
        }}
        style={{ width: '180px' }}
      >
        <div className="relative" style={{ width: '180px', height: `${pegHeight + 16}px` }}>
          <div 
            className="absolute w-3 bg-red-500 rounded-t-sm left-1/2 transform -translate-x-1/2" 
            style={{ height: `${pegHeight}px`, bottom: '16px', zIndex: 0, pointerEvents: 'none' }}
          />
          
          <div className="absolute bottom-0 left-0 right-0 flex flex-col-reverse items-center" style={{ paddingBottom: '16px', zIndex: 10, pointerEvents: 'none' }}>
            {disksArray.map((diskSize, idx) => (
              <div key={idx} style={{ marginBottom: '4px', pointerEvents: 'auto' }}>
                {renderDisk(diskSize, pegLabel)}
              </div>
            ))}
          </div>
          
          <div className="absolute bottom-0 left-0 right-0 h-4 bg-red-500 rounded-lg shadow-lg" style={{ width: '180px', zIndex: 20, pointerEvents: 'none' }} />
        </div>
        
        <div className="mt-3 text-center font-bold text-3xl text-cyan-400">
          {pegLabel}
        </div>
      </div>
    );
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-blue-950 via-blue-900 to-indigo-950 text-white flex items-center justify-center p-4">
      {showPegSelector && (
        <div className="fixed inset-0 bg-black/70 flex items-center justify-center z-50 p-4">
          <div className="bg-gradient-to-br from-blue-800 to-blue-900 rounded-2xl p-8 shadow-2xl border-4 border-cyan-500/50 max-w-md w-full">
            <h2 className="text-3xl font-bold text-cyan-400 mb-6 text-center">Select Number of Pegs</h2>
            <div className="space-y-4">
              <button
                onClick={() => handlePegSelection(3)}
                disabled={loading}
                className="w-full py-4 px-6 bg-gradient-to-r from-cyan-500 to-blue-500 hover:from-cyan-400 hover:to-blue-400 disabled:from-gray-600 disabled:to-gray-700 text-white font-bold text-xl rounded-lg shadow-lg transition-all disabled:cursor-not-allowed transform hover:scale-105"
              >
                3 Pegs (Classic)
              </button>
              <button
                onClick={() => handlePegSelection(4)}
                disabled={loading}
                className="w-full py-4 px-6 bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-400 hover:to-pink-400 disabled:from-gray-600 disabled:to-gray-700 text-white font-bold text-xl rounded-lg shadow-lg transition-all disabled:cursor-not-allowed transform hover:scale-105"
              >
                4 Pegs (Frame-Stewart)
              </button>
              {loading && (
                <p className="text-center text-cyan-300">Starting game...</p>
              )}
            </div>
          </div>
        </div>
      )}

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
            Move all the disks from <span className="font-bold text-green-400">Peg A (Source)</span> to <span className="font-bold text-yellow-400">Peg {pegs === 3 ? 'C' : 'D'} (Destination)</span> using drag and drop. You cannot place a larger disk onto a smaller disk.
          </p>
        </div>

        <div className="bg-blue-900/40 rounded-3xl p-8 border-4 border-cyan-500/40 shadow-2xl">
          <div className="flex flex-wrap items-center justify-between gap-4 mb-6 bg-blue-950/60 rounded-xl p-4 border-2 border-cyan-500/30">
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

            <div className="flex items-center gap-3">
              <span className="text-white font-semibold text-lg">Moves:</span>
              <span className="bg-blue-600 px-6 py-2 rounded-lg text-white font-bold text-xl min-w-[50px] text-center border-2 border-cyan-500/50">
                {currentMoves}
              </span>
            </div>

            <div className="flex items-center gap-3">
              <button
                onClick={handleRestart}
                disabled={loading}
                className="px-6 py-2 bg-cyan-600 hover:bg-cyan-500 disabled:bg-gray-600 text-white font-bold rounded-lg transition-colors border-2 border-cyan-400 shadow-lg disabled:cursor-not-allowed"
              >
                {loading ? 'Restarting...' : 'New Round'}
              </button>
              <button
                onClick={handleLog}
                className="px-6 py-2 bg-cyan-600 hover:bg-cyan-500 text-white font-bold rounded-lg transition-colors border-2 border-cyan-400 shadow-lg"
              >
                My Moves
              </button>
              <button
                onClick={handleSubmitSolution}
                disabled={loading || moveLog.length === 0}
                className="px-6 py-2 bg-green-600 hover:bg-green-500 disabled:bg-gray-600 text-white font-bold rounded-lg transition-colors border-2 border-green-400 shadow-lg disabled:cursor-not-allowed"
              >
                {loading ? 'Submitting...' : 'Submit Solution'}
              </button>
              {algorithmResults && (
                <button
                  onClick={handleViewAlgorithms}
                  className="px-6 py-2 bg-purple-600 hover:bg-purple-500 text-white font-bold rounded-lg transition-colors border-2 border-purple-400 shadow-lg"
                >
                  View Algorithms
                </button>
              )}
            </div>
          </div>

          <div className="bg-blue-950/30 rounded-2xl p-8 border-2 border-cyan-500/30 mb-4">
            <div className={`grid ${pegs === 3 ? 'grid-cols-3' : 'grid-cols-4'} gap-12 justify-items-center`}>
              {renderTower('A', towers.A)}
              {renderTower('B', towers.B)}
              {renderTower('C', towers.C)}
              {pegs === 4 && towers.D !== undefined && renderTower('D', towers.D)}
            </div>
          </div>

          <div className="text-center text-white text-xl font-semibold">
            Minimum Moves: <span className="text-cyan-400">{optimalMoves}</span>
            {roundId && (
              <span className="ml-6 text-sm text-gray-400">Round ID: {roundId.substring(0, 8)}...</span>
            )}
          </div>
        </div>

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

        {showLog && (
          <div className="mt-4 bg-blue-900/90 border-2 border-cyan-500 rounded-lg p-6 shadow-2xl max-h-96 overflow-y-auto">
            <div className="flex justify-between items-center mb-4">
              <h3 className="text-2xl font-bold text-cyan-400">My Moves ({moveLog.length})</h3>
              <button
                onClick={() => setShowLog(false)}
                className="text-white hover:text-red-400 text-2xl font-bold"
              >
                ✕
              </button>
            </div>
            {moveLog.length === 0 ? (
              <p className="text-white text-center">No moves recorded yet. Start playing!</p>
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

        {showResults && algorithmResults && (
          <div className="mt-4 bg-gradient-to-br from-purple-900/90 to-blue-900/90 border-2 border-purple-500 rounded-lg p-6 shadow-2xl">
            <div className="flex justify-between items-center mb-6">
              <h3 className="text-3xl font-bold text-purple-300">Algorithm Comparison Results</h3>
              <button
                onClick={() => setShowResults(false)}
                className="text-white hover:text-red-400 text-2xl font-bold"
              >
                ✕
              </button>
            </div>
            
            <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
              <div className="bg-blue-950/50 rounded-lg p-5 border border-cyan-500/30">
                <h4 className="text-xl font-bold text-cyan-400 mb-3">3-Peg Recursive</h4>
                <div className="space-y-2 text-white">
                  <p><span className="font-semibold">Moves:</span> {algorithmResults.recursive3Peg.moveCount}</p>
                  <p><span className="font-semibold">Time:</span> {algorithmResults.recursive3Peg.executionTimeMs.toFixed(4)} ms</p>
                  <p className="text-sm text-gray-300">({algorithmResults.recursive3Peg.executionTimeNanos} ns)</p>
                </div>
              </div>

              <div className="bg-blue-950/50 rounded-lg p-5 border border-cyan-500/30">
                <h4 className="text-xl font-bold text-cyan-400 mb-3">3-Peg Iterative</h4>
                <div className="space-y-2 text-white">
                  <p><span className="font-semibold">Moves:</span> {algorithmResults.iterative3Peg.moveCount}</p>
                  <p><span className="font-semibold">Time:</span> {algorithmResults.iterative3Peg.executionTimeMs.toFixed(4)} ms</p>
                  <p className="text-sm text-gray-300">({algorithmResults.iterative3Peg.executionTimeNanos} ns)</p>
                </div>
              </div>

              <div className="bg-purple-950/50 rounded-lg p-5 border border-purple-500/30">
                <h4 className="text-xl font-bold text-purple-400 mb-3">4-Peg Frame-Stewart</h4>
                <div className="space-y-2 text-white">
                  <p><span className="font-semibold">Moves:</span> {algorithmResults.frameStewart4Peg.moveCount}</p>
                  <p><span className="font-semibold">Time:</span> {algorithmResults.frameStewart4Peg.executionTimeMs.toFixed(4)} ms</p>
                  <p className="text-sm text-gray-300">({algorithmResults.frameStewart4Peg.executionTimeNanos} ns)</p>
                </div>
              </div>

              <div className="bg-purple-950/50 rounded-lg p-5 border border-purple-500/30">
                <h4 className="text-xl font-bold text-purple-400 mb-3">4-Peg Dynamic Programming</h4>
                <div className="space-y-2 text-white">
                  <p><span className="font-semibold">Moves:</span> {algorithmResults.dynamic4Peg.moveCount}</p>
                  <p><span className="font-semibold">Time:</span> {algorithmResults.dynamic4Peg.executionTimeMs.toFixed(4)} ms</p>
                  <p className="text-sm text-gray-300">({algorithmResults.dynamic4Peg.executionTimeNanos} ns)</p>
                </div>
              </div>
            </div>

            <div className="mt-6 p-4 bg-green-900/30 border border-green-500/50 rounded-lg">
              <p className="text-green-300 text-center font-semibold">
                ✓ Your solution with {moveLog.length} moves has been saved to the database!
              </p>
              <p className="text-white text-center mt-2">
                Player: <span className="font-bold text-cyan-400">{playerName}</span> | 
                Disks: <span className="font-bold text-cyan-400">{disks}</span> | 
                Pegs: <span className="font-bold text-cyan-400">{pegs}</span>
              </p>
            </div>
          </div>
        )}
      </div>
    </div>
  );
};

export default TowerOfHanoiBase;
