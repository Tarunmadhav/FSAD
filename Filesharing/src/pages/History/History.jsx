import { useState } from 'react';
import {
  Container,
  Typography,
  List,
  ListItem,
  ListItemText,
  ListItemSecondaryAction,
  IconButton,
  Divider,
} from '@mui/material';
import DeleteIcon from '@mui/icons-material/Delete';
import GetAppIcon from '@mui/icons-material/GetApp';
import BackButton from '../../components/BackButton/BackButton';
import BottomNavBar from '../../components/BottomNavBar/BottomNavBar';
import './History.css';

const mockHistory = [
  {
    id: 1,
    fileName: 'Project_Presentation.pptx',
    date: '2024-01-15',
    size: '5.2 MB',
    type: 'sent'
  },
  {
    id: 2,
    fileName: 'Budget_Report_2023.xlsx',
    date: '2024-01-14',
    size: '2.8 MB',
    type: 'received'
  },
  {
    id: 3,
    fileName: 'Team_Photo.jpg',
    date: '2024-01-13',
    size: '3.5 MB',
    type: 'sent'
  },
  {
    id: 4,
    fileName: 'Meeting_Notes.pdf',
    date: '2024-01-12',
    size: '1.1 MB',
    type: 'received'
  },
  {
    id: 5,
    fileName: 'Product_Design.fig',
    date: '2024-01-11',
    size: '8.7 MB',
    type: 'sent'
  }
];

function History() {
  const [history, setHistory] = useState(mockHistory);

  const handleDelete = (id) => {
    setHistory(history.filter(item => item.id !== id));
  };

  const handleDownload = (fileName) => {
    // TODO: Implement actual file download functionality
    console.log(`Downloading ${fileName}...`);
  };

  return (
    <Container className="history-container fade-in">
      <div className="header-section">
        <BackButton />
        <Typography variant="h4" component="h1" className="history-title">
          Transfer History
        </Typography>
      </div>

      <List className="history-list">
        {history.map((item, index) => (
          <div key={item.id}>
            <ListItem className={`history-item ${item.type}`}>
              <ListItemText
                primary={
                  <Typography variant="subtitle1" className="file-name">
                    {item.fileName}
                  </Typography>
                }
                secondary={
                  <Typography variant="body2" className="file-info">
                    {item.date} • {item.size} • {item.type === 'sent' ? 'Sent' : 'Received'}
                  </Typography>
                }
              />
              <ListItemSecondaryAction className="item-actions">
                <IconButton
                  edge="end"
                  aria-label="download"
                  onClick={() => handleDownload(item.fileName)}
                  className="action-button"
                >
                  <GetAppIcon />
                </IconButton>
                <IconButton
                  edge="end"
                  aria-label="delete"
                  onClick={() => handleDelete(item.id)}
                  className="action-button delete"
                >
                  <DeleteIcon />
                </IconButton>
              </ListItemSecondaryAction>
            </ListItem>
            {index < history.length - 1 && <Divider />}
          </div>
        ))}
      </List>

      <BottomNavBar value="history" />
    </Container>
  );
}

export default History;