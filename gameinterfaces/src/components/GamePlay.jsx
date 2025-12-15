import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { FaCheckCircle } from 'react-icons/fa';
import axiosInstance from '../api/axiosConfig';

const GamePlay = ({ roundId, roadCapacities, playerName, onGameResult, onLoading }) => {
  const [playerAnswer, setPlayerAnswer] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!playerAnswer.trim()) {
      setError('Please enter your answer');
      return;
    }

    const answer = parseInt(playerAnswer, 10);
    if (isNaN(answer) || answer < 0) {
      setError('Please enter a valid number');
      return;
    }

    try {
      setLoading(true);
      onLoading(true);

      // Build graph data from road capacities
      const graphData = roadCapacities.map(road => ({
        from: road.from,
        to: road.to,
        capacity: road.capacity
      }));

      const response = await axiosInstance.post('/traffic-game/submit', {
        roundId: roundId,
        playerAnswer: answer,
        graphData: graphData
      });

      if (response.data.success) {
        onGameResult(response.data.data);
      } else {
        setError(response.data.message || 'Failed to submit answer');
      }
    } catch (err) {
      setError(err.response?.data?.message || 'Error submitting answer. Please try again.');
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
      className="w-full max-w-2xl mx-auto bg-gradient-to-br from-indigo-900 via-blue-900 to-black rounded-2xl p-8 shadow-2xl border-2 border-blue-400"
    >
      {/* Header */}
      <div className="mb-8">
        <h2 className="text-3xl font-bold text-white mb-2">Calculate Maximum Flow</h2>
        <p className="text-blue-300">Player: <span className="font-bold">{playerName}</span></p>
      </div>

      {/* Instructions */}
      <div className="bg-blue-900 bg-opacity-50 rounded-lg p-6 mb-8 border-l-4 border-blue-400 space-y-3">
        <p className="text-white text-lg font-semibold">
          🚗 Calculate the Maximum Flow
        </p>
        <p className="text-gray-100 leading-relaxed">
          Look at the traffic network graph above. Each road shows how many vehicles per minute it can handle.
          Your job: Find the maximum total number of vehicles that can travel from source <span className="font-bold text-cyan-300">A</span> (start) 
          to sink <span className="font-bold text-pink-300">T</span> (end) all at the same time.
        </p>
        <p className="text-gray-200 text-sm italic">
          💬 Tip: Consider all paths from A to T. Multiple routes can carry flow simultaneously, 
          but each road is limited by its capacity shown in the list below.
        </p>
      </div>

      {/* Road Capacities Display - Table Format */}
      <div className="bg-black bg-opacity-50 rounded-lg p-6 mb-8 max-h-60 overflow-y-auto border border-blue-500">
        <h3 className="text-white font-bold text-lg mb-4">Road Capacities (vehicles/minute):</h3>
        <table className="w-full text-left text-sm">
          <thead>
            <tr className="border-b-2 border-blue-400">
              <th className="text-blue-300 font-bold py-2 px-3">Road</th>
              <th className="text-blue-300 font-bold py-2 px-3 text-right">Capacity</th>
            </tr>
          </thead>
          <tbody>
            {roadCapacities && roadCapacities.map((road, idx) => (
              <tr key={idx} className="border-b border-gray-700 hover:bg-blue-900 hover:bg-opacity-30 transition-colors">
                <td className="text-white py-2 px-3">
                  <span className="font-bold text-green-400">{road.from}</span>
                  <span className="text-blue-300"> → </span>
                  <span className="font-bold text-green-400">{road.to}</span>
                </td>
                <td className="text-yellow-300 font-bold text-right py-2 px-3">{road.capacity}</td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>

      <form onSubmit={handleSubmit} className="space-y-6">
        {/* Answer Input */}
        <div>
          <label className="block text-white font-bold mb-3">Your Answer:</label>
          <div className="relative">
            <input
              type="number"
              value={playerAnswer}
              onChange={(e) => {
                setPlayerAnswer(e.target.value);
                setError('');
              }}
              placeholder="Enter maximum flow (vehicles/minute)"
              disabled={loading}
              min="0"
              className="w-full bg-gray-800 text-white border-2 border-gray-600 rounded-lg py-4 px-4 text-lg focus:outline-none focus:border-purple-500 transition-colors disabled:opacity-50"
            />
            {playerAnswer && !error && (
              <FaCheckCircle className="absolute right-4 top-5 text-green-400 text-xl" />
            )}
          </div>
        </div>

        {/* Error Message */}
        {error && (
          <motion.div
            initial={{ opacity: 0 }}
            animate={{ opacity: 1 }}
            className="bg-red-600 bg-opacity-20 border-l-4 border-red-500 text-red-200 p-4 rounded"
          >
            {error}
          </motion.div>
        )}

        {/* Submit Button */}
        <motion.button
          whileHover={{ scale: 1.05 }}
          whileTap={{ scale: 0.95 }}
          type="submit"
          disabled={loading}
          className="w-full bg-gradient-to-r from-green-500 to-emerald-500 hover:from-green-600 hover:to-emerald-600 text-white font-bold py-4 px-6 rounded-lg text-xl transition-all duration-300 disabled:opacity-50 disabled:cursor-not-allowed shadow-lg"
        >
          {loading ? 'Calculating...' : 'Submit Answer'}
        </motion.button>
      </form>
    </motion.div>
  );
};

export default GamePlay;
