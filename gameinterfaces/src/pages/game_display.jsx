import React, { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { 
  FaChessBoard, 
  FaRoute, 
  FaDiceD6, 
  FaCrown, 
  FaTrafficLight,
  FaStar,
  FaFire,
  FaTrophy,
  FaClock,
  FaUsers,
  FaChartLine,
  FaGamepad,
  FaPlay,
  FaInfoCircle,
  FaHeart,
  FaSearch,
  FaSlidersH,
  FaMedal,
  FaRocket
} from 'react-icons/fa';

function GameDisplay() {
  const navigate = useNavigate();
  const [selectedCategory, setSelectedCategory] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');
  const [favorites, setFavorites] = useState([]);
  const [hoveredGame, setHoveredGame] = useState(null);
  const [showStats, setShowStats] = useState(false);
  const [animatedStats, setAnimatedStats] = useState({
    totalPlayers: 0,
    gamesPlayed: 0,
    achievements: 0
  });

  const games = [
    {
      id: 1,
      name: 'Tower of Hanoi',
      category: 'puzzle',
      difficulty: 'Medium',
      icon: FaDiceD6,
      path: '/towerOfHanoi',
      gradient: 'from-blue-500 via-purple-500 to-pink-500',
      bgGradient: 'from-blue-900 via-purple-900 to-pink-900',
      description: 'Classic mathematical puzzle game. Move disks between pegs following specific rules.',
      players: '2.5K+',
      rating: 4.8,
      playTime: '10-15 min',
      tags: ['Logic', 'Strategy', 'Math'],
      achievements: 12,
      thumbnail: '🗼',
      featured: true,
      difficulty_level: 3,
      status: 'Active'
    },
    {
      id: 2,
      name: 'Snake and Ladder',
      category: 'board',
      difficulty: 'Easy',
      icon: FaCrown,
      path: '/snake-ladder',
      gradient: 'from-green-500 via-emerald-500 to-teal-500',
      bgGradient: 'from-green-900 via-emerald-900 to-teal-900',
      description: 'Traditional board game of luck and strategy. Roll the dice and reach 100 first!',
      players: '5.2K+',
      rating: 4.6,
      playTime: '15-20 min',
      tags: ['Classic', 'Multiplayer', 'Fun'],
      achievements: 8,
      thumbnail: '🐍',
      featured: true,
      difficulty_level: 1,
      status: 'Active'
    },
    {
      id: 3,
      name: 'Traveling Salesman',
      category: 'puzzle',
      difficulty: 'Hard',
      icon: FaRoute,
      path: '/tsp',
      gradient: 'from-orange-500 via-red-500 to-pink-500',
      bgGradient: 'from-orange-900 via-red-900 to-pink-900',
      description: 'Find the shortest route visiting all cities. Test your optimization skills!',
      players: '1.8K+',
      rating: 4.9,
      playTime: '20-30 min',
      tags: ['Algorithm', 'Optimization', 'Challenge'],
      achievements: 15,
      thumbnail: '🗺️',
      featured: false,
      difficulty_level: 5,
      status: 'Active'
    },
    {
      id: 4,
      name: 'Eight Queens Puzzle',
      category: 'puzzle',
      difficulty: 'Hard',
      icon: FaChessBoard,
      path: '/eight-queens',
      gradient: 'from-indigo-500 via-purple-500 to-pink-500',
      bgGradient: 'from-indigo-900 via-purple-900 to-pink-900',
      description: 'Place 8 queens on a chessboard so none can attack each other. Classic puzzle!',
      players: '3.1K+',
      rating: 4.7,
      playTime: '10-20 min',
      tags: ['Chess', 'Logic', 'Strategy'],
      achievements: 10,
      thumbnail: '♛',
      featured: true,
      difficulty_level: 4,
      status: 'Coming Soon'
    },
    {
      id: 5,
      name: 'Traffic Simulation',
      category: 'simulation',
      difficulty: 'Medium',
      icon: FaTrafficLight,
      path: '/traffic-simulation',
      gradient: 'from-yellow-500 via-orange-500 to-red-500',
      bgGradient: 'from-yellow-900 via-orange-900 to-red-900',
      description: 'Manage traffic flow and optimize city transportation. Real-time simulation!',
      players: '4.3K+',
      rating: 4.5,
      playTime: '25-40 min',
      tags: ['Simulation', 'Strategy', 'Management'],
      achievements: 20,
      thumbnail: '🚦',
      featured: false,
      difficulty_level: 3,
      status: 'Coming Soon'
    }
  ];

  const categories = [
    { id: 'all', name: 'All Games', icon: FaGamepad },
    { id: 'puzzle', name: 'Puzzles', icon: FaChessBoard },
    { id: 'board', name: 'Board Games', icon: FaCrown },
    { id: 'simulation', name: 'Simulations', icon: FaTrafficLight }
  ];

  const leaderboard = [
    { rank: 1, name: 'AlgoMaster', score: 15420, badge: '🏆' },
    { rank: 2, name: 'PuzzlePro', score: 14850, badge: '🥈' },
    { rank: 3, name: 'CodeNinja', score: 13990, badge: '🥉' },
    { rank: 4, name: 'LogicKing', score: 12750, badge: '⭐' },
    { rank: 5, name: 'StrategyQueen', score: 11920, badge: '⭐' }
  ];

  useEffect(() => {
    // Animate stats on mount
    const targets = { totalPlayers: 12500, gamesPlayed: 45230, achievements: 1847 };
    const duration = 2000;
    const steps = 60;
    const increment = duration / steps;
    
    let current = { totalPlayers: 0, gamesPlayed: 0, achievements: 0 };
    const interval = setInterval(() => {
      current = {
        totalPlayers: Math.min(current.totalPlayers + targets.totalPlayers / steps, targets.totalPlayers),
        gamesPlayed: Math.min(current.gamesPlayed + targets.gamesPlayed / steps, targets.gamesPlayed),
        achievements: Math.min(current.achievements + targets.achievements / steps, targets.achievements)
      };
      setAnimatedStats(current);
      
      if (current.totalPlayers >= targets.totalPlayers) {
        clearInterval(interval);
      }
    }, increment);

    return () => clearInterval(interval);
  }, []);

  const filteredGames = games.filter(game => {
    const matchesCategory = selectedCategory === 'all' || game.category === selectedCategory;
    const matchesSearch = game.name.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         game.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         game.tags.some(tag => tag.toLowerCase().includes(searchQuery.toLowerCase()));
    return matchesCategory && matchesSearch;
  });

  const toggleFavorite = (gameId) => {
    setFavorites(prev => 
      prev.includes(gameId) 
        ? prev.filter(id => id !== gameId)
        : [...prev, gameId]
    );
  };

  const handlePlayGame = (game) => {
    if (game.status === 'Active') {
      navigate(game.path);
    }
  };

  const getDifficultyColor = (difficulty) => {
    switch(difficulty) {
      case 'Easy': return 'text-green-400';
      case 'Medium': return 'text-yellow-400';
      case 'Hard': return 'text-red-400';
      default: return 'text-gray-400';
    }
  };

  const getDifficultyBadgeColor = (difficulty) => {
    switch(difficulty) {
      case 'Easy': return 'bg-green-500/20 text-green-400 border-green-500';
      case 'Medium': return 'bg-yellow-500/20 text-yellow-400 border-yellow-500';
      case 'Hard': return 'bg-red-500/20 text-red-400 border-red-500';
      default: return 'bg-gray-500/20 text-gray-400 border-gray-500';
    }
  };

  return (
    <div className="min-h-screen bg-gradient-to-br from-gray-900 via-blue-900 to-purple-900 text-white overflow-x-hidden">
      {/* Animated Background */}
      <div className="fixed inset-0 z-0">
        <div className="absolute inset-0 bg-[radial-gradient(circle_at_50%_50%,rgba(59,130,246,0.1),transparent_50%)] animate-pulse"></div>
        <div className="absolute top-0 -left-40 w-80 h-80 bg-purple-500 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob"></div>
        <div className="absolute top-0 -right-40 w-80 h-80 bg-blue-500 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob animation-delay-2000"></div>
        <div className="absolute -bottom-40 left-20 w-80 h-80 bg-pink-500 rounded-full mix-blend-multiply filter blur-3xl opacity-20 animate-blob animation-delay-4000"></div>
      </div>

      {/* Header */}
      <header className="relative z-10 border-b border-white/10 bg-gray-900/50 backdrop-blur-lg sticky top-0">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex items-center justify-between h-20">
            <div className="flex items-center space-x-4">
              <div className="bg-gradient-to-r from-blue-500 to-purple-600 p-3 rounded-xl">
                <FaGamepad className="text-2xl" />
              </div>
              <div>
                <h1 className="text-2xl font-bold bg-gradient-to-r from-blue-400 to-purple-400 bg-clip-text text-transparent">
                  PDSA Game Center
                </h1>
                <p className="text-xs text-gray-400">Algorithm & Data Structure Games</p>
              </div>
            </div>
            
            <nav className="hidden md:flex items-center space-x-6">
              <button className="text-gray-300 hover:text-white transition-colors flex items-center gap-2">
                <FaTrophy className="text-yellow-400" />
                <span>Achievements</span>
              </button>
              <button 
                onClick={() => setShowStats(!showStats)}
                className="text-gray-300 hover:text-white transition-colors flex items-center gap-2"
              >
                <FaChartLine className="text-blue-400" />
                <span>Stats</span>
              </button>
              <button className="text-gray-300 hover:text-white transition-colors flex items-center gap-2">
                <FaUsers className="text-green-400" />
                <span>Community</span>
              </button>
            </nav>

            <div className="flex items-center space-x-4">
              <button className="bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 px-6 py-2 rounded-lg font-semibold transition-all transform hover:scale-105">
                Sign In
              </button>
            </div>
          </div>
        </div>
      </header>

      <div className="relative z-10">
        {/* Hero Section */}
        <section className="py-20 px-4 sm:px-6 lg:px-8">
          <div className="max-w-7xl mx-auto text-center">
            <div className="inline-flex items-center gap-2 bg-blue-500/10 border border-blue-500/30 rounded-full px-6 py-2 mb-8">
              <FaRocket className="text-blue-400 animate-bounce" />
              <span className="text-sm font-semibold text-blue-300">5 Amazing Algorithm Games</span>
            </div>
            
            <h1 className="text-5xl md:text-7xl font-bold mb-6 bg-gradient-to-r from-blue-400 via-purple-400 to-pink-400 bg-clip-text text-transparent">
              Master Algorithms
              <br />
              Through Gaming
            </h1>
            
            <p className="text-xl text-gray-300 mb-12 max-w-3xl mx-auto leading-relaxed">
              Dive into the world of problem-solving with our interactive collection of classic algorithm and data structure games. 
              Learn, compete, and have fun!
            </p>

            {/* Quick Stats */}
            <div className="grid grid-cols-1 md:grid-cols-3 gap-6 max-w-4xl mx-auto mb-12">
              <div className="bg-white/5 backdrop-blur-lg border border-white/10 rounded-2xl p-6 hover:bg-white/10 transition-all">
                <FaUsers className="text-4xl text-blue-400 mb-3 mx-auto" />
                <div className="text-3xl font-bold text-white mb-1">
                  {Math.floor(animatedStats.totalPlayers).toLocaleString()}+
                </div>
                <div className="text-gray-400 text-sm">Active Players</div>
              </div>
              
              <div className="bg-white/5 backdrop-blur-lg border border-white/10 rounded-2xl p-6 hover:bg-white/10 transition-all">
                <FaGamepad className="text-4xl text-purple-400 mb-3 mx-auto" />
                <div className="text-3xl font-bold text-white mb-1">
                  {Math.floor(animatedStats.gamesPlayed).toLocaleString()}+
                </div>
                <div className="text-gray-400 text-sm">Games Played</div>
              </div>
              
              <div className="bg-white/5 backdrop-blur-lg border border-white/10 rounded-2xl p-6 hover:bg-white/10 transition-all">
                <FaMedal className="text-4xl text-yellow-400 mb-3 mx-auto" />
                <div className="text-3xl font-bold text-white mb-1">
                  {Math.floor(animatedStats.achievements).toLocaleString()}+
                </div>
                <div className="text-gray-400 text-sm">Achievements Unlocked</div>
              </div>
            </div>
          </div>
        </section>

        {/* Search and Filter Section */}
        <section className="py-8 px-4 sm:px-6 lg:px-8 bg-black/20 backdrop-blur-lg">
          <div className="max-w-7xl mx-auto">
            <div className="flex flex-col md:flex-row gap-4 items-center justify-between">
              {/* Search Bar */}
              <div className="relative w-full md:w-96">
                <FaSearch className="absolute left-4 top-1/2 transform -translate-y-1/2 text-gray-400" />
                <input
                  type="text"
                  placeholder="Search games, tags, difficulty..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  className="w-full pl-12 pr-4 py-3 bg-white/5 border border-white/10 rounded-xl text-white placeholder-gray-400 focus:outline-none focus:border-blue-500 transition-colors"
                />
              </div>

              {/* Category Filters */}
              <div className="flex gap-2 overflow-x-auto pb-2 md:pb-0">
                {categories.map(category => {
                  const Icon = category.icon;
                  return (
                    <button
                      key={category.id}
                      onClick={() => setSelectedCategory(category.id)}
                      className={`flex items-center gap-2 px-4 py-2 rounded-lg font-medium transition-all whitespace-nowrap ${
                        selectedCategory === category.id
                          ? 'bg-gradient-to-r from-blue-600 to-purple-600 text-white'
                          : 'bg-white/5 text-gray-300 hover:bg-white/10'
                      }`}
                    >
                      <Icon />
                      <span>{category.name}</span>
                    </button>
                  );
                })}
              </div>

              {/* View Toggle */}
              <div className="flex items-center gap-2">
                <FaSlidersH className="text-gray-400" />
                <span className="text-sm text-gray-400">{filteredGames.length} Games</span>
              </div>
            </div>
          </div>
        </section>

        {/* Featured Games Section */}
        <section className="py-12 px-4 sm:px-6 lg:px-8">
          <div className="max-w-7xl mx-auto">
            <div className="flex items-center justify-between mb-8">
              <div className="flex items-center gap-3">
                <FaFire className="text-3xl text-orange-500" />
                <h2 className="text-3xl font-bold">Featured Games</h2>
              </div>
              <button className="text-blue-400 hover:text-blue-300 transition-colors flex items-center gap-2">
                View All
                <span>→</span>
              </button>
            </div>

            {/* Games Grid */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
              {filteredGames.map((game) => {
                const Icon = game.icon;
                const isFavorite = favorites.includes(game.id);
                
                return (
                  <div
                    key={game.id}
                    onMouseEnter={() => setHoveredGame(game.id)}
                    onMouseLeave={() => setHoveredGame(null)}
                    className="group relative bg-white/5 backdrop-blur-lg border border-white/10 rounded-2xl overflow-hidden hover:border-white/30 transition-all duration-300 hover:transform hover:scale-105"
                  >
                    {/* Card Header with Gradient */}
                    <div className={`h-32 bg-gradient-to-br ${game.gradient} relative overflow-hidden`}>
                      <div className="absolute inset-0 bg-black/20"></div>
                      <div className="absolute inset-0 flex items-center justify-center">
                        <div className="text-6xl transform group-hover:scale-110 transition-transform">
                          {game.thumbnail}
                        </div>
                      </div>
                      
                      {/* Favorite Button */}
                      <button
                        onClick={() => toggleFavorite(game.id)}
                        className="absolute top-3 right-3 p-2 bg-black/40 backdrop-blur-sm rounded-full hover:bg-black/60 transition-colors"
                      >
                        <FaHeart className={isFavorite ? 'text-red-500' : 'text-white/60'} />
                      </button>

                      {/* Status Badge */}
                      <div className="absolute top-3 left-3">
                        <span className={`px-3 py-1 rounded-full text-xs font-semibold ${
                          game.status === 'Active' 
                            ? 'bg-green-500/20 text-green-400 border border-green-500'
                            : 'bg-orange-500/20 text-orange-400 border border-orange-500'
                        }`}>
                          {game.status}
                        </span>
                      </div>
                    </div>

                    {/* Card Content */}
                    <div className="p-6">
                      <div className="flex items-start justify-between mb-3">
                        <div>
                          <h3 className="text-xl font-bold text-white mb-1">{game.name}</h3>
                          <div className="flex items-center gap-2 text-sm">
                            <span className={`font-medium ${getDifficultyColor(game.difficulty)}`}>
                              {game.difficulty}
                            </span>
                            <span className="text-gray-500">•</span>
                            <div className="flex items-center gap-1">
                              <FaStar className="text-yellow-400 text-xs" />
                              <span className="text-gray-300">{game.rating}</span>
                            </div>
                          </div>
                        </div>
                        <Icon className="text-2xl text-blue-400" />
                      </div>

                      <p className="text-gray-400 text-sm mb-4 line-clamp-2">
                        {game.description}
                      </p>

                      {/* Tags */}
                      <div className="flex flex-wrap gap-2 mb-4">
                        {game.tags.map((tag, index) => (
                          <span
                            key={index}
                            className="px-2 py-1 bg-white/5 border border-white/10 rounded text-xs text-gray-300"
                          >
                            {tag}
                          </span>
                        ))}
                      </div>

                      {/* Stats Row */}
                      <div className="grid grid-cols-3 gap-4 mb-4 pb-4 border-b border-white/10">
                        <div className="text-center">
                          <div className="text-xs text-gray-400 mb-1">Players</div>
                          <div className="text-sm font-semibold text-white">{game.players}</div>
                        </div>
                        <div className="text-center">
                          <div className="text-xs text-gray-400 mb-1">Time</div>
                          <div className="text-sm font-semibold text-white">{game.playTime}</div>
                        </div>
                        <div className="text-center">
                          <div className="text-xs text-gray-400 mb-1">Achievements</div>
                          <div className="text-sm font-semibold text-white">{game.achievements}</div>
                        </div>
                      </div>

                      {/* Action Buttons */}
                      <div className="flex gap-2">
                        <button
                          onClick={() => handlePlayGame(game)}
                          disabled={game.status !== 'Active'}
                          className={`flex-1 py-3 rounded-lg font-semibold transition-all transform hover:scale-105 flex items-center justify-center gap-2 ${
                            game.status === 'Active'
                              ? 'bg-gradient-to-r from-blue-600 to-purple-600 hover:from-blue-700 hover:to-purple-700 text-white'
                              : 'bg-gray-600 text-gray-400 cursor-not-allowed'
                          }`}
                        >
                          <FaPlay />
                          {game.status === 'Active' ? 'Play Now' : 'Coming Soon'}
                        </button>
                        <button className="p-3 bg-white/5 hover:bg-white/10 rounded-lg transition-colors border border-white/10">
                          <FaInfoCircle className="text-blue-400" />
                        </button>
                      </div>
                    </div>

                    {/* Hover Effect Overlay */}
                    {hoveredGame === game.id && (
                      <div className="absolute inset-0 bg-gradient-to-br from-blue-500/10 to-purple-500/10 pointer-events-none"></div>
                    )}
                  </div>
                );
              })}
            </div>
          </div>
        </section>

        {/* Leaderboard Section */}
        <section className="py-12 px-4 sm:px-6 lg:px-8 bg-black/20 backdrop-blur-lg">
          <div className="max-w-7xl mx-auto">
            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
              {/* Leaderboard */}
              <div className="lg:col-span-2">
                <div className="flex items-center gap-3 mb-6">
                  <FaTrophy className="text-3xl text-yellow-500" />
                  <h2 className="text-3xl font-bold">Top Players</h2>
                </div>
                
                <div className="space-y-3">
                  {leaderboard.map((player, index) => (
                    <div
                      key={player.rank}
                      className={`flex items-center justify-between p-4 rounded-xl transition-all hover:transform hover:scale-102 ${
                        index < 3
                          ? 'bg-gradient-to-r from-yellow-500/10 to-orange-500/10 border border-yellow-500/30'
                          : 'bg-white/5 border border-white/10'
                      }`}
                    >
                      <div className="flex items-center gap-4">
                        <div className={`text-2xl font-bold ${index < 3 ? 'text-yellow-400' : 'text-gray-400'}`}>
                          {player.badge}
                        </div>
                        <div>
                          <div className="font-semibold text-white">{player.name}</div>
                          <div className="text-sm text-gray-400">Rank #{player.rank}</div>
                        </div>
                      </div>
                      <div className="text-right">
                        <div className="text-xl font-bold text-blue-400">{player.score.toLocaleString()}</div>
                        <div className="text-xs text-gray-400">points</div>
                      </div>
                    </div>
                  ))}
                </div>
              </div>

              {/* Recent Achievements */}
              <div>
                <div className="flex items-center gap-3 mb-6">
                  <FaMedal className="text-3xl text-purple-500" />
                  <h2 className="text-2xl font-bold">Recent Achievements</h2>
                </div>
                
                <div className="space-y-3">
                  {[
                    { title: 'Perfect Game', game: 'Tower of Hanoi', icon: '🎯', color: 'blue' },
                    { title: 'Speed Demon', game: 'TSP', icon: '⚡', color: 'yellow' },
                    { title: 'Queen Master', game: 'Eight Queens', icon: '👑', color: 'purple' },
                    { title: 'Lucky Roll', game: 'Snake & Ladder', icon: '🎲', color: 'green' }
                  ].map((achievement, index) => (
                    <div
                      key={index}
                      className="bg-white/5 border border-white/10 rounded-xl p-4 hover:bg-white/10 transition-all"
                    >
                      <div className="flex items-center gap-3">
                        <div className="text-2xl">{achievement.icon}</div>
                        <div className="flex-1">
                          <div className="font-semibold text-white text-sm">{achievement.title}</div>
                          <div className="text-xs text-gray-400">{achievement.game}</div>
                        </div>
                        <FaStar className={`text-${achievement.color}-400`} />
                      </div>
                    </div>
                  ))}
                </div>
              </div>
            </div>
          </div>
        </section>

        {/* Footer */}
        <footer className="py-12 px-4 sm:px-6 lg:px-8 border-t border-white/10 bg-black/30 backdrop-blur-lg">
          <div className="max-w-7xl mx-auto">
            <div className="grid grid-cols-1 md:grid-cols-4 gap-8 mb-8">
              <div className="col-span-1 md:col-span-2">
                <div className="flex items-center space-x-3 mb-4">
                  <div className="bg-gradient-to-r from-blue-500 to-purple-600 p-3 rounded-xl">
                    <FaGamepad className="text-2xl" />
                  </div>
                  <div>
                    <h3 className="text-xl font-bold">PDSA Game Center</h3>
                    <p className="text-sm text-gray-400">Learn algorithms through play</p>
                  </div>
                </div>
                <p className="text-gray-400 text-sm mb-4">
                  Master problem-solving and algorithmic thinking with our interactive game collection. 
                  Perfect for students, developers, and puzzle enthusiasts!
                </p>
                <div className="flex gap-3">
                  <button className="p-2 bg-white/5 hover:bg-white/10 rounded-lg transition-colors">
                    <FaGamepad className="text-blue-400" />
                  </button>
                  <button className="p-2 bg-white/5 hover:bg-white/10 rounded-lg transition-colors">
                    <FaTrophy className="text-yellow-400" />
                  </button>
                  <button className="p-2 bg-white/5 hover:bg-white/10 rounded-lg transition-colors">
                    <FaUsers className="text-green-400" />
                  </button>
                </div>
              </div>

              <div>
                <h4 className="font-semibold mb-4 text-white">Quick Links</h4>
                <ul className="space-y-2 text-sm text-gray-400">
                  <li><a href="#" className="hover:text-white transition-colors">All Games</a></li>
                  <li><a href="#" className="hover:text-white transition-colors">Leaderboard</a></li>
                  <li><a href="#" className="hover:text-white transition-colors">Achievements</a></li>
                  <li><a href="#" className="hover:text-white transition-colors">Community</a></li>
                </ul>
              </div>

              <div>
                <h4 className="font-semibold mb-4 text-white">Support</h4>
                <ul className="space-y-2 text-sm text-gray-400">
                  <li><a href="#" className="hover:text-white transition-colors">Help Center</a></li>
                  <li><a href="#" className="hover:text-white transition-colors">Game Rules</a></li>
                  <li><a href="#" className="hover:text-white transition-colors">Report Bug</a></li>
                  <li><a href="#" className="hover:text-white transition-colors">Contact Us</a></li>
                </ul>
              </div>
            </div>

            <div className="border-t border-white/10 pt-8 flex flex-col md:flex-row items-center justify-between text-sm text-gray-400">
              <p>&copy; 2025 PDSA Game Center. All rights reserved.</p>
              <div className="flex gap-6 mt-4 md:mt-0">
                <a href="#" className="hover:text-white transition-colors">Privacy Policy</a>
                <a href="#" className="hover:text-white transition-colors">Terms of Service</a>
                <a href="#" className="hover:text-white transition-colors">Cookie Policy</a>
              </div>
            </div>
          </div>
        </footer>
      </div>

      {/* Stats Modal */}
      {showStats && (
        <div 
          className="fixed inset-0 bg-black/60 backdrop-blur-sm z-50 flex items-center justify-center p-4"
          onClick={() => setShowStats(false)}
        >
          <div 
            className="bg-gradient-to-br from-gray-900 to-blue-900 border border-white/20 rounded-2xl p-8 max-w-2xl w-full"
            onClick={(e) => e.stopPropagation()}
          >
            <h2 className="text-3xl font-bold mb-6">Your Statistics</h2>
            <div className="grid grid-cols-2 gap-4">
              <div className="bg-white/5 rounded-xl p-4">
                <div className="text-3xl font-bold text-blue-400">127</div>
                <div className="text-sm text-gray-400">Games Played</div>
              </div>
              <div className="bg-white/5 rounded-xl p-4">
                <div className="text-3xl font-bold text-green-400">89</div>
                <div className="text-sm text-gray-400">Games Won</div>
              </div>
              <div className="bg-white/5 rounded-xl p-4">
                <div className="text-3xl font-bold text-yellow-400">42</div>
                <div className="text-sm text-gray-400">Achievements</div>
              </div>
              <div className="bg-white/5 rounded-xl p-4">
                <div className="text-3xl font-bold text-purple-400">5.2K</div>
                <div className="text-sm text-gray-400">Total Points</div>
              </div>
            </div>
            <button 
              onClick={() => setShowStats(false)}
              className="mt-6 w-full bg-blue-600 hover:bg-blue-700 py-3 rounded-lg font-semibold transition-colors"
            >
              Close
            </button>
          </div>
        </div>
      )}
    </div>
  );
}

export default GameDisplay;