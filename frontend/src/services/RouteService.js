import axios from 'axios';

const API_URL = 'http://localhost:8080/api/routes';

class RouteService {
    findRoutes(originCode, destinationCode, date) {
        return axios.get(`${API_URL}/${originCode}/${destinationCode}/${date}`);
    }
}

export default new RouteService();
