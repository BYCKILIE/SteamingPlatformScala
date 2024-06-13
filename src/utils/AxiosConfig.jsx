import axios from 'axios';

const apiClient = axios.create({
    baseURL: 'http://172.20.10.3:9000/api',
    headers: {
        'Content-Type': 'application/json',
    },
});

apiClient.interceptors.request.use(
    function(config) {
        const localToken = localStorage.getItem('token');
        const sessionToken = sessionStorage.getItem('token');
        if (localToken) {
            config.headers.Authorization = localToken;
        } else if (sessionToken) {
            config.headers.Authorization = sessionToken;
        }
        return config;
    },
    function(error) {
        return Promise.reject(error);
    }
);

export default apiClient;
