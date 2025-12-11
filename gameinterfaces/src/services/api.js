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
export const runSimulation = async () => {
    try {
        const response = await axios.post(`${API_URL}/simulate`);
        return response.data;
    } catch (error) {
        console.error("Error running simulation", error);
        throw error;
    }
};

export const getStats = async () => {
    try {
        const response = await axios.get(`${API_URL}/stats`);
        return response.data;
    } catch (error) {
        console.error("Error getting stats", error);
        throw error;
    }
};
