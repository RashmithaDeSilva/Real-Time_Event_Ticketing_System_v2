import React from 'react';
import { Link } from 'react-router-dom'; // Import Link from React Router

const Navbar = () => {
  return (
    <nav className="navbar navbar-expand-lg navbar-light bg-light">
      <div className="collapse navbar-collapse">
        <ul className="navbar-nav mx-auto">
          <li className="nav-item h4">
            <Link className="nav-link" to="/system-config">System config</Link>
          </li>
          <li className="nav-item h4">
            <Link className="nav-link" to="/sales-logs">Sales logs</Link>
          </li>
        </ul>
      </div>
    </nav>
  );
};

export default Navbar;
