import React, {useEffect, useState} from 'react';
import {Link} from 'react-router-dom';
import {Alert, Button, Card, Form, InputGroup, Spinner, Table} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faEdit, faPlus, faSearch, faTrash} from '@fortawesome/free-solid-svg-icons';
import LocationService from '../../services/LocationService';

const LocationsList = () => {
    const [locations, setLocations] = useState([]);
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState('');
    const [searchTerm, setSearchTerm] = useState('');

    useEffect(() => {
        fetchLocations();
    }, []);

    const fetchLocations = () => {
        setLoading(true);
        LocationService.getAll()
            .then(response => {
                setLocations(response.data);
                setLoading(false);
            })
            .catch(error => {
                setError('Failed to fetch locations. ' + (error.response?.data?.message || error.message));
                setLoading(false);
                console.error('Error fetching locations:', error);
            });
    };

    const handleDelete = (id) => {
        if (window.confirm('Are you sure you want to delete this location?')) {
            LocationService.delete(id)
                .then(() => {
                    fetchLocations();
                })
                .catch(error => {
                    setError('Failed to delete location. ' + (error.response?.data?.message || error.message));
                    console.error('Error deleting location:', error);
                });
        }
    };

    const filteredLocations = locations.filter(location =>
        location.name.toLowerCase().includes(searchTerm.toLowerCase()) ||
        location.country.toLowerCase().includes(searchTerm.toLowerCase()) ||
        location.city.toLowerCase().includes(searchTerm.toLowerCase()) ||
        location.code.toLowerCase().includes(searchTerm.toLowerCase())
    );

    return (
        <Card>
            <Card.Header className="d-flex justify-content-between align-items-center">
                <h4>Locations</h4>
                <Link to="/locations/add">
                    <Button variant="success">
                        <FontAwesomeIcon icon={faPlus} className="me-2" />
                        Add Location
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
                        placeholder="Search locations..."
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
                            <th>Name</th>
                            <th>Country</th>
                            <th>City</th>
                            <th>Location Code</th>
                            <th>Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {filteredLocations.length > 0 ? (
                            filteredLocations.map(location => (
                                <tr key={location.id}>
                                    <td>{location.name}</td>
                                    <td>{location.country}</td>
                                    <td>{location.city}</td>
                                    <td>{location.code}</td>
                                    <td>
                                        <Link to={`/locations/edit/${location.id}`} className="me-2">
                                            <Button variant="primary" size="sm">
                                                <FontAwesomeIcon icon={faEdit} />
                                            </Button>
                                        </Link>
                                        <Button
                                            variant="danger"
                                            size="sm"
                                            onClick={() => handleDelete(location.id)}
                                        >
                                            <FontAwesomeIcon icon={faTrash} />
                                        </Button>
                                    </td>
                                </tr>
                            ))
                        ) : (
                            <tr>
                                <td colSpan="5" className="text-center">
                                    {searchTerm ? 'No locations found matching your search.' : 'No locations available.'}
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

export default LocationsList;
