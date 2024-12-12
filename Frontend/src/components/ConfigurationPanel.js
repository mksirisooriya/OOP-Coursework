import React, { useState, useEffect } from 'react';
import {
  Paper,
  Typography,
  TextField,
  Button,
  Grid,
} from '@mui/material';

function ConfigurationPanel({ configuration, onSave, disabled }) {
  const [config, setConfig] = useState({
    totalTickets: '',
    ticketReleaseRate: '',
    customerRetrievalRate: '',
    maxTicketCapacity: ''
  });

  useEffect(() => {
    if (configuration) {
      setConfig(configuration);
    }
  }, [configuration]);

  const handleChange = (e) => {
    const { name, value } = e.target;
    setConfig(prev => ({
      ...prev,
      [name]: parseInt(value) || ''
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    if (validateConfig()) {
      onSave(config);
    }
  };

  const validateConfig = () => {
    if (config.totalTickets <= 0 || config.maxTicketCapacity <= 0 ||
        config.ticketReleaseRate <= 0 || config.customerRetrievalRate <= 0) {
      toast.error('All values must be greater than 0');
      return false;
    }
    if (config.maxTicketCapacity > config.totalTickets) {
      toast.error('Max capacity cannot be greater than total tickets');
      return false;
    }
    return true;
  };

  return (
    <Paper className="control-panel">
      <Typography variant="h6" gutterBottom>
        System Configuration
      </Typography>
      <form onSubmit={handleSubmit}>
        <Grid container spacing={2}>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Total Tickets"
              name="totalTickets"
              type="number"
              value={config.totalTickets}
              onChange={handleChange}
              disabled={disabled}
              required
              inputProps={{ min: 0, step: 1 }}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Max Ticket Capacity"
              name="maxTicketCapacity"
              type="number"
              value={config.maxTicketCapacity}
              onChange={handleChange}
              disabled={disabled}
              required
              inputProps={{ min: 0, step: 1 }}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Ticket Release Rate (seconds)"
              name="ticketReleaseRate"
              type="number"
              value={config.ticketReleaseRate}
              onChange={handleChange}
              disabled={disabled}
              required
              inputProps={{ min: 0, step: 1 }}
            />
          </Grid>
          <Grid item xs={12} sm={6}>
            <TextField
              fullWidth
              label="Customer Retrieval Rate (seconds)"
              name="customerRetrievalRate"
              type="number"
              value={config.customerRetrievalRate}
              onChange={handleChange}
              disabled={disabled}
              required
              inputProps={{ min: 0, step: 1 }}
            />
          </Grid>
          <Grid item xs={12}>
            <Button
              type="submit"
              variant="contained"
              color="primary"
              fullWidth
              disabled={disabled}
            >
              Save Configuration
            </Button>
          </Grid>
        </Grid>
      </form>
    </Paper>
  );
}

export default ConfigurationPanel;