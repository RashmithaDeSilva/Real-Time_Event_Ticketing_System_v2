// component/SalesLogs.js
import React, { useEffect, useState } from 'react';
import 'bootstrap/dist/css/bootstrap.min.css';
import axios from 'axios';

const SalesLogs = () => {
  const [logs, setLogs] = useState([]);
  const [page, setPage] = useState(0);
  const [isLastPage, setIsLastPage] = useState(false);

  useEffect(() => {
    fetchLogs(page);

    const interval = setInterval(() => {
      fetchLogs(page);
    }, 1000);

    return () => clearInterval(interval); // Cleanup on component unmount
  }, [page]);

  const fetchLogs = async (currentPage) => {
    try {
      const response = await axios.get(`http://localhost:8080/api/v1/saleslog/?page=${currentPage}&size=25`);
      const data = response.data;
      
      // Assuming API returns a fixed page size, check for last page condition.
      setIsLastPage(data.length < 25);
      setLogs(data);
    } catch (error) {
      console.error("Error fetching sales logs:", error);
    }
  };

  const handlePrevious = () => {
    if (page > 0) {
      setPage(page - 1);
    }
  };

  const handleNext = () => {
    if (!isLastPage) {
      setPage(page + 1);
    }
  };

  return (
    <div className="container mt-4">
      <h1 className="mb-4">Sales Logs</h1>
      <table className="table table-bordered table-striped">
        <thead className="thead-dark">
          <tr>
            <th>Date and Time</th>
            <th>Log</th>
          </tr>
        </thead>
        <tbody>
          {logs.length > 0 ? (
            logs.map((log, index) => (
              <tr key={index}>
                <td>{log.date_time}</td>
                <td>{log.log}</td>
              </tr>
            ))
          ) : (
            <tr>
              <td colSpan="2" className="text-center">No logs available</td>
            </tr>
          )}
        </tbody>
      </table>
      <div className="d-flex justify-content-evenly">
        <button 
          className="btn btn-primary" 
          onClick={handlePrevious} 
          disabled={page === 0}
        >
          Previous
        </button>
        <button 
          className="btn btn-primary" 
          onClick={handleNext} 
          disabled={isLastPage}
        >
          Next
        </button>
      </div>
    </div>
  );
};

export default SalesLogs;
