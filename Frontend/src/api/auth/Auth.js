import { API_ADDRESS, API_ADDRESS_BASE_PATH } from "../../consts";

const authApi = {
    async loginOrRegister(username, password, isRegistering) {
        const endpoint = isRegistering ? 'register' : 'login';
        try {
            const response = await fetch(`${API_ADDRESS}${API_ADDRESS_BASE_PATH}/auth/${endpoint}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });
            return await response.json();
        } catch (error) {
            return { error: "error occured" }
        }
    }
};

export default authApi;
