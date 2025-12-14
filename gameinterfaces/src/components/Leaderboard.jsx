import React, { useState, useEffect } from 'react';
import { motion } from 'framer-motion';
import { FaTrophy, FaUsers } from 'react-icons/fa';
import axios from 'axios';

const Leaderboard = ({ onLoading }) => {
  const [leaderboard, setLeaderboard] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');

  useEffect(() => {
    fetchLeaderboard();
  }, []);

  const fetchLeaderboard = async () => {
    try {
      onLoading(true);
      const response = await axios.get('/api/traffic-game/leaderboard');

      if (response.data.success) {
        setLeaderboard(response.data.data);
      } else {
        setError(response.data.message || 'Failed to load leaderboard');
      }
    } catch (err) {
      setError('Error loading leaderboard');
    } finally {
      setLoading(false);
      onLoading(false);
    }
  };

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6 }}
      className="w-full bg-gradient-to-br from-yellow-900 via-amber-900 to-black rounded-2xl p-8 shadow-2xl border-2 border-yellow-500"
    >
      {/* Header */}
      <div className="flex items-center gap-3 mb-8">
        <FaTrophy className="text-4xl text-yellow-400" />
        <h2 className="text-3xl font-bold text-white">Leaderboard</h2>
      </div>

      {loading ? (
        <div className="flex justify-center items-center h-96">
          <motion.div
            animate={{ rotate: 360 }}
            transition={{ duration: 2, repeat: Infinity, ease: "linear" }}
            className="w-12 h-12 border-4 border-yellow-400 border-t-transparent rounded-full"
          />
        </div>
      ) : error ? (
        <div className="bg-red-600 bg-opacity-20 border-l-4 border-red-500 text-red-200 p-4 rounded">
          {error}
        </div>
      ) : leaderboard.length === 0 ? (
        <div className="text-center py-12">
          <FaUsers className="text-6xl text-gray-400 mx-auto mb-4" />
          <p className="text-gray-400 text-lg">No players yet. Be the first to play!</p>
        </div>
      ) : (
        <div className="space-y-3">
          {leaderboard.map((player, index) => (
            <motion.div
              key={player.id}
              initial={{ opacity: 0, x: -20 }}
              animate={{ opacity: 1, x: 0 }}
              transition={{ delay: index * 0.1 }}
              className={`flex items-center justify-between p-5 rounded-lg border-2 ${
                index === 0
                  ? 'bg-gradient-to-r from-yellow-600 to-yellow-700 border-yellow-400'
                  : index === 1
                  ? 'bg-gradient-to-r from-gray-500 to-gray-600 border-gray-400'
                  : index === 2
                  ? 'bg-gradient-to-r from-orange-700 to-orange-800 border-orange-400'
                  : 'bg-gray-800 border-gray-600'
              }`}
            >
              {/* Rank and Name */}
              <div className="flex items-center gap-4">
                <motion.div
                  animate={{ scale: [1, 1.1, 1] }}
                  transition={{ duration: 0.5 }}
                  className={`text-2xl font-bold w-10 h-10 rounded-full flex items-center justify-center ${
                    index === 0 ? 'bg-yellow-300 text-black' : 'bg-white text-gray-800'
                  }`}
                >
                  #{index + 1}
                </motion.div>
                <div>
                  <p className="text-white font-bold text-lg">{player.name}</p>
                  <p className="text-gray-200 text-sm">
                    {player.gamesWon} wins / {player.gamesPlayed} games
                  </p>
                </div>
              </div>

              {/* Score */}
              <div className="text-right">
                <p className="text-white font-bold text-2xl">{player.totalScore}</p>
                <p className="text-gray-200 text-sm">points</p>
              </div>
            </motion.div>
          ))}
        </div>
      )}

      {/* Refresh Button */}
      <motion.button
        whileHover={{ scale: 1.05 }}
        whileTap={{ scale: 0.95 }}
        onClick={fetchLeaderboard}
        disabled={loading}
        className="w-full mt-8 bg-gradient-to-r from-yellow-500 to-orange-500 hover:from-yellow-600 hover:to-orange-600 text-white font-bold py-3 px-6 rounded-lg text-lg transition-all duration-300 disabled:opacity-50 shadow-lg"
      >
        Refresh 🔄
      </motion.button>
    </motion.div>
  );
};

export default Leaderboard;
