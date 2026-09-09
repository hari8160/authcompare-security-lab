import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { login } from '../services/authService';
import useAuth from '../hooks/useAuth';

export default function Login() {
  const [form, setForm] = useState({ username: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();
  const { refresh } = useAuth();

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await login(form.username, form.password);
      await refresh();
      navigate('/dashboard');
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || 'Login failed');
    }
  };

  return (
    <div className="container">
      <div className="card" style={{ maxWidth: 420 }}>
        <h2>Login <span className="badge password">TRADITIONAL</span></h2>
        {error && <div className="error">{String(error)}</div>}
        <form onSubmit={onSubmit}>
          <input name="username" placeholder="Username" value={form.username} onChange={onChange} required />
          <input name="password" type="password" placeholder="Password" value={form.password} onChange={onChange} required />
          <button type="submit">Login</button>
        </form>
        <p className="muted">No account? <Link to="/register">Register</Link></p>
        <p className="muted">Or use <Link to="/oauth/login">OAuth Login</Link> instead.</p>
      </div>
    </div>
  );
}
