import React from 'react';

function GuessModal({ choices, onGuess }) {
    return (
        <div style={{
            position: 'fixed',
            top: 0,
            left: 0,
            width: '100%',
            height: '100%',
            background: 'rgba(0, 0, 0, 0.6)',
            backdropFilter: 'blur(8px)',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center',
            zIndex: 1000,
            animation: 'fadeIn 0.3s ease-out'
        }}>
            <div style={{
                background: 'linear-gradient(135deg, #ffffff 0%, #f8fafc 100%)',
                padding: '40px',
                borderRadius: '24px',
                boxShadow: '0 20px 60px rgba(0, 0, 0, 0.3)',
                maxWidth: '500px',
                width: '90%',
                textAlign: 'center',
                animation: 'slideUp 0.4s cubic-bezier(0.16, 1, 0.3, 1)'
            }}>
                <h2 style={{
                    margin: '0 0 12px 0',
                    color: '#0f172a',
                    fontSize: '1.8rem',
                    fontWeight: '700'
                }}>
                    🎯 Guess the Minimum Throws
                </h2>
                <p style={{
                    margin: '0 0 32px 0',
                    color: '#64748b',
                    fontSize: '1rem',
                    lineHeight: '1.5'
                }}>
                    Select the minimum number of dice throws needed to reach the final cell:
                </p>

                <div style={{
                    display: 'flex',
                    gap: '16px',
                    justifyContent: 'center',
                    flexWrap: 'wrap'
                }}>
                    {choices.map((choice, idx) => (
                        <button
                            key={idx}
                            onClick={() => onGuess(choice)}
                            style={{
                                padding: '20px 40px',
                                fontSize: '2rem',
                                fontWeight: '700',
                                background: 'linear-gradient(135deg, #0284c7 0%, #0369a1 100%)',
                                color: 'white',
                                border: 'none',
                                borderRadius: '16px',
                                cursor: 'pointer',
                                transition: 'all 0.3s cubic-bezier(0.4, 0, 0.2, 1)',
                                boxShadow: '0 4px 16px rgba(2, 132, 199, 0.3)',
                                minWidth: '120px'
                            }}
                            onMouseEnter={(e) => {
                                e.target.style.transform = 'translateY(-4px) scale(1.05)';
                                e.target.style.boxShadow = '0 12px 24px rgba(2, 132, 199, 0.4)';
                            }}
                            onMouseLeave={(e) => {
                                e.target.style.transform = 'translateY(0) scale(1)';
                                e.target.style.boxShadow = '0 4px 16px rgba(2, 132, 199, 0.3)';
                            }}
                        >
                            {choice}
                        </button>
                    ))}
                </div>
            </div>
        </div>
    );
}

export default GuessModal;
