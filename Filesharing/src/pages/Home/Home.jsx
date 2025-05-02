import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { IconButton, Button, Container, Box } from '@mui/material';
import { Settings as SettingsIcon } from '@mui/icons-material';
import { AccountCircle as ProfileIcon } from '@mui/icons-material';
import FolderIcon from '@mui/icons-material/Folder';
import BottomNavBar from '../../components/BottomNavBar/BottomNavBar';
import './Home.css';

function Home() {
  const navigate = useNavigate();
  const [navValue, setNavValue] = useState('home');

  return (
    <Container className="home-container fade-in">
      <Box className="header">
        <IconButton
          className="settings-button"
          onClick={() => navigate('/settings')}
          aria-label="settings"
        >
          <SettingsIcon />
        </IconButton>
        <Box className="logo-container">
          <FolderIcon className="file-manager-logo" />
        </Box>
        <IconButton
          className="profile-button"
          onClick={() => navigate('/profile')}
          aria-label="profile"
        >
          <ProfileIcon />
        </IconButton>
      </Box>

      <Box className="main-actions">
        <Button
          variant="contained"
          className="action-button send-button"
          onClick={() => navigate('/send')}
        >
          Send Files
        </Button>
        <Button
          variant="contained"
          className="action-button receive-button"
          onClick={() => navigate('/receive')}
        >
          Receive Files
        </Button>
      </Box>

      <BottomNavBar value={navValue} />
    </Container>
  );
}

export default Home;