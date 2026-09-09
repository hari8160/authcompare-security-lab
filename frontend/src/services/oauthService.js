import { API_BASE_URL } from './api';

// Kicks off the real Authorization Code flow: the browser is redirected to the
// backend, which redirects to the LOCAL LAB OAuth Authorization Server. This is a
// full navigation (not an XHR) because the browser itself must visit the
// authorization endpoint and later follow the redirect back with the code.
export const startOAuthLogin = () => {
  window.location.href = `${API_BASE_URL}/oauth2/authorization/local-lab`;
};
