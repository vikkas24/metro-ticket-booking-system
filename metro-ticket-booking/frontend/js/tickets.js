if (requireLogin()) load();
const $ = id => document.getElementById(id);
async function load() {
  $('welcome').textContent = 'Welcome, ' + session().name;
  try {
    const list = await get('/api/tickets/my-tickets');
    const live = list.filter(t => t.status !== 'CANCELLED');
    $('sTotal').textContent = list.length; $('sActive').textContent = list.filter(t => t.status === 'BOOKED').length;
    $('sCancelled').textContent = list.length - live.length; $('sSpent').textContent = rupees(live.reduce((s, t) => s + Number(t.totalFare), 0));
    $('list').innerHTML = list.length ? list.map(card).join('') : '<div class="card">No bookings yet. <a href="booking.html">Book your first ticket</a>.</div>';
  } catch (e) { showMsg('msg', e.message); }
}
const card = t => `<div class="card ticket ${esc(t.status)}"><div class="top"><span class="no">${esc(t.ticketNumber)}</span><span class="badge ${esc(t.status)}">${esc(t.status)}</span></div>
  <div class="route">${esc(t.source)} → ${esc(t.destination)}</div><div>${esc(t.journeyDate)} · ${t.passengerCount} passenger(s) · <b>${rupees(t.totalFare)}</b></div>
  <div class="actions"><button class="btn dark sm" data-view="${esc(t.ticketNumber)}">View details</button>
  ${t.status === 'BOOKED' ? `<button class="btn danger sm" data-cancel="${esc(t.ticketNumber)}">Cancel ticket</button>` : ''}</div></div>`;
$('list').addEventListener('click', async e => {
  const v = e.target.dataset.view, c = e.target.dataset.cancel; clearMsg('msg');
  try {
    if (v) { const t = await get('/api/tickets/' + encodeURIComponent(v));
      $('detail').innerHTML = `<h3>${esc(t.ticketNumber)}</h3><div class="summary"><div><span>From</span><b>${esc(t.source)}</b></div><div><span>To</span><b>${esc(t.destination)}</b></div>
      <div><span>Journey date</span><b>${esc(t.journeyDate)}</b></div><div><span>Passengers</span><b>${t.passengerCount}</b></div><div><span>Fare each</span><b>${rupees(t.farePerPassenger)}</b></div>
      <div><span>Total</span><b>${rupees(t.totalFare)}</b></div><div><span>Booked</span><b>${dt(t.bookingTime)}</b></div><div><span>Status</span><span class="badge ${esc(t.status)}">${esc(t.status)}</span></div></div>`;
      $('dlg').showModal(); }
    if (c && confirm('Cancel ticket ' + c + '?')) { await put('/api/tickets/' + encodeURIComponent(c) + '/cancel'); showMsg('msg', 'Ticket cancelled.', 'ok'); load(); }
  } catch (err) { showMsg('msg', 'Ticket cancellation failed: ' + err.message); }
});
