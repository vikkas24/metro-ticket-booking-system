if (requireLogin()) init();
let lastFare = null, lastTicket = null;
const $ = id => document.getElementById(id);
async function init() {
  const today = new Date(Date.now() - new Date().getTimezoneOffset() * 60000).toISOString().slice(0, 10);
  $('date').min = today; $('date').value = today;
  try {
    const stations = await get('/api/stations');
    const opts = '<option value="">Select station</option>' + stations.map(s => `<option value="${s.id}">${esc(s.name)}</option>`).join('');
    $('from').innerHTML = opts; $('to').innerHTML = opts;
  } catch (e) { showMsg('msg', e.message); }
  ['from', 'to', 'date', 'pax'].forEach(id => $(id).addEventListener('change', () => { lastFare = null; $('bookBtn').disabled = true; }));
}
function readForm() {
  if (!$('from').value) throw new Error('Please select a source station.');
  if (!$('to').value) throw new Error('Please select a destination station.');
  if ($('from').value === $('to').value) throw new Error('Source and destination cannot be the same.');
  if (!$('date').value || $('date').value < $('date').min) throw new Error('Please select a valid journey date.');
  const n = Number($('pax').value);
  if (!Number.isInteger(n) || n < 1 || n > 10) throw new Error('Passengers must be between 1 and 10.');
  return { sourceStationId: Number($('from').value), destinationStationId: Number($('to').value), passengerCount: n, journeyDate: $('date').value };
}
$('calcBtn').onclick = async () => {
  clearMsg('msg');
  try {
    const req = readForm(); lastFare = await post('/api/tickets/calculate-fare', req);
    $('sFrom').textContent = lastFare.source; $('sTo').textContent = lastFare.destination; $('sDate').textContent = req.journeyDate;
    $('sPax').textContent = lastFare.passengerCount; $('sFare').textContent = rupees(lastFare.farePerPassenger); $('sTotal').textContent = rupees(lastFare.totalFare);
    $('bookBtn').disabled = false;
  } catch (e) { showMsg('msg', e.message); }
};
$('bookBtn').onclick = async () => {
  clearMsg('msg'); $('bookBtn').disabled = true;
  try {
    lastTicket = await post('/api/tickets/book', readForm());
    const t = lastTicket;
    $('ticketCard').innerHTML = `<div class="ok">✓ Booking Successful</div><h2 style="margin-top:.5rem">${esc(t.ticketNumber)}</h2>
      <div class="summary"><div><span>From</span><b>${esc(t.source)}</b></div><div><span>To</span><b>${esc(t.destination)}</b></div>
      <div><span>Journey date</span><b>${esc(t.journeyDate)}</b></div><div><span>Passengers</span><b>${t.passengerCount}</b></div>
      <div><span>Fare per passenger</span><b>${rupees(t.farePerPassenger)}</b></div><div><span>Total fare</span><b>${rupees(t.totalFare)}</b></div>
      <div><span>Booked at</span><b>${dt(t.bookingTime)}</b></div><div><span>Status</span><span class="badge ${esc(t.status)}">${esc(t.status)}</span></div></div>`;
    $('bookingView').style.display = 'none'; $('confirmView').style.display = 'block';
  } catch (e) { showMsg('msg', e.message); $('bookBtn').disabled = false; }
};
$('dlBtn').onclick = () => {
  const t = lastTicket, text = `METRO TICKET\nTicket: ${t.ticketNumber}\nFrom: ${t.source}\nTo: ${t.destination}\nDate: ${t.journeyDate}\nPassengers: ${t.passengerCount}\nTotal fare: ${rupees(t.totalFare)}\nStatus: ${t.status}\n`;
  const a = document.createElement('a'); a.href = URL.createObjectURL(new Blob([text], { type: 'text/plain' })); a.download = t.ticketNumber + '.txt'; a.click();
};
$('printBtn').onclick = () => window.print();
