import { BrowserRouter, Route, Routes } from "react-router-dom";
import { DataProvider } from "./utils/DataContext.jsx";

import LoginForm from './components/LoginForm.jsx';
import RegisterForm from "./components/RegisterForm.jsx";
import WelcomeForm from "./components/WelcomeForm.jsx";

function App() {
    return (
        <div>
            <BrowserRouter>
                <DataProvider>
                    <Routes>
                        <Route path={"/"} Component={WelcomeForm} />
                        <Route path={"/login"} Component={LoginForm} />
                        <Route path={"/register"} Component={RegisterForm} />
                    </Routes>
                </DataProvider>
            </BrowserRouter>
        </div>
    );
}

export default App;
