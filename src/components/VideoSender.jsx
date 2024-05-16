import React, { useState } from 'react';

const VideoSender = () => {
    const [selectedFile, setSelectedFile] = useState(null);
    const [message, setMessage] = useState('');
    const [ws, setWs] = useState(null);

    const handleFileChange = (event) => {
        setSelectedFile(event.target.files[0]);
    };

    const handleUpload = () => {
        if (!selectedFile) {
            setMessage('Please select a file to upload.');
            return;
        }

        const socket = new WebSocket('ws://your-play-server-url/upload');

        socket.onopen = () => {
            setMessage('Socket connected. Uploading...');
            const reader = new FileReader();
            reader.onload = () => {
                socket.send(reader.result);
            };
            reader.readAsArrayBuffer(selectedFile);
        };

        socket.onerror = () => {
            setMessage('Error connecting to WebSocket.');
        };

        socket.onmessage = (event) => {
            setMessage(event.data);
            socket.close();
        };

        setWs(socket);
    };

    return (
        <div>
            <h2>Video Sender</h2>
            <input type="file" onChange={handleFileChange} />
            <button onClick={handleUpload}>Upload</button>
            <p>{message}</p>
        </div>
    );
};

export default VideoSender;
