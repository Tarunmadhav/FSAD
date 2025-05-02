import { useState, useEffect } from 'react';
import {
  Container,
  Box,
  Typography,
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction,
  IconButton,
  Divider,
  LinearProgress,
} from '@mui/material';
import ArrowBackIcon from '@mui/icons-material/ArrowBack';
import HistoryIcon from '@mui/icons-material/History';
import './FileTransfer.css';

function FileTransfer() {
  const [currentTransfers, setCurrentTransfers] = useState([
    {
      id: 1,
      filename: 'presentation.pptx',
      size: '15MB',
      progress: 30,
      speed: '1.8 MB/s',
      timeRemaining: '2 min',
    },
    {
      id: 2,
      filename: 'project.zip',
      size: '50MB',
      progress: 65,
      speed: '3.2 MB/s',
      timeRemaining: '4 min',
    },
  ]);

  const [avgSpeed, setAvgSpeed] = useState('2.5 MB/s');

  useEffect(() => {
    const calculateAvgSpeed = () => {
      const totalSpeed = currentTransfers.reduce((acc, transfer) => {
        const speed = parseFloat(transfer.speed.split(' ')[0]);
        return acc + speed;
      }, 0);
      const avg = totalSpeed / currentTransfers.length;
      setAvgSpeed(`${avg.toFixed(1)} MB/s`);
    };

    calculateAvgSpeed();
  }, [currentTransfers]);

  const [transferHistory, setTransferHistory] = useState([
    {
      id: 1,
      filename: 'report.docx',
      size: '8MB',
      type: 'received',
      date: '2024-01-20',
    },
    {
      id: 2,
      filename: 'photos.zip',
      size: '120MB',
      type: 'received',
      date: '2024-01-19',
    },
    {
      id: 3,
      filename: 'design.fig',
      size: '45MB',
      type: 'received',
      date: '2024-01-18',
    },
  ]);

  return (
    <Container className="transfer-container">
      <Box className="transfer-header">
        <IconButton className="back-button" onClick={() => window.history.back()}>
          <ArrowBackIcon />
        </IconButton>
        <Typography variant="h5" component="h1" className="header-title">
          File Transfer
        </Typography>
        <Typography variant="subtitle1" className="avg-speed">
          Average Speed: {avgSpeed}
        </Typography>
      </Box>

      <Box className="current-transfers">
        <Typography variant="h6" className="section-title">
          Current Transfers
        </Typography>
        <List>
          {currentTransfers.map((transfer) => (
            <ListItem key={transfer.id} className="transfer-item">
              <Box className="transfer-info">
                <Typography variant="subtitle1" className="filename">
                  {transfer.filename}
                </Typography>
                <Box className="transfer-details">
                  <Typography variant="body2" className="size">
                    {transfer.size}
                  </Typography>
                  <Typography variant="body2" className="speed">
                    {transfer.speed}
                  </Typography>
                  <Typography variant="body2" className="time-remaining">
                    {transfer.timeRemaining} remaining
                  </Typography>
                </Box>
                <LinearProgress
                  variant="determinate"
                  value={transfer.progress}
                  className="progress-bar"
                />
              </Box>
            </ListItem>
          ))}
        </List>
      </Box>

      <Divider className="section-divider" />

      <Box className="transfer-history">
        <Typography variant="h6" className="section-title">
          <HistoryIcon className="history-icon" />
          Transfer History
        </Typography>
        <List>
          {transferHistory.map((item) => (
            <ListItem key={item.id} className="history-item">
              <ListItemText
                primary={item.filename}
                secondary={
                  <>
                    <span className="history-size">{item.size}</span>
                    <span className="history-date">{item.date}</span>
                  </>
                }
              />
              <ListItemSecondaryAction>
                <Typography
                  variant="body2"
                  className={`transfer-type ${item.type}`}
                >
                  {item.type}
                </Typography>
              </ListItemSecondaryAction>
            </ListItem>
          ))}
        </List>
      </Box>
    </Container>
  );
}

export default FileTransfer;