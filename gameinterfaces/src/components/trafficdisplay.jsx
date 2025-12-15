import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import axiosInstance from '../api/axiosConfig';
import PlayerRegistration from '../components/PlayerRegistration';
import TrafficNetwork from '../components/TrafficNetwork';
import GamePlay from '../components/GamePlay';
import GameResult from '../components/GameResult';
import Leaderboard from '../components/Leaderboard';
import { FaHome, FaGamepad, FaTrophy, FaInfo } from 'react-icons/fa';

const GameDisplay = () => {
  const [gameState, setGameState] = useState('PLAYER_REGISTRATION'); // PLAYER_REGISTRATION, MENU, PLAYING, RESULT
  const [currentPlayer, setCurrentPlayer] = useState(null);
  const [currentRound, setCurrentRound] = useState(null);
  const [roadCapacities, setRoadCapacities] = useState([]);
  const [gameResult, setGameResult] = useState(null);
  const [loading, setLoading] = useState(false);

  // Handle player registration
  const handlePlayerRegistered = (player) => {
    setCurrentPlayer(player);
    setGameState('MENU');
  };

  // Handle game start
  const handleStartGame = async () => {
    console.log('handleStartGame called');
    console.log('loading state:', loading);
    console.log('currentPlayer:', currentPlayer);
    
    if (loading || !currentPlayer) {
      console.log('Preventing game start - loading:', loading, 'currentPlayer exists:', !!currentPlayer);
      return;
    }
    
    try {
      setLoading(true);
      console.log('=== STARTING GAME ===');
      console.log('Current player ID:', currentPlayer.id);
      console.log('Current player name:', currentPlayer.name);
      
      const requestPayload = {
        playerId: currentPlayer.id
      };
      console.log('Request payload:', requestPayload);
      
      const response = await axiosInstance.post('/traffic-game/start', requestPayload);

      console.log('=== RESPONSE RECEIVED ===');
      console.log('Full response:', response);
      console.log('Response status:', response.status);
      console.log('Response headers:', response.headers);
      console.log('Response data:', response.data);
      console.log('Response data type:', typeof response.data);

      // Debug the response structure
      if (!response.data) {
        console.error('ERROR: Response data is null or undefined');
        alert('Server returned empty response');
        return;
      }

      const { success, message, data } = response.data;
      console.log('Parsed response - success:', success, 'message:', message, 'data:', data);

      if (success === true && data) {
        console.log('✅ Game started successfully');
        console.log('Game round ID:', data.roundId);
        console.log('Road capacities:', data.roadCapacities);
        console.log('Number of roads:', data.roadCapacities?.length);
        
        setCurrentRound(data.roundId);
        setRoadCapacities(data.roadCapacities);
        setGameState('PLAYING');
        setGameResult(null);
        console.log('State updated - should transition to PLAYING');
      } else {
        console.error('❌ Server returned success=false');
        console.error('Message:', message);
        alert(message || 'Failed to start game. Please try again.');
      }
    } catch (err) {
      console.error('=== ERROR CAUGHT ===');
      console.error('Error object:', err);
      console.error('Error message:', err.message);
      console.error('Error response:', err.response);
      
      if (err.response) {
        console.error('Response status:', err.response.status);
        console.error('Response data:', err.response.data);
        console.error('Response headers:', err.response.headers);
      } else {
        console.error('No response received - network error');
      }
      
      let errorMessage = 'Error starting game: ';
      if (err.response?.data?.message) {
        errorMessage += err.response.data.message;
      } else if (err.response?.status) {
        errorMessage += `Server error (${err.response.status})`;
      } else if (err.message) {
        errorMessage += err.message;
      } else {
        errorMessage += 'Unknown error';
      }
      
      console.error('Final error message:', errorMessage);
      alert(errorMessage);
    } finally {
      setLoading(false);
      console.log('=== GAME START ATTEMPT COMPLETE ===');
    }
  };

  // Handle game result
  const handleGameResult = (result) => {
    // Update player score
    setCurrentPlayer({
      ...currentPlayer,
      totalScore: currentPlayer.totalScore + result.score
    });
    setGameResult(result);
    setGameState('RESULT');
  };

  // Handle play again
  const handlePlayAgain = () => {
    handleStartGame();
  };

  // Handle go to menu
  const handleGoToMenu = () => {
    setGameState('MENU');
    setCurrentRound(null);
    setRoadCapacities([]);
    setGameResult(null);
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-950 via-blue-950 to-black">
      {/* Animated Background */}
      <div className="fixed inset-0 overflow-hidden pointer-events-none">
        <motion.div
          animate={{ y: [0, -20, 0] }}
          transition={{ duration: 8, repeat: Infinity }}
          className="absolute -top-1/2 -right-1/2 w-full h-full bg-blue-600 opacity-10 rounded-full blur-3xl"
        />
        <motion.div
          animate={{ y: [0, 20, 0] }}
          transition={{ duration: 10, repeat: Infinity }}
          className="absolute -bottom-1/2 -left-1/2 w-full h-full bg-purple-600 opacity-10 rounded-full blur-3xl"
        />
      </div>

      {/* Content */}
      <div className="relative z-10">
        {gameState === 'PLAYER_REGISTRATION' && (
          <div className="min-h-screen flex items-center justify-center p-4">
            <PlayerRegistration
              onPlayerRegistered={handlePlayerRegistered}
              onLoading={setLoading}
            />
          </div>
        )}

        {gameState === 'MENU' && currentPlayer && (
          <div className="min-h-screen p-8">
            {/* Header */}
            <motion.div
              initial={{ opacity: 0, y: -20 }}
              animate={{ opacity: 1, y: 0 }}
              className="text-center mb-12"
            >
              <h1 className="text-5xl font-bold text-white mb-4">
                🎮 Traffic Simulation Game
              </h1>
              <p className="text-2xl text-blue-300 mb-2">
                Welcome, <span className="font-bold">{currentPlayer.name}</span>!
              </p>
              <p className="text-xl text-gray-300">
                Score: <span className="font-bold text-yellow-400">{currentPlayer.totalScore}</span>
              </p>
            </motion.div>

            {/* Menu Grid */}
            <div className="max-w-4xl mx-auto grid grid-cols-1 md:grid-cols-2 gap-8 mb-12">
              {/* Play Game Card */}
              <motion.div
                whileHover={!loading ? { scale: 1.05, y: -10 } : {}}
                whileTap={!loading ? { scale: 0.95 } : {}}
                onClick={handleStartGame}
                className={`bg-gradient-to-br from-green-900 to-emerald-900 rounded-2xl p-8 cursor-pointer shadow-2xl border-2 border-green-400 hover:border-green-300 transition-all ${
                  loading ? 'opacity-50 cursor-not-allowed' : ''
                }`}
              >
                <FaGamepad className="text-6xl text-green-400 mx-auto mb-4" />
                <h2 className="text-2xl font-bold text-white text-center mb-3">
                  Play Game
                </h2>
                <p className="text-gray-200 text-center">
                  {loading ? 'Starting game...' : 'Start a new game and test your algorithm skills'}
                </p>
              </motion.div>

              {/* Leaderboard Card */}
              <motion.div
                whileHover={{ scale: 1.05, y: -10 }}
                whileTap={{ scale: 0.95 }}
                onClick={() => setGameState('LEADERBOARD')}
                className="bg-gradient-to-br from-yellow-900 to-amber-900 rounded-2xl p-8 cursor-pointer shadow-2xl border-2 border-yellow-400 hover:border-yellow-300 transition-all"
              >
                <FaTrophy className="text-6xl text-yellow-400 mx-auto mb-4" />
                <h2 className="text-2xl font-bold text-white text-center mb-3">
                  Leaderboard
                </h2>
                <p className="text-gray-200 text-center">
                  View the top players and their scores
                </p>
              </motion.div>
            </div>

            {/* How to Play */}
            <motion.div
              initial={{ opacity: 0, y: 20 }}
              animate={{ opacity: 1, y: 0 }}
              transition={{ delay: 0.3 }}
              className="max-w-4xl mx-auto bg-gradient-to-r from-blue-900 to-purple-900 rounded-2xl p-8 border-2 border-blue-400 shadow-2xl"
            >
              <div className="flex items-center gap-3 mb-8">
                <FaInfo className="text-3xl text-blue-400" />
                <h3 className="text-2xl font-bold text-white">How to Play</h3>
              </div>
              
              <div className="space-y-6 text-white">
                {/* What You'll See */}
                <div className="bg-blue-900 bg-opacity-40 rounded-lg p-4 border-l-4 border-blue-300">
                  <h4 className="font-bold text-lg text-blue-200 mb-2">🎮 What You'll See</h4>
                  <p className="text-gray-100 leading-relaxed">
                    When you start a game, you'll see a traffic network graph with roads (edges) connecting cities (nodes).
                    Each road has a capacity that shows how many vehicles per minute can travel on it.
                    Find the maximum flow from the source city <span className="font-bold text-cyan-300">A</span> (green) to the sink city <span className="font-bold text-pink-300">T</span> (magenta).
                  </p>
                </div>

                {/* Your Goal */}
                <div className="bg-purple-900 bg-opacity-40 rounded-lg p-4 border-l-4 border-purple-300">
                  <h4 className="font-bold text-lg text-purple-200 mb-2">🎯 Your Goal</h4>
                  <p className="text-gray-100 leading-relaxed">
                    Use the road capacities to calculate the maximum number of vehicles that can flow from start (A) to end (T) at the same time.
                    Look at all possible paths and find the total capacity limit. Type your answer and submit!
                  </p>
                </div>

                {/* How We Check Your Answer */}
                <div className="bg-green-900 bg-opacity-40 rounded-lg p-4 border-l-4 border-green-300">
                  <h4 className="font-bold text-lg text-green-200 mb-2">✓ How We Check Your Answer</h4>
                  <p className="text-gray-100 leading-relaxed mb-2">
                    When you submit your answer, two smart algorithms work behind the scenes to find the correct answer:
                  </p>
                  <ul className="list-disc list-inside space-y-1 text-gray-100 ml-2">
                    <li><span className="font-bold text-blue-300">Ford-Fulkerson:</span> Uses depth-first search to find flow paths</li>
                    <li><span className="font-bold text-purple-300">Edmonds-Karp:</span> Uses breadth-first search for better performance</li>
                  </ul>
                  <p className="text-gray-100 text-sm mt-2 italic">Both must agree on the same answer for it to be correct!</p>
                </div>

                {/* Win, Draw, Loss */}
                <div className="bg-yellow-900 bg-opacity-40 rounded-lg p-4 border-l-4 border-yellow-300">
                  <h4 className="font-bold text-lg text-yellow-200 mb-2">🏆 Winning & Scoring</h4>
                  <ul className="space-y-2 text-gray-100">
                    <li>✅ <span className="font-bold text-green-400">Correct:</span> Match the algorithm's answer exactly → <span className="font-bold">150 points</span></li>
                    <li>🤝 <span className="font-bold text-blue-400">Close (±1):</span> Off by one vehicle → <span className="font-bold">75 points</span></li>
                    <li>❌ <span className="font-bold text-red-400">Wrong:</span> Doesn't match the answer → <span className="font-bold">0 points</span></li>
                  </ul>
                </div>

                {/* After You Submit */}
                <div className="bg-indigo-900 bg-opacity-40 rounded-lg p-4 border-l-4 border-indigo-300">
                  <h4 className="font-bold text-lg text-indigo-200 mb-2">⚡ After You Submit</h4>
                  <p className="text-gray-100 leading-relaxed">
                    You'll see how fast each algorithm ran (in milliseconds, microseconds, or nanoseconds), 
                    the correct answer, your answer, and your score. Then you can play again or check the leaderboard!
                  </p>
                </div>

                {/* Quick Tips */}
                <div className="bg-pink-900 bg-opacity-40 rounded-lg p-4 border-l-4 border-pink-300">
                  <h4 className="font-bold text-lg text-pink-200 mb-2">💡 Quick Tips</h4>
                  <ul className="list-disc list-inside space-y-1 text-gray-100 ml-2">
                    <li>Hover over the roads to highlight them and see their capacities</li>
                    <li>Think about bottlenecks—the narrow roads limit total flow</li>
                    <li>Multiple paths can carry flow at the same time</li>
                    <li>Play multiple rounds to beat your own score!</li>
                  </ul>
                </div>
              </div>
            </motion.div>
          </div>
        )}

        {gameState === 'PLAYING' && currentPlayer && (
          <div className="min-h-screen p-8">
            {/* Header */}
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              onClick={handleGoToMenu}
              className="mb-8 flex items-center gap-2 text-white hover:text-blue-300 transition-colors"
            >
              <FaHome className="text-xl" />
              Back to Menu
            </motion.button>

            <div className="max-w-6xl mx-auto space-y-8">
              {/* Traffic Network */}
              <TrafficNetwork
                roadCapacities={roadCapacities}
                loading={loading}
              />

              {/* Game Play */}
              <GamePlay
                roundId={currentRound}
                roadCapacities={roadCapacities}
                playerName={currentPlayer.name}
                onGameResult={handleGameResult}
                onLoading={setLoading}
              />
            </div>
          </div>
        )}

        {gameState === 'RESULT' && gameResult && currentPlayer && (
          <div className="min-h-screen p-8 flex flex-col items-center justify-center">
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              onClick={handleGoToMenu}
              className="absolute top-8 left-8 flex items-center gap-2 text-white hover:text-blue-300 transition-colors"
            >
              <FaHome className="text-xl" />
              Back to Menu
            </motion.button>

            <div className="max-w-2xl w-full">
              <GameResult
                result={gameResult}
                playerScore={currentPlayer.totalScore}
                onPlayAgain={handlePlayAgain}
                playerName={currentPlayer.name}
              />
            </div>
          </div>
        )}

        {gameState === 'LEADERBOARD' && (
          <div className="min-h-screen p-8">
            {/* Header */}
            <motion.button
              whileHover={{ scale: 1.05 }}
              whileTap={{ scale: 0.95 }}
              onClick={() => setGameState('MENU')}
              className="mb-8 flex items-center gap-2 text-white hover:text-blue-300 transition-colors"
            >
              <FaHome className="text-xl" />
              Back to Menu
            </motion.button>

            <div className="max-w-2xl mx-auto">
              <Leaderboard onLoading={setLoading} />
            </div>
          </div>
        )}
      </div>

      {/* Loading Overlay */}
      {loading && (
        <motion.div
          initial={{ opacity: 0 }}
          animate={{ opacity: 1 }}
          exit={{ opacity: 0 }}
          className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
        >
          <motion.div
            animate={{ rotate: 360 }}
            transition={{ duration: 2, repeat: Infinity, ease: "linear" }}
            className="w-16 h-16 border-4 border-blue-400 border-t-transparent rounded-full"
          />
        </motion.div>
      )}
    </div>
  );
};

export default GameDisplay;