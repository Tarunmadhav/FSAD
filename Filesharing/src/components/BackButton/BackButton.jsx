import { IconButton } from '@mui/material';
import { ArrowBack } from '@mui/icons-material';
import { useNavigate } from 'react-router-dom';
import './BackButton.css';

function BackButton() {
  const navigate = useNavigate();

  return (
    <IconButton
      className="back-button"
      onClick={() => navigate(-1)}
      aria-label="go back"
    >
      <ArrowBack />
    </IconButton>
  );
}

export default BackButton;