import React, { useState, useEffect } from 'react';
import Board3D from './components/Board3D';
import ControlPanel from './components/ControlPanel';
import GuessModal from './components/GuessModal';
import StatsModal from './components/StatsModal';
import { startGame, submitGuess, runSimulation, getPath } from './services/api';
import SimulationReport from './components/SimulationReport';

function SnakeLadder() {
  const [gameState, setGameState] = useState('SETUP'); // SETUP, GUESSING, PLAYING, FINISHED
  const [boardData, setBoardData] = useState(null);
  const [choices, setChoices] = useState([]);
  const [gameId, setGameId] = useState(null);

  const [playerName, setPlayerName] = useState('');
  const [isVsComputer, setIsVsComputer] = useState(false);

  const [playerPos, setPlayerPos] = useState(1);
  const [computerPos, setComputerPos] = useState(1);
  const [currentTurn, setCurrentTurn] = useState('PLAYER'); // 'PLAYER' or 'COMPUTER'

  const [diceVal, setDiceVal] = useState(null);
  const [message, setMessage] = useState('');
  const [moves, setMoves] = useState(0);
  const [showStats, setShowStats] = useState(false);
  const [rolling, setRolling] = useState(false);
  const [simulationData, setSimulationData] = useState(null);
  const [loadingSim, setLoadingSim] = useState(false);

  const [guessedAnswer, setGuessedAnswer] = useState(null);
  const [correctAnswer, setCorrectAnswer] = useState(null);

  const [showPath, setShowPath] = useState(false);
  const [optimalPath, setOptimalPath] = useState([]);

  const [gameActive, setGameActive] = useState(false);

  // Audio effects could be added here

  const handleStart = async (n, name, vsComputer) => {
    try {
      const data = await startGame(n);
      setBoardData(data.board);
      setChoices(data.choices);
      setGameId(data.gameId);

      setPlayerName(name);
      setIsVsComputer(vsComputer);

      setPlayerPos(1);
      setComputerPos(1);
      setMoves(0);
      setCurrentTurn('PLAYER');

      setGameState('GUESSING');
      setMessage(`Hi ${name}! Guess the minimum throws to win.`);
      setDiceVal(null);
      setGameActive(true);
    } catch (e) {
      setMessage('Error starting game');
    }
  };

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

  const handleQuit = () => {
    setGameActive(false);
    setBoardData(null);
    setGameState('SETUP');
  };

  const handleGuess = async (guess) => {
    try {
      const res = await submitGuess(gameId, playerName, guess);

      // Store answers
      setGuessedAnswer(guess);
      setCorrectAnswer(res.actual);

      // Fetch optimal path
      try {
        const pathData = await getPath(gameId);
        setOptimalPath(pathData.path || []);
      } catch (e) {
        console.error("Error fetching path", e);
      }

      if (res.correct) {
        setMessage("Correct! You identified the minimum throws.");
      } else {
        setMessage(`Wrong! The correct answer was ${res.actual}.`);
      }

      // Delay slightly before starting game
      setTimeout(() => {
        setGameState('PLAYING');
        setMessage(isVsComputer ? "Your Turn! Roll the dice." : "Roll the dice to move!");
      }, 1500);

    } catch (e) {
      setMessage('Error submitting guess');
      setGameState('PLAYING');
    }
  };

  const handleRoll = () => {
    if (gameState !== 'PLAYING' || rolling || (isVsComputer && currentTurn !== 'PLAYER')) return;

    rollDice('PLAYER');
  };

  const rollDice = (who) => {
    setRolling(true);
    // Simulate roll duration
    setTimeout(() => {
      const dice = Math.floor(Math.random() * 6) + 1;
      setDiceVal(dice);

      if (who === 'PLAYER') {
        setMoves(m => m + 1);
      }

      setRolling(false);
      performMove(dice, who);
    }, 600);
  };

  // Computer AI Turn
  useEffect(() => {
    if (gameState === 'PLAYING' && isVsComputer && currentTurn === 'COMPUTER') {
      const timer = setTimeout(() => {
        setMessage("Computer is thinking...");
        setTimeout(() => {
          rollDice('COMPUTER');
        }, 1000);
      }, 1000);
      return () => clearTimeout(timer);
    }
  }, [gameState, isVsComputer, currentTurn]);

  const performMove = (dice, who) => {
    let currentPos = who === 'PLAYER' ? playerPos : computerPos;
    let next = currentPos + dice;
    const max = boardData.size * boardData.size;

    if (next > max) {
      next = currentPos; // Don't move if overshot
    } else {
      // Handle Snakes and Ladders
      let moved = true;
      while (moved) {
        moved = false;
        const snake = boardData.snakes.find(s => s.start === next);
        if (snake) {
          next = snake.end;
          moved = false; // Standard rules usually imply one jump, but recursive is fun. Let's stick to one major jump per tile? 
          // Actually standard recursive logic applies if you land on another.
          // But let's be safe to avoid infinite loops (though board generation prevents simple ones).
          // Let's do recursive checks.
          moved = true; // Check again
        }

        const ladder = boardData.ladders.find(l => l.start === next);
        if (ladder) {
          next = ladder.end;
          moved = true;
        }
        // Safety break for infinite loops (rare but possible in bad gen)
        if (next === currentPos) moved = false;
        // If we didn't change position in this iteration, stop.
        if (snake === undefined && ladder === undefined) moved = false;
      }
    }

    if (who === 'PLAYER') {
      setPlayerPos(next);
      if (next === max) {
        finishGame('WIN');
      } else {
        if (isVsComputer) {
          setCurrentTurn('COMPUTER');
          setMessage("Computer's Turn...");
        } else {
          setMessage("Roll again!");
        }
      }
    } else {
      setComputerPos(next);
      if (next === max) {
        finishGame('LOSE');
      } else {
        setCurrentTurn('PLAYER');
        setMessage("Your Turn!");
      }
    }
  };

  const finishGame = (result) => {
    setGameState('FINISHED');
    if (result === 'WIN') {
      setMessage(isVsComputer ? "Congratulations! You Beat the Computer!" : `Finished in ${moves + 1} moves!`);
    } else {
      setMessage("Game Over! The Computer Won!");
    }
  };

  return (
    <div className="app-container">
      {!gameActive && (
        <div className="menu-view fade-in">
          <div className="game-header">
            <h1>Snake and Ladder</h1>
          </div>

          <ControlPanel
            onStart={handleStart}
            onStats={() => setShowStats(true)}
            onSimulate={handleSimulate}
            loadingSim={loadingSim}
            disabled={gameState === 'GUESSING' && gameActive}
          />
        </div>
      )}

      {gameActive && (
        <div className="game-view slide-up">
          <div className="game-toolbar">
            <button className="btn-small" onClick={handleQuit}>← Exit to Menu</button>
            <h3 style={{ margin: 0, color: '#fff' }}>Playing as: {playerName}</h3>
          </div>

          <div className="status-panel glass-panel">
            <span className="status-text">
              {gameState === 'PLAYING' && isVsComputer && (
                <>Turn: <span style={{ color: currentTurn === 'PLAYER' ? 'lime' : 'orange' }}>
                  {currentTurn === 'PLAYER' ? 'YOU' : 'COMPUTER'}
                </span></>
              )}
            </span>
            <span className="status-text" style={{ flex: 1, textAlign: 'center' }}>
              {message}
            </span>
            <div className="dice-display">
              {rolling ? '...' : (diceVal || '🎲')}
            </div>
          </div>

          {gameState === 'GUESSING' && (
            <GuessModal choices={choices} onGuess={handleGuess} />
          )}

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
                  🐍 Snakes
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

              {/* Center: Board & Controls */}
              <div style={{ display: 'flex', flexDirection: 'column', alignItems: 'center', flex: 1, minWidth: '600px' }}>
                <Board3D
                  size={boardData.size}
                  snakes={boardData.snakes}
                  ladders={boardData.ladders}
                  playerPos={playerPos}
                  computerPos={isVsComputer ? computerPos : null}
                />

                {/* Answer Display */}
                {guessedAnswer !== null && gameState !== 'FINISHED' && (
                  <div style={{
                    marginTop: '15px',
                    padding: '12px 24px',
                    background: 'rgba(255,255,255,0.9)',
                    borderRadius: '12px',
                    display: 'flex',
                    gap: '30px',
                    boxShadow: '0 4px 12px rgba(0,0,0,0.1)'
                  }}>
                    <div>
                      <span style={{ fontWeight: 600, color: '#64748b', fontSize: '0.85rem' }}>Your Guess: </span>
                      <span style={{
                        fontWeight: 700,
                        fontSize: '1.1rem',
                        color: guessedAnswer === correctAnswer ? '#16a34a' : '#dc2626'
                      }}>{guessedAnswer}</span>
                    </div>
                    <div>
                      <span style={{ fontWeight: 600, color: '#64748b', fontSize: '0.85rem' }}>Correct Answer: </span>
                      <span style={{ fontWeight: 700, fontSize: '1.1rem', color: '#0284c7' }}>{correctAnswer}</span>
                    </div>
                  </div>
                )}

                {/* View Path Button & Visualization */}
                {gameState === 'PLAYING' && (
                  <div style={{ marginTop: '15px', width: '100%', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
                    <button
                      onClick={async () => {
                        if (optimalPath.length === 0) {
                          try {
                            const pathData = await getPath(gameId);
                            setOptimalPath(pathData.path || []);
                            setShowPath(true);
                          } catch (e) {
                            console.error("Error fetching path", e);
                            alert("Could not load path. Please try again.");
                          }
                        } else {
                          setShowPath(!showPath);
                        }
                      }}
                      style={{
                        padding: '10px 24px',
                        background: showPath ? '#64748b' : '#0ea5e9',
                        color: 'white',
                        border: 'none',
                        borderRadius: '12px',
                        fontSize: '0.95rem',
                        fontWeight: '600',
                        cursor: 'pointer',
                        transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                        boxShadow: '0 4px 12px rgba(14, 165, 233, 0.3)',
                        display: 'flex',
                        alignItems: 'center',
                        gap: '8px'
                      }}
                    >
                      {showPath ? '✕ Hide Path' : '🗺️ View Optimal Path'}
                    </button>

                    {showPath && optimalPath.length > 0 && (
                      <div style={{
                        marginTop: '16px',
                        padding: '20px',
                        background: 'rgba(255,255,255,0.95)',
                        borderRadius: '16px',
                        maxWidth: '550px',
                        width: '100%',
                        boxShadow: '0 10px 25px rgba(0,0,0,0.1)',
                        border: '1px solid rgba(255,255,255,0.5)',
                        animation: 'slideUp 0.3s ease-out'
                      }}>
                        <h4 style={{ margin: '0 0 16px 0', color: '#0f172a', fontSize: '1rem', borderBottom: '1px solid #e2e8f0', paddingBottom: '8px' }}>
                          Shortest Path ({optimalPath.length - 1} throws)
                        </h4>
                        <div style={{ display: 'flex', flexWrap: 'wrap', gap: '8px', justifyContent: 'center' }}>
                          {optimalPath.map((step, i) => (
                            <div key={i} style={{
                              display: 'flex',
                              alignItems: 'center',
                              gap: '6px'
                            }}>
                              <span style={{
                                padding: '6px 14px',
                                background: i === 0 ? '#22c55e' : (i === optimalPath.length - 1 ? '#dc2626' : '#3b82f6'),
                                color: 'white',
                                borderRadius: '8px',
                                fontWeight: '700',
                                fontSize: '0.9rem',
                                boxShadow: '0 2px 4px rgba(0,0,0,0.1)'
                              }}>
                                {step.cell}
                              </span>
                              {i < optimalPath.length - 1 && (
                                <span style={{
                                  fontSize: '0.8rem',
                                  color: '#64748b',
                                  fontWeight: '600',
                                  background: '#f1f5f9',
                                  padding: '2px 6px',
                                  borderRadius: '4px'
                                }}>
                                  🎲{optimalPath[i + 1].diceRoll}→
                                </span>
                              )}
                            </div>
                          ))}
                        </div>
                      </div>
                    )}
                  </div>
                )}

                {gameState === 'PLAYING' && (
                  <div style={{ marginTop: '20px' }}>
                    <button
                      className="btn-primary"
                      onClick={handleRoll}
                      disabled={rolling || (isVsComputer && currentTurn !== 'PLAYER')}
                      style={{ fontSize: '1.2rem', padding: '15px 60px', opacity: (isVsComputer && currentTurn !== 'PLAYER') ? 0.5 : 1 }}
                    >
                      {rolling ? 'Rolling...' : (isVsComputer && currentTurn === 'COMPUTER' ? 'Waiting...' : 'Roll Dice')}
                    </button>
                  </div>
                )}

                {gameState === 'FINISHED' && (
                  <div style={{ marginTop: '20px', textAlign: 'center' }}>
                    <h2 style={{ color: 'var(--primary-color)', fontSize: '2rem' }}>{message}</h2>
                    <button className="btn-secondary" onClick={handleQuit}>Play Again</button>
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
                  🪜 Ladders
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
