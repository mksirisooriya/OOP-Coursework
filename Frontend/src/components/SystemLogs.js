import React, { useState, useEffect, useRef } from 'react';
import {
  Paper,
  Typography,
  List,
  ListItem,
  ListItemText,
  Chip,
  Box,
  Button,
  ButtonGroup,
  IconButton,
  Tooltip
} from '@mui/material';
import VerticalAlignBottomIcon from '@mui/icons-material/VerticalAlignBottom';
import axios from 'axios';

function SystemLogs({ systemStatus }) {
  const [logs, setLogs] = useState([]);
  const [filter, setFilter] = useState('ALL');
  const [autoScroll, setAutoScroll] = useState(false);
  const logsEndRef = useRef(null);
  const prevLogsLength = useRef(logs.length);

  const scrollToBottom = () => {
    logsEndRef.current?.scrollIntoView({ behavior: 'smooth' });
  };

  useEffect(() => {
    const fetchLogs = async () => {
      try {
        const response = await axios.get(`http://localhost:8080/api/logs`);
        setLogs(response.data);
        
        // Only scroll if new logs were added and autoScroll is enabled
        if (response.data.length > prevLogsLength.current && autoScroll) {
          scrollToBottom();
        }
        prevLogsLength.current = response.data.length;
      } catch (error) {
        console.error('Error fetching logs:', error);
      }
    };

    fetchLogs();
    const interval = setInterval(fetchLogs, 1000);
    return () => clearInterval(interval);
  }, [systemStatus, autoScroll]);

  const getLogColor = (type) => {
    switch (type) {
      case 'VENDOR':
        return '#2196f3';
      case 'CUSTOMER':
        return '#4caf50';
      case 'SYSTEM':
        return '#f50057';
      default:
        return '#666666';
    }
  };

  const filteredLogs = logs.filter(log => 
    filter === 'ALL' || log.eventType === filter
  );

  return (
    <Paper className="system-logs">
      <Box display="flex" justifyContent="space-between" alignItems="center" mb={2}>
        <Typography variant="h6">
          System Logs
        </Typography>
        <Box display="flex" alignItems="center" gap={2}>
          <Tooltip title={autoScroll ? "Auto-scroll enabled" : "Auto-scroll disabled"}>
            <Button
              size="small"
              variant={autoScroll ? "contained" : "outlined"}
              onClick={() => setAutoScroll(!autoScroll)}
            >
              Auto-scroll
            </Button>
          </Tooltip>
          <Tooltip title="Scroll to bottom">
            <IconButton onClick={scrollToBottom} size="small">
              <VerticalAlignBottomIcon />
            </IconButton>
          </Tooltip>
          <ButtonGroup size="small">
            {['ALL', 'SYSTEM', 'VENDOR', 'CUSTOMER'].map((type) => (
              <Button
                key={type}
                variant={filter === type ? 'contained' : 'outlined'}
                onClick={() => setFilter(type)}
              >
                {type}
              </Button>
            ))}
          </ButtonGroup>
        </Box>
      </Box>
      <List style={{ maxHeight: '400px', overflow: 'auto' }}>
        {filteredLogs.map((log, index) => (
          <ListItem key={index} divider>
            <ListItemText
              primary={
                <Box display="flex" alignItems="center" gap={1}>
                  <Chip
                    label={log.eventType}
                    size="small"
                    style={{
                      backgroundColor: getLogColor(log.eventType),
                      color: 'white'
                    }}
                  />
                  <Typography variant="body2">
                    {log.message}
                  </Typography>
                </Box>
              }
              secondary={new Date(log.timestamp).toLocaleString()}
            />
          </ListItem>
        ))}
        <div ref={logsEndRef} />
      </List>
    </Paper>
  );
}

export default SystemLogs;