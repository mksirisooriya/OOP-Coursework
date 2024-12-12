// src/components/Dashboard.js
import React, { useState, useEffect } from 'react';
import {
  Grid,
  Paper,
  Typography,
  Button,
  CircularProgress,
} from '@mui/material';
import ConfigurationPanel from './ConfigurationPanel';
import ControlPanel from './ControlPanel';
import TicketPool from './TicketPool';
import SystemLogs from './SystemLogs';
import { toast } from 'react-toastify';
import axios from 'axios';

const API_URL = 'http://localhost:8080/api';
let vendorIntervals = [];
let customerIntervals = [];

function Dashboard() {
  const [systemStatus, setSystemStatus] = useState('stopped');
  const [ticketStatus, setTicketStatus] = useState({
    availableTickets: 0,
    totalTickets: 0,
    remainingTickets: 0,
    soldTickets: 0
  });
  const [configuration, setConfiguration] = useState(null);
  const [loading, setLoading] = useState(false);

  useEffect(() => {
    fetchConfiguration();
    const statusInterval = setInterval(fetchTicketStatus, 1000);
    return () => {
      clearInterval(statusInterval);
      stopAllOperations();
    };
  }, []);

  const fetchConfiguration = async () => {
    try {
      const response = await axios.get(`${API_URL}/configuration`);
      setConfiguration(response.data);
    } catch (error) {
      console.error('Error fetching configuration:', error);
    }
  };

  const fetchTicketStatus = async () => {
    try {
      const response = await axios.get(`${API_URL}/tickets/status`);
      setTicketStatus(response.data);
    } catch (error) {
      console.error('Error fetching ticket status:', error);
    }
  };

  const startVendorOperations = () => {
    for (let i = 1; i <= 3; i++) {
      const interval = setInterval(async () => {
        try {
          await axios.post(`${API_URL}/tickets/vendor/${i}`);
        } catch (error) {
          console.error(`Vendor ${i} error:`, error);
        }
      }, configuration.ticketReleaseRate * 1000);
      vendorIntervals.push(interval);
    }
  };

  const startCustomerOperations = () => {
    for (let i = 1; i <= 5; i++) {
      const interval = setInterval(async () => {
        try {
          await axios.post(`${API_URL}/tickets/customer/${i}`);
        } catch (error) {
          console.error(`Customer ${i} error:`, error);
        }
      }, configuration.customerRetrievalRate * 1000);
      customerIntervals.push(interval);
    }
  };

  const stopAllOperations = () => {
    vendorIntervals.forEach(clearInterval);
    customerIntervals.forEach(clearInterval);
    vendorIntervals = [];
    customerIntervals = [];
  };

  const handleStart = async () => {
    if (!configuration) {
      toast.error('Please configure the system first');
      return;
    }
    setLoading(true);
    try {
      setSystemStatus('running');
      startVendorOperations();
      startCustomerOperations();
      toast.success('System started successfully');
    } catch (error) {
      toast.error('Error starting system');
      console.error('Error:', error);
    }
    setLoading(false);
  };

  const handleStop = async () => {
    setLoading(true);
    try {
      stopAllOperations();
      setSystemStatus('stopped');
      toast.info('System stopped');
    } catch (error) {
      toast.error('Error stopping system');
      console.error('Error:', error);
    }
    setLoading(false);
  };

  const handleConfigurationSave = async (config) => {
    setLoading(true);
    try {
      const response = await axios.post(`${API_URL}/configuration`, config);
      setConfiguration(response.data);
      toast.success('Configuration saved successfully');
    } catch (error) {
      toast.error('Error saving configuration');
      console.error('Error:', error);
    }
    setLoading(false);
  };

  return (
    <div className="dashboard">
      {loading && (
        <CircularProgress
          style={{
            position: 'fixed',
            top: '50%',
            left: '50%',
            transform: 'translate(-50%, -50%)',
          }}
        />
      )}
      <Grid container spacing={3}>
        <Grid item xs={12}>
          <Typography variant="h4" gutterBottom>
            Event Ticketing System Dashboard
          </Typography>
        </Grid>
        <Grid item xs={12} md={6}>
          <ConfigurationPanel
            configuration={configuration}
            onSave={handleConfigurationSave}
            disabled={systemStatus === 'running'}
          />
        </Grid>
        <Grid item xs={12} md={6}>
          <ControlPanel
            status={systemStatus}
            onStart={handleStart}
            onStop={handleStop}
          />
        </Grid>
        <Grid item xs={12}>
          <TicketPool status={ticketStatus} />
        </Grid>
        <Grid item xs={12}>
          <SystemLogs systemStatus={systemStatus} />
        </Grid>
      </Grid>
    </div>
  );
}

export default Dashboard;