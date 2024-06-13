import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom'; // Import Link from react-router-dom
import apiClient from "../utils/AxiosConfig.jsx";

const HomePage = () => {
    const [videos, setVideos] = useState([]);

    useEffect(() => {
        apiClient.get('/videos')
            .then(response => {
                const parser = new DOMParser();
                const xml = parser.parseFromString(response.data, 'application/xml');
                const videosList = Array.from(xml.getElementsByTagName('video')).map(videoNode => ({
                    id: videoNode.getElementsByTagName('id')[0].textContent,
                    title: videoNode.getElementsByTagName('title')[0].textContent,
                    type: videoNode.getElementsByTagName('type')[0].textContent,
                    thumbnail: videoNode.getElementsByTagName('thumbnail')[0].textContent,
                }));
                setVideos(videosList);
            })
            .catch(error => console.error('Error fetching XML:', error));
    }, []);

    return (
        <div>
            <h1>Videos</h1>
            <ul>
                {videos.map(video => (
                    <li key={video.id}>
                        <Link to={video.type === 'normal' ? `/home/video?id=${video.id}` : `/home/live?id=${video.id}`}>
                            <img src={`data:image/jpeg;base64,${video.thumbnail}`} alt={video.title} />
                            <br />
                            {video.title}
                        </Link>
                    </li>
                ))}
            </ul>
        </div>
    );
}

export default HomePage;
