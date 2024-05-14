import {MdEmail} from "react-icons/md";
import {Link} from "react-router-dom";

const WelcomeForm = () => {
    return (
        <div className={"login-background1"}>
            <h1>Byke</h1>
            <h2>World wide | Streamed kind</h2>

            <div className="wrapperWelcome">
                <h3>Enter your email address to start or restart membership</h3>
                <form>
                    <div className={"input-box1"}>
                        <input type={"text"} placeholder={"Email"}/>
                        <MdEmail className={"icon"}/>
                    </div>

                    <button type={"submit"}>Submit</button>
                </form>
            </div>
        </div>
    );
};

export default WelcomeForm;
