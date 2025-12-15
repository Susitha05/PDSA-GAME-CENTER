import React, { useMemo } from 'react';
import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    Legend,
    ResponsiveContainer,
    LineChart,
    Line
} from 'recharts';

function SimulationReport({ data, onClose }) {
    // Data is List of AlgorithmExecution: { algorithmName, executionTimeNano, resultMinThrows, boardSize }

    // Transform data for charts
    const chartData = useMemo(() => {
        if (!data || data.length === 0) return [];

        // Group by Round (we assume order is conserved or simple 15 rounds imply sequential blocks)
        // Actually the data is flat list. 
        // Since we ran simulation in a loop: startGame(10) -> calls BFS, Dijkstra, A*
        // So for 15 rounds, we have 45 entries.
        // We should group them by "Round". Since they are saved sequentially:
        // Round 1: BFS, Dijkstra, A*
        // Round 2: BFS, Dijkstra, A* ...

        const rounds = [];
        for (let i = 0; i < data.length; i += 3) {
            if (i + 2 < data.length) {
                // Assume order BFS, Dijkstra, A* (based on GameController)
                // Verify names if possible, but for now strict order is simplest or we find by name
                const chunk = data.slice(i, i + 3);
                const roundObj = { name: `Round ${rounds.length + 1}` };

                chunk.forEach(item => {
                    roundObj[item.algorithmName] = item.executionTimeNano;
                });
                rounds.push(roundObj);
            }
        }
        return rounds;
    }, [data]);

    const avgData = useMemo(() => {
        if (!data || data.length === 0) return [];
        const sums = {};
        const counts = {};

        data.forEach(d => {
            sums[d.algorithmName] = (sums[d.algorithmName] || 0) + d.executionTimeNano;
            counts[d.algorithmName] = (counts[d.algorithmName] || 0) + 1;
        });

        return Object.keys(sums).map(key => ({
            name: key,
            avgTime: sums[key] / counts[key]
        }));
    }, [data]);

    return (
        <div className="modal-overlay">
            <div className="modal-content" style={{ maxWidth: '900px', width: '95%' }}>
                <h2>Simulation Report (15 Rounds)</h2>
                <p>Execution Time (Nanoseconds) for Finding Min Throws</p>

                <div style={{ height: '300px', width: '100%', marginTop: '20px' }}>
                    <h4>Time Taken per Round</h4>
                    <ResponsiveContainer width="100%" height="100%">
                        <LineChart data={chartData}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="name" />
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Line type="monotone" dataKey="BFS" stroke="#8884d8" />
                            <Line type="monotone" dataKey="Dijkstra" stroke="#82ca9d" />
                            <Line type="monotone" dataKey="A*" stroke="#ff7300" />
                        </LineChart>
                    </ResponsiveContainer>
                </div>

                <div style={{ height: '250px', width: '100%', marginTop: '40px' }}>
                    <h4>Average Execution Time</h4>
                    <ResponsiveContainer width="100%" height="100%">
                        <BarChart data={avgData}>
                            <CartesianGrid strokeDasharray="3 3" />
                            <XAxis dataKey="name" />
                            <YAxis />
                            <Tooltip />
                            <Legend />
                            <Bar dataKey="avgTime" fill="#8884d8" name="Avg Time (ns)" />
                        </BarChart>
                    </ResponsiveContainer>
                </div>

                <button className="btn-secondary" onClick={onClose} style={{ marginTop: '30px' }}>
                    Close Report
                </button>
            </div>
        </div>
    );
}

export default SimulationReport;
