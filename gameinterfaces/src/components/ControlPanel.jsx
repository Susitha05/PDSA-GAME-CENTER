import React, { useState } from 'react';

const ControlPanel = ({ onStart, onStats, disabled }) => {
    const [n, setN] = useState(10);

    const handleSubmit = (e) => {
        e.preventDefault();
        onStart(n);
    };

    return (
        <div className="control-panel-container glass-panel" style={{ padding: '20px' }}>
            <form onSubmit={handleSubmit} style={{ display: 'flex', gap: '15px', alignItems: 'center' }}>
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
                        style={{ width: '80px' }}
                    />
                </div>
                <button type="submit" className="btn-primary" disabled={disabled}>
                    Start New Game
                </button>
                <button type="button" className="btn-secondary" onClick={onStats}>
                    📈 View Reports
                </button>
            </form>
        </div>
    );
};

export default ControlPanel;
