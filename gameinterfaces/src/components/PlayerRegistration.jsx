import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { FaUser, FaGamepad } from 'react-icons/fa';
import axios from 'axios';

const PlayerRegistration = ({ onPlayerRegistered, onLoading }) => {
  const [playerName, setPlayerName] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleRegister = async (e) => {
    e.preventDefault();
    setError('');

    if (!playerName.trim()) {
      setError('Please enter a valid player name');
      return;
    }

    if (playerName.trim().length < 2) {
      setError('Player name must be at least 2 characters');
      return;
    }

    if (playerName.trim().length > 30) {
      setError('Player name must be less than 30 characters');
      return;
    }

    try {
      setLoading(true);
      onLoading(true);

      console.log('Registering player:', playerName.trim());
      const response = await axios.post('/api/traffic-game/register', {
        playerName: playerName.trim()
      });

      console.log('Registration response:', response.data);

      if (response.data && response.data.success === true) {
        const player = response.data.data;
        console.log('Player registered successfully:', player);
        onPlayerRegistered(player);
        setPlayerName('');
      } else {
        console.log('Registration failed - success flag not true:', response.data);
        setError(response.data?.message || 'Failed to register player');
      }
    } catch (err) {
      console.error('Registration error:', err);
      console.error('Error response:', err.response?.data);
      console.error('Error message:', err.message);
      setError(err.response?.data?.message || err.message || 'Error registering player. Please try again.');
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
      className="w-full max-w-md mx-auto bg-gradient-to-br from-purple-900 via-blue-900 to-black rounded-2xl p-8 shadow-2xl border-2 border-purple-400"
    >
      <div className="flex items-center justify-center gap-3 mb-8">
        <FaGamepad className="text-4xl text-purple-400" />
        <h1 className="text-3xl font-bold text-white">Traffic Sim</h1>
      </div>

      <p className="text-center text-gray-300 mb-8">Enter your name to start playing</p>

      <form onSubmit={handleRegister} className="space-y-6">
        <div className="relative">
          <div className="absolute left-4 top-4 text-gray-400">
            <FaUser className="text-xl" />
          </div>
          <input
            type="text"
            value={playerName}
            onChange={(e) => {
              setPlayerName(e.target.value);
              setError('');
            }}
            placeholder="Enter your name"
            disabled={loading}
            className="w-full bg-gray-800 text-white border-2 border-gray-600 rounded-lg py-3 pl-12 pr-4 focus:outline-none focus:border-purple-500 transition-colors disabled:opacity-50"
            maxLength="30"
          />
        </div>

        {error && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="bg-red-600 bg-opacity-20 border-l-4 border-red-500 text-red-200 p-4 rounded"
          >
            {error}
          </motion.div>
        )}

        <motion.button
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          type="submit"
          disabled={loading}
          className="w-full bg-gradient-to-r from-purple-500 to-pink-500 hover:from-purple-600 hover:to-pink-600 text-white font-bold py-3 px-4 rounded-lg text-lg transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed shadow-lg"
        >
          {loading ? 'Registering...' : 'Start Game'}
        </motion.button>
      </form>

      <p className="text-center text-gray-400 text-sm mt-6">
        Calculate the maximum flow in the traffic network from source to sink
      </p>
    </motion.div>
  );
};

export default PlayerRegistration;
