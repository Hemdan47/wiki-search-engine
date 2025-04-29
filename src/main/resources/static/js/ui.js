
class WikiSearchUI {
    constructor() {
        // DOM elements
        this.searchForm = document.getElementById('search-form');
        this.searchInput = document.getElementById('search-input');
        this.searchButton = document.getElementById('search-button');
        this.resultsContainer = document.getElementById('results-container');
        this.resultsList = document.getElementById('results-list');
        this.errorMessage = document.getElementById('error-message');
        this.noResults = document.getElementById('no-results');
        this.prevPageButton = document.getElementById('prev-page');
        this.nextPageButton = document.getElementById('next-page');
        this.currentPageSpan = document.getElementById('current-page');
        this.totalPagesSpan = document.getElementById('total-pages');

        // Initialize event listeners
        this.initEventListeners();
    }


    initEventListeners() {
        // Ensure all states are hidden on page load
        this.hideAllStates();

        // Search form submission
        this.searchForm.addEventListener('submit', (event) => {
            event.preventDefault();
            const searchEvent = new CustomEvent('wiki-search', {
                detail: {
                    query: this.searchInput.value.trim(),
                    page: 1
                }
            });
            document.dispatchEvent(searchEvent);
        });

        // Pagination event listeners
        this.prevPageButton.addEventListener('click', () => {
            const currentPage = parseInt(this.currentPageSpan.textContent);
            if (currentPage > 1) {
                const pageChangeEvent = new CustomEvent('wiki-page-change', {
                    detail: {
                        query: this.currentQuery,
                        page: currentPage - 1
                    }
                });
                document.dispatchEvent(pageChangeEvent);
            }
        });

        this.nextPageButton.addEventListener('click', () => {
            const currentPage = parseInt(this.currentPageSpan.textContent);
            const totalPages = parseInt(this.totalPagesSpan.textContent);
            if (currentPage < totalPages) {
                const pageChangeEvent = new CustomEvent('wiki-page-change', {
                    detail: {
                        query: this.currentQuery,
                        page: currentPage + 1
                    }
                });
                document.dispatchEvent(pageChangeEvent);
            }
        });
    }


    showError() {
        this.hideAllStates();
        this.errorMessage.classList.remove('hidden');
    }


    showNoResults() {
        this.hideAllStates();
        this.noResults.classList.remove('hidden');
    }


    hideAllStates() {
        this.errorMessage.classList.add('hidden');
        this.noResults.classList.add('hidden');
        this.resultsContainer.classList.add('hidden');
    }

    displayResults(data, query) {
        this.hideAllStates();

        // Store current query for pagination
        this.currentQuery = query;

        // Check if there are no results
        if (data.result.length === 0) {
            this.showNoResults();
            return;
        }

        // Clear previous results
        this.resultsList.innerHTML = '';

        // Add each result to the list
        data.result.forEach(item => {
            const resultItem = document.createElement('div');
            resultItem.className = 'result-item';

            const urlParts = item.url.split('/');
            const title = urlParts[urlParts.length - 1].replace(/_/g, ' ');

            resultItem.innerHTML = `
                <a href="${item.url}" target="_blank" rel="noopener">${title}</a>
                <div class="result-url">${item.url}</div>
                <div class="result-score">Relevance score: ${(item.score * 100).toFixed(2)}%</div>
            `;

            this.resultsList.appendChild(resultItem);
        });

        // Update pagination
        this.updatePagination(data.currentPage, data.totalPages);

        // Show results container
        this.resultsContainer.classList.remove('hidden');
    }

    updatePagination(currentPage, totalPages) {
        this.currentPageSpan.textContent = currentPage;
        this.totalPagesSpan.textContent = totalPages;

        // Update button states
        this.prevPageButton.disabled = currentPage <= 1;
        this.nextPageButton.disabled = currentPage >= totalPages;
    }
}

// Create UI instance
const uiHandler = new WikiSearchUI();