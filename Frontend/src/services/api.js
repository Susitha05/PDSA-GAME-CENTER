import axios from 'axios';

const API_URL = 'http://localhost:8080/api/game';

export const startGame = async (n) => {
    try {
        const response = await axios.post(`${API_URL}/start`, null, { params: { n } });
        return response.data;
    } catch (error) {
        console.error("Error starting game", error);
        throw error;
    }
};

export const submitGuess = async (gameId, playerName, guess) => {
    try {
        const response = await axios.post(`${API_URL}/submit-guess`, null, {
            params: { gameId, playerName, guess }
        });
        return response.data;
    } catch (error) {
        console.error("Error submitting guess", error);
        throw error;
    }
};
