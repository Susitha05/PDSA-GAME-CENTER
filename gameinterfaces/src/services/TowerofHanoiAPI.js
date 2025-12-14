// gameinterfaces/src/services/TowerofHanoiAPI.js

const API_BASE_URL = 'http://localhost:8080/api/hanoi';

export const hanoiApi = {
  startGame: async () => {
    try {
      const response = await fetch(`${API_BASE_URL}/start`, {
        method: 'GET',
        headers: {
          'Accept': 'application/json',
        },
      });
      
      if (!response.ok) {
        throw new Error('Failed to start game');
      }
      
      return await response.json();
    } catch (error) {
      console.error('Error starting game:', error);
      throw error;
    }
  },

  submitSolution: async (roundId, playerName, diskCount, pegCount, moves) => {
    try {
      console.log('Submitting solution:', { roundId, playerName, diskCount, pegCount, movesCount: moves.length });
      
      const response = await fetch(`${API_BASE_URL}/submit`, {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Accept': 'application/json',
        },
        body: JSON.stringify({
          roundId: roundId,
          playerName: playerName,
          diskCount: diskCount,
          pegCount: pegCount,
          moves: moves,
        }),
      });
      
      if (!response.ok) {
        const errorText = await response.text();
        console.error('Backend error response:', errorText);
        throw new Error(`Failed to submit solution: ${response.status} - ${errorText}`);
      }
      
      const data = await response.json();
      console.log('Submit response:', data);
      return data;
    } catch (error) {
      console.error('Error submitting solution:', error);
      throw error;
    }
  },
};