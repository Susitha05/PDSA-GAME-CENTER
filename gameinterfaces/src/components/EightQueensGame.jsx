import React, { useState, useEffect } from 'react';
import './EightQueensGame.css';

const BOARD_SIZE = 8;
const API_BASE_URL = 'http://localhost:8080/api/eight-queens';

const EightQueensGame = () => {
  const [board, setBoard] = useState(Array(BOARD_SIZE).fill(-1));
  const [playerName, setPlayerName] = useState('');
  const [statistics, setStatistics] = useState(null);
  const [computationResults, setComputationResults] = useState([]);
  const [message, setMessage] = useState('');
  const [loading, setLoading] = useState(false);
  const [showResults, setShowResults] = useState(false);
  const [showAnswers, setShowAnswers] = useState(false);
  const [answers, setAnswers] = useState([]);
  const [startTime, setStartTime] = useState(null);
  const [elapsedTime, setElapsedTime] = useState(0);
  const [movesCount, setMovesCount] = useState(0);
  const [showScoreboard, setShowScoreboard] = useState(false);
  const [scoreboard, setScoreboard] = useState([]);
  const [gameStarted, setGameStarted] = useState(false);

  useEffect(() => {
    fetchStatistics();
    fetchComputationResults();
  }, []);

  // Timer effect
  useEffect(() => {
    let interval;
    if (gameStarted && startTime) {
      interval = setInterval(() => {
        setElapsedTime(Math.floor((Date.now() - startTime) / 1000));
      }, 1000);
    }
    return () => clearInterval(interval);
  }, [gameStarted, startTime]);

  const fetchStatistics = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/statistics`);
      const data = await response.json();
      setStatistics(data);
    } catch (error) {
      console.error('Error fetching statistics:', error);
    }
  };

  const fetchComputationResults = async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/computations`);
      const data = await response.json();
      setComputationResults(data);
    } catch (error) {
      console.error('Error fetching computation results:', error);
    }
  };

  const handleCellClick = (row, col) => {
    // Start game on first move
    if (!gameStarted && playerName.trim()) {
      setGameStarted(true);
      setStartTime(Date.now());
    }

    const newBoard = [...board];
    if (newBoard[row] === col) {
      newBoard[row] = -1; // Remove queen
    } else {
      newBoard[row] = col; // Place queen
    }
    setBoard(newBoard);
    setMovesCount(movesCount + 1);
    setMessage('');
  };

  const isQueenAt = (row, col) => {
    return board[row] === col;
  };

  const isAttacked = (row, col) => {
    for (let r = 0; r < BOARD_SIZE; r++) {
      if (board[r] === -1) continue;
      
      // Same row (queen at row r attacks all cells in that row)
      if (r === row && board[r] !== col) return true;
      
      // Same column (queen at column board[r] attacks all cells in that column)
      if (board[r] === col && r !== row) return true;
      
      // Diagonal (both diagonals)
      if (Math.abs(r - row) === Math.abs(board[r] - col) && r !== row) return true;
    }
    return false;
  };

  const handleSubmit = async () => {
    if (!playerName.trim()) {
      setMessage('Please enter your name!');
      return;
    }

    if (!gameStarted) {
      setMessage('Please start playing by placing queens on the board!');
      return;
    }

    const queensCount = board.filter(col => col !== -1).length;
    if (queensCount !== BOARD_SIZE) {
      setMessage(`You must place exactly ${BOARD_SIZE} queens! Currently placed: ${queensCount}`);
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/submit`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify({
          name: playerName,
          board: board,
          timeTakenSeconds: elapsedTime,
          movesCount: movesCount,
        }),
      });

      const data = await response.json();
      
      if (response.ok) {
        if (data.alreadyFound) {
          setMessage(`✓ Correct solution! But this solution was already found. Try another one! (${data.totalFoundSolutions}/${data.totalPossibleSolutions} found)`);
        } else {
          setMessage(`🎉 Congratulations ${playerName}! You found a new solution! (${data.totalFoundSolutions}/${data.totalPossibleSolutions} found)`);
        }
        fetchStatistics();
      } else {
        setMessage(`❌ ${data.message || 'Invalid solution! Queens are attacking each other.'}`);
      }
    } catch (error) {
      setMessage('Error submitting solution. Please try again.');
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const handleReset = () => {
    setBoard(Array(BOARD_SIZE).fill(-1));
    setMessage('');
    setGameStarted(false);
    setStartTime(null);
    setElapsedTime(0);
    setMovesCount(0);
  };

  const runSequentialComputation = async () => {
    setLoading(true);
    setMessage('Computing all solutions sequentially...');
    try {
      const response = await fetch(`${API_BASE_URL}/compute/sequential`, {
        method: 'POST',
      });
      const data = await response.json();
      setMessage(`Sequential computation completed! Found ${data.totalSolutions} solutions in ${data.timeTakenMs}ms`);
      fetchStatistics();
      fetchComputationResults();
    } catch (error) {
      setMessage('Error running sequential computation.');
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const runThreadedComputation = async () => {
    setLoading(true);
    setMessage('Computing all solutions using threads...');
    try {
      const response = await fetch(`${API_BASE_URL}/compute/threaded`, {
        method: 'POST',
      });
      const data = await response.json();
      setMessage(`Threaded computation completed! Found ${data.totalSolutions} solutions in ${data.timeTakenMs}ms`);
      fetchStatistics();
      fetchComputationResults();
    } catch (error) {
      setMessage('Error running threaded computation.');
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const compareResults = () => {
    if (computationResults.length < 2) {
      setMessage('Please run both sequential and threaded computations first to compare.');
      return;
    }

    const sequential = computationResults.find(r => r.computationType === 'SEQUENTIAL');
    const threaded = computationResults.find(r => r.computationType === 'THREADED');

    if (sequential && threaded) {
      const speedup = (sequential.timeTakenMs / threaded.timeTakenMs).toFixed(2);
      setMessage(
        `⚡ Performance Comparison:\n` +
        `Sequential: ${sequential.timeTakenMs}ms\n` +
        `Threaded: ${threaded.timeTakenMs}ms\n` +
        `Speedup: ${speedup}x faster with threading!`
      );
      setShowResults(true);
    }
  };

  const fetchAllAnswers = async () => {
    // Toggle: if already showing, hide it
    if (showAnswers) {
      setShowAnswers(false);
      setMessage('');
      return;
    }

    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/answers`);
      const data = await response.json();
      setAnswers(data);
      setShowAnswers(true);
      setMessage(`📋 Showing all ${data.length} solutions!`);
    } catch (error) {
      setMessage('Error fetching answers. Please run the algorithm first.');
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const displayAnswer = (answer) => {
    setBoard(answer.board);
    setMessage(`Solution #${answer.solutionNumber} (${answer.computationType})`);
    setGameStarted(false);
    setStartTime(null);
    setElapsedTime(0);
    setMovesCount(0);
  };

  const fetchScoreboard = async () => {
    setLoading(true);
    try {
      const response = await fetch(`${API_BASE_URL}/scoreboard`);
      const data = await response.json();
      setScoreboard(data);
      setShowScoreboard(true);
    } catch (error) {
      setMessage('Error fetching scoreboard.');
      console.error('Error:', error);
    } finally {
      setLoading(false);
    }
  };

  const formatTime = (seconds) => {
    const mins = Math.floor(seconds / 60);
    const secs = seconds % 60;
    return `${mins}:${secs.toString().padStart(2, '0')}`;
  };

  return (
    <div className="game-container">
      <h1 className="game-title">Eight Queens Puzzle</h1>
      
      <div className="game-content">
        <div className="board-section">
          <div className="game-stats">
            <div className="stat-box">
              <span className="stat-label">Queens:</span>
              <span className="stat-value">{board.filter(col => col !== -1).length}/8</span>
            </div>
            <div className="stat-box">
              <span className="stat-label">Time:</span>
              <span className="stat-value">{formatTime(elapsedTime)}</span>
            </div>
            <div className="stat-box">
              <span className="stat-label">Moves:</span>
              <span className="stat-value">{movesCount}</span>
            </div>
          </div>

          <div className="chessboard-container">
            <div className="chessboard">
              <div className="row-labels">
                {Array.from({ length: BOARD_SIZE }, (_, i) => (
                  <div key={i} className="row-label">{8 - i}</div>
                ))}
              </div>
              <div className="board-with-col-labels">
                <div className="board-grid">
                  {Array.from({ length: BOARD_SIZE }, (_, row) => (
                    <div key={row} className="board-row">
                {Array.from({ length: BOARD_SIZE }, (_, col) => (
                  <div
                    key={`${row}-${col}`}
                    className={`cell ${(row + col) % 2 === 0 ? 'white' : 'black'} 
                      ${isQueenAt(row, col) ? 'has-queen' : ''} 
                      ${isAttacked(row, col) && !isQueenAt(row, col) ? 'attacked' : ''}`}
                    onClick={() => handleCellClick(row, col)}
                  >
                    {isQueenAt(row, col) && <span className="queen">♛</span>}
                  </div>
                    ))}
                  </div>
                ))}
                </div>
                <div className="col-labels">
                  {['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'].map((label, i) => (
                    <div key={i} className="col-label">{label}</div>
                  ))}
                </div>
              </div>
            </div>
          </div>

          <div className="controls">
            <input
              type="text"
              placeholder="Enter your name"
              value={playerName}
              onChange={(e) => setPlayerName(e.target.value)}
              className="name-input"
              disabled={gameStarted}
            />
            <div className="button-group">
              <button onClick={handleSubmit} disabled={loading} className="btn btn-primary">
                {loading ? 'Submitting...' : 'Submit Solution'}
              </button>
              <button onClick={handleReset} className="btn btn-secondary">
                Reset Board
              </button>
              <button onClick={fetchScoreboard} className="btn btn-scoreboard">
                🏆 View Scoreboard
              </button>
            </div>
          </div>

          {message && (
            <div className={`message ${message.includes('❌') ? 'error' : 'success'}`}>
              {message.split('\n').map((line, i) => (
                <div key={i}>{line}</div>
              ))}
            </div>
          )}
        </div>

        <div className="info-section">
          <div className="statistics-card">
            <h2>Game Statistics</h2>
            {statistics && (
              <div className="stats-content">
                <div className="stat-item">
                  <span className="stat-label">Total Submissions:</span>
                  <span className="stat-value">{statistics.totalSubmissions}</span>
                </div>
                <div className="stat-item">
                  <span className="stat-label">Unique Solutions Found:</span>
                  <span className="stat-value">{statistics.uniqueSolutionsFound}</span>
                </div>
                <div className="stat-item">
                  <span className="stat-label">Total Possible Solutions:</span>
                  <span className="stat-value">{statistics.totalPossibleSolutions}</span>
                </div>
                <div className="progress-bar">
                  <div
                    className="progress-fill"
                    style={{
                      width: `${(statistics.uniqueSolutionsFound / statistics.totalPossibleSolutions) * 100}%`,
                    }}
                  ></div>
                </div>
              </div>
            )}
          </div>

          <div className="computation-card">
            <h2>Algorithm Performance</h2>
            <div className="button-group">
              <button onClick={runSequentialComputation} disabled={loading} className="btn btn-compute">
                Run Sequential
              </button>
              <button onClick={runThreadedComputation} disabled={loading} className="btn btn-compute">
                Run Threaded
              </button>
              <button onClick={compareResults} className="btn btn-compare">
                Compare Results
              </button>
              <button onClick={fetchAllAnswers} disabled={loading} className="btn btn-answers">
                {showAnswers ? 'Hide Solutions' : 'Show All 92 Solutions'}
              </button>
            </div>

            {showResults && computationResults.length > 0 && (
              <div className="computation-results">
                <h3>Computation Results</h3>
                {computationResults.slice(-2).map((result, index) => (
                  <div key={index} className="result-item">
                    <strong>{result.computationType}:</strong>
                    <br />
                    Solutions: {result.totalSolutions}, Time: {result.timeTakenMs}ms
                  </div>
                ))}
              </div>
            )}

            {showAnswers && answers.length > 0 && (
              <div className="answers-list">
                <h3>All 92 Solutions</h3>
                <div className="answers-grid">
                  {answers.map((answer, index) => (
                    <button
                      key={index}
                      onClick={() => displayAnswer(answer)}
                      className="answer-button"
                    >
                      #{answer.solutionNumber}
                    </button>
                  ))}
                </div>
              </div>
            )}
          </div>

          <div className="instructions-card">
            <h2>How to Play</h2>
            <ol>
              <li>Enter your name first</li>
              <li>Click on cells to place queens</li>
              <li>Timer starts on your first move</li>
              <li>Place exactly 8 queens</li>
              <li>No two queens should attack each other</li>
              <li>Submit to see your score!</li>
            </ol>
          </div>
        </div>
      </div>

      {/* Scoreboard Modal */}
      {showScoreboard && (
        <div className="modal-overlay" onClick={() => setShowScoreboard(false)}>
          <div className="modal-content" onClick={(e) => e.stopPropagation()}>
            <div className="modal-header">
              <h2>🏆 Scoreboard - Top Players</h2>
              <button className="modal-close" onClick={() => setShowScoreboard(false)}>×</button>
            </div>
            <div className="scoreboard-table">
              <table>
                <thead>
                  <tr>
                    <th>Rank</th>
                    <th>Player</th>
                    <th>Score</th>
                    <th>Time</th>
                    <th>Moves</th>
                  </tr>
                </thead>
                <tbody>
                  {scoreboard.map((entry, index) => (
                    <tr key={index} className={index < 3 ? 'top-three' : ''}>
                      <td className="rank">
                        {entry.rank === 1 && '🥇'}
                        {entry.rank === 2 && '🥈'}
                        {entry.rank === 3 && '🥉'}
                        {entry.rank > 3 && entry.rank}
                      </td>
                      <td className="player-name">{entry.name}</td>
                      <td className="score">{entry.score}</td>
                      <td>{formatTime(entry.timeTakenSeconds)}</td>
                      <td>{entry.movesCount}</td>
                    </tr>
                  ))}
                </tbody>
              </table>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default EightQueensGame;
