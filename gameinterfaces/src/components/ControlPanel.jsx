import React, { useState } from 'react';

const ControlPanel = ({ onStart, onStats, onSimulate, loadingSim, disabled }) => {
    const [n, setN] = useState(10);
    const [name, setName] = useState('');
    const [vsComputer, setVsComputer] = useState(false);

    const handleSubmit = (e) => {
        e.preventDefault();
        if (!name.trim()) {
            alert("Please enter a player name!");
            return;
        }
        onStart(n, name, vsComputer);
    };

    return (
        <div className="control-panel-container glass-panel" style={{ padding: '20px' }}>
            <form onSubmit={handleSubmit} style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <label style={{ fontWeight: 'bold' }}>Player Name:</label>
                    <input
                        className="input-field"
                        type="text"
                        placeholder="Enter Name"
                        value={name}
                        onChange={(e) => setName(e.target.value)}
                        disabled={disabled}
                        style={{ width: '120px' }}
                    />
                </div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '10px' }}>
                    <label style={{ fontWeight: 'bold' }}>Board Size (N):</label>
                    <input
                        className="input-field"
                        type="number"
                        min="6"
                        max="12"
                        value={n}
                        onChange={(e) => setN(parseInt(e.target.value))}
                        disabled={disabled}
                        style={{ width: '60px' }}
                    />
                </div>
                <div style={{ display: 'flex', alignItems: 'center', gap: '5px' }}>
                    <input
                        type="checkbox"
                        checked={vsComputer}
                        onChange={(e) => setVsComputer(e.target.checked)}
                        disabled={disabled}
                        id="vsComp"
                    />
                    <label htmlFor="vsComp" style={{ cursor: 'pointer' }}>Vs Computer</label>
                </div>
                <button type="submit" className="btn-primary" disabled={disabled}>
                    Start New Game
                </button>
                <button type="button" className="btn-secondary" onClick={onStats}>
                    📈 View Stats
                </button>
                <button type="button" className="btn-primary" onClick={onSimulate} disabled={loadingSim || disabled} style={{ background: '#673ab7' }}>
                    {loadingSim ? 'Running...' : '🔄 Run Simulation (15 Rounds)'}
                </button>
            </form>
        </div>
    );
};

export default ControlPanel;
