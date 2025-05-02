import { useState, useEffect } from 'react';
import {
  Container,
  Box,
  Typography,
  Button,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Divider,
} from '@mui/material';
import QrCode2Icon from '@mui/icons-material/QrCode2';
import DevicesIcon from '@mui/icons-material/Devices';
import { useNavigate } from 'react-router-dom';
import BackButton from '../../components/BackButton/BackButton';
import './ShareOptions.css';

function ShareOptions() {
  const navigate = useNavigate();
  const [code, setCode] = useState('');
  const [nearbyDevices, setNearbyDevices] = useState([
    { id: 1, name: 'Device 1', type: 'Mobile' },
    { id: 2, name: 'Device 2', type: 'Laptop' },
  ]);

  useEffect(() => {
    // Generate random 6-digit code
    const randomCode = Math.floor(100000 + Math.random() * 900000).toString();
    setCode(randomCode);
  }, []);

  const handleShowQR = () => {
    // Handle QR code display
    console.log('Show QR code');
  };

  return (
    <Container className="share-options-container fade-in">
      <BackButton />
      <Typography variant="h4" component="h1" className="share-title">
        Share Files
      </Typography>

      <Box className="code-display">
        <Typography variant="h6" className="code-label">
          Your 6-digit code
        </Typography>
        <Box className="code-digits">
          {code.split('').map((digit, index) => (
            <Typography key={index} className="code-digit">
              {digit}
            </Typography>
          ))}
        </Box>
      </Box>

      <Button
        variant="contained"
        startIcon={<QrCode2Icon />}
        className="qr-button"
        onClick={handleShowQR}
      >
        Show QR Code
      </Button>

      <Box className="nearby-devices">
        <Typography variant="h6" className="devices-title">
          Nearby Devices
        </Typography>
        <List>
          {nearbyDevices.map((device) => (
            <div key={device.id}>
              <ListItem button className="device-item">
                <ListItemIcon>
                  <DevicesIcon className="device-icon" />
                </ListItemIcon>
                <ListItemText
                  primary={device.name}
                  secondary={device.type}
                />
              </ListItem>
              <Divider />
            </div>
          ))}
        </List>
      </Box>
    </Container>
  );
}

export default ShareOptions;