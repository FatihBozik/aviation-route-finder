import React from 'react';
import {Button, Card, ListGroup} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {
    faArrowRight,
    faBus,
    faCar,
    faMapMarkerAlt,
    faPlane,
    faSubway,
    faTimes
} from '@fortawesome/free-solid-svg-icons';

const RouteDetails = ({route, locations, onClose}) => {
    // Create a map of location ids to location objects
    const locationMap = locations.reduce((map, location) => {
        map[location.id] = location;
        return map;
    }, {});

    const getLocationDetails = (locationId) => {
        const location = locationMap[locationId];
        if (!location) return {name: 'Unknown Location', code: '???', city: '', country: ''};

        return {
            name: location.name,
            code: location.code,
            city: location.city,
            country: location.country
        };
    };

    const getTransportationIcon = (type) => {
        switch (type) {
            case 'FLIGHT':
                return <FontAwesomeIcon icon={faPlane}/>;
            case 'BUS':
                return <FontAwesomeIcon icon={faBus}/>;
            case 'SUBWAY':
                return <FontAwesomeIcon icon={faSubway}/>;
            case 'UBER':
                return <FontAwesomeIcon icon={faCar}/>;
            default:
                return null;
        }
    };

    // Get origin and destination details
    const originDetails = getLocationDetails(route.origin);
    const destinationDetails = getLocationDetails(route.destination);

    return (
        <Card className="route-details">
            <Card.Header className="d-flex justify-content-between align-items-center">
                <h5 className="mb-0">Route Details</h5>
                <Button variant="outline-secondary" size="sm" onClick={onClose}>
                    <FontAwesomeIcon icon={faTimes}/>
                </Button>
            </Card.Header>
            <Card.Body>
                <div className="route-header mb-4">
                    <div className="d-flex justify-content-between align-items-center mb-3">
                        <div className="text-center">
                            <h6 className="mb-0">{originDetails.code}</h6>
                            <div className="small text-muted">{originDetails.city}</div>
                        </div>
                        <div className="flex-grow-1 px-3 text-center">
                            <FontAwesomeIcon icon={faArrowRight}/>
                        </div>
                        <div className="text-center">
                            <h6 className="mb-0">{destinationDetails.code}</h6>
                            <div className="small text-muted">{destinationDetails.city}</div>
                        </div>
                    </div>
                    <div className="d-flex justify-content-between">
                        <div className="text-center">
                            <div className="fw-bold">{originDetails.name}</div>
                            <div className="small">{originDetails.country}</div>
                        </div>
                        <div className="text-center">
                            <div className="fw-bold">{destinationDetails.name}</div>
                            <div className="small">{destinationDetails.country}</div>
                        </div>
                    </div>
                </div>

                <h6 className="mb-3">Journey Details</h6>

                <ListGroup className="mb-3">
                    {route.transportations.map((transportation, index) => {
                        const fromLocation = getLocationDetails(transportation.originId);
                        const toLocation = getLocationDetails(transportation.destinationId);

                        return (
                            <ListGroup.Item key={index}>
                                <div className="d-flex align-items-center mb-2">
                                    <div className="me-2">
                                        {getTransportationIcon(transportation.type)}
                                    </div>
                                    <div className="fw-bold">
                                        {transportation.type}
                                    </div>
                                </div>

                                <div className="ms-4">
                                    <div className="d-flex mb-1">
                                        <div className="me-2">
                                            <FontAwesomeIcon icon={faMapMarkerAlt} className="text-primary"/>
                                        </div>
                                        <div>
                                            <div>{fromLocation.name}</div>
                                            <div
                                                className="small text-muted">{fromLocation.city}, {fromLocation.country}</div>
                                        </div>
                                    </div>

                                    <div className="border-start border-2 ms-2 ps-3 my-2"
                                         style={{height: '20px'}}></div>

                                    <div className="d-flex">
                                        <div className="me-2">
                                            <FontAwesomeIcon icon={faMapMarkerAlt} className="text-danger"/>
                                        </div>
                                        <div>
                                            <div>{toLocation.name}</div>
                                            <div
                                                className="small text-muted">{toLocation.city}, {toLocation.country}</div>
                                        </div>
                                    </div>
                                </div>
                            </ListGroup.Item>
                        );
                    })}
                </ListGroup>

                {/* Optional: Add a "View on Map" button for the "Nice-to-Have" map feature */}
                <Button variant="outline-primary" className="w-100">
                    View on Map
                </Button>
            </Card.Body>
        </Card>
    );
};

export default RouteDetails;
