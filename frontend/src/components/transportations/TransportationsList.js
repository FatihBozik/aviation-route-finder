import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import {Alert, Badge, Button, Card, Form, InputGroup, Spinner, Table} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faEdit, faPlus, faSearch, faTrash} from '@fortawesome/free-solid-svg-icons';
import TransportationService from '../../services/TransportationService';
import LocationService from '../../services/LocationService';

const TransportationsList = () => {
    const [transportations, setTransportations] = useState([]);
    const [locations, setLocations] = useState({});
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        fetchTransportations();
        fetchLocations();
    }, []);

    const fetchTransportations = () => {
        setLoading(true);
        TransportationService.getAll()
            .then(response => {
                setTransportations(response.data);
                setLoading(false);
            })
            .catch(error => {
                setError('Failed to fetch transportations. ' + (error.response?.data?.message || error.message));
                setLoading(false);
                console.error('Error fetching transportations:', error);
            });
    };

    const fetchLocations = () => {
        LocationService.getAll()
            .then(response => {
                // Create a map of location ids to location objects for easy lookup
                const locationMap = {};
                response.data.forEach(location => {
                    locationMap[location.id] = location;
                });
                setLocations(locationMap);
            })
            .catch(error => {
                console.error('Error fetching locations:', error);
            });
    };

    const handleDelete = (id) => {
        if (window.confirm('Are you sure you want to delete this transportation?')) {
            TransportationService.delete(id)
                .then(() => {
                    fetchTransportations();
                })
                .catch(error => {
                    setError('Failed to delete transportation. ' + (error.response?.data?.message || error.message));
                    console.error('Error deleting transportation:', error);
                });
        }
    };

    const getTransportationTypeBadge = (type) => {
        const badgeVariant = {
            'FLIGHT': 'primary',
            'BUS': 'success',
            'SUBWAY': 'info',
            'UBER': 'warning'
        };

        return <Badge bg={badgeVariant[type] || 'secondary'}>{type}</Badge>;
    };

    const formatOperatingDays = (days) => {
        const dayNames = ['Mon', 'Tue', 'Wed', 'Thu', 'Fri', 'Sat', 'Sun'];
        return days.map(day => dayNames[day-1]).join(', ');
    };

    const getLocationName = (locationId) => {
        return locations[locationId]?.name || 'Unknown Location';
    };

    const filteredTransportations = transportations.filter(transportation => {
        const originName = getLocationName(transportation.originId).toLowerCase();
        const destName = getLocationName(transportation.destinationId).toLowerCase();
        const searchTermLower = searchTerm.toLowerCase();

        return originName.includes(searchTermLower) ||
            destName.includes(searchTermLower) ||
            transportation.type.toLowerCase().includes(searchTermLower);
    });

    return (
        <Card>
            <Card.Header className="d-flex justify-content-between align-items-center">
                <h4>Transportations</h4>
                <Link to="/transportations/add">
                    <Button variant="success">
                        <FontAwesomeIcon icon={faPlus} className="me-2" />
                        Add Transportation
                    </Button>
                </Link>
            </Card.Header>
            <Card.Body>
                {error && <Alert variant="danger">{error}</Alert>}

                <InputGroup className="mb-3">
                    <InputGroup.Text>
                        <FontAwesomeIcon icon={faSearch} />
                    </InputGroup.Text>
                    <Form.Control
                        placeholder="Search transportations..."
                        value={searchTerm}
                        onChange={(e) => setSearchTerm(e.target.value)}
                    />
                </InputGroup>

                {loading ? (
                    <div className="text-center my-5">
                        <Spinner animation="border" role="status">
                            <span className="visually-hidden">Loading...</span>
                        </Spinner>
                    </div>
                ) : (
                    <Table striped bordered hover responsive>
                        <thead>
                        <tr>
                            <th>Origin</th>
                            <th>Destination</th>
                            <th>Type</th>
                            <th>Operating Days</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {filteredTransportations.length > 0 ? (
                            filteredTransportations.map(transportation => (
                                <tr key={transportation.id}>
                                    <td>{getLocationName(transportation.originId)}</td>
                                    <td>{getLocationName(transportation.destinationId)}</td>
                                    <td>{getTransportationTypeBadge(transportation.type)}</td>
                                    <td>{formatOperatingDays(transportation.operatingDays)}</td>
                                    <td>
                                        <Link to={`/transportations/edit/${transportation.id}`} className="me-2">
                                            <Button variant="primary" size="sm">
                                                <FontAwesomeIcon icon={faEdit} />
                                            </Button>
                                        </Link>
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={() => handleDelete(transportation.id)}
                                        >
                                            <FontAwesomeIcon icon={faTrash} />
                                        </Button>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="5" className="text-center">
                                    {searchTerm ? 'No transportations found matching your search.' : 'No transportations available.'}
                                </td>
                            </tr>
                        )}
                        </tbody>
                    </Table>
                )}
            </Card.Body>
        </Card>
    );
};

export default TransportationsList;
