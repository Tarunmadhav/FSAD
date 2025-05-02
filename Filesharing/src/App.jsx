import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';
import { ThemeProvider, CssBaseline, createTheme } from '@mui/material';
import Home from './pages/Home/Home';
import Send from './pages/Send/Send';
import Receive from './pages/Receive/Receive';
import Profile from './pages/Profile/Profile';
import Settings from './pages/Settings/Settings';
import SignIn from './pages/SignIn/SignIn';
import WaitingForReceiver from './pages/WaitingForReceiver/WaitingForReceiver';
import ReceiverLanding from './pages/ReceiverLanding/ReceiverLanding';
import ShareOptions from './pages/ShareOptions/ShareOptions';
import './App.css';
import SignUp from './pages/SignUp/SignUp';

const darkTheme = createTheme({
  palette: {
    mode: 'dark',
    primary: {
      main: '#2196f3',
      light: '#64b5f6',
      dark: '#1976d2',
    },
    secondary: {
      main: '#424242',
      light: '#616161',
      dark: '#212121',
    },
    background: {
      default: '#121212',
      paper: '#1e1e1e',
    },
    text: {
      primary: '#ffffff',
      secondary: '#b0bec5',
    },
    action: {
      hover: 'rgba(255, 255, 255, 0.08)',
      selected: 'rgba(255, 255, 255, 0.16)',
    },
  },
  typography: {
    fontFamily: '"Roboto", "Helvetica", "Arial", sans-serif',
    h4: {
      fontWeight: 600,
    },
    button: {
      textTransform: 'none',
    },
  },
  components: {
    MuiCssBaseline: {
      styleOverrides: {
        body: {
          backgroundColor: '#121212',
          color: '#ffffff',
        },
      },
    },
  },
});

function App() {
  return (
    <ThemeProvider theme={darkTheme}>
      <CssBaseline />
      <Router>
        <div className="app">
          <main className="main-content fade-in">
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/send" element={<Send />} />
              <Route path="/receive" element={<Receive />} />
              <Route path="/profile" element={<Profile />} />
              <Route path="/settings" element={<Settings />} />
              <Route path="/signin" element={<SignIn />} />
              <Route path="/signup" element={<SignUp />} />
              <Route path="/waiting" element={<WaitingForReceiver />} />
              <Route path="/receiver-landing" element={<ReceiverLanding />} />
              <Route path="/share-options" element={<ShareOptions />} />
            </Routes>
          </main>
        </div>
      </Router>
    </ThemeProvider>
  );
}

export default App;
