import { BottomNavigation, BottomNavigationAction } from '@mui/material';
import SendIcon from '@mui/icons-material/Send';
import GetAppIcon from '@mui/icons-material/GetApp';
import HistoryIcon from '@mui/icons-material/History';
import HomeIcon from '@mui/icons-material/Home';
import { useNavigate } from 'react-router-dom';
import './BottomNavBar.css';

function BottomNavBar({ value }) {
  const navigate = useNavigate();

  const handleNavChange = (event, newValue) => {
    switch (newValue) {
      case 'send':
        navigate('/send');
        break;
      case 'receive':
        navigate('/receive');
        break;
      case 'history':
        navigate('/history');
        break;
      case 'home':
        navigate('/');
        break;
      default:
        break;
    }
  };

  return (
    <BottomNavigation
      value={value}
      onChange={handleNavChange}
      className="bottom-nav"
    >
      <BottomNavigationAction
        label="Home"
        value="home"
        icon={<HomeIcon />}
      />
      <BottomNavigationAction
        label="Send"
        value="send"
        icon={<SendIcon />}
      />
      <BottomNavigationAction
        label="Receive"
        value="receive"
        icon={<GetAppIcon />}
      />
      <BottomNavigationAction
        label="History"
        value="history"
        icon={<HistoryIcon />}
      />
    </BottomNavigation>
  );
}

export default BottomNavBar;