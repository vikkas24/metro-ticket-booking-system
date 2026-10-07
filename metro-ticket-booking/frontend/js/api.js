const API_BASE = 'http://localhost:8080';
/** Single place for all backend calls. Throws Error(message) with a user-friendly message. */
async function api(method, path, body) {
  const headers = { 'Content-Type': 'application/json' };
  const token = localStorage.getItem('token');
  if (token) headers.Authorization = 'Bearer ' + token;
  let res;
  try { res = await fetch(API_BASE + path, { method, headers, body: body ? JSON.stringify(body) : undefined }); }
  catch (e) { throw new Error('Unable to connect to the server.'); }
  const data = await res.json().catch(() => ({}));
  if (res.status === 401 && token) { clearSession(); location.href = 'login.html'; }
  if (!res.ok) throw new Error(data.error || 'Request failed.');
  return data;
}
const get = p => api('GET', p), post = (p, b) => api('POST', p, b), put = (p, b) => api('PUT', p, b), del = p => api('DELETE', p);
