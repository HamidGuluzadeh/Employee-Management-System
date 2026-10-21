'use strict';
// When served by Spring (http://localhost:8082/employee-management-system/), the same origin is used.
const BASE = location.protocol.startsWith('http') && location.pathname.startsWith('/employee-management-system')
  ? '/employee-management-system/api/v1'
  : 'http://localhost:8082/employee-management-system/api/v1';
const PER = 10;
const S = { view: 'employees', depts: [], emps: [], q: '', dept: '', status: '', page: 0 };
const STATUS = { ACTIVE: ['Active', 'ok'], ON_LEAVE: ['On leave', 'warn'], TERMINATED: ['Terminated', 'off'] };
const $ = s => document.querySelector(s);
const esc = v => String(v ?? '').replace(/[&<>"']/g, c => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' }[c]));
const hue = s => { let h = 0; for (const c of s || '') h = (h * 31 + c.charCodeAt(0)) % 360; return h; };
const deptOf = id => S.depts.find(d => d.id === id);
const fmtDate = d => d ? new Date(/^\d{4}-\d{2}-\d{2}$/.test(d) ? d + 'T00:00' : d).toLocaleDateString('en-US', { day: '2-digit', month: 'short', year: 'numeric' }) : '—';
const fmtNum = n => Number(n ?? 0).toLocaleString('en-US', { minimumFractionDigits: 2, maximumFractionDigits: 2 });

async function api(path, opt = {}) {
  let r;
  try { r = await fetch(BASE + path, { headers: { 'Content-Type': 'application/json' }, ...opt }); }
  catch { throw new Error('Could not connect to the server. Is the backend running and is the address correct?'); }
  const txt = await r.text(); let j = null;
  try { j = txt ? JSON.parse(txt) : null; } catch { /* empty or non-JSON response */ }
  if (!r.ok) throw new Error(j?.message || `Error ${r.status}`);
  return j && 'data' in j ? j.data : j;
}

async function load() {
  S.depts = await api('/departments/all');
  const lists = await Promise.all(S.depts.map(async d => {
    const out = []; let p = 0, total = 1;
    while (p < total) {
      const pg = await api(`/employees/department/${d.id}?page=${p}&size=100`);
      out.push(...(pg.content || []));
      total = pg.totalPages ?? pg.page?.totalPages ?? 1; p++;
    }
    return out.map(e => ({ ...e, departmentId: d.id, departmentCode: d.code }));
  }));
  S.emps = lists.flat();
  $('#banner').innerHTML = S.emps.some(e => !e.id)
    ? '<div class="bar"><b>Backend fix required:</b> the employee <code>id</code> field is empty in the API response (<code>ignore</code> in EmployeeMapper). Apply <code>backend-fix/EmployeeMapper.java</code> to enable editing and status changes.</div>' : '';
}

/* ---------- views ---------- */
function render() {
  document.querySelectorAll('nav button').forEach(b => b.classList.toggle('on', b.dataset.view === S.view));
  $('#app').innerHTML = S.view === 'employees' ? employeesView() : departmentsView();
  if (S.view === 'employees') drawTable();
}

function employeesView() {
  const n = s => S.emps.filter(e => e.status === s).length;
  const opts = Object.entries(STATUS).map(([k, v]) => `<option value="${k}" ${S.status === k ? 'selected' : ''}>${v[0]}</option>`).join('');
  const dopts = S.depts.map(d => `<option value="${d.id}" ${S.dept === d.id ? 'selected' : ''}>${esc(d.name)}</option>`).join('');
  return `<div class="head"><div><h1>Employees</h1><p>Your entire company team in one place</p></div>
    <button class="btn pri" data-a="new-emp">Add employee</button></div>
  <div class="stats"><div><b>${S.emps.length}</b><span>Total employees</span></div><div><b>${n('ACTIVE')}</b><span>Active</span></div>
    <div><b>${n('ON_LEAVE')}</b><span>On leave</span></div><div><b>${S.depts.length}</b><span>Departments</span></div></div>
  <div class="tools"><input id="q" type="search" placeholder="Search by name, email or phone" value="${esc(S.q)}" aria-label="Search">
    <select id="fd" aria-label="Department"><option value="">All departments</option>${dopts}</select>
    <select id="fs" aria-label="Status"><option value="">All statuses</option>${opts}</select></div>
  <div class="card" id="tbl"></div>`;
}

function drawTable() {
  const q = S.q.trim().toLowerCase();
  const list = S.emps.filter(e => (!S.dept || e.departmentId === S.dept) && (!S.status || e.status === S.status)
    && (!q || `${e.firstName} ${e.lastName} ${e.email} ${e.phoneNumber}`.toLowerCase().includes(q)));
  const pages = Math.max(1, Math.ceil(list.length / PER)); S.page = Math.min(S.page, pages - 1);
  const rows = list.slice(S.page * PER, S.page * PER + PER);
  if (!S.depts.length) return $('#tbl').innerHTML = '<div class="state"><b>No departments yet</b>Create a department first before adding employees.<br><button class="btn pri" data-a="new-dep">Create department</button></div>';
  if (!list.length) return $('#tbl').innerHTML = `<div class="state"><b>${S.emps.length ? 'No results found' : 'No employees yet'}</b>${S.emps.length ? 'Try changing your search or filter.' : 'Add your first employee.'}</div>`;
  const tr = e => {
    const st = STATUS[e.status] || [e.status, 'off'], d = deptOf(e.departmentId);
    return `<tr><td><div class="who"><span class="av" style="--h:${hue(e.departmentCode)}">${esc((e.firstName[0] || '') + (e.lastName[0] || '')).toUpperCase()}</span>
      <div><b>${esc(e.firstName)} ${esc(e.lastName)}</b><small>${esc(e.email)}</small></div></div></td>
      <td><span class="tag" style="--h:${hue(e.departmentCode)}">${esc(e.departmentCode)}</span>${esc(d?.name)}</td>
      <td>${esc(e.phoneNumber)}</td><td>${fmtDate(e.hireDate)}</td><td class="num">${fmtNum(e.salary)}</td>
      <td><select class="pill ${st[1]}" data-st="${e.id}" aria-label="Status" ${e.status === 'TERMINATED' || !e.id ? 'disabled' : ''}>
        ${Object.entries(STATUS).map(([k, v]) => `<option value="${k}" ${k === e.status ? 'selected' : ''}>${v[0]}</option>`).join('')}</select></td>
      <td class="act"><button class="btn sm" data-a="edit-emp" data-id="${e.id}" ${e.id ? '' : 'disabled'}>Edit</button></td></tr>`;
  };
  $('#tbl').innerHTML = `<div class="scroll"><table><thead><tr><th>Employee</th><th>Department</th><th>Phone</th><th>Hired</th><th class="num">Salary</th><th>Status</th><th></th></tr></thead>
    <tbody>${rows.map(tr).join('')}</tbody></table></div>
    <div class="pager"><span>${S.page * PER + 1}–${S.page * PER + rows.length} / ${list.length}</span>
    <div><button class="btn sm" data-a="prev" ${S.page ? '' : 'disabled'}>Previous</button><button class="btn sm" data-a="next" ${S.page < pages - 1 ? '' : 'disabled'}>Next</button></div></div>`;
}

function departmentsView() {
  const body = S.depts.length ? `<div class="scroll"><table><thead><tr><th>Department</th><th>Code</th><th class="num">Employees</th><th>Created</th><th></th></tr></thead><tbody>
    ${S.depts.map(d => `<tr><td><b>${esc(d.name)}</b></td><td><span class="tag" style="--h:${hue(d.code)}">${esc(d.code)}</span></td>
      <td class="num">${S.emps.filter(e => e.departmentId === d.id).length}</td><td>${fmtDate(d.createdAt)}</td>
      <td class="act"><button class="btn sm" data-a="edit-dep" data-id="${d.id}">Edit</button> <button class="btn sm" data-a="del-dep" data-id="${d.id}">Delete</button></td></tr>`).join('')}
    </tbody></table></div>` : '<div class="state"><b>No departments yet</b>Create your first department.</div>';
  return `<div class="head"><div><h1>Departments</h1><p>The structural units of your team</p></div><button class="btn pri" data-a="new-dep">Create department</button></div><div class="card">${body}</div>`;
}

/* ---------- dialogs and notifications ---------- */
const dlg = $('#dlg');
function modal(html, onSubmit) {
  dlg.innerHTML = `<form id="f">${html}</form>`;
  dlg.showModal();
  $('#f').onsubmit = async ev => {
    ev.preventDefault(); const b = ev.submitter; b.disabled = true;
    try { await onSubmit(Object.fromEntries(new FormData(ev.target))); dlg.close(); }
    catch (x) { $('#ferr').textContent = x.message; b.disabled = false; }
  };
}
dlg.addEventListener('click', e => { if (e.target === dlg || e.target.closest('[data-close]')) dlg.close(); });
const shell = (title, body, label, cls = 'pri') => `<header><h2>${title}</h2><button type="button" class="x" data-close aria-label="Close">✕</button></header>
  <div class="body">${body}<p class="err" id="ferr" role="alert"></p></div>
  <footer><button type="button" class="btn" data-close>Cancel</button><button class="btn ${cls}">${label}</button></footer>`;
function toast(msg, type = '') {
  const t = document.createElement('div'); t.className = 'toast ' + type; t.textContent = msg;
  $('#toasts').append(t); setTimeout(() => t.remove(), 3500);
}
async function refresh() { await load(); render(); }

/* ---------- forms ---------- */
function empForm(e) {
  if (!S.depts.length) return toast('Create a department first', 'err');
  const v = e || {};
  const dep = S.depts.map(d => `<option value="${d.id}" ${d.id === v.departmentId ? 'selected' : ''}>${esc(d.name)}</option>`).join('');
  const create = e ? '' : `<label>Hire date<input type="date" name="hireDate" required></label>
    <label>Status<select name="status">${Object.entries(STATUS).map(([k, s]) => `<option value="${k}">${s[0]}</option>`).join('')}</select></label>`;
  modal(shell(e ? 'Edit employee' : 'New employee', `<div class="grid">
    <label>First name<input name="firstName" required maxlength="10" value="${esc(v.firstName)}"></label>
    <label>Last name<input name="lastName" required maxlength="10" value="${esc(v.lastName)}"></label>
    <label class="full">Email<input type="email" name="email" required maxlength="50" value="${esc(v.email)}"></label>
    <label>Phone<input name="phoneNumber" required maxlength="20" value="${esc(v.phoneNumber)}"></label>
    <label>Salary<input type="number" name="salary" required min="0" max="9999.99" step="0.01" value="${esc(v.salary)}"></label>
    <label class="full">Department<select name="departmentId" required>${dep}</select></label>${create}</div>`, 'Save'),
  async d => {
    const body = { ...d, salary: Number(d.salary) };
    await api(e ? '/employees/' + e.id : '/employees/new', { method: e ? 'PUT' : 'POST', body: JSON.stringify(body) });
    toast(e ? 'Changes saved' : 'Employee added'); await refresh();
  });
}

function depForm(d) {
  modal(shell(d ? 'Edit department' : 'New department', `<div class="grid">
    <label class="full">Name<input name="name" required maxlength="40" value="${esc(d?.name)}"></label>
    <label class="full">Code<input name="code" required maxlength="10" value="${esc(d?.code)}"></label></div>`, 'Save'),
  async v => {
    await api(d ? '/departments/' + d.id : '/departments/new', { method: d ? 'PUT' : 'POST', body: JSON.stringify(v) });
    toast(d ? 'Changes saved' : 'Department created'); await refresh();
  });
}

function delDep(d) {
  modal(shell('Delete department?', `<p>The <b>${esc(d.name)}</b> department will be permanently deleted. A department with employees cannot be deleted.</p>`, 'Delete', 'danger'),
    async () => { await api('/departments/' + d.id, { method: 'DELETE' }); toast('Department deleted'); await refresh(); });
}

function changeStatus(sel) {
  const e = S.emps.find(x => x.id === sel.dataset.st), status = sel.value;
  const run = async () => { await api(`/employees/${e.id}?status=${status}`, { method: 'PATCH' }); toast('Status updated'); await refresh(); };
  if (status === 'TERMINATED') {
    modal(shell('Terminate employee?', `<p><b>${esc(e.firstName)} ${esc(e.lastName)}</b> will be marked as terminated. This status cannot be changed later.</p>`, 'Terminate', 'danger'), run);
    dlg.addEventListener('close', () => render(), { once: true });
  } else run().catch(x => { toast(x.message, 'err'); render(); });
}

/* ---------- events ---------- */
document.addEventListener('click', e => {
  const nav = e.target.closest('nav [data-view]');
  if (nav) { S.view = nav.dataset.view; return render(); }
  const t = e.target.closest('[data-a]'); if (!t) return;
  const id = t.dataset.id;
  ({ 'new-emp': () => empForm(), 'edit-emp': () => empForm(S.emps.find(x => x.id === id)),
     'new-dep': () => depForm(), 'edit-dep': () => depForm(deptOf(id)), 'del-dep': () => delDep(deptOf(id)),
     prev: () => { S.page--; drawTable(); }, next: () => { S.page++; drawTable(); }, retry: boot })[t.dataset.a]?.();
});
document.addEventListener('input', e => { if (e.target.id === 'q') { S.q = e.target.value; S.page = 0; drawTable(); } });
document.addEventListener('change', e => {
  if (e.target.id === 'fd') { S.dept = e.target.value; S.page = 0; drawTable(); }
  else if (e.target.id === 'fs') { S.status = e.target.value; S.page = 0; drawTable(); }
  else if (e.target.dataset.st) changeStatus(e.target);
});

async function boot() {
  try { await load(); render(); }
  catch (x) { $('#app').innerHTML = `<div class="state"><b>Failed to load data</b>${esc(x.message)}<br><button class="btn" data-a="retry">Try again</button></div>`; }
}
boot();
