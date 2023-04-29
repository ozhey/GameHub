import React, { useState } from 'react';
import authApi from '../../api/auth/Auth';
import "./Login.css"

const LoginForm = ({ setUsername }) => {
    const [user, setUser] = useState('');
    const [password, setPassword] = useState('');
    const [error, setError] = useState(null);
    const [isRegistering, setIsRegistering] = useState(false);

    const handleSubmit = async (event) => {
        event.preventDefault();
        try {
            const response = await authApi.loginOrRegister(user, password, isRegistering);
            setUsername(response.username);
        } catch (error) {
            setError(error.message);
        }
    };

    let toggleForm;
    if (isRegistering) {
        toggleForm = <div className="login--form__toggle">
            <span >Already a member? </span>
            <span className="login--form__toggle-button" onClick={() => setIsRegistering(false)}>Login now</span>
        </div>
    } else {
        toggleForm = <div className="login--form__toggle">
            <span>Not a member? </span>
            <span className="login--form__toggle-button" onClick={() => setIsRegistering(true)}>Register now</span>
        </div>
    }

    return (
        <form className="login-form" onSubmit={handleSubmit}>
            <h2 className="login-form__title">{isRegistering ? 'Register' : 'Login'}</h2>
            <label className="login-form__label">Username</label>
            <input
                type="text"
                className="login-form__input"
                value={user}
                onChange={(event) => setUser(event.target.value)}
                required
            />
            <label className="login-form__label">Password</label>
            <input
                type="password"
                className="login-form__input"
                value={password}
                onChange={(event) => setPassword(event.target.value)}
                required
                autoComplete="on" />
            {error && <div className="login-form__error" >{error}</div>}
            <button type="submit" className="login-form__button">{isRegistering ? 'Register' : 'Login'}</button>
            {toggleForm}
        </form>
    );
};

export default LoginForm;
