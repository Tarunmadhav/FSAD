import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import { Button, TextField, Typography, Container, Box } from '@mui/material';
import DevicesIcon from '@mui/icons-material/Devices';
import BackButton from '../../components/BackButton/BackButton';
import './Profile.css';

function Profile() {
  const navigate = useNavigate();
  const [profileName, setProfileName] = useState('');
  const deviceName = 'ASUS_Z01QD'; // This would typically come from device info

  const handleProfileNameChange = (e) => {
    setProfileName(e.target.value);
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle profile update logic here
    console.log('Profile updated:', { profileName });
    navigate('/');
  };

  return (
    <Container className="profile-container fade-in">
      <BackButton />
      <Box className="profile-content">
        <Typography variant="h4" component="h1" className="profile-title">
          Profile
        </Typography>

        <Box className="device-info">
          <DevicesIcon className="device-icon" />
          <Typography variant="h6" className="device-name">
            {deviceName}
          </Typography>
        </Box>

        <Box component="form" onSubmit={handleSubmit} className="profile-form">
          <TextField
            fullWidth
            label="Profile Name"
            value={profileName}
            onChange={handleProfileNameChange}
            margin="normal"
            variant="outlined"
            className="profile-input"
          />

          <Button
            type="submit"
            fullWidth
            variant="contained"
            className="profile-button"
          >
            SIGN IN
          </Button>
        </Box>
      </Box>
    </Container>
  );
}

export default Profile;