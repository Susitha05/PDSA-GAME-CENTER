import React, { useState } from 'react';
import Board from './components/Board';
import ControlPanel from './components/ControlPanel';
import GuessModal from './components/GuessModal';
import StatsModal from './components/StatsModal';
import { startGame, submitGuess } from './services/api';

function SnakeLadder() {
  const [gameState, setGameState] = useState('SETUP'); // SETUP, GUESSING, PLAYING, FINISHED
  const [boardData, setBoardData] = useState(null);
  const [choices, setChoices] = useState([]);
  const [gameId, setGameId] = useState(null);
  const [playerName, setPlayerName] = useState('Player 1');
  const [playerPos, setPlayerPos] = useState(1);
  const [diceVal, setDiceVal] = useState(null);
  const [message, setMessage] = useState('');
  const [moves, setMoves] = useState(0);
  const [showStats, setShowStats] = useState(false);
  const [rolling, setRolling] = useState(false);

  const handleStart = async (n) => {
    try {
      const data = await startGame(n);
      setBoardData(data.board);
      setChoices(data.choices);
      setGameId(data.gameId);
      setPlayerPos(1);
      setMoves(0);
      setGameState('GUESSING');
      setMessage('');
      setDiceVal(null);
    } catch (e) {
      setMessage('Error starting game');
    }
  };

  const handleGuess = async (guess) => {
    try {
      const res = await submitGuess(gameId, playerName, guess);
      if (res.correct) {
        setMessage("Correct! You identified the minimum throws.");
      } else {
        setMessage(`Wrong! The correct answer was ${res.actual}.`);
      }
      setGameState('PLAYING');
    } catch (e) {
      setMessage('Error submitting guess');
      setGameState('PLAYING');
    }
  };

  const handleRoll = () => {
    if (gameState !== 'PLAYING' || rolling) return;

    setRolling(true);
    // Simulate roll duration
    setTimeout(() => {
      const dice = Math.floor(Math.random() * 6) + 1;
      setDiceVal(dice);
      setMoves(m => m + 1);
      setRolling(false);
      performMove(dice);
    }, 600);
  };

  const performMove = (dice) => {
    let next = playerPos + dice;
    const max = boardData.size * boardData.size;

    if (next > max) {
      next = playerPos;
    }

    let moved = true;
    while (moved) {
      moved = false;
      const snake = boardData.snakes.find(s => s.start === next);
      if (snake) {
        next = snake.end;
        moved = false; // Single bounce for safety
      }

      const ladder = boardData.ladders.find(l => l.start === next);
      if (ladder) {
        next = ladder.end;
        moved = false;
      }
    }

    setPlayerPos(next);

    if (next === max) {
      setGameState('FINISHED');
      setMessage(`Game Over! You reached the end in ${moves + 1} moves.`);
    }
  };

  return (
    <div className="app-container">
      <div className="game-header">
        <h1>Snake & Ladder</h1>
        <p style={{ color: '#aaa', margin: 0 }}>Advanced Agentic Problem</p>
      </div>

      <ControlPanel
        onStart={handleStart}
        onStats={() => setShowStats(true)}
        disabled={gameState === 'GUESSING'}
      />

      <div className="status-panel glass-panel">
        <span className="status-text">
          Status: <span style={{ color: 'var(--primary-color)', fontWeight: 'bold' }}>{gameState}</span>
        </span>
        <span className="status-text" style={{ flex: 1, textAlign: 'center' }}>
          {message}
        </span>
        <div className="dice-display">
          {rolling ? '...' : (diceVal || '🎲')}
        </div>
      </div>

      {showStats && <StatsModal onClose={() => setShowStats(false)} />}

      {gameState === 'GUESSING' && (
        <GuessModal choices={choices} onGuess={handleGuess} />
      )}

      {boardData && (
        <div style={{ marginTop: '20px', display: 'flex', flexDirection: 'column', alignItems: 'center' }}>
          <Board
            size={boardData.size}
            snakes={boardData.snakes}
            ladders={boardData.ladders}
            playerPos={playerPos}
          />

          {gameState === 'PLAYING' && (
            <div style={{ marginTop: '30px' }}>
              <button
                className="btn-primary"
                onClick={handleRoll}
                disabled={rolling}
                style={{ fontSize: '1.2rem', padding: '15px 40px' }}
              >
                {rolling ? 'Rolling...' : 'Roll Dice'}
              </button>
            </div>
          )}

          {gameState === 'FINISHED' && (
            <div style={{ marginTop: '30px' }}>
              <h2 style={{ color: 'var(--primary-color)' }}>Congratulations!</h2>
            </div>
          )}
        </div>
      )}
    </div>
  );
}

export default SnakeLadder;
