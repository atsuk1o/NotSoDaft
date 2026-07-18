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

function makeIcon(count) {
    const multiple = count > 1;
    return L.divIcon({
        className: '',
        html: `<div style="position:relative; width:36px; height:36px;">
            <div style="
                background: ${multiple ? '#0d6efd' : '#198754'};
                border: 3px solid white;
                border-radius: 50% 50% 50% 0;
                width: 36px; height: 36px;
                transform: rotate(-45deg);
                box-shadow: 0 3px 10px rgba(0,0,0,0.25);
            "></div>
            <div style="
                position: absolute;
                top: 50%; left: 50%;
                transform: translate(-50%, -60%);
                font-size: 14px;
            ">${multiple ? count : '🏠'}</div>
        </div>`,
        iconSize: [36, 36],
        iconAnchor: [18, 36],
        popupAnchor: [0, -36]
    });
}

function groupByEircode(data) {
    const groups = {};
    data.forEach(r => {
        if (!r.lat || !r.lng) return;
        if (!groups[r.eircode]) {
            groups[r.eircode] = {
                eircode: r.eircode,
                address: r.address,
                county: r.county,
                lat: r.lat,
                lng: r.lng,
                reviews: []
            };
        }
        groups[r.eircode].reviews.push(r);
    });
    return Object.values(groups);
}

function renderMarkers(data) {
    markers.forEach(m => map.removeLayer(m));
    markers = [];

    const groups = groupByEircode(data);

    groups.forEach(g => {
        const count = g.reviews.length;
        const marker = L.marker([g.lat, g.lng], { icon: makeIcon(count) });
        marker.bindTooltip(
            `<strong>${g.address}</strong><br>${g.eircode}<br>${count} review${count > 1 ? 's' : ''}`,
            { direction: 'top', offset: [0, -30] }
        );
        marker.on('click', () => showModal(g));
        marker.addTo(map);
        markers.push(marker);
    });

    document.getElementById('resultLabel').textContent =
        `${groups.length} location${groups.length === 1 ? '' : 's'} · ${data.length} review${data.length === 1 ? '' : 's'}`;
}

function renderGrid(data) {
    const grid = document.getElementById('propertyGrid');
    const groups = groupByEircode(data);

    grid.innerHTML = groups.length === 0
        ? '<div class="col-12 text-center text-muted py-5">No approved reviews yet.</div>'
        : groups.map(g => `
        <div class="col-md-4 col-sm-6">
            <div class="card h-100 shadow-sm property-card" onclick='showModal(${JSON.stringify(g).replace(/'/g, "&#39;")})'>
                <div class="card-body">
                    <h6 class="mb-1">${g.address}</h6>
                    <p class="text-muted small mb-1"><code>${g.eircode}</code> · ${g.county}</p>
                    <p class="text-muted small mb-0">${g.reviews.length} review${g.reviews.length > 1 ? 's' : ''}</p>
                </div>
                <div class="card-footer">
                    <span class="small text-muted">
                        Avg overall: <strong>${(g.reviews.reduce((s, r) => s + r.overallScore, 0) / g.reviews.length).toFixed(1)}/5</strong>
                    </span>
                </div>
            </div>
        </div>`).join('');
}

function showModal(g) {
    document.getElementById('modalTitle').textContent = g.address + ' · ' + g.eircode;

    const reviewsHtml = g.reviews.map((r, i) => `
        <div class="border rounded p-3 mb-3">
            <div class="d-flex justify-content-between mb-2">
                <span class="badge bg-success">Review #${i + 1}</span>
                <small class="text-muted">${new Date(r.createdAt).toLocaleDateString()}</small>
            </div>
            ${r.photos && r.photos.length > 0
                ? `<div class="d-flex flex-wrap gap-2 mb-2">
                    ${r.photos.map(p => `
                        <a href="${p.filePath}" target="_blank">
                            <img src="${p.filePath}"
                                 style="height:80px; width:80px; object-fit:cover; border-radius:6px; cursor:pointer;"
                                 onmouseover="this.style.opacity='0.8'"
                                 onmouseout="this.style.opacity='1'"/>
                        </a>`).join('')}
                   </div>`
                : ''}
            <p class="mb-2 small">${r.reviewText}</p>
            <div class="d-flex gap-3 small text-muted flex-wrap">
                <span>💧 ${r.dampnessScore}/5</span>
                <span>🔥 ${r.heatingScore}/5</span>
                <span>🔧 ${r.maintenanceScore}/5</span>
                <span>⭐ ${r.overallScore}/5</span>
            </div>
        </div>`).join('');

    const firstReviewId = g.reviews[0].id;

    document.getElementById('modalBody').innerHTML = `
        <div class="row">
            <div class="col-md-7 border-end" style="max-height:500px; overflow-y:auto;">
                ${reviewsHtml}
            </div>
            <div class="col-md-5">
                <h6 class="fw-bold mb-3">Comments</h6>
                <div id="commentsList" class="mb-3" style="max-height:300px; overflow-y:auto;">
                    <p class="text-muted small">Loading...</p>
                </div>
                <form onsubmit="submitComment(event, ${firstReviewId})">
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
    loadComments(firstReviewId);
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
        })
        .catch(() => {
            document.getElementById('commentsList').innerHTML =
                '<p class="text-muted small">Could not load comments.</p>';
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
    document.getElementById('viewInput').value = view;
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

if(currentView === 'grid'){
    switchView('grid');
}