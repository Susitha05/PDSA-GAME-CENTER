import React from 'react';

const GuessModal = ({ choices, onGuess }) => {
    return (
        <div className="modal-overlay">
            <div className="modal-content glass-panel" style={{ maxWidth: '450px' }}>
                <h2>Guess Minimum Moves</h2>
                <p>What is the minimum number of dice throws required to reach the last cell?</p>
                <div className="grid-choices">
                    {choices.map((choice) => (
                        <button
                            key={choice}
                            className="btn-secondary"
                            onClick={() => onGuess(choice)}
                        >
                            {choice}
                        </button>
                    ))}
                </div>
            </div>
        </div>
    );
};

export default GuessModal;
