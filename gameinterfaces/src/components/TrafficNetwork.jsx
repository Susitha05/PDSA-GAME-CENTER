import React, { useState } from 'react';
import { motion } from 'framer-motion';
import { FaNetworkWired } from 'react-icons/fa';

const TrafficNetwork = ({ roadCapacities, loading }) => {
  const [highlightedEdge, setHighlightedEdge] = useState(null);
  const [hoveredCapacity, setHoveredCapacity] = useState(null);

  // Professional layered DAG layout - deterministic positions
  // Organized in 5 columns from left (source) to right (sink)
  const nodePositions = {
    // Column 1: Source
    'A': { x: 80, y: 280, layer: 0 },
    
    // Column 2: First hop (B, C, D - upper, middle, lower)
    'B': { x: 240, y: 100, layer: 1 },
    'C': { x: 240, y: 220, layer: 1 },
    'D': { x: 240, y: 340, layer: 1 },
    
    // Column 3: Second layer (E, F)
    'E': { x: 400, y: 140, layer: 2 },
    'F': { x: 400, y: 300, layer: 2 },
    
    // Column 4: Third layer (G, H)
    'G': { x: 560, y: 100, layer: 3 },
    'H': { x: 560, y: 340, layer: 3 },
    
    // Column 5: Sink
    'T': { x: 720, y: 220, layer: 4 }
  };

  const nodeColors = {
    'A': { fill: '#0891B2', stroke: '#06B6D4', glow: '#00E5FF' },     // Cyan
    'T': { fill: '#A855F7', stroke: '#D946EF', glow: '#F0ABFC' },     // Purple/Magenta
    'default': { fill: '#F59E0B', stroke: '#FBBF24', glow: '#FCD34D' } // Amber
  };

  // Calculate perpendicular offset for label positioning (above the line)
  const calculateLabelPosition = (fromPos, toPos) => {
    const midX = (fromPos.x + toPos.x) / 2;
    const midY = (fromPos.y + toPos.y) / 2;
    
    const dx = toPos.x - fromPos.x;
    const dy = toPos.y - fromPos.y;
    const length = Math.sqrt(dx * dx + dy * dy);
    
    if (length === 0) return { x: midX, y: midY };
    
    // Perpendicular vector (upward offset)
    const perpX = -dy / length;
    const perpY = dx / length;
    
    // Position label above the line
    const offset = 20;
    return {
      x: midX + perpX * offset,
      y: midY + perpY * offset
    };
  };

  const NODE_RADIUS = 35;
  
  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ duration: 0.6 }}
      className="w-full bg-gradient-to-br from-slate-950 via-slate-900 to-black rounded-2xl p-8 border-2 border-blue-500 shadow-2xl"
    >
      {/* Header */}
      <div className="flex items-center gap-3 mb-6">
        <FaNetworkWired className="text-2xl text-blue-400" />
        <h2 className="text-3xl font-bold text-white">Flow Network Graph</h2>
        <span className="ml-auto text-xs text-gray-400 font-semibold">DAG Layout • 9 Nodes • 13 Edges</span>
      </div>

      {loading ? (
        <div className="flex justify-center items-center h-96">
          <motion.div
            animate={{ rotate: 360 }}
            transition={{ duration: 2, repeat: Infinity, ease: "linear" }}
            className="w-12 h-12 border-4 border-blue-400 border-t-transparent rounded-full"
          />
        </div>
      ) : (
        <div className="overflow-auto rounded-lg border border-slate-700">
          <svg 
            width="100%" 
            height="550" 
            viewBox="0 0 820 520" 
            preserveAspectRatio="xMidYMid meet"
            className="bg-gradient-to-br from-slate-950 to-slate-900"
          >
            <defs>
              {/* Arrow markers */}
              <marker
                id="arrowDefault"
                markerWidth="15"
                markerHeight="15"
                refX="12"
                refY="3"
                orient="auto"
                markerUnits="strokeWidth"
              >
                <polygon points="0 0, 15 3, 0 6" fill="#64748B" />
              </marker>
              <marker
                id="arrowHighlight"
                markerWidth="15"
                markerHeight="15"
                refX="12"
                refY="3"
                orient="auto"
                markerUnits="strokeWidth"
              >
                <polygon points="0 0, 15 3, 0 6" fill="#FCD34D" />
              </marker>
              
              {/* Advanced filters */}
              <filter id="edgeShadow">
                <feDropShadow dx="0" dy="1" stdDeviation="1.5" floodOpacity="0.3"/>
              </filter>
              <filter id="glow">
                <feGaussianBlur stdDeviation="3" result="coloredBlur"/>
                <feMerge>
                  <feMergeNode in="coloredBlur"/>
                  <feMergeNode in="SourceGraphic"/>
                </feMerge>
              </filter>
            </defs>

            {/* Draw edges first (behind nodes) */}
            {roadCapacities && roadCapacities.map((road, idx) => {
              const fromPos = nodePositions[road.from];
              const toPos = nodePositions[road.to];
              const isHighlighted = highlightedEdge === idx;
              const labelPos = calculateLabelPosition(fromPos, toPos);

              return (
                <g key={`edge-${idx}`}>
                  {/* Edge line with shadow */}
                  <line
                    x1={fromPos.x}
                    y1={fromPos.y}
                    x2={toPos.x}
                    y2={toPos.y}
                    stroke={isHighlighted ? '#FCD34D' : '#64748B'}
                    strokeWidth={isHighlighted ? 3.5 : 2.5}
                    opacity={isHighlighted ? 1 : 0.65}
                    markerEnd={isHighlighted ? 'url(#arrowHighlight)' : 'url(#arrowDefault)'}
                    filter="url(#edgeShadow)"
                    className="transition-all duration-200 cursor-pointer hover:opacity-100"
                    onMouseEnter={() => {
                      setHighlightedEdge(idx);
                      setHoveredCapacity({ x: labelPos.x, y: labelPos.y, capacity: road.capacity });
                    }}
                    onMouseLeave={() => {
                      setHighlightedEdge(null);
                      setHoveredCapacity(null);
                    }}
                  />

                  {/* Capacity label - positioned above edge */}
                  <g
                    onMouseEnter={() => {
                      setHighlightedEdge(idx);
                      setHoveredCapacity({ x: labelPos.x, y: labelPos.y, capacity: road.capacity });
                    }}
                    onMouseLeave={() => {
                      setHighlightedEdge(null);
                      setHoveredCapacity(null);
                    }}
                    className="cursor-pointer"
                  >
                    {/* Label background */}
                    <rect
                      x={labelPos.x - 22}
                      y={labelPos.y - 14}
                      width="44"
                      height="28"
                      rx="5"
                      fill={isHighlighted ? '#FCD34D20' : '#0F172A'}
                      stroke={isHighlighted ? '#FCD34D' : '#475569'}
                      strokeWidth={isHighlighted ? '2' : '1.5'}
                      opacity="0.95"
                      pointerEvents="none"
                    />
                    {/* Label text */}
                    <text
                      x={labelPos.x}
                      y={labelPos.y + 1}
                      fill={isHighlighted ? '#FCD34D' : '#CBD5E1'}
                      fontSize="13"
                      fontWeight="700"
                      textAnchor="middle"
                      dominantBaseline="middle"
                      pointerEvents="none"
                      className="transition-colors duration-150"
                    >
                      {road.capacity}
                    </text>
                  </g>
                </g>
              );
            })}

            {/* Draw nodes with animations */}
            {Object.entries(nodePositions).map(([nodeName, pos], nodeIdx) => {
              const colorConfig = nodeColors[nodeName] || nodeColors.default;
              const isSourceOrSink = nodeName === 'A' || nodeName === 'T';

              const nodeVariants = {
                hidden: { scale: 0, opacity: 0 },
                visible: {
                  scale: 1,
                  opacity: 1,
                  transition: {
                    delay: nodeIdx * 0.08,
                    duration: 0.5,
                    ease: 'easeOut'
                  }
                }
              };

              return (
                <motion.g
                  key={`node-${nodeName}`}
                  initial="hidden"
                  animate="visible"
                  variants={nodeVariants}
                  className="group"
                >
                  {/* Pulsing glow ring for source/sink */}
                  {isSourceOrSink && (
                    <>
                      <circle
                        cx={pos.x}
                        cy={pos.y}
                        r={NODE_RADIUS + 12}
                        fill="none"
                        stroke={colorConfig.glow}
                        strokeWidth="1.5"
                        opacity="0.2"
                        className="animate-pulse"
                      />
                      <circle
                        cx={pos.x}
                        cy={pos.y}
                        r={NODE_RADIUS + 18}
                        fill="none"
                        stroke={colorConfig.glow}
                        strokeWidth="0.5"
                        opacity="0.1"
                        className="animate-pulse"
                        style={{ animationDelay: '0.2s' }}
                      />
                    </>
                  )}

                  {/* Main node circle */}
                  <circle
                    cx={pos.x}
                    cy={pos.y}
                    r={NODE_RADIUS}
                    fill={colorConfig.fill}
                    stroke={colorConfig.stroke}
                    strokeWidth="2.5"
                    opacity="0.9"
                    className="hover:opacity-100 hover:stroke-current transition-all duration-200 cursor-pointer"
                    style={{
                      filter: isSourceOrSink ? 'url(#glow)' : 'none',
                      strokeDasharray: isSourceOrSink ? 'none' : 'none'
                    }}
                  />

                  {/* Node label */}
                  <text
                    x={pos.x}
                    y={pos.y}
                    fill="white"
                    fontSize="22"
                    fontWeight="900"
                    textAnchor="middle"
                    dominantBaseline="middle"
                    pointerEvents="none"
                    className="select-none font-sans"
                  >
                    {nodeName}
                  </text>

                  {/* Optional: Node tooltip on hover */}
                  <title>{nodeName === 'A' ? 'Source Node' : nodeName === 'T' ? 'Sink Node' : `Node ${nodeName}`}</title>
                </motion.g>
              );
            })}

            {/* Column guides (subtle) */}
            <line x1="80" y1="20" x2="80" y2="500" stroke="#1E293B" strokeWidth="1" opacity="0.3" strokeDasharray="5,5" />
            <line x1="240" y1="20" x2="240" y2="500" stroke="#1E293B" strokeWidth="1" opacity="0.3" strokeDasharray="5,5" />
            <line x1="400" y1="20" x2="400" y2="500" stroke="#1E293B" strokeWidth="1" opacity="0.3" strokeDasharray="5,5" />
            <line x1="560" y1="20" x2="560" y2="500" stroke="#1E293B" strokeWidth="1" opacity="0.3" strokeDasharray="5,5" />
            <line x1="720" y1="20" x2="720" y2="500" stroke="#1E293B" strokeWidth="1" opacity="0.3" strokeDasharray="5,5" />
          </svg>
        </div>
      )}

      {/* Legend */}
      <div className="mt-8 grid grid-cols-3 gap-4">
        <motion.div
          whileHover={{ scale: 1.05, translateY: -2 }}
          className="p-4 bg-gradient-to-br from-cyan-950 to-slate-950 rounded-lg border border-cyan-600 hover:border-cyan-500 transition-all"
        >
          <div className="flex items-center gap-3">
            <div className="w-6 h-6 rounded-full bg-cyan-500 shadow-lg shadow-cyan-500/50 animate-pulse"></div>
            <div>
              <p className="text-cyan-300 text-xs font-bold">Source Node</p>
              <p className="text-cyan-200 text-sm">A</p>
            </div>
          </div>
        </motion.div>

        <motion.div
          whileHover={{ scale: 1.05, translateY: -2 }}
          className="p-4 bg-gradient-to-br from-amber-950 to-slate-950 rounded-lg border border-amber-600 hover:border-amber-500 transition-all"
        >
          <div className="flex items-center gap-3">
            <div className="w-6 h-6 rounded-full bg-amber-500 shadow-lg shadow-amber-500/50"></div>
            <div>
              <p className="text-amber-300 text-xs font-bold">Regular Nodes</p>
              <p className="text-amber-200 text-sm">B, C, D, E, F, G, H</p>
            </div>
          </div>
        </motion.div>

        <motion.div
          whileHover={{ scale: 1.05, translateY: -2 }}
          className="p-4 bg-gradient-to-br from-purple-950 to-slate-950 rounded-lg border border-purple-600 hover:border-purple-500 transition-all"
        >
          <div className="flex items-center gap-3">
            <div className="w-6 h-6 rounded-full bg-purple-500 shadow-lg shadow-purple-500/50 animate-pulse"></div>
            <div>
              <p className="text-purple-300 text-xs font-bold">Sink Node</p>
              <p className="text-purple-200 text-sm">T</p>
            </div>
          </div>
        </motion.div>
      </div>

      {/* Info Section */}
      <div className="mt-6 p-5 bg-gradient-to-r from-blue-950 to-slate-950 rounded-lg border border-blue-600 border-opacity-30">
        <p className="text-slate-200 text-sm leading-relaxed">
          <span className="font-bold text-blue-300">🎯 How to Read:</span> This graph shows a flow network with nodes arranged in layers (columns). 
          Each edge is labeled with its <span className="text-amber-300 font-semibold">capacity</span> (max vehicles/minute). 
          <span className="text-cyan-300 font-semibold"> A</span> is the source, <span className="text-purple-300 font-semibold">T</span> is the sink. 
          Find the maximum total flow from source to sink. <span className="text-gray-400 text-xs">Hover over edges to highlight them.</span>
        </p>
      </div>
    </motion.div>
  );
};

export default TrafficNetwork;
