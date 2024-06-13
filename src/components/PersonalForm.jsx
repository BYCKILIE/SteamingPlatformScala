import {useNavigate} from "react-router-dom";
import {useState} from "react";

import DatePicker from 'react-datepicker';
import 'react-datepicker/dist/react-datepicker.css';

import {FaUser} from "react-icons/fa";
import apiClient from "../utils/AxiosConfig.jsx";

const PersonalForm = () => {
    const navigate = useNavigate();

    const initialDate = new Date(2000, 0, 1);

    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [birthDate, setBirthDate] = useState(initialDate);
    const [gender, setGender] = useState('');

    const handleBirthDateChange = (date) => {
        setBirthDate(date);
    };
    const handleGenderChange = (event) => {
        setGender(event.target.value);
    };

    const handleSubmit = (event) => {
        event.preventDefault();

        const year = birthDate.getFullYear();
        const month = String(birthDate.getMonth() + 1).padStart(2, '0');
        const day = String(birthDate.getDate()).padStart(2, '0');
        const formattedDateString = `${year}-${month}-${day}`;

        apiClient.post(
            '/register.complete',
            {
                id: sessionStorage.getItem("_id"),
                firstName: firstName,
                lastName: lastName,
                birthDate: formattedDateString,
                gender: gender
            }
        )
            .then(response => {
                const success = response.status;
                if (success === 200) {
                    navigate('/register.cred');
                }
            })
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
                            showYearDropdown
                            scrollableYearDropdown
                            yearDropdownItemNumber={15}
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
