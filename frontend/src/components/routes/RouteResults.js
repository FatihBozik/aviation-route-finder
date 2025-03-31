import React from 'react';
import {Badge, Card, Table} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faBus, faCar, faLongArrowAltRight, faPlane, faSubway} from '@fortawesome/free-solid-svg-icons';

const RouteResults = ({ routes, locations, onRouteSelect, selectedRoute }) => {
    // Create a map of location ids to location objects
    const locationMap = locations.reduce((map, location) => {
        map[location.id] = location;
        return map;
    }, {});

    const getLocationName = (locationId) => {
        return locationMap[locationId]?.name || 'Unknown Location';
    };

    const getLocationCode = (locationId) => {
        return locationMap[locationId]?.code || '???';
    };

    const getTransportationIcon = (type) => {
        switch (type) {
            case 'FLIGHT':
                return <FontAwesomeIcon icon={faPlane} />;
            case 'BUS':
                return <FontAwesomeIcon icon={faBus} />;
            case 'SUBWAY':
                return <FontAwesomeIcon icon={faSubway} />;
            case 'UBER':
                return <FontAwesomeIcon icon={faCar} />;
            default:
                return null;
        }
    };

    const getTransportationBadge = (type) => {
        const badgeVariant = {
            'FLIGHT': 'primary',
            'BUS': 'success',
            'SUBWAY': 'info',
            'UBER': 'warning'
        };

        return (
            <Badge bg={badgeVariant[type] || 'secondary'} className="me-1">
                {getTransportationIcon(type)} {type}
            </Badge>
        );
    };

    return (
        <Card>
            <Card.Header>
                <h4>Available Routes ({routes.length})</h4>
            </Card.Header>
            <Card.Body className="p-0">
                <Table hover responsive className="mb-0">
                    <thead>
                    <tr>
                        <th>Route</th>
                        <th>Transportation</th>
                    </tr>
                    </thead>
                    <tbody>
                    {routes.map((route, index) => (
                        <tr
                            key={index}
                            onClick={() => onRouteSelect(route)}
                            className={selectedRoute === route ? 'table-primary' : ''}
                            style={{ cursor: 'pointer' }}
                        >
                            <td>
                                <div className="d-flex align-items-center">
                                    <div>
                                        <div className="fw-bold">{getLocationCode(route.origin)} - {getLocationCode(route.destination)}</div>
                                        <div className="text-muted small">
                                            {getLocationName(route.origin)} <FontAwesomeIcon icon={faLongArrowAltRight} className="mx-1" /> {getLocationName(route.destination)}
                                        </div>
                                    </div>
                                </div>
                            </td>
                            <td>
                                {route.transportations.map((transport, idx) => (
                                    <div key={idx} className="mb-1">
                                        {getTransportationBadge(transport.type)}
                                    </div>
                                ))}
                            </td>
                        </tr>
                    ))}
                    </tbody>
                </Table>
            </Card.Body>
        </Card>
    );
};

export default RouteResults;
