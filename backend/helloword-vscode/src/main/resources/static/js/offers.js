const offerWidth = 250 + 2 * 20 + 2 * 1; // Ancho máximo de cada oferta + márgenes + bordes
let isGridColumns = localStorage.getItem('isGridColumns') ? localStorage.getItem('isGridColumns') === "true" : true;
let selectedPosition;
let offers;

document.addEventListener('DOMContentLoaded', function () {
    offers = document.getElementById("offers");

    loadOffers();
});

function calculateColumns() {
    const containerWidth = offers.offsetWidth;
    const columns = Math.floor(containerWidth / offerWidth);

    return columns;
}

async function checkLoadMore() {
    const response = await fetch(`/allOffersLoaded`);
    const allOffersLoaded = await response.json();

    const loadMore = document.getElementById("loadOffers");

    if (allOffersLoaded.value) loadMore.remove();
}

async function loadOffers() {
    const from = offers.childElementCount;
    const amount = Math.max(2, Math.min(5, calculateColumns()));  // Entre 2 y 5 ofertas

    const response = await fetch(`/loadOffers?from=${from}&amount=${amount}`);
    const newOffers = await response.text();

    if (from == 0) { offers.innerHTML = newOffers }
    else { offers.insertAdjacentHTML("beforeend", newOffers) }

    checkLoadMore();
}