import React, {useEffect, useState} from 'react';
import {useNavigate, useParams} from 'react-router-dom';
import {Alert, Button, Card, Form, Spinner} from 'react-bootstrap';
import {Formik} from 'formik';
import * as Yup from 'yup';
import TransportationService from '../../services/TransportationService';
import LocationService from '../../services/LocationService';

const TransportationForm = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [loading, setLoading] = useState(false);
    const [initialLoading, setInitialLoading] = useState(false);
    const [error, setError] = useState('');
    const [locations, setLocations] = useState([]);
    const [initialValues, setInitialValues] = useState({
        originId: null,
        destinationId: null,
        type: 'FLIGHT',
        operatingDays: [1, 2, 3, 4, 5]  // Default to weekdays
    });

    const isAddMode = !id;
    const transportationTypes = ['FLIGHT', 'BUS', 'SUBWAY', 'UBER'];
    const days = [
        { value: 1, label: 'Monday' },
        { value: 2, label: 'Tuesday' },
        { value: 3, label: 'Wednesday' },
        { value: 4, label: 'Thursday' },
        { value: 5, label: 'Friday' },
        { value: 6, label: 'Saturday' },
        { value: 7, label: 'Sunday' }
    ];

    useEffect(() => {
        // Fetch locations
        LocationService.getAll()
            .then(response => {
                setLocations(response.data);
            })
            .catch(error => {
                setError('Failed to fetch locations. ' + (error.response?.data?.message || error.message));
                console.error('Error fetching locations:', error);
            });

        // If in edit mode, fetch transportation details
        if (!isAddMode) {
            setInitialLoading(true);
            TransportationService.get(id)
                .then(response => {
                    const transportation = response.data;
                    setInitialValues({
                        originId: transportation.originId,
                        destinationId: transportation.destinationId,
                        type: transportation.type,
                        operatingDays: transportation.operatingDays
                    });
                    setInitialLoading(false);
                })
                .catch(error => {
                    setError('Failed to fetch transportation details. ' + (error.response?.data?.message || error.message));
                    setInitialLoading(false);
                    console.error('Error fetching transportation:', error);
                });
        }
    }, [id, isAddMode]);

    const validationSchema = Yup.object().shape({
        originId: Yup.string().required('Origin location is required'),
        destinationId: Yup.string()
            .required('Destination location is required')
            .test('different-locations', 'Origin and destination must be different',
                function(value) {
                    return value !== this.parent.originId;
                }
            ),
        type: Yup.string()
            .required('Transportation type is required')
            .oneOf(transportationTypes, 'Invalid transportation type'),
        operatingDays: Yup.array()
            .min(1, 'At least one operating day must be selected')
            .of(Yup.number().min(1).max(7))
    });

    const handleSubmit = (values, { setSubmitting }) => {
        setError('');
        setLoading(true);

        const transportation = {
            originId: values.originId,
            destinationId: values.destinationId,
            type: values.type,
            operatingDays: values.operatingDays
        };

        if (isAddMode) {
            // Create mode
            TransportationService.create(transportation)
                .then(() => {
                    navigate('/transportations');
                })
                .catch(error => {
                    setError('Failed to create transportation. ' + (error.response?.data?.message || error.message));
                    setLoading(false);
                    setSubmitting(false);
                    console.error('Error creating transportation:', error);
                });
        } else {
            // Edit mode
            TransportationService.update(id, transportation)
                .then(() => {
                    navigate('/transportations');
                })
                .catch(error => {
                    setError('Failed to update transportation. ' + (error.response?.data?.message || error.message));
                    setLoading(false);
                    setSubmitting(false);
                    console.error('Error updating transportation:', error);
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
                <h4>{isAddMode ? 'Add Transportation' : 'Edit Transportation'}</h4>
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
                          setFieldValue
                      }) => (
                        <Form onSubmit={handleSubmit}>
                            <Form.Group className="mb-3">
                                <Form.Label>Origin Location</Form.Label>
                                <Form.Select
                                    name="originLocation"
                                    value={values.originId}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    isInvalid={touched.originId && !!errors.originId}
                                >
                                    <option value="">Select Origin Location</option>
                                    {locations.map(location => (
                                        <option key={location.id} value={location.id}>
                                            {location.name} ({location.code}) - {location.city}, {location.country}
                                        </option>
                                    ))}
                                </Form.Select>
                                <Form.Control.Feedback type="invalid">
                                    {errors.originId}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Destination Location</Form.Label>
                                <Form.Select
                                    name="destinationLocation"
                                    value={values.destionationId}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    isInvalid={touched.destionationId && !!errors.destionationId}
                                >
                                    <option value="">Select Destination Location</option>
                                    {locations.map(location => (
                                        <option key={location.id} value={location.id}>
                                            {location.name} ({location.code}) - {location.city}, {location.country}
                                        </option>
                                    ))}
                                </Form.Select>
                                <Form.Control.Feedback type="invalid">
                                    {errors.destionationId}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Transportation Type</Form.Label>
                                <Form.Select
                                    name="transportationType"
                                    value={values.type}
                                    onChange={handleChange}
                                    onBlur={handleBlur}
                                    isInvalid={touched.type && !!errors.type}
                                >
                                    {transportationTypes.map(type => (
                                        <option key={type} value={type}>
                                            {type}
                                        </option>
                                    ))}
                                </Form.Select>
                                <Form.Control.Feedback type="invalid">
                                    {errors.type}
                                </Form.Control.Feedback>
                            </Form.Group>

                            <Form.Group className="mb-3">
                                <Form.Label>Operating Days</Form.Label>
                                {days.map(day => (
                                    <Form.Check
                                        key={day.value}
                                        type="checkbox"
                                        id={`day-${day.value}`}
                                        label={day.label}
                                        checked={values.operatingDays.includes(day.value)}
                                        onChange={e => {
                                            const isChecked = e.target.checked;
                                            const newOperatingDays = isChecked
                                                ? [...values.operatingDays, day.value]
                                                : values.operatingDays.filter(d => d !== day.value);

                                            setFieldValue('operatingDays', newOperatingDays);
                                        }}
                                    />
                                ))}
                                {touched.operatingDays && errors.operatingDays && (
                                    <div className="text-danger mt-1">{errors.operatingDays}</div>
                                )}
                            </Form.Group>

                            <div className="d-flex justify-content-end gap-2">
                                <Button
                                    variant="secondary"
                                    onClick={() => navigate('/transportations')}
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

export default TransportationForm;
