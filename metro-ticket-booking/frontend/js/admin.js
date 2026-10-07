if (requireLogin('ADMIN')) { tab('dash'); }
const $ = id => document.getElementById(id);
document.querySelectorAll('.sidebar a[data-tab]').forEach(a => a.onclick = () => tab(a.dataset.tab));
document.getElementById('lo2').onclick = logout;
const row = (cells) => '<tr>' + cells.map(c => `<td>${c}</td>`).join('') + '</tr>';
const ticketRow = t => row([esc(t.ticketNumber), esc(t.passenger), esc(t.source) + ' → ' + esc(t.destination), esc(t.journeyDate), t.passengerCount, rupees(t.totalFare), `<span class="badge ${esc(t.status)}">${esc(t.status)}</span>`]);
async function tab(name) {
  document.querySelectorAll('.panel').forEach(p => p.classList.toggle('active', p.id === name));
  document.querySelectorAll('.sidebar a[data-tab]').forEach(a => a.classList.toggle('active', a.dataset.tab === name));
  clearMsg('msg');
  try {
    if (name === 'dash') {
      const d = await get('/api/admin/dashboard'), t = await get('/api/admin/tickets');
      $('cards').innerHTML = [['Total users', d.totalUsers], ['Total stations', d.totalStations], ['Total bookings', d.totalTickets], ['Active tickets', d.activeTickets], ['Cancelled tickets', d.cancelledTickets], ['Total revenue', rupees(d.totalRevenue)]]
        .map(([l, v]) => `<div class="card stat"><b>${esc(v)}</b><span>${l}</span></div>`).join('');
      $('recent').innerHTML = t.slice(0, 8).map(ticketRow).join('') || row(['No bookings yet', '', '', '', '', '', '']);
    } else if (name === 'users') {
      $('userRows').innerHTML = (await get('/api/admin/users')).map(u => row([u.id, esc(u.name), esc(u.email), esc(u.phone), esc(u.role), dt(u.createdAt)])).join('');
    } else if (name === 'stations') loadStations();
    else if (name === 'bookings') $('bookRows').innerHTML = (await get('/api/admin/tickets')).map(ticketRow).join('');
  } catch (e) { showMsg('msg', e.message); }
}
let stations = [];
async function loadStations() {
  stations = await get('/api/admin/stations');
  $('stationRows').innerHTML = stations.map(s => row([s.stationOrder, esc(s.name), esc(s.code), esc(s.line), s.active ? 'Active' : 'Inactive',
    `<button class="btn sm dark" data-edit="${s.id}">Edit</button> ${s.active ? `<button class="btn sm danger" data-off="${s.id}">Deactivate</button>` : ''}`])).join('');
}
$('stationRows').addEventListener('click', async e => {
  try {
    if (e.target.dataset.edit) { const s = stations.find(x => x.id == e.target.dataset.edit);
      $('sid').value = s.id; $('sname').value = s.name; $('scode').value = s.code; $('sline').value = s.line || ''; $('sorder').value = s.stationOrder; $('sactive').checked = s.active; }
    if (e.target.dataset.off && confirm('Deactivate this station?')) { await del('/api/admin/stations/' + e.target.dataset.off); loadStations(); }
  } catch (err) { showMsg('msg', err.message); }
});
$('stationForm').addEventListener('submit', async e => {
  e.preventDefault(); clearMsg('msg');
  const body = { name: $('sname').value, code: $('scode').value, line: $('sline').value, stationOrder: Number($('sorder').value), active: $('sactive').checked };
  try { $('sid').value ? await put('/api/admin/stations/' + $('sid').value, body) : await post('/api/admin/stations', body);
    e.target.reset(); $('sid').value = ''; $('sactive').checked = true; showMsg('msg', 'Station saved.', 'ok'); loadStations(); }
  catch (err) { showMsg('msg', err.message); }
});
