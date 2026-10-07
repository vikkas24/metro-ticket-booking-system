const session = () => ({ token: localStorage.getItem('token'), name: localStorage.getItem('name'), role: localStorage.getItem('role') });
function saveSession(d) { localStorage.setItem('token', d.token); localStorage.setItem('name', d.name); localStorage.setItem('role', d.role); }
function clearSession() { ['token', 'name', 'role'].forEach(k => localStorage.removeItem(k)); }
async function logout() { try { await post('/api/auth/logout'); } catch (e) {} clearSession(); location.href = 'index.html'; }
function requireLogin(role) {
  const s = session();
  if (!s.token || (role && s.role !== role)) { location.href = 'login.html'; return false; }
  return true;
}
const esc = v => String(v ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
const rupees = n => '₹' + Number(n || 0).toFixed(2);
const dt = v => v ? new Date(v).toLocaleString() : '';
function showMsg(id, text, type = 'error') { const el = document.getElementById(id); el.textContent = text; el.className = 'msg show ' + type; }
function clearMsg(id) { document.getElementById(id).className = 'msg'; }
function renderNav() {
  const s = session(), el = document.getElementById('nav');
  const links = s.token
    ? (s.role === 'ADMIN' ? '<a href="admin.html">Admin</a>' : '<a href="booking.html">Book Ticket</a><a href="tickets.html">My Tickets</a>') + `<a href="#" id="lo">Logout (${esc(s.name)})</a>`
    : '<a href="login.html">Login</a><a href="register.html">Register</a>';
  el.innerHTML = '<a class="logo" href="index.html"><i>M</i> MetroGo</a><div class="nav-links">' + links + '</div>';
  const lo = document.getElementById('lo'); if (lo) lo.onclick = e => { e.preventDefault(); logout(); };
}
document.addEventListener('DOMContentLoaded', renderNav);
