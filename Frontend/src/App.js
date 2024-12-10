import './App.css';
import Navbar from './component/Navbar';
import { BrowserRouter as Router, Route, Routes } from 'react-router-dom'; // Import React Router components
import SystemConfig from './component/SystemConfig'; // Import your SystemConfig component
import SalesLogs from './component/SalesLogs'; // Import your SalesLogs component


function App() {
  return (
    <Router>
      <div className="App">
        <Navbar /> {/* Render the Navbar component */}
        <Routes>
          {/* Define routes for the SystemConfig and SalesLogs pages */}
          <Route path="/" element={<SystemConfig />} /> {/* Default route */}
          <Route path="/system-config" element={<SystemConfig />} />
          <Route path="/sales-logs" element={<SalesLogs />} />
        </Routes>
      </div>
    </Router>
  );
}

export default App;
