import "../styles/LoginForm.css";
import {FaUser, FaLock} from "react-icons/fa";
import {useNavigate} from "react-router-dom";

import {useState, useEffect} from 'react';
import { generateKeyPair } from '../utils/Encryptor';
import apiClient from "../utils/AxiosConfig.jsx";
import pki from "node-forge/lib/x509.js";

const LoginForm = () => {
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const [rememberMe, setRememberMe] = useState(false);

    const navigate = useNavigate();

    const handleCheckboxChange = (event) => {
        setRememberMe(event.target.checked);
    };

    useEffect(() => {
        // const keyPair = generateKeyPair();
        // const publicKeyPem = pki.publicKeyToPem(keyPair.publicKey);
        // const privateKeyPem = pki.privateKeyToPem(keyPair.privateKey);
        //
        // sessionStorage.setItem("pb_key", publicKeyPem);
        // sessionStorage.setItem("pr_key", privateKeyPem);

        const storedData = sessionStorage.getItem('s_public');
        if (!storedData) {
            apiClient.get('/get-public')
                .then(response => {
                    const responseData = response.data;
                    sessionStorage.setItem('s_public', responseData);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        }

        const token = localStorage.getItem("token");
        if (token !== null) {
            apiClient.post(
                '/authorise',
                {publicKeyPem}
            )
                .then(response => {
                    if (response.status === 200) {
                        localStorage.setItem("token", response.data);
                        navigate("/home");
                    }
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        }
    }, [navigate]);

    const handleSubmit = (event) => {
        event.preventDefault();
        apiClient.post(
            '/authenticate',
            {username: username, email: "NULL", password: password, userId: 0, role: "NULL", pKey: sessionStorage.getItem("pb_key")},
        )
            .then(response => {
                const success = response.status;
                if (success === 200) {
                    if (rememberMe) {
                        localStorage.setItem('token', response.data)
                    } else {
                        sessionStorage.setItem('token', response.data)
                    }
                    navigate('/home');
                } else {
                    console.error('Login failed');
                }
            })
            .catch(error => {
                console.error('Error logging in:', error);
            });
    };

    return (
        <div className={"login-background"}>
            <div className="wrapper">
                <form onSubmit={handleSubmit}>
                    <h1>Login</h1>

                    <div className={"input-box"}>
                        <input type={"text"} placeholder={"Email or Username"} required value={username}
                               onChange={(e) => setUsername(e.target.value)}/>
                        <FaUser className={"icon"}/>
                    </div>

                    <div className={"input-box"}>
                        <input type={"password"} placeholder={"Password"} required value={password}
                               onChange={(e) => setPassword(e.target.value)}/>
                        <FaLock className={"icon"}/>
                    </div>

                    <div className="remember-forgot">
                        <label>
                            <input
                                type="checkbox"
                                checked={rememberMe}
                                onChange={handleCheckboxChange}
                            /> Remember me
                        </label>
                        <a href={"#"}>Forgot password?</a>
                    </div>

                    <button type={"submit"}>Login</button>

                    <div className="register-link">
                        <p>
                            {/* eslint-disable-next-line react/no-unescaped-entities */}
                            Don't have an account?<a href={'/register'}> Register</a>
                        </p>
                    </div>
                </form>
            </div>
        </div>
    );
};

export default LoginForm;
