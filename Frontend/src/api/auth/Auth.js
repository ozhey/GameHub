const authApi = {
    async loginOrRegister(username, password, isRegistering) {
        const endpoint = isRegistering ? 'register' : 'login';
        try {
            const response = await fetch(`http://localhost:8080/api/v1/auth/${endpoint}`, {
                method: 'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({ username, password }),
            });

            if (response.ok) {
                return await response.json();
            } else {
                throw new Error('Invalid email or password');
            }
        } catch (error) {
            throw new Error(error.message);
        }
    }
};

export default authApi;
