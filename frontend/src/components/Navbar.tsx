import React, { useState } from 'react';
import SystemConfigs from './SystemConfigs';
import SalesLog from './SalsLog';


function Navbar() {
  const [activeComponent, setActiveComponent] = useState('SystemConfigs');

  const renderComponent = () => {
    switch (activeComponent) {
      case 'SystemConfigs':
        return <SystemConfigs />;
      case 'SalesLog':
        return <SalesLog />;
      default:
        return <SystemConfigs />;
    }
  };


  return (
    <>
      <nav className="navbar navbar-expand-lg navbar-dark bg-dark">
        <div className="container">
          <a className="navbar-brand" href="#">
          Real-Time Event Ticketing System
          </a>
          <button
            className="navbar-toggler"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#navbarNav"
            aria-controls="navbarNav"
            aria-expanded="false"
            aria-label="Toggle navigation"
          >
            <span className="navbar-toggler-icon"></span>
          </button>
          <div className="collapse navbar-collapse" id="navbarNav">
            <ul className="navbar-nav">
              <li className="nav-item">
                <a
                  className={`nav-link ${
                    activeComponent === 'SystemConfigs' ? 'active' : ''
                  }`}
                  href="#"
                  onClick={() => setActiveComponent('SystemConfigs')}
                >
                  System Configurations
                </a>
              </li>
              <li className="nav-item">
                <a
                  className={`nav-link ${
                    activeComponent === 'SalesLog' ? 'active' : ''
                  }`}
                  href="#"
                  onClick={() => setActiveComponent('SalesLog')}
                >
                  Sales Log
                </a>
              </li>
            </ul>
          </div>
        </div>
      </nav>
      <div className="container mt-4">{renderComponent()}</div>
    </>
  );
}

export default Navbar;
