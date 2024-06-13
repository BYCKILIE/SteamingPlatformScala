import { useEffect, useState } from 'react';
import apiClient from "../utils/AxiosConfig.jsx";
import {useQuery} from "../utils/LinkParameters.jsx";

const VideoPlayer = () => {
    const [videoUrl, setVideoUrl] = useState('');
    const [videoType, setVideoType] = useState('');
    const query = useQuery();
    const id = query.get('id');

    useEffect(() => {
        const fetchVideo = async () => {
            try {
                const response = await apiClient.get(`/stream-video?videoId=${id}`, {
                    responseType: 'blob',
                });
                setVideoType(response.headers.get('content-type'));
                const videoBlob = new Blob([response.data], { type: videoType });
                const url = URL.createObjectURL(videoBlob);
                setVideoUrl(url);
            } catch (error) {
                console.error('Error fetching video:', error);
            }
        };
        return() => fetchVideo();
    }, [id, videoType]);

    return (
        <div>
            {videoUrl && (
                <video controls>
                    <source src={videoUrl} type={videoType} />
                    Your browser does not support the video tag.
                </video>
            )}
        </div>
    );
};

export default VideoPlayer;
