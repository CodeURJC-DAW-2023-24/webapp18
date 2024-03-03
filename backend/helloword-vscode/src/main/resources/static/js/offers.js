const offerWidth = 250 + 2 * 20; // Maximum width of each offer + margins (edges are included in the a)
let isGridColumns = localStorage.getItem('isGridColumns') ? localStorage.getItem('isGridColumns') === "true" : true;
let selectedPosition;
let offers;

document.addEventListener('DOMContentLoaded', function () {
    offers = document.getElementById("offers");

    loadOffers(0);
});

function calculateColumns() {
    const containerWidth = offers.offsetWidth;
    const columns = Math.floor(containerWidth / offerWidth);

    return columns;
}

async function checkLoadMore(page) {
    const loadMore = document.getElementById("loadOffers");
    const loadMoreButton = document.querySelector("#loadOffers button");
    const hasMore = document.getElementById("hasMore");

    loadMoreButton.setAttribute("onclick", "loadOffers("+page+")");

    if (hasMore) hasMore.remove();
    else loadMore.remove();
}

async function loadOffers(page) {
    const amount = Math.max(2, Math.min(5, calculateColumns()));  // Between 2 and 5 offers
    const response = await fetch('/offers/load?page='+page+'&size='+amount);
    const newOffers = await response.text();

    if (page == 0) { offers.innerHTML = newOffers }
    else { offers.insertAdjacentHTML("beforeend", newOffers) }

    checkLoadMore(page+1);  // It will be better if we use nextPage in server side
}