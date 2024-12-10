import React, { useState, useEffect } from "react";
// import "bootstrap/dist/css/bootstrap.min.css";

const SystemConfig = () => {
  const [cliStatus, setCliStatus] = useState(false);
  const [systemStatus, setSystemStatus] = useState(false);
  const [totalTickets, setTotalTickets] = useState(0);
  const [ticketReleaseRate, setTicketReleaseRate] = useState(0);
  const [customerRetrievalRate, setCustomerRetrievalRate] = useState(0);
  const [maxTicketCapacity, setMaxTicketCapacity] = useState(0);

  // Form states to avoid conflicts
  const [formTotalTickets, setFormTotalTickets] = useState(0);
  const [formTicketReleaseRate, setFormTicketReleaseRate] = useState(0);
  const [formCustomerRetrievalRate, setFormCustomerRetrievalRate] = useState(0);
  const [formMaxTicketCapacity, setFormMaxTicketCapacity] = useState(0);

  useEffect(() => {
    const fetchSystemConfig = async () => {
      try {
        const response = await fetch("http://localhost:8080/api/v1/systemconfigs/");
        if (response.ok) {
          const data = await response.json();
          setCliStatus(data.cli_status);
          setSystemStatus(data.system_status);
          setTotalTickets(data.total_tickets);
          setTicketReleaseRate(data.ticket_release_rate);
          setCustomerRetrievalRate(data.customer_retrieval_rate);
          setMaxTicketCapacity(data.max_ticket_capacity);

          // Update form states with fetched values only on the first fetch
          // setFormTotalTickets(data.total_tickets);
          // setFormTicketReleaseRate(data.ticket_release_rate);
          // setFormCustomerRetrievalRate(data.customer_retrieval_rate);
          // setFormMaxTicketCapacity(data.max_ticket_capacity);
        } else {
          console.error("Failed to fetch system configuration");
        }
      } catch (error) {
        console.error("Error fetching system configuration:", error);
      }
    };

    fetchSystemConfig(); // Initial fetch
    const intervalId = setInterval(fetchSystemConfig, 1000); // Fetch every 1 second

    return () => clearInterval(intervalId); // Cleanup on component unmount
  }, []);

  const handleUpdate = async (e) => {
    e.preventDefault();
    try {
      const response = await fetch("http://localhost:8080/api/v1/systemconfigs/update", {
        method: "PATCH",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          total_tickets: formTotalTickets,
          ticket_release_rate: formTicketReleaseRate,
          customer_retrieval_rate: formCustomerRetrievalRate,
          max_ticket_capacity: formMaxTicketCapacity,
        }),
      });

    console.log(formTotalTickets);
      if (response.ok) {
        console.log("System configuration updated successfully");
        // Optionally update the main state to reflect the new values
        setTotalTickets(formTotalTickets);
        setTicketReleaseRate(formTicketReleaseRate);
        setCustomerRetrievalRate(formCustomerRetrievalRate);
        setMaxTicketCapacity(formMaxTicketCapacity);
      } else {
        console.error("Failed to update system configuration");
      }
    } catch (error) {
      console.error("Error updating system configuration:", error);
    }
  };

  return (
    <div className="container mt-4">
      {/* Status */}
      <div className="row mb-4">
        <div className="col-md-6">
          <div
            className={`alert ${cliStatus ? "alert-success" : "alert-danger"}`}
          >
            CLI Status: {cliStatus ? "Running" : "Stopped"}
          </div>
        </div>
        <div className="col-md-6">
          <div
            className={`alert ${systemStatus ? "alert-success" : "alert-danger"}`}
          >
            System Status: {systemStatus ? "Running" : "Stopped"}
          </div>
        </div>
      </div>

      {/* Params */}
      <div className="row mb-4">
        <div className="col-md-3">
          <div className="card">
            <div className="card-body text-center">
              <h5>Total Tickets</h5>
              <p className="card-text">{totalTickets}</p>
            </div>
          </div>
        </div>
        <div className="col-md-3">
          <div className="card">
            <div className="card-body text-center">
              <h5>Ticket Release Rate (ms)</h5>
              <p className="card-text">{ticketReleaseRate}</p>
            </div>
          </div>
        </div>
        <div className="col-md-3">
          <div className="card">
            <div className="card-body text-center">
              <h5>Customer Retrieval Rate (ms)</h5>
              <p className="card-text">{customerRetrievalRate}</p>
            </div>
          </div>
        </div>
        <div className="col-md-3">
          <div className="card">
            <div className="card-body text-center">
              <h5>Max Ticket Capacity</h5>
              <p className="card-text">{maxTicketCapacity}</p>
            </div>
          </div>
        </div>
      </div>

      {/* Update form */}
      <form onSubmit={handleUpdate}>
        <div className="row mb-3">
          <div className="col-md-6">
            <label htmlFor="formTotalTickets" className="form-label">
              Total Tickets
            </label>
            <input
              type="number"
              id="formTotalTickets"
              className="form-control"
              value={formTotalTickets}
              onChange={(e) => setFormTotalTickets(parseInt(e.target.value, 10))}
            />
          </div>
          <div className="col-md-6">
            <label htmlFor="formTicketReleaseRate" className="form-label">
              Ticket Release Rate (ms)
            </label>
            <input
              type="number"
              id="formTicketReleaseRate"
              className="form-control"
              value={formTicketReleaseRate}
              onChange={(e) =>
                setFormTicketReleaseRate(parseInt(e.target.value, 10))
              }
            />
          </div>
        </div>
        <div className="row mb-3">
          <div className="col-md-6">
            <label htmlFor="formCustomerRetrievalRate" className="form-label">
              Customer Retrieval Rate (ms)
            </label>
            <input
              type="number"
              id="formCustomerRetrievalRate"
              className="form-control"
              value={formCustomerRetrievalRate}
              onChange={(e) =>
                setFormCustomerRetrievalRate(parseInt(e.target.value, 10))
              }
            />
          </div>
          <div className="col-md-6">
            <label htmlFor="formMaxTicketCapacity" className="form-label">
              Max Ticket Capacity
            </label>
            <input
              type="number"
              id="formMaxTicketCapacity"
              className="form-control"
              value={formMaxTicketCapacity}
              onChange={(e) =>
                setFormMaxTicketCapacity(parseInt(e.target.value, 10))
              }
            />
          </div>
        </div>
        <button type="submit" className="btn btn-primary">
          Update
        </button>
      </form>
    </div>
  );
};

export default SystemConfig;
