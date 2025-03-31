// src/services/AuthService.js
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';

class AuthService {
    login(username, password) {
        // Base64 encode the username and password for Basic Auth
        const token = btoa(`${username}:${password}`);

        // Set the Authorization header for all future requests
        axios.defaults.headers.common['Authorization'] = `Basic ${token}`;

        // Store the token and user info in local storage
        localStorage.setItem('user', JSON.stringify({
            username,
            token,
            // For the purpose of this demo, we'll hardcode the role based on username
            // In a real application, you would get this from the backend
            role: username.includes('admin') ? 'ADMIN' : 'AGENCY'
        }));

        return axios.get(`${API_URL}/users/me`)
            .then(response => {
                // Store user information in local storage
                localStorage.setItem('user', JSON.stringify({
                    username: response.data.username,
                    token,
                    role: response.data.roles.includes('ROLE_ADMIN') ? 'ADMIN' : 'AGENCY'
                }));

                return response.data;
            });
    }

    logout() {
        localStorage.removeItem('user');
        delete axios.defaults.headers.common['Authorization'];
    }

    getCurrentUser() {
        return JSON.parse(localStorage.getItem('user'));
    }

    isAdmin() {
        const user = this.getCurrentUser();
        return user && user.role === 'ADMIN';
    }

    // Setup axios interceptor to handle 401 and 403 responses
    setupAxiosInterceptors() {
        axios.interceptors.response.use(
            response => response,
            error => {
                if (error.response && (error.response.status === 401 || error.response.status === 403)) {
                    // If not authenticated or forbidden, redirect to login
                    if (error.response.status === 401) {
                        this.logout();
                        window.location.href = '/login';
                    }
                }
                return Promise.reject(error);
            }
        );
    }

    // Initialize auth headers from localStorage on app start
    initializeAuth() {
        const user = this.getCurrentUser();
        if (user && user.token) {
            axios.defaults.headers.common['Authorization'] = `Basic ${user.token}`;
        }
    }
}

export default new AuthService();
