import "../styles/RegisterForm.css";
import {FaUser, FaLock} from "react-icons/fa";
import {Link} from "react-router-dom";

const RegisterForm = () => {
    return (
        <div className={"login-background"}>
            <div className="wrapper">
                <form>
                    <h1>Register</h1>

                    <div className={"input-box"}>
                        <input type={"text"} placeholder={"Username"} required value/>
                        <FaUser className={"icon"}/>
                    </div>

                    <div className={"input-box"}>
                        <input type={"password"} placeholder={"Password"} required value/>
                        <FaLock className={"icon"}/>
                    </div>

                    <button type={"submit"}>Submit</button>

                </form>
            </div>
        </div>
    );
};

export default RegisterForm;
