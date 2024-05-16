import React, { useEffect, useState } from 'react';
import axios from 'axios';

const VideoPlayer = () => {
    const [videoUrl, setVideoUrl] = useState('');

    useEffect(() => {
        const fetchVideo = async () => {
            try {
                const response = await axios.get('http://localhost:9000/api.stream-video', {
                    responseType: 'blob',
                });
                const videoBlob = new Blob([response.data], { type: 'video/mp4' });
                const url = URL.createObjectURL(videoBlob);
                setVideoUrl(url);
            } catch (error) {
                console.error('Error fetching video:', error);
            }
        };

        fetchVideo();
    }, []);

    return (
        <div>
            {videoUrl && (
                <video controls>
                    <source src={videoUrl} type="video/mp4" />
                    Your browser does not support the video tag.
                </video>
            )}
        </div>
    );
};

export default VideoPlayer;
