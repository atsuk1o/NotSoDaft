const map = L.map('map',{
    minZoom: 6,
    maxZoom: 18,
    maxBounds: [ [51.3, -10.7], [55.4, -5.5] ],
    maxBoundsViscosity: 1.0
}).setView([53.1424, -7.6921], 7);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', { attribution: '© OpenStreetMap contributors' }).addTo(map);

let markers = [];

function makeIcon(verified){
    return L.divIcon({
        className: '',
        html: `<div style="
            background: ${verified ? '#198754' : '#6c757d'};
            color: white; border-radius: 50% 50% 50% 0;
            width: 32px; height: 32px;
            display: flex; align-items: center; justify-content: center;
            font-size: 14px; transform: rotate(-45deg);
            border: 2px solid white; box-shadow: 0 2px 6px rgba(0,0,0,0.3);">
            <span style="transform:rotate(45deg)">🏠</span>
        </div>`,
        iconSize: [32, 32], iconAnchor: [16, 32], popupAnchor: [0, -32]
    });
}

function renderMarkers(data){
    markers.forEach(m => map.removeLayer(m));
    markers = [];
    data.forEach(p => {
        if (!p.lat || !p.lng) return;
        const marker = L.marker([p.lat, p.lng], { icon: makeIcon(p.verified) });
        marker.bindTooltip(`<strong>${p.title}</strong><br>€${p.pricePerMonth}/mo`, { direction: 'top', offset: [0, -30] });
        marker.on('click', () => showModal(p));
        marker.addTo(map);
        markers.push(marker);
    });
    document.getElementById('resultLabel').textContent = `${data.length} propert${data.length === 1 ? 'y' : 'ies'}`;
}

function renderGrid(data){
    const grid = document.getElementById('propertyGrid');
    grid.innerHTML = data.length === 0
        ? '<div class="col-12 text-center text-muted py-5">No properties yet.</div>'
        : data.map(p => `
        <div class="col-md-4 col-sm-6">
            <div class="card h-100 shadow-sm property-card" onclick="showModal(${JSON.stringify(p).replace(/"/g, '&quot;')})">
                <div class="card-body">
                    <div class="d-flex justify-content-between mb-2">
                        <h6 class="mb-0">${p.title}</h6>
                        ${p.verified ? '<span class="badge bg-success">Verified</span>' : '<span class="badge bg-secondary">Unverified</span>'}
                    </div>
                    <p class="text-muted small mb-1"><i class="bi bi-geo-alt"></i> ${p.address || ''}, ${p.county || ''}</p>
                    <p class="text-muted small mb-0"><i class="bi bi-door-open"></i> ${p.bedrooms} bed · <code>${p.eircode}</code></p>
                </div>
                <div class="card-footer d-flex justify-content-between">
                    <span class="fw-bold text-success">€${p.pricePerMonth}<span class="text-muted fw-normal small">/mo</span></span>
                </div>
            </div>
        </div>`).join('');
}

function showModal(p) {
    document.getElementById('modalTitle').textContent = p.title;
    document.getElementById('modalBody').innerHTML = `
        <table class="table table-sm">
            <tr><td class="text-muted">Address</td><td>${p.address || '-'}</td></tr>
            <tr><td class="text-muted">County</td><td>${p.county || '-'}</td></tr>
            <tr><td class="text-muted">Eircode</td><td><code>${p.eircode}</code></td></tr>
            <tr><td class="text-muted">Bedrooms</td><td>${p.bedrooms}</td></tr>
            <tr><td class="text-muted">Price</td><td><strong class="text-success">€${p.pricePerMonth}/mo</strong></td></tr>
            <tr><td class="text-muted">Status</td><td>${p.verified ? '<span class="badge bg-success">✅ Verified</span>' : '<span class="badge bg-secondary">Unverified</span>'}</td></tr>
        </table>`;
    new bootstrap.Modal(document.getElementById('propertyModal')).show();
}

function switchView(view) {
    if (view === 'map') {
        document.getElementById('mapView').style.display = 'block';
        document.getElementById('gridView').style.display = 'none';
        document.getElementById('btnMap').classList.add('active');
        document.getElementById('btnGrid').classList.remove('active');
        setTimeout(() => map.invalidateSize(), 100);
    } else {
        document.getElementById('mapView').style.display = 'none';
        document.getElementById('gridView').style.display = 'block';
        document.getElementById('btnMap').classList.remove('active');
        document.getElementById('btnGrid').classList.add('active');
        renderGrid(parsedProperties);
    }
}

renderMarkers(parsedProperties);
renderGrid(parsedProperties);