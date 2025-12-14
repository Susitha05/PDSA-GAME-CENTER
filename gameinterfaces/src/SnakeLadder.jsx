import React, { useState } from 'react';
import Board3D from './components/Board3D';
import ControlPanel from './components/ControlPanel';
import StatsModal from './components/StatsModal';
import { startGame, submitGuess, runSimulation, getPath } from './services/api';
import SimulationReport from './components/SimulationReport';

/**
 * Snake and Ladder Game - Quiz-Based Puzzle
 * 
 * This is NOT a real-time board game. The player's task is to identify
 * the minimum number of dice throws required to reach the final cell.
 * 
 * Game Flow:
 * 1. SETUP: Player enters name, selects board size, optionally enables "Vs Computer"
 * 2. GUESSING: Player selects one answer from 3 choices
 * 3. RESULT: WIN/LOSE/DRAW displayed based on guess correctness
 * 
 * Dice Logic (IMPORTANT):
 * - Dice values {1-6} are simulated ONLY inside algorithms (BFS/Dijkstra)
 * - No physical dice rolling or randomness during solving
 * - Algorithms explore all possible dice outcomes as state transitions
 */
function SnakeLadder() {
  // Game states: SETUP -> GUESSING -> RESULT
  const [gameState, setGameState] = useState('SETUP');
  const [boardData, setBoardData] = useState(null);
  const [choices, setChoices] = useState([]);
  const [gameId, setGameId] = useState(null);

  // Player info
  const [playerName, setPlayerName] = useState('');
  const [isVsComputer, setIsVsComputer] = useState(false);

  // Result state
  const [playerGuess, setPlayerGuess] = useState(null);
  const [aiGuess, setAiGuess] = useState(null);
  const [correctAnswer, setCorrectAnswer] = useState(null);
  const [gameResult, setGameResult] = useState(null); // 'WIN', 'LOSE', 'DRAW'

  // Path visualization
  const [showPath, setShowPath] = useState(false);
  const [optimalPath, setOptimalPath] = useState([]);

  // UI state
  const [message, setMessage] = useState('');
  const [showStats, setShowStats] = useState(false);
  const [simulationData, setSimulationData] = useState(null);
  const [loadingSim, setLoadingSim] = useState(false);
  const [gameActive, setGameActive] = useState(false);

  /**
   * Start a new game round
   * - Generates random board with N-2 snakes and N-2 ladders
   * - Computes minimum throws using BFS and Dijkstra
   * - Records algorithm execution times in database
   */
  const handleStart = async (n, name, vsComputer) => {
    try {
      const data = await startGame(n);
      setBoardData(data.board);
      setChoices(data.choices);
      setGameId(data.gameId);

      setPlayerName(name);
      setIsVsComputer(vsComputer);

      // Reset result state
      setPlayerGuess(null);
      setAiGuess(null);
      setCorrectAnswer(null);
      setGameResult(null);
      setOptimalPath([]);
      setShowPath(false);

      setGameState('GUESSING');
      setMessage(`Hi ${name}! Select the minimum number of dice throws to reach cell ${n * n}.`);
      setGameActive(true);
    } catch (e) {
      setMessage('Error starting game');
    }
  };

  /**
   * Run 15-round simulation for performance analysis
   */
  const handleSimulate = async () => {
    setLoadingSim(true);
    try {
      const data = await runSimulation();
      setSimulationData(data);
    } catch (e) {
      alert("Error running simulation");
    } finally {
      setLoadingSim(false);
    }
  };

  /**
   * Exit to main menu
   */
  const handleQuit = () => {
    setGameActive(false);
    setBoardData(null);
    setGameState('SETUP');
  };

  /**
   * Handle player's guess submission
   * 
   * Win/Lose/Draw Logic:
   * - WIN: Player guesses correctly
   * - LOSE: Player guesses incorrectly
   * - DRAW: Player vs AI mode AND both guess the same answer
   */
  const handleGuess = async (guess) => {
    try {
      const res = await submitGuess(gameId, playerName, guess);

      setPlayerGuess(guess);
      setCorrectAnswer(res.actual);

      // If Vs Computer mode, AI randomly selects from the 3 choices
      let aiSelectedGuess = null;
      if (isVsComputer) {
        const randomIndex = Math.floor(Math.random() * choices.length);
        aiSelectedGuess = choices[randomIndex];
        setAiGuess(aiSelectedGuess);
      }

      // Fetch optimal path for visualization
      try {
        const pathData = await getPath(gameId);
        setOptimalPath(pathData.path || []);
      } catch (e) {
        console.error("Error fetching path", e);
      }

      // Determine game result
      let result;
      if (isVsComputer) {
        // Vs Computer mode: Check for DRAW first
        if (guess === aiSelectedGuess) {
          result = 'DRAW';
        } else if (guess === res.actual) {
          result = 'WIN';
        } else {
          result = 'LOSE';
        }
      } else {
        // Solo mode: Simple WIN/LOSE
        result = guess === res.actual ? 'WIN' : 'LOSE';
      }

      setGameResult(result);
      setGameState('RESULT');

      // Set appropriate message
      if (result === 'WIN') {
        setMessage("🎉 Congratulations! You correctly identified the minimum throws!");
      } else if (result === 'LOSE') {
        setMessage(`❌ Incorrect! The correct answer was ${res.actual}.`);
      } else {
        setMessage("🤝 It's a Draw! Both you and the AI selected the same answer.");
      }

    } catch (e) {
      setMessage('Error submitting guess');
    }
  };

  return (
    <div className="app-container">
      {/* SETUP Screen - Main Menu */}
      {!gameActive && (
        <div className="menu-view fade-in">
          <div className="game-header">
            <h1>Snake and Ladder</h1>
            <p style={{ color: '#64748b', marginTop: '10px' }}>
              Algorithmic Puzzle Game - Guess the Minimum Dice Throws!
            </p>
          </div>

          <ControlPanel
            onStart={handleStart}
            onStats={() => setShowStats(true)}
            onSimulate={handleSimulate}
            loadingSim={loadingSim}
            disabled={false}
          />
        </div>
      )}

      {/* GUESSING & RESULT Screens */}
      {gameActive && (
        <div className="game-view slide-up">
          <div className="game-toolbar">
            <button className="btn-small" onClick={handleQuit}>← Exit to Menu</button>
            <h3 style={{ margin: 0, color: '#fff' }}>Player: {playerName}</h3>
            {isVsComputer && <span style={{ color: '#fbbf24' }}>🤖 Vs Computer Mode</span>}
          </div>

          {/* Status Panel */}
          <div className="status-panel glass-panel">
            <span className="status-text" style={{ flex: 1, textAlign: 'center' }}>
              {message}
            </span>
          </div>

          {/* Board and Result Display */}
          {boardData && (
            <div style={{ marginTop: '20px', display: 'flex', gap: '20px', alignItems: 'flex-start', justifyContent: 'center', width: '100%', maxWidth: '1200px' }}>

              {/* Left Panel: Snakes */}
              <div className="glass-panel" style={{
                padding: '15px',
                minWidth: '200px',
                height: '600px',
                overflowY: 'auto'
              }}>
                <h4 style={{ margin: '0 0 15px 0', color: 'var(--primary-color)', borderBottom: '1px solid #ddd', paddingBottom: '10px' }}>
                  🐍 Snakes ({boardData.snakes.length})
                </h4>
                {boardData.snakes.map((s, i) => (
                  <div key={`snake-${i}`} style={{
                    display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px', fontSize: '0.9rem'
                  }}>
                    <span style={{
                      background: '#ef4444', color: 'white', padding: '2px 8px', borderRadius: '4px', fontWeight: 'bold', minWidth: '35px', textAlign: 'center'
                    }}>{s.start}</span>
                    <span>→</span>
                    <span style={{
                      background: '#fca5a5', padding: '2px 8px', borderRadius: '4px', minWidth: '35px', textAlign: 'center'
                    }}>{s.end}</span>
                  </div>
                ))}
              </div>

              {/* Center: Board & Guess/Results */}
              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', flex: 1, minWidth: '600px' }}>
                {/* Static Board Visualization */}
                <Board3D
                  size={boardData.size}
                  snakes={boardData.snakes}
                  ladders={boardData.ladders}
                  playerPos={1}
                  computerPos={null}
                />

                {/* GUESSING Phase - Inline Choices Below Board */}
                {gameState === 'GUESSING' && (
                  <div style={{
                    marginTop: '25px',
                    padding: '30px',
                    background: 'linear-gradient(135deg, #ffffff 0%, #f8fafc 100%)',
                    borderRadius: '20px',
                    boxShadow: '0 10px 40px rgba(0,0,0,0.15)',
                    textAlign: 'center',
                    maxWidth: '550px',
                    width: '100%'
                  }}>
                    <h3 style={{ margin: '0 0 10px 0', color: '#0f172a', fontSize: '1.4rem' }}>
                      🎯 Guess the Minimum Dice Throws
                    </h3>
                    <p style={{ margin: '0 0 20px 0', color: '#64748b', fontSize: '0.95rem' }}>
                      Select the minimum throws needed to reach cell {boardData.size * boardData.size}:
                    </p>
                    <div style={{ display: 'flex', gap: '16px', justifyContent: 'center', flexWrap: 'wrap' }}>
                      {choices.map((choice, idx) => (
                        <button
                          key={idx}
                          onClick={() => handleGuess(choice)}
                          style={{
                            padding: '20px 40px',
                            fontSize: '2rem',
                            fontWeight: '700',
                            background: 'linear-gradient(135deg, #0284c7 0%, #0369a1 100%)',
                            color: 'white',
                            border: 'none',
                            borderRadius: '16px',
                            cursor: 'pointer',
                            transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                            boxShadow: '0 4px 16px rgba(2, 132, 199, 0.3)',
                            minWidth: '100px'
                          }}
                          onMouseEnter={(e) => {
                            e.target.style.transform = 'translateY(-4px) scale(1.05)';
                            e.target.style.boxShadow = '0 12px 24px rgba(2, 132, 199, 0.4)';
                          }}
                          onMouseLeave={(e) => {
                            e.target.style.transform = 'translateY(0) scale(1)';
                            e.target.style.boxShadow = '0 4px 16px rgba(2, 132, 199, 0.3)';
                          }}
                        >
                          {choice}
                        </button>
                      ))}
                    </div>
                  </div>
                )}

                {/* RESULT Phase - Show Results */}
                {gameState === 'RESULT' && (
                  <div style={{
                    marginTop: '20px',
                    padding: '30px',
                    background: 'rgba(255,255,255,0.95)',
                    borderRadius: '20px',
                    boxShadow: '0 10px 40px rgba(0,0,0,0.15)',
                    textAlign: 'center',
                    maxWidth: '500px',
                    width: '100%'
                  }}>
                    {/* Result Badge */}
                    <div style={{
                      fontSize: '3rem',
                      marginBottom: '20px'
                    }}>
                      {gameResult === 'WIN' && '🏆'}
                      {gameResult === 'LOSE' && '😢'}
                      {gameResult === 'DRAW' && '🤝'}
                    </div>

                    <h2 style={{
                      margin: '0 0 20px 0',
                      color: gameResult === 'WIN' ? '#16a34a' : gameResult === 'LOSE' ? '#dc2626' : '#f59e0b',
                      fontSize: '2rem'
                    }}>
                      {gameResult === 'WIN' && 'YOU WIN!'}
                      {gameResult === 'LOSE' && 'YOU LOSE!'}
                      {gameResult === 'DRAW' && 'DRAW!'}
                    </h2>

                    {/* Answer Summary */}
                    <div style={{ display: 'flex', justifyContent: 'center', gap: '30px', marginBottom: '20px' }}>
                      <div>
                        <div style={{ fontSize: '0.9rem', color: '#64748b', marginBottom: '5px' }}>Your Guess</div>
                        <div style={{
                          fontSize: '2rem',
                          fontWeight: '700',
                          color: playerGuess === correctAnswer ? '#16a34a' : '#dc2626'
                        }}>{playerGuess}</div>
                      </div>

                      <div>
                        <div style={{ fontSize: '0.9rem', color: '#64748b', marginBottom: '5px' }}>Correct Answer</div>
                        <div style={{
                          fontSize: '2rem',
                          fontWeight: '700',
                          color: '#0284c7'
                        }}>{correctAnswer}</div>
                      </div>

                      {isVsComputer && (
                        <div>
                          <div style={{ fontSize: '0.9rem', color: '#64748b', marginBottom: '5px' }}>AI Guess</div>
                          <div style={{
                            fontSize: '2rem',
                            fontWeight: '700',
                            color: aiGuess === correctAnswer ? '#16a34a' : '#dc2626'
                          }}>{aiGuess}</div>
                        </div>
                      )}
                    </div>

                    {/* View Path Button */}
                    <button
                      onClick={() => setShowPath(!showPath)}
                      style={{
                        padding: '10px 24px',
                        background: showPath ? '#64748b' : '#0ea5e9',
                        color: 'white',
                        border: 'none',
                        borderRadius: '12px',
                        fontSize: '0.95rem',
                        fontWeight: '600',
                        cursor: 'pointer',
                        marginBottom: '15px'
                      }}
                    >
                      {showPath ? '✕ Hide Path' : '🗺️ View Optimal Path'}
                    </button>

                    {/* Optimal Path Visualization */}
                    {showPath && optimalPath.length > 0 && (
                      <div style={{
                        padding: '15px',
                        background: '#f8fafc',
                        borderRadius: '12px',
                        marginBottom: '20px'
                      }}>
                        <h4 style={{ margin: '0 0 10px 0', fontSize: '0.9rem', color: '#475569' }}>
                          Shortest Path ({optimalPath.length - 1} throws)
                        </h4>
                        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '6px', justifyContent: 'center' }}>
                          {optimalPath.map((step, i) => (
                            <div key={i} style={{ display: 'flex', alignItems: 'center', gap: '4px' }}>
                              <span style={{
                                padding: '4px 10px',
                                background: i === 0 ? '#22c55e' : (i === optimalPath.length - 1 ? '#dc2626' : '#3b82f6'),
                                color: 'white',
                                borderRadius: '6px',
                                fontWeight: '600',
                                fontSize: '0.85rem'
                              }}>
                                {step.cell}
                              </span>
                              {i < optimalPath.length - 1 && (
                                <span style={{ fontSize: '0.75rem', color: '#94a3b8' }}>
                                  🎲{optimalPath[i + 1].diceRoll}→
                                </span>
                              )}
                            </div>
                          ))}
                        </div>
                      </div>
                    )}

                    {/* Play Again Button */}
                    <button
                      className="btn-primary"
                      onClick={handleQuit}
                      style={{ fontSize: '1.1rem', padding: '15px 40px' }}
                    >
                      Play Again
                    </button>
                  </div>
                )}
              </div>

              {/* Right Panel: Ladders */}
              <div className="glass-panel" style={{
                padding: '15px',
                minWidth: '200px',
                height: '600px',
                overflowY: 'auto'
              }}>
                <h4 style={{ margin: '0 0 15px 0', color: 'var(--primary-color)', borderBottom: '1px solid #ddd', paddingBottom: '10px' }}>
                  🪜 Ladders ({boardData.ladders.length})
                </h4>
                {boardData.ladders.map((l, i) => (
                  <div key={`ladder-${i}`} style={{
                    display: 'flex', alignItems: 'center', gap: '8px', marginBottom: '8px', fontSize: '0.9rem'
                  }}>
                    <span style={{
                      background: '#22c55e', color: 'white', padding: '2px 8px', borderRadius: '4px', fontWeight: 'bold', minWidth: '35px', textAlign: 'center'
                    }}>{l.start}</span>
                    <span>→</span>
                    <span style={{
                      background: '#86efac', padding: '2px 8px', borderRadius: '4px', minWidth: '35px', textAlign: 'center'
                    }}>{l.end}</span>
                  </div>
                ))}
              </div>

            </div>
          )}
        </div>
      )}

      {showStats && <StatsModal onClose={() => setShowStats(false)} />}

      {simulationData && (
        <SimulationReport
          data={simulationData}
          onClose={() => setSimulationData(null)}
        />
      )}
    </div>
  );
}

export default SnakeLadder;
