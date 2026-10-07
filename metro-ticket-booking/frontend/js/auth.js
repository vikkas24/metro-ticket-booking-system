const form = document.getElementById('authForm'), isRegister = !!document.getElementById('confirm');
form.addEventListener('submit', async e => {
  e.preventDefault(); clearMsg('msg');
  const v = id => document.getElementById(id).value.trim();
  const body = { email: v('email'), password: document.getElementById('password').value };
  if (!body.email || !body.password) return showMsg('msg', 'Please enter your email and password.');
  if (isRegister) {
    Object.assign(body, { name: v('name'), phone: v('phone') });
    if (!body.name) return showMsg('msg', 'Please enter your full name.');
    if (body.phone && !/^\d{10}$/.test(body.phone)) return showMsg('msg', 'Phone must be 10 digits.');
    if (body.password.length < 6) return showMsg('msg', 'Password must be at least 6 characters.');
    if (body.password !== document.getElementById('confirm').value) return showMsg('msg', 'Passwords do not match.');
  }
  try {
    const d = await post(isRegister ? '/api/auth/register' : '/api/auth/login', body);
    saveSession(d); location.href = d.role === 'ADMIN' ? 'admin.html' : 'tickets.html';
  } catch (err) { showMsg('msg', err.message); }
});
