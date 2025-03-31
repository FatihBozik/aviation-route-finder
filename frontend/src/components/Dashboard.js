import React from 'react';
import {NavLink, Outlet, useNavigate} from 'react-router-dom';
import {Button, Col, Container, Nav, Navbar, Row} from 'react-bootstrap';
import {FontAwesomeIcon} from '@fortawesome/react-fontawesome';
import {faMapMarked, faPlane, faRoute, faSignOutAlt} from '@fortawesome/free-solid-svg-icons';
import AuthService from '../services/AuthService';

const Dashboard = () => {
    const navigate = useNavigate();
    const isAdmin = AuthService.isAdmin();
    const currentUser = AuthService.getCurrentUser();

    const handleLogout = () => {
        AuthService.logout();
        navigate('/login');
    };

    return (
        <div className="dashboard-container">
            {/* Header */}
            <Navbar bg="primary" variant="dark" expand="lg">
                <Container fluid>
                    <Navbar.Brand href="/">
                        <img
                            src="/thy-logo.png"
                            alt="THY Logo"
                            height="30"
                            className="d-inline-block align-top me-2"
                        />
                        Aviation Routes System
                    </Navbar.Brand>
                    <Navbar.Toggle aria-controls="basic-navbar-nav" />
                    <Navbar.Collapse id="basic-navbar-nav" className="justify-content-end">
                        <Nav>
                            <Navbar.Text className="me-3">
                                Signed in as: <strong>{currentUser?.username}</strong>
                            </Navbar.Text>
                            <Button variant="outline-light" onClick={handleLogout}>
                                <FontAwesomeIcon icon={faSignOutAlt} className="me-2" />
                                Logout
                            </Button>
                        </Nav>
                    </Navbar.Collapse>
                </Container>
            </Navbar>

            <Container fluid>
                <Row>
                    {/* Sidebar */}
                    <Col md={2} className="bg-light sidebar">
                        <Nav className="flex-column pt-3">
                            {isAdmin && (
                                <>
                                    <Nav.Item>
                                        <Nav.Link as={NavLink} to="/locations" className="sidebar-link">
                                            <FontAwesomeIcon icon={faMapMarked} className="me-2" />
                                            Locations
                                        </Nav.Link>
                                    </Nav.Item>
                                    <Nav.Item>
                                        <Nav.Link as={NavLink} to="/transportations" className="sidebar-link">
                                            <FontAwesomeIcon icon={faPlane} className="me-2" />
                                            Transportations
                                        </Nav.Link>
                                    </Nav.Item>
                                </>
                            )}
                            <Nav.Item>
                                <Nav.Link as={NavLink} to="/routes" className="sidebar-link">
                                    <FontAwesomeIcon icon={faRoute} className="me-2" />
                                    Routes
                                </Nav.Link>
                            </Nav.Item>
                        </Nav>
                    </Col>

                    {/* Main Content */}
                    <Col md={10} className="main-content py-3">
                        <Outlet />
                    </Col>
                </Row>
            </Container>
        </div>
    );
};

export default Dashboard;
