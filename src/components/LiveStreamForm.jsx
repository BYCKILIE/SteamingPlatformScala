import React, {useEffect, useRef} from 'react';
import dashjs from "dashjs";
import {useQuery} from "../utils/LinkParameters.jsx";

const VideoStream = () => {
    const videoRef = useRef(null);
    const query = useQuery();
    const id = query.get('id');

    useEffect(() => {
        const url = `http://172.20.10.3:80/tmp_dash/${id}/index.mpd`;
        const player = dashjs.MediaPlayer().create();
        player.initialize(videoRef.current, url, true);

        return () => {
            player.reset();
        };
    }, []);

    return (
        <div style={{ textAlign: "center" }}>
            <video ref={videoRef} controls></video>
        </div>
    );
};

export default VideoStream;
