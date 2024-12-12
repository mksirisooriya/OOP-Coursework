import React from 'react';
import {
  Paper,
  Typography,
  Grid,
  Box
} from '@mui/material';
import { ResponsivePie } from '@nivo/pie';  // Changed this line

function TicketPool({ status }) {
  const pieData = [
    {
      id: 'Available',
      label: 'Available',
      value: status.availableTickets || 0,
      color: '#2196f3'
    },
    {
      id: 'Sold',
      label: 'Sold',
      value: status.soldTickets || 0,
      color: '#4caf50'
    },
    {
      id: 'Remaining',
      label: 'Remaining',
      value: status.remainingTickets || 0,
      color: '#ff9800'
    }
  ];

  return (
    <Paper className="ticket-pool">
      <Typography variant="h6" gutterBottom>
        Ticket Pool Status
      </Typography>
      <Grid container spacing={3}>
        <Grid item xs={12} md={8}>
          <Box height={300}>
            <ResponsivePie
              data={pieData}
              margin={{ top: 40, right: 80, bottom: 80, left: 80 }}
              innerRadius={0.5}
              padAngle={0.7}
              cornerRadius={3}
              activeOuterRadiusOffset={8}
              colors={{ scheme: 'paired' }}
              borderWidth={1}
              borderColor={{ theme: 'background' }}
              enableArcLinkLabels={true}
              arcLinkLabelsSkipAngle={10}
              arcLinkLabelsTextColor="#333333"
              arcLabelsSkipAngle={10}
              legends={[
                {
                  anchor: 'bottom',
                  direction: 'row',
                  justify: false,
                  translateX: 0,
                  translateY: 56,
                  itemsSpacing: 0,
                  itemWidth: 100,
                  itemHeight: 18,
                  itemTextColor: '#999',
                  itemDirection: 'left-to-right',
                  itemOpacity: 1,
                  symbolSize: 18,
                  symbolShape: 'circle'
                }
              ]}
            />
          </Box>
        </Grid>
        <Grid item xs={12} md={4}>
          <Box display="flex" flexDirection="column" gap={2}>
            <StatCard
              title="Available Tickets"
              value={status.availableTickets || 0}
              color="#2196f3"
            />
            <StatCard
              title="Sold Tickets"
              value={status.soldTickets || 0}
              color="#4caf50"
            />
            <StatCard
              title="Remaining to Add"
              value={status.remainingTickets || 0}
              color="#ff9800"
            />
          </Box>
        </Grid>
      </Grid>
    </Paper>
  );
}

function StatCard({ title, value, color }) {
  return (
    <Box p={2} bgcolor={color + '10'} borderRadius={1}>
      <Typography variant="subtitle2" color="textSecondary">
        {title}
      </Typography>
      <Typography variant="h4" style={{ color }}>
        {value}
      </Typography>
    </Box>
  );
}

export default TicketPool;