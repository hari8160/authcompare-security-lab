import React, { useEffect } from 'react';
import { useNavigate, useSearchParams, Link } from 'react-router-dom';
import useAuth from '../hooks/useAuth';

export default function OAuthCallback() {
  const [params] = useSearchParams();
  const status = params.get('status');
  const navigate = useNavigate();
  const { refresh } = useAuth();

  useEffect(() => {
    if (status === 'success') {
      refresh().then(() => navigate('/dashboard'));
    }
  }, [status, refresh, navigate]);

  if (status === 'failure') {
    return (
      <div className="container">
        <div className="card error">
          OAuth login failed. Check redirect URI, client credentials, and Authorization Server logs.
          <div><Link to="/oauth/login">Try again</Link></div>
        </div>
      </div>
    );
  }

  return <div className="container">Completing OAuth sign-in...</div>;
}
