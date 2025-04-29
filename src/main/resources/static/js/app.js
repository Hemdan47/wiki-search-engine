
class WikiSearchApp {
    constructor(apiService, uiHandler) {
        this.apiService = apiService;
        this.ui = uiHandler;
        this.lastQuery = '';

        // Initialize event listeners
        this.initEventListeners();
    }

    initEventListeners() {
        // Listen for search events
        document.addEventListener('wiki-search', async (event) => {
            const { query, page } = event.detail;
            this.lastQuery = query;
            await this.performSearch(query, page);
        });

        // Listen for page change events
        document.addEventListener('wiki-page-change', async (event) => {
            const { query, page } = event.detail;
            await this.performSearch(query, page);
        });
    }

    async performSearch(query, page = 1) {
        try {
            // Disable search button during search
            this.ui.searchButton.disabled = true;

            // Perform search via API
            const data = await this.apiService.search(query, page);

            // Display results
            this.ui.displayResults(data, query);
        } catch (error) {
            console.error('Search error:', error);
            this.ui.showError();
        } finally {
            // Re-enable search button
            this.ui.searchButton.disabled = false;
        }
    }

    initialize(defaultQuery = '') {
        // Make sure all UI states are hidden initially
        this.ui.hideAllStates();

        // Focus on search input
        this.ui.searchInput.focus();

        // If a default query is provided, perform search
        if (defaultQuery) {
            this.ui.searchInput.value = defaultQuery;
            const searchEvent = new CustomEvent('wiki-search', {
                detail: {
                    query: defaultQuery,
                    page: 1
                }
            });
            document.dispatchEvent(searchEvent);
        }
    }
}

// Initialize the application once DOM is fully loaded
document.addEventListener('DOMContentLoaded', () => {
    const app = new WikiSearchApp(apiService, uiHandler);
    app.initialize();
});