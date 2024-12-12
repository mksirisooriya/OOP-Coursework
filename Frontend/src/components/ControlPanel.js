import React from 'react';
import {
  Paper,
  Typography,
  Button,
  Grid,
  Box,
  Chip
} from '@mui/material';
import PlayArrowIcon from '@mui/icons-material/PlayArrow';
import StopIcon from '@mui/icons-material/Stop';

function ControlPanel({ status, onStart, onStop }) {
  return (
    <Paper className="control-panel">
      <Typography variant="h6" gutterBottom>
        System Control
      </Typography>
      <Box mb={2}>
        <Chip
          label={`Status: ${status.toUpperCase()}`}
          color={status === 'running' ? 'success' : 'default'}
          variant="outlined"
        />
      </Box>
      <Grid container spacing={2}>
        <Grid item xs={6}>
          <Button
            variant="contained"
            color="primary"
            fullWidth
            startIcon={<PlayArrowIcon />}
            onClick={onStart}
            disabled={status === 'running'}
          >
            Start System
          </Button>
        </Grid>
        <Grid item xs={6}>
          <Button
            variant="contained"
            color="secondary"
            fullWidth
            startIcon={<StopIcon />}
            onClick={onStop}
            disabled={status === 'stopped'}
          >
            Stop System
          </Button>
        </Grid>
      </Grid>
    </Paper>
  );
}

export default ControlPanel;
