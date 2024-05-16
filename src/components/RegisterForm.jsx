import "../styles/RegisterForm.css";
import {FaUser, FaLock} from "react-icons/fa";
import {Link, useNavigate} from "react-router-dom";
import {getCookie} from "../utils/CookieParser.jsx"
import axios from "axios";
import {useState} from "react";

const RegisterForm = () => {
    const navigate = useNavigate();

    const [username, setUsername] = useState('');
    const [password, setPassword] = useState('');
    const [verifyPassword, setVerifyPassword] = useState('');

    const handleSubmit = (event) => {
        event.preventDefault();

        if (password !== verifyPassword) {
            return
        }

        const date = new Date(getCookie("birthDate"));
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const formattedDateString = `${year}-${month}-${day}`;

        axios.post(
            'http://localhost:9000/api.register',
            {
                username: username,
                email: getCookie("email"),
                password: password,
                firstName: getCookie("firstName"),
                lastName: getCookie("lastName"),
                birthDate: formattedDateString,
                gender: getCookie("gender")
            }
        )
            .then(response => {
                const success = response.status;
                if (success === 200) {
                    navigate('/homepage');
                }
            })
    };

    return (
        <div className={"login-background"}>
            <div className="wrapper">
                <form onSubmit={handleSubmit}>
                    <h1>Register</h1>

                    <div className={"input-box"}>
                        <input type={"text"} placeholder={"Username"} required value={username}
                               onChange={(e) => setUsername(e.target.value)}/>
                        <FaUser className={"icon"}/>
                    </div>

                    <div className={"input-box"}>
                        <input type={"password"} placeholder={"Password"} required value={password}
                               onChange={(e) => setPassword(e.target.value)}/>
                        <FaLock className={"icon"}/>
                    </div>

                    <div className={"input-box"}>
                        <input type={"password"} placeholder={"Verify password"} required value={verifyPassword}
                               onChange={(e) => setVerifyPassword(e.target.value)}/>
                        <FaLock className={"icon"}/>
                    </div>

                    <button type={"submit"}>Submit</button>

                </form>
            </div>
        </div>
    );
};

export default RegisterForm;
