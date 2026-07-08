const map = L.map('map', {
    minZoom: 6,
    maxZoom: 18,
    maxBounds: [[51.3, -10.7], [55.4, -5.5]],
    maxBoundsViscosity: 1.0
}).setView([53.1424, -7.6921], 7);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors'
}).addTo(map);

let markers = [];

function makeIcon() {
    return L.divIcon({
        className: '',
        html: `<div style="
            background: #198754;
            color: white; border-radius: 50% 50% 50% 0;
            width: 32px; height: 32px;
            display: flex; align-items: center; justify-content: center;
            font-size: 14px; transform: rotate(-45deg);
            border: 2px solid white; box-shadow: 0 2px 6px rgba(0,0,0,0.3);">
            <span style="transform:rotate(45deg)">📍</span>
        </div>`,
        iconSize: [32, 32], iconAnchor: [16, 32], popupAnchor: [0, -32]
    });
}

function renderMarkers(data) {
    markers.forEach(m => map.removeLayer(m));
    markers = [];
    data.forEach(r => {
        const marker = L.marker([53.1424, -7.6921], { icon: makeIcon() });
        marker.bindTooltip(`<strong>${r.address}</strong><br>${r.eircode}`, {
            direction: 'top', offset: [0, -30]
        });
        marker.on('click', () => showModal(r));
        marker.addTo(map);
        markers.push(marker);
    });
    document.getElementById('resultLabel').textContent =
        `${data.length} review${data.length === 1 ? '' : 's'}`;
}

function renderGrid(data) {
    const grid = document.getElementById('propertyGrid');
    grid.innerHTML = data.length === 0
        ? '<div class="col-12 text-center text-muted py-5">No approved reviews yet.</div>'
        : data.map(r => `
        <div class="col-md-4 col-sm-6">
            <div class="card h-100 shadow-sm property-card" onclick='showModal(${JSON.stringify(r).replace(/'/g, "&#39;")})'>
                <div class="card-body">
                    <h6 class="mb-1">${r.address}</h6>
                    <p class="text-muted small mb-1"><code>${r.eircode}</code> · ${r.county}</p>
                    <div class="d-flex gap-2 small text-muted">
                        <span> ${r.dampnessScore}/5</span>
                        <span> ${r.heatingScore}/5</span>
                        <span> ${r.maintenanceScore}/5</span>
                        <span> ${r.overallScore}/5</span>
                    </div>
                </div>
            </div>
        </div>`).join('');
}

function showModal(r) {
    document.getElementById('modalTitle').textContent = r.address + ' · ' + r.eircode;

    const photos = r.photos && r.photos.length > 0
        ? r.photos.map(p => `<img src="${p.filePath}" class="img-fluid rounded mb-2" style="max-height:200px; object-fit:cover; width:100%"/>`).join('')
        : '<p class="text-muted small">No photos submitted.</p>';

    document.getElementById('modalBody').innerHTML = `
        <div class="row">
            <div class="col-md-6 border-end">
                ${photos}
                <hr/>
                <p>${r.reviewText}</p>
                <div class="d-flex gap-3 small text-muted flex-wrap">
                    <span> Dampness: <strong>${r.dampnessScore}/5</strong></span>
                    <span> Heating: <strong>${r.heatingScore}/5</strong></span>
                    <span> Maintenance: <strong>${r.maintenanceScore}/5</strong></span>
                    <span> Overall: <strong>${r.overallScore}/5</strong></span>
                </div>
            </div>
            <div class="col-md-6">
                <h6 class="fw-bold mb-3">Comments</h6>
                <div id="commentsList" class="mb-3">
                    <p class="text-muted small">No comments yet.</p>
                </div>
                <form onsubmit="submitComment(event, ${r.id})">
                    <div class="mb-2">
                        <input type="text" id="guestName" class="form-control form-control-sm"
                               placeholder="Your name (optional)"/>
                    </div>
                    <div class="mb-2">
                        <textarea id="commentText" class="form-control form-control-sm" rows="3"
                                  placeholder="Leave a comment..." required></textarea>
                    </div>
                    <button type="submit" class="btn btn-success btn-sm w-100">Post Comment</button>
                </form>
            </div>
        </div>`;

    new bootstrap.Modal(document.getElementById('propertyModal')).show();
    loadComments(r.id);
}

function loadComments(reviewId) {
    fetch(`/reviews/${reviewId}/comments`)
        .then(res => res.json())
        .then(comments => {
            const el = document.getElementById('commentsList');
            if (comments.length === 0) {
                el.innerHTML = '<p class="text-muted small">No comments yet.</p>';
                return;
            }
            el.innerHTML = comments.map(c => `
                <div class="border-bottom pb-2 mb-2">
                    <strong class="small">${c.guestName || 'Anonymous'}</strong>
                    <p class="mb-0 small">${c.text}</p>
                </div>`).join('');
        });
}

function submitComment(e, reviewId) {
    e.preventDefault();
    const name = document.getElementById('guestName').value;
    const text = document.getElementById('commentText').value;

    fetch(`/reviews/${reviewId}/comments`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ guestName: name, text: text })
    }).then(() => {
        document.getElementById('commentText').value = '';
        loadComments(reviewId);
    });
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
        renderGrid(parsedReviews);
    }
}

renderMarkers(parsedReviews);
renderGrid(parsedReviews);