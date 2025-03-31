import axios from 'axios';

const API_URL = 'http://localhost:8080/api/routes';

class RouteService {
    findRoutes(originId, destinationId, date) {
        return axios.get(`${API_URL}/find`, {
            params: {
                origin: originId,
                destination: destinationId,
                date: date
            }
        });
    }
}

export default new RouteService();
