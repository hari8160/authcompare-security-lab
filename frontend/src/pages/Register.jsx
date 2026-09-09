import React, { useState } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { register } from '../services/authService';

export default function Register() {
  const [form, setForm] = useState({ username: '', email: '', password: '' });
  const [error, setError] = useState('');
  const navigate = useNavigate();

  const onChange = (e) => setForm({ ...form, [e.target.name]: e.target.value });

  const onSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await register(form.username, form.email, form.password);
      navigate('/login');
    } catch (err) {
      setError(err.response?.data?.message || err.response?.data || 'Registration failed');
    }
  };

  return (
    <div className="container">
      <div className="card" style={{ maxWidth: 420 }}>
        <h2>Register <span className="badge password">TRADITIONAL</span></h2>
        {error && <div className="error">{String(error)}</div>}
        <form onSubmit={onSubmit}>
          <input name="username" placeholder="Username" value={form.username} onChange={onChange} required />
          <input name="email" type="email" placeholder="Email" value={form.email} onChange={onChange} required />
          <input name="password" type="password" placeholder="Password (min 8 chars)" value={form.password} onChange={onChange} required minLength={8} />
          <button type="submit">Create account</button>
        </form>
        <p className="muted">Already have an account? <Link to="/login">Login</Link></p>
      </div>
    </div>
  );
}
