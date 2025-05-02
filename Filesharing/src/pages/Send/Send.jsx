import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container,
  Box,
  Tabs,
  Tab,
  IconButton,
  InputBase,
  Paper,
  Typography,
  List,
  ListItem,
  ListItemIcon,
  ListItemText,
  Checkbox,
  Button,
  Divider,
} from '@mui/material';
import SearchIcon from '@mui/icons-material/Search';
import SendIcon from '@mui/icons-material/Send';
import InsertDriveFileIcon from '@mui/icons-material/InsertDriveFile';
import ImageIcon from '@mui/icons-material/Image';
import AudioFileIcon from '@mui/icons-material/AudioFile';
import VideoFileIcon from '@mui/icons-material/VideoFile';
import FolderIcon from '@mui/icons-material/Folder';
import ContactsIcon from '@mui/icons-material/Contacts';
import AppsIcon from '@mui/icons-material/Apps';
import BackButton from '../../components/BackButton/BackButton';
import BottomNavBar from '../../components/BottomNavBar/BottomNavBar';
import './Send.css';

function Send() {
  const navigate = useNavigate();
  const [currentTab, setCurrentTab] = useState(0);
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedFiles, setSelectedFiles] = useState([]);

  const handleTabChange = (event, newValue) => {
    setCurrentTab(newValue);
  };

  const handleSearch = (e) => {
    setSearchQuery(e.target.value);
  };

  const handleFileSelect = (fileId) => {
    setSelectedFiles(prev =>
      prev.includes(fileId)
        ? prev.filter(id => id !== fileId)
        : [...prev, fileId]
    );
  };

  const tabs = [
    { id: 'recent', label: 'RECENT', icon: <InsertDriveFileIcon /> },
    { id: 'photos', label: 'PHOTOS', icon: <ImageIcon /> },
    { id: 'videos', label: 'VIDEOS', icon: <VideoFileIcon /> },
    { id: 'audio', label: 'AUDIO', icon: <AudioFileIcon /> },
    { id: 'apps', label: 'APPS', icon: <AppsIcon /> },
    { id: 'contacts', label: 'CONTACTS', icon: <ContactsIcon /> },
    { id: 'files', label: 'FILES', icon: <FolderIcon /> },
  ];

  const filesByCategory = {
    recent: [
      { id: 1, name: 'Recent Document.pdf', size: '2.5 MB', date: '2 hours ago' },
      { id: 2, name: 'Latest Presentation.pptx', size: '5.8 MB', date: '5 hours ago' },
    ],
    photos: [
      { id: 3, name: 'Photo_001.jpg', size: '3.2 MB', date: 'Yesterday' },
      { id: 4, name: 'Screenshot.png', size: '1.1 MB', date: 'Yesterday' },
    ],
    videos: [
      { id: 5, name: 'Project_Video.mp4', size: '25.7 MB', date: '2 days ago' },
      { id: 6, name: 'Meeting_Recording.mp4', size: '15.3 MB', date: '3 days ago' },
    ],
    audio: [
      { id: 7, name: 'Voice_Note.mp3', size: '2.8 MB', date: '4 days ago' },
      { id: 8, name: 'Music_Track.mp3', size: '4.5 MB', date: '5 days ago' },
    ],
    apps: [
      { id: 9, name: 'App_Backup.zip', size: '45.2 MB', date: '1 week ago' },
      { id: 10, name: 'Config_File.json', size: '0.5 MB', date: '1 week ago' },
    ],
    contacts: [
      { id: 11, name: 'Contacts_Backup.vcf', size: '0.8 MB', date: '2 weeks ago' },
    ],
    files: [
      { id: 12, name: 'Documents.zip', size: '128.5 MB', date: '3 weeks ago' },
      { id: 13, name: 'Backup.tar', size: '256.0 MB', date: '1 month ago' },
    ],
  };

  const currentFiles = filesByCategory[tabs[currentTab].id] || [];

  // Filter files based on search query
  const filteredFiles = currentFiles.filter(file =>
    file.name.toLowerCase().includes(searchQuery.toLowerCase())
  );

  const handleSendFiles = () => {
    // Generate a random 6-digit code
    const code = Math.floor(100000 + Math.random() * 900000).toString();
    // Navigate to waiting page with the code
    navigate('/waiting');
  };

  return (
    <Container className="send-container fade-in">
      <Box className="send-header">
        <BackButton />
        <Paper component="form" className="search-bar">
          <SearchIcon className="search-icon" />
          <InputBase
            className="search-input"
            placeholder="Search files..."
            value={searchQuery}
            onChange={handleSearch}
          />
        </Paper>
      </Box>

      <Box className="tabs-container">
        <Tabs
          value={currentTab}
          onChange={handleTabChange}
          variant="scrollable"
          scrollButtons="auto"
          className="tabs"
        >
          {tabs.map((tab) => (
            <Tab
              key={tab.id}
              icon={tab.icon}
              label={tab.label}
              className="tab"
            />
          ))}
        </Tabs>
      </Box>

      <Box className="files-container">
        {filteredFiles.length === 0 ? (
          <Typography variant="body1" className="empty-state">
            No files found
          </Typography>
        ) : (
          <List>
            {filteredFiles.map((file) => (
              <div key={file.id}>
                <ListItem
                  button
                  onClick={() => handleFileSelect(file.id)}
                  className="file-item"
                >
                  <ListItemIcon>
                    <Checkbox
                      checked={selectedFiles.includes(file.id)}
                      onChange={() => handleFileSelect(file.id)}
                      color="primary"
                    />
                  </ListItemIcon>
                  <ListItemText
                    primary={file.name}
                    secondary={`${file.size} • ${file.date}`}
                  />
                </ListItem>
                <Divider />
              </div>
            ))}
          </List>
        )}
      </Box>

      {selectedFiles.length > 0 && (
        <Box className="send-button-container">
          <Button
            variant="contained"
            color="primary"
            fullWidth
            onClick={handleSendFiles}
            className="send-button"
            startIcon={<SendIcon />}
          >
            Send {selectedFiles.length} {selectedFiles.length === 1 ? 'File' : 'Files'}
          </Button>
        </Box>
      )}

      <BottomNavBar value="send" />
    </Container>
  );
}

export default Send;