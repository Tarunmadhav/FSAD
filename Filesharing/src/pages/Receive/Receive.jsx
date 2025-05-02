import { useState } from 'react';
import {
  Container,
  Box,
  Typography,
  TextField,
  Link,
  Paper,
  BottomNavigation,
  BottomNavigationAction,
  IconButton,
} from '@mui/material';
import SendIcon from '@mui/icons-material/Send';
import GetAppIcon from '@mui/icons-material/GetApp';
import HistoryIcon from '@mui/icons-material/History';
import LinkIcon from '@mui/icons-material/Link';
import QrCodeScannerIcon from '@mui/icons-material/QrCodeScanner';
import { useNavigate } from 'react-router-dom';
import BackButton from '../../components/BackButton/BackButton';
import './Receive.css';
import BottomNavBar from '../../components/BottomNavBar/BottomNavBar';

function Receive() {
  const navigate = useNavigate();
  const [key, setKey] = useState('');

  const handleKeyChange = (e) => {
    const value = e.target.value.replace(/\D/g, '').slice(0, 6);
    setKey(value);
    if (value.length === 6) {
      navigate('/receiver-landing', { state: { key: value } });
    }
  };

  const handleScanQR = () => {
    // TODO: Implement QR scanning functionality
    console.log('Scanning QR code...');
  };

  return (
    <Container className="receive-container fade-in">
      <Box className="receive-content">
        <div className="header-section">
          <BackButton />
          <Typography variant="h4" component="h1" className="receive-title">
            Receive Files
          </Typography>
        </div>

        <div className="input-section">
          <TextField
            fullWidth
            label="Enter 6-digit key"
            value={key}
            onChange={handleKeyChange}
            className="key-input"
            inputProps={{
              maxLength: 6,
              pattern: '[0-9]*',
            }}
          />
          <Link href="#" className="guide-link">
            User Guide
          </Link>
          <Box className="scan-qr-container">
            <IconButton
              size="small"
              className="scan-qr-button"
              onClick={handleScanQR}
              color="primary"
              aria-label="scan QR code"
            >
              <QrCodeScannerIcon fontSize="small" />
            </IconButton>
          </Box>
        </div>

        <Paper className="promo-banner">
          <Typography variant="subtitle1" className="promo-text">
            Share files faster and easier with FileShare Pro!
          </Typography>
          <Link href="#" className="upgrade-link">
            Upgrade Now
          </Link>
        </Paper>
      </Box>

      <BottomNavBar value="receive" />
    </Container>
  );
}

export default Receive;