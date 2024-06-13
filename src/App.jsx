import { BrowserRouter, Route, Routes } from "react-router-dom";
import { DataProvider } from "./utils/DataContext.jsx";

import LoginForm from './components/LoginForm.jsx';
import RegisterForm from "./components/RegisterForm.jsx";
import WelcomeForm from "./components/WelcomeForm.jsx";
import VideoPlayer from "./components/VideoPlayer.jsx";
import PersonalForm from "./components/PersonalForm.jsx";
import HomePage from "./components/HomePage.jsx";
import VideoStream from "./components/LiveStreamForm.jsx";
import PostForm from "./components/PostForm.jsx";
import ChatForm from "./components/Chat.jsx";

function App() {
    return (
        <div>
            <BrowserRouter>
                <DataProvider>
                    <Routes>
                        <Route path={"/"} Component={WelcomeForm} />
                        <Route path={"/register.p-data"} Component={PersonalForm} />
                        <Route path={"/register.cred"} Component={RegisterForm} />
                        <Route path={"/login"} Component={LoginForm} />
                        <Route path={"/home/video"} Component={VideoPlayer} />
                        <Route path={"/home"} Component={HomePage} />
                        <Route path={"/home/live"} Component={VideoStream} />
                        <Route path={"/home/create-live"} Component={PostForm} />
                        <Route path={"/home/chat"} Component={ChatForm} />
                    </Routes>
                </DataProvider>
            </BrowserRouter>
        </div>
    );
}

export default App;
