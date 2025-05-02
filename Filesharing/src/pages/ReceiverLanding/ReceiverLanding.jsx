import { useState, useEffect } from 'react';
import {
  Container,
  Box,
  Typography,
  CircularProgress,
  IconButton,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import { useNavigate, useLocation } from 'react-router-dom';
import './ReceiverLanding.css';

function ReceiverLanding() {
  const navigate = useNavigate();
  const location = useLocation();
  const [transferKey] = useState(location.state?.key || '123456');
  const [statusMessage, setStatusMessage] = useState('Connecting to server...');

  useEffect(() => {
    // Simulate checking connection status
    const timer = setTimeout(() => {
      setStatusMessage('Ready to receive files');
    }, 1500);

    return () => clearTimeout(timer);
  }, []);

  return (
    <Container className="receiver-landing-container fade-in">
      <Box className="header-section">
        <IconButton
          className="back-button"
          onClick={() => navigate(-1)}
          aria-label="go back"
        >
          <ArrowBackIcon />
        </IconButton>
        <Typography variant="h4" className="page-title">
          Ready to Receive
        </Typography>
      </Box>

      <Box className="content-section">
        <Box className="key-display">
          <Typography variant="h6" className="key-label">
            Your Transfer Code
          </Typography>
          <Typography variant="h3" className="key-value">
            {transferKey.split('').map((digit, index) => (
              <span key={index} className="key-digit">{digit}</span>
            ))}
          </Typography>
        </Box>

        <Box className="status-section">
          <CircularProgress size={24} className="status-spinner" />
          <Typography variant="body1" className="status-message">
            {statusMessage}
          </Typography>
        </Box>

        <Box className="info-section">
          <Typography variant="body2" className="info-text">
            Share this code with the sender to start receiving files.
            Keep this page open until the transfer begins.
          </Typography>
        </Box>
      </Box>
    </Container>
  );
}

export default ReceiverLanding;