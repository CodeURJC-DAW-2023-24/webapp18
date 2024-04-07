const cardWidth = 250 + 2 * 20; // Maximum width of each offer + margins (edges are included in the a)

document.addEventListener('DOMContentLoaded', function () {
    load(0);
});

function calculateColumns(cards) {
    const containerWidth = cards.offsetWidth;
    const columns = Math.floor(containerWidth / cardWidth);

    return columns;
}

async function checkLoadMore(page) {
    const loadMore = document.getElementById("load");
    const loadMoreButton = document.querySelector("#load button");
    const hasMore = document.getElementById("hasMore");

    loadMoreButton.setAttribute("onclick", "load("+page+")");

    if (hasMore) hasMore.remove();
    else loadMore.remove();
}

function getCardType() {
    if (document.getElementById("offers")) return "offers";
    if (document.getElementById("pools")) return "pools";
    else return null;
}

async function load(page) {
    const cardType = getCardType();
    const cards = document.getElementById(cardType);

    const amount = Math.max(2, Math.min(5, calculateColumns(cards)));  // Between 2 and 5 cards
    const response = await fetch('/'+cardType+'/load?page='+page+'&size='+amount);
    const newCards = await response.text();

    if (page == 0) { cards.innerHTML = newCards }
    else { cards.insertAdjacentHTML("beforeend", newCards) }

    checkLoadMore(page+1);  // It will be better if we use nextPage in server side
}