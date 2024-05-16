import {useNavigate} from "react-router-dom";
import {useState} from "react";

import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

import {FaUser} from "react-icons/fa";

const PersonalForm = () => {
    const navigate = useNavigate();

    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [birthDate, setBirthDate] = useState(null);
    const [gender, setGender] = useState('');

    const handleBirthDateChange = (date) => {
        setBirthDate(date);
    };
    const handleGenderChange = (event) => {
        setGender(event.target.value);
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        document.cookie = `firstName=${firstName}; expires=${new Date(Date.now() + 86400e3).toUTCString()}; path=/register.cred`;
        document.cookie = `lastName=${lastName}; expires=${new Date(Date.now() + 86400e3).toUTCString()}; path=/register.cred`;
        document.cookie = `birthDate=${birthDate}; expires=${new Date(Date.now() + 86400e3).toUTCString()}; path=/register.cred`;
        document.cookie = `gender=${gender}; expires=${new Date(Date.now() + 86400e3).toUTCString()}; path=/register.cred`;

        navigate('/register.cred');
    };

    return (
        <div className={"login-background1"}>
            <div className="wrapper1">
                <form onSubmit={handleSubmit}>
                    <div className={"input-box1"}>
                        <input type={"text"} placeholder={"First name"} required value={firstName}
                               onChange={(e) => setFirstName(e.target.value)}/>
                        <FaUser className={"icon"}/>
                    </div>

                    <div className={"input-box1"}>
                        <input type={"text"} placeholder={"Last name"} required value={lastName}
                               onChange={(e) => setLastName(e.target.value)}/>
                        <FaUser className={"icon"}/>
                    </div>

                    <div className={"input-box1"}>
                        <DatePicker
                            selected={birthDate}
                            onChange={handleBirthDateChange}
                            placeholderText="Birth date"
                            required
                        />
                        <FaUser className={"icon"}/>
                    </div>

                    <div className={"input-box1"}>
                        <select value={gender} onChange={handleGenderChange} required>
                            <option value="">Select Gender</option>
                            <option value="1">Male</option>
                            <option value="2">Female</option>
                            <option value="3">Other</option>
                        </select>
                        <FaUser className={"icon"}/>
                    </div>

                    <button type={"submit"}>Submit</button>

                </form>
            </div>
        </div>

    );
};

export default PersonalForm;
