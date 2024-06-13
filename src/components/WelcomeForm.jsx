import {MdEmail} from "react-icons/md";
import {useNavigate} from "react-router-dom";
import {useState} from "react";

import {validateEmail} from "../utils/EmailValidator.jsx"
import apiClient from "../utils/AxiosConfig.jsx";

const WelcomeForm = () => {
    const [email, setEmail] = useState('');
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();
        const isValidEmail = validateEmail(email);

        if (!isValidEmail) {
            window.alert("Invalid Email address");
            return;
        }

        apiClient.post(
            '/register.is-valid',
            {email: email}
        )
            .then(response => {
                const success = response.status;
                if (success === 200) {
                    navigate('/homepage');
                } else if (success === 201) {
                    sessionStorage.setItem("_id", response.data);
                    setTimeout(() => {
                        sessionStorage.removeItem("_id");
                    }, 30 * 60 * 1000);
                    navigate('/register.p-data');
                }
            })
    };

    return (
        <div className={"login-background1"}>
            <form onSubmit={handleSubmit}>
                <h1>Byke</h1>
                <h2>World wide | Streamed kind</h2>

                <div className="wrapperWelcome">
                    <h3>Enter your email address to start or restart membership</h3>
                    <div className={"input-box1"}>
                        <input type={"text"} placeholder={"Email address"} required
                               onChange={(e) => setEmail(e.target.value)} value={email}/>
                        <MdEmail className={"icon"}/>
                    </div>
                    <button type={"submit"}>Submit</button>
                </div>
            </form>
        </div>
    );
};

export default WelcomeForm;
