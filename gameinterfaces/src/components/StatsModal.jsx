import React, { useEffect, useState } from 'react';
import axios from 'axios';

const StatsModal = ({ onClose }) => {
    const [stats, setStats] = useState(null);
    const [loading, setLoading] = useState(false);
    const [simulating, setSimulating] = useState(false);

    const fetchStats = async () => {
        try {
            setLoading(true);
            const res = await axios.get('http://localhost:8080/api/game/stats');
            setStats(res.data);
            setLoading(false);
        } catch (e) {
            console.error(e);
            setLoading(false);
        }
    };

    const runSimulation = async () => {
        try {
            setSimulating(true);
            await axios.post('http://localhost:8080/api/game/simulate');
            await fetchStats(); // Refresh
            setSimulating(false);
        } catch (e) {
            console.error(e);
            setSimulating(false);
        }
    };

    useEffect(() => {
        fetchStats();
    }, []);

    return (
        <div className="modal-overlay" onClick={onClose}>
            <div className="modal-content glass-panel" onClick={e => e.stopPropagation()} style={{ maxWidth: '600px' }}>
                <h2>Algorithm Performance Report</h2>

                <p style={{ color: '#aaa', marginBottom: '20px' }}>
                    Comparing minimum dice throws calculation times across 15+ rounds.
                </p>

                {loading ? (
                    <p>Loading data...</p>
                ) : (
                    <div style={{ width: '100%', overflowX: 'auto' }}>
                        <table style={{ width: '100%', borderCollapse: 'collapse', marginBottom: '20px' }}>
                            <thead>
                                <tr style={{ borderBottom: '1px solid #444' }}>
                                    <th style={{ padding: '10px', textAlign: 'left' }}>Algorithm</th>
                                    <th style={{ padding: '10px', textAlign: 'right' }}>Avg Time (ns)</th>
                                    <th style={{ padding: '10px', textAlign: 'right' }}>Samples</th>
                                </tr>
                            </thead>
                            <tbody>
                                {stats && Object.keys(stats).map(algo => (
                                    <tr key={algo} style={{ borderBottom: '1px solid #333' }}>
                                        <td style={{ padding: '10px', textAlign: 'left', fontWeight: 'bold', color: 'var(--primary-color)' }}>{algo}</td>
                                        <td style={{ padding: '10px', textAlign: 'right' }}>{stats[algo].averageTimeNano.toFixed(2)}</td>
                                        <td style={{ padding: '10px', textAlign: 'right' }}>{stats[algo].samples}</td>
                                    </tr>
                                ))}
                            </tbody>
                        </table>
                    </div>
                )}

                <div style={{ display: 'flex', gap: '10px', justifyContent: 'center', marginTop: '20px' }}>
                    <button
                        className="btn-primary"
                        onClick={runSimulation}
                        disabled={simulating || loading}
                    >
                        {simulating ? 'Running 15 Rounds...' : 'Run 15-Round Simulation'}
                    </button>
                    <button className="btn-secondary" onClick={onClose}>Close</button>
                </div>
            </div>
        </div>
    );
};

export default StatsModal;
