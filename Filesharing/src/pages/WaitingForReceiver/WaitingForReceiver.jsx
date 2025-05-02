import { useState } from 'react';
import {
  Container,
  Box,
  Typography,
  Button,
  Link,
  List,
  ListItem,
  ListItemText,
  IconButton,
} from '@mui/material';
import QrCodeScannerIcon from '@mui/icons-material/QrCodeScanner';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import DevicesIcon from '@mui/icons-material/Devices';
import './WaitingForReceiver.css';

function WaitingForReceiver() {
  const [key] = useState('123456');
  const [nearbyDevices] = useState([
    { id: 1, name: 'Device 1', status: 'Available' },
    { id: 2, name: 'Device 2', status: 'Available' },
    { id: 3, name: 'Device 3', status: 'Busy' },
  ]);

  return (
    <Container className="waiting-container fade-in">
      <Box className="waiting-content">
        <Box className="header">
          <IconButton className="back-button" onClick={() => window.history.back()}>
            <ArrowBackIcon />
          </IconButton>
          <Typography variant="h4" component="h1" className="waiting-title">
            Waiting for Receiver
          </Typography>
        </Box>

        <Box className="key-display">
          <Typography variant="h3" className="key-text">
            {key.split('').map((digit, index) => (
              <span key={index} className="key-digit">{digit}</span>
            ))}
          </Typography>
          <Typography variant="body1" className="key-hint">
            Share this key with the sender
          </Typography>
        </Box>

        <Box className="nearby-devices">
          <Typography variant="h6" className="devices-title">
            Nearby Devices
          </Typography>
          <List className="devices-list">
            {nearbyDevices.map((device) => (
              <ListItem
                key={device.id}
                button
                className={`device-item ${device.status.toLowerCase()}`}
              >
                <DevicesIcon className="device-icon" />
                <ListItemText
                  primary={device.name}
                  secondary={device.status}
                  className="device-text"
                />
              </ListItem>
            ))}
          </List>
        </Box>

        <Button
          variant="contained"
          startIcon={<QrCodeScannerIcon />}
          className="scan-button"
          onClick={() => console.log('Open QR scanner')}
        >
          Scan QR Code
        </Button>

        <Link href="#" className="trouble-link">
          Trouble?
        </Link>
      </Box>
    </Container>
  );
}

export default WaitingForReceiver;