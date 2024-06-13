import React, { useState } from 'react';
import apiClient from "../utils/AxiosConfig.jsx";
const PostForm = () => {
    const [title, setTitle] = useState('');
    const [type, setType] = useState('');
    const [description, setDescription] = useState('');
    const [photo, setPhoto] = useState(null);
    const [error, setError] = useState(null);
    const [success, setSuccess] = useState(null);

    const handleTitleChange = (e) => setTitle(e.target.value);
    const handleTypeChange = (e) => setType(e.target.value);
    const handleDescriptionChange = (e) => setDescription(e.target.value);
    const handlePhotoChange = (e) => setPhoto(e.target.files[0]);

    const handleSubmit = async (e) => {
        e.preventDefault();
        setError(null);
        setSuccess(null);

        if (photo) {
            const reader = new FileReader();
            reader.onloadend = async () => {
                const base64Photo = reader.result.split(',')[1];

                const postData = {
                    title: title,
                    description: description,
                    postingDate: "ac",
                    path: title,
                    PostType: type,
                    thumbnail: base64Photo
                };

                try {
                    const response = await apiClient.post('/upload', postData);
                    setSuccess('Post created successfully!');
                    setTitle('');
                    setDescription('');
                    setPhoto(null);
                } catch (err) {
                    setError('Failed to create post. Please try again.');
                }
            };
            reader.readAsDataURL(photo);
        } else {
            setError('Photo is required');
        }
    };

    return (
        <div>
            <h2>Create a New Post</h2>
            <form onSubmit={handleSubmit}>
                <div>
                    <label>Title:</label>
                    <input type="text" value={title} onChange={handleTitleChange} required />
                </div>
                <div>
                    <label>Type:</label>
                    <input type="text" placeholder={"normal / live"} value={type} onChange={handleTypeChange} required />
                </div>
                <div>
                    <label>Description:</label>
                    <textarea value={description} onChange={handleDescriptionChange} required></textarea>
                </div>
                <div>
                    <label>Photo:</label>
                    <input type="file" onChange={handlePhotoChange} accept="image/*" required />
                </div>
                <button type="submit">Create Post</button>
            </form>
            {error && <p style={{ color: 'red' }}>{error}</p>}
            {success && <p style={{ color: 'green' }}>{success}</p>}
        </div>
    );
};

export default PostForm;
