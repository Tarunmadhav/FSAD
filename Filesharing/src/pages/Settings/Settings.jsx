import { useState } from 'react';
import { Container, Typography, Box, FormControl, FormControlLabel, Switch, Divider } from '@mui/material';
import WifiIcon from '@mui/icons-material/Wifi';
import FolderIcon from '@mui/icons-material/Folder';
import FileCopyIcon from '@mui/icons-material/FileCopy';
import DevicesIcon from '@mui/icons-material/Devices';
import BlockIcon from '@mui/icons-material/Block';
import BackButton from '../../components/BackButton/BackButton';
import './Settings.css';

function Settings() {
  const [settings, setSettings] = useState({
    wifiOnly: true,
    defaultStorage: true,
    duplicateFiles: false,
    nearbyDevices: true,
    removeAds: false,
  });

  const handleChange = (setting) => (event) => {
    setSettings((prev) => ({
      ...prev,
      [setting]: event.target.checked,
    }));
  };

  return (
    <Container className="settings-container fade-in">
      <BackButton />
      <Typography variant="h4" component="h1" className="settings-title">
        Settings
      </Typography>

      <Box className="settings-content">
        <FormControl component="fieldset" className="settings-form">
          <FormControlLabel
            control={
              <Switch
                checked={settings.wifiOnly}
                onChange={handleChange('wifiOnly')}
                className="settings-switch"
              />
            }
            label={
              <Box className="settings-label">
                <WifiIcon />
                <Box>
                  <Typography variant="subtitle1">File Transfer</Typography>
                  <Typography variant="body2" color="textSecondary">
                    Only when connected to Wi-Fi
                  </Typography>
                </Box>
              </Box>
            }
          />
          <Divider />

          <FormControlLabel
            control={
              <Switch
                checked={settings.defaultStorage}
                onChange={handleChange('defaultStorage')}
                className="settings-switch"
              />
            }
            label={
              <Box className="settings-label">
                <FolderIcon />
                <Box>
                  <Typography variant="subtitle1">Receive</Typography>
                  <Typography variant="body2" color="textSecondary">
                    Default storage path
                  </Typography>
                </Box>
              </Box>
            }
          />
          <Divider />

          <FormControlLabel
            control={
              <Switch
                checked={settings.duplicateFiles}
                onChange={handleChange('duplicateFiles')}
                className="settings-switch"
              />
            }
            label={
              <Box className="settings-label">
                <FileCopyIcon />
                <Box>
                  <Typography variant="subtitle1">Duplicate File</Typography>
                  <Typography variant="body2" color="textSecondary">
                    How to handle duplicate files
                  </Typography>
                </Box>
              </Box>
            }
          />
          <Divider />

          <FormControlLabel
            control={
              <Switch
                checked={settings.nearbyDevices}
                onChange={handleChange('nearbyDevices')}
                className="settings-switch"
              />
            }
            label={
              <Box className="settings-label">
                <DevicesIcon />
                <Box>
                  <Typography variant="subtitle1">Find nearby devices</Typography>
                  <Typography variant="body2" color="textSecondary">
                    Make the device discoverable
                  </Typography>
                </Box>
              </Box>
            }
          />
          <Divider />

          <FormControlLabel
            control={
              <Switch
                checked={settings.removeAds}
                onChange={handleChange('removeAds')}
                className="settings-switch"
              />
            }
            label={
              <Box className="settings-label">
                <BlockIcon />
                <Box>
                  <Typography variant="subtitle1">Remove Ads</Typography>
                  <Typography variant="body2" color="textSecondary">
                    Remove advertisements
                  </Typography>
                </Box>
              </Box>
            }
          />
        </FormControl>
      </Box>
    </Container>
  );
}

export default Settings;