import { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Button,
  TextField,
  Typography,
  Container,
  Box,
  Link,
  Divider,
  IconButton,
} from '@mui/material';
import GoogleIcon from '@mui/icons-material/Google';
import FacebookIcon from '@mui/icons-material/Facebook';
import BackButton from '../../components/BackButton/BackButton';
import './SignIn.css';

function SignIn() {
  const navigate = useNavigate();
  const [formData, setFormData] = useState({
    email: '',
    password: '',
  });

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // Handle sign in logic here
    navigate('/profile');
  };

  return (
    <Container className="signin-container fade-in">
      <BackButton />
      <Typography variant="h4" component="h1" className="signin-title">
        Sign In
      </Typography>
      
      <Box component="form" onSubmit={handleSubmit} className="signin-form">
        <TextField
          fullWidth
          label="Email"
          name="email"
          type="email"
          value={formData.email}
          onChange={handleChange}
          required
          margin="normal"
        />
        <TextField
          fullWidth
          label="Password"
          name="password"
          type="password"
          value={formData.password}
          onChange={handleChange}
          required
          margin="normal"
        />
        
        <Link href="#" className="forgot-password">
          Forgot Password?
        </Link>

        <Button
          type="submit"
          fullWidth
          variant="contained"
          className="signin-button"
        >
          SIGN IN
        </Button>

        <Box className="signin-link">
          <Typography variant="body2">
            Don't have an account?{' '}
            <Link href="/signup" className="link">
              Sign Up
            </Link>
          </Typography>
        </Box>

        <Divider className="divider">
          <Typography variant="body2" color="textSecondary">
            OR
          </Typography>
        </Divider>

        <Box className="social-login">
          <IconButton
            className="social-button google"
            onClick={() => console.log('Google login')}
          >
            <GoogleIcon />
          </IconButton>
          <IconButton
            className="social-button facebook"
            onClick={() => console.log('Facebook login')}
          >
            <FacebookIcon />
          </IconButton>
        </Box>
      </Box>
    </Container>
  );
}

export default SignIn;