import React, { useState, useEffect } from 'react';
import {useQuery} from "../utils/LinkParameters.jsx";

const ChatForm = () => {
    const [message, setMessage] = useState('');
    const [receivedMessages, setReceivedMessages] = useState([]);
    const [webSocket, setWebSocket] = useState(null);
    const query = useQuery();
    const id = query.get('id');

    useEffect(() => {
        const ws = new WebSocket(`ws://172.20.10.3:9000/api/chat?chatId=${id}`); // Change URL to your WebSocket server

        ws.onopen = () => {
            console.log('WebSocket connected');
        };

        ws.onmessage = (event) => {
            const message = event.data;
            setReceivedMessages(prevMessages => [...prevMessages, message]);
        };

        ws.onclose = () => {
            console.log('WebSocket disconnected');
        };

        setWebSocket(ws);

        return () => {
            ws.close();
        };
    }, []);

    const handleMessageChange = (e) => {
        setMessage(e.target.value);
    };

    const sendMessage = () => {
        if (webSocket && message.trim() !== '') {
            webSocket.send(message);
            setMessage('');
        }
    };

    return (
        <div>
            <h1>WebSocket Chat</h1>
            <div>
                <h2>Received Messages</h2>
                <ul>
                    {receivedMessages.map((msg, index) => (
                        <li key={index}>{msg}</li>
                    ))}
                </ul>
            </div>
            <div>
                <input type="text" value={message} onChange={handleMessageChange} />
                <button onClick={sendMessage}>Send</button>
            </div>
        </div>
    );
};

export default ChatForm;
