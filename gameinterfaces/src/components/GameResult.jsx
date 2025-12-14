import React from 'react';
import axios from 'axios';
import { motion } from 'framer-motion';
import { FaCheckCircle, FaTimesCircle, FaTrophy } from 'react-icons/fa';

const GameResult = ({ result, playerScore, onPlayAgain, playerName }) => {
  if (!result) return null;

  return (
    <motion.div
      initial={{ opacity: 0, scale: 0.9 }}
      animate={{ opacity: 1, scale: 1 }}
      transition={{ duration: 0.5 }}
      className="w-full max-w-2xl mx-auto bg-gradient-to-br from-purple-900 via-blue-900 to-black rounded-2xl p-8 shadow-2xl border-2 border-purple-400"
    >
      {/* Result Header */}
      <div className="flex items-center justify-center mb-8">
        {result.isCorrect ? (
          <motion.div
            animate={{ rotate: [0, 10, -10, 0] }}
            transition={{ duration: 0.6 }}
          >
            <FaCheckCircle className="text-6xl text-green-400" />
          </motion.div>
        ) : (
          <motion.div
            animate={{ shake: [0, -5, 5, -5, 0] }}
            transition={{ duration: 0.6 }}
          >
            <FaTimesCircle className="text-6xl text-red-400" />
          </motion.div>
        )}
      </div>

      {/* Result Message */}
      <h2 className={`text-4xl font-bold text-center mb-4 ${result.isCorrect ? 'text-green-400' : 'text-red-400'}`}>
        {result.isCorrect ? 'Correct! 🎉' : 'Incorrect ❌'}
      </h2>

      {/* Result Details */}
      <div className="bg-black bg-opacity-50 rounded-lg p-6 mb-6 border-l-4 border-blue-500 space-y-4">
        <div>
          <p className="text-gray-400 text-sm font-semibold mb-1">Your Answer</p>
          <p className="text-white text-xl font-bold">{result.playerAnswer !== undefined && result.playerAnswer !== null ? result.playerAnswer : 'N/A'} vehicles/minute</p>
        </div>
        <div>
          <p className="text-gray-400 text-sm font-semibold mb-1">Correct Answer</p>
          <p className="text-green-300 text-xl font-bold">{result.correctAnswer !== undefined && result.correctAnswer !== null ? result.correctAnswer : 'N/A'} vehicles/minute</p>
        </div>
        
        {/* Algorithm Execution Times */}
        <div className="border-t border-gray-600 pt-4">
          <p className="text-gray-400 text-sm font-semibold mb-3">Algorithm Execution Times:</p>
          <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
            <div className="bg-blue-900 bg-opacity-30 rounded p-3 border border-blue-400">
              <p className="text-blue-300 text-sm font-semibold">Ford-Fulkerson</p>
              <p className="text-white text-lg font-bold">
                {(() => {
                  const ms = result.fordFulkersonTimeMs;
                  const us = result.fordFulkersonTimeUs;
                  const ns = result.fordFulkersonTimeNs;
                  
                  if (ms !== undefined && ms !== null) {
                    if (ms > 0) {
                      return `${ms} ms`;
                    } else if (us !== undefined && us !== null && us > 0) {
                      return `${us} µs`;
                    } else if (ns !== undefined && ns !== null && ns > 0) {
                      return `${ns} ns`;
                    }
                  }
                  return 'N/A';
                })()}
              </p>
            </div>
            <div className="bg-purple-900 bg-opacity-30 rounded p-3 border border-purple-400">
              <p className="text-purple-300 text-sm font-semibold">Edmonds-Karp</p>
              <p className="text-white text-lg font-bold">
                {(() => {
                  const ms = result.edmondsKarpTimeMs;
                  const us = result.edmondsKarpTimeUs;
                  const ns = result.edmondsKarpTimeNs;
                  
                  if (ms !== undefined && ms !== null) {
                    if (ms > 0) {
                      return `${ms} ms`;
                    } else if (us !== undefined && us !== null && us > 0) {
                      return `${us} µs`;
                    } else if (ns !== undefined && ns !== null && ns > 0) {
                      return `${ns} ns`;
                    }
                  }
                  return 'N/A';
                })()}
              </p>
            </div>
          </div>
        </div>
      </div>

      {/* Score Display */}
      <div className="bg-gradient-to-r from-yellow-500 to-orange-500 rounded-lg p-6 mb-6 text-center">
        <p className="text-black text-xl font-bold mb-2">Round Score</p>
        <motion.p
          animate={{ scale: [1, 1.2, 1] }}
          transition={{ duration: 0.8 }}
          className="text-5xl font-bold text-white"
        >
          +{result.score}
        </motion.p>
      </div>

      {/* Total Score */}
      <div className="bg-blue-800 bg-opacity-70 rounded-lg p-6 mb-6 flex items-center justify-center gap-4">
        <FaTrophy className="text-4xl text-yellow-400" />
        <div>
          <p className="text-white text-sm">Total Score</p>
          <p className="text-white text-3xl font-bold">{playerScore}</p>
        </div>
      </div>

      {/* Message */}
      <p className="text-white text-center text-lg mb-8 italic">
        {result.message}
      </p>

      {/* Play Again Button */}
      <motion.button
        whileHover={{ scale: 1.05 }}
        whileTap={{ scale: 0.95 }}
        onClick={onPlayAgain}
        className="w-full bg-gradient-to-r from-blue-500 to-purple-500 hover:from-blue-600 hover:to-purple-600 text-white font-bold py-4 px-6 rounded-lg text-xl transition-all duration-300 shadow-lg"
      >
        Play Again 🎮
      </motion.button>
    </motion.div>
  );
};

export default GameResult;
