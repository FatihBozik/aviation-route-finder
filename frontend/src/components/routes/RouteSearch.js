import React, {useEffect, useState} from 'react';
import {Alert, Button, Card, Col, Form, Row, Spinner} from 'react-bootstrap';
import {Formik} from 'formik';
import * as Yup from 'yup';
import LocationService from '../../services/LocationService';
import RouteService from '../../services/RouteService';
import RouteResults from './RouteResults';
import RouteDetails from './RouteDetails';

const RouteSearch = () => {
    const [locations, setLocations] = useState([]);
    const [routes, setRoutes] = useState([]);
    const [loading, setLoading] = useState(false);
    const [searching, setSearching] = useState(false);
    const [error, setError] = useState('');
    const [selectedRoute, setSelectedRoute] = useState(null);

    useEffect(() => {
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
    }, []);

    const initialValues = {
        originLocation: '',
        destinationLocation: '',
        date: formatDate(new Date())
    };

    const validationSchema = Yup.object().shape({
        originLocation: Yup.string().required('Origin location is required'),
        destinationLocation: Yup.string()
            .required('Destination location is required')
            .test('different-locations', 'Origin and destination must be different',
                function(value) {
                    return value !== this.parent.originLocation;
                }
            ),
        date: Yup.date()
            .required('Date is required')
            .min(new Date(), 'Date cannot be in the past')
    });

    const handleSearch = (values, { setSubmitting }) => {
        setError('');
        setSearching(true);
        setSelectedRoute(null);
        setRoutes([]);

        RouteService.findRoutes(values.originLocation, values.destinationLocation, values.date)
            .then(response => {
                setRoutes(response.data);
                setSearching(false);
                setSubmitting(false);
            })
            .catch(error => {
                setError('Failed to search routes. ' + (error.response?.data?.message || error.message));
                setSearching(false);
                setSubmitting(false);
                console.error('Error searching routes:', error);
            });
    };

    const handleRouteSelect = (route) => {
        setSelectedRoute(route);
    };

    const handleCloseDetails = () => {
        setSelectedRoute(null);
    };

    // Helper function to format date as YYYY-MM-DD for the date input
    function formatDate(date) {
        const d = new Date(date);
        let month = '' + (d.getMonth() + 1);
        let day = '' + d.getDate();
        const year = d.getFullYear();

        if (month.length < 2) month = '0' + month;
        if (day.length < 2) day = '0' + day;

        return [year, month, day].join('-');
    }

    return (
        <div className="route-search-container">
            <Row>
                <Col md={selectedRoute ? 8 : 12}>
                    <Card className="mb-4">
                        <Card.Header>
                            <h4>Search Routes</h4>
                        </Card.Header>
                        <Card.Body>
                            {error && <Alert variant="danger">{error}</Alert>}

                            {loading ? (
                                <div className="text-center my-3">
                                    <Spinner animation="border" role="status">
                                        <span className="visually-hidden">Loading...</span>
                                    </Spinner>
                                </div>
                            ) : (
                                <Formik
                                    initialValues={initialValues}
                                    validationSchema={validationSchema}
                                    onSubmit={handleSearch}
                                >
                                    {({
                                          values,
                                          errors,
                                          touched,
                                          handleChange,
                                          handleBlur,
                                          handleSubmit,
                                          isSubmitting,
                                      }) => (
                                        <Form onSubmit={handleSubmit}>
                                            <Row>
                                                <Col md={4}>
                                                    <Form.Group className="mb-3">
                                                        <Form.Label>From</Form.Label>
                                                        <Form.Select
                                                            name="originLocation"
                                                            value={values.originLocation}
                                                            onChange={handleChange}
                                                            onBlur={handleBlur}
                                                            isInvalid={touched.originLocation && !!errors.originLocation}
                                                        >
                                                            <option value="">Select Origin</option>
                                                            {locations.map(location => (
                                                                <option key={location.id} value={location.id}>
                                                                    {location.name} ({location.code}) - {location.city}
                                                                </option>
                                                            ))}
                                                        </Form.Select>
                                                        <Form.Control.Feedback type="invalid">
                                                            {errors.originLocation}
                                                        </Form.Control.Feedback>
                                                    </Form.Group>
                                                </Col>

                                                <Col md={4}>
                                                    <Form.Group className="mb-3">
                                                        <Form.Label>To</Form.Label>
                                                        <Form.Select
                                                            name="destinationLocation"
                                                            value={values.destinationLocation}
                                                            onChange={handleChange}
                                                            onBlur={handleBlur}
                                                            isInvalid={touched.destinationLocation && !!errors.destinationLocation}
                                                        >
                                                            <option value="">Select Destination</option>
                                                            {locations.map(location => (
                                                                <option key={location.id} value={location.id}>
                                                                    {location.name} ({location.code}) - {location.city}
                                                                </option>
                                                            ))}
                                                        </Form.Select>
                                                        <Form.Control.Feedback type="invalid">
                                                            {errors.destinationLocation}
                                                        </Form.Control.Feedback>
                                                    </Form.Group>
                                                </Col>

                                                <Col md={3}>
                                                    <Form.Group className="mb-3">
                                                        <Form.Label>Date</Form.Label>
                                                        <Form.Control
                                                            type="date"
                                                            name="date"
                                                            value={values.date}
                                                            onChange={handleChange}
                                                            onBlur={handleBlur}
                                                            isInvalid={touched.date && !!errors.date}
                                                        />
                                                        <Form.Control.Feedback type="invalid">
                                                            {errors.date}
                                                        </Form.Control.Feedback>
                                                    </Form.Group>
                                                </Col>

                                                <Col md={1} className="d-flex align-items-end">
                                                    <Button
                                                        variant="primary"
                                                        type="submit"
                                                        className="mb-3 w-100"
                                                        disabled={isSubmitting || searching}
                                                    >
                                                        {searching ? (
                                                            <Spinner
                                                                as="span"
                                                                animation="border"
                                                                size="sm"
                                                                role="status"
                                                                aria-hidden="true"
                                                            />
                                                        ) : 'Search'}
                                                    </Button>
                                                </Col>
                                            </Row>
                                        </Form>
                                    )}
                                </Formik>
                            )}
                        </Card.Body>
                    </Card>

                    {/* Route Results */}
                    {routes.length > 0 && (
                        <RouteResults
                            routes={routes}
                            locations={locations}
                            onRouteSelect={handleRouteSelect}
                            selectedRoute={selectedRoute}
                        />
                    )}

                    {routes.length === 0 && searching && (
                        <div className="text-center my-5">
                            <Spinner animation="border" role="status">
                                <span className="visually-hidden">Searching routes...</span>
                            </Spinner>
                            <p className="mt-2">Searching for available routes...</p>
                        </div>
                    )}

                    {routes.length === 0 && !searching && !error && !loading && (
                        <Card>
                            <Card.Body className="text-center text-muted py-5">
                                <h5>No routes found</h5>
                                <p>Please search for routes using the form above.</p>
                            </Card.Body>
                        </Card>
                    )}
                </Col>

                {/* Route Details */}
                {selectedRoute && (
                    <Col md={4}>
                        <RouteDetails
                            route={selectedRoute}
                            locations={locations}
                            onClose={handleCloseDetails}
                        />
                    </Col>
                )}
            </Row>
        </div>
    );
};

export default RouteSearch;
