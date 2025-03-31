import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {Alert, Button, Card, Form, Spinner} from 'react-bootstrap';
import {Formik} from 'formik';
import * as Yup from 'yup';
import LocationService from '../../services/LocationService';

const LocationForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [initialLoading, setInitialLoading] = useState(false);
    const [error, setError] = useState('');
    const [initialValues, setInitialValues] = useState({
        name: '',
        country: '',
        city: '',
        code: ''
    });

    const isAddMode = !id;

    useEffect(() => {
        if (!isAddMode) {
            setInitialLoading(true);
            LocationService.get(id)
                .then(response => {
                    const location = response.data;
                    setInitialValues({
                        name: location.name,
                        country: location.country,
                        city: location.city,
                        code: location.code
                    });
                    setInitialLoading(false);
                })
                .catch(error => {
                    setError('Failed to fetch location details. ' + (error.response?.data?.message || error.message));
                    setInitialLoading(false);
                    console.error('Error fetching location:', error);
                });
        }
    }, [id, isAddMode]);

    const validationSchema = Yup.object().shape({
        name: Yup.string().required('Name is required'),
        country: Yup.string().required('Country is required'),
        city: Yup.string().required('City is required'),
        code: Yup.string()
            .required('Location code is required')
            .matches(/^[A-Z0-9]{3,6}$/, 'Location code must be 3-6 uppercase letters or numbers')
    });

    const handleSubmit = (values, { setSubmitting }) => {
        setError('');
        setLoading(true);

        const location = {
            name: values.name,
            country: values.country,
            city: values.city,
            code: values.code
        };

        if (isAddMode) {
            // Create mode
            LocationService.create(location)
                .then(() => {
                    navigate('/locations');
                })
                .catch(error => {
                    setError('Failed to create location. ' + (error.response?.data?.message || error.message));
                    setLoading(false);
                    setSubmitting(false);
                    console.error('Error creating location:', error);
                });
        } else {
            // Edit mode
            LocationService.update(id, location)
                .then(() => {
                    navigate('/locations');
                })
                .catch(error => {
                    setError('Failed to update location. ' + (error.response?.data?.message || error.message));
                    setLoading(false);
                    setSubmitting(false);
                    console.error('Error updating location:', error);
                });
        }
    };

    if (initialLoading) {
        return (
            <div className="text-center my-5">
                <Spinner animation="border" role="status">
                    <span className="visually-hidden">Loading...</span>
                </Spinner>
            </div>
        );
    }

    return (
        <Card>
            <Card.Header>
                <h4>{isAddMode ? 'Add Location' : 'Edit Location'}</h4>
            </Card.Header>
            <Card.Body>
                {error && <Alert variant="danger">{error}</Alert>}

                <Formik
                    initialValues={initialValues}
                    validationSchema={validationSchema}
                    onSubmit={handleSubmit}
                    enableReinitialize
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
                            <Form.Group className="mb-3">
                                <Form.Label>Name</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="name"
                                    value={values.name}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    isInvalid={touched.name && !!errors.name}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.name}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Country</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="country"
                                    value={values.country}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    isInvalid={touched.country && !!errors.country}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.country}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>City</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="city"
                                    value={values.city}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    isInvalid={touched.city && !!errors.city}
                                />
                                <Form.Control.Feedback type="invalid">
                                    {errors.city}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Location Code</Form.Label>
                                <Form.Control
                                    type="text"
                                    name="code"
                                    value={values.code}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    isInvalid={touched.code && !!errors.code}
                                />
                                <Form.Text className="text-muted">
                                    For airports, use the 3-letter IATA code. For other locations, use a custom 3-6 character code.
                                </Form.Text>
                                <Form.Control.Feedback type="invalid">
                                    {errors.code}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <div className="d-flex justify-content-end gap-2">
                                <Button
                                    variant="secondary"
                                    onClick={() => navigate('/locations')}
                                >
                                    Cancel
                                </Button>
                                <Button
                                    variant="primary"
                                    type="submit"
                                    disabled={isSubmitting || loading}
                                >
                                    {loading ? 'Saving...' : 'Save'}
                                </Button>
                            </div>
                        </Form>
                    )}
                </Formik>
            </Card.Body>
        </Card>
    );
};

export default LocationForm;
