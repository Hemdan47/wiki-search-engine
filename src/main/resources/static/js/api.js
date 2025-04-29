class WikiSearchAPI {
    constructor(baseUrl = '') {
        // Set your API base URL here. If empty, it will use the same domain as the frontend
        this.baseUrl = baseUrl;
        this.endpoint = '/search';
    }

    async search(query, page = 1) {
        if (!query || query.trim() === '') {
            throw new Error('Search query cannot be empty');
        }

        try {
            const response = await fetch(`${this.baseUrl}${this.endpoint}?query=${encodeURIComponent(query)}&page=${page}`);
            
            if (!response.ok) {
                throw new Error(`API error: ${response.status}`);
            }
            
            const data = await response.json();
            return data;
        } catch (error) {
            console.error('Search request failed:', error);
            throw error;
        }
    }
}

// Create and export a single instance of the API handler
const apiService = new WikiSearchAPI();