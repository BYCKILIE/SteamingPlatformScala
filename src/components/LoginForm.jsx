import "../styles/LoginForm.css";
import {FaUser, FaLock} from "react-icons/fa";
import {useNavigate} from "react-router-dom";

import {useState, useEffect} from 'react';
import axios from 'axios';

const LoginForm = () => {
    // eslint-disable-next-line no-unused-vars
    const [data, setData] = useState(null);
    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');

    const [rememberMe, setRememberMe] = useState(false);

    const navigate = useNavigate();

    const handleCheckboxChange = (event) => {
        setRememberMe(event.target.checked);
    };

    useEffect(() => {
        document.cookie = "publicKey=12";

        const storedData = localStorage.getItem('sp_public');
        if (storedData) {
            setData(storedData);
        } else {
            axios.get('http://localhost:9000/api.get-public')
                .then(response => {
                    const responseData = response.data;
                    localStorage.setItem('sp_public', JSON.stringify(responseData));
                    setData(responseData);
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        }

        const token = localStorage.getItem("token");
        if (token !== null) {
            document.cookie = `email=${token}`
            axios.get(
                'http://localhost:9000/api.authorise',
                { withCredentials: true}
            )
                .then(response => {
                    if (response.status === 200) {
                        navigate("/home");
                    }
                })
                .catch(error => {
                    console.error('Error fetching data:', error);
                });
        }
    }, []);

    const handleSubmit = (event) => {
        event.preventDefault();
        axios.post(
            'http://localhost:9000/api.authenticate',
            {username: username, email: "NULL", password: password, userId: 0, role: "NULL"},
            {
                withCredentials: true
            }
        )
            .then(response => {
                const success = response.status;
                if (success === 200) {
                    if (rememberMe) {
                        localStorage.setItem('token', response.data.toString())
                    }
                    navigate('/homepage');
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
